package com.supers.clean.junk.customeview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.entity.JunkInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Lody
 */
public class LaunchpadAdapter extends PagerAdapter<JunkInfo> {

    private static final int LOADING_ANIMAL_TIME = 5000;


    private FrameLayout parentView;

    private int currentViewTag = 0;

    public static final int ANIMATION_DURATION = 500;

    public Context context;

    private Handler mHandler;

    private boolean onPause;

    private boolean initSuccess;

    public static final int MSG_START_AD_ANIMAL = 100;

    public static final int DEFAULT_INTERVAL_TIME = 5000;

    public int interval_time = DEFAULT_INTERVAL_TIME;

    public HashMap<Integer, View> nativeAdMap;

    public void addNativeAdToMap(Integer key, View view) {
        if (nativeAdMap == null) {
            nativeAdMap = new HashMap<>();
        }
        nativeAdMap.put(key, view);
    }

    public LaunchpadAdapter(Context context, ArrayList<JunkInfo> list) {
        // noinspection unchecked
        super(context, list);
        this.context = context;


    }


    @Override
    public int getItemLayoutId(int position, JunkInfo appModel) {
        return R.layout.layout_gboost_item;
    }

    @Override
    public void onBindView(View view, JunkInfo junkInfo) {
        ImageView gboost_item_icon = (ImageView) view.findViewById(R.id.gboost_item_icon);
        TextView gboost_item_name = (TextView) view.findViewById(R.id.gboost_item_name);
        gboost_item_icon.setImageDrawable(junkInfo.icon);
        gboost_item_name.setText(junkInfo.label);
    }

    public void onPause() {
        onPause = true;
        if (mHandler.hasMessages(MSG_START_AD_ANIMAL)) {
            mHandler.removeMessages(MSG_START_AD_ANIMAL);
        }
    }

    public void onResume() {
        onPause = false;
        if (initSuccess) {
            mHandler.removeMessages(MSG_START_AD_ANIMAL);
            mHandler.sendEmptyMessageDelayed(MSG_START_AD_ANIMAL, DEFAULT_INTERVAL_TIME);
        }
    }

    public boolean isSameDayClickAd(String adType) {
        SharedPreferences sp = context.getSharedPreferences("home_activity", Context.MODE_PRIVATE);
        return TextUtils.equals(currentTimeString(), sp.getString(adType, ""));
    }

    public void saveCurrentClickAdTime(String adType) {
        SharedPreferences sp = context.getSharedPreferences("home_activity", Context.MODE_PRIVATE);
        sp.edit().putString(adType, currentTimeString()).apply();
    }

    public String currentTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}

