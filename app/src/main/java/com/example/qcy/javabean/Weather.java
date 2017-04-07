package com.example.qcy.javabean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by qcy on 2017/3/26.
 * 天气数据实体类
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
