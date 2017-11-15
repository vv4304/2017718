package com.example.admin.a2017718;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by admin on 2017/10/29.
 */

public class Game1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game1);

        final TextView money = (TextView) findViewById(R.id.money);
        final TextView value = (TextView) findViewById(R.id.value);
        Button submit = (Button) findViewById(R.id.submit);
        final SharedPreferences.Editor output = getSharedPreferences(Movie_view.ACCOUNT, MODE_APPEND).edit();
        final SharedPreferences input = getSharedPreferences(Movie_view.ACCOUNT, MODE_APPEND);
        ImageView back = (ImageView) findViewById(R.id.back);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        money.setText("当前拥有 " + String.valueOf(input.getInt("money", 0)) + " 个观影卷可使用");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getInt("money", 0) >= 1) {
                    String s;
                    output.putInt("money", input.getInt("money", 0) - 1);
                    output.apply();
                    s = String.valueOf(calculate.random());
                    value.setText(s);
                    Toast.makeText(Game1.this, "获得 " + s + "个观影卷", Toast.LENGTH_SHORT).show();
                    output.putInt("money", input.getInt("money", 0) + Integer.valueOf(s));
                    output.apply();
                    money.setText("当前拥有 " + input.getInt("money", 0) + " 个观影卷可使用");
                } else {
                    Toast.makeText(Game1.this, "你没有足够的观影卷！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
