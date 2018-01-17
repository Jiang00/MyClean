package com.security.cleaner.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
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

import com.security.mcleaner.manager.CleanManager;
import com.security.mcleaner.entity.JunkInfo;
import com.security.mcleaner.mutil.PreData;
import com.security.mcleaner.mutil.Util;
import com.android.client.AndroidSdk;
import com.security.cleaner.R;
import com.security.cleaner.myview.SlowScrollView;
import com.security.cleaner.utils.AdUtil;
import com.security.cleaner.utils.Constant;
import com.security.cleaner.utils.UtilGp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/6.
 */

public class SuccessActivity extends BaseActivity {
    LinearLayout main_cooling_button;
    LinearLayout main_ram_button;
    LinearLayout main_junk_button;
    LinearLayout main_gboost_button;
    TextView title_name;
    FrameLayout title_left;
    LinearLayout main_rotate_all;
    ImageView main_rotate_cha;
    LinearLayout main_notifi_button;
    LinearLayout main_file_button;
    TextView success_clean_size;
    ImageView success_clean_icon;
    //    DrawHookView success_drawhook;
    SlowScrollView scrollView;
    LinearLayout success_title;
    LinearLayout main_picture_button;
    TextView main_rotate_bad;
    //    ImageView delete;
    FrameLayout success_dong;
    ImageView xing_1, xing_2, xing_3;
    LinearLayout ll_ad_xiao;
    ImageView power_icon;
    TextView power_text;
    TextView main_rotate_good;
    LinearLayout ad_title;

    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;

    private Handler myHandler;
    private String TAG_CLEAN = "vector_success";
    private String TAG_CLEAN_2 = "vector_success_2";
    private Animation rotate_set;

    private boolean haveAd;
    private boolean animationEnd;
    private MyApplication cleanApplication;
    private AnimatorSet set;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        success_clean_icon = (ImageView) findViewById(R.id.success_clean_icon);
//        success_drawhook = (DrawHookView) findViewById(R.id.success_drawhook);
        scrollView = (SlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_rotate_cha = (ImageView) findViewById(R.id.main_rotate_cha);
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
        success_dong = (FrameLayout) findViewById(R.id.success_dong);
        xing_1 = (ImageView) findViewById(R.id.xing_1);
        xing_2 = (ImageView) findViewById(R.id.xing_2);
        xing_3 = (ImageView) findViewById(R.id.xing_3);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
        success_title = (LinearLayout) findViewById(R.id.success_title);
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
            main_file_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
            main_picture_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("junkClean", getIntent().getStringExtra("from"))) {
            main_notifi_button.setVisibility(View.GONE);
            main_junk_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
            main_picture_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("allJunk", getIntent().getStringExtra("from"))) {
            main_notifi_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_junk_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
            main_file_button.setVisibility(View.GONE);
            main_notifi_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("power", getIntent().getStringExtra("from")) || TextUtils.equals("GBoost", getIntent().getStringExtra("from"))) {
            main_notifi_button.setVisibility(View.GONE);
            main_ram_button.setVisibility(View.GONE);
            main_gboost_button.setVisibility(View.GONE);
        } else if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
            main_file_button.setVisibility(View.GONE);
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
            success_clean_icon.setImageResource(R.mipmap.success_ram_title);
        } else if (sizeJ > 0) {
            success_clean_icon.setImageResource(R.mipmap.success_junk_title);
            success_clean_size.setText(getString(R.string.success_2, Util.convertStorage(sizeJ, true)));
        } else if (count > 0) {
            success_clean_size.setText(getString(R.string.power_1, String.valueOf(count)) + " ");
            success_clean_icon.setImageResource(R.mipmap.success_ram_title);
        } else if (sizeF > 0) {
            success_clean_size.setText(getString(R.string.success_7, Util.convertStorage(sizeF, true)));
            success_clean_icon.setImageResource(R.mipmap.success_file_title);
        } else if (num > 0) {
            success_clean_size.setText(getString(R.string.success_6, num + ""));
            success_clean_icon.setImageResource(R.mipmap.success_file_title);
        } else if (wendu > 0) {
            success_clean_size.setText(getString(R.string.success_5, wendu + "℃"));
            title_name.setText(R.string.cooling_succ);
            success_clean_icon.setImageResource(R.mipmap.success_cooling_title);
        } else if (sizePic > 0) {
            success_clean_size.setText(getString(R.string.success_4, sizePic + ""));
            success_clean_icon.setImageResource(R.mipmap.success_file_title);
        } else {
            success_clean_size.setText(getText(R.string.success_normal));
            success_clean_icon.setImageResource(R.mipmap.success_file_title);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) != 1) {
            main_notifi_button.setVisibility(View.GONE);
        }

        if (PreData.getDB(this, Constant.FILE_KAIGUAN, 1) != 1) {
            main_file_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.PHOTO_KAIGUAN, 1) != 1) {
            main_picture_button.setVisibility(View.GONE);
        }
        initAnimation();
//        success_drawhook.setListener(new DrawHookView.DrawHookListener() {
//
//            @Override
//            public void duogouSc() {
//                if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS, 0) == 1) {
//                    AndroidSdk.showFullAd(AdUtil.DEFAULT);
//                }
//                startSecondAnimation();
//                success_drawhook.setListener(null);
//            }
//        });
        if (PreData.getDB(this, Constant.IS_ROTATE, false) || PreData.getDB(this, Constant.IS__SUCCESS_ROTATE, false)) {
            main_rotate_all.setVisibility(View.GONE);
        }

        addListener();

        shendu();
        if (PreData.getDB(this, Constant.FULL_SUCCESS_NATIVE, 0) == 1) {
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
        for (JunkInfo info : CleanManager.getInstance(this).getAppRamList()) {
            if (info.isSelfBoot) {
                startList.add(info);
            }
        }
//        if (startList.size() == 0) {
//            main_power_button.setVisibility(View.GONE);
//        } else {
//            power_icon.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(0).pkg));
//            String text1 = getString(R.string.power_1, String.valueOf(startList.size())) + " ";
//            SpannableString ss1 = new SpannableString(text1 + getString(R.string.power_4));
//            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3131")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            power_text.setText(ss1);
//        }
    }


    private void initAnimation() {
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startFirstAnimation();
            }
        }, 500);
    }

    private void addListener() {
        title_left.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        main_rotate_cha.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);

    }

    private void addAd() {
//        nativeView = AdUtil.getNativeAdView(TAG_CLEAN, R.layout.native_ad_full);

//        if (ad_native_2 != null && nativeView != null) {
//            ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
//            layout_ad.height = scrollView.getMeasuredHeight() - getResources().getDimensionPixelSize(R.dimen.d9);
//            Log.e("success_ad", "hiegt=" + scrollView.getMeasuredHeight());
//            ad_native_2.setLayoutParams(layout_ad);
//            haveAd = true;
//            ad_native_2.addView(nativeView);
//            if (animationEnd) {
//                ad_native_2.setVisibility(View.VISIBLE);
//                scrollView.isTouch = false;
//                scrollView.smoothScrollToSlow(2000);
//            }
//        }
        if (TextUtils.equals(PreData.getDB(this, Constant.SUCCESS_NATIVVE_SIZE, AdUtil.SIZE_SMALL), AdUtil.SIZE_SMALL)) {
            native_xiao = AdUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_3);
        } else {
            native_xiao = AdUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_2);
        }
        if (ll_ad_xiao != null && native_xiao != null) {
            ll_ad_xiao.addView(native_xiao);
            ll_ad_xiao.setVisibility(View.VISIBLE);
        } else {
            AdUtil.showBanner();
            findViewById(R.id.banner_ad).setVisibility(View.VISIBLE);
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
        set = new AnimatorSet();
        ObjectAnimator scaleX_f = ObjectAnimator.ofFloat(success_dong, "scaleX", 0, 1f);
        scaleX_f.setDuration(600);
        ObjectAnimator scaleY_f = ObjectAnimator.ofFloat(success_dong, "scaleY", 0, 1f);
        scaleY_f.setDuration(600);
        set.play(scaleX_f).with(scaleY_f);
        set.start();
        success_dong.setVisibility(View.VISIBLE);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                set = new AnimatorSet();
                ObjectAnimator animator_1 = ObjectAnimator.ofFloat(xing_1, "scaleX", 0, 1f);
                animator_1.setDuration(300);
                ObjectAnimator animator_2 = ObjectAnimator.ofFloat(xing_1, "scaleY", 0, 1f);
                animator_2.setDuration(300);
                ObjectAnimator animator_3 = ObjectAnimator.ofFloat(xing_1, View.ROTATION, 0, 360);
                animator_3.setDuration(300);
                ObjectAnimator animator_4 = ObjectAnimator.ofFloat(xing_2, "scaleX", 0, 1f);
                animator_4.setDuration(500);
                ObjectAnimator animator_5 = ObjectAnimator.ofFloat(xing_2, "scaleY", 0, 1f);
                animator_5.setDuration(500);
                ObjectAnimator animator_6 = ObjectAnimator.ofFloat(xing_2, View.ROTATION, 0, 360);
                animator_6.setDuration(500);
                ObjectAnimator animator_7 = ObjectAnimator.ofFloat(xing_3, "scaleX", 0, 1f);
                animator_7.setDuration(700);
                ObjectAnimator animator_8 = ObjectAnimator.ofFloat(xing_3, "scaleY", 0, 1f);
                animator_8.setDuration(700);
                ObjectAnimator animator_9 = ObjectAnimator.ofFloat(xing_3, View.ROTATION, 0, 360);
                animator_9.setDuration(700);
//                set.play(animator_1).with(animator_2).with(animator_3);
//                set.play(animator_4).with(animator_5).with(animator_6);
//                set.play(animator_7).with(animator_8).with(animator_9);
//                set.play(animator_4).after(animator_1);
//                set.play(animator_7).after(animator_4);
                set.playTogether(animator_1, animator_2, animator_3, animator_4, animator_5, animator_6, animator_7, animator_8, animator_9);
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        set = new AnimatorSet();
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(success_dong, "scaleX", 1, 0f);
                        scaleX.setDuration(600);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(success_dong, "scaleY", 1, 0f);
                        scaleY.setDuration(600);
                        set.setInterpolator(new LinearInterpolator());
                        set.play(scaleX).with(scaleY);
                        set.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                Log.e("adadad", "succe====");
                                AndroidSdk.showFullAd(AdUtil.DEFAULT);
                                startSecondAnimation();
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }
                        });
                        set.start();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }
                });
                set.start();
                xing_1.setVisibility(View.VISIBLE);
                xing_2.setVisibility(View.VISIBLE);
                xing_3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });


    }

    private void startSecondAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_success);
        success_title.startAnimation(animation);
        success_title.setVisibility(View.VISIBLE);
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
                    PreData.putDB(SuccessActivity.this, Constant.IS__SUCCESS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(JunkFileActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(ARamAvtivity.class);
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
                    jumpTo(FilesManagerActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_gboost_button:
                    if (TextUtils.equals("Gboost", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.GBOOST_CLEAN, true);
                    jumpTo(GameGboostActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入相似图片", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.PHOTO_CLEAN, true);
                    jumpTo(SimilarPhotoActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_notifi_button:
                    AdUtil.track("完成页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(SuccessActivity.this) || !PreData.getDB(SuccessActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SuccessActivity.this, NotifiPermissActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    } else {
                        Intent intent6 = new Intent(SuccessActivity.this, NotificationActivity.class);
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
                Intent intent = new Intent(SuccessActivity.this, NotificationActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SuccessActivity.this, NotifiPermissActivity.class);
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
        if (set != null) {
            set.removeAllListeners();
            set.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        AdUtil.closeBanner();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }

}
