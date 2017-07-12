package com.icleaner.junk.mycustomview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icleaner.junk.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Lody
 */
public class MyLaunchpadAdapter extends PagerAdapter<String> {

    private static final int LOADING_ANIMAL_TIME = 5000;

    private FrameLayout parentView;

    private int currentViewTag = 0;

    public static final int ANIMATION_DURATION = 500;

    public Context context;

    private Handler mHandler;

    private boolean onPause;

    private boolean initSuccess;

    public int interval_time = DEFAULT_INTERVAL_TIME;

    public static final int MSG_START_AD_ANIMAL = 100;

    public static final int DEFAULT_INTERVAL_TIME = 5000;

    public HashMap<Integer, View> nativeAdMap;
    private PackageManager pm;

    public void addNativeAdToMap(Integer key, View view) {
        if (nativeAdMap == null) {
            nativeAdMap = new HashMap<>();
        }
        nativeAdMap.put(key, view);
    }

    public MyLaunchpadAdapter(Context context, ArrayList<String> list) {
        // noinspection unchecked
        super(context, list);
        this.context = context;
        pm = context.getPackageManager();
    }


    @Override
    public int getItemLayoutId(int position, String appModel) {
        return R.layout.layout_gboost_item;
    }

    @Override
    public void onBindView(View view, String junkInfo) {

        LinearLayout linearlayou_add = (LinearLayout) view.findViewById(R.id.linearlayou_add);
        LinearLayout linearlayout_qita = (LinearLayout) view.findViewById(R.id.linearlayout_qita);
        ImageView gboost_item_icon = (ImageView) view.findViewById(R.id.gboost_item_icon);
        ImageView gboost_item_add = (ImageView) view.findViewById(R.id.gboost_item_add);
        TextView gboost_item_name = (TextView) view.findViewById(R.id.gboost_item_name);
        TextView gboost_item_addname = (TextView) view.findViewById(R.id.gboost_item_addname);
        if (TextUtils.equals(context.getString(R.string.gboost_7), junkInfo)) {
            linearlayout_qita.setVisibility(View.GONE);
            gboost_item_add.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.gboost_add));
            gboost_item_addname.setText(junkInfo);
        } else {
            try {
                linearlayou_add.setVisibility(View.GONE);
                ApplicationInfo info = pm.getApplicationInfo(junkInfo, 0);
                gboost_item_icon.setImageDrawable(info.loadIcon(pm));
                gboost_item_name.setText(info.loadLabel(pm));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
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

    public String currentTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public boolean isSameDayClickAd(String adType) {
        SharedPreferences sp = context.getSharedPreferences("home_activity", Context.MODE_PRIVATE);
        return TextUtils.equals(currentTimeString(), sp.getString(adType, ""));
    }

    public void saveCurrentClickAdTime(String adType) {
        SharedPreferences sp = context.getSharedPreferences("home_activity", Context.MODE_PRIVATE);
        sp.edit().putString(adType, currentTimeString()).apply();
    }

}

