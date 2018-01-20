package com.eifmobi.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eifmobi.clean.junk.myview.BubbleLineLayout;
import com.eifmobi.clean.util.PreData;
import com.eifmobi.clean.util.Util;
import com.android.client.AndroidSdk;
import com.eifmobi.clean.junk.R;
import com.eifmobi.clean.junk.myview.SlowScrollView;
import com.eifmobi.clean.junk.util.AdUtil;
import com.eifmobi.clean.junk.util.Constant;
import com.eifmobi.clean.junk.util.UtilGp;

/**
 * Created by on 2017/3/6.
 */

public class SuccessActivity extends BaseActivity {
    FrameLayout title_left;
    SlowScrollView scrollView;
    LinearLayout main_rotate_all;
    TextView main_rotate_good;
    TextView main_rotate_bad;
    ImageView main_rotate_cha;
    LinearLayout main_cooling_button;
    TextView title_name;
    TextView success_clean_size;
    LinearLayout main_ram_button;
    LinearLayout main_junk_button;
    BubbleLineLayout clean_bubble;
    ImageView clean_huojian;

    //    ImageView delete;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;
    FrameLayout ad_fl;
    TextView success_c;

    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;

    private Handler myHandler;
    private String TAG_CLEAN = "_success";
    private String TAG_CLEAN_2 = "r_success_2";


    private boolean haveAd;
    private boolean animationEnd;
    private AnimatorSet animatorSet;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        scrollView = (SlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_cooling_button = (LinearLayout) findViewById(R.id.main_cooling_button);
        main_ram_button = (LinearLayout) findViewById(R.id.main_ram_button);
        main_junk_button = (LinearLayout) findViewById(R.id.main_junk_button);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_rotate_cha = (ImageView) findViewById(R.id.main_rotate_cha);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
        ad_fl = (FrameLayout) findViewById(R.id.ad_fl);
        success_c = (TextView) findViewById(R.id.success_c);
        clean_bubble = (BubbleLineLayout) findViewById(R.id.clean_bubble);
        clean_huojian = (ImageView) findViewById(R.id.clean_huojian);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_success);
        clean_bubble.pause();
        myHandler = new Handler();
        if (getIntent().getStringExtra("name") != null) {
            title_name.setText(getIntent().getStringExtra("name"));
        } else {
            title_name.setText(R.string.success_title);
        }

        if (TextUtils.equals("ramSpeed", getIntent().getStringExtra("from"))) {
            main_ram_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("junkClean", getIntent().getStringExtra("from"))) {
            main_junk_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
            success_c.setText(R.string.cooling_succ);
            main_cooling_button.setVisibility(View.GONE);
        }


        //ram加速
        long sizeR = getIntent().getLongExtra("sizeR", 0);
        //垃圾清理
        long sizeJ = getIntent().getLongExtra("sizeJ", 0);
        //降温
        int wendu = getIntent().getIntExtra("wendu", 0);
        if (sizeR > 0) {
            success_clean_size.setText(getString(R.string.success_3, Util.convertStorage(sizeR, true)));
        } else if (sizeJ > 0) {
            success_clean_size.setText(getString(R.string.success_2, Util.convertStorage(sizeJ, true)));
        } else if (wendu > 0) {
            success_clean_size.setText(getString(R.string.success_5, wendu + "℃"));
        } else {
            success_clean_size.setText(getText(R.string.success_normal));
        }
        if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
            startSecondAnimation();
        } else {
            firstAnimation();
        }


        addListener();
        if (PreData.getDB(this, Constant.FULL_SUCCESS, 0) == 1) {
        } else {
            myHandler.postDelayed(new Runnable() {
                public void run() {
                    addAd();
                }
            }, 1000);
        }
        if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS_NATIVE, 0) == 1) {
            if (TextUtils.equals(PreData.getDB(SuccessActivity.this, Constant.SUCCESS_NATIVE_SIZE, AdUtil.NATIVE_SMALL), AdUtil.NATIVE_SMALL)) {
                native_xiao = AdUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_3);
            } else {
                native_xiao = AdUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_2);
            }
            if (ll_ad_xiao != null && native_xiao != null) {
                ll_ad_xiao.addView(native_xiao);
                AdUtil.startBannerAnimation(SuccessActivity.this, ad_fl);
            } else {
                AdUtil.showBanner();
            }
        }
    }


    private void addListener() {
        title_left.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_rotate_cha.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);

    }

    private void shendu() {
//            SpannableString ss1 = new SpannableString(text1 + getString(R.string.power_4));
//            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3131")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            power_text.setText(ss1);
    }


    private void firstAnimation() {
        clean_bubble.reStart();
        int w = (int) getResources().getDimension(R.dimen.d318);
        int h = (int) getResources().getDimension(R.dimen.d233);
        animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(clean_huojian, View.TRANSLATION_Y, h, 0.5f * h);
        objectAnimator_1.setDuration(500);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(clean_huojian, View.TRANSLATION_X, -w, -0.5f * w);
        objectAnimator_2.setDuration(500);
//        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(clean_huojian, View.TRANSLATION_Y, 0.5f * h, 0.5f * h + 5,
//                0.5f * h, 0.5f * h - 5, 0.5f * h);
        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(clean_huojian, View.TRANSLATION_Y, 0.5f * h, 0.2f * h);
        objectAnimator_3.setDuration(1500);
        objectAnimator_3.setInterpolator(new LinearInterpolator());
        ObjectAnimator objectAnimator_4 = ObjectAnimator.ofFloat(clean_huojian, View.TRANSLATION_X, -0.5f * w, -0.3f * w);
//        ObjectAnimator objectAnimator_4 = ObjectAnimator.ofFloat(clean_huojian, View.TRANSLATION_X, -0.5f * w, -0.5f * w + 5,
//                -0.5f * w, -0.5f * w - 5, -0.5f * w);
        objectAnimator_4.setDuration(1500);
        objectAnimator_4.setInterpolator(new LinearInterpolator());
        ObjectAnimator objectAnimator_5 = ObjectAnimator.ofFloat(clean_huojian, View.TRANSLATION_Y, 0.2f * h, -h);
        objectAnimator_5.setDuration(300);
        ObjectAnimator objectAnimator_6 = ObjectAnimator.ofFloat(clean_huojian, View.TRANSLATION_X, -0.3f * w, w);
        objectAnimator_6.setDuration(300);
        animatorSet.play(objectAnimator_1).with(objectAnimator_2);
        animatorSet.play(objectAnimator_3).with(objectAnimator_4);
        animatorSet.play(objectAnimator_5).with(objectAnimator_6);
        animatorSet.play(objectAnimator_3).after(objectAnimator_1);
        animatorSet.play(objectAnimator_5).after(objectAnimator_3);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                clean_bubble.pause();
                clean_bubble.destroy();
                startSecondAnimation();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
    }

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(TAG_CLEAN, R.layout.native_ad_full);

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


    private void startSecondAnimation() {
        clean_huojian.setVisibility(View.INVISIBLE);
        if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS, 0) == 1) {
            AndroidSdk.showFullAd(AdUtil.FULL_DEFAULT);
        }
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
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE, true);
                    UtilGp.rate(SuccessActivity.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_bad:
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_cha:
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE_SUCC, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(JunkActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(RamAvtivity.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button:
                    AdUtil.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(JiangwenActivity.class);
                    onBackPressed();
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
        AdUtil.closeBanner();
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
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }

}
