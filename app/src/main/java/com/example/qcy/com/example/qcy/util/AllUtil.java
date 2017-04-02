package com.example.qcy.com.example.qcy.util;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by qcy on 2017/3/27.
 * http请求工具类
 */

public class AllUtil {
    public static final int INFO_LEVER = 0;
    public static final int VERBOSE_LEVER = 1;
    public static final int DUBUG_LEVER = 2;
    public static final int WARN_LEVER = 3;
    public static final int ERROR_LEVER = 4;
    private static final boolean LOG_SWITCH = true;  //日志开关

    public static void sendOkHttpRequest(String address, okhttp3.Callback callBack) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callBack);
    }

    public static void logUtil(String TAG, int lever, String msg) {
        if (!LOG_SWITCH) {
            return;
        }
        switch (lever) {
            case 0:
                Log.i(TAG, msg);
                break;
            case 1:
                Log.v(TAG, msg);
                break;
            case 2:
                Log.d(TAG, msg);
                break;
            case 3:
                Log.w(TAG, msg);
                break;
            case 4:
                Log.e(TAG, msg);
                break;
        }

    }
}
