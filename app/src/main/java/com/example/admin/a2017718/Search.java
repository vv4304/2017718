package com.example.admin.a2017718;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/8.
 */

public class Search extends AppCompatActivity {
    List<item> list = new ArrayList<>();
    ListView listView;
    EditText searchtext;
    Button searchok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        listView = (ListView) findViewById(R.id.searchshow);
        searchok = (Button) findViewById(R.id.searchok);
        searchtext = (EditText) findViewById(R.id.searchtext);


        searchok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new getitem().execute(searchtext.getText().toString());


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Log.e("aaaa","aaaaaaaaaaaaaaaaa");

                Intent intent=new Intent(Search.this,PlayPlay.class);
                intent.putExtra("type","onlineline");
                intent.putExtra("url",list.get(position).url);
                startActivity(intent);

            }
        });



    }


    class getitem extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {
            Log.e("sss", params[0]);

            String string = new gethttpcontent().return_contant("http://video.visha.cc/search?name=" + params[0]);

            Log.e("aaa", string);

            try {
                JSONObject jsonObject = new JSONObject(string);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    list.add(new item(jsonObject1.getString("class"), jsonObject1.getString("title"), jsonObject1.getString("url"), jsonObject1.getString("update")));


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < list.size(); i++) {
                Log.e("a", list.get(i).title);

            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listView.setAdapter(new baseadapter());



        }
    }


    class item {
        private String classs;
        private String title;
        private String url;
        private String update;

        public item(String classs, String title, String url, String update) {

            this.classs = classs;
            this.title = title;
            this.url = url;
            this.update = update;


        }

    }


    class baseadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
            convertView = LayoutInflater.from(Search.this).inflate(R.layout.rearch_item, parent, false);
            TextView classs = (TextView) convertView.findViewById(R.id.classs);
            TextView title = (TextView) convertView.findViewById(R.id.itemtitle);
            TextView update = (TextView) convertView.findViewById(R.id.update);

            classs.setText(list.get(position).classs);
            title.setText(list.get(position).title);
            update.setText(list.get(position).update);


            return convertView;
        }
    }


}