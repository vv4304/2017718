package com.example.admin.a2017718;

import android.app.Application;
import android.content.SharedPreferences;

import java.util.Random;

/**
 * Created by admin on 2017/10/28.
 */

public class calculate {

    public int sign_up() {
        Random random = new Random();
        return random.nextInt(5);
    }

    public static int random() {
        //50%*5   35%*30  10%*50  5%*100
        Random random = new Random();
        Random random1 = new Random();
        int i = random.nextInt(10000);
        int o;
        if (i < 500) {
            o = random1.nextInt(101);
        } else if (i < 1000) {
            o = random1.nextInt(51);
        } else if (i < 3500) {
            o = random1.nextInt(31);
        } else {
            o = random1.nextInt(6);
        }
        System.out.println(i + " " + o);
        return o;
    }


}
