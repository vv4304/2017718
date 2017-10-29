package com.example.admin.a2017718;

import android.content.Context;
import android.content.DialogInterface;
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
    public static List<movies> offline = new ArrayList<>();
    public static List<movies> tv_drama = new ArrayList<>();
    public static List<movies> online_movie = new ArrayList<>();
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    public static offline_adapter offline_adapter;
    public static tvdrama_adapter tvdrama_adapter;
    public static movie_adapter movie_adapter;
    ViewPager viewPager;
    ImageView line1;
    DisplayMetrics metric;
    public static Context context;
    public static TextView account;
    public static Boolean VIP = true;
    public static String ACCOUNT = null;
    public static String uid_token = "null";
    String update_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        new online().execute();

        account = (TextView) findViewById(R.id.account);
        metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        builder = new AlertDialog.Builder(Movie_view.this);
        context = getApplication();
        viewfrist();
        button();
    }

    public void frist() {
        new login().execute();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Page1());
        fragments.add(new Page2());
        fragments.add(new Page3());


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) line1.getLayoutParams();
                lp.leftMargin = (int) (positionOffset * metric.widthPixels / 3) + metric.widthPixels / 3 * position;
                line1.setLayoutParams(lp);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    public void button() {
        ImageView setting = (ImageView) findViewById(R.id.setting);
        ImageView search = (ImageView) findViewById(R.id.search);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button2 = (Button) findViewById(R.id.button2);
        TextView game1 = (TextView) findViewById(R.id.game1);


        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Movie_view.this, Game1.class);
                startActivity(intent);
            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Movie_view.this, Setting.class);
                startActivity(intent);

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Movie_view.this, Search.class);
                startActivity(intent);

            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ACCOUNT == null) {
                    Intent intent = new Intent(Movie_view.this, Login.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Movie_view.this, user.class);
                    startActivity(intent);

                }

            }
        });


    }

    public void viewfrist() {
        line1 = (ImageView) findViewById(R.id.line1);
        ViewGroup.LayoutParams layoutParams = line1.getLayoutParams();
        layoutParams.width = metric.widthPixels / 3;
        layoutParams.height = metric.heightPixels / 300;
        line1.setLayoutParams(layoutParams);
        LinearLayout bar = (LinearLayout) findViewById(R.id.bar);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) bar.getLayoutParams();
        layoutParams1.height = metric.heightPixels / 12;
        bar.setLayoutParams(layoutParams1);
    }

    public static Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            account.setText(msg.getData().getString("account"));
            ACCOUNT = msg.getData().getString("account");
            Log.e("VIP", VIP.toString());

            if (VIP == true) {

            }
        }
    };


    public Handler note = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Movie_view.this);
                alertDialogBuilder.setMessage("无法连接到服务,请检查是否连接上校园网WIFI\"HNIU\"！本APP仅限服务于湖南信息职业技术学院");
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }

            if (msg.what == 9) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View view = layoutInflater.inflate(R.layout.dialog, null);
                builder.setView(view);


                builder.setCancelable(false);
                alertDialog = builder.create();
                alertDialog.show();
            }


        }
    };


    class examine_update extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject jsonObject = null;
            int version = 0;
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            String str = new httpcontent().GET(params[0], false);
            Log.e("version", str);


            try {
                jsonObject = new JSONObject(str);
                version = jsonObject.getInt("v");
                update_url = jsonObject.getString("u");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (version > packageInfo.versionCode) {
                return true;
            }

            return false;
        }


        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid == true) {
                new update().execute();
            }

        }
    }

    class update extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            handler.sendEmptyMessage(1);
            InputStream inputStream = null;
            URL url = null;
            OutputStream outputStream = null;
            byte[] bytes = new byte[1024];
            int len = 0;


            try {
                url = new URL(update_url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                inputStream = url.openStream();
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


            return null;
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

        String str;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                JSONObject jsonObject = new JSONObject(new httpcontent().GET(Setting.URL + "/index/api/notice_m", false));
                str = jsonObject.getString("msg");
                handler.sendEmptyMessage(1);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            frist();
            //new examine_update().execute(Setting.URL + "/index/api/update_m");
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
                            new examine_update().execute(Setting.URL + "/index/api/update_m");
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

    class login extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            if (new local_login().auto_login() == true) {
                Log.e("login", "true");
                return true;
            } else {
                Log.e("login", "false");
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean == true) {
                Toast.makeText(Movie_view.this, "登录成功", Toast.LENGTH_SHORT).show();
                MainActivity.sharedPreferences = getSharedPreferences(ACCOUNT, MODE_APPEND).edit();
                MainActivity.sp = getSharedPreferences(Movie_view.ACCOUNT, MODE_APPEND);
                new sign_up().execute();
            }


        }
    }

    class online extends AsyncTask<Object, Object, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            String test = new httpcontent().GET(Setting.URL + "/", false);
            if (test.equals("ERROR")) {
                note.sendEmptyMessage(0);
                return false;
            } else {
                return true;
            }

        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            if (aVoid == true) {
                // new update().execute(Setting.URL + "/index/api/update_m");
                new update_text().execute();
            }
        }
    }


    public void onBackPressed() {
        if (i > 0) {
            Toast.makeText(Movie_view.this, "在按一次退出", Toast.LENGTH_SHORT).show();
            i--;
        } else {
            offline.clear();
            tv_drama.clear();
            online_movie.clear();
            super.onBackPressed();
        }


    }


}