package com.example.admin.a2017718;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private  Handler handler=new Handler()
    {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {

                LayoutInflater layoutInflater=getLayoutInflater();
                View view=layoutInflater.inflate(R.layout.dialog,null);
                builder.setView(view);
                builder.setCancelable(false);
                alertDialog=builder.create();
                alertDialog.show();
            }else{alertDialog.dismiss();}
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder=new AlertDialog.Builder(MainActivity.this);

        startActivity(new Intent(MainActivity.this, Movie_view.class));
        finish();




    }











}