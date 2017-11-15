package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.util.Util;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;

import java.util.List;

/**
 * Created by ${} on 2017/11/8.
 */

public class NotifiCpuActivity extends BaseActivity {
    private LinearLayout ll_ad;
    Button cpu_ok;
    ImageView cpu_cha;

    @Override
    protected void findId() {
        super.findId();
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        cpu_ok = (Button) findViewById(R.id.cpu_ok);
        cpu_cha = (ImageView) findViewById(R.id.cpu_cha);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cpu);
        View nativeView = AdUtil.getNativeAdView(this, "", R.layout.native_ad1);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        }
        cpu_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cpu_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notifyIntentCooling = new Intent(NotifiCpuActivity.this, CoolingActivity.class);
                notifyIntentCooling.putExtra("from", "notifi");
                startActivity(notifyIntentCooling);
                finish();
            }
        });

    }
}
