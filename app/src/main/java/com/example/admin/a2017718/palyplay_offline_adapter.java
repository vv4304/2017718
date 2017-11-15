package com.example.admin.a2017718;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/19.
 */

public class palyplay_offline_adapter extends BaseAdapter {

    Context context;

    public palyplay_offline_adapter(Context context) {
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
            TextView pay = (TextView) convertView.findViewById(R.id.number);
            TextView type = (TextView) convertView.findViewById(R.id.type);

            name.setText(PlayPlay.infometion.get(0));
            type.setText("豆瓣评分：" + PlayPlay.infometion.get(1) + "   " + "上映日期：" + PlayPlay.infometion.get(3));
            if (PlayPlay.infometion.get(4).equals("1")) {
                pay.setText("需要消耗1个观影卷才能播放此剧");
            } else {
                pay.setText("试看");
            }

        }

        if (position == 1) {
            convertView = LayoutInflater.from(context).inflate(R.layout.selectpage, parent, false);
            RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            Henadatpter henadatpter = new Henadatpter(PlayPlay.lists);
            recyclerView.setAdapter(henadatpter);
        }

        if (position == 2) {
            convertView = LayoutInflater.from(context).inflate(R.layout.introduction, parent, false);
            TextView introduction = (TextView) convertView.findViewById(R.id.introduction);
            introduction.setText(PlayPlay.infometion.get(2));
        }
        return convertView;
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
                            PlayPlay.ijk.setTitle("第" + (position + 1) + "集");
                            PlayPlay.ijk.setVideoUrl(PlayPlay.lists.get(position));
                            PlayPlay.ijk.start();
                            Toast.makeText(context, "播放消耗1个观影卷,剩余数量：" + String.valueOf(MainActivity.sp.getInt("money", 0)), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "需要消耗1个观影卷才能播放此剧", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, user.class);
                            context.startActivity(intent);
                        }


                        new Thread() {
                            @Override
                            public void run() {
                                /*
                                String statistics =new httpcontent().GET("http://sv.icodef.com/user/movie/statistics?vid="+PlayPlay.id,true);
                                Log.e("statistics",PlayPlay.id+"/"+statistics);
*/
                                try {
                                    new httpcontent().POST("影视播放点击记录", PlayPlay.infometion.get(0) + "||||||||||||||||||||");
                                } catch (IOException e) {
                                }

                            }
                        }.start();


                    } else {
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

