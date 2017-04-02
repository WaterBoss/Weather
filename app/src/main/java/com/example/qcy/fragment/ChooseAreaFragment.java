package com.example.qcy.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qcy.com.example.qcy.util.AllUtil;
import com.example.qcy.javabean.City;
import com.example.qcy.javabean.Country;
import com.example.qcy.javabean.Province;
import com.example.qcy.net.UtilityNet;
import com.example.qcy.weather.R;

import java.util.List;

import static com.example.qcy.net.UtilityNet.queryProvinces;

/**
 * 左侧城市选择菜单的fragment
 */
public class ChooseAreaFragment extends Fragment {
    private static final int LEVER_PROVINCE = 0;
    private static final int LEVER_CITY = 1;
    private static final int LEVER_COUNTRY = 2;
    private static final String TAG= "ChooseAreaFragment";


    private TextView mTitleText;
    private Button mBackButton;
    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<Country> mCountryList;
    private Province mSelectdProvince;
    private int mCurrentLever;
    private RecyclerView mReclView;
    private City mSelectdCity;
    private RecyclerView.Adapter<MyViewHolder> mAdapter;
    private MCallBack mCallBack;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        mTitleText = (TextView) view.findViewById(R.id.title_tv);
        mBackButton = (Button) view.findViewById(R.id.back_button);
        mReclView = (RecyclerView) view.findViewById(R.id.citylist_recyView);
        mCallBack = new MCallBack();
        mAdapter = new MyAdapter();
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity());
        linearlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mReclView.setLayoutManager(linearlayoutManager);
        mReclView.setAdapter(mAdapter);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentLever == LEVER_COUNTRY) {
                    isCity();
                } else if (mCurrentLever == LEVER_CITY) {
                    isProvinces();
                }
            }
        });
        isProvinces();
        return view;
    }

    /**
     * 點擊鄉村項
     */
    private void isCountries() {
        mTitleText.setText(mSelectdCity.getCityName());
        mBackButton.setVisibility(View.VISIBLE);
        mCountryList = UtilityNet.queryCountries(mSelectdProvince.getProvinceCode(),
                mSelectdCity.getCityCode(), mCallBack, mSelectdCity.getId());
        if (!mCountryList.isEmpty()) {
            mCurrentLever = LEVER_COUNTRY;
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 點擊的是城市單項后，對城市天氣數據進行查詢并更新
     */
    private void isCity() {
        mTitleText.setText(mSelectdProvince.getProvinceName());
        mBackButton.setVisibility(View.VISIBLE);
        mCityList = UtilityNet.queryCity(mSelectdProvince.getProvinceCode(), mCallBack, mSelectdProvince.getId());
        if (!mCityList.isEmpty()) {
            mCurrentLever = LEVER_CITY;
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 點擊省項
     */
    private void isProvinces() {
        mTitleText.setText(R.string.china);
        mBackButton.setVisibility(View.GONE);
        mProvinceList = queryProvinces(mCallBack);
        if (!mProvinceList.isEmpty()) {
            mCurrentLever = LEVER_PROVINCE;
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * recycleview的viewholder
     */
    private static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
//            linearLayout = (LinearLayout) itemView;
//            mTextView = (TextView) linearLayout.findViewById(R.id.item_textview);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LinearLayout linearLayout = new LinearLayout(getActivity());
            LayoutParams layoutParms = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//            linearLayout.setLayoutParams(layoutParms);
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 10, 0, 10);
            textView.setLayoutParams(layoutParms);
            textView.setTextSize(25);
            textView.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
            textView.setId(R.id.item_textview);
//            linearLayout.addView(textView);
//            MyViewHolder viewHolder = new MyViewHolder(linearLayout);
            MyViewHolder viewHolder = new MyViewHolder(textView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            String itemStr = "";
            if (mCurrentLever == LEVER_PROVINCE) {
                itemStr = mProvinceList.get(position).getProvinceName();
            } else if (mCurrentLever == LEVER_CITY) {
                itemStr = mCityList.get(position).getCityName();
            } else if (mCurrentLever == LEVER_COUNTRY){
                itemStr = mCountryList.get(position).getCountryName();
            }
            holder.mTextView.setText(itemStr);
            AllUtil.logUtil(TAG, AllUtil.DUBUG_LEVER,
                    "adapter bind data"+itemStr+"lever===="+mCurrentLever);
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurrentLever == LEVER_PROVINCE) {
                        mSelectdProvince = mProvinceList.get(position);
                        isCity();
                    } else if (mCurrentLever == LEVER_CITY) {
                        mSelectdCity = mCityList.get(position);
                        isCountries();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            int size = 0;
            if (mCurrentLever == LEVER_PROVINCE) {
                size = mProvinceList.size();
            } else if (mCurrentLever == LEVER_CITY) {
                size = mCityList.size();
            } else if (mCurrentLever == LEVER_COUNTRY){
                size = mCountryList.size();
            }
            return size;
        }
    }

    //实现请求之后的回调接口,主要是对界面刷新的操作
    private class MCallBack implements UtilityNet.MCallBack{

        @Override
        public void beginQuery() {
            showProgressDialog();
        }

        @Override
        public void response() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    closeProgressDialog();
                    if (mCurrentLever == LEVER_PROVINCE) {
                        mProvinceList = UtilityNet.queryProvinces(null);
                    } else if (mCurrentLever == LEVER_CITY) {
                        mCityList = UtilityNet.queryCity(0, null, mSelectdProvince.getId());
                    } else if (mCurrentLever == LEVER_COUNTRY) {
                        mCountryList = UtilityNet.queryCountries(mSelectdProvince.getProvinceCode(),
                                mSelectdCity.getCityCode(), null, mSelectdCity.getId());
                    }
                        mAdapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public void failure() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    closeProgressDialog();
                    Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void endQuery() {
            closeProgressDialog();
            Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void setMCurrentLever(int currentLever) {
            mCurrentLever = currentLever;
        }
    }

    /**
     * 打开进度条
     */
    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载。。。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度条
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
