package com.vater.clean.junk.jiemian;

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
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vater.clean.core.CleanManager;
import com.vater.clean.entity.JunkInfo;
import com.vater.clean.util.LoadManager;
import com.vater.clean.util.PreData;
import com.vater.clean.util.Util;
import com.android.client.AndroidSdk;
import com.twee.module.tweenengine.Tween;
import com.twee.module.tweenengine.TweenEquations;
import com.twee.module.tweenengine.TweenManager;
import com.vater.clean.junk.R;
import com.vater.clean.junk.myview.DrawHookView;
import com.vater.clean.junk.myview.ImageAccessor;
import com.vater.clean.junk.myview.SlowScrollView;
import com.vater.clean.junk.gongju.AdUtil;
import com.vater.clean.junk.gongju.Constant;
import com.vater.clean.junk.gongju.MUtilGp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/6.
 */

public class SuccessActivity extends BaseActivity {
    LinearLayout main_gboost_button;
    FrameLayout title_left;
    LinearLayout main_rotate_all;
    LinearLayout main_power_button;
    LinearLayout main_notifi_button;
    LinearLayout main_cooling_button;
    LinearLayout main_ram_button;
    LinearLayout main_junk_button;
    TextView title_name;
    ImageView success_jiantou;
    TextView success_clean_size;
    DrawHookView success_drawhook;
    SlowScrollView scrollView;
    LinearLayout success;
    LinearLayout main_picture_button;
    LinearLayout main_file_button;
    TextView main_rotate_bad;
    ImageView rotate_cha;
    ImageView success_progress;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;
    ImageView power_icon;
    TextView power_text;
    TextView main_rotate_good;

    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;

    private boolean isdoudong;
    private TweenManager tweenManager;
    private boolean istween;
    private Handler myHandler;
    private String TAG_CLEAN = "vater_success";
    private String TAG_CLEAN_2 = "vater_success_2";
    private Animation rotate_set;

    private boolean haveAd;
    private boolean animationEnd;
    private MyApplication cleanApplication;
    private boolean duigousuccess;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        success_jiantou = (ImageView) findViewById(R.id.success_jiantou);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        success_drawhook = (DrawHookView) findViewById(R.id.success_drawhook);
        scrollView = (SlowScrollView) findViewById(R.id.scrollView);
        success = (LinearLayout) findViewById(R.id.success);
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
        rotate_cha = (ImageView) findViewById(R.id.rotate_cha);
        power_icon = (ImageView) findViewById(R.id.power_icon);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        success_progress = (ImageView) findViewById(R.id.success_progress);
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
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            main_notifi_button.setVisibility(View.GONE);
        }
        initAnimation();
        success_drawhook.setListener(new DrawHookView.DrawHookListener() {

            @Override
            public void duogouSc() {
                success_drawhook.setListener(null);
                duigousuccess = true;
                if (PreData.getDB(SuccessActivity.this, Constant.FULL_SUCCESS, 0) == 1) {
                    if (TextUtils.equals("ramSpeed", getIntent().getStringExtra("from"))) {
                        AndroidSdk.showFullAd(AdUtil.DEFAULT);
                    } else if (TextUtils.equals("junkClean", getIntent().getStringExtra("from"))) {
                        AndroidSdk.showFullAd(AdUtil.DEFAULT);
                    } else if (TextUtils.equals("allJunk", getIntent().getStringExtra("from"))) {
                        AndroidSdk.showFullAd(AdUtil.DEFAULT);
                    } else if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
                        AndroidSdk.showFullAd(AdUtil.DEFAULT);
                    } else {
                        AndroidSdk.showFullAd(AdUtil.DEFAULT);
                    }
                }
                startSecondAnimation();
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
        if (PreData.getDB(this, Constant.FULL_SUCCESS, 0) == 1) {

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

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(TAG_CLEAN, R.layout.native_ad_full);
        native_xiao = AdUtil.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_2);
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

    private void initAnimation() {
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                startFirstAnimation();
            }
        });
    }

    private void startSecondAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_success);
        success.startAnimation(animation);
        success.setVisibility(View.VISIBLE);
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

    public void startFirstAnimation() {
        isdoudong = false;
        success_progress.setVisibility(View.GONE);
        success_drawhook.startProgress(500);
//        final ObjectAnimator rotate = ObjectAnimator.ofFloat(success_progress, "rotation", 0f, 360f);
//        rotate.setDuration(600);
//        rotate.setRepeatCount(3);
//        rotate.setInterpolator(new LinearInterpolator());
//        rotate.start();
//        rotate.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.main_rotate_good:
                    AdUtil.track("完成页面", "点击好评good", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE, true);
                    MUtilGp.rate(SuccessActivity.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_bad:
                    AdUtil.track("完成页面", "点击好评bad", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.rotate_cha:
                    AdUtil.track("完成页面", "点击好评叉号", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_power_button:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入深度清理", "", 1);
                    jumpTo(DeepActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button:
                    AdUtil.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(LogActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    AdUtil.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(NeicunAvtivity.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button:
                    AdUtil.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(BatteryActivity.class);
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
                    jumpTo(GameActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    AdUtil.track("完成页面", "点击进入相似图片", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.PHOTO_CLEAN, true);
                    jumpTo(SimerActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_notifi_button:
                    AdUtil.track("完成页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(SuccessActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(SuccessActivity.this)) {
                        startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 100);
                    } else if (!PreData.getDB(SuccessActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SuccessActivity.this, NotifiAnimaActivity.class);
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
                Intent intent = new Intent(SuccessActivity.this, NotifiAnimaActivity.class);
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
    protected void onRestart() {
        super.onRestart();
        if (!duigousuccess) {
            success.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        success_drawhook.setListener(null);
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
