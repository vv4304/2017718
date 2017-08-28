package com.example.admin.a2017718;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * Created by admin on 2017/8/20.
 */

public class list_adapter extends BaseAdapter {

    LayoutInflater inflater;

    Context context;

    public list_adapter(Context context)
    {
        this.context=context;


    }



    @Override
    public int getCount() {
        return 5;
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

        convertView=LayoutInflater.from(context).inflate(R.layout.select_item,parent,false);


        Log.e("s","ssss");
        Button button= (Button) convertView.findViewById(R.id.button1);

        button.setText(PlayPlay.list.get(position));

        Log.e("SSSS",String.valueOf(position));






        return convertView;
    }
}
