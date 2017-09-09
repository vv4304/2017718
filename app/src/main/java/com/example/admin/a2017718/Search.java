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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    int number = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        listView = (ListView) findViewById(R.id.searchshow);
        searchok = (Button) findViewById(R.id.searchok);
        searchtext = (EditText) findViewById(R.id.searchtext);
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number < 5) {
                    number++;
                    list.clear();
                    new getitem().execute(searchtext.getText().toString());
                } else {
                    Toast.makeText(Search.this, "请勿频繁搜索", Toast.LENGTH_SHORT).show();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (list.get(position).classs.equals("电影")) {
                    Intent intent = new Intent(Search.this, PlayPlay.class);
                    intent.putExtra("type", "movie");
                    intent.putExtra("url", list.get(position).url);
                    startActivity(intent);
                }
                if (list.get(position).classs.equals("电视剧")) {
                    Intent intent = new Intent(Search.this, PlayPlay.class);
                    intent.putExtra("type", "teleplay");
                    intent.putExtra("url", list.get(position).url);
                    startActivity(intent);
                }


            }
        });


    }


    class getitem extends AsyncTask<String, Object, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            String string = new gethttpcontent().return_contant("http://video.visha.cc/search?name=" + params[0]);

            Log.e("string", string);
            if (string == null) {
                return false;
            }

            try {
                JSONObject jsonObject = new JSONObject(string);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                if (jsonObject.getString("rows").equals("null")) {
                    return false;
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    list.add(new item(jsonObject1.getString("class"), jsonObject1.getString("title"), jsonObject1.getString("url")));


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return true;
        }


        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            if (aVoid == true) {
                listView.setAdapter(new baseadapter());
            } else {
                Toast.makeText(Search.this, "搜索错误", Toast.LENGTH_SHORT).show();
            }

        }
    }


    class item {
        private String classs;
        private String title;
        private String url;
        private String update;

        public item(String classs, String title, String url) {

            this.classs = classs;
            this.title = title;
            this.url = url;


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

            classs.setText(list.get(position).classs);
            title.setText(list.get(position).title);

            return convertView;
        }
    }


}