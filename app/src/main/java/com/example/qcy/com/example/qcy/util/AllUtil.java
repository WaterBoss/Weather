package com.example.qcy.com.example.qcy.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.qcy.net.UtilityNet;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    private static ProgressDialog progressDialog;

    public static void sendOkHttpRequest(String address, okhttp3.Callback callBack) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callBack);
    }
    public static void sendOkHttpRequestByMCallBack(String address, final UtilityNet.MCallBack mCallBack) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallBack.failure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mCallBack.response();
            }
        });
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

    /**
     * 打开进度条
     * @param activity
     */
    public static void showProgressDialog(Context activity) {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("正在加载。。。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度条
     */
    public static void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
    public static void saveDateBySP(String nameStr, String dateStr) {
        if (dateStr == null){
            return;
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                (MyApplication.getInstance().getContext()).edit();
        editor.putString(nameStr, dateStr);
        editor.apply();
    }
    public static void saveDateBySP(String nameStr, Boolean dateBoolean){
        if (dateBoolean == null){
            return;
        }
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                (MyApplication.getInstance().getContext()).edit();
        editor.putBoolean(nameStr, dateBoolean);
        editor.apply();
    }

    public static String getSpDate(String weatherId) {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance().getContext())
                .getString(weatherId, null);
    }

    public static boolean getSpBooleanDate(String weatherId) {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance().getContext())
                .getBoolean(weatherId, false);
    }
}
