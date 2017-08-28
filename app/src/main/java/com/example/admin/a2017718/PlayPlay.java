package com.example.admin.a2017718;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by admin on 2017/7/24.
 */

public class PlayPlay extends AppCompatActivity {

    public static List<parts> parts = new ArrayList<>();
    public static List<String> infometion = new ArrayList<>();
    public static List<String> lists = new ArrayList<>();
    public static List<String> qq = new ArrayList<>();
    public static List<String> youku = new ArrayList<>();
    public static List<String> sohu = new ArrayList<>();
    public static List<String> imgo = new ArrayList<>();
    public static List<CharSequence> list = new ArrayList<>();
    RecyclerView recyclerView;
    private static final String TAG = "ijkvideo";
    private ImageButton fullButton;
    private TextView mTitleText;
    public static com.example.admin.ijkplayer.IjkVideoView mVideoView = null;
    private int mWidth, mHeight, mLeft, mTop;
    private ImageButton playButton;
    private SeekBar seekBar;
    private LinearLayout mPlayTop, mPlayController;
    RelativeLayout relativeLayout;
    public GestureDetector mGestureDetector;
    TextView playtime;
    private ListView listView;
    public static String type;
    private boolean isFull = false;
    //public static boolean vip=false;
    public static String account=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contantlist);

        Intent intent = getIntent();
        initView();
        createPlayer();
        type = intent.getStringExtra("type");
        Log.e("play_type", type);
        if (type.equals("offline")) {
            new offlinemovie().execute(intent.getStringExtra("id"));
        } else {
            new onlinemovie().execute(intent.getStringExtra("url"));
        }

    }


    private void initView() {

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mLeft = 0;
        mTop = 0;
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = (int) (mWidth * 0.5);

    }


    public void createPlayer() {
        seekBar = (SeekBar) findViewById(R.id.play_seekbar);
        relativeLayout = (RelativeLayout) findViewById(R.id.combineCtrl);
        mVideoView = (com.example.admin.ijkplayer.IjkVideoView) findViewById(R.id.video_view);
        mPlayTop = (LinearLayout) findViewById(R.id.play_top);
        mPlayController = (LinearLayout) findViewById(R.id.play_controller);
        mTitleText = (TextView) findViewById(R.id.play_title);
        playtime = (TextView) findViewById(R.id.playtime);
        listView = (ListView) findViewById(R.id.playlistview);


        LinearLayout.LayoutParams rllp = new LinearLayout.LayoutParams(mWidth, mHeight);
        rllp.leftMargin = mLeft;
        rllp.topMargin = mTop;
        relativeLayout.setLayoutParams(rllp);

        hidden();
        toggleAspectRatio(1);
        mGestureDetector = new GestureDetector(PlayPlay.this, new myGestureListener());


        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });


        playButton = (ImageButton) findViewById(R.id.play_play);
        playButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (mVideoView != null) {
                    pause();
                    show();
                }
            }
        });
        fullButton = (ImageButton) findViewById(R.id.play_full);
        fullButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (mVideoView != null) {
                    fullScreen();
                }
            }
        });
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isClick = false;
                int value = (int) Math.floor((double) allTime * ((double) seekBar.getProgress() / 100));
                mVideoView.seekTo(value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isClick = true;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }
        });
        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.e("INFO", i + "");
                if (i == IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START) {
                    allTime = mVideoView.getDuration();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            Message message = Message.obtain();
                            message.arg1 = IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START;
                            mHandler.sendMessage(message);//发送消息
                        }
                    }, 1000, 1000);
                }
                return false;
            }
        });
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    playButton.setImageDrawable(mVideoView.getResources().getDrawable(R.drawable.vip, null));
                }
            }
        });
    }


    public void setTitle(String str) {
        mTitleText.setText(str);
    }

    public void fullScreen() {
        show();
        if (isFull) {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            move(mLeft, mTop, mWidth, mHeight);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fullButton.setImageDrawable(mVideoView.getResources().getDrawable(R.drawable.vip, null));
            }
            isFull = false;
        } else {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
//            ViewGroup.LayoutParams rllp = mViewHolder.getLayoutParams();
//            rllp.height = -1;
//            rllp.width = -1;
//            mViewHolder.setLayoutParams(rllp);
            move(0, 0, -1, -1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fullButton.setImageDrawable(mVideoView.getResources().getDrawable(R.drawable.vip, null));
            }
            isFull = true;
        }
    }

    public void move(int left, int top, int width, int height) {
        Log.e("MOVE", "1");
        ViewGroup.LayoutParams rllp = relativeLayout.getLayoutParams();
        rllp.width = width;
        rllp.height = height;
        relativeLayout.setLayoutParams(rllp);
    }

    public void pause() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playButton.setImageDrawable(mVideoView.getResources().getDrawable(R.drawable.btn_play, null));
            }
        } else {
            mVideoView.start();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playButton.setImageDrawable(mVideoView.getResources().getDrawable(R.drawable.btn_pause, null));
            }
        }
    }

    private int allTime;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1) {
                case IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START: {
                    int nowTime = mVideoView.getCurrentPosition();
                    playtime.setText(timeLengthToTime(nowTime) + "/" + timeLengthToTime(allTime));
                    if (!isClick) {
                        int value = (int) Math.floor(((double) nowTime / (double) allTime) * 100);
                        seekBar.setProgress(value);
                    }
                    break;
                }


            }

        }
    };

    private String timeLengthToTime(int length) {
        String retStr = new String();
        length /= 1000;
        int tmp = (int) Math.floor(length / 3600);
        length %= 3600;
        if (tmp > 0) {
            retStr = int2str(tmp) + ":";
        }
        tmp = (int) Math.floor(length / 60);
        length %= 60;
        retStr += int2str(tmp) + ":" + int2str(length);
        return retStr;
    }

    private String int2str(int value) {
        String ret = String.valueOf(value);
        while (ret.length() < 2) {
            ret = "0" + ret;
        }
        return ret;
    }

    class myGestureListener extends GestureDetector.SimpleOnGestureListener {
        public myGestureListener() {
            super();
            Log.e("mygestu", "1");
        }

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,      //滑动事件
                               float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {//双击
            pause();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {//单纯点击
            if (isShowing()) {
                hidden();
            } else {
                show();
            }
            return true;
        }
    }

    public boolean isShowing() {
        if (delay > 0) {
            return true;
        }
        return false;
    }

    public void setVideoUrl(String url) {
        mVideoView.setVideoPath(url);
    }

    public void start() {
        mVideoView.start();
    }

    public int toggleAspectRatio(int aspect_ratio) {
        return mVideoView.toggleAspectRatio(aspect_ratio);
    }

    public void hidden() {
        delay = 0;
        mPlayTop.setVisibility(View.INVISIBLE);
        mPlayController.setVisibility(View.INVISIBLE);
    }

    private int delay = 0;
    private boolean isClick = false;

    public void show() {
        Log.e("show", "1");
        show(5000);
    }

    public void show(int time) {
        mPlayTop.setVisibility(View.VISIBLE);
        mPlayController.setVisibility(View.VISIBLE);
        if (delay >= 0) {
            if (delay == 0) {
                delay = time;
                sleepHide();
            } else {
                delay = time;
            }
        }
    }

    private void sleepHide() {
        Log.e("slepp", "1");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (delay > 0) {
                    if (isClick) {
                        delay = 5000;
                    } else {
                        delay -= 1000;
                    }
                    sleepHide();
                } else {
                    hidden();
                }
            }
        }, 1000);
    }


    class onlinemovie extends AsyncTask<String, Void, Void> {
        JSONObject jsonObject;
        JSONObject rows = null;

        @Override
        protected Void doInBackground(String... params) {


            String str = new gethttpcontent().return_contant("http://video.visha.cc/search/volume?url=" + params[0]);

            try {
                jsonObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                infometion.add(jsonObject.getString("title"));
                infometion.add(jsonObject.getString("year"));
                infometion.add(jsonObject.getString("place"));
                infometion.add(jsonObject.getString("actor"));
                infometion.add(jsonObject.getString("introduction"));
                infometion.add(jsonObject.getString("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                rows = jsonObject.getJSONObject("rows");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (rows == null) {
                return null;
            }

            list.clear();

            if (rows.has("qq")) {
                list.add("腾讯");

                try {
                    if (type.equals("teleplay")) {
                        JSONArray jsonArray = rows.getJSONArray("qq");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            qq.add(jsonObject.getString("url"));
                        }
                    }

                    if (type.equals("movie")) {

                        JSONObject jsonObject = rows.getJSONObject("qq");
                        qq.add(jsonObject.getString("url"));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            if (rows.has("youku")) {

                list.add("优酷");

                try {

                    if (type.equals("teleplay")) {
                        JSONArray jsonArray = rows.getJSONArray("youku");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            youku.add(jsonObject.getString("url"));

                        }

                    }


                    if (type.equals("movie")) {

                        JSONObject jsonObject = rows.getJSONObject("youku");
                        youku.add(jsonObject.getString("url"));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (rows.has("sohu")) {
                list.add("搜狐");
                try {


                    if (type.equals("teleplay")) {
                        JSONArray jsonArray = rows.getJSONArray("sohu");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            sohu.add(jsonObject.getString("url"));

                        }

                    }

                    if (type.equals("movie")) {

                        JSONObject jsonObject = rows.getJSONObject("sohu");
                        sohu.add(jsonObject.getString("url"));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            if (rows.has("imgo")) {
                list.add("芒果");


                try {

                    if (type.equals("teleplay")) {
                        JSONArray jsonArray = rows.getJSONArray("imgo");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            imgo.add(jsonObject.getString("url"));
                        }
                    }

                    if (type.equals("movie")) {

                        JSONObject jsonObject = rows.getJSONObject("imgo");
                        imgo.add(jsonObject.getString("url"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            for (int i = 0; i < list.size(); i++) {
                Log.e("qq", (String) list.get(i));


            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listView.setAdapter(new plaplay_online_adapter(PlayPlay.this));
        }
    }

    class offlinemovie extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {

            String string = new gethttpcontent().return_contant("http://s.icodef.com/user/movie/volume?vid=" + params[0]);
            Log.e("str", string);
            JSONObject jsonObject = null;
            JSONObject rows = null;
            try {
                jsonObject = new JSONObject(string);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                rows = jsonObject.getJSONObject("rows");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                infometion.add(rows.getString("name"));
                infometion.add(rows.getString("mark"));
                infometion.add(rows.getString("introduction"));
                infometion.add(rows.getString("release_time"));
                infometion.add(rows.getString("pay"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray part = null;
            try {
                part = rows.getJSONArray("part");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < part.length(); i++) {
                try {
                    JSONObject part1 = part.getJSONObject(i);
                    lists.add(part1.getString("url"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            listView.setAdapter(new palyplay_offline_adapter(PlayPlay.this));


        }
    }

    @Override
    public void onBackPressed() {
        if (isFull) {
            fullScreen();
        } else {
            infometion.clear();
            lists.clear();
            qq.clear();
            youku.clear();
            imgo.clear();
            sohu.clear();
            finish();
            super.onBackPressed();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.isPlaying()) {
            pause();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pause();

    }


}