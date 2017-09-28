package com.supers.clean.junk.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.fraumobi.call.Utils.BadgerCount;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.wifi.RunAppInfo;
import com.supers.clean.junk.wifi.RunAppManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/4/18.
 */

public class NetMonitor extends BaseActivity implements RunAppManager.LoadListener {
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private Context mContext;
    private LinearLayout ll_netmon_main, ll_netmon_unconnect, ll_netmon_none;
    FrameLayout ll_netmon_list;
    Button junk_button_clean;
    private ListView lv_netmon;
    private TextView tv_netmon_ssid, tv_netmon_up, tv_netmon_down;
    private FrameLayout title_left;
    private ProgressBar iv_empty;
    private List<RunAppInfo> mRunAppInfoList = new ArrayList<>();
    Handler mHandler;
    private long lastTotalRxBytes = 0; // 最后缓存的字节数
    private long lastTotalTxBytes = 0;
    private long lastTimeStamp = 0; // 当前缓存时间
    private MyAdapter mAdapter;
    LinearLayout ll_ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_netmonitor);
        PreData.putDB(this, Constant.HONG_NET, false);
        BadgerCount.setCount(this);
        mContext = this;
        findId();

        mRunAppInfoList.clear();
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.removeCallbacks(runnableW);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (isDataConnected(mContext)) {
            ll_netmon_unconnect.setVisibility(View.GONE);
            ll_netmon_main.setVisibility(View.VISIBLE);
            ll_netmon_list.setVisibility(View.VISIBLE);
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            tv_netmon_ssid.setText(tm.getSimOperatorName());
            tv_netmon_up.setText("0 KB/s");
            tv_netmon_down.setText("0 KB/s");
            mAdapter = new MyAdapter(mContext, mRunAppInfoList);
            lv_netmon.setAdapter(mAdapter);
            lv_netmon.setEmptyView(iv_empty);


            mHandler.post(runnableW);
        }
        if (isWifiConnected(mContext)) {
            ll_netmon_unconnect.setVisibility(View.GONE);
            ll_netmon_main.setVisibility(View.VISIBLE);
            ll_netmon_list.setVisibility(View.VISIBLE);
            mWifiInfo = mWifiManager.getConnectionInfo();
            tv_netmon_ssid.setText(mWifiInfo.getSSID().replaceAll("\"", ""));
            tv_netmon_up.setText("0 KB/s");
            tv_netmon_down.setText("0 KB/s");
            mAdapter = new MyAdapter(mContext, mRunAppInfoList);
            lv_netmon.setAdapter(mAdapter);
            lv_netmon.setEmptyView(iv_empty);
            mHandler.post(runnableW);

        }
        junk_button_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NetMonitor.this, RamAvtivity.class);
                startActivity(intent);
                finish();
            }
        });
        addAd();
    }

    private void addAd() {
        View native_xiao = AdUtil.getNativeAdView("", R.layout.native_ad_3);
        if (ll_ad != null && native_xiao != null) {
            ll_ad.addView(native_xiao);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }


    public void findId() {
        ll_netmon_none = (LinearLayout) findViewById(R.id.ll_netmon_none);
        ll_netmon_unconnect = (LinearLayout) findViewById(R.id.ll_netmon_unconnect);
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        ll_netmon_main = (LinearLayout) findViewById(R.id.ll_netmon_main);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ll_netmon_list = (FrameLayout) findViewById(R.id.ll_netmon_list);
        lv_netmon = (ListView) findViewById(R.id.lv_netmon);
        tv_netmon_ssid = (TextView) findViewById(R.id.tv_netmon_ssid);
        tv_netmon_up = (TextView) findViewById(R.id.tv_netmon_up);
        tv_netmon_down = (TextView) findViewById(R.id.tv_netmon_down);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        iv_empty = (ProgressBar) findViewById(R.id.iv_empty);
    }

    private long downSpeed;
    private long upSpeed;
    private Runnable runnableW = new Runnable() {
        public void run() {
            long nowTotalRxBytes = (TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                    : (TrafficStats.getTotalRxBytes())); // 获取当前数据总量
            long nowTotalTxBytes = (TrafficStats.getUidTxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
                    : (TrafficStats.getTotalTxBytes())); // 获取当前数据总量
            long nowTimeStamp = System.currentTimeMillis(); // 当前时间
            // kb/s
            downSpeed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp == lastTimeStamp ? nowTimeStamp : nowTimeStamp
                    - lastTimeStamp));// 毫秒转换
            upSpeed = ((nowTotalTxBytes - lastTotalTxBytes) * 1000 / (nowTimeStamp == lastTimeStamp ? nowTimeStamp : nowTimeStamp
                    - lastTimeStamp));
            tv_netmon_down.setText(Util.convertStorageWifi(downSpeed));
            tv_netmon_up.setText(Util.convertStorageWifi(upSpeed));
            lastTotalRxBytes = nowTotalRxBytes;
            lastTotalTxBytes = nowTotalTxBytes;
            lastTimeStamp = nowTimeStamp;
            RunAppManager.getInstance(NetMonitor.this).loadRunAppInfo();
            mHandler.postDelayed(this, 5000);
        }
    };

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isDataConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getTypeName().toString().equals("MOBILE")) {
            return true;

        } else if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        RunAppManager.getInstance(this).setLoadListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RunAppManager.getInstance(this).removeLoadListener();
    }

    @Override
    public void loadFinish(final List<RunAppInfo> lastRunAppInfoList, boolean isNon) {
        if (lastRunAppInfoList.size() == 0) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    iv_empty.setVisibility(View.GONE);
                    ll_netmon_none.setVisibility(View.VISIBLE);
                }
            });
            return;
        }
        if (isWifiConnected(mContext)) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mRunAppInfoList.clear();
                    lastRunAppInfoList.get(0).setUpspeed(Util.convertStorageWifi(upSpeed));
                    lastRunAppInfoList.get(0).setDownspeed(Util.convertStorageWifi(downSpeed));
                    mRunAppInfoList.addAll(lastRunAppInfoList);
                    iv_empty.setVisibility(View.GONE);
                    mAdapter.refresh(mRunAppInfoList);
                }
            });
        }
    }

    public class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;

        List<RunAppInfo> list;

        public MyAdapter(Context context, List<RunAppInfo> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        public void refresh(List<RunAppInfo> l) {
            list = l;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"ViewHolder", "InflateParams"})
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.netmon_item, null);
                holder.iv_netmonitem_icon = (ImageView) convertView.findViewById(R.id.iv_netmonitem_icon);
                holder.tv_netmonitem_name = (TextView) convertView.findViewById(R.id.tv_netmonitem_name);
                holder.tv_netmonitem_down = (TextView) convertView.findViewById(R.id.tv_netmonitem_down);
                holder.tv_netmonitem_up = (TextView) convertView.findViewById(R.id.tv_netmonitem_up);
                holder.bt_netmonitem_stop = (ImageView) convertView.findViewById(R.id.bt_netmonitem_stop);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final RunAppInfo appInfo = list.get(position);
            holder.iv_netmonitem_icon.setImageDrawable(appInfo.getAppIcon());
            holder.tv_netmonitem_name.setText(appInfo.getAppLabel());
            holder.tv_netmonitem_down.setText(appInfo.getDownspeed());
            holder.tv_netmonitem_up.setText(appInfo.getUpspeed());
            if (appInfo.getChecked()) {
                holder.bt_netmonitem_stop.setImageResource(R.mipmap.ram_passed);
            } else {
                holder.bt_netmonitem_stop.setImageResource(R.mipmap.ram_normal);
            }
            holder.bt_netmonitem_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appInfo.setChecked(!appInfo.getChecked());
                    if (appInfo.getChecked()) {
                        holder.bt_netmonitem_stop.setImageResource(R.mipmap.ram_passed);
                    } else {
                        holder.bt_netmonitem_stop.setImageResource(R.mipmap.ram_normal);
                    }

                }
            });
            return convertView;
        }

        public class ViewHolder {
            ImageView iv_netmonitem_icon;
            TextView tv_netmonitem_name, tv_netmonitem_down, tv_netmonitem_up;
            ImageView bt_netmonitem_stop;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //RunAppManager.getInstance(this).removeLoadListener();
        mHandler.removeCallbacks(runnableW);
    }
}
