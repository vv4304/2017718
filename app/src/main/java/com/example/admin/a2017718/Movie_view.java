package com.example.admin.a2017718;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/7/19.
 */

public class Movie_view extends AppCompatActivity {
    int i = 1;
    public static List<movies> movielist = new ArrayList<>();
    public static List<movies> tv_drama=new ArrayList<>();
    public static List<movies> online_movie=new ArrayList<>();

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    public static movie_adapter movie_adapter;
    public static tvdrama_adapter tvdrama_adapter;
    public static online_adapter online_adapter;
    public ViewPager viewPager;
    ImageView line1;
    DisplayMetrics metric;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        viewfrist();
        button();
        movie_adapter = new movie_adapter(Movie_view.this);
        tvdrama_adapter=new tvdrama_adapter(Movie_view.this);
        online_adapter=new online_adapter(Movie_view.this);

        List<Fragment> fragments=new ArrayList<>();
        fragments.add(new Page1());
        fragments.add(new Page2());
        fragments.add(new Page3());



        viewPager= (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line1.getLayoutParams();
                lp.leftMargin=(int)(positionOffset*metric.widthPixels/3)+metric.widthPixels/3*position;
                line1.setLayoutParams(lp);
                Log.e("SS",String.valueOf(position+"/"+positionOffset));









            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });





        builder = new AlertDialog.Builder(Movie_view.this);



        new update().execute("http://s.icodef.com/user/api/update");




    }


    public void filelist(){
        String[] strings = fileList();
        for (int i = 0; i < strings.length; i++) {
            Log.e("list:", strings[i]);
        }

    }

    public void button()
    {
        Button button1= (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });




        Button button2= (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        Button button3= (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });






    }

    public void viewfrist()
    {
        line1= (ImageView) findViewById(R.id.line1);
         metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        ViewGroup.LayoutParams layoutParams=line1.getLayoutParams();
        layoutParams.width=metric.widthPixels/3;
        layoutParams.height=metric.heightPixels/300;
        line1.setLayoutParams(layoutParams);



    }









    @Override
    public void onBackPressed() {
        if (i > 0) {
            Toast.makeText(Movie_view.this, "在按一次退出", Toast.LENGTH_SHORT).show();
            i--;
        } else {
            System.exit(0);

        }

    }


    class update extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {

            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            String str = new gethttpcontent().return_contant(params[0]);

            JSONObject jsonObject = null;
            int version = 0;
            String url = null;
            InputStream inputStream = null;
            URL url1 = null;
            OutputStream outputStream = null;
            byte[] bytes = new byte[1024];
            int len = 0;

            try {
                jsonObject = new JSONObject(str);
                version = jsonObject.getInt("v");
                url = jsonObject.getString("u");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (version > packageInfo.versionCode) {


                handler.sendEmptyMessage(1);


                try {
                    url1 = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    inputStream = url1.openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    outputStream = openFileOutput("1.apk", MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                try {
                    while ((len = inputStream.read(bytes)) > 0) {
                        outputStream.write(bytes, 0, len);


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


                String[] command = {"chmod", "777", new File(getFilesDir() + "/1.apk").getPath()};
                ProcessBuilder builder = new ProcessBuilder(command);
                try {
                    builder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (Build.VERSION.SDK_INT <= 23) {

                    handler.sendEmptyMessage(0);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(getFilesDir() + "/1.apk")), "application/vnd.android.package-archive");
                    startActivity(intent);


                } else {

                    handler.sendEmptyMessage(0);

                    File file = new File(getFilesDir() + "/1.apk");

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(FileProvider.getUriForFile(Movie_view.this, "com.example.admin.a2017718", file), "application/vnd.android.package-archive");
                    startActivity(intent);


                }

                finish();


                return 1;
            }
            return 0;

        }


        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);

            if (aVoid == 0) {


                new update_text().execute();
            }


        }

        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {

                    LayoutInflater layoutInflater = getLayoutInflater();
                    View view = layoutInflater.inflate(R.layout.dialog, null);
                    builder.setView(view);


                    builder.setCancelable(false);
                    alertDialog = builder.create();
                    alertDialog.show();
                }

                if (msg.what == 0) {

                    alertDialog.dismiss();

                }


            }
        };


    }


    class update_text extends AsyncTask<Void, Void, Void> {

        String str = "aaa\n这是版本二";


        @Override
        protected Void doInBackground(Void... params) {

            handler.sendEmptyMessage(1);


            return null;
        }

        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {

                    LayoutInflater layoutInflater = getLayoutInflater();
                    View view = layoutInflater.inflate(R.layout.notice, null);
                    TextView textView = (TextView) view.findViewById(R.id.notice);
                    textView.setText(str);
                    Button button = (Button) view.findViewById(R.id.close);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });


                    builder.setView(view);
                    builder.setCancelable(false);
                    alertDialog = builder.create();
                    alertDialog.show();


                }

            }
        };


    }












}
