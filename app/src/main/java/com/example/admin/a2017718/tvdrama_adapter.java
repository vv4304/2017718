package com.example.admin.a2017718;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by admin on 2017/8/16.
 */

public class tvdrama_adapter extends BaseAdapter {

    Context context;
    int Page = 1;
    boolean down = false;

    public tvdrama_adapter(Context context) {
        this.context = context;


        if (Movie_view.tv_drama.size() == 0) {

            new getitme().execute(Page);
            Page++;


        }


    }


    @Override
    public int getCount() {
        return Movie_view.tv_drama.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (position == Movie_view.tv_drama.size() - 1 && down != true) {


            new getitme().execute(Page);
            Page++;


        }


        convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);


        TextView update = (TextView) convertView.findViewById(R.id.vip);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image1);
        TextView name = (TextView) convertView.findViewById(R.id.name);


        name.setText(Movie_view.tv_drama.get(position).getId());
        update.setText(Movie_view.tv_drama.get(position).getPay());

        new loadimage().execute(Movie_view.tv_drama.get(position).getId(), Movie_view.tv_drama.get(position).getName(), imageView);


        return convertView;
    }


    class getitme extends AsyncTask<Integer, Void, Void> {

        JSONObject jsonObject;
        JSONArray jsonArray;

        @Override
        protected Void doInBackground(Integer... params) {

            String src = new gethttpcontent().return_contant("http://video.visha.cc/search?class=%E7%94%B5%E8%A7%86%E5%89%A7&page=" + params[0]);

            try {
                jsonObject = new JSONObject(src);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.e("A", src);

            try {
                jsonArray = jsonObject.getJSONArray("rows");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < jsonArray.length(); i++) {

                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    Movie_view.tv_drama.add(new movies(jsonObject.getString("title"), jsonObject.getString("update"), jsonObject.getString("image"), jsonObject.getString("url")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (Page == 2) {
                notifyDataSetChanged();
            }
        }
    }


    class loadimage extends AsyncTask<Object, Void, String> {

        String name;
        String url = null;
        URL Url;
        ImageView imageView;

        @Override
        protected String doInBackground(Object... params) {

            name = (String) params[0];
            url = (String) params[1];
            imageView = (ImageView) params[2];


            InputStream inputStream = null;
            OutputStream outputStream = null;


            File file = new File(context.getFilesDir() + "/" + name + ".jpg");
            if (file.exists()) {
                return name;
            } else {


                try {
                    Url = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                try {
                    inputStream = Url.openStream();
                } catch (IOException e) {
                    Log.e("Error", "inputstream");
                }


                try {
                    outputStream = context.openFileOutput(name + ".jpg", context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    Log.e("Error", "outputstream");
                }


                byte[] by = new byte[1024];
                int len = 0;


                try {
                    while ((len = inputStream.read(by)) > 0) {
                        outputStream.write(by, 0, len);
                    }


                } catch (IOException e) {
                    Log.e("Error", "write");
                }


                try {
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return params[0].toString();

            }


        }


        @Override
        protected void onPostExecute(String s) {


            FileInputStream fileInputStream = null;


            try {
                fileInputStream = new FileInputStream(context.getFilesDir() + "/" + s + ".jpg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            imageView.setImageBitmap(bitmap);


        }


    }


}