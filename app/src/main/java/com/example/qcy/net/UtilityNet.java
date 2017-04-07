package com.example.qcy.net;

import android.text.TextUtils;

import com.example.qcy.com.example.qcy.util.AllUtil;
import com.example.qcy.javabean.City;
import com.example.qcy.javabean.Country;
import com.example.qcy.javabean.Province;
import com.example.qcy.javabean.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by qcy on 2017/3/26.
 * 网络数据层：负责网络数据的请求，及管理
 */

public class UtilityNet {
    private static String TAG = "UtilityNet";
    /**
     * 将返回的json数据解析成weather实体类
     */

    public static Weather handleWeatherResponse(String response) {
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
     *
     * @param response
     * @return 是否请求到该省的数据
     */
    private static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++) {
                    JSONObject provinceObject  = allProvince.getJSONObject(i);
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
     *
     * @param response
     * @param provinceId
     * @return 是否请求到city的数据
     */
    private static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++) {
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
     */
    protected static boolean handleCountryResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCountrys = new JSONArray(response);
                for (int i = 0; i < allCountrys.length(); i++) {
                    JSONObject jsonObject = allCountrys.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryName(jsonObject.getString("name"));
                    country.setCityId(cityId);
                    country.setWeatherId(jsonObject.getString("weather_id"));
                    country.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 查詢城市數，并解析
     *
     * @param provinceCode
     * @param selectedProvinceId
     */
    public static List<City> queryCity(int provinceCode, MCallBack mCallBack, int selectedProvinceId) {
        List<City> mCityList = new ArrayList<City>();
        mCityList.clear();
        mCityList.addAll(DataSupport.where("provinceid = ?",
                String.valueOf(selectedProvinceId)).find(City.class));
        if (mCityList.isEmpty()){
            String address = "http://guolin.tech/api/china/"+provinceCode;
            String mType = "city";
            queryFromServer(address, mCallBack, mType, selectedProvinceId, 0);
        }

        return mCityList;
    }

    /**
     * 查詢鄉村數據，并解析
     *  @param provinceCode
     * @param cityCode
     * @param selectedCityId
     */
    public static List<Country> queryCountries(int provinceCode, int cityCode,
                                              MCallBack mCallBack, int selectedCityId) {
        List<Country> mCountryList = new ArrayList<Country>();
        mCountryList.clear();
        mCountryList.addAll(DataSupport.where("cityid = ?", String.valueOf(selectedCityId))
                .find(Country.class));
        if (mCountryList.isEmpty()) {
            String address = "http://guolin.tech/api/china/"
                    +provinceCode+"/"+cityCode;
            String mType = "country";
            queryFromServer(address, mCallBack, mType, 0, selectedCityId);
        }
        return mCountryList;
    }

    /**
     * 查詢省 數據，并解析，以上三個查詢全部優先向本地數據庫進行查詢，if null 在向服務器進行查詢
     */
    public static List<Province> queryProvinces(MCallBack mCallBack) {
        List<Province> mProvinceList = new ArrayList<Province>();
        mProvinceList.addAll(DataSupport.findAll(Province.class));
        if (mProvinceList.isEmpty()) {
            String address = "http://guolin.tech/api/china";
            String mType = "province";
            queryFromServer(address, mCallBack, mType, 0, 0);
        }
        return mProvinceList;
    }

    /**
     * 向服务器请求数据
     * @param address
     * @param mCallBack
     * @param mType
     * @param selectedCityId
     */
    private static void queryFromServer(String address, final MCallBack mCallBack, final String mType,
                                        final int selectedProvinceId, final int selectedCityId) {
        if (mCallBack != null){
            mCallBack.beginQuery();
        }
        AllUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mCallBack != null){
                    mCallBack.failure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(mType)) {
                    result = handleProvinceResponse(responseText);
                } else if ("city".equals(mType)) {
                    result = handleCityResponse(responseText, selectedProvinceId);
                } else if ("country".equals(mType)) {
                    result = handleCountryResponse(responseText, selectedCityId);
                }
                if (result) {
                    //睡眠两秒
                    try {
                        Thread.sleep(200l);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    int currentLever = 0;
                    if (mCallBack != null){
                        if ("province".equals(mType)) {
                            currentLever =0;
                        } else if ("city".equals(mType)) {
                            currentLever =1;
                        } else if ("country".equals(mType)) {
                            currentLever =2;
                        }
                        mCallBack.setMCurrentLever(currentLever);
                        mCallBack.response();
                    }
                } else {
                    if (mCallBack != null){
                        mCallBack.failure();
                    }
                }
            }
        });
    }

    /**
     * 以weatheId作为参数向服务器请求天气数据
     * @param weatherId
     * @param mCallBack
     */
    //TODO weatherId还没有设置
    public static void requestWeather(String weatherId, final MCallBack mCallBack) {
//        String wearherStr = "http://guolin.tech/api/weather?cityid=CN101190401&key=d0c8c897ecdf4efdac93b9dc14caac81";
        String wearherStr = "http://guolin.tech/api/weather?cityid="+weatherId+
                "&key=d0c8c897ecdf4efdac93b9dc14caac81";
        AllUtil.logUtil(TAG, AllUtil.DUBUG_LEVER, wearherStr);
        if (mCallBack != null) {
            mCallBack.beginQuery();
        }
        AllUtil.sendOkHttpRequest(wearherStr, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mCallBack != null) {
                    mCallBack.failure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String responseStr = response.body().string();
                Weather weather = handleWeatherResponse(responseStr);
                AllUtil.logUtil(TAG, AllUtil.DUBUG_LEVER, "weather.status=="+weather.status+
                "responseStr"+responseStr);
                if (!TextUtils.isEmpty(responseStr) && "ok".equals(weather.status)){
                    AllUtil.saveDateBySP("weather", responseStr);
                    if (mCallBack != null) {
                        mCallBack.response(weather);
                    }
                } else {
                    if (mCallBack != null) {
                        mCallBack.failure();
                    }
                }
            }
        });
    }

    /**
     * 请求网络图片
     * @param loadPicCallBack
     * @param address
     */
    public static void loadBingPic(final MCallBack loadPicCallBack, String address) {
        AllUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (loadPicCallBack != null) {
                    loadPicCallBack.failure();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String picAddress = response.body().string();
                AllUtil.saveDateBySP("bing_pic", picAddress);
                if (loadPicCallBack != null) {
                    loadPicCallBack.response(picAddress);
                }
            }
        });
    }

    /**
     * 请求之后，对界面进行刷新操作的回调函数
     */
    public interface MCallBack {
        //开始请求
        void beginQuery();

        //请求成功
        void response();

        //请求失败
        void failure();

        //结束请求
        void endQuery();

        void setMCurrentLever(int i);

        void response(Object parma);
    }
}
