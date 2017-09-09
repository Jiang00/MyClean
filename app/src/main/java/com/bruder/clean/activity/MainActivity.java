package com.bruder.clean.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
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
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.bruder.clean.customeview.CustomRoundCpuView;
import com.bruder.clean.customeview.ListViewForScrollMyView;
import com.bruder.clean.customeview.MyMainScrollView;
import com.bruder.clean.customeview.PullToMyRefreshLayout;
import com.bruder.clean.entity.SideInfo;
import com.bruder.clean.junk.R;
import com.bruder.clean.myadapter.SideAdapter;
import com.bruder.clean.myview.MainMyView;
import com.bruder.clean.presenter.MainPresenter;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.TransmitValue;
import com.bruder.clean.util.UtilAd;
import com.cleaner.heart.CleanManager;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.my.bruder.charge.saver.Util.Constants;
import com.my.bruder.charge.saver.Util.Utils;
import com.sample.lottie.LottieAnimationView;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements MainMyView, DrawerLayout.DrawerListener {

    private String TAG_HUA = "bruder_hua";
    private String TAG_FULL_PULL = "pull_full";
    private String TAG_START_FULL = "bruder_start_native";
    private String TAG_EXIT_FULL = "bruder_exit_native";
    private String TAG_SIDE = "bruder_side";
    private String TAG_MAIN = "bruder_main";
    PullToMyRefreshLayout main_pull_refresh;
    LinearLayout l_title_left, l_title_right;
    LinearLayout l_title_ad;
    MyMainScrollView main_scroll_view;
    ImageView clear1, clear2, clear3;
    public static final String TAG = "MainActivity";
    TextView main_garbage_button, main_garbage_wait_clear, main_clear_button, main_garbage_company;
    ImageView iv_title_left;
    RelativeLayout main_junk_button, main_ram_button, main_manager_button;
    TextView main_junk_h, main_ram_h, main_cooling_h;
    LinearLayout main_rotate_all;
    RelativeLayout main_clear_relativeLayout, main_clear_relativeLayout1;
    CustomRoundCpuView main_custom_cpu, main_custom_sd, main_custom_ram;
    LinearLayout main_theme_button, main_cooling_button;
    FrameLayout main_scale_all;
    Button main_power_button, main_file_button, main_notifi_button;
    Button main_rotate_good;
    Button main_good_refuse;
    private AnimatorSet set;
    TextView main_msg_ram_percent, main_msg_sd_percent, main_msg_sd_unit, main_msg_cpu_percent;
    ListViewForScrollMyView side_listView;
    LinearLayout main_msg_button;
    LinearLayout main_picture_button;
    ProgressBar ad_progressbar;
    RelativeLayout poweracativity, fileactivity, notifiactivity, main_more;
    DrawerLayout main_drawer;
    LinearLayout ll_ad, ll_ad_side, ad_native_2;
    LinearLayout ll_ad_full;
    ImageView lot_ad;
    ImageView main_full_time;
    FrameLayout fl_lot_side;

    private boolean isFirstIn = false;// 判断是否是第一次登陆
    private ArrayList<View> arrayList;//记录main_circle布局文件的控件绑定信息
    private AlertDialog dialog, dialog1;
    private boolean mDrawerOpened = false;
    LottieAnimationView lot_side;

    private Handler handler = new Handler();
    private MyApplication cleanApplication;
    private SideAdapter adapter;
    private MainPresenter mainPresenter;
    private int temp;
    int sdProgress;
    private long allSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        cleanApplication = (MyApplication) getApplication();
        isSharedPreferences();
        //Build.VERSION.SDK_INT  判断Android SDK版本号
        // Build.VERSION_CODES.KITKAT  是一个Android的版本4.4
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }
        tuiGuang();

        arrayList = new ArrayList<>();
        // 获得指定的布局文件，然后通过其对象绑定控件
        View view = LayoutInflater.from(this).inflate(R.layout.main_circle, null);
        main_custom_cpu = (CustomRoundCpuView) findViewById(R.id.main_custom_cpu);
        main_garbage_button = (TextView) findViewById(R.id.main_garbage_button);
        main_garbage_wait_clear = (TextView) findViewById(R.id.main_garbage_wait_clear);
        main_garbage_company = (TextView) findViewById(R.id.main_garbage_company);
        main_clear_button = (TextView) findViewById(R.id.main_clear_button);
        main_clear_relativeLayout = (RelativeLayout) findViewById(R.id.main_clear_relativeLayout);
        main_clear_relativeLayout1 = (RelativeLayout) findViewById(R.id.main_clear_relativeLayout1);
        main_custom_sd = (CustomRoundCpuView) findViewById(R.id.main_custom_sd);
        main_custom_ram = (CustomRoundCpuView) findViewById(R.id.main_custom_ram);
        clear2 = (ImageView) findViewById(R.id.clear2);
        clear3 = (ImageView) findViewById(R.id.clear3);
        clear1 = (ImageView) findViewById(R.id.clear1);
        arrayList.add(view);

        // 广告
        View viewpager_2 = LayoutInflater.from(this).inflate(R.layout.main_ad, null);
        LinearLayout view_ad = (LinearLayout) viewpager_2.findViewById(R.id.view_ad);
        // native_ad_2是点击广告跳转的界面
        View adView = UtilAd.getNativeAdView(TAG_HUA, R.layout.native_ad_2);
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
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        //设置侧边栏滑出距离,从屏幕哪里可以滑出
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);
    }

    //判断是否是第一次登陆，并弹出对话框提示
    private void isSharedPreferences() {
        // 第一个参数是在手机里的一个文件名，第二个参数是安全性
        SharedPreferences pref = this.getSharedPreferences(com.cleaner.util.Constant.SHARED_FILE, 0);
        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = pref.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            pref = this.getSharedPreferences(com.cleaner.util.Constant.SHARED_FILE, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirstIn", false);
            // editor.commit();若没有对应的文件名，则创建
            editor.commit();
            isFirstInDialog();
        }
    }

    // 第一次登陆对话框
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void isFirstInDialog() {
        // 绑定布局文件
        View view = View.inflate(this, R.layout.dialog_first, null);
        // 绑定id
        TextView frist_button = (TextView) view.findViewById(R.id.frist_button);
        ImageView main_cancel = (ImageView) view.findViewById(R.id.main_cancel);
        // 点击Enable事件
        frist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();// 隐藏对话框，并释放资源
                Utils.writeData(MainActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                initSideData();
//                adapter.notifyDataSetChanged();
            }
        });
        main_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1 = new AlertDialog.Builder(this, R.style.add_dialog).create();
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.show();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        WindowManager.LayoutParams lp = dialog1.getWindow().getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        lp.height = dm.heightPixels; //设置高度
        if (DataPre.getDB(this, Constant.IS_ACTION_BAR, true)) {
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
            dialog1.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
        dialog1.getWindow().setAttributes(lp);
        dialog1.getWindow().setContentView(view);
    }

    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        main_scroll_view = (MyMainScrollView) findViewById(R.id.main_scroll_view);
        main_pull_refresh = (PullToMyRefreshLayout) findViewById(R.id.main_pull_refresh);
        main_scale_all = (FrameLayout) findViewById(R.id.main_scale_all);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        l_title_left = (LinearLayout) findViewById(R.id.l_title_left);
        l_title_right = (LinearLayout) findViewById(R.id.l_title_right);
        l_title_ad = (LinearLayout) findViewById(R.id.l_title_ad);
        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_manager_button = (RelativeLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (LinearLayout) findViewById(R.id.main_cooling_button);
        main_theme_button = (LinearLayout) findViewById(R.id.main_theme_button);
        main_junk_h = (TextView) findViewById(R.id.main_junk_h);
        main_ram_h = (TextView) findViewById(R.id.main_ram_h);
        main_cooling_h = (TextView) findViewById(R.id.main_cooling_h);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_good_refuse = (Button) findViewById(R.id.main_good_refuse);
        main_rotate_good = (Button) findViewById(R.id.main_rotate_good);
        main_msg_button = (LinearLayout) findViewById(R.id.main_msg_button);
        main_power_button = (Button) findViewById(R.id.main_power_button);
        main_notifi_button = (Button) findViewById(R.id.main_notifi_button);
        main_file_button = (Button) findViewById(R.id.main_file_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        main_msg_ram_percent = (TextView) findViewById(R.id.main_msg_ram_percent);
        main_msg_sd_percent = (TextView) findViewById(R.id.main_msg_sd_percent);
        main_msg_sd_unit = (TextView) findViewById(R.id.main_msg_sd_unit);
        main_msg_cpu_percent = (TextView) findViewById(R.id.main_msg_cpu_percent);
        side_listView = (ListViewForScrollMyView) findViewById(R.id.side_listView);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);

        poweracativity = (RelativeLayout) findViewById(R.id.poweracativity);
        fileactivity = (RelativeLayout) findViewById(R.id.fileactivity);
        notifiactivity = (RelativeLayout) findViewById(R.id.notifiactivity);
        main_more = (RelativeLayout) findViewById(R.id.main_more);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);
        lot_ad = (ImageView) findViewById(R.id.lot_ad);

        //lot_side = (LottieAnimationView) findViewById(R.id.lot_side);
        fl_lot_side = (FrameLayout) findViewById(R.id.fl_lot_side);
//        lot_family = (LottieAnimationView) findViewById(R.id.lot_family);

        //float left, float top, float right, float bottom  xyxy
        view1 = findViewById(R.id.moved_item);

    }

    //初始化监听
    public void onClick() {
        main_pull_refresh.setOnRefreshListener(refreshListener);
        iv_title_left.setOnClickListener(onClickListener);
        l_title_left.setOnClickListener(onClickListener);
        l_title_right.setOnClickListener(onClickListener);
        l_title_ad.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_theme_button.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_good_refuse.setOnClickListener(onClickListener);
        main_msg_button.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        fl_lot_side.setOnClickListener(onClickListener);
//        lot_family.setOnClickListener(onClickListener);
        main_clear_button.setOnClickListener(onClickListener);
        main_clear_relativeLayout.setOnClickListener(onClickListener);
        main_clear_relativeLayout1.setOnClickListener(onClickListener);
        main_scroll_view.setScrollViewListener(new MyMainScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MyMainScrollView scrollView, int x, int y, int oldx, int oldy) {
            }
        });
    }

    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        main_custom_sd.startProgress(true, percent);
        final Runnable sdRunable = new Runnable() {
            @Override
            public void run() {
            }
        };
        main_custom_sd.setCustomRoundListener(new CustomRoundCpuView.CustomRoundListener() {
            @Override
            public void progressUpdate(int progress) {
                MainActivity.this.sdProgress = progress;
                handler.post(sdRunable);
            }

        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
                long junk_size = CleanManager.getInstance(MainActivity.this).getApkSize() + CleanManager.getInstance(MainActivity.this).getCacheSize() +
                        CleanManager.getInstance(MainActivity.this).getUnloadSize() + CleanManager.getInstance(MainActivity.this).getLogSize()
                        + CleanManager.getInstance(MainActivity.this).getDataSize();
                allSize = junk_size;
                if (main_junk_h.getVisibility() == View.INVISIBLE) {
                    if (junk_size > 0) {
                        main_junk_h.setText(Util.convertStorage(junk_size, true));
                        main_junk_h.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void initGuard(int num, RotateAnimation rotateAnimation) {
    }

    // 菜单
    @Override
    public void initSideData() {
        if (adapter == null) {
            adapter = new SideAdapter(this);
        }
        adapter.clear();
        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, false)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, DataPre.getDB(this, Constant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DataPre.getDB(this, Constant.FILEACTIVITY, 0) != 0) {
            adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DataPre.getDB(this, Constant.POWERACATIVITY, 0) != 0) {
            adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        }
        adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DataPre.getDB(this, Constant.NOTIFIACTIVITY, 0) != 0) {
            adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        }
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        side_listView.setAdapter(adapter);
    }


    @Override
    public void loadAirAnimator(TranslateAnimation translate) {
    }

    @Override
    public void openDrawer() {
        main_drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void setRotateGone() {
        //评价过了，设置隐藏
        main_rotate_all.setVisibility(View.GONE);
    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
        main_custom_cpu.startProgress(false, temp);
        main_custom_cpu.setCustomRoundListener(new CustomRoundCpuView.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置硬件信息里的CPU温度
                        main_msg_cpu_percent.setText(String.valueOf(progress));
                        if (main_cooling_h.getVisibility() == View.INVISIBLE) {
                            main_cooling_h.setText(String.valueOf(temp) + " ℃");
                            main_cooling_h.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

    }

    //上拉刷新监听
    PullToMyRefreshLayout.OnRefreshListener refreshListener = new PullToMyRefreshLayout.OnRefreshListener() {
        // 下拉刷新操作
        @Override
        public void onRefresh(PullToMyRefreshLayout pullToRefreshLayout) {
        }

        //上拉加载操作
        @Override
        public void onLoadMore(PullToMyRefreshLayout pullToRefreshLayout) {
            UtilAd.track("主页面", "刷新成功", "", 1);
            AndroidSdk.loadNativeAd(TAG_FULL_PULL, R.layout.native_ad_full, new ClientNativeAd.NativeAdLoadListener() {
                @Override
                public void onNativeAdLoadSuccess(View view) {
                    main_pull_refresh.loadmoreFinish(PullToMyRefreshLayout.SUCCEED);
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
                    main_pull_refresh.loadmoreFinish(PullToMyRefreshLayout.FAIL);
                }
            });
        }
    };

    //点击事件监听
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.l_title_left:
                    // 菜单按钮
                    mainPresenter.openDrawer();
                    UtilAd.track("主页面", "点击进入侧边栏按钮", "", 1);
                    break;
                case R.id.l_title_right:
                    //设置按钮
                    UtilAd.track("主页面", "点击进入设置页面", "", 1);
                    mainPresenter.jumpToActivity(SetActivity.class, 1);
                    break;
                case R.id.l_title_ad:
                    //设置按钮
                    UtilAd.track("主页面", "点击AD", "", 1);

                    showMainAdAnim();
                    break;
                case R.id.iv_title_left:
                    // 菜单按钮
                    mainPresenter.openDrawer();
                    UtilAd.track("主页面", "点击进入侧边栏按钮", "", 1);
                    break;
                case R.id.main_clear_relativeLayout:
                    showToast((String) getText(R.string.toast_ing));
                    UtilAd.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(GarbageActivity.class, 1);
                    break;
                case R.id.main_clear_button:
                    //垃圾清理按钮
                    showToast((String) getText(R.string.toast_ing));
                    UtilAd.track("主页面", "点击所有垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(GarbageAndRamActivity.class, 1);
                    break;
                //好评上面的六个按钮点击事件
                case R.id.main_junk_button:
                    // 垃圾清理事件
                    UtilAd.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(GarbageActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    //ram清理按钮
                    UtilAd.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(PhoneRamAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    //  应用管理按钮
                    UtilAd.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(AppManagerActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    //降温
                    UtilAd.track("主页面", "点击降温按钮", "", 1);
                    mainPresenter.jumpToActivity(CoolActivity.class, 1);
                    break;
                case R.id.main_theme_button:
                    //游戏加速
                    UtilAd.track("主页面", "点击进入buton游戏加速", "", 1);
                    mainPresenter.jumpToActivity(GBoostingActivity.class, 1);
                    break;
                case R.id.main_good_refuse:
                    UtilAd.track("主页面", "点击好评refuse按钮", "", 1);
                    mainPresenter.clickRotate(false);
                    break;
                case R.id.main_rotate_good:
                    UtilAd.track("主页面", "点击好评good按钮", "", 1);
                    mainPresenter.clickRotate(true);
                    break;
                case R.id.main_msg_button:
                    //硬件信息
                    UtilAd.track("主页面", "点击进入硬件信息", "", 1);
                    mainPresenter.jumpToActivity(InformationActivity.class, 1);
                    break;
                //下方五个清理事件
                case R.id.main_power_button:
                    // 深度清理
                    UtilAd.track("主页面", "点击进入深度清理", "", 1);
                    DataPre.putDB(MainActivity.this, Constant.DEEP_CLEAN, true);
                    mainPresenter.jumpToActivity(PoweringActivity.class, 1);
                    break;
                case R.id.main_file_button:
                    //文件管理
                    UtilAd.track("主页面", "点击进入文件管理", "", 1);
                    DataPre.putDB(MainActivity.this, Constant.FILE_CLEAN, true);
                    mainPresenter.jumpToActivity(FilesActivity.class, 1);
                    break;
                case R.id.main_picture_button:
                    //相似图片
                    UtilAd.track("主页面", "点击进入相似图片", "", 1);
                    DataPre.putDB(MainActivity.this, Constant.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(PicturesActivity.class, 1);
                    break;
                case R.id.main_notifi_button:
                    //通知栏清理
                    UtilAd.track("主页面", "点击进入通知栏清理", "", 1);
                    DataPre.putDB(MainActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!DataPre.getDB(MainActivity.this, Constant.KEY_NOTIFI, true) || !Util.isNotificationListenEnabled(MainActivity.this)) {
                        Intent intent6 = new Intent(MainActivity.this, NotifiIfActivity.class);
                        startActivityForResult(intent6, 1);
                    } else {
                        Intent intent6 = new Intent(MainActivity.this, NotifingActivity.class);
                        startActivityForResult(intent6, 1);
                    }
                    break;
            }
        }
    };

    View view1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showMainAdAnim() {

        showAd();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showAd() {
        View view = View.inflate(this, R.layout.main_ad_dialog, null);
        final LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        dialog = new AlertDialog.Builder(this, R.style.add_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        if (DataPre.getDB(this, Constant.FULL_EXIT, 0) == 0) {
            final View nativeExit = UtilAd.getNativeAdView(TAG_EXIT_FULL, R.layout.main_ad_native);
            if (nativeExit != null) {
                ll_ad_exit.addView(nativeExit);
                ll_ad_exit.setVisibility(View.VISIBLE);
            } else {
                dialog.dismiss();
            }
        }
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        lp.height = dm.heightPixels; //设置高度
        if (DataPre.getDB(this, Constant.IS_ACTION_BAR, true)) {
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
    protected void onResume() {
        super.onResume();
        initSideData();
//        adapter.notifyDataSetChanged();
        if (TransmitValue.isJunk && TransmitValue.isRam && TransmitValue.isCool) {
            main_clear_relativeLayout.setVisibility(View.GONE);
            main_clear_relativeLayout1.setVisibility(View.VISIBLE);
        }
        if (DataPre.getDB(this, Constant.POWERACATIVITY, 0) == 0) {
            poweracativity.setVisibility(View.GONE);
        }
        if (DataPre.getDB(this, Constant.FILEACTIVITY, 0) == 0) {
            fileactivity.setVisibility(View.GONE);
        }
        if (DataPre.getDB(this, Constant.NOTIFIACTIVITY, 0) == 0) {
            notifiactivity.setVisibility(View.GONE);
            main_more.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerOpened = true;
        Log.e(TAG, "onDrawerOpened");
        if (lot_side != null) {
            lot_side.playAnimation();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lot_side != null) {
            lot_side.clearAnimation();
        }
    }

    @Override
    public void closeDrawer() {
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        }
    }

    //内存
    @Override
    public void initRam(final int percent, final String size) {
        main_custom_ram.startProgress(false, percent);
        main_custom_ram.setCustomRoundListener(new CustomRoundCpuView.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        // 设置内存占用率
//                        main_ram_per.setText(String.valueOf(progress) + "%");
//                        // 设置运行内存信息main_ram_h
//                        main_ram_size.setText(size);
                        // 设置硬件信息里的已用内存
                        main_msg_ram_percent.setText(String.valueOf(progress));
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
    public void loadFullAd() {
        if (DataPre.getDB(this, Constant.FULL_MAIN, 0) == 1) {
        } else {
            View nativeView = UtilAd.getNativeAdView(TAG_MAIN, R.layout.native_ad_2);
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
            View nativeView_side = UtilAd.getNativeAdView(TAG_SIDE, R.layout.native_ad_2);
            if (ll_ad_side != null && nativeView_side != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_side.getLayoutParams();
                layout_ad.height = nativeView_side.getMeasuredHeight();
                ll_ad_side.setLayoutParams(layout_ad);
                ll_ad_side.addView(nativeView_side);
            }

        }
        if (DataPre.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.showFullAd("bruder_start_full");
        } else {
            View nativeView_full = UtilAd.getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_main);
            if (nativeView_full != null) {
                ll_ad_full.addView(nativeView_full);
                ll_ad_full.setVisibility(View.VISIBLE);
                nativeView_full.findViewById(R.id.ad_delete).setVisibility(View.GONE);
                main_full_time = (ImageView) nativeView_full.findViewById(R.id.main_full_time);
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
                int skip = DataPre.getDB(this, Constant.SKIP_TIME, 6);
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

    @Override
    protected void onPause() {
        super.onPause();
        pauseAnimator();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.COOLING_RESUIL) {
            int wendu = data.getIntExtra("wendu", 0);
            temp -= wendu;
            if (temp == 0) {
                temp = 40;
            }
            main_cooling_h.setVisibility(View.GONE);
        } else if (requestCode == 100) {
            if (Util.isNotificationListenEnabled(MainActivity.this)) {
                DataPre.putDB(MainActivity.this, Constant.KEY_NOTIFI, true);
                Intent intent = new Intent(MainActivity.this, NotifingActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(MainActivity.this, NotifiIfActivity.class);
                startActivityForResult(intent, 1);
            }
        } else if (resultCode == Constant.RAM_RESUIL) {
            main_ram_h.setVisibility(View.GONE);
        } else if (resultCode == Constant.JUNK_RESUIL) {
            main_junk_h.setVisibility(View.GONE);
        } else if (resultCode == Constant.RAM_JUNK_RESUIL) {
            main_ram_h.setVisibility(View.GONE);
            main_junk_h.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    //退出
    public void onBackPressed() {
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {
            if (DataPre.getDB(this, Constant.FULL_EXIT, 0) == 1) {
                AndroidSdk.showFullAd("bruder_exit_full");
            }
            showExitDialog();
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        //旋转动画
//        startAnimator(0f, 360f, 0f, -360f);
        startAnimator(0f, -359f, 0f, 359f);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                if (allSize == 0) {
                    allSize = CleanManager.getInstance(MainActivity.this).getApkSize() + CleanManager.getInstance(MainActivity.this).getCacheSize() + CleanManager.getInstance(MainActivity.this).getUnloadSize() + CleanManager.getInstance(MainActivity.this).getLogSize()
                            + CleanManager.getInstance(MainActivity.this).getDataSize() + CleanManager.getInstance(MainActivity.this).getRamSize();
                } else {
                    allSize += CleanManager.getInstance(MainActivity.this).getRamSize();
                }

                for (long i = 0; i <= allSize; i += (allSize / 15)) {
//                    if (allSize == 0) {
//                        i += 15;
//                    }
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String mainGarbageButtonSize = Util.convertStorage(finalI, true);
                            if ((finalI + allSize / 15) >= allSize && allSize != 0) {
                                main_garbage_button.setText(mainGarbageButtonSize.subSequence(0, mainGarbageButtonSize.length() - 2));
                                main_garbage_company.setText(mainGarbageButtonSize.subSequence(mainGarbageButtonSize.length() - 2, mainGarbageButtonSize.length()));
                                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_notifi);
//                                main_garbage_wait_clear.startAnimation(animation);
//                                main_garbage_wait_clear.setVisibility(View.VISIBLE);
                                main_clear_button.setText(R.string.garbage);
//                                pauseAnimator();
//                                startAnimator(0f, -360f, 0f, 360f);
                            } else if (allSize == 0) {
//                                pauseAnimator();
                                main_clear_relativeLayout.setVisibility(View.GONE);
                                main_clear_relativeLayout1.setVisibility(View.VISIBLE);
//                                startAnimator(0f, -360f, 0f, 360f);
                            } else {
                                // 设置大小
                                main_garbage_button.setText(mainGarbageButtonSize.subSequence(0, mainGarbageButtonSize.length() - 2));
                                main_garbage_company.setText(mainGarbageButtonSize.subSequence(mainGarbageButtonSize.length() - 2, mainGarbageButtonSize.length()));
                            }
                        }
                    });
                    if (allSize == 0) {
                        break;
                    }
                }
            }
        }).start();
        AndroidSdk.onResumeWithoutTransition(this);
        Log.e("ad_mob_l", "h=" + ll_ad.getHeight() + "w=" + ll_ad.getWidth());
//        if (lot_family != null) {
//            lot_family.playAnimation();
//        }
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Util.dp2px(115), Util.dp2px(130));
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
//        main_guard_rotate.startAnimation(rotateAnimation);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mainPresenter.reStart();
        initCpu(temp);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_exit, null);
        LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (DataPre.getDB(this, Constant.FULL_EXIT, 0) == 0) {
            View nativeExit = UtilAd.getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_full_exit);
            if (nativeExit != null) {
                ll_ad_exit.addView(nativeExit);
                ll_ad_exit.setVisibility(View.VISIBLE);
            }
        }
        // 点击确定退出事件
        exit_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                endAnimator();
                TransmitValue.isCool = false;
                TransmitValue.isRam = false;
                TransmitValue.isJunk = false;
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
        if (DataPre.getDB(this, Constant.IS_ACTION_BAR, true)) {
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
    public void onDrawerClosed(View drawerView) {
        mDrawerOpened = false;
        Log.e(TAG, "onDrawerClosed");
        if (lot_side != null) {
            lot_side.pauseAnimation();
        }
    }

    // 启动动画
    public void startAnimator(float start1, float end1, float start2, float end2) {
        set = new AnimatorSet();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(clear3, "rotation", start1, end1);
        animator2.setRepeatCount(-1);//设置重复次数
        LinearInterpolator lin = new LinearInterpolator();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(clear2, "rotation", start2, end2);
        animator3.setRepeatCount(-1);//设置重复次数
        set.setDuration(10000);
        set.setInterpolator(lin);
        set.play(animator2).with(animator3);
        set.start();
    }

    // 暂停动画
    public void pauseAnimator() {
        if (set != null) {
            set.cancel();
        }
    }

    // 停止动画
    public void endAnimator() {
        set.end();
    }
}
