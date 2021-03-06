package com.supers.clean.junk.activity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
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
import com.android.clean.util.Constant;
import com.android.clean.util.LoadManager;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AdListener;
import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.android.client.PaymentSystemListener;
import com.android.theme.internal.data.ThemeManager;
import com.eos.manager.AppLockPatternEosActivity;
import com.eos.manager.meta.SecurityMyPref;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.module.charge.saver.service.BatteryService;
import com.eos.ui.demo.cross.CrossManager;
import com.eos.ui.demo.dialog.DialogManager;
import com.eos.ui.demo.entries.CrossData;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.rd.PageIndicatorView;
import com.sample.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.SideAdapter;
import com.supers.clean.junk.customeview.CustomRoundCpu;
import com.supers.clean.junk.customeview.ListViewForScrollView;
import com.supers.clean.junk.customeview.LoadTime;
import com.supers.clean.junk.customeview.MyScrollView;
import com.supers.clean.junk.customeview.PullToRefreshLayout;
import com.supers.clean.junk.customeview.SnowView;
import com.supers.clean.junk.entity.SideInfo;
import com.supers.clean.junk.presenter.MainPresenter;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.UtilGp;
import com.supers.clean.junk.view.MainView;

import org.json.JSONException;
import org.json.JSONObject;

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
    Button main_rotate_good;
    ImageView rotate_ic;
    ImageView rotate_delete;
    LinearLayout main_tuiguang_button;
    FrameLayout fl_lot_main;
    TextView main_msg_tuiguang;
    LinearLayout main_msg_button;
    TextView main_msg_ram_percent, main_msg_sd_percent, main_msg_sd_unit, main_msg_cpu_percent;
    TextView main_gurad_num;
    ImageView main_guard_rotate;
    FrameLayout main_guard_all;
    ListViewForScrollView side_listView;
    DrawerLayout main_drawer;
    LinearLayout ll_ad, ll_ad_side;
    com.mingle.widget.LinearLayout ll_ad_full;
    ProgressBar ad_progressbar;
//    SnowView ad_snow_view;

    // LottieAnimationView lot_side;
    FrameLayout fl_lot_side;
    ImageView side_title;
    LottieAnimationView lot_main;
    LottieAnimationView lot_main_tap;
    ImageView lot_family;
    ImageView bill;
    FrameLayout load_loading;
    ImageView load_1, load_2;
    ImageView menu_hong;

    LottieAnimationView lot_side;
    LinearLayout main_battery;

    private String TAG_MAIN = "eos_main";
    private String TAG_HUA = "eos_hua";
    private String TAG_SIDE = "eos_side";
    private String TAG_START_FULL = "eos_start_native";
    private String TAG_EXIT_FULL = "eos_exit_native";
    private String TAG_DETECT = "detect";
    private String TAG_LOAD_FULL = "loading_full";

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
    private CrossData.CrossPromotionBean bean;
    private View viewpager_3;
    private String from;
    private AlertDialog dialog;
    private LinearLayout deep;
    private FrameLayout lot_tap;
    private ArrayList<View> arrayList;
    private AnimatorSet animatorSet;
    private AnimationDrawable animationDrawable;
    private AnimatorSet animatorSet_rotate;
    private AlertDialog bill_dialog;
    private int bill_id;
    private View viewpager_2;
    private View nativeExit;

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
        main_rotate_good = (Button) findViewById(R.id.main_rotate_good);
        rotate_ic = (ImageView) findViewById(R.id.rotate_ic);
        rotate_delete = (ImageView) findViewById(R.id.rotate_delete);
        main_tuiguang_button = (LinearLayout) findViewById(R.id.main_tuiguang_button);
        fl_lot_main = (FrameLayout) findViewById(R.id.fl_lot_main);
        main_msg_tuiguang = (TextView) findViewById(R.id.main_msg_tuiguang);
        main_msg_button = (LinearLayout) findViewById(R.id.main_msg_button);
        main_msg_ram_percent = (TextView) findViewById(R.id.main_msg_ram_percent);
        main_msg_sd_percent = (TextView) findViewById(R.id.main_msg_sd_percent);
        main_msg_sd_unit = (TextView) findViewById(R.id.main_msg_sd_unit);
        main_msg_cpu_percent = (TextView) findViewById(R.id.main_msg_cpu_percent);
        main_gurad_num = (TextView) findViewById(R.id.main_gurad_num);
        main_guard_rotate = (ImageView) findViewById(R.id.main_guard_rotate);
        main_guard_all = (FrameLayout) findViewById(R.id.main_guard_all);
        side_listView = (ListViewForScrollView) findViewById(R.id.side_listView);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);
//        ad_snow_view = (SnowView) findViewById(R.id.ad_snow_view);
        //lot_side = (LottieAnimationView) findViewById(R.id.lot_side);
        fl_lot_side = (FrameLayout) findViewById(R.id.fl_lot_side);
        side_title = (ImageView) findViewById(R.id.side_title);
        lot_family = (ImageView) findViewById(R.id.lot_family);
        bill = (ImageView) findViewById(R.id.bill);
        load_loading = (FrameLayout) findViewById(R.id.load_loading);
        load_1 = (ImageView) findViewById(R.id.load_1);
        load_2 = (ImageView) findViewById(R.id.load_2);
        menu_hong = (ImageView) findViewById(R.id.menu_hong);
        main_battery = (LinearLayout) findViewById(R.id.main_battery);
    }

    boolean isSave;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("onSave", true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isSave = savedInstanceState.getBoolean("onSave");
        }

        setContentView(R.layout.activity_dra);
        cleanApplication = (MyApplication) getApplication();
        try {
            from = getIntent().getStringExtra("from");
            if (TextUtils.equals(from, "detect")) {
                showDetectDialog();
            }

            String pkg = getIntent().getStringExtra("theme_package_name");
            if (pkg != null) {
                ThemeManager.applyTheme(this, pkg, false);
                Utils.writeData(this, Constants.CHARGE_SAVER_SWITCH, true);
                startService(new Intent(this, BatteryService.class).putExtra("show", true));
                Log.e("jfy", "main=" + pkg);
            }
            if (TextUtils.equals(from, "translate")) {
                DialogManager.showCrossDialog(this, AndroidSdk.getExtraData(), "list2", "flight", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        menu_hong.setVisibility(View.GONE);
        if (PreData.getDB(this, Constant.HONG_RAM, true) || PreData.getDB(this, Constant.HONG_JUNK, true) ||
                PreData.getDB(this, Constant.HONG_MANAGER, true)) {
            menu_hong.setVisibility(View.VISIBLE);
        } else {
            menu_hong.setVisibility(View.GONE);
        }
        tuiGuang();
        arrayList = new ArrayList<>();
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
        viewpager_2 = LayoutInflater.from(this).inflate(R.layout.main_ad, null);
        LinearLayout view_ad = (LinearLayout) viewpager_2.findViewById(R.id.view_ad);
        View adView = AdUtil.getNativeAdView(this, TAG_HUA, R.layout.native_ad_2);
        if (adView != null) {
            ViewGroup.LayoutParams layout_ad = view_ad.getLayoutParams();
            if (adView.getHeight() == Util.dp2px(250)) {
                layout_ad.height = Util.dp2px(250);
            }
            view_ad.setLayoutParams(layout_ad);
            view_ad.addView(adView);
            view_ad.setGravity(Gravity.CENTER);
            arrayList.add(viewpager_2);
        }
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
            if (startList.size() == 0) {
                if (addTapTuiguang()) {
                    deep.setVisibility(View.INVISIBLE);
                    lot_tap.setVisibility(View.VISIBLE);
                    arrayList.add(viewpager_3);
                }
            } else {
                if (startList.size() > 0) {

                    tap_iv_1.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(0).pkg));
                }
                if (startList.size() > 1) {
                    tap_iv_2.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(1).pkg));
                    tap_iv_2.setVisibility(View.VISIBLE);
                }
                if (startList.size() > 2) {
                    tap_iv_3.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(2).pkg));
                    tap_iv_3.setVisibility(View.VISIBLE);
                }
                if (startList.size() <= 3) {
                    tap_ll.setVisibility(View.GONE);
                } else {
                    if (startList.size() > 3) {
                        tap_iv_4.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(3).pkg));
                    }
                    if (startList.size() > 4) {
                        tap_iv_5.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(4).pkg));
                    }
                    if (startList.size() > 5) {
                        tap_iv_6.setVisibility(View.VISIBLE);
                    }
                }
                tap_deep_text.setText(getString(R.string.tap_deep, startList.size()));
                arrayList.add(viewpager_3);
            }

        } else {
            if (addTapTuiguang()) {
                deep.setVisibility(View.INVISIBLE);
                lot_tap.setVisibility(View.VISIBLE);
                arrayList.add(viewpager_3);
            }
        }

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
                viewpager.setCurrentItem(1);
            }
        }, 7000);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewpager.setCurrentItem(2);
            }
        }, 12000);
//        }
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);

        AdUtil.track("主页面", "进入主页面", "", 1);
        if (PreData.getDB(MainActivity.this, Constant.BILL_YOUXIAO, true)) {
            bill.setVisibility(View.GONE);
            lot_family.setVisibility(View.GONE);
        } else {
            animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.zhen);
            lot_family.setBackgroundDrawable(animationDrawable);
        }
        if (PreData.getDB(this, Constant.FIRST_BATTERY, true)) {
            PreData.putDB(this, Constant.FIRST_BATTERY, false);
            main_battery.setVisibility(View.VISIBLE);
            ImageView battery_cha = (ImageView) findViewById(R.id.battery_cha);
            Button battery_button = (Button) findViewById(R.id.battery_button);
            battery_cha.setOnClickListener(onClickListener);
            battery_button.setOnClickListener(onClickListener);
        }
        if (main_rotate_all.getVisibility() == View.VISIBLE) {
            animatorSet_rotate = new AnimatorSet();
            ObjectAnimator objectAnimator_0 = ObjectAnimator.ofFloat(rotate_ic, View.ALPHA, 1, 0);
            objectAnimator_0.setDuration(800);
            ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(rotate_ic, View.SCALE_X, 0, 1);
            objectAnimator_1.setDuration(700);
            ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(rotate_ic, View.SCALE_Y, 0, 1);
            objectAnimator_2.setDuration(700);
            ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(rotate_ic, View.ALPHA, 0, 1);
            objectAnimator_3.setDuration(700);
            animatorSet_rotate.play(objectAnimator_1).with(objectAnimator_2).with(objectAnimator_3).after(objectAnimator_0);
            handler.postDelayed(runnable_rotate, 2000);
        }
    }

    Runnable runnable_rotate = new Runnable() {
        @Override
        public void run() {
            if (animatorSet_rotate != null) {
                animatorSet_rotate.start();
                handler.postDelayed(this, 3000);
            }
        }
    };

    private void showDetectDialog() {
        View view = View.inflate(this, R.layout.layout_battery_r, null);
        ImageView detect_cha = (ImageView) view.findViewById(com.eos.module.charge.saver.R.id.detect_cha);
        TextView detect_zhuangtai = (TextView) view.findViewById(com.eos.module.charge.saver.R.id.detect_zhuangtai);
        TextView detect_time = (TextView) view.findViewById(com.eos.module.charge.saver.R.id.detect_time);
        TextView detect_baifen = (TextView) view.findViewById(com.eos.module.charge.saver.R.id.detect_baifen);
        TextView detect_shiyong = (TextView) view.findViewById(com.eos.module.charge.saver.R.id.detect_shiyong);
        LinearLayout detect_ram = (LinearLayout) view.findViewById(com.eos.module.charge.saver.R.id.detect_ram);
        LinearLayout detect_ad = (LinearLayout) view.findViewById(com.eos.module.charge.saver.R.id.detect_ad);
        Button detect_clean = (Button) view.findViewById(com.eos.module.charge.saver.R.id.detect_clean);
        detect_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        detect_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToActivity(RamAvtivity.class, 1);
                dialog.dismiss();
            }
        });
        long chongdian_time = PreData.getDB(this, Constant.CONNECTED_TIME_MAIN, 60 * 60 * 1000l);
        long use_time = PreData.getDB(this, Constant.CONNECTED_LEFT_TIME_MAIN, 60 * 60 * 1000l);
        int level = PreData.getDB(this, Constant.CONNECTED_LEVEL_MAIN, 100);
        detect_zhuangtai.setText(com.eos.module.charge.saver.R.string.detect_2);
        detect_zhuangtai.setTextColor(ContextCompat.getColor(this, com.eos.module.charge.saver.R.color.A3));
        detect_time.setText(millTransFate(chongdian_time));
        detect_shiyong.setText(millTransFate(use_time));
        detect_baifen.setText(level + "%");
        int count = 0;
        detect_ram.setOrientation(LinearLayout.HORIZONTAL);
        for (JunkInfo info : CleanManager.getInstance(this).getAppRamList()) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Util.dp2px(15), Util.dp2px(15));
            layoutParams.rightMargin = Util.dp2px(1);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (count == 4) {
                imageView.setImageResource(com.eos.module.charge.saver.R.mipmap.detect_ram);
                detect_ram.addView(imageView, 0);
                break;
            }
            if (LoadManager.getInstance(this).getAppIcon(info.pkg) != null) {
                imageView.setImageDrawable(LoadManager.getInstance(this).getAppIcon(info.pkg));
                detect_ram.addView(imageView, 0);
                count++;
            }
        }
        View view_ad = AdUtil.getNativeAdView(this, TAG_DETECT, R.layout.native_ad);
        if (detect_ad != null && view_ad != null) {
            detect_ad.addView(view_ad);
        }

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

    //多少天
    public static String millTransFate(long millisecond) {
        String str = "";
        long day = millisecond / 86400000;
        long hour = (millisecond % 86400000) / 3600000;
        long minute = (millisecond % 86400000 % 3600000) / 60000;
        if (day > 0) {
            str = String.valueOf(day) + "d";
        }
        if (hour > 0) {
            str += String.valueOf(hour) + "h ";
        }
        if (minute > 0) {
            str += String.valueOf(minute) + "min";
        }
        return str;
    }

    private boolean addTapTuiguang() {
        if (bean == null || bean.pkg == null) {
            return false;
        }
        if (PreData.getDB(MainActivity.this, Constant.BILL_YOUXIAO, true)) {
            return false;
        }
        Picasso.with(this).load(bean.iconUrl).into((ImageView) viewpager_3.findViewById(R.id.ad_icon));
        Picasso.with(this).load(bean.topPicUrl).into((ImageView) viewpager_3.findViewById(R.id.ad_image));
        ((TextView) viewpager_3.findViewById(R.id.ad_title)).setText(bean.appName.get(0).content);
        ((TextView) viewpager_3.findViewById(R.id.ad_subtitle)).setText(bean.content.get(0).content.get(0));
        ((TextView) viewpager_3.findViewById(R.id.ad_action)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoadManager.getInstance(MainActivity.this).isPkgInstalled(bean.pkg)) {
                    Util.doStartApplicationWithPackageName(MainActivity.this, bean.pkg);
                } else {
                    UtilGp.openPlayStore(getApplicationContext(), bean.pkg);
                }
            }
        });
        return true;
    }

    public void tuiGuang() {
        if (PreData.getDB(MainActivity.this, Constant.BILL_YOUXIAO, true)) {
            return;
        }
        super.tuiGuang();
        Log.e("onSave", "==" + isSave);
        if (isSave) {
            return;
        }
        bean = DialogManager.getCrossManager().getCrossData(this, extraData, "list1", "side");
        if (bean != null) {
            tuiguang = bean.pkg;
        }
        try {
            DialogManager.getCrossView(getApplicationContext(), extraData, "list1", "side", true, new CrossManager.onCrossViewClickListener() {
                @Override
                public void onClick(View view) {

                }

                @Override
                public void onLoadView(View view) {
                    if (view != null) {
                        fl_lot_side.setVisibility(View.VISIBLE);
                        side_title.setVisibility(View.GONE);
                        ((ImageView) view.findViewById(R.id.cross_default_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        lot_side = (LottieAnimationView) view.findViewById(R.id.cross_default_lottie);
                        if (lot_side == null) {
                            return;
                        }
                        lot_side.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        fl_lot_side.addView(view, 0);
                        if (mDrawerOpened) {
                            lot_side.playAnimation();
                        } else {
                            lot_side.pauseAnimation();
                        }
                    } else {
                        fl_lot_side.setVisibility(View.GONE);
                        side_title.setVisibility(View.VISIBLE);
                    }
                }
            });
            DialogManager.getCrossView(getApplicationContext(), extraData, "list1", "main", true, new CrossManager.onCrossViewClickListener() {
                @Override
                public void onClick(View view) {

                }

                @Override
                public void onLoadView(View view) {
                    if (view != null) {
                        if (bean != null && bean.appName != null & bean.appName.get(0) != null && bean.appName.get(0).content != null) {
                            main_msg_tuiguang.setText(bean.appName.get(0).content);
                        }
                        ((ImageView) view.findViewById(R.id.cross_default_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        lot_main = ((LottieAnimationView) view.findViewById(R.id.cross_default_lottie));
                        lot_main.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Log.e("tuiguang", "main 不为空");
                        main_tuiguang_button.setVisibility(View.VISIBLE);
                        if (onPause) {
                            lot_main.pauseAnimation();
                        }
                        fl_lot_main.addView(view, 0);
                    } else {
                        main_tuiguang_button.setVisibility(View.GONE);
                        Log.e("tuiguang", "main 为空");
                    }
                }
            });
        } catch (Exception e) {
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
        rotate_delete.setOnClickListener(onClickListener);
        main_msg_button.setOnClickListener(onClickListener);
        main_tuiguang_button.setOnClickListener(onClickListener);
        fl_lot_side.setOnClickListener(onClickListener);
        lot_family.setOnClickListener(onClickListener);
        bill.setOnClickListener(onClickListener);

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
                        main_cpu_temp.setText(String.valueOf(progress) + "℃");
                        main_msg_cpu_percent.setText(String.valueOf(progress) + "℃");
                        if (main_cooling_h.getVisibility() == View.INVISIBLE) {
                            main_cooling_h.setText(String.valueOf(temp) + "℃");
                            if (PreData.getDB(MainActivity.this, Constant.HONG_COOLING, true)) {
                                main_cooling_h.setVisibility(View.VISIBLE);
                            }
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
                main_sd_size.setText(size);
                main_msg_sd_percent.setText(Util.convertStorage(sd_kongxian, false));
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
                        if (PreData.getDB(MainActivity.this, Constant.HONG_JUNK, true)) {
                            main_junk_h.setVisibility(View.VISIBLE);
                        }
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
                        main_ram_per.setText(String.valueOf(progress) + "%");
                        main_ram_size.setText(size);
                        main_msg_ram_percent.setText(String.valueOf(progress) + "%");
                        if (main_ram_h.getVisibility() == View.INVISIBLE) {
                            long ram_size = CleanManager.getInstance(MainActivity.this).getRamSize();
                            if (ram_size > 0) {
                                main_ram_h.setText(Util.convertStorage(ram_size, true));
                                if (PreData.getDB(MainActivity.this, Constant.HONG_RAM, true)) {
                                    main_ram_h.setVisibility(View.VISIBLE);
                                }
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
            main_gurad_num.setText(String.valueOf(num) + " ");
        }
        main_guard_rotate.startAnimation(rotateAnimation);
    }

    @Override
    public void initSideData() {
        if (adapter == null) {
            adapter = new SideAdapter(this, handler);
            side_listView.setAdapter(adapter);
        }
        adapter.clear();
        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, false)));//充电屏保
        adapter.addData(new SideInfo(R.string.battery_jiance, R.mipmap.side_detect, PreData.getDB(this, Constant.DETECT_KAIGUAN, true)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, Constant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        adapter.addData(new SideInfo(R.string.main_tool_name, R.mipmap.side_tool));//工具箱
//        adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
//        adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
//        adapter.addData(new SideInfo(R.string.call_lanjie, R.mipmap.side_call));//骚扰拦截
//        adapter.addData(new SideInfo(R.string.privacy_clean, R.mipmap.privacy_side));//隐私清理
//        adapter.addData(new SideInfo(R.string.wifi_name, R.mipmap.side_wifi));//wifi
//        adapter.addData(new SideInfo(R.string.language, R.mipmap.side_lag_setting));//yuyan
//        adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
//        adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
//        adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
        adapter.addData(new SideInfo(R.string.side_family, R.mipmap.side_theme));//family
        adapter.addData(new SideInfo(R.string.side_theme, R.mipmap.side_theme));//主题
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评

    }

    @Override
    public void loadAirAnimator(TranslateAnimation translate) {
        main_air_all.startAnimation(translate);
    }

    @Override
    public void setRotateGone() {
        main_rotate_all.setVisibility(View.GONE);
        handler.removeCallbacks(runnable_rotate);
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
        if (PreData.getDB(MainActivity.this, Constant.BILL_YOUXIAO, true)) {
            return;
        }
        if (PreData.getDB(this, Constant.FULL_MAIN, 0) == 1) {

        } else {
            View nativeView = AdUtil.getNativeAdView(this, TAG_MAIN, R.layout.native_ad_2);
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
            View nativeView_side = AdUtil.getNativeAdView(this, TAG_SIDE, R.layout.native_ad_2);
            if (ll_ad_side != null && nativeView_side != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad_side.getLayoutParams();
                layout_ad.height = nativeView_side.getMeasuredHeight();
                ll_ad_side.setLayoutParams(layout_ad);
                ll_ad_side.addView(nativeView_side);
            }

        }
        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            if (!PreData.getDB(MainActivity.this, Constant.BILL_YOUXIAO, true)) {
                AndroidSdk.showFullAd(TAG_LOAD_FULL);
            }
        } else {
            if (TextUtils.equals(from, "translate") || isSave) {
                return;
            }
            View nativeView_full = AdUtil.getNativeAdViewV(this, TAG_START_FULL, R.layout.native_ad_full_main);
            if (ll_ad_full != null && nativeView_full != null) {
                ll_ad_full.addView(nativeView_full);
                ll_ad_full.setVisibility(View.VISIBLE);
                final LoadTime ad_loading = (LoadTime) nativeView_full.findViewById(R.id.ad_loading);
                LinearLayout loading_text = (LinearLayout) nativeView_full.findViewById(R.id.loading_text);
                loading_text.setOnClickListener(null);
                ad_loading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad_loading.cancle();
                        handler.removeCallbacks(fullAdRunnale);
                        adDelete();
                    }
                });
                ad_loading.startProgress();
                ad_loading.setCustomRoundListener(new LoadTime.CustomRoundListener() {
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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!PreData.getDB(MainActivity.this, Constant.BILL_YOUXIAO, true)) {
                        AndroidSdk.showFullAd(AdUtil.DEFAULT);
                    }
                    main_pull_refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }, 500);
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


    private boolean load;
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
                    mainPresenter.jumpToActivity(CoolingActivity.class, bundle, 1);
                    break;
                case R.id.main_sd_air_button:
                    AdUtil.track("主页面", "点击sd球进入垃圾清理页面", "", 1);
                    mainPresenter.jumpToActivity(JunkActivity.class, 1);
                    break;
                case R.id.main_ram_air_button:
                    AdUtil.track("主页面", "点击ram球进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_air_all:
                    AdUtil.track("主页面", "点击火箭进入清理所有界面", "", 1);
                    mainPresenter.jumpToActivity(JunkAndRamActivity.class, 1);
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(JunkActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    AdUtil.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(ManagerActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    AdUtil.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from", "main");
                    bundle1.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(CoolingActivity.class, bundle1, 1);
                    break;
                case R.id.main_applock_button:
                    AdUtil.track("applock", "主界面点击", "", 1);
                    int type = PreData.getDB(MainActivity.this, Constant.FIRST_APPLOCK, 0);
                    if (!TextUtils.equals(SecurityMyPref.getPasswd(), "")) {
                        Intent intent = new Intent(MainActivity.this, AppLockPatternEosActivity.class);
                        intent.putExtra("is_main", true);
                        startActivity(intent);
                        break;
                    }
                    if (type == 0) {
                        if (LoadManager.getInstance(MainActivity.this).isPkgInstalled("com.eosmobi.applock")) {
                            Util.doStartApplicationWithPackageName(MainActivity.this, "com.eosmobi.applock");
                            PreData.putDB(MainActivity.this, Constant.FIRST_APPLOCK, 1);
                        } else {
                            Intent intent = new Intent(MainActivity.this, ApplockActivity.class);
                            startActivity(intent);
                        }
                    } else if (type == 1) {
                        if (LoadManager.getInstance(MainActivity.this).isPkgInstalled("com.eosmobi.applock")) {
                            Util.doStartApplicationWithPackageName(MainActivity.this, "com.eosmobi.applock");
                        } else {
                            Intent intent = new Intent(MainActivity.this, ApplockActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(MainActivity.this, AppLockPatternEosActivity.class);
                        intent.putExtra("is_main", true);
                        startActivity(intent);
                    }
                    break;
                case R.id.main_theme_button:
                    AdUtil.track("主页面", "点击进入buton工具箱", "", 1);
                    mainPresenter.jumpToActivity(ToolActivity.class, 1);
                    break;
                case R.id.lot_family:
                    AdUtil.track("主页面", "点击广告礼包", "", 1);
//                    ShopMaster.launch(MainActivity.this, "EOS_Family",
//                            new Theme(R.raw.battery_0, getPackageName())
//                    );
                    if (PreData.getDB(MainActivity.this, Constant.FULL_START, 0) == 1) {

                        animatorSet = new AnimatorSet();
                        load_2.setScaleX(1);
                        load_2.setScaleY(1);
                        load_2.setTranslationX(0);
                        ObjectAnimator animator_1 = ObjectAnimator.ofFloat(load_2, View.SCALE_Y, 1, 1.2f, 1);
                        animator_1.setRepeatCount(3);
                        animator_1.setDuration(1000);
                        ObjectAnimator animator_2 = ObjectAnimator.ofFloat(load_2, View.SCALE_X, 1, 1.2f, 1);
                        animator_2.setRepeatCount(3);
                        animator_2.setDuration(1000);
                        ObjectAnimator animator_3 = ObjectAnimator.ofFloat(load_2, View.TRANSLATION_X, 0, -getResources().getDimensionPixelSize(R.dimen.d5), 0);
                        animator_3.setRepeatCount(3);
                        animator_3.setDuration(1000);
                        animatorSet.play(animator_1).with(animator_2).with(animator_3);
//                        animatorSet.setInterpolator(new DecelerateInterpolator());
                        animatorSet.start();
                        load_loading.setVisibility(View.VISIBLE);
                        load = false;
                        AndroidSdk.loadFullAd(TAG_LOAD_FULL, new AdListener() {
                            @Override
                            public void onAdLoadSuccess() {
                                super.onAdLoadSuccess();
                                load = true;
                            }

                            @Override
                            public void onAdLoadFails() {
                                super.onAdLoadFails();
                                load = false;
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                load_loading.setVisibility(View.GONE);
                            }
                        });
                        handler.postDelayed(runnable_load, 4500);
                    } else {
                        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.tran_left_in);
                        ll_ad_full.startAnimation(animation);
                        ll_ad_full.setVisibility(View.VISIBLE);
                        ad_progressbar.setVisibility(View.VISIBLE);
//                    if (ad_snow_view != null) {
//                        ad_snow_view.playAni();
//                    }
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
//                                    if (ad_snow_view != null) {
////                                        ad_snow_view.cancelAni();
//                                    }
                                        ad_progressbar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onNativeAdLoadFails() {
                                        showToast(getString(R.string.load_fails));
                                        adDelete();
//                                    if (ad_snow_view != null) {
////                                        ad_snow_view.cancelAni();
//                                    }
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
                case R.id.rotate_delete:
                    AdUtil.track("主页面", "点击好评bad按钮", "", 1);
                    mainPresenter.deleteRotate();
                    break;
                case R.id.main_msg_button:
                    AdUtil.track("主页面", "点击进入硬件信息", "", 1);
                    mainPresenter.jumpToActivity(MessageActivity.class, 1);
                    break;
                case R.id.main_tuiguang_button:
                case R.id.fl_lot_side:
                    if (LoadManager.getInstance(MainActivity.this).isPkgInstalled(tuiguang)) {
                        Util.doStartApplicationWithPackageName(getApplicationContext(), tuiguang);
                        AdUtil.track("主页面", "启动" + tuiguang, "", 1);
                    } else {
                        UtilGp.openPlayStore(getApplicationContext(), tuiguang);
                    }
                    break;
                case R.id.battery_button:
                    main_battery.setVisibility(View.GONE);
                    Utils.writeData(MainActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                    initSideData();
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.battery_cha:
                    main_battery.setVisibility(View.GONE);
                    break;
                case R.id.bill:
                    showBillDialog();
                    break;
                default:
                    break;

            }
        }
    };

    private void showBillDialog() {
        bill_id = 1;
        View view = View.inflate(this, R.layout.layout_bill, null);
        final ImageView bill_week_check = (ImageView) view.findViewById(R.id.bill_week_check);
        final ImageView bill_month_check = (ImageView) view.findViewById(R.id.bill_month_check);
        final ImageView bill_year_check = (ImageView) view.findViewById(R.id.bill_year_check);
        ImageView bill_cha = (ImageView) view.findViewById(R.id.bill_cha);
        TextView bill_week_name = (TextView) view.findViewById(R.id.bill_week_name);
        TextView bill_month_name = (TextView) view.findViewById(R.id.bill_month_name);
        TextView bill_year_name = (TextView) view.findViewById(R.id.bill_year_name);
        Button bill_button = (Button) view.findViewById(R.id.bill_button);
        try {
            JSONObject jsonObject = new JSONObject(AndroidSdk.getExtraData());
            String bill_week = jsonObject.getString("bill_week");
            bill_week_name.setText("$" + bill_week + getString(R.string.bill_week));
            String bill_month = jsonObject.getString("bill_month");
            bill_month_name.setText("$" + bill_month + getString(R.string.bill_month));
            String bill_year = jsonObject.getString("bill_year");
            bill_year_name.setText("$" + bill_year + getString(R.string.bill_year));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        View.OnClickListener bill_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bill_week_check:
                        bill_id = 1;
                        bill_week_check.setImageResource(R.mipmap.bill_p);
                        bill_month_check.setImageResource(R.mipmap.bill_n);
                        bill_year_check.setImageResource(R.mipmap.bill_n);
                        break;
                    case R.id.bill_month_check:
                        bill_id = 2;
                        bill_week_check.setImageResource(R.mipmap.bill_n);
                        bill_month_check.setImageResource(R.mipmap.bill_p);
                        bill_year_check.setImageResource(R.mipmap.bill_n);
                        break;
                    case R.id.bill_year_check:
                        bill_id = 3;
                        bill_week_check.setImageResource(R.mipmap.bill_n);
                        bill_month_check.setImageResource(R.mipmap.bill_n);
                        bill_year_check.setImageResource(R.mipmap.bill_p);
                        break;
                    case R.id.bill_button:
                        AndroidSdk.pay(bill_id, new PaymentSystemListener() {
                            @Override
                            public void onPaymentSuccess(int i) {
                                PreData.putDB(MainActivity.this, Constant.BILL_YOUXIAO, true);
                                updateAd();
                            }

                            @Override
                            public void onPaymentFail(int i) {
                            }

                            @Override
                            public void onPaymentSystemValid() {
                            }

                            @Override
                            public void onPaymentCanceled(int i) {
                            }
                        });
                        bill_dialog.dismiss();
                        break;
                    case R.id.bill_cha:
                        bill_dialog.dismiss();
                        break;
                }
            }
        };
        bill_week_check.setOnClickListener(bill_click);
        bill_month_check.setOnClickListener(bill_click);
        bill_year_check.setOnClickListener(bill_click);
        bill_button.setOnClickListener(bill_click);
        bill_cha.setOnClickListener(bill_click);
        bill_dialog = new AlertDialog.Builder(this, R.style.exit_dialog).create();
        bill_dialog.setCanceledOnTouchOutside(false);
        bill_dialog.setView(view);
        bill_dialog.show();

    }

    public void updateAd() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        bill.setVisibility(View.GONE);
        lot_family.setVisibility(View.GONE);
        ll_ad_side.setVisibility(View.GONE);
        ll_ad.setVisibility(View.GONE);
        if (viewpager_3 != null && arrayList.contains(viewpager_3)) {
            arrayList.remove(viewpager_3);
            viewpager.removeView(viewpager_3);
        }
        if (viewpager_2 != null && arrayList.contains(viewpager_2)) {
            arrayList.remove(viewpager_2);
            viewpager.removeView(viewpager_2);
        }
        pageindicatorview.setViewPager(viewpager);
        pagerAdapter.notifyDataSetChanged();
    }

    Runnable runnable_load = new Runnable() {
        @Override
        public void run() {
//            load广告
            if (load) {
                AndroidSdk.showFullAd(TAG_LOAD_FULL);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        load_loading.setVisibility(View.GONE);
                    }
                }, 500);
            } else {
                if (animatorSet != null) {
                    animatorSet.removeAllListeners();
                    animatorSet.cancel();
                }
                if (colorAnim != null) {
                    colorAnim.cancel();
                }
                load_loading.setVisibility(View.GONE);
            }
            if (colorAnim != null) {
                colorAnim.cancel();
            }
        }
    };
    private ValueAnimator colorAnim;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.SETTING_RESUIL) {
            recreate();
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
            if (viewpager_3 != null && arrayList.contains(viewpager_3)) {
                arrayList.remove(viewpager_3);
                viewpager.removeView(viewpager_3);
                if (addTapTuiguang()) {
                    deep.setVisibility(View.INVISIBLE);
                    lot_tap.setVisibility(View.VISIBLE);
                    arrayList.add(viewpager_3);
                }
                pageindicatorview.setViewPager(viewpager);
                pagerAdapter.notifyDataSetChanged();
            }

        } else if (requestCode == Constant.LANGUAGE_RESUIL) {
            recreate();
        }
        initSideData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (animationDrawable != null) {
            animationDrawable.start();
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
        if (lot_main != null) {
            lot_main.playAnimation();
        }
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1 && !PreData.getDB(MainActivity.this, Constant.BILL_YOUXIAO, true)) {
            AndroidSdk.loadFullAd("eos_exit_full", null);
        }
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Util.dp2px(115), Util.dp2px(130));
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
        main_guard_rotate.startAnimation(rotateAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lot_main != null) {
            lot_main.clearAnimation();
        }
        if (lot_side != null) {
            lot_side.clearAnimation();
        }
//        if (ad_snow_view != null) {
//            ad_snow_view = null;
//        }
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
        if (colorAnim != null) {
            colorAnim.cancel();
        }
        if (animationDrawable != null) {
            if (animationDrawable.isRunning()) {
                animationDrawable.stop();
            }
        }
        if (handler != null) {
            handler.removeCallbacks(runnable_rotate);
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
            return;
        }
        if (load_loading.getVisibility() == View.VISIBLE) {
            load_loading.setVisibility(View.GONE);
            if (colorAnim != null) {
                colorAnim.cancel();
            }
            if (animatorSet != null) {
                animatorSet.removeAllListeners();
                animatorSet.cancel();
            }
            handler.removeCallbacks(runnable_load);
            return;
        }
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {

            if (!PreData.getDB(MainActivity.this, Constant.BILL_YOUXIAO, true)) {
                if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
                    AndroidSdk.showFullAd("eos_exit_full");
                    AndroidSdk.onQuit();
                } else {
                    showExitDialog();
                }
            } else {
                showExitDialog();
            }

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
        if (PreData.getDB(this, Constant.NATIVE_EXIT, 0) == 1) {
            nativeExit = AdUtil.getNativeAdViewV(this, TAG_EXIT_FULL, R.layout.native_ad_2);
            if (nativeExit != null) {
                ll_ad_exit.addView(nativeExit);
                ll_ad_exit.setVisibility(View.INVISIBLE);
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
        if (ll_ad_exit.getVisibility() == View.INVISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(ll_ad_exit, View.TRANSLATION_Y, -getResources().getDimensionPixelOffset(R.dimen.d280), 0);
            animator.setDuration(600);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();
            ll_ad_exit.setVisibility(View.VISIBLE);
        }
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = dm.widthPixels; //设置宽度
//        lp.height = dm.heightPixels; //设置高度
//        if (PreData.getDB(this, Constant.IS_ACTION_BAR, true)) {
//            int uiOptions =
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                            //布局位于状态栏下方
//                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                            //隐藏导航栏
//                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//            if (Build.VERSION.SDK_INT >= 19) {
//                uiOptions |= 0x00001000;
//            } else {
//                uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
//            }
//            dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
//        }
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setContentView(view);
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
        if (PreData.getDB(this, Constant.HONG_RAM, true) || PreData.getDB(this, Constant.HONG_JUNK, true) ||
                PreData.getDB(this, Constant.HONG_MANAGER, true)) {
            menu_hong.setVisibility(View.VISIBLE);
        } else {
            menu_hong.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
