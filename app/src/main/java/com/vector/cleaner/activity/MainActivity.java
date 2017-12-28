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
import android.view.animation.LinearInterpolator;
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
import com.vector.cleaner.myview.BubbleCenterLayout;
import com.vector.cleaner.myview.LineRam;
import com.vector.cleaner.myview.MyScrollView;
import com.vector.cleaner.myview.PullToRefreshLayout;
import com.vector.cleaner.presenter.MainPresenter;
import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.cleaner.utils.UtilGp;
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
import com.vector.module.mView.BubbleLayout;

public class MainActivity extends BaseActivity implements MainView, DrawerLayout.DrawerListener {

    public static final String TAG = "MainActivity";
    MyScrollView main_scroll_view;
    FrameLayout main_ram_qiu, main_file_qiu;
    TextView main_sd_per, main_sd_size, main_ram_per, main_ram_size;
    RelativeLayout main_junk_button, main_applock_button, main_cooling_button;
    LinearLayout main_manager_button, main_theme_button;
    PullToRefreshLayout main_pull_refresh;
    ImageView iv_title_right;
    ImageView iv_title_left;
    ImageView main_ram_ani;
    FrameLayout main_file_ani_1;
    BubbleLayout bubble_file;
    ImageView main_file_ani_2;
    LineRam main_custom_sd;
    LineRam main_custom_ram;
    com.mingle.widget.LinearLayout ll_ad_full;
    ProgressBar ad_progressbar;
    LinearLayout main_battery;

    ImageView lot_ad;
    FrameLayout full_load;
    ImageView full_zhuan;
    BubbleCenterLayout full_bubble;
    ListViewForScrollView side_listView;
    DrawerLayout main_drawer;

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
    private AnimatorSet animatorSet_ram;
    private AnimatorSet animator_file;
    private ObjectAnimator objectAnimator_ad;

    @Override
    protected void findId() {
        super.findId();
        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        main_drawer.addDrawerListener(this);
        //main_all_cercle = (FrameLayout) findViewById(R.id.main_all_cercle);
        main_scroll_view = (MyScrollView) findViewById(R.id.main_scroll_view);
        main_ram_qiu = (FrameLayout) findViewById(R.id.main_ram_qiu);
        main_file_qiu = (FrameLayout) findViewById(R.id.main_file_qiu);
        main_pull_refresh = (PullToRefreshLayout) findViewById(R.id.main_pull_refresh);
        iv_title_right = (ImageView) findViewById(R.id.iv_title_right);
        iv_title_left = (ImageView) findViewById(R.id.iv_title_left);


        main_custom_sd = (LineRam) findViewById(R.id.main_custom_sd);
        main_sd_per = (TextView) findViewById(R.id.main_sd_per);
        main_sd_size = (TextView) findViewById(R.id.main_sd_size);
        main_custom_ram = (LineRam) findViewById(R.id.main_custom_ram);
        main_ram_ani = (ImageView) findViewById(R.id.main_ram_ani);
        main_file_ani_1 = (FrameLayout) findViewById(R.id.main_file_ani_1);
        bubble_file = (BubbleLayout) findViewById(R.id.bubble_file);
        main_file_ani_2 = (ImageView) findViewById(R.id.main_file_ani_2);
        main_ram_per = (TextView) findViewById(R.id.main_ram_per);
        main_ram_size = (TextView) findViewById(R.id.main_ram_size);


        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_applock_button = (RelativeLayout) findViewById(R.id.main_applock_button);
        main_manager_button = (LinearLayout) findViewById(R.id.main_manager_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_theme_button = (LinearLayout) findViewById(R.id.main_theme_button);
        side_listView = (ListViewForScrollView) findViewById(R.id.side_listView);
        ll_ad_full = (com.mingle.widget.LinearLayout) findViewById(R.id.ll_ad_full);
        ad_progressbar = (ProgressBar) findViewById(R.id.ad_progressbar);

        lot_ad = (ImageView) findViewById(R.id.lot_ad);
        full_load = (FrameLayout) findViewById(R.id.full_load);
        full_zhuan = (ImageView) findViewById(R.id.full_zhuan);
        full_bubble = (BubbleCenterLayout) findViewById(R.id.full_bubble);
        main_battery = (LinearLayout) findViewById(R.id.main_battery);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dra);
        full_bubble.pause();
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.init();
        mainPresenter.setDrawerLeftEdgeSize(main_drawer, 0.1f);
        objectAnimator_ad = ObjectAnimator.ofFloat(lot_ad, View.ROTATION, 0, 360);
        objectAnimator_ad.setDuration(1000);
        objectAnimator_ad.setRepeatCount(-1);
        objectAnimator_ad.setInterpolator(new LinearInterpolator());
        objectAnimator_ad.start();
        AdUtil.track("主页面", "进入主页面", "", 1);
    }


    //初始化监听
    public void onClick() {
        //main_scroll_view.setOnTouchListener(scrollViewTouchListener);
        main_pull_refresh.setOnRefreshListener(refreshListener);
        iv_title_right.setOnClickListener(onClickListener);
        iv_title_left.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_applock_button.setOnClickListener(onClickListener);
        main_manager_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_theme_button.setOnClickListener(onClickListener);
        main_ram_qiu.setOnClickListener(onClickListener);
        main_file_qiu.setOnClickListener(onClickListener);
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

        adapter.addData(new SideInfo(R.string.main_cooling_name, R.mipmap.side_cooling));//降温
        adapter.addData(new SideInfo(R.string.side_ram, R.mipmap.side_ram));//内存加速
        adapter.addData(new SideInfo(R.string.side_junk, R.mipmap.side_junk));//垃圾清理
        adapter.addData(new SideInfo(R.string.side_manager, R.mipmap.side_manager));//应用管理
//        adapter.addData(new SideInfo(R.string.side_file, R.mipmap.side_file));//文件管理
//        adapter.addData(new SideInfo(R.string.side_power, R.mipmap.side_power));//深度清理
        adapter.addData(new SideInfo(R.string.gboost_0, R.mipmap.side_gboost));//游戏加速
        adapter.addData(new SideInfo(R.string.side_notifi, R.mipmap.side_nitifi));//通知栏清理
//        adapter.addData(new SideInfo(R.string.side_picture, R.mipmap.side_picture));//相似图片
        adapter.addData(new SideInfo(R.string.side_rotate, R.mipmap.side_rotate));//好评
        adapter.addData(new SideInfo(R.string.side_setting, R.mipmap.side_setting));//设置

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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bubble_file != null) {
                    main_file_ani_1.setScaleY(1);
                    main_file_ani_1.setScaleX(1);
                    main_file_ani_1.setAlpha(1);
                    main_file_ani_2.setVisibility(View.INVISIBLE);
                    bubble_file.reStart();
                }
                main_sd_size.setText(size);
                if (percent < 50) {
                    main_file_qiu.setBackgroundResource(R.drawable.main_1);
                } else if (percent < 78) {
                    main_file_qiu.setBackgroundResource(R.drawable.main_2);
                } else {
                    main_file_qiu.setBackgroundResource(R.drawable.main_3);
                }
            }
        });
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

            @Override
            public void progressSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bubble_file != null) {
                            bubble_file.pause();
                        }
                        animator_file = new AnimatorSet();
                        ObjectAnimator objectanimator_1 = ObjectAnimator.ofFloat(main_file_ani_1, View.SCALE_X, 1, 0.5f);
                        ObjectAnimator objectanimator_2 = ObjectAnimator.ofFloat(main_file_ani_1, View.SCALE_Y, 1, 0.5f);
                        ObjectAnimator objectanimator_3 = ObjectAnimator.ofFloat(main_file_ani_1, View.ALPHA, 1, 0);
                        final ObjectAnimator objectanimator_4 = ObjectAnimator.ofFloat(main_file_ani_2, View.SCALE_X, 0.5f, 1);
                        final ObjectAnimator objectanimator_5 = ObjectAnimator.ofFloat(main_file_ani_2, View.SCALE_Y, 0.5f, 1);

                        animator_file.playTogether(objectanimator_1, objectanimator_2, objectanimator_3);
                        animator_file.setDuration(700);
                        animator_file.start();
                        animator_file.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animator_file = new AnimatorSet();
                                animator_file.playTogether(objectanimator_4, objectanimator_5);
                                animator_file.setDuration(700);
                                animator_file.start();
                                main_file_ani_2.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }
                        });
                    }
                });
            }
        });


    }


    @Override
    public void initRam(final int percent, final String size) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (percent < 50) {
                    main_ram_qiu.setBackgroundResource(R.drawable.main_1);
                } else if (percent < 78) {
                    main_ram_qiu.setBackgroundResource(R.drawable.main_2);
                } else {
                    main_ram_qiu.setBackgroundResource(R.drawable.main_3);
                }
            }
        });
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

            @Override
            public void progressSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (animatorSet_ram == null) {
                            animatorSet_ram = new AnimatorSet();
                            ObjectAnimator objectAnimator_y = ObjectAnimator.ofFloat(main_ram_ani, View.TRANSLATION_Y, main_ram_ani.getHeight(), 0);
                            ObjectAnimator objectAnimator_x = ObjectAnimator.ofFloat(main_ram_ani, View.TRANSLATION_X, -main_ram_ani.getWidth() * 0.6f, 0);
                            animatorSet_ram.play(objectAnimator_x).with(objectAnimator_y);
                            animatorSet_ram.setDuration(800);
                        }
                        animatorSet_ram.start();
                        main_ram_ani.setVisibility(View.VISIBLE);
                    }
                });
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
        }

        //上拉加载操作
        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
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
                case R.id.main_file_qiu:
                    AdUtil.track("主页面", "点击sd球进入垃圾清理页面", "", 1);
                    mainPresenter.jumpToActivity(LajiFileActivity.class, 1);
                    break;
                case R.id.main_ram_qiu:
                    AdUtil.track("主页面", "点击ram球进入内存加速页面", "", 1);
                    mainPresenter.jumpToActivity(ANeicunAvtivity.class, 1);
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("主页面", "点击垃圾清理按钮", "", 1);
                    mainPresenter.jumpToActivity(LajiFileActivity.class, 1);
                    break;
                case R.id.main_applock_button:
                    AdUtil.track("主页面", "点击applock", "", 1);
                    // TODO: 2017/12/25  
                    UtilGp.openPlayStore(MainActivity.this, "");
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
                        full_bubble.reStart();
                        ObjectAnimator objectanimator_zhuan = ObjectAnimator.ofFloat(full_zhuan, View.ROTATION, 0, 3600);
                        objectanimator_zhuan.setDuration(3600);
                        ObjectAnimator objectanimator_zhuan_2 = ObjectAnimator.ofFloat(full_bubble, View.ROTATION, 0, 720);
                        objectanimator_zhuan_2.setDuration(3600);
                        animatorSet.play(objectanimator_zhuan).with(objectanimator_zhuan_2);
                        animatorSet.start();
                        animatorSet.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                full_bubble.pause();
                                AndroidSdk.showFullAd(LOADING_FULL);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        full_load.setVisibility(View.GONE);
                                    }
                                }, 500);
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

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
        main_ram_ani.setVisibility(View.INVISIBLE);
        if (animatorSet_ram != null && animatorSet_ram.isRunning()) {
            animatorSet_ram.cancel();
        }
        if (animator_file != null && animator_file.isRunning()) {

            animator_file.cancel();
        }
        main_file_ani_2.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        if (PreData.getDB(this, Constant.FULL_EXIT, 0) == 1) {
            AndroidSdk.loadFullAd("vector_exit_full", null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bubble_file != null) {
            bubble_file.pause();
            bubble_file.destroy();
        }
        if (full_bubble != null) {
            full_bubble.pause();
            full_bubble.destroy();
        }
        if (objectAnimator_ad != null) {
            objectAnimator_ad.cancel();
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
            full_bubble.pause();
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
