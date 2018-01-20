package com.mutter.clean.junk.myActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AdListener;
import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.jkb.slidemenu.SlideMenuLayout;
import com.mingle.circletreveal.CircularRevealCompat;
import com.mingle.widget.animation.CRAnimation;
import com.mingle.widget.animation.SimpleAnimListener;
import com.mutter.clean.core.CleanManager;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.entity.SideInfo;
import com.mutter.clean.junk.myAdapter.SideAdapter;
import com.mutter.clean.junk.myview.BubbleMainLayout;
import com.mutter.clean.junk.myview.CatShuView;
import com.mutter.clean.junk.myview.LoadingTime;
import com.mutter.clean.junk.myview.RoundRam;
import com.mutter.clean.junk.myview.RoundSd;
import com.mutter.clean.junk.presenter.MainPresenter;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.BadgerCount;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.UtilGp;
import com.mutter.clean.junk.view.MainView;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.mutter.module.charge.saver.Util.Constants;
import com.mutter.module.charge.saver.Util.Utils;

public class MainActivity extends BaseActivity implements MainView {

    ImageView iv_title_right;
    ImageView iv_title_left;
    RelativeLayout main_sd_air_button, main_ram_air_button;
    public static final String TAG = "MainActivity";
    RoundSd main_custom_sd;
    RelativeLayout main_junk_button, main_cooling_button;
    RoundRam main_custom_ram;
    TextView main_sd_per, main_sd_size, main_ram_per, main_ram_size;
    RelativeLayout main_manager_button;
    TextView main_junk_h, main_cooling_h;
    ListView side_listView;
    SlideMenuLayout main_drawer;
    com.mingle.widget.LinearLayout ll_ad_full;
    TextView main_full_time;
    FrameLayout libao_load;
    ImageView load_1;
    CatShuView shu_1, shu_2;

    // LottieAnimationView lot_side;
    FrameLayout ad_delete;
    BubbleMainLayout bubble_main;
    private LoadingTime ad_loading;


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
    PagerAdapter pagerAdapter;
    /* private PagerAdapter pagerAdapter;
     private View pageView;*/

    private boolean mDrawerOpened = false;
    private String from;
    private AlertDialog dialog;
    private LinearLayout deep;
    private FrameLayout lot_tap;
    private AlertDialog dialogB;
    private ObjectAnimator load_rotate;
    private String LOADING_FULL = "loading_full";
    private String EXIT_FULL = "loading_full";//mutter_exit_full


    @Override
    protected void findId() {
        super.findId();
        main_drawer = (SlideMenuLayout) findViewById(R.id.main_drawer);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);

        main_sd_air_button = (RelativeLayout) findViewById(R.id.main_sd_air_button);
        main_custom_sd = (RoundSd) findViewById(R.id.main_custom_sd);
        main_sd_per = (TextView) findViewById(R.id.main_sd_per);
        main_sd_size = (TextView) findViewById(R.id.main_sd_size);
        main_ram_air_button = (RelativeLayout) findViewById(R.id.main_ram_air_button);
        main_custom_ram = (RoundRam) findViewById(R.id.main_custom_ram);
        main_ram_per = (TextView) findViewById(R.id.main_ram_per);
        main_ram_size = (TextView) findViewById(R.id.main_ram_size);

        side_listView = (ListView) findViewById(R.id.side_listView);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);

        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_manager_button = (RelativeLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_junk_h = (TextView) findViewById(R.id.main_junk_h);
        main_cooling_h = (TextView) findViewById(R.id.main_cooling_h);

        ad_delete = (FrameLayout) findViewById(R.id.ad_delete);
        bubble_main = (BubbleMainLayout) findViewById(R.id.bubble_main);
        libao_load = (FrameLayout) findViewById(R.id.libao_load);
        load_1 = (ImageView) findViewById(R.id.load_1);
        shu_1 = (CatShuView) findViewById(R.id.shu_1);
        shu_2 = (CatShuView) findViewById(R.id.shu_2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        cleanApplication = (MyApplication) getApplication();
        BadgerCount.setCount(this);

        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        AdUtil.track("主页面", "进入主页面", "", 1);

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
                    final long junk_size = CleanManager.getInstance(MainActivity.this).getApkSize() + CleanManager.getInstance(MainActivity.this).getCacheSize() +
                            CleanManager.getInstance(MainActivity.this).getUnloadSize() + CleanManager.getInstance(MainActivity.this).getLogSize() + CleanManager.getInstance(MainActivity.this).getDataSize();
                    if (junk_size > 0) {
                        main_junk_h.setText(Util.convertStorage(junk_size, true));
                        if (PreData.getDB(MainActivity.this, Constant.HONG_JUNK, true)) {
                            main_junk_h.setVisibility(View.VISIBLE);
                        }
                    } else {
                        main_junk_h.setVisibility(View.GONE);
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
                    }
                });
            }
        });

    }


    //初始化监听
    public void onClick() {
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_sd_air_button.setOnClickListener(onClickListener);
        main_ram_air_button.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        ad_delete.setOnClickListener(onClickListener);


    }

    @Override
    public void initCpu(final int temp) {
        this.temp = temp;
        if (main_cooling_h.getVisibility() == View.INVISIBLE) {
            main_cooling_h.setText(String.valueOf(temp) + "℃");
            if (PreData.getDB(MainActivity.this, Constant.HONG_COOLING, true)) {
                main_cooling_h.setVisibility(View.VISIBLE);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int time = 500;
//                        for (long i = 0; i <= temp; i += (temp / 40)) {
//                            final long finalI = i;
//                            time -= 5;
//                            if (time < 30) {
//                                time = 30;
//                            }
//                            if (onDestroyed) {
//                                break;
//                            }
//                            try {
//                                Thread.sleep(time);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            if (onDestroyed) {
//                                break;
//                            }
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    main_cooling_h.setText(String.valueOf(finalI) + "℃");
//                                }
//                            });
//                        }
//                    }
//                }).start();
            }

        }
    }

    public void initSideData() {
        if (adapter == null) {
            adapter = new SideAdapter(this);
            side_listView.setAdapter(adapter);
        }
        adapter.clear();
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
        adapter.addData(new SideInfo(R.string.main_cooling_name, R.mipmap.side_battery));//电池降温
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评


    }

    @Override
    public void loadFullAd() {
        int num = PreData.getDB(this, Constant.ROTATE_FIRST, 0);
        if (num == 2) {
            showRotate();
            num++;
            PreData.putDB(this, Constant.ROTATE_FIRST, num);
        } else if (num < 2) {
            num++;
            PreData.putDB(this, Constant.ROTATE_FIRST, num);
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
    }

    @Override
    public void openDrawer() {
        main_drawer.openLeftSlide();
    }

    @Override
    public void closeDrawer() {
        if (main_drawer.isLeftSlideOpen()) {
            main_drawer.closeLeftSlide();
        }
    }

    Runnable fullAdRunnale = new Runnable() {
        @Override
        public void run() {
            adDelete();
        }
    };

    private void showRotate() {
        View view = getLayoutInflater().inflate(R.layout.dialog_rotate, null);
        TextView exit_quxiao = (TextView) view.findViewById(R.id.exit_quxiao);
        TextView exit_queren = (TextView) view.findViewById(R.id.exit_queren);
        ImageView rotate_cha = (ImageView) view.findViewById(R.id.rotate_cha);
        rotate_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogB.dismiss();
            }
        });
        exit_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogB.dismiss();
            }
        });
        exit_queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogB.dismiss();
                UtilGp.rate(MainActivity.this.getApplicationContext());
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
                case R.id.ad_delete:
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
                        load_1.setVisibility(View.VISIBLE);
                        load_rotate = ObjectAnimator.ofFloat(load_1, View.ROTATION, 0, -360 * 2);
                        load_rotate.setDuration(4500);
                        load_rotate.start();
                        load_rotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float value = (float) animation.getAnimatedValue();
                                shu_1.setProgress((int) value);
                                shu_2.setProgress((int) value);
                            }
                        });
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

            main_cooling_h.setVisibility(View.GONE);
        } else if (resultCode == Constant.JUNK_RESUIL) {
            main_junk_h.setVisibility(View.GONE);
        } else if (resultCode == Constant.POWER_RESUIL) {

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
        if (bubble_main != null)
            bubble_main.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AndroidSdk.onResumeWithoutTransition(this);
        AndroidSdk.loadFullAd(EXIT_FULL, null);
        if (bubble_main != null)
            bubble_main.reStart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bubble_main != null)
            bubble_main.destroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (ad_loading != null) {
            ad_loading.cancle();
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
        if (main_drawer.isLeftSlideOpen()) {
            main_drawer.closeLeftSlide();
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
        dialog = new AlertDialog.Builder(this, R.style.exit_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }


}
