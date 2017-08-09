package com.mutter.clean.junk.myActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.mutter.clean.core.CleanManager;
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

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements MainView, DrawerLayout.DrawerListener {

    ImageView iv_title_right;
    ImageView iv_title_left;
    RelativeLayout main_sd_air_button, main_ram_air_button;
    public static final String TAG = "MainActivity";
    MyScrollView main_scroll_view;
    PullToRefreshLayout main_pull_refresh;
    RoundSd main_custom_sd;
    RelativeLayout main_junk_button, main_ram_button, main_cooling_button;
    RoundRam main_custom_ram;
    TextView main_sd_per, main_sd_size, main_ram_per, main_ram_size;
    LinearLayout main_rotate_all;
    TextView main_rotate_good, main_rotate_bad;
    ImageView main_rotate_cha;
    LinearLayout main_manager_button;
    TextView main_junk_h, main_ram_h, main_cooling_h;
    LinearLayout main_power_button;
    LinearLayout main_gboost_button;
    LinearLayout main_picture_button;
    ListView side_listView;
    LinearLayout main_notifi_button;
    LinearLayout main_file_button;
    DrawerLayout main_drawer;
    LinearLayout ll_ad_side, ad_native_2;
    com.mingle.widget.LinearLayout ll_ad_full;
    ProgressBar ad_progressbar;
    TextView main_full_time;
    ImageView main_circle;
    LinearLayout tuiguang_main;

    // LottieAnimationView lot_side;
    ImageView side_title;
    ImageView lot_family;
    FrameLayout ad_delete;
    FrameLayout main_ad;


    private String TAG_MAIN = "mutter_main";
    private String TAG_HUA = "mutter_hua";
    private String TAG_SIDE = "mutter_side";
    private String TAG_START_FULL = "mutter_start_native";
    private String TAG_EXIT_FULL = "mutter_exit_native";
    private String TAG_FULL_PULL = "pull_full";

    private MyApplication cleanApplication;
    private Handler handler = new Handler();
    private MainPresenter mainPresenter;
    private SideAdapter adapter;
    private long mExitTime;
    private int temp;
    private ViewPager viewpager;
    PagerAdapter pagerAdapter;
    /* private PagerAdapter pagerAdapter;
     private View pageView;*/

    private boolean mDrawerOpened = false;
    private String from;
    private AlertDialog dialog;
    private LinearLayout deep;
    private FrameLayout lot_tap;
    private ArrayList<View> arrayList;
    private AlertDialog dialogB;

    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        main_scroll_view = (MyScrollView) findViewById(R.id.main_scroll_view);
        main_pull_refresh = (PullToRefreshLayout) findViewById(R.id.main_pull_refresh);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);

        main_circle = (ImageView) findViewById(R.id.main_circle);
        main_sd_air_button = (RelativeLayout) findViewById(R.id.main_sd_air_button);
        main_custom_sd = (RoundSd) findViewById(R.id.main_custom_sd);
        main_sd_per = (TextView) findViewById(R.id.main_sd_per);
        main_sd_size = (TextView) findViewById(R.id.main_sd_size);
        main_ram_air_button = (RelativeLayout) findViewById(R.id.main_ram_air_button);
        main_custom_ram = (RoundRam) findViewById(R.id.main_custom_ram);
        main_ram_per = (TextView) findViewById(R.id.main_ram_per);
        main_ram_size = (TextView) findViewById(R.id.main_ram_size);

        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_rotate_cha = (ImageView) findViewById(R.id.main_rotate_cha);
        main_power_button = (LinearLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (LinearLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (LinearLayout) findViewById(R.id.main_file_button);
        main_gboost_button = (LinearLayout) findViewById(R.id.main_gboost_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        side_listView = (ListView) findViewById(R.id.side_listView);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);

        side_title = (ImageView) findViewById(R.id.side_title);
        lot_family = (ImageView) findViewById(R.id.lot_family);
        ad_delete = (FrameLayout) findViewById(R.id.ad_delete);
        main_ad = (FrameLayout) findViewById(R.id.main_ad);
        tuiguang_main = (LinearLayout) findViewById(R.id.tuiguang_main);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        cleanApplication = (MyApplication) getApplication();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) == 0) {
            main_notifi_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.DEEP_KAIGUAN, 1) == 0) {
            main_power_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.FILE_KAIGUAN, 1) == 0) {
            main_file_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.GBOOST_KAIGUAN, 1) == 0) {
            main_gboost_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.PICTURE_KAIGUAN, 1) == 0) {
            main_picture_button.setVisibility(View.GONE);
        }

        arrayList = new ArrayList<>();
        View view = LayoutInflater.from(this).inflate(R.layout.main_kuai_4, null);
        main_junk_button = (RelativeLayout) view.findViewById(R.id.main_junk_button);
        main_ram_button = (RelativeLayout) view.findViewById(R.id.main_ram_button);
        main_manager_button = (LinearLayout) view.findViewById(R.id.main_manager_button);
        main_cooling_button = (RelativeLayout) view.findViewById(R.id.main_cooling_button);
        main_junk_h = (TextView) view.findViewById(R.id.main_junk_h);
        main_ram_h = (TextView) view.findViewById(R.id.main_ram_h);
        main_cooling_h = (TextView) view.findViewById(R.id.main_cooling_h);
        arrayList.add(view);

        View viewpager_2 = LayoutInflater.from(this).inflate(R.layout.main_ad, null);
        LinearLayout view_ad = (LinearLayout) viewpager_2.findViewById(R.id.view_ad);
        View adView = AdUtil.getNativeAdView(TAG_HUA, R.layout.native_ad_6);
        if (adView != null) {
            view_ad.addView(adView);
            view_ad.setGravity(Gravity.CENTER);
            arrayList.add(viewpager_2);
        }
        viewpager = (ViewPager) findViewById(R.id.viewpager);

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
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewpager.setCurrentItem(1);
            }
        }, 4000);
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);

        AdUtil.track("主页面", "进入主页面", "", 1);
        RotateAnimation animation = new RotateAnimation(0, 360, 2, 0.5f, 2, 0.5f);
        animation.setDuration(1000);//设置动画持续时间
        animation.setRepeatCount(-1);//设置重复次数
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        lot_family.startAnimation(animation);
        tuiguang(TUIGUAN_MAIN_SOFT, true, tuiguang_main);
        tuiguang(TUIGUAN_MAIN, false, tuiguang_main);
        tuiguang(TUIGUAN_SIDE_SOFT, true, side_listView);
        tuiguang(TUIGUAN_SIDE, false, side_listView);

        initSideData();
    }


    int sdProgress;

    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        main_custom_sd.startProgress(true, percent);
        final Runnable sdRunable = new Runnable() {
            @Override
            public void run() {
                main_sd_per.setText(String.valueOf(sdProgress) + "");
            }
        };
        main_custom_sd.setCustomRoundListener(new RoundSd.CustomRoundListener() {
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
                if (main_junk_h.getVisibility() == View.INVISIBLE) {
                    long junk_size = CleanManager.getInstance(MainActivity.this).getApkSize() + CleanManager.getInstance(MainActivity.this).getCacheSize() +
                            CleanManager.getInstance(MainActivity.this).getUnloadSize() + CleanManager.getInstance(MainActivity.this).getLogSize() + CleanManager.getInstance(MainActivity.this).getDataSize();
                    if (junk_size > 0) {
                        main_junk_h.setText(Util.convertStorage(junk_size, true));
//                        main_junk_h.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    @Override
    public void initRam(final int percent, final String size) {
        main_custom_ram.startProgress(false, percent);
        main_custom_ram.setCustomRoundListener(new RoundRam.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main_ram_per.setText(String.valueOf(progress) + "");
                        main_ram_size.setText(size);
                        if (main_ram_h.getVisibility() == View.INVISIBLE) {
                            long ram_size = CleanManager.getInstance(MainActivity.this).getRamSize();
                            if (ram_size > 0) {
                                main_ram_h.setText(Util.convertStorage(ram_size, true));
//                                main_ram_h.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        });

    }

    @Override
    public void initGuard(int num, RotateAnimation rotateAnimation) {
    }

    //初始化监听
    public void onClick() {
        //main_scroll_view.setOnTouchListener(scrollViewTouchListener);
        main_pull_refresh.setOnRefreshListener(refreshListener);
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
        if (main_cooling_h.getVisibility() == View.INVISIBLE) {
            main_cooling_h.setText(String.valueOf(temp) + "℃");
//                            main_cooling_h.setVisibility(View.VISIBLE);

        }
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
        if (PreData.getDB(this, Constant.DEEP_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        }
        if (PreData.getDB(this, Constant.PICTURE_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        }
        if (PreData.getDB(this, Constant.FILE_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        }
        adapter.addData(new SideInfo(R.string.main_cooling_name, R.mipmap.side_battery));//电池降温
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        if (PreData.getDB(this, Constant.GBOOST_KAIGUAN, 1) != 0) {
            adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
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
            return;
        }

        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.showFullAd("mutter_start_full");
            Log.e("ad", "full===");
        } else {
            Log.e("ad", "native===");
            View nativeView_full = AdUtil.getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_main);
            if (ll_ad_full != null && nativeView_full != null) {
                ll_ad_full.addView(nativeView_full);
                ll_ad_full.setVisibility(View.VISIBLE);
                LinearLayout loading_text = (LinearLayout) nativeView_full.findViewById(R.id.loading_text);
                loading_text.setOnClickListener(null);
                ImageView ad_delete = (ImageView) nativeView_full.findViewById(R.id.ad_delete);
                ad_delete.setOnClickListener(new View.OnClickListener() {
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
                        layout_ad.height = main_scroll_view.getMeasuredHeight() - getResources().getDimensionPixelSize(R.dimen.d10);
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
                case R.id.main_circle:
                    AdUtil.track("主页面", "点击进入清理所有页面", "", 1);
                    mainPresenter.jumpToActivity(CleanAndRamActivity.class, 1);
                    break;

                case R.id.main_rotate_good:
                    AdUtil.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(true);
                    break;
                case R.id.main_rotate_bad:
                    AdUtil.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(false);
                    break;
                case R.id.main_rotate_cha:
                    AdUtil.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(false);
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
        handler.removeCallbacks(runnable1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        handler.postDelayed(runnable1, 500);
    }

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            toggleEditAnimation(R.id.main_ad, R.id.main_circle);
            handler.postDelayed(this, 4000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lot_family != null) {
            lot_family.clearAnimation();
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
                AndroidSdk.showFullAd("mutter_exit_full");
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

    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_exit, null);
        LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 0) {
            View nativeExit = AdUtil.getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_6);
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
        dialog = new AlertDialog.Builder(this).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerOpened = true;
        Log.e(TAG, "onDrawerOpened");
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerOpened = false;
        Log.e(TAG, "onDrawerClosed");
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}