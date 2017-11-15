package com.example.admin.a2017718;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by admin on 2017/10/28.
 */

public class user extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        TextView id = (TextView) findViewById(R.id.ID);
        TextView value = (TextView) findViewById(R.id.value);
        Button exit = (Button) findViewById(R.id.exit);
        Button output = (Button) findViewById(R.id.output);
        Button input = (Button) findViewById(R.id.input);
        ImageView back = (ImageView) findViewById(R.id.back);
        SharedPreferences sharedPreferences = getSharedPreferences(Movie_view.ACCOUNT, MODE_PRIVATE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id.setText(Movie_view.ACCOUNT);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Movie_view.VIP = false;
                Movie_view.ACCOUNT = null;
                Movie_view.account.setText("点击登录");
                finish();

            }
        });
        value.setText("$ " + String.valueOf(sharedPreferences.getInt("money", 0)) + " 观影卷");
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(user.this, Webview.class);
                intent.putExtra("url", Setting.URL + "/index/login/login");
                startActivity(intent);
            }
        });
        output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(user.this, Game1.class);
                startActivity(intent);
            }
        });


    }
}
