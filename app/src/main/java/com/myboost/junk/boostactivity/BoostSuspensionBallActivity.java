package com.myboost.junk.boostactivity;

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
import com.myboost.clean.core.CleanManager;
import com.myboost.clean.utilsprivacy.MemoryManager;
import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.customviewboost.BoostImageAccessor;
import com.myboost.junk.customviewboost.ImageAccessorBoost;
import com.myboost.junk.boosttools.CheckBoostState;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.boosttools.SetAdUtilPrivacy;
import com.myboost.junk.boosttools.SwitchControl;
import com.twee.module.tweenengine.Tween;
import com.twee.module.tweenengine.TweenEquations;
import com.twee.module.tweenengine.TweenManager;

import java.util.List;

public class BoostSuspensionBallActivity extends BaseActivity {
    LinearLayout ll_ad;
    private View nativeView;
    private static final int LIGHT_NORMAL = 64;
    private static final int LIGHT_50_PERCENT = 127;
    TweenManager tweenManager;
    ImageView float_huojian, float_huan;
    AnimatorSet animSet1;
    LinearLayout ll_wifi, ll_liuliang, ll_xianshi, ll_shengyin, ll_gps;
    private Handler myHandler;
    private boolean istween;
    private Intent intent;
    private static final int LIGHT_100_PERCENT = 255;
    private static final int LIGHT_AUTO = 0;
    private static final int LIGHT_ERR = -1;
    private MyApplication cleanApplication;
    private Animation rotate, suo, fang;
    private ObjectAnimator animator, animator1;
    ImageView iv_wifi, iv_liuliang, iv_xianshi, iv_shengyin, iv_gps;
    private String TAG_FLAOT = "flashclean_float";
    private boolean isdoudong;
    ObjectAnimator animator2, animator11, animator12;
    TextView float_memory, float_tishi;

    private void startTween() {
        final float hy = float_huojian.getTop();
        final float hx = float_huojian.getLeft();
        isdoudong = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isdoudong) {
                    int x = (int) (Math.random() * (10)) - 5;
                    int y = (int) (Math.random() * (10)) - 5;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Tween.to(float_huojian, BoostImageAccessor.BOUNCE_EFFECT, 0.08f).target(hx + x, hy + y, 1f, 1)
                            .ease(TweenEquations.easeInQuad).delay(0)
                            .start(tweenManager);
                }
            }
        }).start();

    }

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
        Tween.registerAccessor(ImageView.class, new ImageAccessorBoost());
        istween = true;
        setAnimationThread();
        loadAd();
        initList();
        wifi();
        shengYin();
        xianshiD();
        addListener();
    }

    private void initList() {
        float_memory.setText(MyUtils.getMemory(this) + "%");
    }

    private void loadAd() {
        if (PreData.getDB(this, BoostMyConstant.FULL_FLOAT, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(SetAdUtilPrivacy.DEFAULT_FULL);
                }
            }, 1000);
        } else {
            nativeView = SetAdUtilPrivacy.getNativeAdView(TAG_FLAOT, R.layout.native_ad_5);
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
        float_huojian = (ImageView) findViewById(R.id.float_huojian);
        float_huan = (ImageView) findViewById(R.id.float_huan);
        float_memory = (TextView) findViewById(R.id.float_memory);
        float_tishi = (TextView) findViewById(R.id.float_tishi);
    }

    private void addListener() {
        ll_wifi.setOnClickListener(kuaijieListener);
        ll_liuliang.setOnClickListener(kuaijieListener);
        ll_xianshi.setOnClickListener(kuaijieListener);
        ll_shengyin.setOnClickListener(kuaijieListener);
        ll_gps.setOnClickListener(kuaijieListener);
        float_huojian.setOnClickListener(kuaijieListener);
    }

    private void startRamAnimtion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                killAll(BoostSuspensionBallActivity.this);
            }
        }).start();
        CleanManager.getInstance(this).clearRam();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTween();
            }
        }, 200);
        //旋转
        animator2 = ObjectAnimator.ofFloat(float_huan, "rotation", 0, 359f);
        animator2.setDuration(500);
        animator2.setRepeatCount(-1);
        animator2.start();

        animator11 = ObjectAnimator.ofFloat(float_huojian, "translationX",
                0, getResources().getDimension(R.dimen.d100));
        animator12 = ObjectAnimator.ofFloat(float_huojian, "translationY",
                0, -getResources().getDimension(R.dimen.d100));
        animSet1 = new AnimatorSet();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isdoudong = false;
                if (animSet1 != null) {
                    animSet1.play(animator11).with(animator12);
                    animSet1.setDuration(800);
                    animSet1.start();
                }
                animSet1AddListener();
            }
        }, 2500);
    }

    private void animSet1AddListener() {
        if (animSet1 != null) {
            animSet1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    float_huojian.setVisibility(View.GONE);

                    float_memory.setText(MyUtils.getMemory(BoostSuspensionBallActivity.this) + "%");
                    float_tishi.setVisibility(View.VISIBLE);
                    float_memory.setVisibility(View.VISIBLE);
                    float_memory.startAnimation(fang);
                    float_tishi.startAnimation(fang);
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animator2.cancel();
                        }
                    }, 400);
//                            myHandler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    float_tishi.startAnimation(suo);
//                                    float_memory.startAnimation(suo);
//                                    suo.setAnimationListener(new Animation.AnimationListener() {
//                                        @Override
//                                        public void onAnimationStart(Animation animation) {
//
//                                        }
//
//                                        @Override
//                                        public void onAnimationEnd(Animation animation) {
//                                            animator11 = ObjectAnimator.ofFloat(float_huojian, "translationX",
//                                                    0, -getResources().getDimension(R.dimen.d100));
//                                            animator12 = ObjectAnimator.ofFloat(float_huojian, "translationY",
//                                                    0, +getResources().getDimension(R.dimen.d100));
//                                            animSet1.end();
//                                            float_tishi.setVisibility(View.GONE);
//                                            float_memory.setVisibility(View.GONE);
//                                            float_huojian.setVisibility(View.VISIBLE);
//                                            float_huojian.startAnimation(fang);
//                                        }
//
//                                        @Override
//                                        public void onAnimationRepeat(Animation animation) {
//
//                                        }
//                                    });
//                                }
//                            }, 1000);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    View.OnClickListener kuaijieListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.float_huojian:
                    // 开启动画
                    startRamAnimtion();
                    break;
                case R.id.ll_wifi:
                    wifiD();
                    // 开启WiFi
                    SwitchControl.switchWifi(BoostSuspensionBallActivity.this);
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
                        SwitchControl.setMobileData(BoostSuspensionBallActivity.this, !CheckBoostState.networkState(BoostSuspensionBallActivity.this, null));
                    }


                    break;
                case R.id.ll_xianshi:
                    brightnessSwitchUtils(BoostSuspensionBallActivity.this);
                    break;
                case R.id.ll_shengyin:
                    SwitchControl.switchSound(BoostSuspensionBallActivity.this);
                    shengYin();
                    break;
                case R.id.ll_gps:
//                    SwitchControl.toggleGPS(BoostSuspensionBallActivity.this);
                    try {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                killAll(BoostSuspensionBallActivity.this);
            }
        }).start();
        CleanManager.getInstance(this).clearRam();
        isdoudong = true;

    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    private void wifi() {
        if (CheckBoostState.wifiState(this)) {
            iv_wifi.setImageResource(R.mipmap.float_wifi1);
        } else {
            iv_wifi.setImageResource(R.mipmap.float_wifi);
        }
    }


    private void liuLiangd() {
        if (!CheckBoostState.networkState(this, null)) {
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
        if (!CheckBoostState.wifiState(this)) {
            iv_wifi.setImageResource(R.mipmap.float_wifi1);
        } else {
            iv_wifi.setImageResource(R.mipmap.float_wifi);
        }
    }

    private void liuLiang() {
        if (CheckBoostState.networkState(this, null)) {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang1);
        } else {
            iv_liuliang.setImageResource(R.mipmap.float_liuliang);
        }
    }

    private void shengYin() {
        if (CheckBoostState.soundState(this)) {
            iv_shengyin.setImageResource(R.mipmap.float_shengyin1);
        } else {
            iv_shengyin.setImageResource(R.mipmap.float_shengyin);
        }
    }

    private void gps() {
        if (CheckBoostState.gpsState(this)) {
            iv_gps.setImageResource(R.mipmap.float_gps1);
        } else {
            iv_gps.setImageResource(R.mipmap.float_gps);
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

    @Override
    protected void onPause() {
        super.onPause();
        isdoudong = false;
        istween = false;
        if (animator2 != null) {
            animator2.cancel();
        }
    }
}