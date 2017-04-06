package com.example.qcy.javabean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qcy on 2017/4/2.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
