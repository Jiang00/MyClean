package com.privacy.junk.activityprivacy;

import android.animation.ObjectAnimator;
import android.content.Intent;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.privacy.clean.core.CleanManager;
import com.privacy.clean.utilsprivacy.MyUtils;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.clean.entity.JunkInfo;
import com.privacy.junk.R;
import com.privacy.junk.privacycustomview.PrivacyDrawHookView;
import com.privacy.junk.privacycustomview.PrivacyImageAccessor;
import com.privacy.junk.privacycustomview.KuoShan;
import com.privacy.junk.privacycustomview.PrivacyMainRoundView;
import com.privacy.junk.privacycustomview.PrivacySlowScrollView;
import com.privacy.junk.toolsprivacy.PrivacyUtilGp;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.toolsprivacy.SetAdUtilPrivacy;
import com.twee.module.tweenengine.Tween;
import com.twee.module.tweenengine.TweenManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/6.
 */

public class PrivacySucceedActivity extends BaseActivity {
    private String TAG_CLEAN = "cprivacy_success";
    private String TAG_CLEAN_2 = "cprivacy_success_2";
    RelativeLayout main_gboost_button;
    FrameLayout title_left;
    TextView main_rotate_good;
    TextView clean_size;
    KuoShan success_kuoshan;
    ImageView success_diancirle, success_diancirle1;
    RelativeLayout main_ram_button;
    private boolean haveAd;
    private boolean animationEnd;
    private MyApplication cleanApplication;
    RelativeLayout main_junk_button;
    TextView title_name;
    ImageView success_jiantou;
    PrivacyMainRoundView mainRoundView;
    PrivacySlowScrollView scrollView;
    long endStart = -1;
    ObjectAnimator objectAnimator;
    int sizeInt;
    long sizeInt1;
    PrivacyDrawHookView success_drawhook;
    private boolean isdoudong;
    private TweenManager tweenManager;
    private boolean istween;
    private Handler myHandler;
    LinearLayout main_rotate_all;
    RelativeLayout main_power_button;
    RelativeLayout main_notifi_button;
    LinearLayout main_cooling_button;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;
    RelativeLayout main_picture_button;
    RelativeLayout main_file_button;
    TextView success_clean_size;
    TextView success_textview;
    TextView main_rotate_bad;
    ImageView rotate_cha;
    private Animation rotate_set;
    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_success);

        //深度清理
        if (PreData.getDB(this, MyConstantPrivacy.POWERACTIVITY, 1) == 0) {
            main_power_button.setVisibility(View.GONE);
        }
        //文件
        if (PreData.getDB(this, MyConstantPrivacy.FILEACTIVITY, 1) == 0) {
            main_file_button.setVisibility(View.GONE);
        }
        //通知栏
        if (PreData.getDB(this, MyConstantPrivacy.NOTIFIACTIVITY, 1) == 0) {
            main_notifi_button.setVisibility(View.GONE);
        }
        //游戏
        if (PreData.getDB(this, MyConstantPrivacy.GOODGAME, 1) == 0) {
            main_gboost_button.setVisibility(View.GONE);
        }
        //相似图片
        if (PreData.getDB(this, MyConstantPrivacy.PICTUREX, 1) == 0) {
            main_picture_button.setVisibility(View.GONE);
        }

        tweenManager = new TweenManager();
        Tween.registerAccessor(ImageView.class, new PrivacyImageAccessor());
        istween = true;
        setAnimationThread();
        myHandler = new Handler();
        if (TextUtils.equals("cooling", getIntent().getStringExtra("from"))) {
            //电池降温跳过动画
            //广告
            if (PreData.getDB(PrivacySucceedActivity.this, MyConstantPrivacy.FULL_SUCCESS, 0) == 1) {
                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
            }
            //动画结束换内容的
            startSecondAnimation();
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
        initAnimation();

        if (PreData.getDB(this, MyConstantPrivacy.IS_ROTATE, false)) {
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
        if (PreData.getDB(this, MyConstantPrivacy.FULL_SUCCESS, 0) == 1) {

        } else {
            myHandler.postDelayed(new Runnable() {
                public void run() {
                    addAd();
                }
            }, 1000);

        }
    }

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

//            power_icon.setImageDrawable(LoadManager.getInstance(this).getAppIcon(startList.get(0).pkg));
            String text1 = getString(R.string.power_1, String.valueOf(startList.size())) + " ";
            SpannableString ss1 = new SpannableString(text1 + getString(R.string.power_4));
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ff3131")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            power_text.setText(ss1);
        }
    }

    public void startSizeAni(final long size, final String cleanName) {
        success_kuoshan.setVisibility(View.VISIBLE);
        clean_size.setVisibility(View.VISIBLE);
        success_diancirle.setVisibility(View.VISIBLE);

        String[] str = MyUtils.convertStorage(size, false).split("\\.");
        if ("0".equals(str[0])) {
            sizeInt = (int) size;
            sizeInt1 = size;
        } else {
            sizeInt = Integer.parseInt(str[0]);
        }
        success_diancirle1.setVisibility(View.GONE);
        objectAnimator = ObjectAnimator.ofFloat(success_diancirle, "rotation", 0f, 360f);
        objectAnimator.setRepeatCount(-1);
        LinearInterpolator lir = new LinearInterpolator();
        objectAnimator.setInterpolator(lir);
        objectAnimator.setDuration(300);
        objectAnimator.start();
        success_kuoshan.start(getResources().getDimensionPixelSize(com.privacy.module.charge.saver.R.dimen.d121),
                getResources().getDimensionPixelSize(com.privacy.module.charge.saver.R.dimen.d82),
                getResources().getDimensionPixelSize(com.privacy.module.charge.saver.R.dimen.d2), 15, 0.28f);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int time = 100;
                for (long i = sizeInt; i > -1; ) {
                    final long finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            clean_size.setText(String.valueOf(finalI));
                        }
                    });
                    time -= 5;
                    if (time < 30) {
                        time = 30;
                    }
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == 0) {
                        endStart = 0;
                    } else if (i < sizeInt / 15) {
                        endStart = 0;
                    }
                    if (sizeInt > 100) {
                        i -= sizeInt / 15;
                    } else {
                        i--;
                    }
                }
                if (endStart == 0) {
                    if (objectAnimator != null) {
                        objectAnimator.pause();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            success_diancirle.setVisibility(View.GONE);
                            success_diancirle1.setVisibility(View.VISIBLE);
                            if (clean_size != null && clean_size.getVisibility() == View.VISIBLE) {
                                clean_size.setVisibility(View.GONE);
                            }
                            success_drawhook.setVisibility(View.VISIBLE);
                        }
                    });
                    isdoudong = false;
                    success_drawhook.startProgress(500);
                    success_drawhook.setListener(new PrivacyDrawHookView.DrawHookListener() {

                        @Override
                        public void duogouSc() {
                            if (PreData.getDB(PrivacySucceedActivity.this, MyConstantPrivacy.FULL_SUCCESS, 0) == 1) {
                                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                            }
                            success_textview.setVisibility(View.VISIBLE);
                            if (sizeInt1 != 0) {
                                if ("app".equals(cleanName)) {
                                    success_textview.setText(getResources().getText(R.string.qingli) + " " + getString(R.string.power_1, String.valueOf(size)));
                                } else if ("picture".equals(cleanName)) {
                                    success_textview.setText(getString(R.string.success_4, size));
                                } else if ("noti".equals(cleanName)) {
                                    success_textview.setText(getString(R.string.success_6, size));
                                }
                            } else {
                                success_textview.setText(getResources().getText(R.string.qingli) + " " + MyUtils.convertStorage(size, true));
                            }
                            //广告
                            if (PreData.getDB(PrivacySucceedActivity.this, MyConstantPrivacy.FULL_SUCCESS, 0) == 1) {
                                AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                            }
                            //动画结束换内容的
                            startSecondAnimation();
                            success_drawhook.setListener(null);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void findId() {
        super.findId();
//        notifi_info_lot = (LottieAnimationView) findViewById(R.id.success_huojian);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
//        imageview_beijing = (ImageView) findViewById(R.id.imageview_beijing);
        success_jiantou = (ImageView) findViewById(R.id.success_jiantou);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        success_drawhook = (PrivacyDrawHookView) findViewById(R.id.success_drawhook);
//        success_huojian = (ImageView) findViewById(R.id.success_huojian);
        mainRoundView = (PrivacyMainRoundView) findViewById(R.id.main_progress);
        success_textview = (TextView) findViewById(R.id.success_textview);
        scrollView = (PrivacySlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_power_button = (RelativeLayout) findViewById(R.id.main_power_button);
        main_notifi_button = (RelativeLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (RelativeLayout) findViewById(R.id.main_file_button);
        main_cooling_button = (LinearLayout) findViewById(R.id.main_cooling_button);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
//        success_cirlewaveview = (Success_CircleWaveView) findViewById(R.id.success_cirlewaveview);
        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_gboost_button = (RelativeLayout) findViewById(R.id.main_gboost_button);
        main_picture_button = (RelativeLayout) findViewById(R.id.main_picture_button);
//        power_text = (TextView) findViewById(R.id.power_text);
        main_rotate_good = (TextView) findViewById(R.id.main_rotate_good);
        main_rotate_bad = (TextView) findViewById(R.id.main_rotate_bad);
        rotate_cha = (ImageView) findViewById(R.id.main_rotate_close);
//        power_icon = (ImageView) findViewById(R.id.power_icon);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
//        success_progress = (ImageView) findViewById(R.id.success_progress);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
        clean_size = (TextView) findViewById(R.id.clean_size);
        success_diancirle = (ImageView) findViewById(R.id.success_diancirle);
        success_diancirle1 = (ImageView) findViewById(R.id.success_diancirle1);
        success_kuoshan = (KuoShan) findViewById(R.id.success_kuoshan);
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
                    SetAdUtilPrivacy.track("完成页面", "点击好评good", "", 1);
                    PreData.putDB(PrivacySucceedActivity.this, MyConstantPrivacy.IS_ROTATE, true);
                    PrivacyUtilGp.rate(PrivacySucceedActivity.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_bad:
                    SetAdUtilPrivacy.track("完成页面", "点击好评bad", "", 1);
                    PreData.putDB(PrivacySucceedActivity.this, MyConstantPrivacy.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_rotate_close:
                    SetAdUtilPrivacy.track("完成页面", "点击好评叉号", "", 1);
                    PreData.putDB(PrivacySucceedActivity.this, MyConstantPrivacy.IS_ROTATE, true);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_power_button:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtilPrivacy.track("完成页面", "点击进入深度清理", "", 1);
                    jumpTo(DeepingActivityPrivacy.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button:
                    SetAdUtilPrivacy.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(RubbishActivityPrivacy.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    SetAdUtilPrivacy.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(PrivacyMemoryAvtivity.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button:
                    SetAdUtilPrivacy.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(PrivacyBatteriesActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_file_button:
                    if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtilPrivacy.track("完成页面", "点击进入文件管理", "", 1);
                    PreData.putDB(PrivacySucceedActivity.this, MyConstantPrivacy.FILE_CLEAN, true);
                    jumpTo(FileManagerActivityPrivacy.class);
                    onBackPressed();
                    break;
                case R.id.main_gboost_button:
                    if (TextUtils.equals("Gboost", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtilPrivacy.track("完成页面", "点击进入游戏加速", "", 1);
                    PreData.putDB(PrivacySucceedActivity.this, MyConstantPrivacy.GBOOST_CLEAN, true);
                    jumpTo(GoodGameActivityPrivacy.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    SetAdUtilPrivacy.track("完成页面", "点击进入相似图片", "", 1);
                    PreData.putDB(PrivacySucceedActivity.this, MyConstantPrivacy.PHOTO_CLEAN, true);
                    jumpTo(PictActivityPrivacy.class);
                    onBackPressed();
                    break;
                case R.id.main_notifi_button:
                    SetAdUtilPrivacy.track("完成页面", "点击进入通知栏清理", "", 1);
                    PreData.putDB(PrivacySucceedActivity.this, MyConstantPrivacy.NOTIFI_CLEAN, true);
                    if (!PreData.getDB(PrivacySucceedActivity.this, MyConstantPrivacy.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(PrivacySucceedActivity.this)) {
                        Intent intent6 = new Intent(PrivacySucceedActivity.this, PrivacyNotifingAnimationActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    } else {
                        Intent intent6 = new Intent(PrivacySucceedActivity.this, MyNotifingActivityPrivacy.class);
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
            if (MyUtils.isNotificationListenEnabled(PrivacySucceedActivity.this)) {
                PreData.putDB(PrivacySucceedActivity.this, MyConstantPrivacy.KEY_NOTIFI, true);
                Intent intent = new Intent(PrivacySucceedActivity.this, MyNotifingActivityPrivacy.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(PrivacySucceedActivity.this, PrivacyNotifingAnimationActivity.class);
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
