package com.example.admin.a2017718;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import static com.example.admin.a2017718.Movie_view.context;

/**
 * Created by admin on 2017/8/27.
 */

public class local_login {
    public Boolean writelogin(String account, String password) {
        FileOutputStream fileOutputStream = null;
        String data = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", account);
            jsonObject.put("password", password);
            data = jsonObject.toString();
            Log.e("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            fileOutputStream = context.openFileOutput("login", context.MODE_PRIVATE);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
    public Boolean auto_login() {

        String json = null;
        String login = null;
        String html = null;
        URL url1 = null;
        HttpURLConnection getuser = null;
        String user = null, password = null;
        JSONObject jsonobject = null;
        InputStream input = null;

        File file = new File(context.getFilesDir() + "/login");
        if (file.exists()) {
            Log.e("login", "登录文件存在");

            try {
                input = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                json = new httpcontent().readstream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                jsonobject = new JSONObject(json);
                user = jsonobject.getString("account");
                password = jsonobject.getString("password");
                Log.e("登录文件帐号密码", user + " " + password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                login = new local_login().login(user, password);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("login2", login);

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
                    html = new httpcontent().readstream(getuser.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (html != null) {

                    Log.e("aa", html);
                    try {
                        Movie_view.ACCOUNT = jsonobject.getString("account");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (html.indexOf("影视VIP") != -1) {
                        Log.e("VIP", "true");
                        Movie_view.VIP = true;
                    }

                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    try {
                        bundle.putString("account", jsonobject.getString("account"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    msg.setData(bundle);
                    Movie_view.handler.sendMessage(msg);

                }

                return true;
            } else {
                Log.e("LOGIN", "登录失败");
                return false;
            }


        } else {

            Log.e("login", "登录文件不存在");
        }

        return false;
    }
    public String login(String user, String password) throws IOException, JSONException {

        String data = "user=" + user + "&pwd=" + password;
        URL url = new URL(Setting.URL + "/index/login/login");
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
            String str = new httpcontent().readstream(in);
            JSONObject json = new JSONObject(str);

            Log.e("login", str);

            if (json.getString("msg").equals("登陆成功")) {
                List<String> map = http.getHeaderFields().get("Set-Cookie");
                Movie_view.uid_token = map.get(0).substring(0, map.get(0).indexOf(";")) + ";" + map.get(1).substring(0, map.get(1).indexOf(";"));
                return json.getString("msg");
            } else {
                return json.getString("msg");
            }


        } else {
            return "ERROR";
        }


    }
}