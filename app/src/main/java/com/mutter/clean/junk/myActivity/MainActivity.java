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
import android.widget.Toast;

import com.android.client.AdListener;
import com.frigate.event.IFrigateEventListener;
import com.frigate.layout.AutoDrawerLayout;
import com.frigate.parser.Command;
import com.frigate.parser.FrigateData;
import com.frigate.utils.AutoUtils;
import com.mutter.clean.core.CleanManager;
import com.mutter.clean.imageclean.ImageInfo;
import com.mutter.clean.imageclean.RecyclerDbHelper;
import com.mutter.clean.junk.entity.TuiguangInfo;
import com.mutter.clean.junk.myAdapter.JunkRamAdapter;
import com.mutter.clean.junk.myview.LoadingTime;
import com.mutter.clean.junk.myview.conpentview.AutoImageText2View;
import com.mutter.clean.junk.myview.conpentview.AutoImageTextView;
import com.mutter.clean.junk.myview.conpentview.AutoLoadFllView;
import com.mutter.clean.junk.myview.conpentview.AutoRotateXView;
import com.mutter.clean.junk.myview.conpentview.AutoRoundView;
import com.mutter.clean.junk.myview.conpentview.AutoSlideListView;
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

    ImageView menu_hong;
    AutoRoundView main_sd_round;
    AutoRoundView main_ram_round;
    public static final String TAG = "MainActivity";
    FrameLayout main_rotate_all;
    AutoSlideListView slide_listView;
    AutoImageText2View main_notifi_button;
    DrawerLayout main_drawer;
    com.mingle.widget.LinearLayout main_ad_full;
    AutoLoadFllView main_load_full;

    // LottieAnimationView lot_side;
    ImageView side_title;
    AutoRotateXView main_rotate_x;
    private LoadingTime ad_loading;


    private String TAG_MAIN = "mutter_main";
    private String TAG_HUA = "mutter_hua";
    private String TAG_SIDE = "mutter_side";
    private String TAG_START_FULL = "mutter_start_native";
    private String TAG_EXIT_FULL = "mutter_exit_native";
    private String TAG_FULL_PULL = "mutter_native";//pull_full

    private Handler handler = new Handler();
    private MainPresenter mainPresenter;
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
    private AutoDrawerLayout cv;


    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);


        side_title = (ImageView) findViewById(R.id.side_title);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        cv = (AutoDrawerLayout) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        menu_hong = (ImageView) cv.findViewWithTag("main_title_menu");
        main_sd_round = (AutoRoundView) cv.findViewWithTag("main_sd_round");
        main_ram_round = (AutoRoundView) cv.findViewWithTag("main_ram_round");
        main_rotate_x = (AutoRotateXView) cv.findViewWithTag("main_rotate_x");
        main_rotate_all = (FrameLayout) cv.findViewWithTag("main_rotate_all");
        main_notifi_button = (AutoImageText2View) cv.findViewWithTag("main_notifi_button");
        main_ad_full = (com.mingle.widget.LinearLayout) cv.findViewWithTag("main_ad_full");
        main_load_full = (AutoLoadFllView) cv.findViewWithTag("main_load_full");
        slide_listView = (AutoSlideListView) cv.findViewWithTag("slide_listView");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
            PreData.putDB(this, Constant.HONG_NOTIFI, false);
        }
        if (PreData.getDB(this, Constant.HONG_RAM, true) || PreData.getDB(this, Constant.HONG_JUNK, true) ||
                PreData.getDB(this, Constant.HONG_COOLING, true) || PreData.getDB(this, Constant.HONG_MESSAGE, true) || PreData.getDB(this, Constant.HONG_NOTIFI, true) ||
                PreData.getDB(this, Constant.HONG_FILE, true) || PreData.getDB(this, Constant.HONG_MANAGER, true) ||
                PreData.getDB(this, Constant.HONG_DEEP, true) || PreData.getDB(this, Constant.HONG_PHOTO, true) || PreData.getDB(this, Constant.HONG_GBOOST, true)) {
            if (menu_hong != null) {
                menu_hong.setVisibility(View.VISIBLE);
            }
        } else {
            if (menu_hong != null) {
                menu_hong.setVisibility(View.GONE);
            }
        }
        BadgerCount.setCount(this);


        mainPresenter = new MainPresenter(this, this, handler);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);

        AdUtil.track("主页面", "进入主页面", "", 1);
    }

    public void startSetting() {
        mainPresenter.jumpToActivity(SettingActivity.class, 1);
    }

    public void startRamAndJunk() {
        mainPresenter.jumpToActivity(CleanAndRamActivity.class, 1);
    }

    public void startJunk() {
        mainPresenter.jumpToActivity(CleanActivity.class, 1);
    }

    public void startRam() {
        mainPresenter.jumpToActivity(RamAvtivity.class, 1);
    }

    public void startCooling() {
        Bundle bundle1 = new Bundle();
        bundle1.putString("from", "main");
        bundle1.putInt("wendu", temp);
        mainPresenter.jumpToActivity(JiangwenActivity.class, bundle1, 1);
    }

    public void startManager() {
        mainPresenter.jumpToActivity(UserAppActivity.class, 1);
    }

    public void startFileManager() {
        PreData.putDB(MainActivity.this, Constant.FILE_CLEAN, true);
        mainPresenter.jumpToActivity(FileManaActivity.class, 1);
    }

    public void startGboost() {
        PreData.putDB(MainActivity.this, Constant.GBOOST_CLEAN, true);
        mainPresenter.jumpToActivity(GameActivity.class, 1);
    }

    public void startSimilar() {
        PreData.putDB(MainActivity.this, Constant.PHOTO_CLEAN, true);
        mainPresenter.jumpToActivity(SimilarActivity.class, 1);
    }

    public void startRotateDelete() {
        mainPresenter.deleteRotate();
    }

    public void startRotateGood() {
        mainPresenter.clickRotate(true);
    }

    public void startRotateBad() {
        mainPresenter.clickRotate(false);
    }

    public void startPower() {
        PreData.putDB(MainActivity.this, Constant.DEEP_CLEAN, true);
        mainPresenter.jumpToActivity(PowerActivity.class, 1);
    }

    public void startNotification() {
        PreData.putDB(MainActivity.this, Constant.NOTIFI_CLEAN, true);
        if (!Util.isNotificationListenEnabled(MainActivity.this) || !PreData.getDB(MainActivity.this, Constant.KEY_NOTIFI, true)) {
            Intent intent6 = new Intent(MainActivity.this, NotifiAnimationActivity.class);
            startActivityForResult(intent6, 1);
        } else {
            Intent intent6 = new Intent(MainActivity.this, NotifiActivity.class);
            startActivityForResult(intent6, 1);
        }
    }

    public void startFull() {
        AdUtil.track("主页面", "点击广告礼包", "", 1);
        if (PreData.getDB(MainActivity.this, Constant.FULL_START, 0) == 1) {
            main_load_full.startLoad();
        } else {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.tran_left_in);
            main_ad_full.startAnimation(animation);
            main_ad_full.setVisibility(View.VISIBLE);
            main_ad_full.removeAllViews();
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
                            main_ad_full.addView(view);
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

    }


    @Override
    public void initSd(final int percent, final String size, final long sd_kongxian) {
        main_sd_round.reFresh(percent, size);
    }

    @Override
    public void initRam(final int percent, final String size) {
        main_ram_round.reFresh(percent, size);
    }


    //初始化监听
    public void onClick() {
    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
    }


    @Override
    public void loadFullAd() {

        if (!PreData.getDB(this, Constant.BATTERY_FIRST, false)) {
            showBattery();
            PreData.putDB(this, Constant.BATTERY_FIRST, true);
        }

        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.showFullAd(LOADING_FULL);
        } else {
            // TODO: 2017/10/31
            View nativeView_full = AdUtil.getNativeAdView(TAG_START_FULL, R.layout.native_ad_full_main);
            if (main_ad_full != null && nativeView_full != null) {
                main_ad_full.addView(nativeView_full);
                main_ad_full.setVisibility(View.VISIBLE);
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
                slide_listView.initSideData();
                dialogB.dismiss();
            }
        });
        dialogB = new AlertDialog.Builder(this).create();
        dialogB.setCanceledOnTouchOutside(false);
        dialogB.setView(view);
        dialogB.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.COOLING_RESUIL) {
            int wendu = data.getIntExtra("wendu", 0);
            temp -= wendu;
            if (temp == 0) {
                temp = 40;
            }
        }
        slide_listView.initSideData();
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
        if (main_ad_full.getVisibility() == View.VISIBLE) {
            adDelete();
            handler.removeCallbacks(fullAdRunnale);
            return;
        }
        if (main_load_full.getVisibility() == View.VISIBLE) {
            main_load_full.setVisibility(View.GONE);
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
        if (main_ad_full == null) {
            return;
        }
        if (onPause || !onResume) {
            main_ad_full.setVisibility(View.GONE);
            return;
        }

        CRAnimation crA = new CircularRevealCompat(main_ad_full).circularReveal(main_rotate_x.getLeft() + main_rotate_x.getWidth() / 2,
                main_rotate_x.getTop() + main_rotate_x.getWidth() / 2, main_ad_full.getHeight(), 0);
        if (crA != null) {
            crA.addListener(new SimpleAnimListener() {
                @Override
                public void onAnimationEnd(CRAnimation animation) {
                    super.onAnimationEnd(animation);
                    main_ad_full.setVisibility(View.GONE);
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
            if (menu_hong != null) {
                menu_hong.setVisibility(View.VISIBLE);
            }
        } else {
            if (menu_hong != null) {
                menu_hong.setVisibility(View.GONE);
            }
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
