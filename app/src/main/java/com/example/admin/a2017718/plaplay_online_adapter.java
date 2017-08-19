package com.example.admin.a2017718;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by admin on 2017/8/19.
 */

public class plaplay_online_adapter extends BaseAdapter {

    LayoutInflater inflater;

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

        if(position==0)
        {
            convertView=inflater.inflate(R.layout.head,parent,false);

            TextView name= (TextView) convertView.findViewById(R.id.name);
            TextView type= (TextView) convertView.findViewById(R.id.type);
            TextView number= (TextView) convertView.findViewById(R.id.number);

            name.setText(PlayPlay.infometion.get(0));
            type.setText(PlayPlay.infometion.get(1)+"*"+PlayPlay.infometion.get(2)+"*"+PlayPlay.infometion.get(3));
            number.setText(PlayPlay.infometion.get(4));
        }


        if(position==1)
        {
            convertView=inflater.inflate(R.layout.selectpage,parent,false);
            GridView line= (GridView) convertView.findViewById(R.id.line);
            GridView selectpage= (GridView) convertView.findViewById(R.id.selectpage);
            line.setAdapter();
            selectpage.setAdapter();










        }











        return convertView;
    }





}
