package com.example.admin.a2017718;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 2017/7/18.
 */

public class Play extends AppCompatActivity {

    int i = 0;
    int numberOFtimes = 1;

    boolean show = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Intent intent = getIntent();


        final VideoView videoView = (VideoView) findViewById(R.id.videoView1);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);
        final ImageView back = (ImageView) findViewById(R.id.back);
        RelativeLayout R1 = (RelativeLayout) findViewById(R.id.R1);
        Uri uri = Uri.parse(intent.getStringExtra("url"));
        videoView.setVideoPath(uri.toString());
        videoView.start();
        videoView.requestFocus();


        R1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (show == true) {
                    seekBar.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.INVISIBLE);
                    show = false;

                } else {
                    seekBar.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    show = true;


                }


            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                videoView.stopPlayback();
                Intent intent = new Intent(Play.this, Context.class);
                startActivity(intent);
                finish();


            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                i = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(i);
                videoView.pause();
                videoView.start();

            }
        });


        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                seekBar.setMax(videoView.getDuration());
                seekBar.setProgress(videoView.getCurrentPosition());


            }
        };


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                handler.sendEmptyMessage(0x123);

            }
        }, 0, 10);


    }

    @Override
    public void onBackPressed() {
        if (numberOFtimes > 0) {
            Toast.makeText(Play.this, "再按一下", Toast.LENGTH_SHORT).show();
            numberOFtimes--;
        } else {
            finish();
        }
    }


}
