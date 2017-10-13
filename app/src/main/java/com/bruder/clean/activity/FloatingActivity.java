package com.bruder.clean.activity;

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
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.junk.R;
import com.bruder.clean.util.CheckState;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.SwitchControl;
import com.bruder.clean.util.UtilAd;
import com.cleaner.entity.JunkInfo;
import com.cleaner.heart.CleanManager;
import com.cleaner.util.DataPre;
import com.cleaner.util.MemoryManager;
import com.cleaner.util.Util;

import java.util.ArrayList;
import java.util.List;


public class FloatingActivity extends BaseActivity {
    private View nativeView;
    private MyApplication cleanApplication;
    LinearLayout ll_ad;
    LinearLayout ll_wifi, ll_liuliang, ll_xianshi, ll_shengyin, ll_gps;
    private ArrayList<JunkInfo> listFloat, listFloat_white;
    private Handler myHandler;
    private String TAG_FLAOT = "bruder_float";
    private Animation rotate, suo, fang;
    ImageView iv_wifi, iv_liuliang, iv_xianshi, iv_shengyin, iv_gps;
    ImageView float_rotate, float_cricle;
    TextView float_memory;
    LinearLayout rl_memory;
    TextView float_tishi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_float);
        cleanApplication = (MyApplication) getApplication();
        myHandler = new Handler();
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_ni);
        suo = AnimationUtils.loadAnimation(this, R.anim.suo);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        loadAd();
        initList();
        wifi();
        shengYin();
        xianshiD();
        addListener();
    }

    private void loadAd() {
        if (DataPre.getDB(this, Constant.FULL_FLOAT, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(UtilAd.DEFAULT_FULL);
                }
            }, 1000);
        } else {
            nativeView = UtilAd.getNativeAdView(TAG_FLAOT, R.layout.native_ad_5);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                if (nativeView.getHeight() == Util.dp2px(250)) {
                    layout_ad.height = Util.dp2px(250);
                }
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
            }
        }
    }

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
        float_cricle = (ImageView) findViewById(R.id.float_cricle);
        float_rotate = (ImageView) findViewById(R.id.float_rotate);
        float_memory = (TextView) findViewById(R.id.float_memory);
        rl_memory = (LinearLayout) findViewById(R.id.rl_memory);
        float_tishi = (TextView) findViewById(R.id.float_tishi);
    }

    private void initList() {

        float_memory.setText(Util.getMemory(this) + "");

    }

    View.OnClickListener kuaijieListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_wifi:
                    wifiD();
                    SwitchControl.switchWifi(FloatingActivity.this);
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
                        SwitchControl.setMobileData(FloatingActivity.this, !CheckState.networkState(FloatingActivity.this, null));
                    }
                    break;
                case R.id.ll_xianshi:
                    brightnessSwitchUtils(FloatingActivity.this);
                    break;
                case R.id.ll_shengyin:
                    SwitchControl.switchSound(FloatingActivity.this);
                    shengYin();
                    break;
                case R.id.ll_gps:
//                    SwitchControl.toggleGPS(FloatingActivity.this);
                    try {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.float_cricle:
                    float_rotate.setVisibility(View.VISIBLE);
                    float_rotate.startAnimation(rotate);
                    startCleanAnimation();
                    break;
            }
        }
    };

    private void addListener() {
//        waterView.setFloatWaterListener(floatWaterListener);
        ll_wifi.setOnClickListener(kuaijieListener);
        ll_liuliang.setOnClickListener(kuaijieListener);
        ll_xianshi.setOnClickListener(kuaijieListener);
        ll_shengyin.setOnClickListener(kuaijieListener);
        ll_gps.setOnClickListener(kuaijieListener);
        float_cricle.setOnClickListener(kuaijieListener);
    }

    private void setListAnimation() {

    }

    public int killAll(Context context) {
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
        int pratent = (int) ((ram_all - afterMem) * 100 / ram_all);
        return pratent;
    }

    private void startCleanAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                killAll(FloatingActivity.this);
            }
        }).start();
        CleanManager.getInstance(this).clearRam();

        ObjectAnimator suoX = ObjectAnimator.ofFloat(rl_memory, "scaleX", 1f, 0f, 1f);
        ObjectAnimator suoY = ObjectAnimator.ofFloat(rl_memory, "scaleY", 1f, 0f, 1f);
        ObjectAnimator suoX2 = ObjectAnimator.ofFloat(float_tishi, "scaleX", 1f, 0f, 1f);
        ObjectAnimator suoY2 = ObjectAnimator.ofFloat(float_tishi, "scaleY", 1f, 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(suoY).with(suoX).with(suoX2).with(suoY2);
        animSet.setDuration(2000);
        animSet.start();
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotate.cancel();
                float_cricle.setVisibility(View.VISIBLE);
                float_rotate.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                float_memory.setText(Util.getMemory(FloatingActivity.this) + "");
                float_tishi.setText(R.string.float_yijiasu);
                float_tishi.startAnimation(fang);
                rl_memory.startAnimation(fang);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
        fang.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                float_cricle.setVisibility(View.VISIBLE);
                float_rotate.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    private void wifi() {
        Drawable d = iv_wifi.getDrawable();
        BitmapDrawable bitmap = (BitmapDrawable) d;
        if (CheckState.wifiState(this)) {
            bitmap.setColorFilter(getResources().getColor(R.color.A1), PorterDuff.Mode.SRC_ATOP);
        } else {
            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void liuLiang() {
        Drawable d = iv_liuliang.getDrawable();
        BitmapDrawable bitmap = (BitmapDrawable) d;
        if (CheckState.networkState(this, null)) {
            bitmap.setColorFilter(getResources().getColor(R.color.A1), PorterDuff.Mode.SRC_ATOP);
        } else {
            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void wifiD() {
        Drawable d = iv_wifi.getDrawable();
        BitmapDrawable bitmap = (BitmapDrawable) d;
        if (!CheckState.wifiState(this)) {
            bitmap.setColorFilter(getResources().getColor(R.color.A1), PorterDuff.Mode.SRC_ATOP);
        } else {
            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void liuLiangd() {
        Drawable d = iv_liuliang.getDrawable();
        BitmapDrawable bitmap = (BitmapDrawable) d;
        if (!CheckState.networkState(this, null)) {
            bitmap.setColorFilter(getResources().getColor(R.color.A1), PorterDuff.Mode.SRC_ATOP);
        } else {
            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
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

    private void gps() {
        Drawable d = iv_gps.getDrawable();
        BitmapDrawable bitmap = (BitmapDrawable) d;
        if (CheckState.gpsState(this)) {
            bitmap.setColorFilter(getResources().getColor(R.color.A1), PorterDuff.Mode.SRC_ATOP);
        } else {
            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void shengYin() {
        Drawable d = iv_shengyin.getDrawable();
        BitmapDrawable bitmap = (BitmapDrawable) d;
        if (CheckState.soundState(this)) {
            bitmap.setColorFilter(getResources().getColor(R.color.A1), PorterDuff.Mode.SRC_ATOP);
        } else {
            bitmap.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
        }
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
}