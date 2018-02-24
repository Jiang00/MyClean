package com.upupup.module.charge.saver;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.upupup.clean.core.CleanManager;
import com.upupup.clean.entity.JunkInfo;
import com.upupup.clean.util.CleanConstant;
import com.upupup.clean.util.PreData;
import com.upupup.module.charge.saver.Util.Constants;
import com.upupup.module.charge.saver.Util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/7/5.
 */

public class DetectActivity extends Activity {
    ImageView detect_cha;
    TextView detect_zhuangtai, detect_time, detect_baifen, detect_shiyong;
    TextView detect_ram;
    Button detect_clean;
    LinearLayout detect_ad;
    private String TAG_DETECT = "clean_native";
    private ImageView icon;
    private ImageView battery_iv;
    private FrameLayout battery_fl;

    private void findId() {
        detect_cha = (ImageView) findViewById(R.id.detect_cha);
        battery_iv = (ImageView) findViewById(R.id.battery_iv);
        icon = (ImageView) findViewById(R.id.icon);
        detect_zhuangtai = (TextView) findViewById(R.id.detect_zhuangtai);
        detect_time = (TextView) findViewById(R.id.detect_time);
        detect_baifen = (TextView) findViewById(R.id.detect_baifen);
        detect_shiyong = (TextView) findViewById(R.id.detect_shiyong);
        detect_ram = (TextView) findViewById(R.id.detect_ram);
        detect_ad = (LinearLayout) findViewById(R.id.detect_ad);
        detect_clean = (Button) findViewById(R.id.detect_clean);
        battery_fl = (FrameLayout) findViewById(R.id.battery_fl);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        setContentView(R.layout.layout_battery_r);
        findId();
        int iconId = (int) Utils.readData(this, Constants.CHARGE_SAVER_ICON, R.mipmap.battery_inner_icon);
        if (iconId > -1) {
            icon.setImageResource(iconId);
        }
        long chongdian_time = PreData.getDB(this, CleanConstant.CONNECTED_TIME_LUN, 60 * 60 * 1000l);
        long use_time = PreData.getDB(this, CleanConstant.CONNECTED_LEFT_TIME_LUN, 60 * 60 * 1000l);
        int level = PreData.getDB(this, CleanConstant.CONNECTED_LEVEL_LUN, 100);
        int zhuangtai = PreData.getDB(this, CleanConstant.CONNECTED_ZZ, 0);
        detect_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.upupup.clean.CleanActivity").putExtra("from", "notifi"));
                finish();
            }
        });
        detect_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (zhuangtai == 0) {
            detect_zhuangtai.setText(R.string.detect_2);
            detect_zhuangtai.setTextColor(ContextCompat.getColor(this, R.color.battery_normal));
        } else if (zhuangtai == 1) {
            detect_zhuangtai.setText(R.string.detect_2);
            detect_zhuangtai.setTextColor(ContextCompat.getColor(this, R.color.battery_normal));
        } else {
            detect_zhuangtai.setText(R.string.detect_3);
            detect_zhuangtai.setTextColor(ContextCompat.getColor(this, R.color.battery_excessive));
        }
        detect_time.setText(millTransFate(chongdian_time));
        detect_shiyong.setText(millTransFate(use_time));
        detect_baifen.setText(level + "%");
        int count = 0;
//        addAd();
        List<JunkInfo> list = new ArrayList<>();
        list.addAll(CleanManager.getInstance(this).getAppRamList());
        if (list.size() == 0) {
            return;
        }
//        for (JunkInfo info : list) {
//            ImageView imageView = new ImageView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.dp2px(15), Util.dp2px(15));
//            layoutParams.rightMargin = Util.dp2px(1);
//            imageView.setLayoutParams(layoutParams);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            if (count == 4) {
//                imageView.setImageResource(R.mipmap.detect_ram);
//                detect_ram.addView(imageView, 0);
//                break;
//            }
//            if (LoadManager.getInstance(this).getAppIcon(info.pkg) != null) {
//                imageView.setImageDrawable(LoadManager.getInstance(this).getAppIcon(info.pkg));
//                detect_ram.addView(imageView, 0);
//                count++;
//            }
//        }

        detect_ram.setText(list.size() + "s");


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        battery_iv.setBackground(getResources().getDrawable(R.mipmap.backage));
        ViewGroup.LayoutParams params = battery_iv.getLayoutParams();

        params.height = battery_fl.getHeight();
        params.width = battery_fl.getWidth();
        battery_iv.setLayoutParams(params);
    }


    private void addAd() {
        View nativeView = getNativeAdView(TAG_DETECT, R.layout.native_ad1);
        if (detect_ad != null && nativeView != null) {
            detect_ad.addView(nativeView);
        }
    }

    public View getNativeAdView(String tag, @LayoutRes int layout) {
        if (PreData.getDB(this.getApplicationContext(), CleanConstant.BILL_YOUXIAO, true)) {
            return null;
        }
        if (!AndroidSdk.hasNativeAd(tag)) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout(tag, layout, null);
        if (nativeView == null) {
            return null;
        }

        if (nativeView != null) {
            ViewGroup viewParent = (ViewGroup) nativeView.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
            }
        }
        return nativeView;
    }

    //多少天
    public static String millTransFate(long millisecond) {
        String str = "";
        long day = millisecond / 86400000;
        long hour = (millisecond % 86400000) / 3600000;
        long minute = (millisecond % 86400000 % 3600000) / 60000;
        if (day > 0) {
            str = String.valueOf(day) + "d";
        }
        if (hour > 0) {
            str += String.valueOf(hour) + "h ";
        }
        if (minute > 0) {
            str += String.valueOf(minute) + "min";
        }
        return str;
    }

    protected void hideBottomUIMenu() {
        try {
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
                View v = this.getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception e) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}
