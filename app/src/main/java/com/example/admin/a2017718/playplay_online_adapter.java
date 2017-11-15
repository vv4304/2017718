package com.example.admin.a2017718;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.admin.a2017718.PlayPlay.imgo;
import static com.example.admin.a2017718.PlayPlay.ijk;
import static com.example.admin.a2017718.PlayPlay.qq;
import static com.example.admin.a2017718.PlayPlay.sohu;
import static com.example.admin.a2017718.PlayPlay.youku;

/**
 * Created by admin on 2017/8/19.
 */

public class playplay_online_adapter extends BaseAdapter {
    LayoutInflater inflater;
    RecyclerView recyclerView;
    Spinner sp;
    Context context;
    String select;
    Henadatpter henadatpter = null;
    playplay_online_adapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
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

        if (position == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.head, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView type = (TextView) convertView.findViewById(R.id.type);
            TextView number = (TextView) convertView.findViewById(R.id.number);

            name.setText(PlayPlay.infometion.get(0));
            type.setText(PlayPlay.infometion.get(1) + "*" + PlayPlay.infometion.get(2));
            number.setText(PlayPlay.infometion.get(3));

        }


        if (position == 1) {
            convertView = LayoutInflater.from(context).inflate(R.layout.selectpage, parent, false);

            ArrayAdapter<CharSequence> eduAdapter = null;
            sp = (Spinner) convertView.findViewById(R.id.spinner);
            recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerview);


            eduAdapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, PlayPlay.list);
            eduAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp.setAdapter(eduAdapter);


           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
           linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);


            recyclerView.setLayoutManager(linearLayoutManager);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("select", String.valueOf(PlayPlay.list.get(position)));

                    switch (String.valueOf(PlayPlay.list.get(position))) {
                        case "腾讯":
                            henadatpter = new Henadatpter(qq);
                            select = "qq";
                            break;
                        case "优酷":
                            henadatpter = new Henadatpter(youku);
                            select = "youku";
                            break;
                        case "芒果":
                            henadatpter = new Henadatpter(imgo);
                            select = "imgo";
                            break;
                        case "搜狐":
                            henadatpter = new Henadatpter(sohu);
                            select = "sohu";
                            break;
                    }


                    recyclerView.setAdapter(henadatpter);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }


        if (position == 2) {

            convertView = LayoutInflater.from(context).inflate(R.layout.introduction, parent, false);
            TextView introduction = (TextView) convertView.findViewById(R.id.introduction);
            introduction.setText(PlayPlay.infometion.get(4));


        }


        return convertView;
    }

    class loadurl extends AsyncTask<Integer, Void, String> {
        String string = null;

        @Override
        protected String doInBackground(Integer... params) {

            switch (select) {
                case "qq":
                    try {
                        Log.e("解析地址", qq.get(params[0]));
                        string = new JSONObject(new httpcontent().GET(Setting.URL+"/user/movie/api?url=https" + qq.get(params[0]).substring(4),true)).getJSONArray("video").getJSONObject(2).getString("m3u8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "sohu":
                    try {
                        Log.e("解析地址", sohu.get(params[0]));
                        string = new JSONObject(new httpcontent().GET(Setting.URL+"/user/movie/api?url=" + sohu.get(params[0]),true)).getJSONArray("video").getJSONObject(2).getString("m3u8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "youku":
                    try {
                        Log.e("解析地址", youku.get(params[0]));
                        if (PlayPlay.type.equals("teleplay")) {
                            string = new JSONObject(new httpcontent().GET(Setting.URL+"/user/movie/api?url=" + youku.get(params[0]),true)).getJSONArray("video").getJSONObject(3).getString("m3u8");
                        } else {
                            string = new JSONObject(new httpcontent().GET(Setting.URL+"/user/movie/api?url=" + youku.get(params[0]),true)).getJSONArray("video").getJSONObject(2).getString("m3u8");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case "imgo":
                    try {
                        Log.e("解析地址", imgo.get(params[0]));
                        string = new JSONObject(new httpcontent().GET(Setting.URL+"/user/movie/api?url=" + imgo.get(params[0]),true)).getJSONArray("video").getJSONObject(2).getString("m3u8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

            }

            if (string != null) {
                Log.e("GET_xml_m3u8", string);

                if (string.indexOf("xml") != -1) {
                    if (werter("http://video.visha.cc/index.php?action=x2f&url=" + string) == true) {
                        return context.getFilesDir() + "/temporary.ffconcat";
                    } else {
                        Log.e("GET_ffconcat", "null");
                        return "0";
                    }
                } else {
                    return string;
                }


            } else {
                Log.e("GET_xml_m3u8", "null");
                return "0";
            }


        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            Log.e("aVoid",aVoid);
            if (aVoid.equals("0") != true) {
                ijk.setVideoUrl(aVoid);
                ijk.start();
            } else {
                Toast.makeText(context, "线路维护中,结束的时候公告将会通知。。", Toast.LENGTH_LONG).show();
            }

        }


    }

    public Boolean werter(String url) {
        Log.e("Error", url);
        int code = 0;

        HttpURLConnection httpURLConnection = null;


        InputStream inputStream = null;
        OutputStream outputStream = null;


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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            outputStream = context.openFileOutput("temporary.ffconcat", context.MODE_PRIVATE);

        } catch (FileNotFoundException e) {
            Log.e("Error", "outputstream");
        }


        byte[] by = new byte[1024];
        int len = 0;


        try {
            inputStream.read();
            inputStream.read();
            inputStream.read();
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


        return true;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        Button mTitle;

        public MyViewHolder(View view) {
            super(view);
            mTitle = (Button) view.findViewById(R.id.button1);
        }

    }
    class Henadatpter extends RecyclerView.Adapter<MyViewHolder> {
        View view;
        List<String> str = new ArrayList<>();

        public Henadatpter(List<String> str) {
            this.str = str;
            Log.e("str", String.valueOf(str.size()));
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(context).inflate(R.layout.select_item, parent, false);
            return new MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            holder.mTitle.setText(String.valueOf(position + 1));
            holder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Movie_view.ACCOUNT != null) {
                        if (MainActivity.sp.getInt("money", 0) >= 1) {
                            MainActivity.sharedPreferences.putInt("money", MainActivity.sp.getInt("money", 0) - 1);
                            MainActivity.sharedPreferences.apply();
                            PlayPlay.ijk.loading.setVisibility(View.VISIBLE);
                            PlayPlay.ijk.setTitle("第"+(position+1)+"集");
                            new loadurl().execute(position);
                            Toast.makeText(context, "播放消耗1个观影卷,剩余数量："+String.valueOf(MainActivity.sp.getInt("money", 0)), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Movie_view.context,"需要消耗1个观影卷才能播放此剧",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, user.class);
                            context.startActivity(intent);
                        }
                    } else {
                        Toast.makeText(context,"需要先登录",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, Login.class);
                        context.startActivity(intent);
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return str.size();
        }
    }

}