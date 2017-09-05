package com.icleaner.junk.icleaneractivity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.icleaner.junk.mycustomview.ICleanerWaterView;
import com.icleaner.junk.mytools.MyConstant;
import com.icleaner.junk.mytools.SetAdUtil;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.icleaner.clean.core.CleanManager;
import com.icleaner.clean.entity.JunkInfo;
import com.icleaner.junk.R;
import com.icleaner.junk.mycustomview.CircleView;
import com.icleaner.junk.mycustomview.CircleWaveView;
import com.icleaner.junk.mycustomview.CustomRoundCpu;
import com.icleaner.junk.mycustomview.ListViewForScrollView;
import com.icleaner.junk.mycustomview.MainRoundView;
import com.icleaner.junk.mycustomview.MyScrollView;
import com.icleaner.junk.mycustomview.PullToRefreshLayout;
import com.icleaner.junk.mycustomview.VirtuaRingView;
import com.icleaner.junk.mypresenter.PresenterMain;
import com.icleaner.junk.mycustomadapter.MySidebarAdapter;
import com.icleaner.junk.shitis.SideInfo;
import com.icleaner.junk.interfaceview.MainView;
import com.icleaner.clean.utils.PreData;
import com.icleaner.clean.utils.MyUtils;
import com.icleaner.module.charge.saver.Utils.BatteryConstants;
import com.icleaner.module.charge.saver.Utils.Utils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements MainView, DrawerLayout.DrawerListener {

    public static final String TAG = "MainActivity";
    CustomRoundCpu main_custom_cpu, main_custom_ram, main_custom_sd;
    RelativeLayout main_junk_button, main_ram_button, main_cooling_button;
    LinearLayout main_manager_button;
    LinearLayout main_rotate_all;
    TextView main_junk_h;
    TextView main_full_time;
    LinearLayout main_battery;
    RelativeLayout main_all_cercle;
    RelativeLayout main_title;
    RelativeLayout main_wave;
    TextView main_rotate_good, main_rotate_bad;
    ImageView main_rotate_close;
    LinearLayout main_msg_button;
    MyScrollView main_scroll_view;
    PullToRefreshLayout main_pull_refresh;
    ImageView iv_title_right;
    CircleWaveView main_circlewaveview;
    ImageView iv_title_left;
    String junkSize, junkSize1;
    ImageView lot_ad;
    ListViewForScrollView side_listView;
    DrawerLayout main_drawer;
    LinearLayout ll_ad, ll_ad_side, ad_native_2, ll_ad_s;
    ICleanerWaterView main_water;
    private int temp;
    private String from;
    private AlertDialog dialog;
    ObjectAnimator animator, animator1, animator2;
    AnimatorSet animatorSet;
    private Handler handler = new Handler();
    private PresenterMain mainPresenter;
    private MySidebarAdapter adapter;
    private long mExitTime;
    LinearLayout main_power_button;
    TextView main_msg_ram_percent, main_msg_sd_percent, main_msg_cpu_percent;
    private ArrayList<JunkInfo> startList;
    TextView main_fenshu;
    VirtuaRingView virtuaRingView;
    ImageView main_pointer;
    CircleView main_circleview;
    LinearLayout main_gboost_button, main_picture_button;
    TextView power_size;
    ImageView main_aerobee;
    LinearLayout main_junk_button2, main_cooling_button2;
    com.mingle.widget.LinearLayout ll_ad_full;
    // icleaner
    private String TAG_START_FULL = "icleaner_start_native";
    private String TAG_EXIT_FULL = "icleaner_exit_native";
    private String TAG_FULL_PULL = "pull_full";
    private String TAG_MAIN = "icleaner_main";
    private String TAG_SIDE = "icleaner_side";
    private String TAG_REFRESH = "drag";
    ProgressBar ad_progressbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);

        mainPresenter = new PresenterMain(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);
        SetAdUtil.track("主页面", "进入主页面", "", 1);
    }

    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        main_scroll_view = (MyScrollView) findViewById(R.id.main_scroll_view);
        main_pull_refresh = (PullToRefreshLayout) findViewById(R.id.main_pull_refresh);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        main_water = (ICleanerWaterView) findViewById(R.id.main_water);
        power_size = (TextView) findViewById(R.id.power_size);
        main_junk_h = (TextView) findViewById(R.id.main_junk_h);
        main_fenshu = (TextView) findViewById(R.id.main_fenshu);
        virtuaRingView = (VirtuaRingView) findViewById(R.id.virtuaringview);
        main_all_cercle = (RelativeLayout) findViewById(R.id.main_all_cercle);
        main_title = (RelativeLayout) findViewById(R.id.main_title);
        main_circlewaveview = (CircleWaveView) findViewById(R.id.main_circlewaveview);
        main_pointer = (ImageView) findViewById(R.id.main_pointer);
        main_circleview = (CircleView) findViewById(R.id.main_circleview);
        main_wave = (RelativeLayout) findViewById(R.id.main_wave);
        main_msg_ram_percent = (TextView) findViewById(R.id.main_msg_ram_percent);
        main_msg_sd_percent = (TextView) findViewById(R.id.main_msg_sd_percent);
        main_msg_cpu_percent = (TextView) findViewById(R.id.main_msg_cpu_percent);
        main_custom_cpu = (CustomRoundCpu) findViewById(R.id.main_custom_cpu);
        main_custom_ram = (CustomRoundCpu) findViewById(R.id.main_custom_ram);
        main_custom_sd = (CustomRoundCpu) findViewById(R.id.main_custom_sd);
        main_aerobee = (ImageView) findViewById(R.id.main_aerobee);

        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_junk_button2 = (LinearLayout) findViewById(R.id.main_junk_button2);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_manager_button = (LinearLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_cooling_button2 = (LinearLayout) findViewById(R.id.main_cooling_button2);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_rotate_close = (ImageView) findViewById(R.id.main_rotate_close);
        main_msg_button = (LinearLayout) findViewById(R.id.main_msg_button);
        main_power_button = (LinearLayout) findViewById(R.id.main_power_button);
        main_gboost_button = (LinearLayout) findViewById(R.id.main_gboost_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        side_listView = (ListViewForScrollView) findViewById(R.id.side_listView);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_s = (LinearLayout) findViewById(R.id.ll_ad_s);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);

        lot_ad = (ImageView) findViewById(R.id.lot_ad);
        main_battery = (LinearLayout) findViewById(R.id.main_battery);
    }

    //初始化监听
    public void onClick() {
        main_pull_refresh.setOnRefreshListener(refreshListener);
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_junk_button2.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_cooling_button2.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_rotate_close.setOnClickListener(onClickListener);
        main_msg_button.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        lot_ad.setOnClickListener(onClickListener);
        main_all_cercle.setOnClickListener(onClickListener);
        main_aerobee.setOnClickListener(onClickListener);

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
                        // 设置硬件信息里的CPU温度
                        main_msg_cpu_percent.setText(String.valueOf(progress) + "℃");
                    }
                });
            }
        });
    }

    int sdProgress;


    private void setColorAnimation(View view, int startColor, int endColor) {
        ValueAnimator colorAnim = ObjectAnimator.ofInt(view, "backgroundColor", startColor, endColor);
        colorAnim.setDuration(2000);
        colorAnim.setRepeatCount(0);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
    }

    private void setColor(int percent, boolean isReStart) {
        int color = ContextCompat.getColor(this, R.color.A1);
        Drawable background = main_all_cercle.getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) background;
            color = colordDrawable.getColor();
        }
    }

    @Override
    public void initSideData() {
        if (adapter == null) {
            adapter = new MySidebarAdapter(this);
        }
        adapter.clear();

        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, BatteryConstants.CHARGE_SAVER_SWITCH, false)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, MyConstant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, MyConstant.FILEACTIVITY, 0) != 0) {
            adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, MyConstant.GOODGAME, 0) != 0) {
            adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(this, MyConstant.POWERACTIVITY, 0) != 0) {
            adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        }
        adapter.addData(new SideInfo(R.string.white_list_name, R.mipmap.side_white));//白名单
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评
        side_listView.setAdapter(adapter);
    }

    @Override
    public void initQiu(final int fenshu, boolean isReStart) {
        Log.e("fenshu", fenshu + "===");
        setColor(fenshu, isReStart);
        if (!isReStart) {
            main_water.setPratent(fenshu);
            main_water.setFloatWaterListener(new ICleanerWaterView.FloatWaterListener() {
                @Override
                public void success() {

                }

                @Override
                public void update(int jindu) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            main_fenshu.setText(String.valueOf(fenshu));
                            virtuaRingView.setNum(fenshu * 60 / 100);
                            animator = ObjectAnimator.ofFloat(main_pointer, "rotation", 0f, fenshu * 60 / 100 * 6);
                            animator1 = ObjectAnimator.ofFloat(main_circleview, "scaleX", 1f, 1.2f, 1f);
                            animator2 = ObjectAnimator.ofFloat(main_circleview, "scaleY", 1f, 1.2f, 1f);
                            animatorSet = new AnimatorSet();
                            animatorSet.setDuration(2000);
                            animatorSet.play(animator).with(animator1).with(animator2);
                            animatorSet.start();
                        }
                    });
                }
            });
        } else {
            main_water.upDate(fenshu);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    main_fenshu.setText(String.valueOf(fenshu));
                    virtuaRingView.setNum(fenshu * 60 / 100);
                    animator = ObjectAnimator.ofFloat(main_pointer, "rotation", 0f, fenshu * 60 / 100 * 6);
                    animator1 = ObjectAnimator.ofFloat(main_circleview, "scaleX", 1f, 1.2f, 1f);
                    animator2 = ObjectAnimator.ofFloat(main_circleview, "scaleY", 1f, 1.2f, 1f);
                    animatorSet = new AnimatorSet();
                    animatorSet.setDuration(2000);
                    animatorSet.play(animator).with(animator1).with(animator2);
                    animatorSet.start();
                }
            });
        }
    }

    @Override
    public void loadFullAd() {
        if (PreData.getDB(this, MyConstant.FULL_MAIN, 0) == 1) {
        } else {
            View nativeView = SetAdUtil.getNativeAdView(TAG_MAIN, R.layout.native_ad_2);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                Log.e("aaa", "=====" + layout_ad.height);
                if (nativeView.getHeight() == MyUtils.dp2px(250)) {
                    layout_ad.height = MyUtils.dp2px(250);
                }
                Log.e("ad_mob", "h=" + nativeView.getHeight() + "w=" + nativeView.getWidth());
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
            View nativeView_side = SetAdUtil.getNativeAdView(TAG_SIDE, R.layout.native_ad_2);
            if (ll_ad_side != null && nativeView_side != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_side.getLayoutParams();
                layout_ad.height = nativeView_side.getMeasuredHeight();
                ll_ad_side.setLayoutParams(layout_ad);
                ll_ad_side.addView(nativeView_side);
            }

        }
        if (PreData.getDB(this, MyConstant.FIRST_BATTERY, true)) {
            Utils.writeData(MainActivity.this, BatteryConstants.CHARGE_SAVER_SWITCH, false);
            main_battery.setVisibility(View.VISIBLE);
            LinearLayout battery_cha = (LinearLayout) findViewById(R.id.battery_cha);
            Button battery_button = (Button) findViewById(R.id.battery_button);
            battery_cha.setOnClickListener(onClickListener);
            battery_button.setOnClickListener(onClickListener);
            return;
        }

        if (PreData.getDB(this, MyConstant.FULL_START, 0) == 1) {
            AndroidSdk.showFullAd("icleaner_start_full");

        } else {
//            View nativeView_full = SetAdUtil.getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_main);
//            if (ll_ad_full != null && nativeView_full != null) {
//                ll_ad_full.addView(nativeView_full);
//                ll_ad_full.setVisibility(View.VISIBLE);
//                nativeView_full.findViewById(R.id.ad_delete).setVisibility(View.GONE);
//                main_full_time = (TextView) nativeView_full.findViewById(R.id.main_full_time);
//                LinearLayout loading_text = (LinearLayout) nativeView_full.findViewById(R.id.loading_text);
//                loading_text.setOnClickListener(null);
//                main_full_time.setVisibility(View.VISIBLE);
//                main_full_time.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        handler.removeCallbacks(fullAdRunnale);
//                        adDelete();
//                    }
//                });
//                int skip = PreData.getDB(this, MyConstant.SKIP_TIME, 6);
//                handler.postDelayed(fullAdRunnale, skip * 1000);
//            } else {
//            }
        }
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

    //上拉刷新监听
    PullToRefreshLayout.OnRefreshListener refreshListener = new PullToRefreshLayout.OnRefreshListener() {
        // 下拉刷新操作
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        }

        //上拉加载操作
        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            SetAdUtil.track("主页面", "刷新成功", "", 1);
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
                    SetAdUtil.track("主页面", "点击进入侧边栏按钮", "", 1);
                    break;
                case R.id.main_aerobee:
                    SetAdUtil.track("主页面", "点击小火箭", "", 1);
                    mainPresenter.jumpToActivity(RubbishAndRamActivity.class, 1);
                    break;
                case R.id.iv_title_right:
                    SetAdUtil.track("主页面", "点击进入设置页面", "", 1);
                    mainPresenter.jumpToActivity(SetActivity.class, 1);
                    break;
                case R.id.main_water:
                    SetAdUtil.track("主页面", "点击火箭进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(RubbishAndRamActivity.class, 1);
                    break;
                case R.id.main_junk_button:
                    SetAdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(RubbishActivity.class, 1);
                    break;
                case R.id.main_junk_button2:
                    SetAdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(RubbishActivity.class, 1);
                    break;
                case R.id.main_all_cercle:
                    SetAdUtil.track("主页面", "点击垃圾所有按钮", "", 1);
                    mainPresenter.jumpToActivity(RubbishAndRamActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    SetAdUtil.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(MemoryAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    SetAdUtil.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(AllAppsActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    SetAdUtil.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from", "main");
                    bundle1.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(BatteriesActivity.class, bundle1, 1);
                    break;
                case R.id.main_cooling_button2:
                    SetAdUtil.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("from", "main");
                    bundle2.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(BatteriesActivity.class, bundle2, 1);
                    break;
                case R.id.lot_ad:
                    SetAdUtil.track("主页面", "点击广告礼包", "", 1);
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
                    SetAdUtil.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(true);
                    break;
                case R.id.main_rotate_bad:
                    SetAdUtil.track("主页面", "点击好评bad按钮", "", 1);
                    PreData.putDB(MainActivity.this, MyConstant.IS_ROTATE, true);
                    setRotateGone();
                    break;
                case R.id.main_rotate_close:
                    SetAdUtil.track("主页面", "点击好评close按钮", "", 1);
                    PreData.putDB(MainActivity.this, MyConstant.IS_ROTATE, true);
                    setRotateGone();
                    break;
                case R.id.main_msg_button:
                    SetAdUtil.track("主页面", "点击进入硬件信息", "", 1);
                    mainPresenter.jumpToActivity(PhonesMessageActivity.class, 1);
                    break;
                case R.id.main_power_button:
                    SetAdUtil.track("主页面", "点击进入深度清理", "", 1);
                    mainPresenter.jumpToActivity(DeepingActivity.class, 1);
                    break;
//                case R.id.main_file_button:
//                    SetAdUtil.track("主页面", "点击进入文件管理", "", 1);
//                    PreData.putDB(MainActivity.this, MyConstant.FILE_CLEAN, true);
//                    mainPresenter.jumpToActivity(PhoneFileManagerActivity.class, 1);
//                    break;
                case R.id.main_gboost_button:
                    SetAdUtil.track("主页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(MainActivity.this, MyConstant.GBOOST_CLEAN, true);
                    mainPresenter.jumpToActivity(GoodGameActivity.class, 1);
                    break;
                case R.id.main_picture_button:
                    SetAdUtil.track("主页面", "点击进入相似图片", "", 1);
                    PreData.putDB(MainActivity.this, MyConstant.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(PictActivity.class, 1);
                    break;
//                case R.id.main_notifi_button:
//                    SetAdUtil.track("主页面", "点击进入通知栏清理", "", 1);
//                    PreData.putDB(MainActivity.this, MyConstant.NOTIFI_CLEAN, true);
//                    if (!MyUtils.isNotificationListenEnabled(MainActivity.this) || !PreData.getDB(MainActivity.this, MyConstant.KEY_NOTIFI, true)) {
//                        Intent intent6 = new Intent(MainActivity.this, NotifingAnimationActivity.class);
//                        startActivityForResult(intent6, 1);
//                    } else {
//                        Intent intent6 = new Intent(MainActivity.this, MyNotifingActivity.class);
//                        startActivityForResult(intent6, 1);
//                    }
//                    break;
                case R.id.battery_button:
                    main_battery.setVisibility(View.GONE);
                    PreData.putDB(MainActivity.this, MyConstant.FIRST_BATTERY, false);
                    Utils.writeData(MainActivity.this, BatteryConstants.CHARGE_SAVER_SWITCH, true);
                    initSideData();
//                    adapter.notifyDataSetChanged();
                    SetAdUtil.track("主界面", "充电屏保引导", "叉掉", 1);
                    break;
                case R.id.battery_cha:
                    main_battery.setVisibility(View.GONE);
                    SetAdUtil.track("主界面", "充电屏保引导", "打开", 1);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MyConstant.SETTING_RESUIL) {
            initSideData();
//            adapter.notifyDataSetChanged();
        } else if (resultCode == MyConstant.COOLING_RESUIL) {
            int wendu = data.getIntExtra("wendu", 0);
            temp -= wendu;
            if (temp == 0) {
                temp = 40;
            }
        } else if (requestCode == 100) {
            if (MyUtils.isNotificationListenEnabled(MainActivity.this)) {
                PreData.putDB(MainActivity.this, MyConstant.KEY_NOTIFI, true);
                Intent intent = new Intent(MainActivity.this, MyNotifingActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(MainActivity.this, NotifingAnimationActivity.class);
                startActivityForResult(intent, 1);
            }
        } else if (resultCode == MyConstant.RAM_RESUIL) {
        } else if (resultCode == MyConstant.JUNK_RESUIL) {
            main_junk_h.setVisibility(View.GONE);
        } else if (resultCode == MyConstant.POWER_RESUIL) {

        }
    }

    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        Log.e("main", "=============initSd+");
        main_custom_sd.startProgress(true, percent);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //根据剩余的空间大小来设置单位
                if (sd_kongxian < 1024) {
                    junkSize = MyUtils.convertStorage(sd_kongxian, false) + "B";
                } else if (sd_kongxian < 1048576) {
                    junkSize = MyUtils.convertStorage(sd_kongxian, false) + "KB";
                } else if (sd_kongxian < 1073741824) {
                    junkSize = MyUtils.convertStorage(sd_kongxian, false) + "MB";
                } else {
                    junkSize = MyUtils.convertStorage(sd_kongxian, false) + "GB";
                }
                //设置硬件信息里的剩余空间
                main_msg_sd_percent.setText(junkSize);
                if (main_junk_h.getVisibility() == View.INVISIBLE) {
                    long junk_size = CleanManager.getInstance(MainActivity.this).getApkSize() + CleanManager.getInstance(MainActivity.this).getCacheSize() +
                            CleanManager.getInstance(MainActivity.this).getUnloadSize() + CleanManager.getInstance(MainActivity.this).getLogSize() + CleanManager.getInstance(MainActivity.this).getDataSize();
                    junkSize1 = MyUtils.convertStorage(junk_size, true);
                    if (junk_size > 0) {
                        main_junk_h.setText(MyUtils.convertStorage(junk_size, true));
                        main_junk_h.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void initRam(final int percent, final String size) {
        Log.e("main", "===========");
        main_custom_ram.startProgress(false, percent);
        main_custom_ram.setCustomRoundListener(new CustomRoundCpu.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置硬件信息里的已用内存
                        main_msg_ram_percent.setText(String.valueOf(progress) + "%");
                    }
                });
            }
        });
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
        main_water.stop();
        main_circlewaveview.stop();
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
        main_water.start();
        AndroidSdk.onResumeWithoutTransition(this);
        Log.e("ad_mob_l", "h=" + ll_ad.getHeight() + "w=" + ll_ad.getWidth());
        initData();
        if (PreData.getDB(this, MyConstant.GOODGAME, 1) == 0) {
            main_gboost_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.PICTUREX, 1) == 0) {
            main_picture_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.POWERACTIVITY, 1) == 0) {
            main_power_button.setVisibility(View.GONE);
        }
        power_size.setText(getString(R.string.power_1, startList.size() + "") + " ");
        // 充电屏保关闭智能充电，刷新无效果，重新调用 initSideData()可以
//        adapter.notifyDataSetChanged();
        main_circlewaveview.startCircleWaveCiew();
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
            SetAdUtil.track("主界面", "充电屏保引导", "返回键返回", 1);
            return;
        }
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {
            if (PreData.getDB(this, MyConstant.FULL_EXIT, 0) == 1) {
                AndroidSdk.showFullAd("icleaner_exit_full");
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

    //退出
    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_exit, null);
        LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (PreData.getDB(this, MyConstant.FULL_EXIT, 0) == 0) {
            View nativeExit = SetAdUtil.getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_2);
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
        if (PreData.getDB(this, MyConstant.IS_ACTION_BAR, true)) {
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
        if (lot_ad != null) {
            lot_ad.clearAnimation();
        }
        if (main_circlewaveview != null) {
            main_circlewaveview.stop();
        }
    }
}