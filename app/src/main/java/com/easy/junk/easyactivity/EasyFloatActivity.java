package com.easy.junk.easyactivity;

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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.easy.clean.core.CleanManager;
import com.easy.clean.easyutils.MemoryManager;
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easycustomview.ImageAccessor;
import com.easy.junk.easytools.EasyCheckState;
import com.easy.junk.easytools.EasyConstant;
import com.easy.junk.easytools.SetAdUtil;
import com.easy.junk.easytools.SwitchControl;
import com.twee.module.tweenengine.Tween;
import com.twee.module.tweenengine.TweenManager;

import java.util.List;

public class EasyFloatActivity extends BaseActivity {
    private String TAG_FLAOT = "cleanmobi_float";
    private boolean isdoudong;
    TweenManager tweenManager;
    private boolean istween;
    ImageView float_cricle1;
    private View nativeView;
    private MyApplication cleanApplication;
    private Animation rotate, suo, fang;
    TextView float_memory, float_tishi;
    LinearLayout ll_ad;
    LinearLayout ll_wifi, ll_liuliang, ll_xianshi, ll_shengyin, ll_gps;
    private Handler myHandler;
    private static final int LIGHT_100_PERCENT = 255;
    private static final int LIGHT_AUTO = 0;
    private static final int LIGHT_ERR = -1;
    private RelativeLayout float_yidong_linearlayout;
    private Intent intent;
    private ObjectAnimator animator;
    ImageView iv_wifi, iv_liuliang, iv_xianshi, iv_shengyin, iv_gps;
    private static final int LIGHT_NORMAL = 64;
    private static final int LIGHT_50_PERCENT = 127;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_float);
        intent = getIntent();
        cleanApplication = (MyApplication) getApplication();
        myHandler = new Handler();
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_ni);
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        tweenManager = new TweenManager();
        Tween.registerAccessor(ImageView.class, new ImageAccessor());
        istween = true;
        setAnimationThread();
        loadAd();
        initList();
        wifi();
        shengYin();
        xianshiD();
        addListener();
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
        float_yidong_linearlayout = (RelativeLayout) findViewById(R.id.float_yidong_linearlayout);
        iv_wifi = (ImageView) findViewById(R.id.iv_wifi);
        iv_liuliang = (ImageView) findViewById(R.id.iv_liuliang);
        iv_xianshi = (ImageView) findViewById(R.id.iv_xianshi);
        iv_shengyin = (ImageView) findViewById(R.id.iv_shengyin);
        iv_gps = (ImageView) findViewById(R.id.iv_gps);
        float_cricle1 = (ImageView) findViewById(R.id.float_cricle1);
        float_memory = (TextView) findViewById(R.id.float_memory);
        float_tishi = (TextView) findViewById(R.id.float_tishi);
    }

    private void initList() {
        float_memory.setText(MyUtils.getMemory(this) + "%");
    }

    private void setAnimationThread() {
        new Thread(new Runnable() {
            private long lastMillis = -1;

            public void run() {
                while (istween) {
                    if (lastMillis > 0) {
                        long currentMillis = System.currentTimeMillis();
                        final float delta = (currentMillis - lastMillis) / 1000f;

                        runOnUiThread(new Runnable() {

                            public void run() {
                                tweenManager.update(delta);

                            }
                        });

                        lastMillis = currentMillis;
                    } else {
                        lastMillis = System.currentTimeMillis();
                    }
                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }).start();
    }

    private void addListener() {
        ll_wifi.setOnClickListener(kuaijieListener);
        ll_liuliang.setOnClickListener(kuaijieListener);
        ll_xianshi.setOnClickListener(kuaijieListener);
        ll_shengyin.setOnClickListener(kuaijieListener);
        ll_gps.setOnClickListener(kuaijieListener);
        float_cricle1.setOnClickListener(kuaijieListener);
    }

    private void loadAd() {
        if (PreData.getDB(this, EasyConstant.FULL_FLOAT, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(SetAdUtil.DEFAULT_FULL);
                }
            }, 1000);
        } else {
            nativeView = SetAdUtil.getNativeAdView(TAG_FLAOT, R.layout.native_ad_5);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                if (nativeView.getHeight() == MyUtils.dp2px(250)) {
                    layout_ad.height = MyUtils.dp2px(250);
                }
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
            }
        }
    }

    View.OnClickListener kuaijieListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_wifi:
                    wifiD();
                    // 开启WiFi
                    SwitchControl.switchWifi(EasyFloatActivity.this);
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
                        SwitchControl.setMobileData(EasyFloatActivity.this, !EasyCheckState.networkState(EasyFloatActivity.this, null));
                    }


                    break;
                case R.id.ll_xianshi:
                    brightnessSwitchUtils(EasyFloatActivity.this);
                    break;
                case R.id.ll_shengyin:
                    SwitchControl.switchSound(EasyFloatActivity.this);
                    shengYin();
                    break;
                case R.id.ll_gps:
//                    SwitchControl.toggleGPS(EasyFloatActivity.this);
                    try {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.float_cricle1:
//                    float_cricle1.setOnClickListener(null);
                    Log.e("onclick", "===================");
                    startCleanAnimation();
                    break;
            }
        }
    };


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
                killAll(EasyFloatActivity.this);
            }
        }).start();
        CleanManager.getInstance(this).clearRam();
        isdoudong = true;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final float hx = float_cricle.getX();
//                final float hy = float_cricle.getY();
//                while (isdoudong) {
//                    int x = (int) (Math.random() * (16)) - 8;
//                    int y = (int) (Math.random() * (16)) - 8;
//                    try {
//                        Thread.sleep(80);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Tween.to(float_cricle, ImageAccessor.BOUNCE_EFFECT, 0.08f).target(hx + x, hy + y, 1, 1)
//                            .ease(TweenEquations.easeInQuad).delay(0)
//                            .start(tweenManager);
//                }
//            }
//        }).start();
        float_cricle1.startAnimation(rotate);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isdoudong = false;
                float_memory.setText(MyUtils.getMemory(EasyFloatActivity.this) + "%");
                float_tishi.setText(R.string.float_yijiasu);
                float_memory.startAnimation(fang);
                float_tishi.startAnimation(fang);
                float_memory.setVisibility(View.VISIBLE);
//                float_cricle.setVisibility(View.GONE);
                float_tishi.setVisibility(View.VISIBLE);
//                float_cricle1.startAnimation(suo);
            }
        }, 1500);
        suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                float_memory.setText(MyUtils.getMemory(EasyFloatActivity.this) + "%");
                float_tishi.setText(R.string.float_yijiasu);
                float_memory.startAnimation(fang);
                float_tishi.startAnimation(fang);
                float_memory.setVisibility(View.VISIBLE);
//                float_cricle.setVisibility(View.GONE);
                float_tishi.setVisibility(View.VISIBLE);
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
//                float_cricle.setVisibility(View.VISIBLE);
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
        if (EasyCheckState.wifiState(this)) {
            iv_wifi.setImageResource(R.mipmap.float_wifi);
        } else {
            iv_wifi.setImageResource(R.mipmap.float_wifi1);
        }
    }


    private void liuLiangd() {
        if (!EasyCheckState.networkState(this, null)) {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang1);
        } else {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang);
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

    private void wifiD() {
        if (!EasyCheckState.wifiState(this)) {
            iv_wifi.setImageResource(R.mipmap.float_wifi);
        } else {
            iv_wifi.setImageResource(R.mipmap.float_wifi1);
        }
    }

    private void liuLiang() {
        if (EasyCheckState.networkState(this, null)) {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang);
        } else {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang1);
        }
    }

    private void shengYin() {
        if (EasyCheckState.soundState(this)) {
            iv_shengyin.setImageResource(R.mipmap.float_shengyin);
        } else {
            iv_shengyin.setImageResource(R.mipmap.float_shengyin1);
        }
    }

    private void gps() {
        if (EasyCheckState.gpsState(this)) {
            iv_gps.setImageResource(R.mipmap.float_gps);
        } else {
            iv_gps.setImageResource(R.mipmap.float_gps1);
        }
    }


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
                    light = LIGHT_50_PERCENT;
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
    protected void onPause() {
        super.onPause();
        isdoudong = false;
        istween = false;
    }
}