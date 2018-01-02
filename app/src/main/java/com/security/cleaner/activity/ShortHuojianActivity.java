package com.security.cleaner.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.security.cleaner.myview.ShortBubbleCenterLayout;
import com.security.cleaner.utils.AdUtil;
import com.security.cleaner.R;
import com.security.mcleaner.mutil.Util;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class ShortHuojianActivity extends BaseActivity {

    private static final int FLAKE_NUM = 3;

    private int count;
    private Handler myHandler;
    private boolean isdoudong;
    private long size;
    private View nativeView;
    ShortBubbleCenterLayout short_bubble;
    LinearLayout ll_ad;
    FrameLayout short_fl;
    ImageView short_zhuan, short_huo;
    private Dialog dialog;
    private String TAG_SHORTCUT = "security_shortcut";
    private Animation suo;
    private AnimatorSet animatorSet;

    @Override
    protected void findId() {
        super.findId();
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        short_bubble = (ShortBubbleCenterLayout) findViewById(R.id.short_bubble);
        short_zhuan = (ImageView) findViewById(R.id.short_zhuan);
        short_huo = (ImageView) findViewById(R.id.short_huo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_cut);
        myHandler = new Handler();
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);

        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(ShortHuojianActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        show_text();
                    }
                });
            }
        }).start();
    }

    private void loadAd() {
        nativeView = AdUtil.getNativeAdView(TAG_SHORTCUT, R.layout.native_ad1);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            if (nativeView.getHeight() == Util.dp2px(250)) {
                layout_ad.height = Util.dp2px(250);
            }
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        } else {
        }
    }


    private void show_text() {
        if (count == 2) {
            count = 0;
            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
            loadAd();
            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
            TextView short_clean_msg = (TextView) view.findViewById(R.id.short_clean_msg);
            ImageView delete = (ImageView) view.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (size < 0) {
                size = 0;
            }
            short_clean_szie.setText(Util.convertStorage(size, true));
            short_clean_msg.setText(getString(R.string.success_3, ""));
            dialog = new Dialog(ShortHuojianActivity.this, R.style.add_dialog);
            dialog.show();

            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.dialog_anim);
//		dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = dm.widthPixels; //设置宽度
            lp.height = dm.heightPixels; //设置高度
            window.setAttributes(lp);
            window.setContentView(view);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ShortHuojianActivity.this.finish();
                }
            });
        }
    }


    private void clear(Context context) {
        size = killAll(context);
    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    public long killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        final long beforeMem = getAvailMemory(am);
        int count = 0;
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            if (onPause) {
                break;
            }
            if (packageInfo.packageName.equals(context.getPackageName()) || packageInfo.packageName.contains("com.security")) {
                continue;
            }
            am.killBackgroundProcesses(packageInfo.packageName);
            ++count;
        }
        final long afterMem = getAvailMemory(am);
        final long M = (afterMem - beforeMem);
        final int clearedCount = count;
        return M;
    }


    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        animatorSet = new AnimatorSet();
        float huo_size = getResources().getDimension(R.dimen.d176) * 2;
        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(short_huo, View.TRANSLATION_Y, huo_size, 0);
        objectAnimator_1.setDuration(1000);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(short_huo, View.TRANSLATION_X, -huo_size, 0);
        objectAnimator_2.setDuration(1000);
        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(short_huo, View.TRANSLATION_Y, 0, -5, 0, 5, 0);
        objectAnimator_3.setInterpolator(new LinearInterpolator());
        objectAnimator_3.setDuration(200);
        objectAnimator_3.setRepeatCount(8);
        ObjectAnimator objectAnimator_4 = ObjectAnimator.ofFloat(short_huo, View.TRANSLATION_X, 0, -5, 0, 5, 0);
        objectAnimator_4.setInterpolator(new LinearInterpolator());
        objectAnimator_4.setDuration(200);
        objectAnimator_4.setRepeatCount(8);
        ObjectAnimator objectAnimator_5 = ObjectAnimator.ofFloat(short_huo, View.TRANSLATION_Y, 0, -huo_size);
        objectAnimator_5.setDuration(500);
        ObjectAnimator objectAnimator_6 = ObjectAnimator.ofFloat(short_huo, View.TRANSLATION_X, 0, huo_size);
        objectAnimator_6.setDuration(500);

        ObjectAnimator objectAnimator_zhuan = ObjectAnimator.ofFloat(short_zhuan, View.ROTATION, 0, 360);
        objectAnimator_zhuan.setDuration(1000);
        objectAnimator_zhuan.setRepeatCount(4);
        objectAnimator_zhuan.setInterpolator(new LinearInterpolator());
        animatorSet.play(objectAnimator_zhuan);
        animatorSet.play(objectAnimator_1).with(objectAnimator_2);
        animatorSet.play(objectAnimator_3).with(objectAnimator_4);
        animatorSet.play(objectAnimator_5).with(objectAnimator_6);
        animatorSet.play(objectAnimator_3).after(objectAnimator_1);
        animatorSet.play(objectAnimator_5).after(objectAnimator_3);
        animatorSet.start();
        short_huo.setVisibility(View.VISIBLE);
        myHandler.postDelayed(runnable, 4000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            short_fl.startAnimation(suo);
            suo.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (animatorSet != null && animatorSet.isRunning()) {
                        animatorSet.cancel();
                    }
                    short_fl.setVisibility(View.GONE);
                    count++;
                    isdoudong = false;
                    show_text();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationStart(Animation animation) {

                }
            });
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        myHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        finish();
    }
}
