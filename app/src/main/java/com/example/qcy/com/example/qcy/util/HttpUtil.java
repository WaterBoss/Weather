package com.example.qcy.com.example.qcy.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by qcy on 2017/3/27.
 * http请求工具类
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callBack){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callBack);
    }
}
