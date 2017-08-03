package com.privacy.junk.activityprivacy;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.mingle.privacycircletreveal.CircularRevealCompat;
import com.mingle.widgetprivacy.animation.CRAnimation;
import com.mingle.widgetprivacy.animation.SimpleAnimListener;
import com.privacy.clean.core.CleanManager;
import com.privacy.clean.entity.JunkInfo;
import com.privacy.clean.utilsprivacy.MyUtils;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.junk.R;
import com.privacy.junk.customadapterprivacy.PrivacySidebarAdapter;
import com.privacy.junk.interfaceviewprivacy.MainViewPrivacy;
import com.privacy.junk.presenterprivacy.PrivacyPresenterMain;
import com.privacy.junk.privacycustomview.ListViewForScrollViewPrivacy;
import com.privacy.junk.privacycustomview.MainYuanHuView;
import com.privacy.junk.privacycustomview.PrivacyCustomRoundCpu;
import com.privacy.junk.privacycustomview.PrivacyYuanHuView;
import com.privacy.junk.privacycustomview.PullToRefreshLayout;
import com.privacy.junk.privacycustomview.ScrollView;
import com.privacy.junk.privacymodel.SideInfo;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.toolsprivacy.SetAdUtilPrivacy;
import com.privacy.module.charge.saver.privacyutils.BatteryConstantsPrivacy;
import com.privacy.module.charge.saver.privacyutils.UtilsPrivacy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseActivity implements MainViewPrivacy, DrawerLayout.DrawerListener {
    // cprivacy
    private String TAG_START_FULL = "cprivacy_start_native";
    private String TAG_EXIT_FULL = "cprivacy_exit_native";
    private String TAG_FULL_PULL = "pull_full";
    private String TAG_MAIN = "cprivacy_main";
    private String TAG_SIDE = "cprivacy_side";
    private String TAG_REFRESH = "drag";
    public static final String TAG = "MainActivity";
    PrivacyCustomRoundCpu main_custom_cpu, main_custom_ram, main_custom_sd;
    RelativeLayout main_cooling_button;
    ObjectAnimator objectAnimator;
    ScrollView main_scroll_view;
    LinearLayout iv_title_right;
    LinearLayout iv_title_left;
    ListViewForScrollViewPrivacy side_listView;
    DrawerLayout main_drawer;
    TextView main_msg_sd_percent_danwei;
    LinearLayout ll_ad, ll_ad_side, ad_native_2, ll_ad_s;
    RelativeLayout main_junk_button;
    LinearLayout ll_ad_full;
    LinearLayout main_rotate_all;
    TextView main_full_time;
    MainYuanHuView main_yunahuan;
    private PrivacySidebarAdapter adapter;
    RelativeLayout main_power_button, main_file_button, main_notifi_button, main_picture_button2, main_picture_button, main_ram_button;
    LinearLayout main_manager_button;
    RelativeLayout main_gboost_button;
    TextView main_msg_ram_percent, main_msg_sd_percent, main_msg_cpu_percent;
    private ArrayList<JunkInfo> startList;
    TextView main_fenshu;
    ImageView main_point;
    long junk_size;
    LinearLayout main_battery;
    LinearLayout main_clean_lin;
    RelativeLayout main_title;
    TextView main_junk_huan;
    private int temp;
    private AlertDialog dialog;
    private Handler handler = new Handler();
    private PrivacyPresenterMain mainPresenter;
    TextView main_rotate_good, main_rotate_bad;
    ImageView main_rotate_close;
    LinearLayout main_msg_button;
    ProgressBar ad_progressbar;
    PullToRefreshLayout main_pull_refresh;
    String junkSize;
    PrivacyYuanHuView main_msg_ram_yuanhu, main_msg_sd_yuanhu, main_msg_cpu_yuanhu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        if (PreData.getDB(this, MyConstantPrivacy.FILEACTIVITY, 1) == 0) {
            main_file_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstantPrivacy.NOTIFIACTIVITY, 1) == 0) {
            main_notifi_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstantPrivacy.POWERACTIVITY, 1) == 0) {
            main_power_button.setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }
        mainPresenter = new PrivacyPresenterMain(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);
        SetAdUtilPrivacy.track("主页面", "进入主页面", "", 1);
    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
        main_custom_cpu.startProgress(false, temp);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 设置硬件信息里的CPU温度
                main_msg_cpu_percent.setText(String.valueOf(temp) + "℃");
                main_msg_cpu_yuanhu.setDuShu(temp * 3.6f, ContextCompat.getColor(MainActivity.this, R.color.A9), 225);
            }
        });
    }

    private void setColorAnimation(View view, int startColor, int endColor) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", startColor, endColor);
        colorAnim.setDuration(2000);
        colorAnim.setRepeatCount(0);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
    }

    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        main_scroll_view = (ScrollView) findViewById(R.id.main_scroll_view);
        iv_title_right = (LinearLayout) findViewById(R.id.iv_title_right);
        iv_title_left = (LinearLayout) findViewById(R.id.iv_title_left);
        main_fenshu = (TextView) findViewById(R.id.main_fenshu);
        main_clean_lin = (LinearLayout) findViewById(R.id.main_clean_lin);
        main_pull_refresh = (PullToRefreshLayout) findViewById(R.id.main_pull_refresh);
        main_junk_huan = (TextView) findViewById(R.id.main_junk_huan);
        main_title = (RelativeLayout) findViewById(R.id.main_title);
        main_msg_sd_percent_danwei = (TextView) findViewById(R.id.main_msg_sd_percent_danwei);
        main_point = (ImageView) findViewById(R.id.main_point);
        main_msg_ram_percent = (TextView) findViewById(R.id.main_msg_ram_percent);
        main_msg_sd_percent = (TextView) findViewById(R.id.main_msg_sd_percent);
        main_msg_cpu_percent = (TextView) findViewById(R.id.main_msg_cpu_percent);
        main_custom_cpu = (PrivacyCustomRoundCpu) findViewById(R.id.main_custom_cpu);
        main_custom_ram = (PrivacyCustomRoundCpu) findViewById(R.id.main_custom_ram);
        main_custom_sd = (PrivacyCustomRoundCpu) findViewById(R.id.main_custom_sd);
        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_rotate_close = (ImageView) findViewById(R.id.main_rotate_close);
        main_msg_button = (LinearLayout) findViewById(R.id.main_msg_button);
        main_power_button = (RelativeLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (RelativeLayout) findViewById(R.id.main_notifi_button);
        main_picture_button2 = (RelativeLayout) findViewById(R.id.main_picture_button2);
        main_picture_button = (RelativeLayout) findViewById(R.id.main_picture_button);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_manager_button = (LinearLayout) findViewById(R.id.main_manager_button);
        main_file_button = (RelativeLayout) findViewById(R.id.main_file_button);
        main_gboost_button = (RelativeLayout) findViewById(R.id.main_gboost_button);
        side_listView = (ListViewForScrollViewPrivacy) findViewById(R.id.side_listview);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_s = (LinearLayout) findViewById(R.id.ll_ad_s);
        ll_ad_full = (com.mingle.widgetprivacy.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);
        main_battery = (LinearLayout) findViewById(R.id.main_battery);

        main_msg_ram_yuanhu = (PrivacyYuanHuView) findViewById(R.id.main_msg_ram_yuanhu);
        main_msg_sd_yuanhu = (PrivacyYuanHuView) findViewById(R.id.main_msg_sd_yuanhu);
        main_msg_cpu_yuanhu = (PrivacyYuanHuView) findViewById(R.id.main_msg_cpu_yuanhu);
        main_yunahuan = (MainYuanHuView) findViewById(R.id.main_yunahuan);
    }

    //初始化监听
    public void onClick() {
        main_pull_refresh.setOnRefreshListener(refreshListener);
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_rotate_close.setOnClickListener(onClickListener);
        main_msg_button.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button2.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_clean_lin.setOnClickListener(onClickListener);

    }

    //上拉刷新监听
    PullToRefreshLayout.OnRefreshListener refreshListener = new PullToRefreshLayout.OnRefreshListener() {
        // 下拉刷新操作
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        }

        //上拉加载操作
        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            SetAdUtilPrivacy.track("主页面", "刷新成功", "", 1);
            AndroidSdk.loadNativeAd(TAG_FULL_PULL, R.layout.native_ad_full, new ClientNativeAd.NativeAdLoadListener() {
                @Override
                public void onNativeAdLoadSuccess(View view) {
                    main_pull_refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    main_scroll_view.setXadSuccess(true);
                    if (ad_native_2 != null) {
                        ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
                        layout_ad.height = main_scroll_view.getMeasuredHeight() - getResources().getDimensionPixelSize(R.dimen.d9);
                        ad_native_2.setLayoutParams(layout_ad);
                        ad_native_2.addView(view);
                        ad_native_2.setVisibility(View.VISIBLE);
                        main_scroll_view.isTouch = false;
                        main_scroll_view.smoothScrollToSlow(2000);
                    }
                }

                @Override
                public void onNativeAdLoadFails() {
                    main_pull_refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            });
        }
    };

    private void setColor(int percent, boolean isReStart) {
    }

    @Override
    public void initSideData() {
        if (adapter == null) {
            adapter = new PrivacySidebarAdapter(this);
        }
        adapter.clear();

        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) UtilsPrivacy.readData(this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, false)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, MyConstantPrivacy.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, MyConstantPrivacy.POWERACTIVITY, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, MyConstantPrivacy.NOTIFIACTIVITY, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        }
        adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片清理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, MyConstantPrivacy.FILEACTIVITY, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        }
        adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.side_gboost));//游戏加速
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评
        side_listView.setAdapter(adapter);
    }

    @Override
    public void initQiu(final int fenshu, boolean isReStart) {
        setColor(fenshu, isReStart);
        main_yunahuan.start(fenshu * 2.4f);
        main_yunahuan.setScanCallBack(new MainYuanHuView.DrawYuanHuListener() {
            @Override
            public void scanDushu(final float dushu) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dushu == -1 || dushu == -2) {
                            if (objectAnimator != null) {
                                objectAnimator.end();
                            }
                            dushu1 = 0;
                            return;
                        }
                        if (dushu == 1) {
                            dushu1 = 0;
                        }
                        mianTemp += 1.2;
                        objectAnimator = ObjectAnimator.ofFloat(main_point, "rotation", dushu1, dushu);
                        dushu1 = dushu;
                        objectAnimator.start();
                        if (mianTemp == 2.4f) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    main_fenshu.setText(String.valueOf((int) (dushu / 2.4f)));
                                }
                            });
                            mianTemp = 0;
                        } else if (dushu == -2) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    main_fenshu.setText(String.valueOf(fenshu));
                                }
                            });
                            mianTemp = 0;
                        } else if (dushu == 240) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    main_fenshu.setText(String.valueOf(100));
                                }
                            });
                            mianTemp = 0;
                        }
                    }
                });
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (int i = 0; i <= fenshu; i++) {
                    if (onDestroyed) {
                        break;
                    }
                    final long finalI = i;
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();
    }


    @Override
    public void openDrawer() {
        main_drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void loadFullAd() {
        if (PreData.getDB(this, MyConstantPrivacy.FULL_MAIN, 0) == 1) {
        } else {
            View nativeView = SetAdUtilPrivacy.getNativeAdView(TAG_MAIN, R.layout.native_ad_2);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                if (nativeView.getHeight() == MyUtils.dp2px(250)) {
                    layout_ad.height = MyUtils.dp2px(250);
                }
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
                ll_ad.setGravity(Gravity.CENTER_HORIZONTAL);
                ll_ad.setVisibility(View.VISIBLE);
                final Animation animation = AnimationUtils.loadAnimation(this, R.anim.tran_left_in);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_ad.startAnimation(animation);
                    }
                }, 1000);
                main_scroll_view.setScrollY(0);
//                main_scroll_view.fullScroll(ScrollView.FOCUS_UP);

            } else {
                ll_ad.setVisibility(View.GONE);
            }
            View nativeView_side = SetAdUtilPrivacy.getNativeAdView(TAG_SIDE, R.layout.native_ad_2);
            if (ll_ad_side != null && nativeView_side != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_side.getLayoutParams();
                layout_ad.height = nativeView_side.getMeasuredHeight();
                ll_ad_side.setLayoutParams(layout_ad);
                ll_ad_side.addView(nativeView_side);
            }

        }
        //提示
        if (PreData.getDB(this, MyConstantPrivacy.FIRST_BATTERY, true)) {
            UtilsPrivacy.writeData(MainActivity.this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, false);
            main_battery.setVisibility(View.VISIBLE);
            LinearLayout battery_cha = (LinearLayout) findViewById(R.id.battery_cha);
            Button battery_button = (Button) findViewById(R.id.battery_button);
            battery_cha.setOnClickListener(onClickListener);
            battery_button.setOnClickListener(onClickListener);
            return;
        }

        if (PreData.getDB(this, MyConstantPrivacy.FULL_START, 0) == 1) {
            AndroidSdk.showFullAd("cprivacy_start_full");
        } else {
            //loading页面广告
            View nativeView_full = SetAdUtilPrivacy.getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_main);
            if (ll_ad_full != null && nativeView_full != null) {
                ll_ad_full.addView(nativeView_full);
                ll_ad_full.setVisibility(View.VISIBLE);
                nativeView_full.findViewById(R.id.ad_delete).setVisibility(View.GONE);
                main_full_time = (TextView) nativeView_full.findViewById(R.id.main_full_time);
                RelativeLayout loading_text = (RelativeLayout) nativeView_full.findViewById(R.id.loading_text);
                loading_text.setOnClickListener(null);
                main_full_time.setVisibility(View.VISIBLE);
                main_full_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.removeCallbacks(fullAdRunnale);
                        adDelete();
                    }
                });
                int skip = PreData.getDB(this, MyConstantPrivacy.SKIP_TIME, 6);
                handler.postDelayed(fullAdRunnale, skip * 1000);
            } else {
            }
        }
    }

    @Override
    public void setRotateGone() {
        main_rotate_all.setVisibility(View.GONE);
    }

    Runnable fullAdRunnale = new Runnable() {
        @Override
        public void run() {
            adDelete();
        }
    };


    //点击事件监听
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_title_left:
                    mainPresenter.openDrawer();
                    SetAdUtilPrivacy.track("主页面", "点击进入侧边栏按钮", "", 1);
                    break;
                case R.id.iv_title_right:
                    SetAdUtilPrivacy.track("主页面", "点击进入设置页面", "", 1);
                    mainPresenter.jumpToActivity(SetActivityPrivacy.class, 1);
                    break;
                case R.id.main_junk_button:
                    SetAdUtilPrivacy.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(RubbishActivityPrivacy.class, 1);
                    break;
                case R.id.main_clean_lin:
                    SetAdUtilPrivacy.track("主页面", "点击垃圾所有按钮", "", 1);
                    mainPresenter.jumpToActivity(PrivacyRubbishAndRamActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    SetAdUtilPrivacy.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from", "main");
                    bundle1.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(PrivacyBatteriesActivity.class, bundle1, 1);
                    break;
                case R.id.main_rotate_good:
                    SetAdUtilPrivacy.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(true);
                    break;
                case R.id.main_rotate_bad:
                    SetAdUtilPrivacy.track("主页面", "点击好评bad按钮", "", 1);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.IS_ROTATE, true);
                    setRotateGone();
                    break;
                case R.id.main_rotate_close:
                    SetAdUtilPrivacy.track("主页面", "点击好评close按钮", "", 1);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.IS_ROTATE, true);
                    setRotateGone();
                    break;
                case R.id.main_msg_button:
                    SetAdUtilPrivacy.track("主页面", "点击进入硬件信息", "", 1);
                    mainPresenter.jumpToActivity(PhonesMessageActivityPrivacy.class, 1);
                    break;
                case R.id.main_power_button:
                    SetAdUtilPrivacy.track("主页面", "点击进入深度清理", "", 1);
                    mainPresenter.jumpToActivity(DeepingActivityPrivacy.class, 1);
                    break;
                case R.id.main_file_button:
                    SetAdUtilPrivacy.track("主页面", "点击进入文件管理", "", 1);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.FILE_CLEAN, true);
                    mainPresenter.jumpToActivity(FileManagerActivityPrivacy.class, 1);
                    break;
                case R.id.main_gboost_button:
                    SetAdUtilPrivacy.track("主页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.GBOOST_CLEAN, true);
                    mainPresenter.jumpToActivity(GoodGameActivityPrivacy.class, 1);
                    break;
                case R.id.main_picture_button2:
                    SetAdUtilPrivacy.track("主页面", "点击进入相似图片", "", 1);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(PictActivityPrivacy.class, 1);
                    break;
                case R.id.main_picture_button:
                    SetAdUtilPrivacy.track("主页面", "点击进入相似图片", "", 1);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(PictActivityPrivacy.class, 1);
                    break;
                case R.id.main_ram_button:
                    SetAdUtilPrivacy.track("主页面", "点击进入相似图片", "", 1);
                    mainPresenter.jumpToActivity(PrivacyMemoryAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    SetAdUtilPrivacy.track("主页面", "点击进入应用管理", "", 1);
                    mainPresenter.jumpToActivity(AllAppsActivityPrivacy.class, 1);
                    break;
                case R.id.main_notifi_button:
                    SetAdUtilPrivacy.track("主页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.NOTIFI_CLEAN, true);
                    if (!MyUtils.isNotificationListenEnabled(MainActivity.this) || !PreData.getDB(MainActivity.this, MyConstantPrivacy.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(MainActivity.this, PrivacyNotifingAnimationActivity.class);
                        startActivityForResult(intent6, 1);
                    } else {
                        Intent intent6 = new Intent(MainActivity.this, MyNotifingActivityPrivacy.class);
                        startActivityForResult(intent6, 1);
                    }
                    break;
                case R.id.battery_button:
                    main_battery.setVisibility(View.GONE);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.FIRST_BATTERY, false);
                    UtilsPrivacy.writeData(MainActivity.this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true);
                    initSideData();
                    adapter.notifyDataSetChanged();
                    SetAdUtilPrivacy.track("主界面", "充电屏保引导", "打开", 1);
                    break;
                case R.id.battery_cha:
                    main_battery.setVisibility(View.GONE);
                    PreData.putDB(MainActivity.this, MyConstantPrivacy.FIRST_BATTERY, false);
                    UtilsPrivacy.writeData(MainActivity.this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, false);
                    SetAdUtilPrivacy.track("主界面", "充电屏保引导", "叉掉", 1);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MyConstantPrivacy.SETTING_RESUIL) {
            initSideData();
            adapter.notifyDataSetChanged();
        } else if (resultCode == MyConstantPrivacy.COOLING_RESUIL) {
            int wendu = data.getIntExtra("wendu", 0);
            temp -= wendu;
            if (temp == 0) {
                temp = 40;
            }
        } else if (requestCode == 100) {
            if (MyUtils.isNotificationListenEnabled(MainActivity.this)) {
                PreData.putDB(MainActivity.this, MyConstantPrivacy.KEY_NOTIFI, true);
                Intent intent = new Intent(MainActivity.this, MyNotifingActivityPrivacy.class);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(MainActivity.this, PrivacyNotifingAnimationActivity.class);
                startActivityForResult(intent, 1);
            }
        } else if (resultCode == MyConstantPrivacy.RAM_RESUIL) {
        } else if (resultCode == MyConstantPrivacy.JUNK_RESUIL) {
        } else if (resultCode == MyConstantPrivacy.POWER_RESUIL) {

        }
    }

    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        final String[] arr = size.split("/");

        main_custom_sd.startProgress(true, percent);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //根据剩余的空间大小来设置单位
                if (sd_kongxian < 1024) {
                    junkSize = MyUtils.convertStorage(sd_kongxian, false) + "B";
                    //设置硬件信息里的剩余空间
                    main_msg_sd_percent.setText(junkSize.substring(0, junkSize.length() - 2));
                    main_msg_sd_percent_danwei.setText("B");
                } else if (sd_kongxian < 1048576) {
                    junkSize = MyUtils.convertStorage(sd_kongxian, false) + "KB";
                    //设置硬件信息里的剩余空间
                    main_msg_sd_percent.setText(junkSize.substring(0, junkSize.length() - 2));
                    main_msg_sd_percent_danwei.setText("KB");
                } else if (sd_kongxian < 1073741824) {
                    junkSize = MyUtils.convertStorage(sd_kongxian, false) + "MB";
                    //设置硬件信息里的剩余空间
                    main_msg_sd_percent.setText(junkSize.substring(0, junkSize.length() - 2));
                    main_msg_sd_percent_danwei.setText("MB");
                } else {
                    junkSize = MyUtils.convertStorage(sd_kongxian, false) + "GB";
                    //设置硬件信息里的剩余空间
                    main_msg_sd_percent.setText(junkSize.substring(0, junkSize.length() - 2));
                    main_msg_sd_percent_danwei.setText("GB");
                }
                main_msg_sd_yuanhu.setDuShu((100 - percent) * 3.6f, ContextCompat.getColor(MainActivity.this, R.color.A7), 135);

                junk_size = CleanManager.getInstance(MainActivity.this).getApkSize() + CleanManager.getInstance(MainActivity.this).getCacheSize() +
                        CleanManager.getInstance(MainActivity.this).getUnloadSize() + CleanManager.getInstance(MainActivity.this).getLogSize() + CleanManager.getInstance(MainActivity.this).getDataSize();
                // MyUtils.convertStorage(junk_size, true) true返回的带单位，false不带单位
                long ramSize = CleanManager.getInstance(MainActivity.this).getRamSize();
                main_junk_huan.setText(getResources().getString(R.string.main_junk_file) + " " + MyUtils.convertStorage(junk_size + ramSize, true) + "B");
            }
        });
    }

    @Override
    public void initRam(final int percent, final String size) {
        Log.e("sdfsd", "========" + size + "===" + percent);
        main_custom_ram.startProgress(false, percent);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 设置硬件信息里的内存
                main_msg_ram_percent.setText(String.valueOf(percent) + "%");
                main_msg_ram_yuanhu.setDuShu(percent * 3.6f, ContextCompat.getColor(MainActivity.this, R.color.A1), 90);
            }
        });
    }

    float dushu1 = 0;
    float mianTemp = 0;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainPresenter.reStart(true);
        initCpu(temp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (objectAnimator != null) {
            objectAnimator.pause();
        }
        if (main_yunahuan != null) {
            main_yunahuan.stop();
        }
    }

    private void initData() {
        startList = new ArrayList<>();
        for (JunkInfo info : CleanManager.getInstance(this).getAppRamList()) {
            if (info.isSelfBoot) {
                startList.add(info);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        initData();
//        power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        // 充电屏保关闭智能充电，刷新无效果，重新调用 initSideData()可以
//        adapter.notifyDataSetChanged();
        initSideData();
    }

    public void onBackPressed() {
        if (ll_ad_full.getVisibility() == View.VISIBLE) {
            adDelete();
            handler.removeCallbacks(fullAdRunnale);
            return;
        }
        if (main_battery.getVisibility() == View.VISIBLE) {
            main_battery.setVisibility(View.GONE);
            SetAdUtilPrivacy.track("主界面", "充电屏保引导", "返回键返回", 1);
            return;
        }
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {
            if (PreData.getDB(this, MyConstantPrivacy.FULL_EXIT, 0) == 1) {
                AndroidSdk.showFullAd("cprivacy_exit_full");
            }
            showExitDialog();
        }
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    private void adDelete() {
        if (ll_ad_full == null) {
            return;
        }
        if (onPause || !onResume) {
            ll_ad_full.setVisibility(View.GONE);
            return;
        }
        int[] loc = new int[2];
//        lot_ad.getLocationOnScreen(loc);
        CRAnimation crA = new CircularRevealCompat(ll_ad_full).circularReveal(loc[0],
                loc[1], ll_ad_full.getHeight(), 0);
        if (crA != null) {
            crA.addListener(new SimpleAnimListener() {
                @Override
                public void onAnimationEnd(CRAnimation animation) {
                    super.onAnimationEnd(animation);
                    ll_ad_full.setVisibility(View.GONE);
                }
            });
            crA.start();
        }
    }

    //退出
    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_exit, null);
        LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (PreData.getDB(this, MyConstantPrivacy.FULL_EXIT, 0) == 0) {
            View nativeExit = SetAdUtilPrivacy.getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_2);
            if (nativeExit != null) {
                ll_ad_exit.addView(nativeExit);
                ll_ad_exit.setVisibility(View.VISIBLE);
            }
        }
        exit_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        exit_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = new AlertDialog.Builder(this, R.style.add_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        lp.height = dm.heightPixels; //设置高度
        if (PreData.getDB(this, MyConstantPrivacy.IS_ACTION_BAR, true)) {
            int uiOptions =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            //布局位于状态栏下方
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            //隐藏导航栏
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            if (Build.VERSION.SDK_INT >= 19) {
                uiOptions |= 0x00001000;
            } else {
                uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
            }
            dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        Log.e(TAG, "onDrawerOpened");
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        Log.e(TAG, "onDrawerClosed");
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (objectAnimator != null) {
            objectAnimator.pause();
        }
    }

    public String getAnZhuangTime() {
        String str = null;
        try {
            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            String appFile = applicationInfo.sourceDir;
            long installed = new File(appFile).lastModified();
            Date installDate = new Date(installed);
            str = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(installDate);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return str;
    }
}
