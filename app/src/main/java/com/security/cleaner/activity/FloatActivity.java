package com.security.cleaner.activity;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.security.cleaner.utils.AdUtil;
import com.security.cleaner.utils.Constant;
import com.security.cleaner.utils.ASwitchControl;
import com.security.mcleaner.manager.CleanManager;
import com.security.mcleaner.mutil.MemoryManager;
import com.security.mcleaner.mutil.PreData;
import com.security.mcleaner.mutil.Util;
import com.android.client.AndroidSdk;
import com.security.cleaner.R;
import com.security.cleaner.utils.CheckState;

import java.util.List;


public class FloatActivity extends BaseActivity {
    LinearLayout ll_ad;
    ImageView iv_wifi, iv_liuliang, iv_xianshi, iv_shengyin, iv_gps;
    LinearLayout ll_wifi, ll_liuliang, ll_xianshi, ll_shengyin, ll_gps;
    ImageView float_jiasu;

    private View nativeView;
    private String TAG_FLAOT = "my_float";
    private TextView float_jiasu_tv, float_jiasu_tv2;
    private LinearLayout float_jiasu_ll;

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
        float_jiasu = (ImageView) findViewById(R.id.float_jiasu);
        float_jiasu_tv = (TextView) findViewById(R.id.float_jiasu_tv);
        float_jiasu_ll = (LinearLayout) findViewById(R.id.float_jiasu_ll);
        float_jiasu_tv2 = (TextView) findViewById(R.id.float_jiasu_tv2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_float);
        loadAd();
        initList();
        wifi();
        shengYin();
        xianshiD();
        addListener();
    }

    private void initList() {
    }

    private void addListener() {
//        waterView.setFloatWaterListener(floatWaterListener);
        ll_wifi.setOnClickListener(kuaijieListener);
        ll_liuliang.setOnClickListener(kuaijieListener);
        ll_xianshi.setOnClickListener(kuaijieListener);
        ll_shengyin.setOnClickListener(kuaijieListener);
        ll_gps.setOnClickListener(kuaijieListener);
        float_jiasu.setOnClickListener(kuaijieListener);
    }


    private ObjectAnimator objectAnimator;
    private AnimatorSet animator_set;
    View.OnClickListener kuaijieListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_wifi:
                    wifiD();
                    ASwitchControl.switchWifi(FloatActivity.this);
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
                        ASwitchControl.setMobileData(FloatActivity.this, !CheckState.networkState(FloatActivity.this, null));
                    }


                    break;
                case R.id.ll_xianshi:
                    brightnessSwitchUtils(FloatActivity.this);
                    break;
                case R.id.ll_shengyin:
                    ASwitchControl.switchSound(FloatActivity.this);
                    shengYin();
                    break;
                case R.id.ll_gps:
//                    ASwitchControl.toggleGPS(FloatActivity.this);
                    try {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.float_jiasu:
                    float_jiasu.setOnClickListener(null);
                    count = 0;
                    startCleanAnimation();
                    objectAnimator = ObjectAnimator.ofFloat(float_jiasu, View.ROTATION, 0, 3600);
                    objectAnimator.setDuration(3000);
                    objectAnimator.start();
                    objectAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startSeondAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }

                        @Override
                        public void onAnimationStart(Animator animation) {

                        }
                    });
                    break;
            }
        }
    };
    int count;
    long kill_size;

    //qingli
    private void startCleanAnimation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                kill_size = killAll(FloatActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startSeondAnimation();
                    }
                });
            }
        }).start();
        CleanManager.getInstance(this).clearRam();
    }

    private void startSeondAnimation() {
        count++;
        if (count < 2) {
            return;
        }
        if (kill_size < 0) {
            kill_size = 0;
        }
        float_jiasu_tv2.setText(Util.convertStorage(kill_size, true));
        animator_set = new AnimatorSet();
        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(float_jiasu_tv, View.SCALE_Y, 1, 0);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(float_jiasu_tv, View.SCALE_X, 1, 0);

        animator_set.play(objectAnimator_1).with(objectAnimator_2);
        animator_set.setDuration(500);
        animator_set.start();
        animator_set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animator_set = new AnimatorSet();
                ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(float_jiasu_ll, View.SCALE_Y, 0, 1);
                ObjectAnimator objectAnimator_4 = ObjectAnimator.ofFloat(float_jiasu_ll, View.SCALE_X, 0, 1);
                animator_set.play(objectAnimator_3).with(objectAnimator_4);
                animator_set.setDuration(500);
                animator_set.start();
                float_jiasu_ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
    }

    public long killAll(Context context) {
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        final ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        final long beforeMem = getAvailMemory(am);
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
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
        int pratent = (int) ((beforeMem - afterMem) * 100 / ram_all);
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
            iv_wifi.setImageResource(R.mipmap.float_wifi);
        } else {
            iv_wifi.setImageResource(R.mipmap.float_wifi_n);
        }
    }

    private void loadAd() {
        if (TextUtils.equals(PreData.getDB(this, Constant.FLOAT_NATIVVE_SIZE, AdUtil.SIZE_SMALL), AdUtil.SIZE_SMALL)) {
            nativeView = AdUtil.getNativeAdView(TAG_FLAOT, R.layout.native_ad_3);
        } else {
            nativeView = AdUtil.getNativeAdView(TAG_FLAOT, R.layout.native_ad_5);
        }
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
        } else {
            AdUtil.showBanner();
        }
    }

    private void wifiD() {
        if (!CheckState.wifiState(this)) {
            iv_wifi.setImageResource(R.mipmap.float_wifi);
        } else {
            iv_wifi.setImageResource(R.mipmap.float_wifi_n);
        }
    }

    private void liuLiang() {
        if (CheckState.networkState(this, null)) {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang);
        } else {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang_n);
        }
    }

    private void liuLiangd() {
        if (!CheckState.networkState(this, null)) {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang);
        } else {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang_n);
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
        } else if (light == 0) {
            //自动
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_zidong);
        }
    }

    private void shengYin() {
        if (CheckState.soundState(this)) {
            iv_shengyin.setImageResource(R.mipmap.float_shengyin);
        } else {
            iv_shengyin.setImageResource(R.mipmap.float_shengyin_n);
        }
    }

    private void gps() {
        if (CheckState.gpsState(this)) {
            iv_gps.setImageResource(R.mipmap.float_gps);
        } else {
            iv_gps.setImageResource(R.mipmap.float_gps_n);
        }
    }

    private int getLight() {
        int light = 0;
        ContentResolver cr = this.getContentResolver();
        try {
            boolean auto = Settings.System.getInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
            light = android.provider.Settings.System.getInt(cr,
                    Settings.System.SCREEN_BRIGHTNESS, -1);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
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
        AdUtil.closeBanner();
        count = 0;
        super.onDestroy();
        if (objectAnimator != null && objectAnimator.isRunning()) {
            objectAnimator.removeAllListeners();
            objectAnimator.cancel();
        }
        if (animator_set != null && animator_set.isRunning()) {
            animator_set.removeAllListeners();
            animator_set.cancel();
        }
    }
}