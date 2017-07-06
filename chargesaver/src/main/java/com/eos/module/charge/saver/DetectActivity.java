package com.eos.module.charge.saver;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.entity.JunkInfo;
import com.android.clean.util.LoadManager;
import com.android.clean.util.Util;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.DetectData;

/**
 * Created by Ivy on 2017/7/5.
 */

public class DetectActivity extends Activity {
    ImageView detect_cha;
    TextView detect_zhuangtai, detect_time, detect_baifen, detect_shiyong;
    LinearLayout detect_ram;

    private void findId() {
        detect_cha = (ImageView) findViewById(R.id.detect_cha);
        detect_zhuangtai = (TextView) findViewById(R.id.detect_zhuangtai);
        detect_time = (TextView) findViewById(R.id.detect_time);
        detect_baifen = (TextView) findViewById(R.id.detect_baifen);
        detect_shiyong = (TextView) findViewById(R.id.detect_shiyong);
        detect_ram = (LinearLayout) findViewById(R.id.detect_ram);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        setContentView(R.layout.layout_battery_r);
        findId();
        long chongdian_time = DetectData.getDB(this, Constants.CONNECTED_TIME_LUN, 60 * 60 * 1000l);
        long use_time = DetectData.getDB(this, Constants.CONNECTED_LEFT_TIME_LUN, 60 * 60 * 1000l);
        int level = DetectData.getDB(this, Constants.CONNECTED_LEVEL_LUN, 100);
        int zhuangtai = DetectData.getDB(this, Constants.CONNECTED_ZZ, 0);
        detect_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (zhuangtai == 0) {
            detect_zhuangtai.setText(R.string.detect_1);
            detect_zhuangtai.setTextColor(ContextCompat.getColor(this, R.color.A4));
        } else if (zhuangtai == 1) {
            detect_zhuangtai.setText(R.string.detect_2);
            detect_zhuangtai.setTextColor(ContextCompat.getColor(this, R.color.A3));
        } else {
            detect_zhuangtai.setText(R.string.detect_3);
            detect_zhuangtai.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        detect_time.setText(millTransFate(chongdian_time));
        detect_shiyong.setText(millTransFate(use_time));
        detect_baifen.setText(level + "%");
        int count = 0;
        for (JunkInfo info : CleanManager.getInstance(this).getAppRamList()) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.dp2px(15), Util.dp2px(15));
            layoutParams.rightMargin = Util.dp2px(1);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            if (count == 4) {
//                imageView.setImageResource(R.mipmap.detect_ram);
//                detect_ram.addView(imageView, 0);
//                break;
//            }
            if (LoadManager.getInstance(this).getAppIcon(info.pkg) != null) {
                imageView.setImageDrawable(LoadManager.getInstance(this).getAppIcon(info.pkg));
                detect_ram.addView(imageView,0);
                count++;
            }
        }
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
