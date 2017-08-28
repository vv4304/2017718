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
import java.util.List;

/**
 * Created by admin on 2017/7/19.
 */

public class Login extends AppCompatActivity {

    EditText account;
    EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new login().execute(account.getText().toString(), password.getText().toString());
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new register().execute(account.getText().toString(), password.getText().toString());
            }
        });


    }


    class login extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String data = "user=" + params[0] + "&pwd=" + params[1];

            try {
                URL url = new URL("http://s.icodef.com/index/login/login");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setConnectTimeout(5000);

                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                http.setRequestProperty("Content-Length", String.valueOf(data.length()));
                http.setDoOutput(true);
                http.setDoInput(true);

                OutputStream out = http.getOutputStream();
                out.write(data.getBytes());
                out.close();


                if (http.getResponseCode() == 200) {



                    InputStream in = http.getInputStream();
                    String str = new gethttpcontent().readstream(in);
                    JSONObject json = new JSONObject(str);


                    if (json.getString("msg").equals("登陆成功")) {
                        List<String> map = http.getHeaderFields().get("Set-Cookie");
                        String uid_token = map.get(0).substring(0, map.get(0).indexOf(";")) + ";" + map.get(1).substring(0, map.get(1).indexOf(";"));
                        URL url1 = new URL("http://s.icodef.com/user/index/index");
                        HttpURLConnection getuser = (HttpURLConnection) url1.openConnection();
                        getuser.setRequestProperty("Cookie", uid_token);
                        getuser.setDoInput(true);
                        getuser.setRequestMethod("GET");
                        getuser.setConnectTimeout(5000);
                        String login = new gethttpcontent().readstream(getuser.getInputStream());



                        if (login != null) {

                            if (login.indexOf(params[0]) != -1) {
                                Movie_view.ACCOUNT = params[0];

                                if (login.indexOf("网络VIP1") != -1) {
                                    Movie_view.VIP = true;
                                }

                                if (new locallogin().writelogin(params[0], params[1]) != true) {
                                    Log.e("weitelogin", "写入错误");
                                }

                            }


                        }
                        return "登陆成功";

                    } else {
                        return json.getString("msg");
                    }

                } else {
                    return "连接失败";
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "null";

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("dada",s);


            if (s.equals("登陆成功")) {


                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("account", account.getText().toString());
                msg.setData(bundle);
                Movie_view.handler.sendMessage(msg);
                Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
                finish();

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
            String data = "user=" + params[0] + "&pwd=" + params[1] + "&email=zqzz%40qq.com&inv_code=af3z1z";


            try {
                url = new URL("http://s.icodef.com/index/login/register");
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
                    String str = new gethttpcontent().readstream(in);
                    JSONObject json = new JSONObject(str);


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