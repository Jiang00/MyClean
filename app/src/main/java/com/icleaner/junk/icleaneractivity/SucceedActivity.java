package com.icleaner.junk.icleaneractivity;

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
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.icleaner.clean.utils.MyUtils;
import com.icleaner.junk.mytools.MyConstant;
import com.icleaner.junk.mytools.SetAdUtil;
import com.twee.module.tweenengine.Tween;
import com.twee.module.tweenengine.TweenManager;
import com.icleaner.clean.core.CleanManager;
import com.icleaner.clean.entity.JunkInfo;
import com.icleaner.junk.R;
import com.icleaner.junk.mytools.MUtilGp;
import com.icleaner.junk.mycustomview.DrawHookView;
import com.icleaner.junk.mycustomview.ImageAccessor;
import com.icleaner.junk.mycustomview.MainRoundView;
import com.icleaner.junk.mycustomview.MySlowScrollView;
import com.icleaner.junk.mycustomview.Success_CircleWaveView;
import com.icleaner.clean.utils.LoadManager;
import com.icleaner.clean.utils.PreData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/6.
 */

public class SucceedActivity extends BaseActivity {
    LinearLayout main_gboost_button;
    FrameLayout title_left;
    LinearLayout main_rotate_all;
    LinearLayout main_power_button;
    LinearLayout main_notifi_button;
    LinearLayout main_cooling_button;
    LinearLayout main_ram_button;
    private boolean haveAd;
    private boolean animationEnd;
    private MyApplication cleanApplication;
    LinearLayout main_junk_button;
    TextView title_name;
    ImageView success_jiantou;
    MainRoundView mainRoundView;
    MySlowScrollView scrollView;
    LinearLayout main_picture_button;
    LinearLayout main_file_button;
    TextView success_clean_size;
    TextView success_textview;
    TextView main_rotate_bad;
    ImageView rotate_cha;
    //    ImageView success_progress;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;
    private String TAG_CLEAN = "icleaner_success";
    private String TAG_CLEAN_2 = "icleaner_success_2";
    private Animation rotate_set;
    Success_CircleWaveView success_cirlewaveview;
    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;
    DrawHookView success_drawhook;
    //    ImageView success_huojian;
    ImageView imageview_beijing;
    ImageView power_icon;
    TextView power_text;
    private boolean isdoudong;
    private TweenManager tweenManager;
    private boolean istween;
    private Handler myHandler;
    TextView main_rotate_good;


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
            success_clean_size.setText(getString(R.string.success_3, MyUtils.convertStorage(sizeR, true)));
        } else if (sizeJ > 0) {
            success_clean_size.setText(getString(R.string.success_2, MyUtils.convertStorage(sizeJ, true)));
        } else if (count > 0) {
            success_clean_size.setText(getString(R.string.power_1, String.valueOf(count)) + " ");
        } else if (sizeF > 0) {
            success_clean_size.setText(getString(R.string.success_7, MyUtils.convertStorage(sizeF, true)));
        } else if (num > 0) {
            success_clean_size.setText(getString(R.string.success_6, num + ""));
        } else if (wendu > 0) {
            success_clean_size.setText(getString(R.string.success_5, wendu + "℃"));
        } else if (sizePic > 0) {
            success_clean_size.setText(getString(R.string.success_4, sizePic + ""));
        } else {
            success_clean_size.setText(getText(R.string.success_normal));
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }
        initAnimation();
        success_drawhook.setListener(new DrawHookView.DrawHookListener() {

            @Override
            public void duogouSc() {
                if (PreData.getDB(SucceedActivity.this, MyConstant.FULL_SUCCESS, 0) == 1) {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
                success_textview.setVisibility(View.VISIBLE);
                startSecondAnimation();
                success_drawhook.setListener(null);
            }
        });
        if (PreData.getDB(this, MyConstant.IS_ROTATE, false)) {
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
        if (PreData.getDB(this, MyConstant.FULL_SUCCESS, 0) == 1) {

        } else {
            myHandler.postDelayed(new Runnable() {
                public void run() {
                    addAd();
                }
            }, 1000);

        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        imageview_beijing = (ImageView) findViewById(R.id.imageview_beijing);
        success_jiantou = (ImageView) findViewById(R.id.success_jiantou);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        success_drawhook = (DrawHookView) findViewById(R.id.success_drawhook);
//        success_huojian = (ImageView) findViewById(R.id.success_huojian);
        mainRoundView = (MainRoundView) findViewById(R.id.main_progress);
        success_textview = (TextView) findViewById(R.id.success_textview);
        scrollView = (MySlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_power_button = (LinearLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (LinearLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (LinearLayout) findViewById(R.id.main_file_button);
        main_cooling_button = (LinearLayout) findViewById(R.id.main_cooling_button);
        main_ram_button = (LinearLayout) findViewById(R.id.main_ram_button);
        success_cirlewaveview = (Success_CircleWaveView) findViewById(R.id.success_cirlewaveview);
        main_junk_button = (LinearLayout) findViewById(R.id.main_junk_button);
        main_gboost_button = (LinearLayout) findViewById(R.id.main_gboost_button);
        main_picture_button = (LinearLayout) findViewById(R.id.main_picture_button);
        power_text = (TextView) findViewById(R.id.power_text);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        rotate_cha = (ImageView) findViewById(R.id.rotate_cha);
        power_icon = (ImageView) findViewById(R.id.power_icon);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
//        success_progress = (ImageView) findViewById(R.id.success_progress);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
    }


    private void addAd() {
        nativeView = SetAdUtil.getNativeAdView(TAG_CLEAN, R.layout.native_ad_full);
        native_xiao = SetAdUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_2);
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

            power_icon.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(0).pkg));
            String text1 = getString(R.string.power_1, String.valueOf(startList.size())) + " ";
            SpannableString ss1 = new SpannableString(text1 + getString(R.string.power_4));
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3131")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            power_text.setText(ss1);
        }
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
                    SetAdUtil.track("完成页面", "点击好评good", "", 1);
                    PreData.putDB(SucceedActivity.this, MyConstant.IS_ROTATE, true);
                    MUtilGp.rate(SucceedActivity.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_bad:
                    SetAdUtil.track("完成页面", "点击好评bad", "", 1);
                    PreData.putDB(SucceedActivity.this, MyConstant.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.rotate_cha:
                    SetAdUtil.track("完成页面", "点击好评叉号", "", 1);
                    PreData.putDB(SucceedActivity.this, MyConstant.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_power_button:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtil.track("完成页面", "点击进入深度清理", "", 1);
                    jumpTo(DeepingActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button:
                    SetAdUtil.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(RubbishActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    SetAdUtil.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(MemoryAvtivity.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button:
                    SetAdUtil.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(BatteriesActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_file_button:
                    if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtil.track("完成页面", "点击进入文件管理", "", 1);
                    PreData.putDB(SucceedActivity.this, MyConstant.FILE_CLEAN, true);
                    jumpTo(PhoneFileManagerActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_gboost_button:
                    if (TextUtils.equals("Gboost", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtil.track("完成页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(SucceedActivity.this, MyConstant.GBOOST_CLEAN, true);
                    jumpTo(GoodGameActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtil.track("完成页面", "点击进入相似图片", "", 1);
                    PreData.putDB(SucceedActivity.this, MyConstant.PHOTO_CLEAN, true);
                    jumpTo(PictActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_notifi_button:
                    SetAdUtil.track("完成页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(SucceedActivity.this, MyConstant.NOTIFI_CLEAN, true);
                    if (!MyUtils.isNotificationListenEnabled(SucceedActivity.this)) {
                        startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 100);
                    } else if (!PreData.getDB(SucceedActivity.this, MyConstant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SucceedActivity.this, NotifingAnimationActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    } else {
                        Intent intent6 = new Intent(SucceedActivity.this, MyNotifingActivity.class);
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
            if (MyUtils.isNotificationListenEnabled(SucceedActivity.this)) {
                PreData.putDB(SucceedActivity.this, MyConstant.KEY_NOTIFI, true);
                Intent intent = new Intent(SucceedActivity.this, MyNotifingActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SucceedActivity.this, NotifingAnimationActivity.class);
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
        //缩小
        new Handler().postDelayed(new Runnable() {
            public void run() {
                success_cirlewaveview.setVisibility(View.VISIBLE);
//                success_cirlewaveview.startCircleWaveCiew(true);
            }
        }, 1000);
        // 缩放
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(success_cirlewaveview, "scaleY", 1f, 1.2f, 1f);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(success_cirlewaveview, "scaleX", 1f, 1.2f, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.setDuration(500);
                animSet.play(animator).with(animator1);
                animSet.start();
//                imageview_beijing.setVisibility(View.VISIBLE);
            }
        }, 1500);
        // 对勾
        new Handler().postDelayed(new Runnable() {
            public void run() {
                isdoudong = false;
                success_drawhook.startProgress(500);
            }
        }, 2000);
//        final ObjectAnimator rotate = ObjectAnimator.ofFloat(success_progress, "rotation", 0f, 360f);
//        rotate.setDuration(600);
//        rotate.setRepeatCount(3);
//        rotate.setInterpolator(new LinearInterpolator());
//        rotate.start();
//        Animation animation = AnimationUtils.loadAnimation(SucceedActivity.this, R.anim.huojian_pop);
//        success_huojian.startAnimation(animation);
//        animation.setFillAfter(true);
//        success_huojian.setVisibility(View.VISIBLE);
//        animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
//            @Override
//            public void onAnimationEnd(android.view.animation.Animation animation) {
//                final float hx = success_huojian.getX();
//                final float hy = success_huojian.getY();
//                isdoudong = true;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (isdoudong) {
//                            if (onDestroyed) {
//                                break;
//                            }
//                            int x = (int) (Math.random() * (16)) - 8;
//                            int y = (int) (Math.random() * (16)) - 8;
//                            try {
//                                Thread.sleep(80);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            Tween.to(success_huojian, ImageAccessor.BOUNCE_EFFECT, 0.08f).target(hx + x, hy + y, 1, 1)
//                                    .ease(TweenEquations.easeInQuad).delay(0)
//                                    .start(tweenManager);
//                        }
//                    }
//                }).start();
//                rotate.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
//                        ObjectAnimator a = ObjectAnimator.ofFloat(success_huojian, View.TRANSLATION_Y, 0, -2500);
//                        a.setDuration(300);
//                        a.start();
//                        a.addListener(new Animator.AnimatorListener() {
//                            @Override
//                            public void onAnimationCancel(Animator animation) {
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                success_progress.setVisibility(View.GONE);
//                                success_huojian.setVisibility(View.GONE);
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animator animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationStart(Animator animation) {
//
//                            }
//                        });
//                    }

//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onAnimationRepeat(android.view.animation.Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationStart(android.view.animation.Animation animation) {
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        if (PreData.getDB(this, MyConstant.GOODGAME, 1) == 0) {
            main_gboost_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.PICTUREX, 1) == 0) {
            main_picture_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.POWERACTIVITY, 1) == 0) {
            main_power_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.FILEACTIVITY, 1) == 0) {
            main_file_button.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.NOTIFIACTIVITY, 1) == 0) {
            main_notifi_button.setVisibility(View.GONE);
        }
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
