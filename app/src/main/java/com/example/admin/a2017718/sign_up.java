package com.example.admin.a2017718;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2017/10/28.
 */

public class sign_up extends AsyncTask<Void, Void, String> {


    @Override
    protected String doInBackground(Void... params) {

        String str = new httpcontent().GET("http://sv.icodef.com/user/api/check", true);
        Log.e("sign_up", str);
        try {
            JSONObject jsonObject = new JSONObject(str);
            String i;
            if (jsonObject.getString("msg").equals("签到成功")) {
                i = String.valueOf(new calculate().sign_up());
                MainActivity.sharedPreferences.putInt("money", MainActivity.sp.getInt("money", 0) + Integer.valueOf(i));
                MainActivity.sharedPreferences.apply();


                return "每日签到成功获得" + i + "个观影卷";
            } else if (jsonObject.getString("msg").equals("今天已经签过到了")) {
                return "今天已经签过到了";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "签到发生错误";
    }


    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(Movie_view.context, aVoid, Toast.LENGTH_LONG).show();

    }


}
