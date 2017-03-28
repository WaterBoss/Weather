package com.example.qcy.net;

import android.text.TextUtils;

import com.example.qcy.javabean.City;
import com.example.qcy.javabean.Country;
import com.example.qcy.javabean.Province;
import com.example.qcy.javabean.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qcy on 2017/3/26.
 * 网络数据层：负责网络数据的请求，及管理
 */

public  class UtilityNet {

    /**
     * 将返回的json数据解析成weather实体类
     */

    private Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将返回的数据进行存储，并返回一个boolEAN类型的值
     * @param response
     * @return 是否请求到该省的数据
     */
    private boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++){
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.setProvinceName(provinceObject.getString("name"));
                    province.save();
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 存储，管理city数据
     * @param response
     * @return 是否请求到city的数据
     */
    private boolean handleCityResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++){
                    JSONObject jsonObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(jsonObject.getString("name"));
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 存储，管理country数据
     *
     */
    private boolean handleCountryResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                    JSONArray allCountrys = new JSONArray(response);
                    for (int i = 0 ; i < allCountrys.length(); i++) {
                        JSONObject jsonObject = allCountrys.getJSONObject(i);
                        Country country = new Country();
                        country.setCountryName(jsonObject.getString("name"));
                        country.setCityId(cityId);
                        country.setWeatherId(jsonObject.getString("weather_id");
                        country.save();
                     }
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return false;
    }

}
