package com.example.admin.a2017718;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by admin on 2017/7/19.
 */

public class Login extends AppCompatActivity {
    EditText account;
    EditText password;
    public static String uid_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        final TextView login = (TextView) findViewById(R.id.login);
        final EditText emailedittext = (EditText) findViewById(R.id.emailedittext);
        final LinearLayout email = (LinearLayout) findViewById(R.id.email);
        final Button register = (Button) findViewById(R.id.register);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setText("登录");
                email.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (register.getText().toString().equals("注册")) {
                    new register().execute(account.getText().toString(), password.getText().toString(), emailedittext.getText().toString());
                } else {
                    new login().execute(account.getText().toString(), password.getText().toString());
                }
            }
        });
    }

    class login extends AsyncTask<String, Void, String> {
        String login = null;
        URL url1 = null;
        String user = null;
        HttpURLConnection getuser = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                login = new local_login().login(params[0], params[1]);
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            Log.e("login", login);
            if (login.equals("登陆成功")) {
                try {
                    url1 = new URL(Setting.URL + "/user/api/getauth");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    getuser = (HttpURLConnection) url1.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getuser.setRequestProperty("Cookie", Movie_view.uid_token);
                getuser.setDoInput(true);
                try {
                    getuser.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                getuser.setConnectTimeout(5000);
                try {
                    user = new httpcontent().readstream(getuser.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("login", user);
                if (user != null) {
                    if (user.indexOf("你没有相应的权限") != -1) {
                        return "无法登录：你的注册帐号可能没有通过邮箱激活,请登录你填写的邮箱中查看激活连接";
                    }
/*
                    if (login.indexOf("影视VIP") != -1) {
                        Movie_view.VIP = true;
                    }
*/
                    if (new local_login().writelogin(params[0], params[1]) != true) {
                        Log.e("weitelogin", "写入错误");
                    }
                }
                return "登陆成功";
            } else {
                return login;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("登陆成功")) {
                Movie_view.ACCOUNT = account.getText().toString();
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("account", account.getText().toString());
                msg.setData(bundle);
                Movie_view.handler.sendMessage(msg);
                Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
                finish();
                MainActivity.sharedPreferences = getSharedPreferences(Movie_view.ACCOUNT, MODE_APPEND).edit();
                MainActivity.sp = getSharedPreferences(Movie_view.ACCOUNT, MODE_APPEND);
                new sign_up().execute();

            } else {
                Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
            }

        }
    }

    class register extends AsyncTask<Object, Object, String> {


        @Override
        protected String doInBackground(Object... params) {

            URL url = null;
            OutputStream out = null;
            HttpURLConnection http = null;
            String data = "user=" + params[0] + "&pwd=" + params[1] + "&email=" + params[2];


            try {
                url = new URL(Setting.URL + "/index/login/register");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                http = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                http.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            http.setConnectTimeout(5000);

            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setRequestProperty("Content-Length", String.valueOf(data.length()));
            http.setDoOutput(true);
            http.setDoInput(true);


            try {
                out = http.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.write(data.getBytes());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                if (http.getResponseCode() == 200) {


                    InputStream in = http.getInputStream();
                    String str = new httpcontent().readstream(in);
                    JSONObject json = new JSONObject(str);

                    Log.e("register", str);

                    if (json.getString("msg").equals("注册成功")) {
                        return "注册成功";

                    } else {
                        return json.getString("msg");
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;

        }


        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);


            if (aVoid != null) {

                if (aVoid.equals("注册成功")) {

                    Toast.makeText(Login.this, "注册成功！快登录吧", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, aVoid, Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

}