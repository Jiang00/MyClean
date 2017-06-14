package com.supers.clean.junk.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.entity.JunkInfo;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.my.module.charge.saver.Util.Constants;
import com.my.module.charge.saver.Util.Utils;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.rd.PageIndicatorView;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.SideAdapter;
import com.supers.clean.junk.customeview.CustomRoundCpu;
import com.supers.clean.junk.customeview.ListViewForScrollView;
import com.supers.clean.junk.customeview.MyScrollView;
import com.supers.clean.junk.customeview.PullToRefreshLayout;
import com.supers.clean.junk.entity.SideInfo;
import com.supers.clean.junk.presenter.MainPresenter;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.view.MainView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainView, DrawerLayout.DrawerListener {

    public static final String TAG = "MainActivity";
    MyScrollView main_scroll_view;
    PullToRefreshLayout main_pull_refresh;
    FrameLayout main_scale_all;
    ImageView iv_title_right;
    ImageView iv_title_left;
    RelativeLayout main_cpu_air_button, main_sd_air_button, main_ram_air_button;
    CustomRoundCpu main_custom_cpu, main_custom_sd, main_custom_ram;
    TextView main_cpu_temp, main_sd_per, main_sd_size, main_ram_per, main_ram_size;
    LinearLayout main_air_all;
    RelativeLayout main_junk_button, main_ram_button, main_cooling_button;
    LinearLayout main_manager_button, main_applock_button, main_theme_button;
    TextView main_junk_h, main_ram_h, main_cooling_h;
    LinearLayout main_rotate_all;
    ImageView main_rotate_good;
    LinearLayout main_msg_button;
    LinearLayout main_power_button;
    LinearLayout main_notifi_button;
    LinearLayout main_file_button;
    LinearLayout main_gboost_button;
    LinearLayout main_picture_button;
    TextView main_msg_ram_percent, main_msg_sd_percent, main_msg_sd_unit, main_msg_cpu_percent;
    TextView main_gurad_num;
    ImageView main_guard_rotate;
    FrameLayout main_guard_all;
    ListViewForScrollView side_listView;
    DrawerLayout main_drawer;
    LinearLayout ll_ad, ll_ad_side, ad_native_2;
    com.mingle.widget.LinearLayout ll_ad_full;
    ProgressBar ad_progressbar;
    TextView main_full_time;

    // LottieAnimationView lot_side;
    FrameLayout fl_lot_side;
    ImageView side_title;
    LottieAnimationView lot_main;
    LottieAnimationView lot_main_tap;
    LottieAnimationView lot_family;

    LottieAnimationView lot_side;

    private String TAG_MAIN = "eos_main";
    private String TAG_HUA = "eos_hua";
    private String TAG_SIDE = "eos_side";
    private String TAG_START_FULL = "eos_start_native";
    private String TAG_EXIT_FULL = "eos_exit_native";
    private String TAG_FULL_PULL = "pull_full";

    private MyApplication cleanApplication;
    private Handler handler = new Handler();
    private MainPresenter mainPresenter;
    private SideAdapter adapter;
    private long mExitTime;
    private int temp;
    private ViewPager viewpager;
    PageIndicatorView pageindicatorview;
    PagerAdapter pagerAdapter;
    /* private PagerAdapter pagerAdapter;
     private View pageView;*/

    private boolean mDrawerOpened = false;
    private View viewpager_3;
    private String from;
    private AlertDialog dialog;
    private LinearLayout deep;
    private FrameLayout lot_tap;
    private ArrayList<View> arrayList;//记录main_circle布局文件的控件绑定信息

    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        //main_all_cercle = (FrameLayout) findViewById(R.id.main_all_cercle);
        main_scroll_view = (MyScrollView) findViewById(R.id.main_scroll_view);
        main_pull_refresh = (PullToRefreshLayout) findViewById(R.id.main_pull_refresh);
        main_scale_all = (FrameLayout) findViewById(R.id.main_scale_all);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);

        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_manager_button = (LinearLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_applock_button = (LinearLayout) findViewById(R.id.main_applock_button);
        main_theme_button = (LinearLayout) findViewById(R.id.main_theme_button);
        main_junk_h = (TextView) findViewById(R.id.main_junk_h);
        main_ram_h = (TextView) findViewById(R.id.main_ram_h);
        main_cooling_h = (TextView) findViewById(R.id.main_cooling_h);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_good = (ImageView) findViewById(R.id.main_rotate_good);
        main_msg_button = (LinearLayout) findViewById(R.id.main_msg_button);
        main_power_button = (LinearLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (LinearLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (LinearLayout) findViewById(R.id.main_file_button);
        main_gboost_button = (LinearLayout) findViewById(R.id.main_gboost_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        main_msg_ram_percent = (TextView) findViewById(R.id.main_msg_ram_percent);
        main_msg_sd_percent = (TextView) findViewById(R.id.main_msg_sd_percent);
        main_msg_sd_unit = (TextView) findViewById(R.id.main_msg_sd_unit);
        main_msg_cpu_percent = (TextView) findViewById(R.id.main_msg_cpu_percent);
        main_gurad_num = (TextView) findViewById(R.id.main_gurad_num);
        main_guard_rotate = (ImageView) findViewById(R.id.main_guard_rotate);
        main_guard_all = (FrameLayout) findViewById(R.id.main_guard_all);
        side_listView = (ListViewForScrollView) findViewById(R.id.side_listView);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);

        //lot_side = (LottieAnimationView) findViewById(R.id.lot_side);
        fl_lot_side = (FrameLayout) findViewById(R.id.fl_lot_side);
        side_title = (ImageView) findViewById(R.id.side_title);
        lot_family = (LottieAnimationView) findViewById(R.id.lot_family);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        cleanApplication = (MyApplication) getApplication();

      /*  new Thread() {
            @Override
            public void run() {
                super.run();
                // 返回系统包名
                if (Util.isRoot()) {
                    String apkRoot = "chmod 777 " + getPackageCodePath();
                    Util.RootCommand(apkRoot);
                }
            }
        }.start();*/
        //Build.VERSION.SDK_INT  判断Android SDK版本号
        // Build.VERSION_CODES.KITKAT  是一个Android的版本4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }
        tuiGuang();

        arrayList = new ArrayList<>();
        // 获得指定的布局文件，然后通过其对象绑定控件
        View view = LayoutInflater.from(this).inflate(R.layout.main_circle, null);
        main_cpu_air_button = (RelativeLayout) view.findViewById(R.id.main_cpu_air_button);
        main_custom_cpu = (CustomRoundCpu) view.findViewById(R.id.main_custom_cpu);
        main_cpu_temp = (TextView) view.findViewById(R.id.main_cpu_temp);
        main_sd_air_button = (RelativeLayout) view.findViewById(R.id.main_sd_air_button);
        main_custom_sd = (CustomRoundCpu) view.findViewById(R.id.main_custom_sd);
        main_sd_per = (TextView) view.findViewById(R.id.main_sd_per);
        main_sd_size = (TextView) view.findViewById(R.id.main_sd_size);
        main_ram_air_button = (RelativeLayout) view.findViewById(R.id.main_ram_air_button);
        main_custom_ram = (CustomRoundCpu) view.findViewById(R.id.main_custom_ram);
        main_ram_per = (TextView) view.findViewById(R.id.main_ram_per);
        main_ram_size = (TextView) view.findViewById(R.id.main_ram_size);
        main_air_all = (LinearLayout) view.findViewById(R.id.main_air_all);
        arrayList.add(view);

        // 广告
        View viewpager_2 = LayoutInflater.from(this).inflate(R.layout.main_ad, null);
        LinearLayout view_ad = (LinearLayout) viewpager_2.findViewById(R.id.view_ad);
        // native_ad_2是点击广告跳转的界面
        View adView = AdUtil.getNativeAdView(TAG_HUA, R.layout.native_ad_2);
        if (adView != null) {
            ViewGroup.LayoutParams layout_ad = view_ad.getLayoutParams();
            if (adView.getHeight() == Util.dp2px(250)) {
                layout_ad.height = Util.dp2px(250);
            }
            // 设置LinearLayout控件的高度
            view_ad.setLayoutParams(layout_ad);
            view_ad.addView(adView);
            view_ad.setGravity(Gravity.CENTER);
            arrayList.add(viewpager_2);
        }
        //main_deep界面的深度清理(没有用)
        viewpager_3 = LayoutInflater.from(this).inflate(R.layout.main_deep, null);
        Button deep_ok = (Button) viewpager_3.findViewById(R.id.deep_ok);
        deep = (LinearLayout) viewpager_3.findViewById(R.id.deep);
        LinearLayout tap_ll = (LinearLayout) viewpager_3.findViewById(R.id.tap_ll);
        ImageView tap_iv_1 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_1);
        ImageView tap_iv_2 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_2);
        ImageView tap_iv_3 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_3);
        ImageView tap_iv_4 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_4);
        ImageView tap_iv_5 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_5);
        TextView tap_iv_6 = (TextView) viewpager_3.findViewById(R.id.tap_iv_6);
        TextView tap_deep_text = (TextView) viewpager_3.findViewById(R.id.tap_deep_text);
        lot_tap = (FrameLayout) viewpager_3.findViewById(R.id.lot_tap);
        if (!PreData.getDB(this, Constant.DEEP_CLEAN, false)) {
            List<JunkInfo> startList = new ArrayList<>();
            for (JunkInfo info : CleanManager.getInstance(this).getAppRamList()) {
                if (info.isSelfBoot) {
                    startList.add(info);
                }
            }
            deep_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdUtil.track("主页面", "点击Tap进入深度清理", "", 1);
                    mainPresenter.jumpToActivity(PowerActivity.class, 1);
                }
            });

            viewpager = (ViewPager) findViewById(R.id.viewpager);
            pageindicatorview = (PageIndicatorView) findViewById(R.id.pageindicatorview);

            viewpager.setAdapter(pagerAdapter = new PagerAdapter() {
                @Override
                public int getCount() {
                    return arrayList.size();
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    container.addView(arrayList.get(position), 0);
                    return arrayList.get(position);
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    View view = (View) object;
                    container.removeView(view);
                    view = null;
                }

                @Override
                public int getItemPosition(Object object) {
                    return POSITION_NONE;
                }


                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return arg0 == arg1;
                }
            });

//        if (adView == null && Util.isAccessibilitySettingsOn(this)) {
//            pageView.setVisibility(View.GONE);
//        } else {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewpager.setCurrentItem(2);
                }
            }, 3000);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //setCurrentItem(1)方法设置viewpager其初始显示的页面
                    viewpager.setCurrentItem(1);
                }
            }, 4000);
//        }
            mainPresenter = new MainPresenter(this, this);
            mainPresenter.init();
            mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);

            AdUtil.track("主页面", "进入主页面", "", 1);
            lot_family.setImageAssetsFolder("images/box/");
            lot_family.setAnimation("box.json");
            lot_family.loop(true);
            lot_family.playAnimation();
        }

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
        main_air_all.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_applock_button.setOnClickListener(onClickListener);
        main_theme_button.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_msg_button.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        fl_lot_side.setOnClickListener(onClickListener);
        lot_family.setOnClickListener(onClickListener);

        main_scroll_view.setScrollViewListener(new MyScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y == 0 && oldy != 0) {
                    viewpager.setCurrentItem(0);
                }
            }
        });

    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
        main_custom_cpu.startProgress(false, temp);
        main_custom_cpu.setCustomRoundListener(new CustomRoundCpu.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置CPU温度
                        main_cpu_temp.setText(String.valueOf(progress) + "℃");
                        // 设置硬件信息里的CPU温度
                        main_msg_cpu_percent.setText(String.valueOf(progress) + "℃");
                        if (main_cooling_h.getVisibility() == View.INVISIBLE) {
                            main_cooling_h.setText(String.valueOf(temp) + "℃");
                            main_cooling_h.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

    }

    int sdProgress;

    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        main_custom_sd.startProgress(true, percent);
        final Runnable sdRunable = new Runnable() {
            @Override
            public void run() {
                // 设置存储空间占用率
                main_sd_per.setText(String.valueOf(sdProgress) + "%");
            }
        };
        main_custom_sd.setCustomRoundListener(new CustomRoundCpu.CustomRoundListener() {
            @Override
            public void progressUpdate(int progress) {
                MainActivity.this.sdProgress = progress;
                handler.post(sdRunable);
            }

        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 设置存储空间信息
                main_sd_size.setText(size);
                //设置硬件信息里的剩余空间
                main_msg_sd_percent.setText(Util.convertStorage(sd_kongxian, false));
                //根据剩余的空间大小来设置单位
                if (sd_kongxian < 1024) {
                    main_msg_sd_unit.setText("B");
                } else if (sd_kongxian < 1048576) {
                    main_msg_sd_unit.setText("KB");
                } else if (sd_kongxian < 1073741824) {
                    main_msg_sd_unit.setText("MB");
                } else {
                    main_msg_sd_unit.setText("GB");
                }
                if (main_junk_h.getVisibility() == View.INVISIBLE) {
                    long junk_size = CleanManager.getInstance(MainActivity.this).getApkSize() + CleanManager.getInstance(MainActivity.this).getCacheSize() +
                            CleanManager.getInstance(MainActivity.this).getUnloadSize() + CleanManager.getInstance(MainActivity.this).getLogSize() + CleanManager.getInstance(MainActivity.this).getDataSize();
                    if (junk_size > 0) {
                        main_junk_h.setText(Util.convertStorage(junk_size, true));
                        main_junk_h.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void initRam(final int percent, final String size) {
        main_custom_ram.startProgress(false, percent);
        main_custom_ram.setCustomRoundListener(new CustomRoundCpu.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置内存占用率
                        main_ram_per.setText(String.valueOf(progress) + "%");
                        // 设置运行内存信息
                        main_ram_size.setText(size);
                        // 设置硬件信息里的已用内存
                        main_msg_ram_percent.setText(String.valueOf(progress) + "%");
                        if (main_ram_h.getVisibility() == View.INVISIBLE) {
                            long ram_size = CleanManager.getInstance(MainActivity.this).getRamSize();
                            if (ram_size > 0) {
                                main_ram_h.setText(Util.convertStorage(ram_size, true));
                                main_ram_h.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void initGuard(int num, RotateAnimation rotateAnimation) {
        if (num != -1) {
            main_gurad_num.setText(String.valueOf(num) + " ");//valueOf是将num转换为字符串
        }
        main_guard_rotate.startAnimation(rotateAnimation);
    }

    // 菜单
    @Override
    public void initSideData() {
        if (adapter == null) {
            adapter = new SideAdapter(this);
            side_listView.setAdapter(adapter);
        }
        adapter.clear();
        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, true)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, Constant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评

    }

    @Override
    public void loadAirAnimator(TranslateAnimation translate) {
        //小火箭动画
        main_air_all.startAnimation(translate);
    }

    @Override
    public void setRotateGone() {
        //评价过了，设置隐藏
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
        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.showFullAd("eos_start_full");
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
                    }
                });
                int skip = PreData.getDB(this, Constant.SKIP_TIME, 6);
                handler.postDelayed(fullAdRunnale, skip * 1000);
            }
        }
    }

    Runnable fullAdRunnale = new Runnable() {
        @Override
        public void run() {
            adDelete();
        }
    };

    //上拉刷新监听
    PullToRefreshLayout.OnRefreshListener refreshListener = new PullToRefreshLayout.OnRefreshListener() {
        // 下拉刷新操作
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        }

        //上拉加载操作
        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            AdUtil.track("主页面", "刷新成功", "", 1);
            AndroidSdk.loadNativeAd(TAG_FULL_PULL, R.layout.native_ad_full, new ClientNativeAd.NativeAdLoadListener() {
                @Override
                public void onNativeAdLoadSuccess(View view) {
                    main_pull_refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    main_scroll_view.setAdSuccess(true);
                    if (ad_native_2 != null) {
                        ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
                        layout_ad.height = main_scroll_view.getMeasuredHeight();
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
                    // 菜单按钮
                    mainPresenter.openDrawer();
                    AdUtil.track("主页面", "点击进入侧边栏按钮", "", 1);
                    break;
                case R.id.iv_title_right:
                    //设置按钮
                    AdUtil.track("主页面", "点击进入设置页面", "", 1);
                    mainPresenter.jumpToActivity(SettingActivity.class, 1);
                    break;
                case R.id.main_cpu_air_button:
                    // 点击CPU小球，跳转到CPU降温界面
                    Bundle bundle = new Bundle();
                    bundle.putString("from", "main");
                    bundle.putInt("wendu", temp);
                    AdUtil.track("主页面", "点击cpu球进入降温页面", "", 1);
                    mainPresenter.jumpToActivity(CoolingActivity.class, bundle, 1);
                    break;
                case R.id.main_sd_air_button:
                    // 点击存储空间小球，跳转到垃圾清理界面
                    AdUtil.track("主页面", "点击sd球进入垃圾清理页面", "", 1);
                    mainPresenter.jumpToActivity(JunkActivity.class, 1);
                    break;
                case R.id.main_ram_air_button:
                    // 点击运行内存小球，跳转到内存清理界面
                    AdUtil.track("主页面", "点击ram球进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_air_all:
                    //点击小火箭，跳转到清理列表
                    AdUtil.track("主页面", "点击火箭进入清理所有界面", "", 1);
                    mainPresenter.jumpToActivity(JunkAndRamActivity.class, 1);
                    break;
                //好评上面的六个按钮点击事件
                case R.id.main_junk_button:
                    // 垃圾清理事件
                    AdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(JunkActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    //ram清理按钮
                    AdUtil.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    //  应用管理按钮
                    AdUtil.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(ManagerActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    //降温
                    AdUtil.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from", "main");
                    bundle1.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(CoolingActivity.class, bundle1, 1);
                    break;
                case R.id.main_applock_button:
                    //应用锁
                    break;
                case R.id.main_theme_button:
                    //游戏加速
                    AdUtil.track("主页面", "点击进入buton游戏加速", "", 1);
                    mainPresenter.jumpToActivity(GBoostActivity.class, 1);
                    break;

                case R.id.lot_family:
                    //广告礼盒点击事件
                    AdUtil.track("主页面", "点击广告礼包", "", 1);
//                    ShopMaster.launch(MainActivity.this, "EOS_Family",
//                            new Theme(R.raw.battery_0, getPackageName())
//                    );
                    // 位移动画
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
                case R.id.main_msg_button:
                    //硬件信息
                    AdUtil.track("主页面", "点击进入硬件信息", "", 1);
                    mainPresenter.jumpToActivity(MessageActivity.class, 1);
                    break;
                //下方五个清理事件
                case R.id.main_power_button:
                    // 深度清理
                    AdUtil.track("主页面", "点击进入深度清理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.DEEP_CLEAN, true);
                    mainPresenter.jumpToActivity(PowerActivity.class, 1);
                    break;
                case R.id.main_file_button:
                    //文件管理
                    AdUtil.track("主页面", "点击进入文件管理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.FILE_CLEAN, true);
                    mainPresenter.jumpToActivity(FileActivity.class, 1);
                    break;
                case R.id.main_gboost_button:
                    //游戏加速
                    AdUtil.track("主页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(MainActivity.this, Constant.GBOOST_CLEAN, true);
                    mainPresenter.jumpToActivity(GBoostActivity.class, 1);
                    break;
                case R.id.main_picture_button:
                    //相似图片
                    AdUtil.track("主页面", "点击进入相似图片", "", 1);
                    PreData.putDB(MainActivity.this, Constant.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(PictureActivity.class, 1);
                    break;
                case R.id.main_notifi_button:
                    //通知栏清理
                    AdUtil.track("主页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(MainActivity.this)) {
                        startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 100);
                    } else if (!PreData.getDB(MainActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(MainActivity.this, NotifiInfoActivity.class);
                        startActivityForResult(intent6, 1);
                    } else {
                        Intent intent6 = new Intent(MainActivity.this, NotifiActivity.class);
                        startActivityForResult(intent6, 1);
                    }
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

            main_cooling_h.setVisibility(View.GONE);
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
            main_ram_h.setVisibility(View.GONE);
        } else if (resultCode == Constant.JUNK_RESUIL) {
            main_junk_h.setVisibility(View.GONE);
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
        if (lot_family != null) {
            lot_family.pauseAnimation();
        }
        if (lot_main != null) {
            lot_main.pauseAnimation();
        }
        Animation animation = main_guard_rotate.getAnimation();
        if (animation == null) {
            return;
        }
        animation.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        Log.e("ad_mob_l", "h=" + ll_ad.getHeight() + "w=" + ll_ad.getWidth());
        if (lot_family != null) {
            lot_family.playAnimation();
        }
        if (lot_main != null) {
            lot_main.playAnimation();
        }
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Util.dp2px(115), Util.dp2px(130));
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
        main_guard_rotate.startAnimation(rotateAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lot_family != null) {
            lot_family.clearAnimation();
        }
        if (lot_main != null) {
            lot_main.clearAnimation();
        }
        if (lot_side != null) {
            lot_side.clearAnimation();
        }
    }

    public void onBackPressed() {
        if (ll_ad_full.getVisibility() == View.VISIBLE) {
            adDelete();
            handler.removeCallbacks(fullAdRunnale);
            return;
        }
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {
            if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
                AndroidSdk.showFullAd("eos_exit_full");
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

        CRAnimation crA = new CircularRevealCompat(ll_ad_full).circularReveal(lot_family.getLeft() + lot_family.getWidth() / 2,
                lot_family.getTop() + lot_family.getHeight() / 2, ll_ad_full.getHeight(), 0);
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

    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_exit, null);
        LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 0) {
            View nativeExit = AdUtil.getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_full_exit);
            if (nativeExit != null) {
                ll_ad_exit.addView(nativeExit);
                // ll_ad_exit可见
                ll_ad_exit.setVisibility(View.VISIBLE);
            }
        }
        // 点击确定退出事件
        exit_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        // 点击取消退出事件
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
        mDrawerOpened = true;
        Log.e(TAG, "onDrawerOpened");
        if (lot_side != null) {
            lot_side.playAnimation();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerOpened = false;
        Log.e(TAG, "onDrawerClosed");
        if (lot_side != null) {
            lot_side.pauseAnimation();
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
