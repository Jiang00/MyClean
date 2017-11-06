package com.mutter.clean.junk.myActivity;

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
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mutter.clean.core.CleanManager;
import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myview.FloatRound;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.CheckState;
import com.mutter.clean.util.Util;
import com.mutter.clean.util.MemoryManager;
import com.mutter.clean.util.PreData;
import com.mutter.clean.junk.util.SwitchControl;

import java.util.List;


public class XuanfuActivity extends BaseActivity {
    LinearLayout ll_wifi, ll_liuliang, ll_xianshi, ll_shengyin, ll_gps;
    FloatRound float_rotate;
    LinearLayout ll_ad;
    ImageView iv_wifi, iv_liuliang, iv_xianshi, iv_shengyin, iv_gps;
    TextView float_ram_free, float_ram_use;
    TextView float_tishi;

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
        float_rotate = (FloatRound) findViewById(R.id.float_rotate);
        float_tishi = (TextView) findViewById(R.id.float_tishi);
        float_ram_free = (TextView) findViewById(R.id.float_ram_free);
        float_ram_use = (TextView) findViewById(R.id.float_ram_use);
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
                    float_rotate.setOnClickListener(null);
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
            nativeView = AdUtil.getNativeAdView(TAG_FLAOT, R.layout.native_ad_5);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
                View ad_action = nativeView.findViewWithTag("ad_action");
                animatorSet = new AnimatorSet();
                ObjectAnimator animator_1 = ObjectAnimator.ofFloat(ll_ad, View.SCALE_X, 1, 1.2f, 0.9f, 1.1f, 1);
                animator_1.setDuration(800);
                ObjectAnimator animator_2 = ObjectAnimator.ofFloat(ll_ad, View.SCALE_Y, 1, 0.8f, 1.1f, 0.9f, 1);
                animator_2.setDuration(800);
                animatorSet.play(animator_1).with(animator_2);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (animatorSet != null && !animatorSet.isRunning()) {
                            animatorSet.start();
                        }
                        myHandler.postDelayed(this, 2000);
                    }
                });
            }
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
        float_rotate.setCustomRoundListener(new FloatRound.CustomRoundListener() {
            @Override
            public void progressUpdate() {
                float_rotate.startProgress2(Util.getMemory(XuanfuActivity.this));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initSize();
                    }
                });
            }
        });
        float_rotate.startProgress1();
        float_tishi.startAnimation(suo);
        suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                float_tishi.setText(R.string.float_yijiasu);
                float_tishi.startAnimation(fang);
                initSize();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
    }

    private void initSize() {
        long size_all = MemoryManager.getPhoneTotalRamMemory();
        long kong = getAvailMemory((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        float_ram_free.setText(Util.convertStorage(kong, true));
        float_ram_use.setText(Util.convertStorage(size_all - kong, true));
        float_rotate.setProgress(Util.getMemory(this));
    }

    public int killAll(Context context) {
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
            iv_wifi.setColorFilter(getResources().getColor(R.color.float_kaiguan), PorterDuff.Mode.SRC_ATOP);
        } else {
            iv_wifi.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void wifiD() {
        if (!CheckState.wifiState(this)) {
            iv_wifi.setColorFilter(getResources().getColor(R.color.float_kaiguan), PorterDuff.Mode.SRC_ATOP);
        } else {
            iv_wifi.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void liuLiang() {
        if (CheckState.networkState(this, null)) {
            iv_liuliang.setColorFilter(getResources().getColor(R.color.float_kaiguan), PorterDuff.Mode.SRC_ATOP);
        } else {
            iv_liuliang.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void liuLiangd() {
        if (!CheckState.networkState(this, null)) {
            iv_liuliang.setColorFilter(getResources().getColor(R.color.float_kaiguan), PorterDuff.Mode.SRC_ATOP);
        } else {
            iv_liuliang.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
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
            iv_shengyin.setColorFilter(ContextCompat.getColor(this, R.color.float_kaiguan));
        } else {
            iv_shengyin.setColorFilter(null);
        }
    }

    private void gps() {
        if (CheckState.gpsState(this)) {
            iv_gps.setColorFilter(getResources().getColor(R.color.float_kaiguan));
        } else {
            iv_gps.setColorFilter(null);
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
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }
}