package com.example.admin.a2017718;

/**
 * Created by admin on 2017/7/21.
 */

public class movies {

    private String id;
    private String imageurl;
    private String name;
    private String pay;


    public movies(String id, String pay, String name, String imageurl) {
        this.id = id;
        this.imageurl = imageurl;
        this.name = name;
        this.pay = pay;
    }


    public String getId() {

        return this.id;

    }

    public String getName() {

        return this.name;

    }

    public String getImageurl() {
        return this.imageurl;

    }

    public String getPay() {
        return this.pay;


    }


}
