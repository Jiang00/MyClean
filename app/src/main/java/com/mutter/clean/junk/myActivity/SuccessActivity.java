package com.mutter.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mutter.clean.core.CleanManager;
import com.mutter.clean.entity.JunkInfo;
import com.mutter.clean.util.LoadManager;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myview.SlowScrollView;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.UtilGp;
import com.sample.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

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
    LinearLayout main_power_button;
    TextView success_clean_2;
    LinearLayout main_cooling_button;
    TextView title_name;
    TextView success_clean_size;
    LinearLayout main_ram_button;
    LinearLayout main_junk_button;
    LinearLayout main_picture_button;
    TextView power_text;
    LottieAnimationView lottie_power;
    LinearLayout success_ani_1;

    //    ImageView delete;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;
    FrameLayout ad_fl;

    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;

    private Handler myHandler;
    private String TAG_CLEAN = "mutter_success";
    private String TAG_CLEAN_2 = "mutter_success_2";
    private String TAG_TITLE = "mutter_icon";


    private boolean haveAd;
    private boolean animationEnd;
    private AnimatorSet animatorSet;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        success_clean_2 = (TextView) findViewById(R.id.success_clean_2);
        scrollView = (SlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_power_button = (LinearLayout) findViewById(R.id.main_power_button);
        main_cooling_button = (LinearLayout) findViewById(R.id.main_cooling_button);
        main_ram_button = (LinearLayout) findViewById(R.id.main_ram_button);
        main_junk_button = (LinearLayout) findViewById(R.id.main_junk_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        power_text = (TextView) findViewById(R.id.power_text);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        main_rotate_cha = (ImageView) findViewById(R.id.main_rotate_cha);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
        ad_fl = (FrameLayout) findViewById(R.id.ad_fl);
        lottie_power = (LottieAnimationView) findViewById(R.id.lottie_power);
        success_ani_1 = (LinearLayout) findViewById(R.id.success_ani_1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_success);
        myHandler = new Handler();
        if (getIntent().getStringExtra("name") != null) {
            title_name.setText(getIntent().getStringExtra("name"));
        } else {
            title_name.setText(R.string.success_title);
        }

        if (TextUtils.equals("ramSpeed", getIntent().getStringExtra("from"))) {
            main_ram_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("junkClean", getIntent().getStringExtra("from"))) {
            main_junk_button.setVisibility(View.GONE);
            main_picture_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
            main_ram_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("power", getIntent().getStringExtra("from")) || TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
            main_power_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
            main_picture_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
        }


        //ram加速
        long sizeR = getIntent().getLongExtra("sizeR", 0);
        //垃圾清理
        long sizeJ = getIntent().getLongExtra("sizeJ", 0);
        //相似图片清理
        int sizePic = getIntent().getIntExtra("sizePic", 0);
        //强力清理
        int count = getIntent().getIntExtra("count", 0);
        //降温
        int wendu = getIntent().getIntExtra("wendu", 0);
        // TODO: 2018/1/4
        //标题字
//        if (sizeR > 0) {
//            success_clean_size.setText(getString(R.string.success_3, Util.convertStorage(sizeR, true)));
//        } else if (sizeJ > 0) {
//            success_clean_size.setText(getString(R.string.success_2, Util.convertStorage(sizeJ, true)));
//        } else if (count > 0) {
//            success_clean_size.setText(getString(R.string.power_1, String.valueOf(count)) + " ");
//        } else if (sizeF > 0) {
//            success_clean_size.setText(getString(R.string.success_7, Util.convertStorage(sizeF, true)));
//        } else if (num > 0) {
//            success_clean_size.setText(getString(R.string.success_6, num + ""));
//        } else if (wendu > 0) {
//            success_clean_size.setText(getString(R.string.success_5, wendu + "℃"));
//        } else if (sizePic > 0) {
//            success_clean_size.setText(getString(R.string.success_4, sizePic + ""));
//        } else {
//            success_clean_size.setText(getText(R.string.success_normal));
//            success_clean_2.setVisibility(View.GONE);
//        }

        initAnimation();
        addListener();
//        shendu();
        if (PreData.getDB(this, Constant.FULL_SUCCESS, 0) == 1) {
        } else {
            myHandler.postDelayed(new Runnable() {
                public void run() {
                    addAd();
                }
            }, 1000);

        }
        if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS_NATIVE, 0) == 1) {
            native_xiao = AdUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_6);
            if (ll_ad_xiao != null && native_xiao != null) {
                ll_ad_xiao.addView(native_xiao);
                ad_fl.setVisibility(View.VISIBLE);
//                AdUtil.startBannerAnimation(SuccessActivity.this, ad_fl);
            }
        }

    }

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd("mutter_start_native")) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout("mutter_start_native", layout, null);
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

    private void addListener() {
        title_left.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_rotate_cha.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);

    }

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
            String text1 = getString(R.string.power_1, String.valueOf(startList.size())) + " ";
            SpannableString ss1 = new SpannableString(text1 + getString(R.string.power_4));
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3131")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            power_text.setText(ss1);
        }
    }


    private void initAnimation() {
        animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(success_ani_1, View.SCALE_Y, 0, 1);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(success_ani_1, View.SCALE_X, 0, 1);
        animatorSet.play(objectAnimator_1).with(objectAnimator_2);
        animatorSet.setDuration(1500);
        animatorSet.start();
        success_ani_1.setVisibility(View.VISIBLE);
        lottie_power.playAnimation();
        lottie_power.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS, 0) == 1) {
                    AndroidSdk.showFullAd(AdUtil.FULL_DEFAULT);
                }
                startSecondAnimation();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
//        myHandler.postDelayed(runnable, 3000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS, 0) == 1) {
                AndroidSdk.showFullAd(AdUtil.FULL_DEFAULT);
            }
            startSecondAnimation();
        }
    };

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
                case R.id.main_power_button:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入深度清理", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.DEEP_CLEAN, true);
                    jumpTo(PowerActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(CleanActivity.class);
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
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入相似图片", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.PHOTO_CLEAN, true);
                    jumpTo(SimilarActivity.class);
                    onBackPressed();
                    break;
            }
        }
    };


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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }

}
