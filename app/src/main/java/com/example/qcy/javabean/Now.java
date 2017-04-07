package com.example.qcy.javabean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by qcy on 2017/4/2.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
