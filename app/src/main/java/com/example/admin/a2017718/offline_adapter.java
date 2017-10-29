package com.example.admin.a2017718;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by admin on 2017/7/21.
 */

public class offline_adapter extends BaseAdapter {
    Context contexts;
    String url;
    boolean down = false;
    int page = 1;

    public offline_adapter(Context context) {
        this.contexts = context;


        if (Movie_view.offline.size() == 0) {
            new movie_getitem().execute(page);
            page++;


        }


    }

    @Override
    public int getCount() {
        return Movie_view.offline.size();
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

        if (position == Movie_view.offline.size() - 1 && down != true) {
            new movie_getitem().execute(page);
            page++;
        }

        convertView = LayoutInflater.from(contexts).inflate(R.layout.item, parent, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image1);
        TextView textView = (TextView) convertView.findViewById(R.id.name);
        TextView vip = (TextView) convertView.findViewById(R.id.vip);
        textView.setText(Movie_view.offline.get(position).getName());
        new loadimage().execute(String.valueOf(Movie_view.offline.get(position).getId()), String.valueOf(Movie_view.offline.get(position).getImageurl()), imageView);

        if (Movie_view.offline.get(position).getPay().equals("0")) {
            vip.setVisibility(View.GONE);
        }


        return convertView;
    }


    class loadimage extends AsyncTask<Object, Void, String> {

        String id;
        String url = null;
        URL Url;
        ImageView imageView;
        HttpURLConnection urlcon = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        @Override
        protected String doInBackground(Object... params) {
            id = (String) params[0];
            url = (String) params[1];
            imageView = (ImageView) params[2];

            File file = new File(contexts.getFilesDir() + "/" + id + ".jpg");

            try {
                Url = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (file.exists()) {
                if (file.length() > 3) {
                    return id;
                } else {
                    writer();
                }
                return id;
            } else {
                writer();
            }

            return id;

        }


        private void writer() {
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


            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // 计算缩放比例
            float scaleWidth = ((float) 240) / width;
            float scaleHeight = ((float) 320) / height;
            // 取得想要缩放的matrix参数

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);

            Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);







            imageView.setImageBitmap(newbm);
        }
    }

    class movie_getitem extends AsyncTask<Integer, Object, Boolean> {


        @Override
        protected Boolean doInBackground(Integer... params) {

            String contant = new httpcontent().GET(Setting.URL + "/user/movie/mlist?page=" + params[0], false);
            Log.e("offline", contant);
            if (contant.indexOf("DOCTYPE") != -1 || contant.equals("ERROR")) {
                Log.e("page3", "无法连接");
                return false;
            }
            if (contant.indexOf("rows") != -1) {
                try {
                    JSONObject jsonObject = new JSONObject(contant);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Movie_view.offline.add(new movies(jsonObject1.getString("vid"), jsonObject1.getString("pay"), jsonObject1.getString("name"), jsonObject1.getString("image_url")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                down = true;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            if (aVoid == true) {
                if (page == 2) {
                    notifyDataSetChanged();
                }
            } else {
                Page3.offlineview.setVisibility(View.VISIBLE);
            }


        }
    }
}