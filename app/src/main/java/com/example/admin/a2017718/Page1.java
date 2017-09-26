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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by admin on 2017/8/14.
 */

public class Page1 extends Fragment {

    public GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page1, container, false);

        Movie_view.offline_adapter = new offline_adapter(Movie_view.context);

        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(Movie_view.offline_adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PlayPlay.class);
                intent.putExtra("type", "offline");
                intent.putExtra("id", String.valueOf(Movie_view.offline.get(position).getId()));
                startActivity(intent);

            }
        });



        return view;
    }

    @Override
    public void onDestroyView() {
        Movie_view.offline.clear();
        super.onDestroyView();
    }
}
