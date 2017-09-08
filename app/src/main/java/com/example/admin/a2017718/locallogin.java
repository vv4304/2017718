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

public class locallogin {


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


    public Boolean login() {


        String json = null;
        URL url = null;
        JSONObject jsonobject = null;
        InputStream input = null;
        OutputStream out = null;
        String data = null;
        HttpURLConnection http = null;

        File file = new File(context.getFilesDir() + "/login");
        if (file.exists()) {
            Log.e("login", "登录文件存在");

            try {
                input = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                json = new gethttpcontent().readstream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                jsonobject = new JSONObject(json);
                data = "user=" + jsonobject.getString("account") + "&pwd=" + jsonobject.getString("password");
                Log.e("登录文件内容", data);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                url = new URL(Setting.URL + "/index/login/login");
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

            if (out == null) {
                return false;
            }
            try {
                out.write(data.getBytes());
                out.close();
            } catch (IOException e) {
                Log.e("login", "写入失败");
            }


            try {
                if (http.getResponseCode() == 200) {
                    InputStream in = http.getInputStream();
                    String str = new gethttpcontent().readstream(in);
                    Log.e("aaa", str);

                    JSONObject re = null;
                    try {
                        re = new JSONObject(str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (re.getString("msg").equals("登陆成功")) {
                        Log.e("login", "登录成功");

                        List<String> map = http.getHeaderFields().get("Set-Cookie");
                        Movie_view.uid_token = map.get(0).substring(0, map.get(0).indexOf(";")) + ";" + map.get(1).substring(0, map.get(1).indexOf(";"));


                        URL url1 = new URL(Setting.URL + "/user/api/getauth");
                        HttpURLConnection getuser = (HttpURLConnection) url1.openConnection();
                        getuser.setRequestProperty("Cookie", Movie_view.uid_token);
                        getuser.setDoInput(true);
                        getuser.setRequestMethod("GET");
                        getuser.setConnectTimeout(5000);
                        String login = new gethttpcontent().readstream(getuser.getInputStream());

                        if (login != null) {

                            Log.e("aa", login);
                            Movie_view.ACCOUNT = jsonobject.getString("account");
                            if (login.indexOf("影视VIP") != -1) {
                                Log.e("VIP", "true");
                                Movie_view.VIP = true;
                            }

                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("account", jsonobject.getString("account"));
                            msg.setData(bundle);
                            Movie_view.handler.sendMessage(msg);

                        }

                    } else {
                        Log.e("LOGIN", "登录失败");
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            return false;
        }


        return true;
    }


}