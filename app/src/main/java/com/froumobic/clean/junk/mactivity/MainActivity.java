package com.froumobic.clean.junk.mactivity;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.entity.JunkInfo;
import com.android.clean.util.LoadManager;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.android.ui.demo.UiManager;
import com.android.ui.demo.cross.Builder;
import com.android.ui.demo.cross.CrossView;
import com.android.ui.demo.entry.CrossItem;
import com.android.ui.demo.util.JsonParser;
import com.froumobic.clean.junk.R;
import com.froumobic.clean.junk.adapter.MSideAdapter;
import com.froumobic.clean.junk.entity.SideInfo;
import com.froumobic.clean.junk.mview.CustomRoundCpu;
import com.froumobic.clean.junk.mview.ListViewForScrollView;
import com.froumobic.clean.junk.mview.MyScrollView;
import com.froumobic.clean.junk.mview.PullToRefreshLayout;
import com.froumobic.clean.junk.mview.RoundJindu;
import com.froumobic.clean.junk.mview.RoundJinduRam;
import com.froumobic.clean.junk.presenter.MainPresenter;
import com.froumobic.clean.junk.util.AdUtil;
import com.froumobic.clean.junk.util.Constant;
import com.froumobic.clean.junk.view.MainView;
import com.froumobic.module.charge.saver.Util.Constants;
import com.froumobic.module.charge.saver.Util.Utils;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MBaseActivity implements MainView, DrawerLayout.DrawerListener {

    public static final String TAG = "MainActivity";
    MyScrollView main_scroll_view;
    PullToRefreshLayout main_pull_refresh;
    ImageView iv_title_right;
    ImageView iv_title_left;
    TextView main_fenshu;
    CustomRoundCpu main_fenshu_round;
    RelativeLayout main_all_cercle;
    RelativeLayout main_title;
    RelativeLayout main_junk_button, main_ram_button, main_cooling_button;
    ImageView main_button_tuiguang_1_icon, main_button_tuiguang_2_icon;
    TextView main_button_tuiguang_1_lable, main_button_tuiguang_2_lable;
    LinearLayout main_manager_button, main_button_tuiguang_1, main_button_tuiguang_2;
    LinearLayout main_junk_text, main_junk_image;
    LinearLayout main_ram_text, main_ram_image;
    LinearLayout main_cooling_text, main_cooling_image;
    TextView main_junk_h, main_ram_h, main_cooling_h;
    LinearLayout main_rotate_all;
    FrameLayout main_rotate_good;
    FrameLayout main_rotate_bad;
    LinearLayout main_msg_button;
    LinearLayout main_power_button;
    LinearLayout main_notifi_button;
    LinearLayout main_file_button;
    LinearLayout main_gboost_button;
    LinearLayout main_picture_button;
    TextView main_msg_ram_percent, main_msg_sd_percent, main_msg_sd_unit, main_msg_cpu_percent;
    ListViewForScrollView side_listView;
    DrawerLayout main_drawer;
    LinearLayout ll_ad, ll_ad_side;
    ListView mopub_ad;
    com.mingle.widget.LinearLayout ll_ad_full;
    LinearLayout ll_ad_loading;
    LinearLayout ad_native_2;
    ProgressBar ad_progressbar;
    RelativeLayout main_qiu;
    ImageView main_red;
    RoundJindu main_msg_sd_backg;
    RoundJinduRam main_msg_ram_backg;
    FrameLayout main_battery;
    LinearLayout main_tuiguang;

    // LottieAnimationView lot_side;
    ImageView side_title;
    ImageView lot_family;
    PagerAdapter pagerAdapter;

    private String TAG_MAIN = "main";
    private String TAG_HUA = "hua";
    private String TAG_SIDE = "side";
    private String TAG_START_FULL = "start_native";
    private String TAG_EXIT_FULL = "exit_native";
    private String TAG_FULL_PULL = "pull_full";

    private Handler handler = new Handler();
    private MainPresenter mainPresenter;
    private MSideAdapter adapter;
    private int temp;
    private ViewPager viewpager;
    PageIndicatorView pageindicatorview;

    private View viewpager_3;
    private AlertDialog dialog;
    private ArrayList<View> arrayList;


    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        main_all_cercle = (RelativeLayout) findViewById(R.id.main_all_cercle);
        main_title = (RelativeLayout) findViewById(R.id.main_title);
        main_scroll_view = (MyScrollView) findViewById(R.id.main_scroll_view);
        main_pull_refresh = (PullToRefreshLayout) findViewById(R.id.main_pull_refresh);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);
        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_junk_text = (LinearLayout) findViewById(R.id.main_junk_text);
        main_junk_image = (LinearLayout) findViewById(R.id.main_junk_image);
        main_ram_text = (LinearLayout) findViewById(R.id.main_ram_text);
        main_ram_image = (LinearLayout) findViewById(R.id.main_ram_image);
        main_cooling_text = (LinearLayout) findViewById(R.id.main_cooling_text);
        main_cooling_image = (LinearLayout) findViewById(R.id.main_cooling_image);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_manager_button = (LinearLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_button_tuiguang_1 = (LinearLayout) findViewById(R.id.main_button_tuiguang_1);
        main_button_tuiguang_2 = (LinearLayout) findViewById(R.id.main_button_tuiguang_2);
        main_button_tuiguang_1_icon = (ImageView) findViewById(R.id.main_button_tuiguang_1_icon);
        main_button_tuiguang_1_lable = (TextView) findViewById(R.id.main_button_tuiguang_1_lable);
        main_button_tuiguang_2_icon = (ImageView) findViewById(R.id.main_button_tuiguang_2_icon);
        main_button_tuiguang_2_lable = (TextView) findViewById(R.id.main_button_tuiguang_2_lable);
        main_junk_h = (TextView) findViewById(R.id.main_junk_h);
        main_ram_h = (TextView) findViewById(R.id.main_ram_h);
        main_cooling_h = (TextView) findViewById(R.id.main_cooling_h);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_good = (FrameLayout) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (FrameLayout) findViewById(R.id.main_rotate_bad);
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
        side_listView = (ListViewForScrollView) findViewById(R.id.side_listView);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        mopub_ad = (ListView) findViewById(R.id.mopub_ad);
        ll_ad_side = (LinearLayout) findViewById(R.id.ll_ad_side);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ll_ad_loading = (LinearLayout) findViewById(R.id.ll_ad_loading);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        side_title = (ImageView) findViewById(R.id.side_title);
        lot_family = (ImageView) findViewById(R.id.lot_family);
        main_red = (ImageView) findViewById(R.id.main_red);
        main_msg_sd_backg = (RoundJindu) findViewById(R.id.main_msg_sd_backg);
        main_msg_ram_backg = (RoundJinduRam) findViewById(R.id.main_msg_ram_backg);
        main_battery = (FrameLayout) findViewById(R.id.main_battery);
        main_tuiguang = (LinearLayout) findViewById(R.id.main_tuiguang);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        if (PreData.getDB(this, Constant.SIDE_ROTATE, false) && PreData.getDB(this, Constant.SIDE_DEEP, false)
                && PreData.getDB(this, Constant.SIDE_NOTIFI, false)) {
            main_red.setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }

        arrayList = new ArrayList<>();
        View view = LayoutInflater.from(this).inflate(R.layout.main_circle, null);
        main_fenshu = (TextView) view.findViewById(R.id.main_fenshu);
        main_fenshu_round = (CustomRoundCpu) view.findViewById(R.id.main_fenshu_round);
        main_qiu = (RelativeLayout) view.findViewById(R.id.main_qiu);
        arrayList.add(view);

        View viewpager_2 = LayoutInflater.from(this).inflate(R.layout.main_ad, null);
        LinearLayout view_ad = (LinearLayout) viewpager_2.findViewById(R.id.view_ad);
        View adView = AdUtil.getNativeAdView(TAG_HUA, R.layout.native_ad_2);
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
        LinearLayout tap_ll = (LinearLayout) viewpager_3.findViewById(R.id.tap_ll);
        ImageView tap_iv_1 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_1);
        ImageView tap_iv_2 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_2);
        ImageView tap_iv_3 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_3);
        ImageView tap_iv_4 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_4);
        ImageView tap_iv_5 = (ImageView) viewpager_3.findViewById(R.id.tap_iv_5);
        TextView tap_iv_6 = (TextView) viewpager_3.findViewById(R.id.tap_iv_6);
        TextView tap_deep_text = (TextView) viewpager_3.findViewById(R.id.tap_deep_text);
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
                    mainPresenter.jumpToActivity(DeepActivity.class, 1);
                }
            });
            if (startList.size() == 0) {
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

//            public void removeItem() {
//                arrayList.remove(arrayList.size() - 1);
//                notifyDataSetChanged();
//            }

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
        }, 4000);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewpager.setCurrentItem(1);
            }
        }, 5000);
//        }
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);

        AdUtil.track("主页面", "进入主页面", "", 1);

        if (PreData.getDB(this, Constant.FIRST_BATTERY, true)) {
            PreData.putDB(this, Constant.FIRST_BATTERY, false);
            main_battery.setVisibility(View.VISIBLE);
            ImageView battery_cha = (ImageView) findViewById(R.id.battery_cha);
            Button battery_button = (Button) findViewById(R.id.battery_button);
            battery_cha.setOnClickListener(onClickListener);
            battery_button.setOnClickListener(onClickListener);
        }
        tuiguang(TUIGUAN_MAIN_SOFT, true, main_tuiguang);
        tuiguang(TUIGUAN_MAIN, false, main_tuiguang);
        tuiguang(TUIGUAN_SIDE_SOFT, true, side_listView);
        tuiguang(TUIGUAN_SIDE, false, side_listView);
        tuiguangButton(TUIGUAN_TAB);
        initSideData();
    }

    private void tuiguangButton(String tag) {
        final ArrayList<CrossItem> crossItems = JsonParser.getCrossData(this, AndroidSdk.getExtraData(), tag);
        if (crossItems != null && crossItems.size() >= 2) {
            final int tuiguang_1 = (int) (Math.random() * crossItems.size());
            int tuiguang_2 = (int) (Math.random() * crossItems.size());
            while (tuiguang_2 == tuiguang_1) {
                tuiguang_2 = (int) (Math.random() * crossItems.size());
            }
            Util.loadImg(this, crossItems.get(tuiguang_1).getTagIconUrl(), R.mipmap.icon, main_button_tuiguang_1_icon);
            Util.loadImg(this, crossItems.get(tuiguang_2).getTagIconUrl(), R.mipmap.icon, main_button_tuiguang_2_icon);
            main_button_tuiguang_1_lable.setText(crossItems.get(tuiguang_1).getTitle());
            main_button_tuiguang_2_lable.setText(crossItems.get(tuiguang_2).getTitle());
            AdUtil.track("交叉推广_广告位", "广告位_交叉", "展示" + crossItems.get(tuiguang_1).getPkgName(), 1);
            AdUtil.track("交叉推广_广告位", "广告位_交叉", "展示" + crossItems.get(tuiguang_2).getPkgName(), 1);
            main_button_tuiguang_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.android.ui.demo.util.Utils.reactionForAction(MainActivity.this, AndroidSdk.getExtraData(), crossItems.get(tuiguang_1).getPkgName(),
                            crossItems.get(tuiguang_1).getAction());
                    AdUtil.track("交叉推广_广告位", "广告位_交叉", "点击" + crossItems.get(tuiguang_1).getPkgName(), 1);
                }
            });
            final int finalTuiguang_ = tuiguang_2;
            main_button_tuiguang_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.android.ui.demo.util.Utils.reactionForAction(MainActivity.this, AndroidSdk.getExtraData(), crossItems.get(finalTuiguang_).getPkgName(),
                            crossItems.get(finalTuiguang_).getAction());
                    AdUtil.track("交叉推广_广告位", "广告位_交叉", "点击" + crossItems.get(finalTuiguang_).getPkgName(), 1);
                }
            });
        } else {
            AdUtil.track("交叉推广_广告位", "广告位_交叉", "展示" + "com.fraumobi.applock", 1);
            AdUtil.track("交叉推广_广告位", "广告位_交叉", "展示" + "com.fraumobi.galleryvault.lockphoto.hidevideo", 1);
            main_button_tuiguang_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.android.ui.demo.util.Utils.reactionForAction(MainActivity.this, AndroidSdk.getExtraData(), "com.fraumobi.applock", null);
                    AdUtil.track("交叉推广_广告位", "广告位_交叉", "点击" + "com.fraumobi.applock", 1);
                }
            });
            main_button_tuiguang_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    com.android.ui.demo.util.Utils.reactionForAction(MainActivity.this, AndroidSdk.getExtraData(), "com.fraumobi.galleryvault.lockphoto.hidevideo", null);
                    AdUtil.track("交叉推广_广告位", "广告位_交叉", "点击" + "com.fraumobi.galleryvault.lockphoto.hidevideo", 1);
                }
            });
        }
    }


    @Override
    public void startFenshu(final int percent, final boolean isRestart) {
        if (isRestart) {
            main_fenshu_round.reStartProgress(percent);
        } else {
            main_fenshu_round.startProgress(percent);
        }
        main_fenshu_round.setCustomRoundListener(new CustomRoundCpu.CustomRoundListener() {
            @Override
            public void progressUpdate(int progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main_fenshu.setText(String.valueOf(percent));
                    }
                });
            }
        });
        handler.post(new Runnable() {
            @Override
            public void run() {
                setColor(percent, isRestart);
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

    private void setColor(int percent, boolean isReStart) {
        int color = ContextCompat.getColor(this, R.color.A1);
        Drawable background = main_all_cercle.getBackground();
        if (background instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) background;
            color = colordDrawable.getColor();
        }
        if (percent < 40) {
            if (!isReStart) {
                setColorAnimation(main_all_cercle, ContextCompat.getColor(this, R.color.A1), ContextCompat.getColor(this, R.color.A2));
                setColorAnimation(view_title_bar, ContextCompat.getColor(this, R.color.A1), ContextCompat.getColor(this, R.color.A2));
                setColorAnimation(main_title, ContextCompat.getColor(this, R.color.A1), ContextCompat.getColor(this, R.color.A2));
            }
        } else if (percent < 80) {
            if (isReStart) {
                setColorAnimation(main_all_cercle, color, ContextCompat.getColor(this, R.color.A4));
                setColorAnimation(view_title_bar, color, ContextCompat.getColor(this, R.color.A4));
                setColorAnimation(main_title, color, ContextCompat.getColor(this, R.color.A4));
            } else {
                setColorAnimation(main_all_cercle, ContextCompat.getColor(this, R.color.A1), ContextCompat.getColor(this, R.color.A4));
                setColorAnimation(view_title_bar, ContextCompat.getColor(this, R.color.A1), ContextCompat.getColor(this, R.color.A4));
                setColorAnimation(main_title, ContextCompat.getColor(this, R.color.A1), ContextCompat.getColor(this, R.color.A4));
            }
        } else {
            if (isReStart) {
                setColorAnimation(main_all_cercle, color, ContextCompat.getColor(this, R.color.A1));
                setColorAnimation(view_title_bar, color, ContextCompat.getColor(this, R.color.A1));
                setColorAnimation(main_title, color, ContextCompat.getColor(this, R.color.A1));
            }
        }
    }

    //初始化监听
    public void onClick() {
        //main_scroll_view.setOnTouchListener(scrollViewTouchListener);
        main_pull_refresh.setOnRefreshListener(refreshListener);
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_msg_button.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        lot_family.setOnClickListener(onClickListener);
        main_qiu.setOnClickListener(onClickListener);

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
    public void initSd(final int percent, final long sd_kongxian) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main_msg_sd_backg.setProgress(percent);
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
                        main_junk_h.setVisibility(View.VISIBLE);
                        toggleEditAnimation(R.id.main_junk_image, R.id.main_junk_text);
                    }
                }
            }
        });

    }

    @Override
    public void initRam(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main_msg_ram_backg.setProgress(percent);
                main_msg_ram_percent.setText(percent + "%");
                if (main_ram_h.getVisibility() == View.INVISIBLE) {
                    long ram_size = CleanManager.getInstance(MainActivity.this).getRamSize();
                    if (ram_size > 0) {
                        main_ram_h.setText(Util.convertStorage(ram_size, true));
                        main_ram_h.setVisibility(View.VISIBLE);
                        toggleEditAnimation(R.id.main_ram_image, R.id.main_ram_text);
                    }
                }
            }
        });

    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
        main_msg_cpu_percent.setText(String.valueOf(temp) + "℃");
        if (main_cooling_h.getVisibility() == View.INVISIBLE) {
            main_cooling_h.setText(String.valueOf(temp) + "℃");
            main_cooling_h.setVisibility(View.VISIBLE);
            toggleEditAnimation(R.id.main_cooling_image, R.id.main_cooling_text);
        }

    }

    public void initSideData() {
        if (adapter == null) {
            adapter = new MSideAdapter(this);
            side_listView.setAdapter(adapter);
        }
        adapter.clear();
        adapter.addData(new SideInfo(R.string.side_float, R.mipmap.side_float, PreData.getDB(this, Constant.FlOAT_SWITCH, true)));//桌面悬浮球
        adapter.addData(new SideInfo(R.string.side_charging, R.mipmap.side_charging, (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, false)));//充电屏保
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
        adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
        adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.gboost_side));//游戏加速
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评

    }

    @Override
    public void loadAirAnimator(TranslateAnimation translate) {
    }

    @Override
    public void setRotateGone() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                main_rotate_all.setVisibility(View.GONE);
            }
        });

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
            int a = (int) (1 + Math.random() * (2)); //从1到10的int型随数
            if (true) {

                View nativeView = AdUtil.getNativeAdView(TAG_MAIN, R.layout.native_ad_2);
                if (ll_ad != null && nativeView != null) {
                    ll_ad.removeAllViews();
                    ll_ad.addView(nativeView);
                    ll_ad.setGravity(Gravity.CENTER_HORIZONTAL);
                    main_scroll_view.setScrollY(0);
//                main_scroll_view.fullScroll(ScrollView.FOCUS_UP);
                } else {
                    ll_ad.setVisibility(View.GONE);
                }
            } else {
                try {
                    Log.e("cross", "0" + "-==");
                    UiManager.getCrossView(this, new Builder("cross")
                                    .setServiceData(AndroidSdk.getExtraData())
                                    .setType(Builder.Type.TYPE_SQUARE_272).setAdTagImageId(R.mipmap.ad)
                                    .setIsShouldShowDownLoadBtn(true)
                                    .setRootViewBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.white_100))
                                    .setActionBtnBackground(R.drawable.select_text_ad)
                                    .setActionTextColor(getResources().getColor(R.color.white_100))
                                    .setTitleTextColor(getResources().getColor(R.color.B2))
                                    .setSubTitleTextColor(getResources().getColor(R.color.B3))
                                    .setTrackTag("广告位_好评")
                            , new CrossView.OnDataFinishListener() {
                                @Override
                                public void onFinish(CrossView crossView) {
                                    ll_ad.removeAllViews();
                                    ll_ad.addView(crossView);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                    ll_ad.setVisibility(View.GONE);
                }
            }


            View nativeView_side = AdUtil.getNativeAdView(TAG_SIDE, R.layout.native_ad_2);
            if (false) {
                ll_ad_side.removeAllViews();
                ll_ad_side.addView(nativeView_side);
            } else {
                try {
                    UiManager.getCrossView(this, new Builder("cross")
                                    .setServiceData(AndroidSdk.getExtraData())
                                    .setType(Builder.Type.TYPE_SQUARE_193)
                                    .setIsShouldShowDownLoadBtn(true).setAdTagImageId(R.mipmap.ad)
                                    .setActionBtnBackground(R.drawable.select_text_ad)
                                    .setActionTextColor(getResources().getColor(R.color.white_100))
                                    .setTitleTextColor(getResources().getColor(R.color.B2))
                                    .setTrackTag("广告位_侧边栏")
                                    .setSubTitleTextColor(getResources().getColor(R.color.B3))
                            , new CrossView.OnDataFinishListener() {
                                @Override
                                public void onFinish(CrossView crossView) {
                                    ll_ad_side.removeAllViews();
                                    ll_ad_side.addView(crossView);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.showFullAd("start_full");
        } else {
            int a = (int) (1 + Math.random() * (2)); //从1到10的int型随数
            if (true) {
                View nativeView_full = AdUtil.getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_exit);
                if (ll_ad_loading != null && nativeView_full != null) {
                    ll_ad_loading.removeAllViews();
                    ll_ad_loading.addView(nativeView_full);
                    ll_ad_full.setVisibility(View.VISIBLE);
                    ImageView ad_delete = (ImageView) findViewById(R.id.ad_delete);
                    ad_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handler.removeCallbacks(fullAdRunnale);
                            adDelete();
                        }
                    });
                    int skip = PreData.getDB(this, Constant.SKIP_TIME, 6);
                    Log.e("timead", skip + "-==");
                    handler.postDelayed(fullAdRunnale, skip * 1000);
                }
            } else {
                try {
                    UiManager.getCrossView(this, new Builder("cross")
                                    .setServiceData(AndroidSdk.getExtraData())
                                    .setType(Builder.Type.TYPE_DIALOG).setAdTagImageId(R.mipmap.ad)
                                    .setIsShouldShowDownLoadBtn(true)
                                    .setRootViewBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.A1))
                                    .setActionBtnBackground(R.drawable.select_text_ad)
                                    .setActionTextColor(getResources().getColor(R.color.white_100))
                                    .setTitleTextColor(getResources().getColor(R.color.white_100))
                                    .setSubTitleTextColor(getResources().getColor(R.color.white_100))
                                    .setTrackTag("广告位_loading")
                            , new CrossView.OnDataFinishListener() {
                                @Override
                                public void onFinish(CrossView crossView) {
                                    ll_ad_loading.removeAllViews();
                                    ll_ad_loading.addView(crossView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                    ImageView ad_delete = (ImageView) findViewById(R.id.ad_delete);
                                    ad_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            handler.removeCallbacks(fullAdRunnale);
                                            adDelete();
                                        }
                                    });
                                    ll_ad_full.setVisibility(View.VISIBLE);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int skip = PreData.getDB(this, Constant.SKIP_TIME, 6);
                Log.e("timead", skip + "-==");
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
                case R.id.main_qiu:
                    AdUtil.track("主页面", "点击进入清理所有界面", "", 1);
                    mainPresenter.jumpToActivity(LajjiAndRamActivity.class, 1);
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(LajiActivity.class, 1);
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("主页面", "点击ram清理按钮", "", 1);
                    mainPresenter.jumpToActivity(RamAvtivity.class, 1);
                    break;
                case R.id.main_manager_button:
                    AdUtil.track("主页面", "点击应用管理按钮", "", 1);
                    mainPresenter.jumpToActivity(AppActivity.class, 1);
                    break;
                case R.id.main_cooling_button:
                    AdUtil.track("主页面", "点击降温按钮", "", 1);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("from", "main");
                    bundle1.putInt("wendu", temp);
                    mainPresenter.jumpToActivity(CoolingActivity.class, bundle1, 1);
                    break;
                case R.id.lot_family:
                    AdUtil.track("主页面", "点击广告礼包", "", 1);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.tran_left_in);
                    ll_ad_full.startAnimation(animation);
                    ll_ad_full.setVisibility(View.VISIBLE);
                    ad_progressbar.setVisibility(View.VISIBLE);
                    ll_ad_loading.removeAllViews();
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            AndroidSdk.loadNativeAd(TAG_START_FULL, R.layout.native_ad_full_exit, new ClientNativeAd.NativeAdLoadListener() {
                                @Override
                                public void onNativeAdLoadSuccess(View view) {
                                    ImageView ad_delete = (ImageView) findViewById(R.id.ad_delete);
                                    ad_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            adDelete();
                                        }
                                    });
                                    ll_ad_loading.addView(view);
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
                    mainPresenter.jumpToActivity(MessageActivity.class, 1);
                    break;
                case R.id.main_power_button:
                    AdUtil.track("主页面", "点击进入深度清理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.DEEP_CLEAN, true);
                    mainPresenter.jumpToActivity(DeepActivity.class, 1);
                    break;
                case R.id.main_file_button:
                    AdUtil.track("主页面", "点击进入文件管理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.FILE_CLEAN, true);
                    mainPresenter.jumpToActivity(FileActivity.class, 1);
                    break;
                case R.id.main_gboost_button:
                    AdUtil.track("主页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(MainActivity.this, Constant.GBOOST_CLEAN, true);
                    mainPresenter.jumpToActivity(GBoostActivity.class, 1);
                    break;
                case R.id.main_picture_button:
                    AdUtil.track("主页面", "点击进入相似图片", "", 1);
                    PreData.putDB(MainActivity.this, Constant.PHOTO_CLEAN, true);
                    mainPresenter.jumpToActivity(PhotoActivity.class, 1);
                    break;
                case R.id.main_notifi_button:
                    AdUtil.track("主页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(MainActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(MainActivity.this) || !PreData.getDB(MainActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(MainActivity.this, NotifiAnimaActivity.class);
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
                    adapter.notifyDataSetChanged();
                    AdUtil.track("主界面", "充电屏保引导", "打开了", 1);
                    break;
                case R.id.battery_cha:
                    main_battery.setVisibility(View.GONE);
                    AdUtil.track("主界面", "充电屏保引导", "叉掉了", 1);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mainPresenter.reStart(true);
            }
        }).start();
        if (resultCode == Constant.SETTING_RESUIL) {
            initSideData();
            adapter.notifyDataSetChanged();
        } else if (resultCode == Constant.JUNK_RAM_RESUIL) {
            main_cooling_h.setVisibility(View.GONE);
            main_cooling_text.setVisibility(View.GONE);
            main_cooling_image.setRotationY(0);
            main_cooling_image.setVisibility(View.VISIBLE);

            main_ram_text.setVisibility(View.GONE);
            main_ram_image.setRotationY(0);
            main_ram_image.setVisibility(View.VISIBLE);
            main_ram_h.setVisibility(View.GONE);

            main_junk_text.setVisibility(View.GONE);
            main_junk_h.setVisibility(View.GONE);
            main_junk_image.setRotationY(0);
            main_junk_image.setVisibility(View.VISIBLE);
        } else if (resultCode == Constant.COOLING_RESUIL) {
            int wendu = data.getIntExtra("wendu", 0);
            temp -= wendu;
            if (temp == 0) {
                temp = 40;
            }
            main_cooling_h.setVisibility(View.GONE);
            main_cooling_text.setVisibility(View.GONE);
            main_cooling_image.setRotationY(0);
            main_cooling_image.setVisibility(View.VISIBLE);
        } else if (requestCode == 100) {
            if (Util.isNotificationListenEnabled(MainActivity.this)) {
                PreData.putDB(MainActivity.this, Constant.KEY_NOTIFI, true);
                Intent intent = new Intent(MainActivity.this, NotifiActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(MainActivity.this, NotifiAnimaActivity.class);
                startActivityForResult(intent, 1);
            }
        } else if (resultCode == Constant.RAM_RESUIL) {
            main_ram_text.setVisibility(View.GONE);
            main_ram_image.setRotationY(0);
            main_ram_image.setVisibility(View.VISIBLE);
            main_ram_h.setVisibility(View.GONE);
        } else if (resultCode == Constant.JUNK_RESUIL) {
            main_junk_text.setVisibility(View.GONE);
            main_junk_h.setVisibility(View.GONE);
            main_junk_image.setRotationY(0);
            main_junk_image.setVisibility(View.VISIBLE);
        } else if (resultCode == Constant.POWER_RESUIL) {
            if (viewpager_3 != null && arrayList.contains(viewpager_3)) {
                arrayList.remove(arrayList.size() - 1);
                viewpager.removeView(viewpager_3);
                pagerAdapter.notifyDataSetChanged();
                pageindicatorview.setViewPager(viewpager);
            }

        }
        if (PreData.getDB(this, Constant.SIDE_ROTATE, false) && PreData.getDB(this, Constant.SIDE_DEEP, false)
                && PreData.getDB(this, Constant.SIDE_NOTIFI, false)) {
            main_red.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

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
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Util.dp2px(115), Util.dp2px(130));
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        if (ll_ad_full.getVisibility() == View.VISIBLE) {
            adDelete();
            handler.removeCallbacks(fullAdRunnale);
            return;
        }
        if (main_battery.getVisibility() == View.VISIBLE) {
            AdUtil.track("主界面", "充电屏保引导", "返回键退出", 1);
            main_battery.setVisibility(View.GONE);
            return;
        }
        if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START);
        } else {
            if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
                AndroidSdk.showFullAd("exit_full");
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
        final LinearLayout ll_ad_exit = (LinearLayout) view.findViewById(R.id.ll_ad_exit);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 0) {
            int a = (int) (1 + Math.random() * (2)); //从1到10的int型随数
            if (true) {
                View nativeExit = AdUtil.getNativeAdView(TAG_EXIT_FULL, R.layout.native_ad_full_exit);
                if (nativeExit != null) {
                    ll_ad_exit.addView(nativeExit);
                    ll_ad_exit.setVisibility(View.VISIBLE);
                }
            } else {
                try {
                    UiManager.getCrossView(this, new Builder("cross")
                                    .setServiceData(AndroidSdk.getExtraData())
                                    .setType(Builder.Type.TYPE_DIALOG)
                                    .setIsShouldShowDownLoadBtn(true)
                                    .setAdTagImageId(R.mipmap.ad)
                                    .setRootViewBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.A1))
                                    .setActionBtnBackground(R.drawable.select_text_ad)
                                    .setActionTextColor(getResources().getColor(R.color.white_100))
                                    .setTitleTextColor(getResources().getColor(R.color.white_100))
                                    .setSubTitleTextColor(getResources().getColor(R.color.white_100))
                                    .setTrackTag("广告位_退出")
                            , new CrossView.OnDataFinishListener() {
                                @Override
                                public void onFinish(CrossView crossView) {
                                    ll_ad_exit.addView(crossView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                    ll_ad_exit.setVisibility(View.VISIBLE);
                                }
                            });

                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    }

    @Override
    public void onDrawerClosed(View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}