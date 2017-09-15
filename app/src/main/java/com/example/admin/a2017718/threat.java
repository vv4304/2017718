package com.example.admin.a2017718;

import android.util.Log;

/**
 * Created by admin on 2017/9/15.
 */

public class threat extends Thread {

    @Override
    public void run() {
        while (true)
        {
            String string=new httpcontent().GET("http://sv.icodef.com/index/api/online",false);
            Log.e("online",string);
            try {
                Thread.currentThread().sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
