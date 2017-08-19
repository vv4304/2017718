package com.example.admin.a2017718;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2017/7/25.
 */

public class gethttpcontent {

    private HttpURLConnection httpURLConnection;
    private int code;
    private InputStream inputStream;
    private String contant;


    public String return_contant(String url) {


        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setReadTimeout(5000);
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
                contant = readstream();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return contant;


        } else {
            return "ERROR";

        }


    }


    private String readstream() throws IOException {

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
