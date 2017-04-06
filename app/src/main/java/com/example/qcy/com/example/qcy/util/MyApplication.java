package com.example.qcy.com.example.qcy.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by qcy on 2017/4/4.
 */
public class MyApplication extends Application{

    private static MyApplication mInstance;
    private Activity mActivity;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = this;
        LitePalApplication.initialize(mContext);

    }
    public Context getContext() {
        return mContext;
    }

    public static MyApplication getInstance(){
        return mInstance;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }
}
