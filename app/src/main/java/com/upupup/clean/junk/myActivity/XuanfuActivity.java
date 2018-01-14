package com.upupup.clean.junk.myActivity;

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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.upupup.clean.core.CleanManager;
import com.android.client.AndroidSdk;
import com.upupup.clean.junk.R;
import com.upupup.clean.junk.util.AdUtil;
import com.upupup.clean.junk.util.Constant;
import com.upupup.clean.junk.util.CheckState;
import com.upupup.clean.util.MemoryManager;
import com.upupup.clean.util.PreData;
import com.upupup.clean.junk.util.SwitchControl;

import java.util.List;


public class XuanfuActivity extends BaseActivity {
    LinearLayout ll_wifi, ll_liuliang, ll_xianshi, ll_shengyin, ll_gps;
    LinearLayout ll_ad;
    ImageView iv_wifi, iv_liuliang, iv_xianshi, iv_shengyin, iv_gps;
    TextView memory;
    RelativeLayout float_rotate;
    ImageView short_rotate, short_rotate_2;
    FrameLayout short_ani;

    private Handler myHandler;
    private View nativeView;
    private String TAG_FLAOT = "mutter_float";
    private Animation suo, fang;
    private AnimatorSet animatorSet;

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
        memory = (TextView) findViewById(R.id.memory);
        float_rotate = (RelativeLayout) findViewById(R.id.float_rotate);
        short_ani = (FrameLayout) findViewById(R.id.short_ani);
        short_rotate = (ImageView) findViewById(R.id.short_rotate);
        short_rotate_2 = (ImageView) findViewById(R.id.short_rotate_2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_float);
        myHandler = new Handler();
        suo = AnimationUtils.loadAnimation(this, R.anim.suo);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        loadAd();
        initSize();
        wifi();
        shengYin();
        xianshiD();
        addListener();
    }


    private void addListener() {
//        waterView.setFloatWaterListener(floatWaterListener);
        ll_wifi.setOnClickListener(kuaijieListener);
        ll_liuliang.setOnClickListener(kuaijieListener);
        ll_xianshi.setOnClickListener(kuaijieListener);
        ll_shengyin.setOnClickListener(kuaijieListener);
        ll_gps.setOnClickListener(kuaijieListener);
        float_rotate.setOnClickListener(kuaijieListener);
    }

    private boolean isAni;
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
                case R.id.float_rotate:
                    if (isAni) {
                        return;
                    }
                    isAni = true;
                    startCleanAnimation();
                    break;
            }
        }
    };

    private void loadAd() {
        nativeView = AdUtil.getNativeAdView(TAG_FLAOT, R.layout.native_ad_5);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }

    private void startCleanAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                killAll(XuanfuActivity.this);
            }
        }).start();
        CleanManager.getInstance(this).clearRam();
        animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(short_ani, View.SCALE_X, 0, 1);
        objectAnimator_1.setDuration(500);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(short_ani, View.SCALE_Y, 0, 1);
        objectAnimator_2.setDuration(500);
        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(short_rotate, View.ROTATION, 0, 3600);
        objectAnimator_3.setDuration(3600);
        ObjectAnimator objectAnimator_4 = ObjectAnimator.ofFloat(short_rotate_2, View.ROTATION, 0, 3600);
        objectAnimator_4.setDuration(3600);
        ObjectAnimator objectAnimator_5 = ObjectAnimator.ofFloat(short_ani, View.SCALE_X, 1, 0);
        objectAnimator_1.setDuration(500);
        ObjectAnimator objectAnimator_6 = ObjectAnimator.ofFloat(short_ani, View.SCALE_Y, 1, 0);
        objectAnimator_2.setDuration(500);
        animatorSet.play(objectAnimator_1).with(objectAnimator_2).with(objectAnimator_3).with(objectAnimator_4);
        animatorSet.play(objectAnimator_5).with(objectAnimator_6).after(3100);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                short_ani.setVisibility(View.GONE);
                initSize();
                isAni = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
        short_ani.setVisibility(View.VISIBLE);
    }

    private void initSize() {
        long ram_size_kongxian = MemoryManager.getPhoneFreeRamMemory(this);
        long ram__size_all = MemoryManager.getPhoneTotalRamMemory();
        long ram_shiyong = ram__size_all - ram_size_kongxian;
        int memo = (int) (ram_shiyong * 100 / ram__size_all);
        memory.setText(memo + "%");
    }

    public int killAll(Context context) {
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        final ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
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
        int pratent = (int) ((ram_all - afterMem) * 100 / ram_all);
        return pratent;
    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    private void wifi() {
        if (CheckState.wifiState(this)) {
            iv_wifi.setBackgroundResource(R.drawable.shape_a3_round);
        } else {
            iv_wifi.setBackgroundResource(R.drawable.shape_ffffff_20_round);
        }
    }

    private void wifiD() {
        if (!CheckState.wifiState(this)) {
            iv_wifi.setBackgroundResource(R.drawable.shape_a3_round);
        } else {
            iv_wifi.setBackgroundResource(R.drawable.shape_ffffff_20_round);
        }
    }

    private void liuLiang() {
        if (CheckState.networkState(this, null)) {
            iv_liuliang.setBackgroundResource(R.drawable.shape_a3_round);
        } else {
            iv_liuliang.setBackgroundResource(R.drawable.shape_ffffff_20_round);
        }
    }

    private void liuLiangd() {
        if (!CheckState.networkState(this, null)) {
            iv_liuliang.setBackgroundResource(R.drawable.shape_a3_round);
        } else {
            iv_liuliang.setBackgroundResource(R.drawable.shape_ffffff_20_round);
        }
    }

    private void xianshiD() {
        int light = getLight();
        if (light > 0 && light <= LIGHT_NORMAL) {
            //低亮度
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_0);
            iv_xianshi.setBackgroundResource(R.drawable.shape_ffffff_20_round);
        } else if (light > LIGHT_NORMAL && light < LIGHT_100_PERCENT) {
            //中
            iv_xianshi.setBackgroundResource(R.drawable.shape_ffffff_20_round);
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_50);
        } else if (light == LIGHT_100_PERCENT) {
            //高
            iv_xianshi.setBackgroundResource(R.drawable.shape_ffffff_20_round);
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_100);
        } else if (light == -1) {
            //自动
            iv_xianshi.setBackgroundResource(R.drawable.shape_a3_round);
            iv_xianshi.setImageResource(R.mipmap.float_liangdu_zidong);
        }
    }

    private void shengYin() {
        if (CheckState.soundState(this)) {
            iv_shengyin.setBackgroundResource(R.drawable.shape_a3_round);
        } else {
            iv_shengyin.setBackgroundResource(R.drawable.shape_ffffff_20_round);
        }
    }

    private void gps() {
        if (CheckState.gpsState(this)) {
            iv_gps.setBackgroundResource(R.drawable.shape_a3_round);
        } else {
            iv_gps.setBackgroundResource(R.drawable.shape_ffffff_20_round);
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
                    iv_xianshi.setBackgroundResource(R.drawable.shape_ffffff_20_round);
                    break;
                case LIGHT_50_PERCENT:
                    light = LIGHT_100_PERCENT - 1;
                    iv_xianshi.setImageResource(R.mipmap.float_liangdu_100);
                    iv_xianshi.setBackgroundResource(R.drawable.shape_ffffff_20_round);
                    break;
                case LIGHT_100_PERCENT:
                    light = LIGHT_NORMAL - 1;
                    Settings.System.putInt(cr,
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                    iv_xianshi.setImageResource(R.mipmap.float_liangdu_zidong);
                    iv_xianshi.setBackgroundResource(R.drawable.shape_a3_round);
                    break;
                case LIGHT_AUTO:
                    light = LIGHT_NORMAL - 1;
                    Settings.System.putInt(cr,
                            Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    iv_xianshi.setImageResource(R.mipmap.float_liangdu_0);
                    iv_xianshi.setBackgroundResource(R.drawable.shape_ffffff_20_round);

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
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }
}