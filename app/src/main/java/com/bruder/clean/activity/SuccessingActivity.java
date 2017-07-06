package com.bruder.clean.activity;

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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.customeview.MySlowScrollView;
import com.bruder.clean.junk.R;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.TransmitValue;
import com.bruder.clean.util.UtilAd;
import com.bruder.clean.util.UtilGp;
import com.cleaner.entity.JunkInfo;
import com.cleaner.heart.CleanManager;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;
import com.sample.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/6.
 */

public class SuccessingActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    TextView success_clean_size;
    LinearLayout main_rotate_all;
    RelativeLayout main_power_button;
    RelativeLayout main_notifi_button;
    RelativeLayout main_file_button;
    Button main_notifi_button1, main_file_button1;
    TextView success_clean_2;
    ImageView success_huojian;
    //    LinearLayout success_stars;
    ImageView power_icon;
    TextView power_text;
    Button main_rotate_good, main_good_refuse;
    LinearLayout ad_title;
    LinearLayout ll_ad_xiao;
    MySlowScrollView scrollView;
    Button junk_button_clean;
    RelativeLayout main_cooling_button;
    RelativeLayout main_ram_button;
    RelativeLayout main_junk_button;
    Button main_junk_button1, main_cooling_button1, main_power_button1, main_picture_button1, main_gboost_button1, main_ram_button1;
    private boolean haveAd;
    private boolean animationEnd;
    private MyApplication cleanApplication;
    LinearLayout ad_native_2;
    private View nativeView;
    private View native_xiao;
    private boolean istween;
    private Handler myHandler;
    private String TAG_CLEAN = "bruder_success";//
    private String TAG_CLEAN_2 = "bruder_success_2";//
    private Animation rotate;
    RelativeLayout main_gboost_button;
    RelativeLayout main_picture_button;
    LottieAnimationView notifi_info_lot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_success);
        istween = true;
        setAnimationThread();
        myHandler = new Handler();
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_ni);
        notifi_info_lot.setImageAssetsFolder("images/success/");
        notifi_info_lot.setAnimation("success.json");
        notifi_info_lot.loop(false);
        notifi_info_lot.setSpeed(0.7f);
        notifi_info_lot.playAnimation();
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
            main_ram_button.setVisibility(View.GONE);
            main_power_button.setVisibility(View.GONE);
            main_junk_button.setVisibility(View.GONE);
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
        if (DataPre.getDB(this, Constant.IS_ROTATE, false)) {
            main_rotate_all.setVisibility(View.GONE);
        }

        addListener();
        TranslateAnimation translate = new TranslateAnimation(0, 0, 10, 2);
        translate.setInterpolator(new AccelerateInterpolator());//OvershootInterpolator
        translate.setDuration(400);
        translate.setRepeatCount(-1);
        translate.setRepeatMode(Animation.REVERSE);
//        success_jiantou.startAnimation(translate);
        shendu();
        if (DataPre.getDB(this, Constant.FULL_SUCCESS, 0) == 1) {

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
        notifi_info_lot = (LottieAnimationView) findViewById(R.id.success_huojian);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        success_clean_size = (TextView) findViewById(R.id.success_clean_size);
        success_clean_2 = (TextView) findViewById(R.id.success_clean_2);
        success_huojian = (ImageView) findViewById(R.id.success_huojian);
        scrollView = (MySlowScrollView) findViewById(R.id.scrollView);
        main_rotate_all = (LinearLayout) findViewById(R.id.main_rotate_all);
        main_power_button = (RelativeLayout) findViewById(R.id.main_power_button);
        main_power_button1 = (Button) findViewById(R.id.main_power_button1);
        main_notifi_button = (RelativeLayout) findViewById(R.id.main_notifi_button);
        main_file_button = (RelativeLayout) findViewById(R.id.main_file_button);
        main_notifi_button1 = (Button) findViewById(R.id.main_notifi_button1);
        junk_button_clean = (Button) findViewById(R.id.junk_button_clean);
        main_file_button1 = (Button) findViewById(R.id.main_file_button1);
        main_cooling_button = (RelativeLayout) findViewById(R.id.main_cooling_button);
        main_cooling_button1 = (Button) findViewById(R.id.main_cooling_button1);
        main_ram_button = (RelativeLayout) findViewById(R.id.main_ram_button);
        main_ram_button1 = (Button) findViewById(R.id.main_ram_button1);
        main_junk_button = (RelativeLayout) findViewById(R.id.main_junk_button);
        main_junk_button1 = (Button) findViewById(R.id.main_junk_button1);
        main_gboost_button = (RelativeLayout) findViewById(R.id.main_gboost_button);
        main_gboost_button1 = (Button) findViewById(R.id.main_gboost_button1);
        main_picture_button = (RelativeLayout) findViewById(R.id.main_picture_button);
        main_picture_button1 = (Button) findViewById(R.id.main_picture_button1);
        power_text = (TextView) findViewById(R.id.power_text);
        main_rotate_good = (Button) findViewById(R.id.main_rotate_good);
        main_good_refuse = (Button) findViewById(R.id.main_good_refuse);
//        delete = (ImageView) findViewById(R.id.delete);
        power_icon = (ImageView) findViewById(R.id.power_icon);
        ad_native_2 = (LinearLayout) findViewById(R.id.ad_native_2);
        ad_title = (LinearLayout) findViewById(R.id.ad_title);
        ll_ad_xiao = (LinearLayout) findViewById(R.id.ll_ad_xiao);
    }

    private void addListener() {
        title_left.setOnClickListener(onClickListener);
        main_rotate_good.setOnClickListener(onClickListener);
        main_good_refuse.setOnClickListener(onClickListener);

        main_power_button.setOnClickListener(onClickListener);
        main_power_button1.setOnClickListener(onClickListener);
        main_notifi_button.setOnClickListener(onClickListener);
        main_file_button.setOnClickListener(onClickListener);
        main_notifi_button1.setOnClickListener(onClickListener);
        junk_button_clean.setOnClickListener(onClickListener);
        main_file_button1.setOnClickListener(onClickListener);
        main_cooling_button.setOnClickListener(onClickListener);
        main_cooling_button1.setOnClickListener(onClickListener);
        main_ram_button.setOnClickListener(onClickListener);
        main_ram_button1.setOnClickListener(onClickListener);
        main_junk_button.setOnClickListener(onClickListener);
        main_junk_button1.setOnClickListener(onClickListener);
        main_gboost_button.setOnClickListener(onClickListener);
        main_gboost_button1.setOnClickListener(onClickListener);
        main_picture_button.setOnClickListener(onClickListener);
        main_picture_button1.setOnClickListener(onClickListener);
    }

    private void addAd() {
        nativeView = UtilAd.getNativeAdView(TAG_CLEAN, R.layout.native_ad_full);
        native_xiao = UtilAd.getNativeAdView(TAG_CLEAN_2, R.layout.native_ad_2);
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

    public void startFirstAnimation() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ObjectAnimator huojian_translation = ObjectAnimator.ofFloat(success_huojian, "translationY", 0f, -2000f);
        huojian_translation.setDuration(1500);
        huojian_translation.start();
        final AnimatorSet animSet2 = new AnimatorSet();

        huojian_translation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//广告
                if (DataPre.getDB(SuccessingActivity.this, Constant.FULL_SUCCESS, 0) == 1) {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
                //动画结束换内容的
                startSecondAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

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
                // 好评
                case R.id.main_rotate_good:
                    DataPre.putDB(SuccessingActivity.this, Constant.IS_ROTATE, true);
                    UtilGp.rate(SuccessingActivity.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_good_refuse:
                    DataPre.putDB(SuccessingActivity.this, Constant.IS_ROTATE, true);
//                    UtilGp.rate(SuccessingActivity.this);
                    main_rotate_all.setVisibility(View.GONE);
                    break;
                case R.id.main_power_button:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    UtilAd.track("完成页面", "点击进入深度清理", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.DEEP_CLEAN, true);
                    jumpTo(PoweringActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_power_button1:
                    if (TextUtils.equals("power", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    UtilAd.track("完成页面", "点击进入深度清理", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.DEEP_CLEAN, true);
                    jumpTo(PoweringActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button:
                    UtilAd.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(GarbageActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_junk_button1:
                    UtilAd.track("完成页面", "点击进入垃圾清理", "", 1);
                    jumpTo(GarbageActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button:
                    UtilAd.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(PhoneRamAvtivity.class);
                    onBackPressed();
                    break;
                case R.id.main_ram_button1:
                    UtilAd.track("完成页面", "点击进入内存加速", "", 1);
                    jumpTo(PhoneRamAvtivity.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button:
                    UtilAd.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(CoolActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_cooling_button1:
                    UtilAd.track("完成页面", "点击进入降温页面", "", 1);
                    jumpTo(CoolActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_file_button:
                    if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    UtilAd.track("完成页面", "点击进入文件管理", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.FILE_CLEAN, true);
                    jumpTo(FilesActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_file_button1:
                    if (TextUtils.equals("file", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    UtilAd.track("完成页面", "点击进入文件管理", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.FILE_CLEAN, true);
                    jumpTo(FilesActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_gboost_button:
                    if (TextUtils.equals("Gboost", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    UtilAd.track("完成页面", "点击进入游戏加速", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.GBOOST_CLEAN, true);
                    jumpTo(GBoostingActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_gboost_button1:
                    if (TextUtils.equals("Gboost", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    UtilAd.track("完成页面", "点击进入游戏加速", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.GBOOST_CLEAN, true);
                    jumpTo(GBoostingActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    UtilAd.track("完成页面", "点击进入相似图片", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.PHOTO_CLEAN, true);
                    jumpTo(PicturesActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_picture_button1:
                    if (TextUtils.equals("picture", getIntent().getStringExtra("from"))) {
                        finish();
                        return;
                    }
                    UtilAd.track("完成页面", "点击进入相似图片", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.PHOTO_CLEAN, true);
                    jumpTo(PicturesActivity.class);
                    onBackPressed();
                    break;
                case R.id.main_notifi_button:
                    UtilAd.track("完成页面", "点击进入通知栏清理", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!DataPre.getDB(SuccessingActivity.this, Constant.KEY_NOTIFI, true) || !Util.isNotificationListenEnabled(SuccessingActivity.this)) {
                        Intent intent6 = new Intent(SuccessingActivity.this, NotifiIfActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    } else {
                        Intent intent6 = new Intent(SuccessingActivity.this, NotifingActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    }
                    break;
                case R.id.main_notifi_button1:
                    UtilAd.track("完成页面", "点击进入通知栏清理", "", 1);
                    DataPre.putDB(SuccessingActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(SuccessingActivity.this)) {
                        startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 100);
                    } else if (!DataPre.getDB(SuccessingActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SuccessingActivity.this, NotifiIfActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    } else {
                        Intent intent6 = new Intent(SuccessingActivity.this, NotifingActivity.class);
                        startActivity(intent6);
                        onBackPressed();
                    }
                    break;
                case R.id.junk_button_clean:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (Util.isNotificationListenEnabled(SuccessingActivity.this)) {
                DataPre.putDB(SuccessingActivity.this, Constant.KEY_NOTIFI, true);
                Intent intent = new Intent(SuccessingActivity.this, NotifingActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SuccessingActivity.this, NotifiIfActivity.class);
                startActivity(intent);
            }
            onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        istween = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        if (DataPre.getDB(this, Constant.POWERACATIVITY, 0) == 0) {
            main_power_button.setVisibility(View.GONE);
        }
        if (DataPre.getDB(this, Constant.FILEACTIVITY, 0) == 0) {
            main_file_button.setVisibility(View.GONE);
        }
        if (DataPre.getDB(this, Constant.NOTIFIACTIVITY, 0) == 0) {
            main_notifi_button.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if ("junkClean".equals(getIntent().getStringExtra("from"))) {
            TransmitValue.isJunk = true;
        } else if ("ramSpeed".equals(getIntent().getStringExtra("from"))) {
            TransmitValue.isRam = true;
        } else if ("cooling".equals(getIntent().getStringExtra("from"))) {
            TransmitValue.isCool = true;
        }
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
//                                tweenManager.update(delta);
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
