package com.fast.clean.junk.ui;

import android.content.Intent;
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

import com.fast.clean.mutil.PreData;
import com.fast.clean.mutil.Util;
import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.fast.module.charge.saver.Util.Constants;
import com.fast.module.charge.saver.Util.Utils;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.sample.lottie.LottieAnimationView;
import com.fast.clean.junk.R;
import com.fast.clean.junk.adapter.MSideAdapter;
import com.fast.clean.junk.myview.CpuRoundView;
import com.fast.clean.junk.myview.RamRoundView;
import com.fast.clean.junk.myview.ListViewForScrollView;
import com.fast.clean.junk.myview.MyScrollView;
import com.fast.clean.junk.myview.PullToRefreshLayout;
import com.fast.clean.junk.entity.SideInfo;
import com.fast.clean.junk.presenter.MainPresenter;
import com.fast.clean.junk.util.AdUtil;
import com.fast.clean.junk.util.Constant;
import com.fast.clean.junk.view.MainView;

public class MainActivity extends BaseActivity implements MainView, DrawerLayout.DrawerListener {

    public static final String TAG = "MainActivity";
    TextView main_cpu_temp, main_sd_per, main_sd_size, main_ram_per, main_ram_size;
    RelativeLayout main_junk_button, main_ram_button, main_cooling_button;
    LinearLayout main_manager_button, main_applock_button, ll_lot_ad, main_theme_button;
    LinearLayout main_rotate_all;
    TextView main_rotate_good, main_rotate_bad;
    LinearLayout main_msg_button;
    MyScrollView main_scroll_view;
    PullToRefreshLayout main_pull_refresh;
    ImageView iv_title_right;
    ImageView iv_title_left;
    RelativeLayout main_cpu_air_button, main_sd_air_button, main_ram_air_button;
    ImageView main_huojian;
    CpuRoundView main_custom_cpu, main_custom_sd;
    RamRoundView main_custom_ram;
    LinearLayout main_power_button;
    LinearLayout main_notifi_button;
    LinearLayout main_file_button;
    LinearLayout main_gboost_button;
    LinearLayout main_picture_button;
    TextView main_msg_ram_percent, main_msg_sd_percent, main_msg_cpu_percent;
    com.mingle.widget.LinearLayout ll_ad_full;
    ProgressBar ad_progressbar;
    TextView main_full_time;
    LinearLayout main_battery;

    LottieAnimationView lot_ad;
    ListViewForScrollView side_listView;
    DrawerLayout main_drawer;
    LinearLayout ll_ad, ll_ad_side, ad_native_2, ll_ad_s;

    private String TAG_START_FULL = "acht_start_native";
    private String TAG_EXIT_FULL = "acht_exit_native";
    private String TAG_FULL_PULL = "pull_full";
    private String TAG_MAIN = "acht_main";
    private String TAG_SIDE = "acht_side";
    private String TAG_REFRESH = "drag";

    private Handler handler = new Handler();
    private MainPresenter mainPresenter;
    private MSideAdapter adapter;
    private long mExitTime;
    private int temp;
    private String from;
    private AlertDialog dialog;

    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        //main_all_cercle = (FrameLayout) findViewById(R.id.main_all_cercle);
        main_scroll_view = (MyScrollView) findViewById(R.id.main_scroll_view);
        main_pull_refresh = (PullToRefreshLayout) findViewById(R.id.main_pull_refresh);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);


        main_cpu_air_button = (RelativeLayout) findViewById(R.id.main_cpu_air_button);
        main_custom_cpu = (CpuRoundView) findViewById(R.id.main_custom_cpu);
        main_cpu_temp = (TextView) findViewById(R.id.main_cpu_temp);
        main_sd_air_button = (RelativeLayout) findViewById(R.id.main_sd_air_button);
        main_custom_sd = (CpuRoundView) findViewById(R.id.main_custom_sd);
        main_sd_per = (TextView) findViewById(R.id.main_sd_per);
        main_sd_size = (TextView) findViewById(R.id.main_sd_size);
        main_ram_air_button = (RelativeLayout) findViewById(R.id.main_ram_air_button);
        main_huojian = (ImageView) findViewById(R.id.main_huojian);
        main_custom_ram = (RamRoundView) findViewById(R.id.main_custom_ram);
        main_ram_per = (TextView) findViewById(R.id.main_ram_per);
        main_ram_size = (TextView) findViewById(R.id.main_ram_size);


        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_manager_button = (LinearLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_applock_button = (LinearLayout) findViewById(R.id.main_applock_button);
        ll_lot_ad = (LinearLayout) findViewById(R.id.ll_lot_ad);
        main_theme_button = (LinearLayout) findViewById(R.id.main_theme_button);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_msg_button = (LinearLayout) findViewById(R.id.main_msg_button);
        main_power_button = (LinearLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (LinearLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (LinearLayout) findViewById(R.id.main_file_button);
        main_gboost_button = (LinearLayout) findViewById(R.id.main_gboost_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        main_msg_ram_percent = (TextView) findViewById(R.id.main_msg_ram_percent);
        main_msg_sd_percent = (TextView) findViewById(R.id.main_msg_sd_percent);
        main_msg_cpu_percent = (TextView) findViewById(R.id.main_msg_cpu_percent);
        side_listView = (ListViewForScrollView) findViewById(R.id.side_listView);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_s = (LinearLayout) findViewById(R.id.ll_ad_s);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);

        lot_ad = (LottieAnimationView) findViewById(R.id.lot_ad);
        main_battery = (LinearLayout) findViewById(R.id.main_battery);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);

        AdUtil.track("主页面", "进入主页面", "", 1);
        int main_lot_ad = PreData.getDB(this, Constant.MAIN_LOT_AD, 0);
        if (main_lot_ad % 3 == 0) {
            main_applock_button.setVisibility(View.GONE);
            ll_lot_ad.setVisibility(View.VISIBLE);
            lot_ad.setImageAssetsFolder("images/box/");
            lot_ad.setAnimation("box.json");
            lot_ad.loop(true);
            lot_ad.playAnimation();
        } else {
            main_applock_button.setVisibility(View.VISIBLE);
            ll_lot_ad.setVisibility(View.GONE);
        }
        PreData.putDB(this, Constant.MAIN_LOT_AD, ++main_lot_ad);

    }


    //初始化监听
    public void onClick() {
        //main_scroll_view.setOnTouchListener(scrollViewTouchListener);
        main_pull_refresh.setOnRefreshListener(refreshListener);
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_cpu_air_button.setOnClickListener(onClickListener);
        main_sd_air_button.setOnClickListener(onClickListener);
        main_ram_air_button.setOnClickListener(onClickListener);
        main_huojian.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_applock_button.setOnClickListener(onClickListener);
        ll_lot_ad.setOnClickListener(onClickListener);
        main_theme_button.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_msg_button.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        lot_ad.setOnClickListener(onClickListener);


    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
        main_custom_cpu.startProgress(false, temp);
        main_custom_cpu.setCustomRoundListener(new CpuRoundView.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main_cpu_temp.setText(String.valueOf(progress) + "℃");
                        main_msg_cpu_percent.setText(String.valueOf(progress) + "℃");
                        if (temp < 50) {
                            main_msg_cpu_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A4));
                        } else if (temp < 60) {
                            main_msg_cpu_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A3));
                        } else {
                            main_msg_cpu_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A2));
                        }

                    }
                });
            }
        });

    }

    int sdProgress;


    @Override
    public void initSideData() {
        if (adapter == null) {
            adapter = new MSideAdapter(this);
            side_listView.setAdapter(adapter);
        }
        adapter.clear();

        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
//        adapter.addData(new SideInfo(R.string.privary_0, R.mipmap.side_power));//隐私清理
        adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, true)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, Constant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评

    }

    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        main_custom_sd.startProgress(true, percent);
        final Runnable sdRunable = new Runnable() {
            @Override
            public void run() {
                main_sd_per.setText(String.valueOf(sdProgress) + "%");
            }
        };
        main_custom_sd.setCustomRoundListener(new CpuRoundView.CustomRoundListener() {
            @Override
            public void progressUpdate(int progress) {
                MainActivity.this.sdProgress = progress;
                handler.post(sdRunable);
            }

        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main_sd_size.setText(size);
                main_msg_sd_percent.setText(Util.convertStorage(sd_kongxian, true));
                if (percent < 40) {
                    main_msg_sd_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A4));
                } else if (percent < 80) {
                    main_msg_sd_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A3));
                } else {
                    main_msg_sd_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A2));
                }
            }
        });

    }

    @Override
    public void initRam(final int percent, final String size) {
        main_custom_ram.startProgress(false, percent);
        main_custom_ram.setCustomRoundListener(new RamRoundView.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main_ram_per.setText(String.valueOf(progress) + "%");

                        main_ram_size.setText(size);
                        main_msg_ram_percent.setText(String.valueOf(progress) + "%");
                        if (progress < 40) {
                            main_msg_ram_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A4));
                        } else if (progress < 80) {
                            main_msg_ram_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A3));
                        } else {
                            main_msg_ram_percent.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.A2));
                        }
                    }
                });
            }
        });

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

    @Override
    public void loadFullAd() {
        if (PreData.getDB(this, Constant.FULL_MAIN, 0) == 1) {
        } else {
            View nativeView = AdUtil.getNativeAdView(TAG_MAIN, R.layout.native_ad_2);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                Log.e("aaa", "=====" + layout_ad.height);
                if (nativeView.getHeight() == Util.dp2px(250)) {
                    layout_ad.height = Util.dp2px(250);
                }
                Log.e("ad_mob", "h=" + nativeView.getHeight() + "w=" + nativeView.getWidth());
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
                ll_ad.setGravity(Gravity.CENTER_HORIZONTAL);
                main_scroll_view.setScrollY(0);
//                main_scroll_view.fullScroll(ScrollView.FOCUS_UP);

            } else {
                ll_ad.setVisibility(View.GONE);
            }
            View nativeView_side = AdUtil.getNativeAdView(TAG_SIDE, R.layout.native_ad_2);
            if (ll_ad_side != null && nativeView_side != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_side.getLayoutParams();
                layout_ad.height = nativeView_side.getMeasuredHeight();
                ll_ad_side.setLayoutParams(layout_ad);
                ll_ad_side.addView(nativeView_side);
            }

        }
        if (PreData.getDB(this, Constant.FIRST_BATTERY, true)) {
            PreData.putDB(this, Constant.FIRST_BATTERY, false);
            Utils.writeData(MainActivity.this, Constants.CHARGE_SAVER_SWITCH, false);
            main_battery.setVisibility(View.VISIBLE);
            ImageView battery_cha = (ImageView) findViewById(R.id.battery_cha);
            Button battery_button = (Button) findViewById(R.id.battery_button);
            battery_cha.setOnClickListener(onClickListener);
            battery_button.setOnClickListener(onClickListener);
            return;
        }

        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.showFullAd("acht_start_full");

        } else {
            View nativeView_full = AdUtil.getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_main);
            if (ll_ad_full != null && nativeView_full != null) {
                ll_ad_full.addView(nativeView_full);
                ll_ad_full.setVisibility(View.VISIBLE);
                nativeView_full.findViewById(R.id.ad_delete).setVisibility(View.GONE);
                main_full_time = (TextView) nativeView_full.findViewById(R.id.main_full_time);
                LinearLayout loading_text = (LinearLayout) nativeView_full.findViewById(R.id.loading_text);
                loading_text.setOnClickListener(null);
                main_full_time.setVisibility(View.VISIBLE);
                main_full_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.removeCallbacks(fullAdRunnale);
                        adDelete();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                main_pull_refresh.autoRefresh();
                            }
                        }, 2000);
                    }
                });
                int skip = PreData.getDB(this, Constant.SKIP_TIME, 6);
                handler.postDelayed(fullAdRunnale, skip * 1000);
            } else {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main_pull_refresh.autoRefresh();
                    }
                }, 2000);
            }
        }
    }

    Runnable fullAdRunnale = new Runnable() {
        @Override
        public void run() {
            adDelete();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    main_pull_refresh.autoRefresh();
                }
            }, 2000);
        }
    };

    //上拉刷新监听
    PullToRefreshLayout.OnRefreshListener refreshListener = new PullToRefreshLayout.OnRefreshListener() {
        // 下拉刷新操作
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            AndroidSdk.loadNativeAd(TAG_REFRESH, R.layout.native_ad_6, new ClientNativeAd.NativeAdLoadListener() {
                @Override
                public void onNativeAdLoadSuccess(View view) {
                    main_pull_refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                    main_scroll_view.setSadSuccess(true);
                    if (ll_ad_s != null) {
                        final ImageView ad_cha = (ImageView) view.findViewById(R.id.ad_cha);
                        ll_ad_s.addView(view);
                        ll_ad_s.setVisibility(View.VISIBLE);
                        main_scroll_view.isTouch = false;
                        main_scroll_view.smoothScrollToSlowS(1000);
                        ad_cha.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ad_cha.setOnClickListener(null);
                                main_scroll_view.smoothScrollToSlowS();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        main_scroll_view.setSadSuccess(false);
                                        ll_ad_s.removeAllViews();
                                        ll_ad_s.setVisibility(View.GONE);
                                        main_scroll_view.smoothScrollToSlowS(1);
                                    }
                                }, 500);

                            }
                        });

                    }
                }

                @Override
                public void onNativeAdLoadFails() {
                    main_pull_refresh.refreshFinish(PullToRefreshLayout.FAIL);
                }
            });
        }

        //上拉加载操作
        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            AdUtil.track("主页面", "刷新成功", "", 1);
            AndroidSdk.loadNativeAd(TAG_FULL_PULL, R.layout.native_ad_full, new ClientNativeAd.NativeAdLoadListener() {
                @Override
                public void onNativeAdLoadSuccess(View view) {
                    main_pull_refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    main_scroll_view.setXadSuccess(true);
                    if (ad_native_2 != null) {
                        ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
                        layout_ad.height = main_scroll_view.getMeasuredHeight() - getResources().getDimensionPixelSize(R.dimen.d9);
                        Log.e("success_ad", "hiegt=" + main_scroll_view.getMeasuredHeight());
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


  /*  public void loadValueAnimator(ValueAnimator animation) {
        int hight = (int) animation.getAnimatedValue();
        Log.e("animaterh", "===" + cercleHeight + "===" + hight);
        cercle_linearParams.height = hight;
        //main_all_cercle.setLayoutParams(cercle_linearParams);
        main_scale_all.setScaleY((float) cercle_linearParams.height / );
        main_scale_all.setScaleX((float) cercle_linearParams.height / cercle_value);
    }*/

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
                case R.id.main_cpu_air_button:
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "main");
                    bundle.putInt("wendu", temp);
                    AdUtil.track("主页面", "点击cpu球进入降温页面", "", 1);
                    mainPresenter.jumpToActivity(CpuCoolingActivity.class, bundle, 1);
                    break;
                case R.id.main_sd_air_button:
                    AdUtil.track("主页面", "点击sd球进入垃圾清理页面", "", 1);
                    mainPresenter.jumpToActivity(FileLajiActivity.class, 1);
                    break;
                case R.id.main_ram_air_button:
                    AdUtil.track("主页面", "点击ram球进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(NeicunAvtivity.class, 1);
                    break;
                case R.id.main_huojian:
                    AdUtil.track("主页面", "点击火箭进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(FileLajiAndRamActivity.class, 1);
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(FileLajiActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(NeicunAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    AdUtil.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(AppManagerActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    AdUtil.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from", "main");
                    bundle1.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(CpuCoolingActivity.class, bundle1, 1);
                    break;
                case R.id.main_applock_button:
                    AdUtil.track("主页面", "点击深度清理按钮", "", 1);
                    mainPresenter.jumpToActivity(PowerActivity.class, 1);
                    break;
                case R.id.main_theme_button:
                    AdUtil.track("主页面", "点击进入buton游戏加速", "", 1);
                    mainPresenter.jumpToActivity(DyxGboostActivity.class, 1);
                    break;
                case R.id.lot_ad:
                    AdUtil.track("主页面", "点击广告礼包", "", 1);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.tran_left_in);
                    ll_ad_full.startAnimation(animation);
                    ll_ad_full.setVisibility(View.VISIBLE);
                    ad_progressbar.setVisibility(View.VISIBLE);
                    ll_ad_full.removeAllViews();
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            AndroidSdk.loadNativeAd(TAG_START_FULL, R.layout.native_ad_full_main, new ClientNativeAd.NativeAdLoadListener() {
                                @Override
                                public void onNativeAdLoadSuccess(View view) {
                                    LinearLayout loading_text = (LinearLayout) view.findViewById(R.id.loading_text);
                                    loading_text.setOnClickListener(null);
                                    ImageView ad_delete = (ImageView) view.findViewById(R.id.ad_delete);
                                    ad_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            adDelete();
                                        }
                                    });
                                    ll_ad_full.addView(view);
                                    ad_progressbar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onNativeAdLoadFails() {
                                    showToast(getString(R.string.load_fails));
                                    adDelete();
                                    ad_progressbar.setVisibility(View.GONE);
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

                    break;

                case R.id.main_rotate_good:
                    AdUtil.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(true);
                    break;
                case R.id.main_rotate_bad:
                    AdUtil.track("主页面", "点击好评bad按钮", "", 1);
                    mainPresenter.clickRotate(false);
                    break;
                case R.id.main_msg_button:
                    AdUtil.track("主页面", "点击进入硬件信息", "", 1);
                    mainPresenter.jumpToActivity(PhoneMessageActivity.class, 1);
                    break;
                case R.id.main_power_button:
                    AdUtil.track("主页面", "点击进入深度清理", "", 1);
                    mainPresenter.jumpToActivity(PowerActivity.class, 1);
                    break;
                case R.id.main_file_button:
                    AdUtil.track("主页面", "点击进入文件管理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.FILE_CLEAN, true);
                    mainPresenter.jumpToActivity(FileManagerActivity.class, 1);
                    break;
                case R.id.main_gboost_button:
                    AdUtil.track("主页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(MainActivity.this, Constant.GBOOST_CLEAN, true);
                    mainPresenter.jumpToActivity(DyxGboostActivity.class, 1);
                    break;
                case R.id.main_picture_button:
                    AdUtil.track("主页面", "点击进入相似图片", "", 1);
                    PreData.putDB(MainActivity.this, Constant.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(PictureActivity.class, 1);
                    break;
                case R.id.main_notifi_button:
                    AdUtil.track("主页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(MainActivity.this) || !PreData.getDB(MainActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(MainActivity.this, NotifiInfoActivity.class);
                        startActivityForResult(intent6, 1);
                    } else {
                        Intent intent6 = new Intent(MainActivity.this, NotifiActivity.class);
                        startActivityForResult(intent6, 1);
                    }
                    break;
                case R.id.battery_button:
                    main_battery.setVisibility(View.GONE);
                    Utils.writeData(MainActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                    initSideData();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            main_pull_refresh.autoRefresh();
                        }
                    }, 2000);
                    adapter.notifyDataSetChanged();
                    AdUtil.track("主界面", "充电屏保引导", "叉掉", 1);
                    break;
                case R.id.battery_cha:
                    main_battery.setVisibility(View.GONE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            main_pull_refresh.autoRefresh();
                        }
                    }, 2000);
                    AdUtil.track("主界面", "充电屏保引导", "打开", 1);
                    break;


            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.SETTING_RESUIL) {
            initSideData();
            adapter.notifyDataSetChanged();
        } else if (resultCode == Constant.COOLING_RESUIL) {
            int wendu = data.getIntExtra("wendu", 0);
            temp -= wendu;
            if (temp == 0) {
                temp = 40;
            }
        } else if (requestCode == 100) {
            if (Util.isNotificationListenEnabled(MainActivity.this)) {
                PreData.putDB(MainActivity.this, Constant.KEY_NOTIFI, true);
                Intent intent = new Intent(MainActivity.this, NotifiActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(MainActivity.this, NotifiInfoActivity.class);
                startActivityForResult(intent, 1);
            }
        } else if (resultCode == Constant.RAM_RESUIL) {
        } else if (resultCode == Constant.JUNK_RESUIL) {
        } else if (resultCode == Constant.POWER_RESUIL) {

        }

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
        if (lot_ad != null) {
            lot_ad.pauseAnimation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        Log.e("ad_mob_l", "h=" + ll_ad.getHeight() + "w=" + ll_ad.getWidth());
        if (lot_ad != null) {
            lot_ad.playAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lot_ad != null) {
            lot_ad.clearAnimation();
        }
    }

    public void onBackPressed() {
        if (ll_ad_full.getVisibility() == View.VISIBLE) {
            adDelete();
            handler.removeCallbacks(fullAdRunnale);
            return;
        }
        if (main_battery.getVisibility() == View.VISIBLE) {
            main_battery.setVisibility(View.GONE);
            AdUtil.track("主界面", "充电屏保引导", "返回键返回", 1);
            return;
        }
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {
            if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
                AndroidSdk.showFullAd("acht_exit_full");
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
        int[] loc = new int[2];
        lot_ad.getLocationOnScreen(loc);
        CRAnimation crA = new CircularRevealCompat(ll_ad_full).circularReveal(loc[0] + lot_ad.getWidth() / 2,
                loc[1] + lot_ad.getHeight() / 2, ll_ad_full.getHeight(), 0);
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

    public int[] getLocation(View v) {
        int[] loc = new int[4];
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        loc[0] = location[0];
        loc[1] = location[1];
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);

        loc[2] = v.getMeasuredWidth();
        loc[3] = v.getMeasuredHeight();

        //base = computeWH();
        return loc;
    }

    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_exit, null);
        LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 0) {
            View nativeExit = AdUtil.getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_full_exit);
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
        if (PreData.getDB(this, Constant.IS_ACTION_BAR, true)) {
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
    public void onDrawerSlide(View drawerView, float slideOffset) {

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
}
