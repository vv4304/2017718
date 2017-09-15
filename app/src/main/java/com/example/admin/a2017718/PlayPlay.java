package com.example.admin.a2017718;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.admin.ijkplayer.ijkvideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private ListView listView;
    public static String type;
    private boolean isFull = false;

    public static ijkvideo ijk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contantlist);
        Intent intent = getIntent();

        type = intent.getStringExtra("type");
        ijk = new ijkvideo(this);
        ijk.createPlayer();
        listView = (ListView) findViewById(R.id.playlistview);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, ijk.getHeight(), 0, 0);
        listView.setLayoutParams(lp);

        if (type.equals("offline")) {
            new offlinemovie().execute(intent.getStringExtra("id"));
        } else {
            new onlinemovie().execute(intent.getStringExtra("url"));
        }
    }

    class onlinemovie extends AsyncTask<String, Void, Void> {
        JSONObject jsonObject;
        JSONObject rows = null;

        @Override
        protected Void doInBackground(String... params) {


            String str = new httpcontent().GET("http://video.visha.cc/search/volume?url=" + params[0],false);

            try {
                jsonObject = new JSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("ssss", str + "  /  " + params[0]);

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

            /*
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
*/

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

            String string = new httpcontent().GET(Setting.URL + "/user/movie/volume?vid=" + params[0],false);
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
        if (ijk.isFull == false) {
            infometion.clear();
            lists.clear();
            qq.clear();
            youku.clear();
            imgo.clear();
            sohu.clear();
            ijk.deletePlayer();
            super.onBackPressed();

        }


    }


    @Override
    protected void onPause() {
        ijk.pause();
        super.onPause();
    }


}