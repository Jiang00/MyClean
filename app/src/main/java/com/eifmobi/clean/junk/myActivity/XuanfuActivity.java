package com.eifmobi.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eifmobi.clean.core.CleanManager;
import com.android.client.AndroidSdk;
import com.eifmobi.clean.junk.R;
import com.eifmobi.clean.junk.myview.BubbleLineLayout;
import com.eifmobi.clean.junk.util.AdUtil;
import com.eifmobi.clean.junk.util.Constant;
import com.eifmobi.clean.junk.util.CheckState;
import com.eifmobi.clean.util.Util;
import com.eifmobi.clean.util.MemoryManager;
import com.eifmobi.clean.util.PreData;
import com.eifmobi.clean.junk.util.SwitchControl;

import java.util.List;


public class XuanfuActivity extends BaseActivity {
    LinearLayout ll_wifi, ll_liuliang, ll_xianshi, ll_shengyin, ll_gps;
    LinearLayout ll_ad;
    ImageView iv_wifi, iv_liuliang, iv_xianshi, iv_shengyin, iv_gps;
    TextView float_size;
    LinearLayout float_tishi;
    FrameLayout wifi_fl, liuliang_fl, shengyin_fl, liangdu_fl, gps_fl;
    ImageView float_huojian;
    ImageView float_zhuan;
    FrameLayout clean_button;
    BubbleLineLayout bubble_line;


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 10) {
                count++;
                initSize();
            }
        }
    };
    private View nativeView;
    private String TAG_FLAOT = "_float";
    private Animation fang;
    private AnimatorSet animatorSet;
    private long shifang;

    @Override
    protected void findId() {
        super.findId();
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ll_wifi = (LinearLayout) findViewById(R.id.ll_wifi);
        ll_liuliang = (LinearLayout) findViewById(R.id.ll_liuliang);
        ll_xianshi = (LinearLayout) findViewById(R.id.ll_xianshi);
        ll_shengyin = (LinearLayout) findViewById(R.id.ll_shengyin);
        ll_gps = (LinearLayout) findViewById(R.id.ll_gps);
        iv_wifi = (ImageView) findViewById(R.id.iv_wifi);
        iv_liuliang = (ImageView) findViewById(R.id.iv_liuliang);
        iv_xianshi = (ImageView) findViewById(R.id.iv_xianshi);
        iv_shengyin = (ImageView) findViewById(R.id.iv_shengyin);
        iv_gps = (ImageView) findViewById(R.id.iv_gps);
        float_tishi = (LinearLayout) findViewById(R.id.float_tishi);
        float_size = (TextView) findViewById(R.id.float_size);
        wifi_fl = (FrameLayout) findViewById(R.id.wifi_fl);
        liuliang_fl = (FrameLayout) findViewById(R.id.liuliang_fl);
        shengyin_fl = (FrameLayout) findViewById(R.id.shengyin_fl);
        liangdu_fl = (FrameLayout) findViewById(R.id.liangdu_fl);
        gps_fl = (FrameLayout) findViewById(R.id.gps_fl);
        clean_button = (FrameLayout) findViewById(R.id.clean_button);
        bubble_line = (BubbleLineLayout) findViewById(R.id.bubble_line);
        float_huojian = (ImageView) findViewById(R.id.float_huojian);
        float_zhuan = (ImageView) findViewById(R.id.float_zhuan);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_float);
        bubble_line.pause();
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);

        rotate(liuliang_fl, ll_liuliang, 36);

        rotate(liangdu_fl, ll_xianshi, 36 * 2);

        rotate(shengyin_fl, ll_shengyin, 36 * 3);

        rotate(gps_fl, ll_gps, 36 * 4);

        loadAd();
        wifi();
        shengYin();
        xianshiD();
        addListener();
    }

    private void rotate(View view, View view2, float rotate) {
        view.setPivotY(getResources().getDimension(R.dimen.d141));
        view.setPivotX(getResources().getDimension(R.dimen.d288) / 2);
        view.setRotation(rotate);
        view2.setRotation(-rotate);
    }

    private void addListener() {
//        waterView.setFloatWaterListener(floatWaterListener);
        ll_wifi.setOnClickListener(kuaijieListener);
        ll_liuliang.setOnClickListener(kuaijieListener);
        ll_xianshi.setOnClickListener(kuaijieListener);
        ll_shengyin.setOnClickListener(kuaijieListener);
        ll_gps.setOnClickListener(kuaijieListener);
        clean_button.setOnClickListener(kuaijieListener);
    }

    View.OnClickListener kuaijieListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_wifi:
                    wifiD();
                    SwitchControl.switchWifi(XuanfuActivity.this);
                    break;
                case R.id.ll_liuliang:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        try {
                            Intent intentNet = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentNet.setComponent(new ComponentName("com.android.settings",
                                    "com.android.settings.Settings$DataUsageSummaryActivity"));
                            startActivity(intentNet);
                        } catch (Exception ee) {
                        }
                    } else {
                        liuLiangd();
                        SwitchControl.setMobileData(XuanfuActivity.this, !CheckState.networkState(XuanfuActivity.this, null));
                    }


                    break;
                case R.id.ll_xianshi:
                    brightnessSwitchUtils(XuanfuActivity.this);
                    break;
                case R.id.ll_shengyin:
                    SwitchControl.switchSound(XuanfuActivity.this);
                    shengYin();
                    break;
                case R.id.ll_gps:
//                    SwitchControl.toggleGPS(XuanfuActivity.this);
                    try {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.clean_button:
                    clean_button.setOnClickListener(null);
                    startCleanAnimation();
                    break;
            }
        }
    };

    private void loadAd() {
        if (PreData.getDB(this, Constant.FULL_FLOAT, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AdUtil.FULL_DEFAULT);
                }
            }, 1000);
        } else {
            if (TextUtils.equals(PreData.getDB(XuanfuActivity.this, Constant.FLOAT_NATIVE_SIZE, AdUtil.NATIVE_SMALL), AdUtil.NATIVE_SMALL)) {
                nativeView = AdUtil.getNativeAdView("", R.layout.native_ad_3);
            } else {
                nativeView = AdUtil.getNativeAdView("", R.layout.native_ad_5);
            }
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
            } else {
                AdUtil.showBanner();
            }
        }
    }

    private void startCleanAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                shifang = killAll(XuanfuActivity.this);
                myHandler.sendEmptyMessage(10);
            }
        }).start();
        CleanManager.getInstance(this).clearRam();
        int w = float_huojian.getWidth();
        int h = float_huojian.getHeight();
        bubble_line.setParticleBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.float_line));
        bubble_line.reStart();
        animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(float_zhuan, View.ROTATION, 0, 3600);
        objectAnimator_2.setDuration(2000);
        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(float_huojian, View.TRANSLATION_Y, 0, 5,
                0, -5, 0);
        objectAnimator_3.setDuration(300);
        objectAnimator_3.setInterpolator(new LinearInterpolator());
        objectAnimator_3.setRepeatCount(5);
        ObjectAnimator objectAnimator_4 = ObjectAnimator.ofFloat(float_huojian, View.TRANSLATION_X, 0, 5,
                0, -0.5f * -5, 0);
        objectAnimator_4.setDuration(300);
        objectAnimator_4.setInterpolator(new LinearInterpolator());
        objectAnimator_4.setRepeatCount(5);
        ObjectAnimator objectAnimator_5 = ObjectAnimator.ofFloat(float_huojian, View.TRANSLATION_Y, 0, 0.3f * h, -h * 2);
        objectAnimator_5.setDuration(500);
        ObjectAnimator objectAnimator_6 = ObjectAnimator.ofFloat(float_huojian, View.TRANSLATION_X, 0, -0.3f * w, w * 2);
        objectAnimator_6.setDuration(500);
        animatorSet.play(objectAnimator_2);
        animatorSet.play(objectAnimator_3).with(objectAnimator_4);
        animatorSet.play(objectAnimator_5).with(objectAnimator_6);
        animatorSet.play(objectAnimator_5).after(objectAnimator_3);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                float_huojian.setVisibility(View.INVISIBLE);
                bubble_line.pause();
                bubble_line.destroy();
                myHandler.sendEmptyMessage(10);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });

    }

    int count;

    private void initSize() {
        if (count < 2) {
            return;
        }
        float_size.setText(Util.convertStorage(Math.abs(shifang), true));

        float_tishi.startAnimation(fang);
        float_tishi.setVisibility(View.VISIBLE);
    }

    public long killAll(Context context) {
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final long beforeMem = getAvailMemory(am);
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packageInfo : installedPackages) {
            if (packageInfo.packageName.equals(context.getPackageName())) {
                continue;
            }
            try {
                am.killBackgroundProcesses(packageInfo.packageName);
            } catch (Exception e) {

            }
        }
        final long afterMem = getAvailMemory(am);
//        int pratent = (int) ((ram_all - afterMem) * 100 / ram_all);
        return afterMem - beforeMem;

    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    private void wifi() {
        if (CheckState.wifiState(this)) {
            iv_wifi.setAlpha(1f);
        } else {
            iv_wifi.setAlpha(0.4f);
        }
    }

    private void wifiD() {
        if (!CheckState.wifiState(this)) {
            iv_wifi.setAlpha(1f);
        } else {
            iv_wifi.setAlpha(0.4f);
        }
    }

    private void liuLiang() {
        if (CheckState.networkState(this, null)) {
            iv_liuliang.setAlpha(1f);
        } else {
            iv_liuliang.setAlpha(0.4f);
        }
    }

    private void liuLiangd() {
        if (!CheckState.networkState(this, null)) {
            iv_liuliang.setAlpha(1f);
        } else {
            iv_liuliang.setAlpha(0.4f);
        }
    }

    private void xianshiD() {
        int light = getLight();
        if (light > 0 && light <= LIGHT_NORMAL) {
            //低亮度
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_0);
        } else if (light > LIGHT_NORMAL && light < LIGHT_100_PERCENT) {
            //中
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_50);
        } else if (light == LIGHT_100_PERCENT) {
            //高
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_100);
        } else if (light == -1) {
            //自动
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_zidong);
        }
    }

    private void shengYin() {
        if (CheckState.soundState(this)) {
            iv_shengyin.setAlpha(1f);
        } else {
            iv_shengyin.setAlpha(0.4f);
        }
    }

    private void gps() {
        if (CheckState.gpsState(this)) {
            iv_gps.setAlpha(1f);
        } else {
            iv_gps.setAlpha(0.4f);
        }
    }

    private int getLight() {
        int light = 0;
        ContentResolver cr = this.getContentResolver();
        boolean auto = false;
        try {
            auto = Settings.System.getInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
            light = android.provider.Settings.System.getInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS, -1);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (auto) {
            return -1;
        }
        return light;
    }

    private static final int LIGHT_NORMAL = 64;
    private static final int LIGHT_50_PERCENT = 127;
    //    private static final int LIGHT_75_PERCENT = 191;
    private static final int LIGHT_100_PERCENT = 255;
    private static final int LIGHT_AUTO = 0;
    private static final int LIGHT_ERR = -1;

    public void brightnessSwitchUtils(Context context) {
        int light = 0;
        ContentResolver cr = context.getContentResolver();
        try {
            boolean auto = Settings.System.getInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

            if (!auto) {
                light = android.provider.Settings.System.getInt(cr,
                        Settings.System.SCREEN_BRIGHTNESS, -1);
                if (light > 0 && light <= LIGHT_NORMAL) {
                    light = LIGHT_NORMAL;
                } else if (light > LIGHT_NORMAL && light <= LIGHT_50_PERCENT) {
                    light = LIGHT_50_PERCENT;
                } else {
                    light = LIGHT_100_PERCENT;
                }
            } else {
                light = LIGHT_AUTO;
            }
            switch (light) {
                case LIGHT_NORMAL:
                    light = LIGHT_50_PERCENT - 1;
                    iv_xianshi.setImageResource(R.mipmap.float_liangdu_50);
                    break;
                case LIGHT_50_PERCENT:
                    light = LIGHT_100_PERCENT - 1;
                    iv_xianshi.setImageResource(R.mipmap.float_liangdu_100);
                    break;
                case LIGHT_100_PERCENT:
                    light = LIGHT_NORMAL - 1;
                    Settings.System.putInt(cr,
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                    iv_xianshi.setImageResource(R.mipmap.float_liangdu_zidong);
                    break;
                case LIGHT_AUTO:
                    light = LIGHT_NORMAL - 1;
                    Settings.System.putInt(cr,
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    iv_xianshi.setImageResource(R.mipmap.float_liangdu_0);

                    break;
                case LIGHT_ERR:
                    light = LIGHT_NORMAL - 1;
                    break;

            }

            changeAppBrightness(context, light);
            android.provider.Settings.System.putInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS, light);

        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void changeAppBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }


    @Override
    protected void onResume() {
        super.onResume();
        liuLiang();
        gps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        count = 0;
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        AdUtil.closeBanner();
        fang.cancel();
        float_tishi.clearAnimation();
    }
}