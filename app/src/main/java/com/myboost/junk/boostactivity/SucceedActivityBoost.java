package com.myboost.junk.boostactivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.myboost.clean.core.CleanManager;
import com.myboost.clean.entity.JunkInfo;
import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.customviewboost.ImageAccessorBoost;
import com.myboost.junk.customviewboost.MainRoundViewBoost;
import com.myboost.junk.customviewboost.BoostSlowScrollView;
import com.myboost.junk.customviewboost.RenderingView;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.boosttools.UtilGpBoost;
import com.myboost.junk.boosttools.SetAdUtilPrivacy;
import com.twee.module.tweenengine.Tween;
import com.twee.module.tweenengine.TweenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/6.
 */

public class SucceedActivityBoost extends BaseActivity {
    private String TAG_CLEAN = "flashclean_success";
    private String TAG_CLEAN_2 = "flashclean_success_2";
    RelativeLayout main_gboost_button;
    FrameLayout title_left;
    TextView main_rotate_good;
    ObjectAnimator animator1, animator2, animator3, animator4, animator5, animator6;
    FrameLayout success_animator;
    AnimatorSet animSet, animSet1;
    BoostSlowScrollView scrollView;
    long endStart = -1;
    RelativeLayout main_ram_button;
    private boolean haveAd;
    private boolean animationEnd;
    private MyApplication cleanApplication;
    RelativeLayout main_junk_button;
    TextView title_name;
    MainRoundViewBoost mainRoundView;
    ImageView success_eye_leght, success_eye_right, success_eye_right1;
    RelativeLayout main_power_button;
    RelativeLayout main_notifi_button;
    RelativeLayout main_cooling_button;
    ObjectAnimator objectAnimator;
    int sizeInt;
    long sizeInt1;
    private TweenManager tweenManager;
    private boolean istween;
    private Handler myHandler;
    LinearLayout main_rotate_all;
    TextView success_clean_size;
    TextView success_textview;
    TextView main_rotate_bad;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;
    RenderingView success_mouth;
    RelativeLayout main_picture_button;
    RelativeLayout main_file_button;
    ImageView rotate_cha;
    private Animation rotate_set;
    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;

    private void addAd() {
        nativeView = SetAdUtilPrivacy.getNativeAdView(TAG_CLEAN, R.layout.native_ad_full);
        native_xiao = SetAdUtilPrivacy.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_2);
        if (ad_native_2 != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ad_native_2.getLayoutParams();
            layout_ad.height = scrollView.getMeasuredHeight() - getResources().getDimensionPixelSize(R.dimen.d9);
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
        if (ll_ad_xiao != null && native_xiao != null) {
            ll_ad_xiao.addView(native_xiao);
            ll_ad_xiao.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_success);

        //深度清理
        if (PreData.getDB(this, BoostMyConstant.POWERACTIVITY, 1) == 0) {
            main_power_button.setVisibility(View.GONE);
        }
        //文件
        if (PreData.getDB(this, BoostMyConstant.FILEACTIVITY, 1) == 0) {
            main_file_button.setVisibility(View.GONE);
        }
        //通知栏
        if (PreData.getDB(this, BoostMyConstant.NOTIFIACTIVITY, 1) == 0) {
            main_notifi_button.setVisibility(View.GONE);
        }

        tweenManager = new TweenManager();
        Tween.registerAccessor(ImageView.class, new ImageAccessorBoost());
        istween = true;
        setAnimationThread();
        myHandler = new Handler();
        if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
            success_animator.setVisibility(View.GONE);
            success_textview.setVisibility(View.GONE);
            //电池降温跳过动画
            //广告
            if (PreData.getDB(SucceedActivityBoost.this, BoostMyConstant.FULL_SUCCESS, 0) == 1) {
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
            }
            //动画结束换内容的
            startSecondAnimation();
        } else {
            success_animator.setVisibility(View.VISIBLE);
            success_textview.setVisibility(View.VISIBLE);
            initAnimation();

            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //左眼睛
                    animator1 = ObjectAnimator.ofFloat(success_eye_leght, "alpha", 0f, 1f);
                    //右眼睛
                    animator2 = ObjectAnimator.ofFloat(success_eye_right1, "alpha", 0f, 1f);
                    animSet = new AnimatorSet();
                    animSet.setDuration(1000);
                    animSet.play(animator1).with(animator2);
                    animSet.start();
                    animSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //右眼睛
                            animSet1 = new AnimatorSet();
                            animator3 = ObjectAnimator.ofFloat(success_eye_right1, "alpha", 1f, 0f);
                            animator4 = ObjectAnimator.ofFloat(success_eye_right, "alpha", 0f, 1f);
                            animSet.setDuration(1000);
                            animSet1.play(animator4).after(animator3);
                            animSet1.start();
                            success_mouth.setVisibility(View.VISIBLE);
                            success_mouth.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.success_7));
                            myHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //广告
                                    if (PreData.getDB(SucceedActivityBoost.this, BoostMyConstant.FULL_SUCCESS, 0) == 1) {
                                        AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                                    }
                                    //动画结束换内容的
                                    startSecondAnimation();
                                }
                            }, 1500);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            }, 1100);
        }
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
        } else if (TextUtils.equals("appClean", getIntent().getStringExtra("from"))) {
            main_picture_button.setVisibility(View.GONE);
            main_notifi_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
        } else {
            main_picture_button.setVisibility(View.GONE);
            main_notifi_button.setVisibility(View.GONE);
            main_cooling_button.setVisibility(View.GONE);
        }

        //所有垃圾清理
        long sizeAll = getIntent().getLongExtra("size", 0);
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
        //卸载
        long sizeApp = getIntent().getLongExtra("sizeApp", 0);
        if (sizeR > 0) {
            startSizeAni(sizeR, null);
            success_clean_size.setText(getString(R.string.success_3, MyUtils.convertStorage(sizeR, true)));
        } else if (sizeAll > 0) {
            startSizeAni(sizeAll, null);
            success_clean_size.setText(getString(R.string.success_2, MyUtils.convertStorage(sizeAll, true)));
        } else if (sizeJ > 0) {
            startSizeAni(sizeJ, null);
            success_clean_size.setText(getString(R.string.success_2, MyUtils.convertStorage(sizeJ, true)));
        } else if (count > 0) {
            startSizeAni(count, "app");
            success_clean_size.setText(getString(R.string.power_1, String.valueOf(count)) + " ");
        } else if (sizeF > 0) {
            startSizeAni(sizeF, null);
            success_clean_size.setText(getString(R.string.success_7, MyUtils.convertStorage(sizeF, true)));
        } else if (num > 0) {
            startSizeAni(num, "noti");
            success_clean_size.setText(getString(R.string.success_6, num + ""));
        } else if (wendu > 0) {
            success_clean_size.setText(getString(R.string.success_5, wendu + "℃"));
        } else if (sizePic > 0) {
            startSizeAni(sizePic, "picture");
            success_clean_size.setText(getString(R.string.success_4, sizePic + ""));
        } else if (sizeApp > 0) {
            startSizeAni(sizeApp, null);
            success_clean_size.setText(getString(R.string.success_xiezai, MyUtils.convertStorage(sizeApp, true)));
        } else {
            startSizeAni(0, null);
            success_clean_size.setText(getText(R.string.success_normal));
        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }

        if (PreData.getDB(this, BoostMyConstant.IS_ROTATE, false)) {
            main_rotate_all.setVisibility(View.GONE);
        }

        addListener();
        shendu();
        if (PreData.getDB(this, BoostMyConstant.FULL_SUCCESS, 0) == 1) {

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
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        mainRoundView = (MainRoundViewBoost) findViewById(R.id.main_progress);
        success_textview = (TextView) findViewById(R.id.success_textview);
        scrollView = (BoostSlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_power_button = (RelativeLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (RelativeLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (RelativeLayout) findViewById(R.id.main_file_button);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_gboost_button = (RelativeLayout) findViewById(R.id.main_gboost_button);
        main_picture_button = (RelativeLayout) findViewById(R.id.main_picture_button);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        rotate_cha = (ImageView) findViewById(R.id.main_rotate_close);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
        success_mouth = (RenderingView) findViewById(R.id.success_mouth);
        success_eye_leght = (ImageView) findViewById(R.id.success_eye_leght);
        success_eye_right = (ImageView) findViewById(R.id.success_eye_right);
        success_eye_right1 = (ImageView) findViewById(R.id.success_eye_right1);
        success_animator = (FrameLayout) findViewById(R.id.success_animator);
    }

    private void addListener() {
        title_left.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_rotate_bad.setOnClickListener(onClickListener);
        rotate_cha.setOnClickListener(onClickListener);
        main_power_button.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);

    }

    public void startSizeAni(final long size, final String cleanName) {

        String[] str = MyUtils.convertStorage(size, false).split("\\.");
        if ("0".equals(str[0])) {
            sizeInt = (int) size;
            sizeInt1 = size;
        } else {
            sizeInt = Integer.parseInt(str[0]);
        }
        String strDanWei = MyUtils.convertStorage(size, true);
        if ("M".equals(strDanWei.substring(strDanWei.length() - 1, strDanWei.length()))) {
            sizeInt *= 1024;
        } else if ("G".equals(strDanWei.substring(strDanWei.length() - 1, strDanWei.length()))) {
            sizeInt = sizeInt * 1024 * 1024;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (objectAnimator != null) {
                    objectAnimator.pause();
                }
            }
        }).start();
    }

    private void initAnimation() {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                startFirstAnimation();
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
                    SetAdUtilPrivacy.track("完成页面", "点击好评good", "", 1);
                    PreData.putDB(SucceedActivityBoost.this, BoostMyConstant.IS_ROTATE, true);
                    UtilGpBoost.rate(SucceedActivityBoost.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_bad:
                    SetAdUtilPrivacy.track("完成页面", "点击好评bad", "", 1);
                    PreData.putDB(SucceedActivityBoost.this, BoostMyConstant.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_close:
                    SetAdUtilPrivacy.track("完成页面", "点击好评叉号", "", 1);
                    PreData.putDB(SucceedActivityBoost.this, BoostMyConstant.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_power_button:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtilPrivacy.track("完成页面", "点击进入深度清理", "", 1);
                    jumpTo(BoostDeepingActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button:
                    SetAdUtilPrivacy.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(BoostRubbishActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    SetAdUtilPrivacy.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(MemoryAvtivityBoost.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button:
                    SetAdUtilPrivacy.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(BatteriesActivityBoost.class);
                    onBackPressed();
                    break;
                case R.id.main_file_button:
                    if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtilPrivacy.track("完成页面", "点击进入文件管理", "", 1);
                    PreData.putDB(SucceedActivityBoost.this, BoostMyConstant.FILE_CLEAN, true);
                    jumpTo(BoostFileManagerActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_gboost_button:
                    if (TextUtils.equals("Gboost", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtilPrivacy.track("完成页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(SucceedActivityBoost.this, BoostMyConstant.GBOOST_CLEAN, true);
                    jumpTo(BoostGoodGameActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtilPrivacy.track("完成页面", "点击进入相似图片", "", 1);
                    PreData.putDB(SucceedActivityBoost.this, BoostMyConstant.PHOTO_CLEAN, true);
                    jumpTo(BoostPictActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_notifi_button:
                    SetAdUtilPrivacy.track("完成页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(SucceedActivityBoost.this, BoostMyConstant.NOTIFI_CLEAN, true);
                    if (!PreData.getDB(SucceedActivityBoost.this, BoostMyConstant.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(SucceedActivityBoost.this)) {
                        Intent intent6 = new Intent(SucceedActivityBoost.this, NotifingAnimationActivityBoost.class);
                        startActivity(intent6);
                        onBackPressed();
                    } else {
                        Intent intent6 = new Intent(SucceedActivityBoost.this, BoostNotifingActivity.class);
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
            if (MyUtils.isNotificationListenEnabled(SucceedActivityBoost.this)) {
                PreData.putDB(SucceedActivityBoost.this, BoostMyConstant.KEY_NOTIFI, true);
                Intent intent = new Intent(SucceedActivityBoost.this, BoostNotifingActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SucceedActivityBoost.this, NotifingAnimationActivityBoost.class);
                startActivity(intent);
            }
            onBackPressed();
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
                }, 1500);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
    }

    public void startFirstAnimation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 写子线程中的操作
                for (int i = 1; i <= 100; i++) {
                    mainRoundView.setProgress(i);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
