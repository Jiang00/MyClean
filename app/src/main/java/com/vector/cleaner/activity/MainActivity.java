package com.vector.cleaner.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.vector.cleaner.madapter.MSideAdapter;
import com.vector.cleaner.entity.SideInfo;
import com.vector.cleaner.myview.LineRam;
import com.vector.cleaner.myview.MyScrollView;
import com.vector.cleaner.myview.PullToRefreshLayout;
import com.vector.cleaner.presenter.MainPresenter;
import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.cleaner.view.MainView;
import com.vector.cleaner.R;
import com.vector.cleaner.myview.ListViewForScrollView;
import com.vector.mcleaner.mutil.PreData;
import com.vector.mcleaner.mutil.Util;
import com.vector.module.Util.Constants;
import com.vector.module.Util.Utils;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;

public class MainActivity extends BaseActivity implements MainView, DrawerLayout.DrawerListener {

    public static final String TAG = "MainActivity";
    TextView main_rotate_good, main_rotate_bad;
    MyScrollView main_scroll_view;
    TextView main_sd_per, main_sd_size, main_ram_per, main_ram_size;
    RelativeLayout main_junk_button, main_ram_button, main_cooling_button;
    LinearLayout main_manager_button, main_applock_button, main_theme_button;
    LinearLayout main_rotate_all;
    ImageView main_rotate_cha;
    PullToRefreshLayout main_pull_refresh;
    ImageView iv_title_right;
    ImageView iv_title_left;
    LinearLayout main_sd_air_button, main_ram_air_button;
    ImageView main_huojian, main_yun;
    LineRam main_custom_sd;
    LineRam main_custom_ram;
    LinearLayout main_power_button;
    LinearLayout main_file_button;
    LinearLayout main_picture_button;
    com.mingle.widget.LinearLayout ll_ad_full;
    ProgressBar ad_progressbar;
    LinearLayout main_battery;
    LinearLayout main_notifi_button;

    FrameLayout lot_ad;
    ImageView lot_ad_2;
    FrameLayout full_load;
    TextView full_text;
    ListViewForScrollView side_listView;
    DrawerLayout main_drawer;
    LinearLayout ll_ad, ll_ad_side, ad_native_2, ll_ad_s;

    private String TAG_START_FULL = "vector_start_native";
    private String TAG_EXIT_FULL = "vector_exit_native";
    private String TAG_FULL_PULL = "clean_native";
    private String TAG_MAIN = "vector_main";
    private String TAG_SIDE = "vector_side";
    private String TAG_REFRESH = "drag";
    private String LOADING_FULL = "loading_full";

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


        main_sd_air_button = (LinearLayout) findViewById(R.id.main_sd_air_button);
        main_custom_sd = (LineRam) findViewById(R.id.main_custom_sd);
        main_sd_per = (TextView) findViewById(R.id.main_sd_per);
        main_sd_size = (TextView) findViewById(R.id.main_sd_size);
        main_ram_air_button = (LinearLayout) findViewById(R.id.main_ram_air_button);
        main_huojian = (ImageView) findViewById(R.id.main_huojian);
        main_yun = (ImageView) findViewById(R.id.main_yun);
        main_custom_ram = (LineRam) findViewById(R.id.main_custom_ram);
        main_ram_per = (TextView) findViewById(R.id.main_ram_per);
        main_ram_size = (TextView) findViewById(R.id.main_ram_size);


        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_manager_button = (LinearLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_applock_button = (LinearLayout) findViewById(R.id.main_applock_button);
        main_theme_button = (LinearLayout) findViewById(R.id.main_theme_button);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_cha = (ImageView) findViewById(R.id.main_rotate_cha);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_power_button = (LinearLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (LinearLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (LinearLayout) findViewById(R.id.main_file_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        side_listView = (ListViewForScrollView) findViewById(R.id.side_listView);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_s = (LinearLayout) findViewById(R.id.ll_ad_s);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);

        lot_ad = (FrameLayout) findViewById(R.id.lot_ad);
        lot_ad_2 = (ImageView) findViewById(R.id.lot_ad_2);
        full_load = (FrameLayout) findViewById(R.id.full_load);
        full_text = (TextView) findViewById(R.id.full_text);
        main_battery = (LinearLayout) findViewById(R.id.main_battery);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) != 1) {
            main_notifi_button.setVisibility(View.GONE);
        }

        if (PreData.getDB(this, Constant.FILE_KAIGUAN, 1) != 1) {
            main_file_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.PHOTO_KAIGUAN, 1) != 1) {
            main_picture_button.setVisibility(View.GONE);
        }

        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);

        AdUtil.track("主页面", "进入主页面", "", 1);
        main_applock_button.setVisibility(View.VISIBLE);
        handler.post(run);
    }

    boolean light;
    Runnable run = new Runnable() {
        @Override
        public void run() {
            light = !light;
            if (light) {
                lot_ad_2.setVisibility(View.INVISIBLE);
            } else {
                lot_ad_2.setVisibility(View.VISIBLE);
            }
            handler.postDelayed(this, 1000);
        }
    };


    //初始化监听
    public void onClick() {
        //main_scroll_view.setOnTouchListener(scrollViewTouchListener);
        main_pull_refresh.setOnRefreshListener(refreshListener);
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_sd_air_button.setOnClickListener(onClickListener);
        main_ram_air_button.setOnClickListener(onClickListener);
        main_huojian.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_applock_button.setOnClickListener(onClickListener);
        main_theme_button.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_rotate_cha.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        lot_ad.setOnClickListener(onClickListener);


    }


    int sdProgress;


    @Override
    public void initSideData() {
        if (adapter == null) {
            adapter = new MSideAdapter(this);
            side_listView.setAdapter(adapter);
        }
        adapter.clear();

        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, true)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, Constant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        if (PreData.getDB(this, Constant.FILE_KAIGUAN, 1) == 1) {
            adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        }
        adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        if (PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) == 1 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        }
        if (PreData.getDB(this, Constant.PHOTO_KAIGUAN, 1) == 1) {
            adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        }
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评

    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        main_custom_sd.startProgress(percent);
        final Runnable sdRunable = new Runnable() {
            @Override
            public void run() {
                main_sd_per.setText(String.valueOf(sdProgress) + "");
            }
        };
        main_custom_sd.setCustomRoundListener(new LineRam.CustomRoundListener() {
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
            }
        });

    }

    @Override
    public void initHuojian() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_main);
                main_huojian.startAnimation(animation);
                main_huojian.setVisibility(View.VISIBLE);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation animationA = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_main);
                        main_yun.startAnimation(animationA);
                        main_yun.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }
                });

            }
        });
    }

    @Override
    public void initRam(final int percent, final String size) {
        main_custom_ram.startProgress(percent);
        main_custom_ram.setCustomRoundListener(new LineRam.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main_ram_per.setText(String.valueOf(progress) + "");
                        main_ram_size.setText(size);
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

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
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

    @Override
    public void loadFullAd() {
        if (PreData.getDB(this, Constant.FULL_MAIN, 0) == 1) {
        } else {
            View nativeView = AdUtil.getNativeAdView(TAG_MAIN, R.layout.native_ad_2);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
                ll_ad.setGravity(Gravity.CENTER_HORIZONTAL);
                main_scroll_view.setScrollY(0);
//                main_scroll_view.fullScroll(ScrollView.FOCUS_UP);
                ll_ad.setVisibility(View.VISIBLE);
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
            AndroidSdk.showFullAd(LOADING_FULL);

        } else {
            View nativeView_full = getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_main);
            if (ll_ad_full != null && nativeView_full != null) {
                ll_ad_full.addView(nativeView_full);
                ll_ad_full.setVisibility(View.VISIBLE);
                ImageView ad_delete = (ImageView) nativeView_full.findViewById(R.id.ad_delete);
                LinearLayout loading_text = (LinearLayout) nativeView_full.findViewById(R.id.loading_text);
                loading_text.setOnClickListener(null);
                ad_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.removeCallbacks(fullAdRunnale);
                        adDelete();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                main_pull_refresh.autoRefresh();
//                            }
//                        }, 2000);
                    }
                });
                int skip = PreData.getDB(this, Constant.SKIP_TIME, 6);
                handler.postDelayed(fullAdRunnale, skip * 1000);
            } else {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        main_pull_refresh.autoRefresh();
//                    }
//                }, 2000);
            }
        }
    }

    Runnable fullAdRunnale = new Runnable() {
        @Override
        public void run() {
            adDelete();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    main_pull_refresh.autoRefresh();
//                }
//            }, 2000);
        }
    };

    //上拉刷新监听
    PullToRefreshLayout.OnRefreshListener refreshListener = new PullToRefreshLayout.OnRefreshListener() {
        // 下拉刷新操作
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//            AndroidSdk.loadNativeAd(TAG_REFRESH, R.layout.native_ad_6, new ClientNativeAd.NativeAdLoadListener() {
//                @Override
//                public void onNativeAdLoadSuccess(View view) {
//                    main_pull_refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
//                    main_scroll_view.setSadSuccess(true);
//                    if (ll_ad_s != null) {
//                        final ImageView ad_cha = (ImageView) view.findViewById(R.id.ad_cha);
//                        ll_ad_s.addView(view);
//                        ll_ad_s.setVisibility(View.VISIBLE);
//                        main_scroll_view.isTouch = false;
//                        main_scroll_view.smoothScrollToSlowS(1000);
//                        ad_cha.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ad_cha.setOnClickListener(null);
//                                main_scroll_view.smoothScrollToSlowS();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        main_scroll_view.setSadSuccess(false);
//                                        ll_ad_s.removeAllViews();
//                                        ll_ad_s.setVisibility(View.GONE);
//                                        main_scroll_view.smoothScrollToSlowS(1);
//                                    }
//                                }, 500);
//
//                            }
//                        });
//
//                    }
//                }
//
//                @Override
//                public void onNativeAdLoadFails() {
//                    main_pull_refresh.refreshFinish(PullToRefreshLayout.FAIL);
//                }
//            });
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

    private AnimatorSet animatorSet;
    private int time;
    private ObjectAnimator animator_y;
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
                    mainPresenter.jumpToActivity(LajiFileActivity.class, 1);
                    break;
                case R.id.main_ram_air_button:
                    AdUtil.track("主页面", "点击ram球进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(ANeicunAvtivity.class, 1);
                    break;
                case R.id.main_huojian:
                    AdUtil.track("主页面", "点击火箭进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(LajiAndRamActivity.class, 1);
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(LajiFileActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(ANeicunAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    AdUtil.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(AllAppActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    AdUtil.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from", "main");
                    bundle1.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(BatteryCoolingActivity.class, bundle1, 1);
                    break;
                case R.id.main_applock_button:
                    AdUtil.track("主页面", "点击深度清理按钮", "", 1);
                    mainPresenter.jumpToActivity(DeepActivity.class, 1);
                    break;
                case R.id.main_theme_button:
                    AdUtil.track("主页面", "点击进入buton游戏加速", "", 1);
                    mainPresenter.jumpToActivity(DyxGboostActivity.class, 1);
                    break;
                case R.id.lot_ad:
                    AdUtil.track("主页面", "点击广告礼包", "", 1);
                    if (PreData.getDB(MainActivity.this, Constant.FULL_START, 0) == 1) {
                        AndroidSdk.loadFullAd(LOADING_FULL, null);
                        full_load.setVisibility(View.VISIBLE);
                        animatorSet = new AnimatorSet();
                        time = 3;
                        full_text.setText(time + "");
                        full_text.setScaleX(1);
                        full_text.setScaleY(1);
                        final ObjectAnimator animator_x = ObjectAnimator.ofFloat(full_text, View.SCALE_X, 0, 1);
                        animator_x.setRepeatCount(2);
                        animator_y = ObjectAnimator.ofFloat(full_text, View.SCALE_Y, 0, 1);
                        animator_y.setRepeatCount(2);
                        animatorSet.play(animator_x).with(animator_y);
                        animatorSet.setDuration(1000);
                        animatorSet.start();
                        full_text.setVisibility(View.VISIBLE);
                        animator_y.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                AndroidSdk.showFullAd(LOADING_FULL);
                                full_load.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                                time--;
                                if (time > 0) {
                                    full_text.setText(time + "");
                                }
                            }

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }
                        });
                    } else {
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
                    }
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
                    AdUtil.track("主页面", "点击好评叉号", "", 1);
                    PreData.putDB(MainActivity.this, Constant.IS__MAIN_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_power_button:
                    AdUtil.track("主页面", "点击进入深度清理", "", 1);
                    mainPresenter.jumpToActivity(DeepActivity.class, 1);
                    break;
                case R.id.main_file_button:
                    AdUtil.track("主页面", "点击进入文件管理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.FILE_CLEAN, true);
                    mainPresenter.jumpToActivity(FilesActivity.class, 1);
                    break;
                case R.id.main_picture_button:
                    AdUtil.track("主页面", "点击进入相似图片", "", 1);
                    PreData.putDB(MainActivity.this, Constant.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(SimilarPhotoActivity.class, 1);
                    break;
                case R.id.main_notifi_button:
                    AdUtil.track("主页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(MainActivity.this) || !PreData.getDB(MainActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(MainActivity.this, NotifiYindaoActivity.class);
                        startActivityForResult(intent6, 1);
                    } else {
                        Intent intent6 = new Intent(MainActivity.this, NotificationActivity.class);
                        startActivityForResult(intent6, 1);
                    }
                    break;
                case R.id.battery_button:
                    main_battery.setVisibility(View.GONE);
                    Utils.writeData(MainActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                    initSideData();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            main_pull_refresh.autoRefresh();
//                        }
//                    }, 2000);
                    adapter.notifyDataSetChanged();
                    AdUtil.track("主界面", "充电屏保引导", "叉掉", 1);
                    break;
                case R.id.battery_cha:
                    main_battery.setVisibility(View.GONE);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            main_pull_refresh.autoRefresh();
//                        }
//                    }, 2000);
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
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(MainActivity.this, NotifiYindaoActivity.class);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        main_huojian.setVisibility(View.INVISIBLE);
        main_yun.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
            AndroidSdk.loadFullAd("vector_exit_full", null);
        }
        Log.e("ad_mob_l", "h=" + ll_ad.getHeight() + "w=" + ll_ad.getWidth());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(run);
        }
    }

    public void onBackPressed() {
        if (ll_ad_full.getVisibility() == View.VISIBLE) {
            adDelete();
            handler.removeCallbacks(fullAdRunnale);
            return;
        }
        if (full_load.getVisibility() == View.VISIBLE) {
            full_load.setVisibility(View.GONE);
            if (animator_y != null) {
                animator_y.removeAllListeners();
                animator_y.cancel();
            }
            if (animatorSet != null) {
                animatorSet.removeAllListeners();
                animatorSet.cancel();
            }

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
                AndroidSdk.showFullAd("vector_exit_full");
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
            View nativeExit = getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_full_exit);
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