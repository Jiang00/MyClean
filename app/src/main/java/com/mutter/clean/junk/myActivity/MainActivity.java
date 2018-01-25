package com.mutter.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.util.LruCache;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AdListener;
import com.frigate.utils.AutoUtils;
import com.mutter.clean.core.CleanManager;
import com.mutter.clean.imageclean.ImageInfo;
import com.mutter.clean.imageclean.RecyclerDbHelper;
import com.mutter.clean.junk.entity.TuiguangInfo;
import com.mutter.clean.junk.myview.LoadingTime;
import com.mutter.clean.junk.myview.conpentview.AutoImageText2View;
import com.mutter.clean.junk.myview.conpentview.AutoImageTextView;
import com.mutter.clean.junk.myview.conpentview.AutoRotateXView;
import com.mutter.clean.junk.myview.conpentview.AutoRoundView;
import com.mutter.clean.junk.myview.conpentview.GrildRecycleView;
import com.mutter.clean.junk.util.BadgerCount;
import com.mutter.clean.junk.util.UtilGp;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.mutter.module.charge.saver.Util.Constants;
import com.mutter.module.charge.saver.Util.Utils;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myAdapter.SideAdapter;
import com.mutter.clean.junk.myview.RoundRam;
import com.mutter.clean.junk.myview.RoundSd;
import com.mutter.clean.junk.myview.MyScrollView;
import com.mutter.clean.junk.myview.PullToRefreshLayout;
import com.mutter.clean.junk.entity.SideInfo;
import com.mutter.clean.junk.presenter.MainPresenter;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.view.MainView;
import com.mutter.ui.demo.entry.CrossItem;
import com.mutter.ui.demo.util.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends BaseActivity implements MainView, DrawerLayout.DrawerListener {

    ImageView iv_title_right;
    ImageView iv_title_left;
    ImageView menu_hong;
    AutoRoundView main_sd_air_button;
    AutoRoundView main_ram_air_button;
    public static final String TAG = "MainActivity";
    MyScrollView main_scroll_view;
    AutoImageTextView main_junk_button, main_ram_button, main_cooling_button;
    FrameLayout main_rotate_all;
    TextView main_rotate_good, main_rotate_bad;
    ImageView main_rotate_cha;
    AutoImageTextView main_manager_button;
    AutoImageText2View main_power_button;
    AutoImageText2View main_gboost_button;
    AutoImageText2View main_picture_button;
    ListView side_listView;
    AutoImageText2View main_notifi_button;
    AutoImageText2View main_file_button;
    DrawerLayout main_drawer;
    LinearLayout ll_ad_side, ad_native_2;
    com.mingle.widget.LinearLayout ll_ad_full;
    TextView main_full_time;
    ImageView main_circle;
    LinearLayout tuiguang_main;
    LinearLayout tuiguang_side;
    RelativeLayout tuiguang_side_title;
    FrameLayout libao_load;
    ImageView load_2, load_2_2, load_3, load_1, load_4;

    // LottieAnimationView lot_side;
    ImageView side_title;
    ImageView lot_family;
    AutoRotateXView ad_delete;
    private LoadingTime ad_loading;
    private GrildRecycleView recyc_tuiguang;


    private String TAG_MAIN = "mutter_main";
    private String TAG_HUA = "mutter_hua";
    private String TAG_SIDE = "mutter_side";
    private String TAG_START_FULL = "mutter_start_native";
    private String TAG_EXIT_FULL = "mutter_exit_native";
    private String TAG_FULL_PULL = "mutter_native";//pull_full

    private MyApplication cleanApplication;
    private Handler handler = new Handler();
    private MainPresenter mainPresenter;
    private SideAdapter adapter;
    private long mExitTime;
    private int temp;
    /* private PagerAdapter pagerAdapter;
     private View pageView;*/

    private String from;
    private AlertDialog dialog;
    private LinearLayout deep;
    private FrameLayout lot_tap;
    private AlertDialog dialogB;
    private ObjectAnimator load_rotate;
    private String LOADING_FULL = "loading_full";
    private String EXIT_FULL = "loading_full";//mutter_exit_full
    private ObjectAnimator translate;


    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        main_scroll_view = (MyScrollView) findViewById(R.id.main_scroll_view);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        menu_hong = (ImageView) findViewById(R.id.menu_hong);


        main_sd_air_button = (AutoRoundView) findViewById(R.id.main_sd_air_button);
        main_ram_air_button = (AutoRoundView) findViewById(R.id.main_ram_air_button);

        main_rotate_all = (FrameLayout) findViewById(R.id.main_rotate_all);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_rotate_cha = (ImageView) findViewById(R.id.main_rotate_cha);
        main_power_button = (AutoImageText2View) findViewById(R.id.main_power_button);
        main_notifi_button = (AutoImageText2View) findViewById(R.id.main_notifi_button);
        main_file_button = (AutoImageText2View) findViewById(R.id.main_file_button);
        main_gboost_button = (AutoImageText2View) findViewById(R.id.main_gboost_button);
        main_picture_button = (AutoImageText2View) findViewById(R.id.main_picture_button);
        side_listView = (ListView) findViewById(R.id.side_listView);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);

        side_title = (ImageView) findViewById(R.id.side_title);
        ad_delete = (AutoRotateXView) findViewById(R.id.ad_delete);
        lot_family = (ImageView) ad_delete.findViewById(R.id.lot_family);
        main_circle = (ImageView) ad_delete.findViewById(R.id.main_circle);
        tuiguang_main = (LinearLayout) findViewById(R.id.tuiguang_main);
        tuiguang_side = (LinearLayout) findViewById(R.id.tuiguang_side);
        tuiguang_side_title = (RelativeLayout) findViewById(R.id.tuiguang_side_title);
        libao_load = (FrameLayout) findViewById(R.id.libao_load);
        load_1 = (ImageView) findViewById(R.id.load_1);
        load_2 = (ImageView) findViewById(R.id.load_2);
        load_2_2 = (ImageView) findViewById(R.id.load_2_2);
        load_3 = (ImageView) findViewById(R.id.load_3);
        load_4 = (ImageView) findViewById(R.id.load_4);
        recyc_tuiguang = (GrildRecycleView) findViewById(R.id.recyc_tuiguang);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        View cv = ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
        cleanApplication = (MyApplication) getApplication();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
            PreData.putDB(this, Constant.HONG_NOTIFI, false);
        }
        if (PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) == 0) {
            main_notifi_button.setVisibility(View.GONE);
            PreData.putDB(this, Constant.HONG_NOTIFI, false);
        }
        if (PreData.getDB(this, Constant.DEEP_KAIGUAN, 1) == 0) {
            main_power_button.setVisibility(View.GONE);
            PreData.putDB(this, Constant.HONG_DEEP, false);
        }
        if (PreData.getDB(this, Constant.FILE_KAIGUAN, 1) == 0) {
            main_file_button.setVisibility(View.GONE);
            PreData.putDB(this, Constant.HONG_FILE, false);
        }
        if (PreData.getDB(this, Constant.GBOOST_KAIGUAN, 1) == 0) {
            main_gboost_button.setVisibility(View.GONE);
            PreData.putDB(this, Constant.HONG_GBOOST, false);
        }
        if (PreData.getDB(this, Constant.PICTURE_KAIGUAN, 1) == 0) {
            main_picture_button.setVisibility(View.GONE);
            PreData.putDB(this, Constant.HONG_PHOTO, false);
        }
        if (PreData.getDB(this, Constant.HONG_RAM, true) || PreData.getDB(this, Constant.HONG_JUNK, true) ||
                PreData.getDB(this, Constant.HONG_COOLING, true) || PreData.getDB(this, Constant.HONG_MESSAGE, true) || PreData.getDB(this, Constant.HONG_NOTIFI, true) ||
                PreData.getDB(this, Constant.HONG_FILE, true) || PreData.getDB(this, Constant.HONG_MANAGER, true) ||
                PreData.getDB(this, Constant.HONG_DEEP, true) || PreData.getDB(this, Constant.HONG_PHOTO, true) || PreData.getDB(this, Constant.HONG_GBOOST, true)) {
            menu_hong.setVisibility(View.VISIBLE);
        } else {
            menu_hong.setVisibility(View.GONE);
        }
        BadgerCount.setCount(this);

        main_junk_button = (AutoImageTextView) findViewById(R.id.main_junk_button);
        main_ram_button = (AutoImageTextView) findViewById(R.id.main_ram_button);
        main_manager_button = (AutoImageTextView) findViewById(R.id.main_manager_button);
        main_cooling_button = (AutoImageTextView) findViewById(R.id.main_cooling_button);


        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);

        AdUtil.track("主页面", "进入主页面", "", 1);
        tuiguang(TUIGUAN_MAIN_SOFT, true, tuiguang_main);
        tuiguang(TUIGUAN_MAIN, false, tuiguang_main);
        tuiguang(TUIGUAN_SIDE_SOFT, true, tuiguang_side);
        tuiguang(TUIGUAN_SIDE, false, tuiguang_side);
        ArrayList<TuiguangInfo> tuiguangList = new ArrayList<>();
        ArrayList<CrossItem> crossItems_soft = JsonParser.getCrossData(this, AndroidSdk.getExtraData(), TUIGUAN_MAIN_SOFT);
        if (crossItems_soft != null) {
            for (int i = 0; i < crossItems_soft.size(); i++) {
                CrossItem item = crossItems_soft.get(i);
                TuiguangInfo info = new TuiguangInfo();
                info.action = item.action;
                info.packageName = item.getPkgName();
                info.title = item.title;
                info.url = item.getTagIconUrl();
                tuiguangList.add(info);
                AdUtil.track("交叉推广_广告位", "广告位_主界面", "展示" + info.packageName, 1);
            }
        }
        ArrayList<CrossItem> crossItems = JsonParser.getCrossData(this, AndroidSdk.getExtraData(), TUIGUAN_MAIN);
        if (crossItems != null) {
            for (int i = 0; i < crossItems.size(); i++) {
                CrossItem item = crossItems.get(i);
                TuiguangInfo info = new TuiguangInfo();
                info.action = item.action;
                info.packageName = item.getPkgName();
                info.title = item.title;
                info.url = item.getTagIconUrl();
                tuiguangList.add(info);
                AdUtil.track("交叉推广_广告位", "广告位_主界面", "展示" + info.packageName, 1);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) == 1) {
            TuiguangInfo info = new TuiguangInfo();
            info.title = getString(R.string.side_notifi);
            info.drable_id = R.mipmap.tuiguang_notifi;
            tuiguangList.add(info);
        }
        TuiguangInfo info = new TuiguangInfo();
        info.title = getString(R.string.side_rotate);
        info.drable_id = R.mipmap.tuiguang_rotate;
        tuiguangList.add(info);
        recyc_tuiguang.setData(tuiguangList);
//        recyc_tuiguang.setLayoutManager(new GridLayoutManager(this, 3));
//        recyc_tuiguang.setAdapter(new HuiAdapter(tuiguangList));
        if (tuiguang_side.getChildCount() == 0) {
            tuiguang_side_title.setVisibility(View.GONE);
        }
        initSideData();
    }

    private void startKuai(final AnimatorSet animatorset, View view, int time) {
        ObjectAnimator animator_1 = ObjectAnimator.ofFloat(view, View.SCALE_X, 1, 1.2f, 0.9f, 1.1f, 1);
        animator_1.setDuration(800);
        ObjectAnimator animator_2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1, 0.8f, 1.1f, 0.9f, 1);
        animator_2.setDuration(800);
        animatorset.play(animator_1).with(animator_2);
        animatorset.setInterpolator(new DecelerateInterpolator());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animatorset != null && !animatorset.isRunning()) {
                    animatorset.start();
                }
                handler.postDelayed(this, 10400);
            }
        }, time);
    }


    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        main_sd_air_button.setMemory(percent);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main_sd_air_button.setUsage(size);

            }
        });

    }

    @Override
    public void initRam(final int percent, final String size) {
        main_ram_air_button.setMemory(percent);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main_ram_air_button.setUsage(size);
            }
        });

    }


    //初始化监听
    public void onClick() {
        //main_scroll_view.setOnTouchListener(scrollViewTouchListener);
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_sd_air_button.setOnClickListener(onClickListener);
        main_ram_air_button.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_cha.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        lot_family.setOnClickListener(onClickListener);
        main_circle.setOnClickListener(onClickListener);


    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
    }

    public void initSideData() {
        if (adapter == null) {
            adapter = new SideAdapter(this);
            side_listView.setAdapter(adapter);
        }
        adapter.clear();
        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, false)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, Constant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.main_cooling_name, R.mipmap.side_battery));//电池降温
        if (PreData.getDB(this, Constant.DEEP_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        }
        if (PreData.getDB(this, Constant.GBOOST_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
        }
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        if (PreData.getDB(this, Constant.PICTURE_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        }
        if (PreData.getDB(this, Constant.FILE_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        }
        adapter.addData(new SideInfo(R.string.main_msg_title, R.mipmap.side_message));//硬件信息
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评


    }

    @Override
    public void loadFullAd() {
        if (PreData.getDB(this, Constant.FULL_MAIN, 0) == 1) {
        } else {
            View nativeView_side = AdUtil.getNativeAdView(TAG_SIDE, R.layout.native_ad_2);
            if (ll_ad_side != null && nativeView_side != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_side.getLayoutParams();
                layout_ad.height = nativeView_side.getMeasuredHeight();
                ll_ad_side.setLayoutParams(layout_ad);
                ll_ad_side.addView(nativeView_side);
            }

        }

        if (!PreData.getDB(this, Constant.BATTERY_FIRST, false)) {
            showBattery();
            PreData.putDB(this, Constant.BATTERY_FIRST, true);
//            return;
        }

        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
//        if (true) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
            AndroidSdk.showFullAd(LOADING_FULL);
            Log.e("adadad", "Showloading_full");
//                }
//            }, 10000);

        } else {
            // TODO: 2017/10/31
            View nativeView_full = AdUtil.getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_main);
            if (ll_ad_full != null && nativeView_full != null) {
                ll_ad_full.addView(nativeView_full);
                ll_ad_full.setVisibility(View.VISIBLE);
                LinearLayout loading_text = (LinearLayout) nativeView_full.findViewById(R.id.loading_text);
                loading_text.setOnClickListener(null);
                ad_loading = (LoadingTime) nativeView_full.findViewById(R.id.ad_loading);
                ad_loading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad_loading.cancle();
                    }
                });
                ad_loading.startProgress();
                ad_loading.setCustomRoundListener(new LoadingTime.CustomRoundListener() {
                    @Override
                    public void progressUpdate() {
                        handler.post(fullAdRunnale);
                    }
                });
//                int skip = PreData.getDB(this, Constant.SKIP_TIME, 6);
//                handler.postDelayed(fullAdRunnale, skip * 1000);
            }
        }
    }

    @Override
    public void loadAirAnimator(TranslateAnimation translate) {
    }

    @Override
    public void setRotateGone() {
        main_rotate_all.setVisibility(View.GONE);
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

    Runnable fullAdRunnale = new Runnable() {
        @Override
        public void run() {
            adDelete();
        }
    };

    private void showBattery() {
        View view = getLayoutInflater().inflate(R.layout.dialog_battery, null);
        TextView battery_cha = (TextView) view.findViewById(R.id.battery_cancel);
        TextView battery_button = (TextView) view.findViewById(R.id.battery_ok);
        battery_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogB.dismiss();
            }
        });
        battery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.writeData(MainActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                initSideData();
                adapter.notifyDataSetChanged();
                dialogB.dismiss();
            }
        });
        dialogB = new AlertDialog.Builder(this).create();
        dialogB.setCanceledOnTouchOutside(false);
        dialogB.setView(view);
        dialogB.show();
    }


    //点击事件监听
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.iv_title_left:
                    mainPresenter.openDrawer();
                    AdUtil.track("主页面", "点击进入侧边栏按钮", "", 1);

                    break;
                case R.id.iv_title_right:
                    AdUtil.track("主页面", "点击进入设置页面", "", 1);
                    mainPresenter.jumpToActivity(SettingActivity.class, 1);
                    break;
                case R.id.main_sd_air_button:
                    AdUtil.track("主页面", "点击sd球进入垃圾清理页面", "", 1);
                    mainPresenter.jumpToActivity(CleanActivity.class, 1);
                    break;
                case R.id.main_ram_air_button:
                    AdUtil.track("主页面", "点击ram球进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(CleanActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    AdUtil.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(UserAppActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    AdUtil.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from", "main");
                    bundle1.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(JiangwenActivity.class, bundle1, 1);
                    break;
                case R.id.lot_family:
                    AdUtil.track("主页面", "点击广告礼包", "", 1);
                    if (PreData.getDB(MainActivity.this, Constant.FULL_START, 0) == 1) {
                        AndroidSdk.loadFullAd(LOADING_FULL, new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                if (load_rotate != null) {
                                    load_rotate.removeAllListeners();
                                    load_rotate.cancel();
                                }
                                libao_load.setVisibility(View.GONE);
                            }
                        });
                        load_2.setVisibility(View.VISIBLE);
                        load_1.setVisibility(View.VISIBLE);
                        load_3.setVisibility(View.VISIBLE);
                        load_2_2.setVisibility(View.VISIBLE);
                        load_4.setVisibility(View.GONE);
                        load_rotate = ObjectAnimator.ofFloat(load_1, View.ROTATION, 0, 3600);
                        load_rotate.setDuration(4000);
                        load_rotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            int count = 0;

                            public void onAnimationUpdate(ValueAnimator animation) {
                                count++;
                                if (count % 15 == 0) {
                                    if (load_2.getVisibility() == View.VISIBLE) {
                                        load_2.setVisibility(View.GONE);
                                    } else {
                                        load_2.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                        load_rotate.start();
                        libao_load.setVisibility(View.VISIBLE);
                        handler.postDelayed(runnable_load, 4500);
                    } else {
                        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.tran_left_in);
                        ll_ad_full.startAnimation(animation);
                        ll_ad_full.setVisibility(View.VISIBLE);
                        ll_ad_full.removeAllViews();
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                AndroidSdk.loadNativeAd(TAG_START_FULL, R.layout.native_ad_full_main, new ClientNativeAd.NativeAdLoadListener() {
                                    @Override
                                    public void onNativeAdLoadSuccess(View view) {
                                        LinearLayout loading_text = (LinearLayout) view.findViewById(R.id.loading_text);
                                        loading_text.setOnClickListener(null);
                                        LoadingTime ad_loading = (LoadingTime) view.findViewById(R.id.ad_loading);
                                        ad_loading.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                adDelete();
                                            }
                                        });
                                        ll_ad_full.addView(view);
                                    }

                                    @Override
                                    public void onNativeAdLoadFails() {
                                        showToast(getString(R.string.load_fails));
                                        adDelete();
                                    }
                                });
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }

                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                        });
                    }

                    break;
                case R.id.main_circle:
                    AdUtil.track("主页面", "点击进入清理所有页面", "", 1);
                    mainPresenter.jumpToActivity(CleanAndRamActivity.class, 1);
                    break;

                case R.id.main_rotate_good:
                    AdUtil.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(true);
                    break;
                case R.id.main_rotate_bad:
                    AdUtil.track("主页面", "点击好评bad按钮", "", 1);
                    mainPresenter.clickRotate(false);
                    break;
                case R.id.main_rotate_cha:
                    AdUtil.track("主页面", "点击好评cha按钮", "", 1);
                    mainPresenter.deleteRotate();
                    break;
                case R.id.main_power_button:
                    AdUtil.track("主页面", "点击进入深度清理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.DEEP_CLEAN, true);
                    mainPresenter.jumpToActivity(PowerActivity.class, 1);
                    break;
                case R.id.main_file_button:
                    AdUtil.track("主页面", "点击进入文件管理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.FILE_CLEAN, true);
                    mainPresenter.jumpToActivity(FileManaActivity.class, 1);
                    break;
                case R.id.main_gboost_button:
                    AdUtil.track("主页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(MainActivity.this, Constant.GBOOST_CLEAN, true);
                    mainPresenter.jumpToActivity(GameActivity.class, 1);
                    break;
                case R.id.main_picture_button:
                    AdUtil.track("主页面", "点击进入相似图片", "", 1);
                    PreData.putDB(MainActivity.this, Constant.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(SimilarActivity.class, 1);
                    break;
                case R.id.main_notifi_button:
                    AdUtil.track("主页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(MainActivity.this) || !PreData.getDB(MainActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(MainActivity.this, NotifiAnimationActivity.class);
                        startActivityForResult(intent6, 1);
                    } else {
                        Intent intent6 = new Intent(MainActivity.this, NotifiActivity.class);
                        startActivityForResult(intent6, 1);
                    }
                    break;


            }
        }
    };

    Runnable runnable_load = new Runnable() {
        @Override
        public void run() {
            AndroidSdk.showFullAd(LOADING_FULL);

            if (libao_load != null) {
                libao_load.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.SETTING_RESUIL) {

        } else if (resultCode == Constant.COOLING_RESUIL) {
            int wendu = data.getIntExtra("wendu", 0);
            temp -= wendu;
            if (temp == 0) {
                temp = 40;
            }

        }
        initSideData();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainPresenter.reStart();
        initCpu(temp);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AndroidSdk.onResumeWithoutTransition(this);
        AndroidSdk.loadFullAd(EXIT_FULL, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (ad_loading != null) {
            ad_loading.cancle();
        }
        if (translate != null) {
            translate.cancel();
        }

    }

    public void onBackPressed() {
        if (ll_ad_full.getVisibility() == View.VISIBLE) {
            adDelete();
            handler.removeCallbacks(fullAdRunnale);
            return;
        }
        if (libao_load.getVisibility() == View.VISIBLE) {
            libao_load.setVisibility(View.GONE);
            if (load_rotate != null) {
                load_rotate.removeAllListeners();
                load_rotate.cancel();
            }
            handler.removeCallbacks(runnable_load);
            return;
        }
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {
            if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
                AndroidSdk.showFullAd(EXIT_FULL);
            }
            showExitDialog();
        }
    }

    private void adDelete() {
        if (ll_ad_full == null) {
            return;
        }
        if (onPause || !onResume) {
            ll_ad_full.setVisibility(View.GONE);
            return;
        }

        CRAnimation crA = new CircularRevealCompat(ll_ad_full).circularReveal(ad_delete.getLeft() + ad_delete.getWidth() / 2,
                ad_delete.getTop() + ad_delete.getWidth() / 2, ll_ad_full.getHeight(), 0);
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

    public static View getNativeAd(String tag, @LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd(tag)) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout(tag, layout, null);
        if (nativeView == null) {
            return null;
        }

        if (nativeView != null) {
            ViewGroup viewParent = (ViewGroup) nativeView.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
            }
        }
        return nativeView;
    }

    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_exit, null);
        final LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (PreData.getDB(this, Constant.FULL_EDIT_NATIVE, 0) == 1) {
            View nativeExit = AdUtil.getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_6);
            if (nativeExit != null) {
                ll_ad_exit.addView(nativeExit);
                ll_ad_exit.setVisibility(View.INVISIBLE);
//                int w = View.MeasureSpec.makeMeasureSpec(0,
//                        View.MeasureSpec.UNSPECIFIED);
//                int h = View.MeasureSpec.makeMeasureSpec(0,
//                        View.MeasureSpec.UNSPECIFIED);
//                ll_ad_exit.measure(w, h);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        translate = ObjectAnimator.ofFloat(ll_ad_exit, View.TRANSLATION_X, -ll_ad_exit.getWidth(), 0);
                        translate.setDuration(500);
                        translate.start();
                        ll_ad_exit.setVisibility(View.VISIBLE);
                    }
                }, 500);

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
        dialog = new AlertDialog.Builder(this, R.style.exit_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (PreData.getDB(this, Constant.HONG_RAM, true) || PreData.getDB(this, Constant.HONG_JUNK, true) ||
                PreData.getDB(this, Constant.HONG_COOLING, true) || PreData.getDB(this, Constant.HONG_MESSAGE, true) || PreData.getDB(this, Constant.HONG_NOTIFI, true) ||
                PreData.getDB(this, Constant.HONG_FILE, true) || PreData.getDB(this, Constant.HONG_MANAGER, true) ||
                PreData.getDB(this, Constant.HONG_DEEP, true) || PreData.getDB(this, Constant.HONG_PHOTO, true) || PreData.getDB(this, Constant.HONG_GBOOST, true)) {
            menu_hong.setVisibility(View.VISIBLE);
        } else {
            menu_hong.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    class HuiAdapter extends RecyclerView.Adapter<HuiAdapter.HomeViewHolder> {
        ArrayList<TuiguangInfo> list;

        public HuiAdapter(ArrayList<TuiguangInfo> list) {
            this.list = list;
        }


        public HuiAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HuiAdapter.HomeViewHolder holder = new HuiAdapter.HomeViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.layout_tuiguang_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final HuiAdapter.HomeViewHolder holder, final int position) {
            final TuiguangInfo info = list.get(position);
            holder.recyc_name.setText(info.title);
            if (info.drable_id == -1) {
                holder.recyc_ad.setVisibility(View.VISIBLE);
                Util.loadImg(MainActivity.this, info.url, R.mipmap.icon, holder.recyc_icon);
                holder.recycle_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.mutter.ui.demo.util.Utils.reactionForAction(MainActivity.this, AndroidSdk.getExtraData(), info.packageName, info.action);
                        AdUtil.track("交叉推广_广告位", "广告位_主界面", "点击" + info.packageName, 1);
                    }
                });
            } else if (info.drable_id == R.mipmap.tuiguang_notifi) {
                holder.recyc_ad.setVisibility(View.INVISIBLE);
                holder.recyc_icon.setImageResource(info.drable_id);
                holder.recycle_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Util.isNotificationListenEnabled(MainActivity.this) || !PreData.getDB(MainActivity.this, Constant.KEY_NOTIFI, true)) {
                            Intent intent6 = new Intent(MainActivity.this, NotifiAnimationActivity.class);
                            startActivityForResult(intent6, 1);
                        } else {
                            Intent intent6 = new Intent(MainActivity.this, NotifiActivity.class);
                            startActivityForResult(intent6, 1);
                        }
                    }
                });
            } else {
                holder.recyc_ad.setVisibility(View.INVISIBLE);
                holder.recyc_icon.setImageResource(info.drable_id);
                holder.recycle_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdUtil.track("主页面", "点击方格好评", "", 1);
                        UtilGp.rate(MainActivity.this);
                        PreData.putDB(MainActivity.this, Constant.IS_ROTATE, true);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }

        class HomeViewHolder extends RecyclerView.ViewHolder {
            ImageView recyc_icon;
            TextView recyc_name;
            TextView recyc_ad;
            LinearLayout recycle_item;

            public HomeViewHolder(View view) {
                super(view);
                AutoUtils.autoSize(itemView);
                recycle_item = (LinearLayout) view.findViewById(R.id.recycle_item);
                recyc_icon = (ImageView) view.findViewById(R.id.recyc_icon);
                recyc_name = (TextView) view.findViewById(R.id.recyc_name);
                recyc_ad = (TextView) view.findViewById(R.id.recyc_ad);
            }
        }
    }

}
