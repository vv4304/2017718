package com.example.admin.a2017718;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by admin on 2017/8/15.
 */

public class Page2 extends Fragment {

    public static LinearLayout offlineview;
    public static GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page2, container, false);

        offlineview = (LinearLayout) view.findViewById(R.id.offlineview);
        gridView = (GridView) view.findViewById(R.id.gridview);

        Movie_view.tvdrama_adapter = new tvdrama_adapter(Movie_view.context);
        offlineview.setVisibility(View.INVISIBLE);
        gridView.setAdapter(Movie_view.tvdrama_adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("A", String.valueOf(Movie_view.tv_drama.get(position).getImageurl()));

                Intent intent = new Intent(Movie_view.context, PlayPlay.class);
                intent.putExtra("url", Movie_view.tv_drama.get(position).getImageurl());
                intent.putExtra("type", "teleplay");
                startActivity(intent);


            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        Movie_view.tv_drama.clear();
        super.onDestroyView();
    }
}
