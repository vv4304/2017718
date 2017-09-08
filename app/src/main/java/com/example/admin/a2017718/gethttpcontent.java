package com.example.admin.a2017718;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2017/7/25.
 */

public class gethttpcontent {

    public String return_contant(String url) {

        InputStream inputStream;
        String contant = null;
        HttpURLConnection httpURLConnection=null;
        int code=0;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestProperty("Cookie", Movie_view.uid_token);
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
