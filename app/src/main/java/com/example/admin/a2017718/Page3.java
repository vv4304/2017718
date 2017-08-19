package com.example.admin.a2017718;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by admin on 2017/8/16.
 */

public class Page3 extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page3, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridview3);
        gridView.setAdapter(Movie_view.online_adapter);


        return view;
    }
}
