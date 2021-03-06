package com.example.admin.a2017718;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by admin on 2017/9/2.
 */

public class Setting extends AppCompatActivity {

    public static final String URL = "http://sv.icodef.com/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);


        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button file = (Button) findViewById(R.id.file);
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Setting.this, "未完成", Toast.LENGTH_SHORT).show();


            }
        });

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Movie_view.ACCOUNT != null) {
                    Intent intent = new Intent(Setting.this, com.example.admin.a2017718.feedback.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Setting.this, "你没有登录，无法发送反馈", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void filelist() {
        String[] strings = fileList();
        for (int i = 0; i < strings.length; i++) {
            Toast.makeText(Setting.this, "删除" + strings[i], Toast.LENGTH_SHORT).show();
            deleteFile(getFilesDir() + "/" + strings[i]);
        }
    }

    public void check(String string)//检查文件
    {

        String[] strings1 = fileList();

        for (String sr : strings1) {
            Log.e("LIST", sr);

        }

        String urll = getFilesDir() + "/temporary.ffconcat";
        try {

            FileReader file = new FileReader(urll);
            BufferedReader buff = new BufferedReader(file);
            String valueString = null;
            while ((valueString = buff.readLine()) != null) {
                Log.e("contant", valueString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file1 = new File(urll);
        InputStream in = null;
        try {
            in = new FileInputStream(file1);
            byte[] b = new byte[3];
            in.read(b);
            in.close();
            if (b[0] == -17 && b[1] == -69 && b[2] == -65)
                string = "带BOMUTF-8";
            else
                string = "不带BOM";

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}