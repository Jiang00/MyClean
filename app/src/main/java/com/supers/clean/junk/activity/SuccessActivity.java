package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.android.common.EventBus;
import com.ivy.module.tweenengine.Tween;
import com.ivy.module.tweenengine.TweenEquations;
import com.ivy.module.tweenengine.TweenManager;
import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.UtilGp;
import com.supers.clean.junk.modle.entity.Contents;
import com.supers.clean.junk.myView.DrawHookView;
import com.supers.clean.junk.myView.ImageAccessor;
import com.supers.clean.junk.myView.SlowScrollView;

/**
 * Created by Ivy on 2017/3/6.
 */

public class SuccessActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView success_jiantou;
    TextView success_clean_size;
    DrawHookView success_drawhook;
    ImageView success_huojian;
    SlowScrollView scrollView;
    LinearLayout main_rotate_all;
    TextView main_rotate_bad;
    LinearLayout main_rotate_good;
    ImageView delete;
    ImageView success_progress;

    LinearLayout ad_native_2;
    private TextView tv_next;
    private ImageView iv_next;
    private View nativeView;

    private boolean isdoudong;
    private TweenManager tweenManager;
    private boolean istween;
    private Handler myHandler;
    private String TAG_CLEAN = "eos_success";
    private Animation rotate;

    private boolean haveAd;
    private boolean animationEnd;


    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        success_jiantou = (ImageView) findViewById(R.id.success_jiantou);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        success_drawhook = (DrawHookView) findViewById(R.id.success_drawhook);
        success_huojian = (ImageView) findViewById(R.id.success_huojian);
        scrollView = (SlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_rotate_good = (LinearLayout) findViewById(R.id.main_rotate_good);
        delete = (ImageView) findViewById(R.id.delete);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        success_progress = (ImageView) findViewById(R.id.success_progress);
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
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_ni);
        success_progress.startAnimation(rotate);
        title_name.setText(R.string.success_title);
        long size = getIntent().getLongExtra("size", 0);
        if (size > 0) {
            success_clean_size.setText(CommonUtil.getFileSize4(size) + " " + getText(R.string.success_cleaned));
        } else {
            success_clean_size.setText(getText(R.string.success_normal));
        }
        initAnimation();
        success_drawhook.setListener(new DrawHookView.DrawHookListener() {

            @Override
            public void duogouSc() {
                startSecondAnimation();
            }
        });
        if (PreData.getDB(this, Contents.IS_ROTATE, false)) {
            main_rotate_all.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Contents.FULL_SUCCESS, 0) == 1) {
            AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
        } else {
            addAd();
        }
        addListener();
        TranslateAnimation translate = new TranslateAnimation(0, 0, 10, 2);
        translate.setInterpolator(new AccelerateInterpolator());//OvershootInterpolator
        translate.setDuration(400);
        translate.setRepeatCount(-1);
        translate.setRepeatMode(Animation.REVERSE);
        success_jiantou.startAnimation(translate);
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
        main_rotate_bad.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);

    }

    private void addAd() {
        AndroidSdk.loadNativeAd(TAG_CLEAN, R.layout.native_ad_full, new ClientNativeAd.NativeAdLoadListener() {
            @Override
            public void onNativeAdLoadSuccess(View view) {
                Log.e("inf", "reload success1");
                nativeView = view;
                haveAd = true;
                ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
                layout_ad.height = scrollView.getMeasuredHeight();
                ad_native_2.setLayoutParams(layout_ad);
                ad_native_2.addView(nativeView);
                tv_next = (TextView) nativeView.findViewWithTag("ad_next");
                iv_next = (ImageView) nativeView.findViewWithTag("ad_iv_next");
                tv_next.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        reloadNativeAd();
                        tv_next.setVisibility(View.INVISIBLE);
                        iv_next.setVisibility(View.VISIBLE);
                        iv_next.startAnimation(rotate);
                    }
                });

                if (animationEnd) {
                    ad_native_2.setVisibility(View.VISIBLE);
                    scrollView.isTouch = false;
                    scrollView.smoothScrollToSlow(2000);
                }
            }

            @Override
            public void onNativeAdLoadFails() {
                Log.e("inf", "reload shibai1");
                haveAd = false;
                nativeView = null;
                ad_native_2.setVisibility(View.GONE);
            }
        });
    }

    public void reloadNativeAd() {
        AndroidSdk.reLoadNativeAd(TAG_CLEAN, nativeView, new ClientNativeAd.NativeAdLoadListener() {
            @Override
            public void onNativeAdLoadSuccess(View view) {
                Log.e("inf", "reload success");
                tv_next.setVisibility(View.VISIBLE);
                iv_next.setVisibility(View.GONE);
                iv_next.clearAnimation();
                view.findViewWithTag("ad_next").setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reloadNativeAd();
                        tv_next.setVisibility(View.INVISIBLE);
                        iv_next.setVisibility(View.VISIBLE);
                        iv_next.startAnimation(rotate);
                    }
                });
            }

            @Override
            public void onNativeAdLoadFails() {
                Log.e("inf", "reload fails");
                tv_next.setVisibility(View.VISIBLE);
                iv_next.setVisibility(View.GONE);
                iv_next.clearAnimation();
            }
        });
    }

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
                case R.id.main_rotate_bad:
                    PreData.putDB(SuccessActivity.this, Contents.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_good:
                    PreData.putDB(SuccessActivity.this, Contents.IS_ROTATE, true);
                    UtilGp.openPlayStore(SuccessActivity.this, SuccessActivity.this.getPackageName());
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.delete:
                    main_rotate_all.setVisibility(View.GONE);
                    break;
            }
        }
    };

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
