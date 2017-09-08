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
                pay.setText("VIP可观看");
            } else {
                pay.setText("免费试看");
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


                        if (PlayPlay.infometion.get(4).equals("1")) {

                            if (Movie_view.VIP == true) {
//                                loading.setVisibility(View.VISIBLE);
                                PlayPlay.ijk.setVideoUrl(PlayPlay.lists.get(position));
                                PlayPlay.ijk.start();
                            } else {
                                Intent intent = new Intent(context, Webview.class);
                                intent.putExtra("url", Setting.URL+"/VALLEY/vip.html");
                                context.startActivity(intent);

                            }

                        } else {
//                            loading.setVisibility(View.VISIBLE);
                            PlayPlay.ijk.setVideoUrl(PlayPlay.lists.get(position));
                            PlayPlay.ijk.start();

                        }


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

