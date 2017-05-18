package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.eos.manager.page.SecuritySharPFive;
import com.eos.module.tweenengine.Tween;
import com.eos.module.tweenengine.TweenEquations;
import com.eos.module.tweenengine.TweenManager;
import com.eos.ui.demo.cross.CrossManager;
import com.eos.ui.demo.dialog.DialogManager;
import com.eos.ui.demo.entries.CrossData;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.DrawHookView;
import com.supers.clean.junk.customeview.ImageAccessor;
import com.supers.clean.junk.customeview.SlowScrollView;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;
import com.supers.clean.junk.util.UtilGp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/6.
 */

public class SuccessActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView success_jiantou;
    TextView success_clean_size;
    TextView success_clean_2;
    DrawHookView success_drawhook;
    ImageView success_huojian;
    SlowScrollView scrollView;
    LinearLayout main_rotate_all;
    LinearLayout main_power_button;
    LinearLayout main_notifi_button;
    LinearLayout main_file_button;
    LinearLayout main_cooling_button;
    LinearLayout main_ram_button;
    LinearLayout main_junk_button;
    LinearLayout main_gboost_button;
    LinearLayout main_picture_button;
    ImageView power_icon;
    TextView power_text;
    ImageView main_rotate_good;
    //    ImageView delete;
    ImageView success_progress;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;

    LinearLayout ad_native_2;
    FrameLayout fl_lot_success;
    LinearLayout main_tuiguang_button;
    TextView main_msg_tuiguang;
    LottieAnimationView lot_success;
    private View nativeView;
    private View native_xiao;
    private View native_title;

    private boolean isdoudong;
    private TweenManager tweenManager;
    private boolean istween;
    private Handler myHandler;
    private String TAG_CLEAN = "eos_success";
    private String TAG_CLEAN_2 = "eos_success_2";
    private String TAG_TITLE = "eos_icon";
    private Animation rotate;

    private boolean haveAd;
    private boolean animationEnd;
    private SecuritySharPFive shareFive;
    private MyApplication cleanApplication;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        success_jiantou = (ImageView) findViewById(R.id.success_jiantou);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        success_clean_2 = (TextView) findViewById(R.id.success_clean_2);
        success_drawhook = (DrawHookView) findViewById(R.id.success_drawhook);
        success_huojian = (ImageView) findViewById(R.id.success_huojian);
        scrollView = (SlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_power_button = (LinearLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (LinearLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (LinearLayout) findViewById(R.id.main_file_button);
        main_cooling_button = (LinearLayout) findViewById(R.id.main_cooling_button);
        main_ram_button = (LinearLayout) findViewById(R.id.main_ram_button);
        main_junk_button = (LinearLayout) findViewById(R.id.main_junk_button);
        main_gboost_button = (LinearLayout) findViewById(R.id.main_gboost_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        power_text = (TextView) findViewById(R.id.power_text);
        main_rotate_good = (ImageView) findViewById(R.id.main_rotate_good);
//        delete = (ImageView) findViewById(R.id.delete);
        power_icon = (ImageView) findViewById(R.id.power_icon);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        success_progress = (ImageView) findViewById(R.id.success_progress);
        fl_lot_success = (FrameLayout) findViewById(R.id.fl_lot_success);
        main_tuiguang_button = (LinearLayout) findViewById(R.id.main_tuiguang_button);
        main_msg_tuiguang = (TextView) findViewById(R.id.main_msg_tuiguang);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_success);
        tweenManager = new TweenManager();
        Tween.registerAccessor(ImageView.class, new ImageAccessor());
        istween = true;
        setAnimationThread();
        myHandler = new Handler();
        shareFive = new SecuritySharPFive(this);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_ni);
        success_progress.startAnimation(rotate);
        if (getIntent().getStringExtra("name") != null) {
            title_name.setText(getIntent().getStringExtra("name"));
        } else {
            title_name.setText(R.string.success_title);
        }

        if (TextUtils.equals("ramSpeed", getIntent().getStringExtra("from"))) {
            main_notifi_button.setVisibility(View.GONE);
            main_file_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
            main_picture_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("junkClean", getIntent().getStringExtra("from"))) {
            main_notifi_button.setVisibility(View.GONE);
            main_file_button.setVisibility(View.GONE);
            main_junk_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
            main_picture_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("allJunk", getIntent().getStringExtra("from"))) {
            main_power_button.setVisibility(View.GONE);
            main_notifi_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_junk_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
            main_file_button.setVisibility(View.GONE);
            main_notifi_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
            main_picture_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("power", getIntent().getStringExtra("from")) || TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
            main_power_button.setVisibility(View.GONE);
            main_notifi_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
            main_file_button.setVisibility(View.GONE);
            main_notifi_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
            main_picture_button.setVisibility(View.GONE);
            main_notifi_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
        }


        //ram加速
        long sizeR = getIntent().getLongExtra("sizeR", 0);
        //垃圾清理
        long sizeJ = getIntent().getLongExtra("sizeJ", 0);
        //文件清理
        long sizeF = getIntent().getLongExtra("sizeF", 0);
        //相似图片清理
        int sizePic = getIntent().getIntExtra("sizePic", 0);
        //强力清理
        int count = getIntent().getIntExtra("count", 0);
        //通知栏
        int num = getIntent().getIntExtra("num", 0);
        //降温
        int wendu = getIntent().getIntExtra("wendu", 0);
        if (sizeR > 0) {
            success_clean_size.setText(getString(R.string.success_3, CommonUtil.convertStorage(sizeR, true)));
        } else if (sizeJ > 0) {
            success_clean_size.setText(getString(R.string.success_2, CommonUtil.convertStorage(sizeJ, true)));
        } else if (count > 0) {
            success_clean_size.setText(getString(R.string.power_1, String.valueOf(count)) + " ");
        } else if (sizeF > 0) {
            success_clean_size.setText(getString(R.string.success_7, CommonUtil.convertStorage(sizeF, true)));
        } else if (num > 0) {
            success_clean_size.setText(getString(R.string.success_6, num + ""));
        } else if (wendu > 0) {
            success_clean_size.setText(getString(R.string.success_5, wendu + "℃"));
        } else if (sizePic > 0) {
            success_clean_size.setText(getString(R.string.success_4, sizePic + ""));
        } else {
            success_clean_size.setText(getText(R.string.success_normal));
            success_clean_2.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }
        initAnimation();
        success_drawhook.setListener(new DrawHookView.DrawHookListener() {

            @Override
            public void duogouSc() {
                if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS, 0) == 1) {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
                startSecondAnimation();
                success_drawhook.setListener(null);
            }
        });
        if (PreData.getDB(this, Constant.IS_ROTATE, false) || shareFive.getFiveRate()) {
            main_rotate_all.setVisibility(View.GONE);
        }

        addListener();
        TranslateAnimation translate = new TranslateAnimation(0, 0, 10, 2);
        translate.setInterpolator(new AccelerateInterpolator());//OvershootInterpolator
        translate.setDuration(400);
        translate.setRepeatCount(-1);
        translate.setRepeatMode(Animation.REVERSE);
        success_jiantou.startAnimation(translate);
        tuiGuang();
        shendu();
        if (PreData.getDB(this, Constant.FULL_SUCCESS, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    success_jiantou.clearAnimation();
//                    success_jiantou.setVisibility(View.INVISIBLE);
//                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
            }, data.inter_time * 1000);

        } else {
            myHandler.postDelayed(new Runnable() {
                public void run() {
                    addAd();
                }
            }, 1000);

        }
    }

    private void shendu() {
        cleanApplication = (MyApplication) getApplication();
        List<JunkInfo> startList = new ArrayList<>();
        for (JunkInfo info : cleanApplication.getAppRam()) {
            if (info.isStartSelf) {
                startList.add(info);
            }
        }
        if (startList.size() == 0) {
            main_power_button.setVisibility(View.GONE);
        } else {
            power_icon.setImageDrawable(startList.get(0).icon);
            String text1 = getString(R.string.power_1, String.valueOf(startList.size())) + " ";
            SpannableString ss1 = new SpannableString(text1 + getString(R.string.power_4));
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3131")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            power_text.setText(ss1);
        }
    }

    @Override
    public void tuiGuang() {
        super.tuiGuang();
        CrossData.CrossPromotionBean bean = DialogManager.getCrossManager().getCrossData(this, extraData, "list1", "side");
        if (bean != null) {
            tuiguang = bean.pkg;
        }
        DialogManager.getCrossView(getApplicationContext(), extraData, "list1", "success", true, new CrossManager.onCrossViewClickListener() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void onLoadView(View view) {
                if (view != null) {
                    if (TextUtils.equals(tuiguang, "com.eosmobi.applock")) {
                        main_msg_tuiguang.setText("EOS Applock");
                    } else if (TextUtils.equals(tuiguang, "com.eosmobi.flashlight.free")) {
                        main_msg_tuiguang.setText("EOS Flashlight");
                    }
                    ((ImageView) view.findViewById(R.id.cross_default_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    lot_success = ((LottieAnimationView) view.findViewById(R.id.cross_default_lottie));
                    lot_success.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Log.e("tuiguang", "main 不为空");
                    main_tuiguang_button.setVisibility(View.VISIBLE);
                    if (onPause) {
                        lot_success.pauseAnimation();
                    }
                    fl_lot_success.addView(view, 0);
                } else {
                    main_tuiguang_button.setVisibility(View.GONE);
                    Log.e("tuiguang", "main 为空");
                }
            }
        });
    }

    private void initAnimation() {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                startFirstAnimation();
            }
        });
    }

    private void addListener() {
        title_left.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
//        delete.setOnClickListener(onClickListener);
        main_tuiguang_button.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);

    }

    private void addAd() {
        nativeView = CommonUtil.getNativeAdView(TAG_CLEAN, R.layout.native_ad_full);
        native_xiao = CommonUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_2);
        native_title = CommonUtil.getNativeAdView(TAG_TITLE, R.layout.native_ad_title);
        if (ad_native_2 != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
            layout_ad.height = scrollView.getMeasuredHeight();
            Log.e("success_ad", "hiegt=" + scrollView.getMeasuredHeight());
            ad_native_2.setLayoutParams(layout_ad);
            haveAd = true;
            ad_native_2.addView(nativeView);
            if (animationEnd) {
                ad_native_2.setVisibility(View.VISIBLE);
                scrollView.isTouch = false;
                scrollView.smoothScrollToSlow(2000);
            }
        }
        if (ad_title != null && native_title != null) {
            ad_title.addView(native_title);
            ad_title.setVisibility(View.VISIBLE);
        }
        if (ll_ad_xiao != null && native_xiao != null) {
            ll_ad_xiao.addView(native_xiao);
            ll_ad_xiao.setVisibility(View.VISIBLE);
        }
    }

//    private void addAd() {
//        AndroidSdk.loadNativeAd(TAG_CLEAN, R.layout.native_ad_full, new ClientNativeAd.NativeAdLoadListener() {
//            @Override
//            public void onNativeAdLoadSuccess(View view) {
//                Log.e("inf", "reload success1");
//                nativeView = view;
//                haveAd = true;
//                ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
//                layout_ad.height = scrollView.getMeasuredHeight();
//                ad_native_2.setLayoutParams(layout_ad);
//                ad_native_2.addView(nativeView);
//                tv_next = (TextView) nativeView.findViewWithTag("ad_next");
//                iv_next = (ImageView) nativeView.findViewWithTag("ad_iv_next");
//                tv_next.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        reloadNativeAd();
//                        tv_next.setVisibility(View.INVISIBLE);
//                        iv_next.setVisibility(View.VISIBLE);
//                        iv_next.startAnimation(rotate);
//                    }
//                });
//
//                if (animationEnd) {
//                    ad_native_2.setVisibility(View.VISIBLE);
//                    scrollView.isTouch = false;
//                    scrollView.smoothScrollToSlow(2000);
//                }
//            }
//
//            @Override
//            public void onNativeAdLoadFails() {
//                Log.e("inf", "reload shibai1");
//                haveAd = false;
//                nativeView = null;
//                ad_native_2.setVisibility(View.GONE);
//            }
//        });
//    }

//    public void reloadNativeAd() {
//        AndroidSdk.reLoadNativeAd(TAG_CLEAN, nativeView, new ClientNativeAd.NativeAdLoadListener() {
//            @Override
//            public void onNativeAdLoadSuccess(View view) {
//                Log.e("inf", "reload success");
//                tv_next.setVisibility(View.VISIBLE);
//                iv_next.setVisibility(View.GONE);
//                iv_next.clearAnimation();
//                view.findViewWithTag("ad_next").setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        reloadNativeAd();
//                        tv_next.setVisibility(View.INVISIBLE);
//                        iv_next.setVisibility(View.VISIBLE);
//                        iv_next.startAnimation(rotate);
//                    }
//                });
//            }
//
//            @Override
//            public void onNativeAdLoadFails() {
//                Log.e("inf", "reload fails");
//                tv_next.setVisibility(View.VISIBLE);
//                iv_next.setVisibility(View.GONE);
//                iv_next.clearAnimation();
//            }
//        });
//    }

    public void startFirstAnimation() {
        Animation animation = AnimationUtils.loadAnimation(SuccessActivity.this, R.anim.huojian_pop);
        success_huojian.startAnimation(animation);
        animation.setFillAfter(true);
        success_huojian.setVisibility(View.VISIBLE);
        animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                final float hx = success_huojian.getX();
                final float hy = success_huojian.getY();
                isdoudong = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (isdoudong) {
                            if (onDestroyed) {
                                break;
                            }
                            int x = (int) (Math.random() * (16)) - 8;
                            int y = (int) (Math.random() * (16)) - 8;
                            try {
                                Thread.sleep(80);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Tween.to(success_huojian, ImageAccessor.BOUNCE_EFFECT, 0.08f).target(hx + x, hy + y, 1, 1)
                                    .ease(TweenEquations.easeInQuad).delay(0)
                                    .start(tweenManager);
                        }
                    }
                }).start();
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        success_progress.clearAnimation();
                        success_progress.setVisibility(View.GONE);
                        isdoudong = false;
                        ObjectAnimator a = ObjectAnimator.ofFloat(success_huojian, View.TRANSLATION_Y, 0, -2500);
                        a.setDuration(300);
                        a.start();
                        a.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                success_huojian.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }
                        });
                        success_drawhook.startProgress(500);
                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }

            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

            }
        });
    }

    private void startSecondAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_success);
        scrollView.startAnimation(animation);
        scrollView.setVisibility(View.VISIBLE);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationEnd = true;
                        if (haveAd) {
                            ad_native_2.setVisibility(View.VISIBLE);
                            scrollView.isTouch = false;
                            scrollView.smoothScrollToSlow(2000);
                        }
                    }
                }, 1000);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.main_rotate_good:
                    shareFive.setFiveRate(true);
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE, true);
                    UtilGp.rate(SuccessActivity.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_power_button:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    CommonUtil.track("完成页面", "点击进入深度清理", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.DEEP_CLEAN, true);
                    jumpTo(PowerActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button:
                    CommonUtil.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(JunkActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    CommonUtil.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(RamAvtivity.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button:
                    CommonUtil.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(CoolingActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_file_button:
                    if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    CommonUtil.track("完成页面", "点击进入文件管理", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.FILE_CLEAN, true);
                    jumpTo(FileActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_gboost_button:
                    if (TextUtils.equals("Gboost", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    CommonUtil.track("完成页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.GBOOST_CLEAN, true);
                    jumpTo(GBoostActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    CommonUtil.track("完成页面", "点击进入相似图片", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.PHOTO_CLEAN, true);
                    jumpTo(PictureActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_notifi_button:
                    CommonUtil.track("完成页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!CommonUtil.isNotificationListenEnabled(SuccessActivity.this)) {
                        startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 100);
                    } else if (!PreData.getDB(SuccessActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SuccessActivity.this, NotifiInfoActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    } else {
                        Intent intent6 = new Intent(SuccessActivity.this, NotifiActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    }

                    break;
                case R.id.main_tuiguang_button:
                    if (CommonUtil.isPkgInstalled(tuiguang, getPackageManager())) {
                        CommonUtil.doStartApplicationWithPackageName(getApplicationContext(), tuiguang);
                    } else {
                        UtilGp.openPlayStore(getApplicationContext(), tuiguang);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (CommonUtil.isNotificationListenEnabled(SuccessActivity.this)) {
                PreData.putDB(SuccessActivity.this, Constant.KEY_NOTIFI, true);
                Intent intent = new Intent(SuccessActivity.this, NotifiActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SuccessActivity.this, NotifiInfoActivity.class);
                startActivity(intent);
            }
            onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        if (lot_success != null) {
            lot_success.playAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lot_success != null) {
            lot_success.pauseAnimation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        istween = false;
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }

    private void setAnimationThread() {
        new Thread(new Runnable() {
            private long lastMillis = -1;

            public void run() {
                while (istween) {
                    if (lastMillis > 0) {
                        long currentMillis = System.currentTimeMillis();
                        final float delta = (currentMillis - lastMillis) / 1000f;

                        runOnUiThread(new Runnable() {

                            public void run() {
                                tweenManager.update(delta);

                            }
                        });

                        lastMillis = currentMillis;
                    } else {
                        lastMillis = System.currentTimeMillis();
                    }
                    try {
                        Thread.sleep(1000 / 60);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }).start();
    }
}
