package com.example.admin.a2017718;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by admin on 2017/7/24.
 */

public class content_recyclerviewAdapter extends RecyclerView.Adapter<content_recyclerviewAdapter.ViewHolder> {


    Activity activity;

    public content_recyclerviewAdapter(Activity activity) {

        this.activity = activity;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.button1);


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_item, parent, false));
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.button.setText(PlayPlay.parts.get(position).getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity, Play.class);
                intent.putExtra("url", String.valueOf(PlayPlay.parts.get(position).getUrl()));


                activity.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return PlayPlay.parts.size();
    }
}
