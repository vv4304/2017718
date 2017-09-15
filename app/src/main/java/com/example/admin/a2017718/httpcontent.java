package com.example.admin.a2017718;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 2017/7/25.
 */

public class httpcontent {

    public String GET(String url, Boolean cookie) {
        InputStream inputStream;
        String contant = null;
        HttpURLConnection httpURLConnection = null;
        int code = 0;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            if (cookie == true) {
                httpURLConnection.setRequestProperty("Cookie", Movie_view.uid_token);
            }
            httpURLConnection.setRequestMethod("GET");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            code = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (code == 200) {
            try {
                inputStream = httpURLConnection.getInputStream();
                contant = readstream(inputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return contant;


        } else {
            return "ERROR";
        }


    }


    public String POST(String... str) throws IOException {

        String data = "call=" + str[0] + "&msg=" + str[1];

        URL url = new URL("http://sv.icodef.com/user/api/feedback");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");

        conn.setConnectTimeout(5000);


        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Cookie", Movie_view.uid_token);

        conn.setRequestProperty("Content-Length", String.valueOf(data.length()));


        conn.setDoOutput(true);


        conn.getOutputStream().write(data.getBytes());


        if (conn.getResponseCode() == 200) {

            InputStream is = conn.getInputStream();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();


            return baos.toString();

        }

        return null;
    }

    public String readstream(InputStream inputStream) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];

        int len = 0;

        while ((len = inputStream.read(bytes)) != -1) {

            byteArrayOutputStream.write(bytes, 0, len);

        }

        inputStream.close();


        return new String(byteArrayOutputStream.toByteArray(), "UTF-8");


    }


}
