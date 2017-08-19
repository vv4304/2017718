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

import static com.example.admin.a2017718.Movie_view.movielist;

/**
 * Created by admin on 2017/7/21.
 */

public class movie_adapter extends BaseAdapter {
    Context contexts;
    String url;
    boolean down = false;
    int page = 1;

    public movie_adapter(Context context) {
        this.contexts = context;


        if (Movie_view.movielist.size() == 0) {
            new movie_getitem().execute(page);
            page++;


        }


    }

    @Override
    public int getCount() {
        return movielist.size();
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

        if (position == Movie_view.movielist.size() - 1 && down != true) {


            new movie_getitem().execute(page);
            page++;


        }


        convertView = LayoutInflater.from(contexts).inflate(R.layout.item, parent, false);


        ImageView imageView = (ImageView) convertView.findViewById(R.id.image1);
        TextView textView = (TextView) convertView.findViewById(R.id.name);
        TextView vip = (TextView) convertView.findViewById(R.id.vip);

        textView.setText(movielist.get(position).getName());

        new loadimage().execute(String.valueOf(movielist.get(position).getId()), String.valueOf(movielist.get(position).getImageurl()), imageView);

        if (movielist.get(position).getPay() != "0") {
            vip.setVisibility(View.GONE);


        }


        return convertView;
    }


    class loadimage extends AsyncTask<Object, Void, String> {

        String id;
        String url = null;
        URL Url;
        ImageView imageView;

        @Override
        protected String doInBackground(Object... params) {

            id = (String) params[0];
            url = (String) params[1];
            imageView = (ImageView) params[2];


            InputStream inputStream = null;
            OutputStream outputStream = null;


            File file = new File(contexts.getFilesDir() + "/" + id + ".jpg");
            if (file.exists()) {
                return id.toString();
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
                    outputStream = contexts.openFileOutput(id + ".jpg", contexts.MODE_PRIVATE);
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
                fileInputStream = new FileInputStream(contexts.getFilesDir() + "/" + s + ".jpg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            imageView.setImageBitmap(bitmap);


        }


    }


    class movie_getitem extends AsyncTask<Integer, Void, Void> {


        String contant;

        @Override
        protected Void doInBackground(Integer... params) {


            contant = new gethttpcontent().return_contant("http://s.icodef.com/user/movie/mlist?page=" + params[0]);


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (contant != "ERROR") {


                if (contant.indexOf("rows") != -1) {
                    try {
                        JSONObject jsonObject = new JSONObject(contant);
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            Movie_view.movielist.add(new movies(jsonObject1.getString("vid"), jsonObject1.getString("pay"), jsonObject1.getString("name"), jsonObject1.getString("image_url")));
                        }

                        if (page == 2) {
                            notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    down = true;
                }


            }


        }
    }


}











