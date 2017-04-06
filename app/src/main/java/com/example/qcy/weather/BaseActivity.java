package com.example.qcy.weather;


import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.example.qcy.com.example.qcy.util.AllUtil;

/**
 * Created by qcy on 2017/4/5.
 */

class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (TextUtils.isEmpty((CharSequence) AllUtil.getSpDate("weatherId"))) {
            String defaultWeatherId = "CN101190401";
            AllUtil.saveDateBySP("weatherId", defaultWeatherId);
        }
    }
}
