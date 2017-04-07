package com.example.qcy.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.qcy.com.example.qcy.util.AllUtil;
import com.example.qcy.net.UtilityNet;
import com.example.qcy.net.UtilityNet.MCallBack;


public class AutoUpdateWeatherDateService extends Service {
    public static String TAG = "AutoUpdateWeatherDateService";
    private AlarmManager manager;
    private PendingIntent pIntent;
    private long triggerAtTime;

    public AutoUpdateWeatherDateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBackgrountImage();
        manager.cancel(pIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);
        AllUtil.logUtil(TAG, AllUtil.DUBUG_LEVER, "onStartCommand（）执行");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AllUtil.logUtil(TAG, AllUtil.DUBUG_LEVER, "onCreate() 执行");
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000;//每次更新数据的间隔时长，8小时，单位毫秒
        triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateWeatherDateService.class);
        pIntent = PendingIntent.getService(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 更新背景图片
     */
    private void updateBackgrountImage() {
        String address = "http://guolin.tech/api/bing_pic";
        UtilityNet.loadBingPic(null, address);
    }

    /**
     * 更新天气数据
     */
    public void updateWeather() {
        UtilityNet.requestWeather(String.valueOf(AllUtil.getSpDate("weatherId")), new MCallBack() {
            @Override
            public void beginQuery() {

            }

            @Override
            public void response() {

            }

            @Override
            public void failure() {

            }

            @Override
            public void endQuery() {

            }

            @Override
            public void setMCurrentLever(int i) {

            }

            @Override
            public void response(final Object parma) {
                AllUtil.saveDateBySP("weatherInfoIfUpDate", true);
//                WeatherActivity.toastText("天气数据已更新");
                AllUtil.logUtil(TAG, AllUtil.DUBUG_LEVER, "天气数据已更新！");
            }
        });
    }
}
