package com.fast.clean.junk.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AdListener;
import com.android.client.AndroidSdk;
import com.fast.clean.core.CleanManager;
import com.fast.clean.entity.JunkInfo;
import com.fast.clean.junk.R;
import com.fast.clean.junk.myview.DrawHookView;
import com.fast.clean.junk.myview.ImageAccessor;
import com.fast.clean.junk.myview.SlowScrollView;
import com.fast.clean.junk.util.AdUtil;
import com.fast.clean.junk.util.Constant;
import com.fast.clean.junk.util.UtilGp;
import com.fast.clean.mutil.LoadManager;
import com.fast.clean.mutil.PreData;
import com.fast.clean.mutil.Util;
import com.twee.module.tweenengine.Tween;
import com.twee.module.tweenengine.TweenEquations;
import com.twee.module.tweenengine.TweenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/6.
 */

public class SuccessActivity extends BaseActivity {
    FrameLayout title_left;
    LinearLayout main_rotate_all;
    LinearLayout main_power_button;
    LinearLayout main_notifi_button;
    LinearLayout main_file_button;
    LinearLayout main_cooling_button;
    LinearLayout main_ram_button;
    LinearLayout main_junk_button;
    LinearLayout main_gboost_button;
    TextView title_name;
    ImageView success_jiantou;
    TextView success_clean_size;
    TextView success_clean_2;
    DrawHookView success_drawhook;
    ImageView success_huojian;
    SlowScrollView scrollView;
    LinearLayout main_picture_button;
    TextView main_rotate_bad;
    //    ImageView delete;
    ImageView success_progress;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;
    ImageView power_icon;
    TextView power_text;
    TextView main_rotate_good;

    TextView loading_text;

    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;

    private boolean isdoudong;
    private TweenManager tweenManager;
    private boolean istween;
    private Handler myHandler;
    private String TAG_CLEAN = "acht_success";
    private String TAG_CLEAN_2 = "acht_success_2";

    private boolean animationEnd;

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
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
//        delete = (ImageView) findViewById(R.id.delete);
        power_icon = (ImageView) findViewById(R.id.power_icon);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        success_progress = (ImageView) findViewById(R.id.success_progress);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
        loading_text = (TextView) findViewById(R.id.loading_text);
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
            success_clean_size.setText(getString(R.string.success_3, Util.convertStorage(sizeR, true)));
        } else if (sizeJ > 0) {
            success_clean_size.setText(getString(R.string.success_2, Util.convertStorage(sizeJ, true)));
        } else if (count > 0) {
            success_clean_size.setText(getString(R.string.power_1, String.valueOf(count)) + " ");
        } else if (sizeF > 0) {
            success_clean_size.setText(getString(R.string.success_7, Util.convertStorage(sizeF, true)));
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
                //startSecondAnimation();
                success_drawhook.setListener(null);
                final int maxShowCount = PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS_MAX_COUNT, 0);
                int showCount = PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS_COUNT, 0);

                if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS, 0) == 1) {
                    Log.e("rqy", maxShowCount + "--" + showCount);
                    PreData.putDB(SuccessActivity.this, Constant.FULL_SUCCESS_COUNT, ++showCount);
                    if (showCount > maxShowCount) {
                        PreData.putDB(SuccessActivity.this, Constant.NEED_SHOW_FULL_SUCCESS, true);
                    }
                    boolean need_show_full_success = PreData.getDB(SuccessActivity.this, Constant.NEED_SHOW_FULL_SUCCESS, false);
                    if (need_show_full_success) {
                        int ad_loading_time = PreData.getDB(SuccessActivity.this, Constant.AD_LOADING_TIME, 0);
                        if (ad_loading_time != 0) {
                            success_drawhook.setVisibility(View.GONE);
                            findViewById(R.id.success_1).setVisibility(View.GONE);
                            findViewById(R.id.success_progress).setVisibility(View.GONE);
                            loading_text.setVisibility(View.VISIBLE);
                        }
                        Log.e("rqy", maxShowCount + "--" + showCount + "--" + need_show_full_success + "--ad_loading_time=" + ad_loading_time);

                        myHandler.postDelayed(runnable, ad_loading_time * 1000);
                    } else {
                        startSecondAnimation();
                    }

                } else {
                    startSecondAnimation();
                }

            }
        });
        if (PreData.getDB(this, Constant.IS_ROTATE, false)) {
            main_rotate_all.setVisibility(View.GONE);
        }

        addListener();
        TranslateAnimation translate = new TranslateAnimation(0, 0, 10, 2);
        translate.setInterpolator(new AccelerateInterpolator());//OvershootInterpolator
        translate.setDuration(400);
        translate.setRepeatCount(-1);
        translate.setRepeatMode(Animation.REVERSE);
        success_jiantou.startAnimation(translate);
        shendu();
        if (PreData.getDB(this, Constant.NATIVE_SUCCESS, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                public void run() {
                    addAd();
                }
            }, 1000);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (TextUtils.equals("ramSpeed", getIntent().getStringExtra("from"))) {
                AndroidSdk.showFullAd("ramboost");
            } else if (TextUtils.equals("junkClean", getIntent().getStringExtra("from"))) {
                AndroidSdk.showFullAd("junkclean");
            } else if (TextUtils.equals("allJunk", getIntent().getStringExtra("from"))) {
                AndroidSdk.showFullAd("Rocketclean");
            } else if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
                AndroidSdk.showFullAd("cpucooler");
            } else {
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
            }
            startSecondAnimation();
        }
    };


    private void shendu() {
        List<JunkInfo> startList = new ArrayList<>();
        for (JunkInfo info : CleanManager.getInstance(this).getAppRamList()) {
            if (info.isSelfBoot) {
                startList.add(info);
            }
        }
        if (startList.size() == 0) {
            main_power_button.setVisibility(View.GONE);
        } else {

            power_icon.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(0).pkg));
            String text1 = getString(R.string.power_1, String.valueOf(startList.size())) + " ";
            SpannableString ss1 = new SpannableString(text1 + getString(R.string.power_4));
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3131")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            power_text.setText(ss1);
        }
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
        main_rotate_bad.setOnClickListener(onClickListener);
//        delete.setOnClickListener(onClickListener);
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
        nativeView = AdUtil.getNativeAdView(TAG_CLEAN, R.layout.native_ad_full);
        native_xiao = AdUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_2);
        if (ad_native_2 != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
            layout_ad.height = scrollView.getMeasuredHeight() - getResources().getDimensionPixelSize(R.dimen.d9);
            Log.e("success_ad", "hiegt=" + scrollView.getMeasuredHeight());
            ad_native_2.setLayoutParams(layout_ad);
            ad_native_2.addView(nativeView);
            if (animationEnd) {
                ad_native_2.setVisibility(View.VISIBLE);
                scrollView.isTouch = false;
                scrollView.smoothScrollToSlow(2000);
            }
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
//        rotate_set = AnimationUtils.loadAnimation(this, R.anim.set_success);
//        success_progress.startAnimation(rotate_set);
        final AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(success_progress, "scaleX", 1f, 1.3f, 1f);
        scaleX.setDuration(2400);
        scaleX.setRepeatCount(0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(success_progress, "scaleY", 1f, 1.3f, 1f);
        scaleY.setDuration(2400);
        scaleY.setRepeatCount(0);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(success_progress, "rotation", 0f, 360f);
        rotate.setDuration(600);
        rotate.setRepeatCount(3);
        set.setInterpolator(new LinearInterpolator());
//        rotate.start();
        set.playTogether(scaleX, scaleY, rotate);
        set.start();
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
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
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

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }
                });
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
                        ad_native_2.setVisibility(View.VISIBLE);
                        scrollView.isTouch = false;
                        scrollView.smoothScrollToSlow(2000);
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
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE, true);
                    UtilGp.rate(SuccessActivity.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_bad:
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_power_button:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入深度清理", "", 1);
                    jumpTo(PowerActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(FileLajiActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(NeicunAvtivity.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button:
                    AdUtil.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(CpuCoolingActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_file_button:
                    if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入文件管理", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.FILE_CLEAN, true);
                    jumpTo(FileManagerActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_gboost_button:
                    if (TextUtils.equals("Gboost", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.GBOOST_CLEAN, true);
                    jumpTo(DyxGboostActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入相似图片", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.PHOTO_CLEAN, true);
                    jumpTo(PictureActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_notifi_button:
                    AdUtil.track("完成页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(SuccessActivity.this)) {
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
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (Util.isNotificationListenEnabled(SuccessActivity.this)) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myHandler.removeCallbacks(runnable);
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
