package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;

import java.util.List;

/**
 * Created by ${} on 2017/11/8.
 */

public class FloatAnimationActivity extends BaseActivity {
    AnimationDrawable animationDrawable;
    ImageView float_yan, float_huo;
    AnimatorSet animatorSet;
    Handler handler;
    private Dialog dialog;
    private long size;
    private int count;
    private LinearLayout ll_ad;
    private AnimatorSet animatorSet_2;

    @Override
    protected void findId() {
        super.findId();
        float_yan = (ImageView) findViewById(R.id.float_yan);
        float_huo = (ImageView) findViewById(R.id.float_huo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.layout_float_animation);
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.float_yan);
        float_yan.setBackgroundDrawable(animationDrawable);
        animatorSet = new AnimatorSet();
        ObjectAnimator animator_x = ObjectAnimator.ofFloat(float_huo, View.SCALE_X, 0.8f, 1);
        ObjectAnimator animator_y = ObjectAnimator.ofFloat(float_huo, View.SCALE_Y, 0.8f, 1f);
        ObjectAnimator animator_t = ObjectAnimator.ofFloat(float_huo, View.TRANSLATION_Y, 0, -getResources().getDimensionPixelOffset(R.dimen.d146));
        animatorSet.setDuration(500);
        animatorSet.play(animator_t).with(animator_x).with(animator_y).after(500);
        animatorSet.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                animatorSet_2 = new AnimatorSet();
                ObjectAnimator animator_1 = ObjectAnimator.ofFloat(float_huo, View.TRANSLATION_Y, -getResources().getDimensionPixelOffset(R.dimen.d146),
                        -metrics.heightPixels);
                ObjectAnimator animator_2 = ObjectAnimator.ofFloat(float_huo, View.SCALE_X, 1, 0.4f);
                ObjectAnimator animator_3 = ObjectAnimator.ofFloat(float_huo, View.SCALE_Y, 1, 0.4f);
                animatorSet_2.setDuration(500);
                animatorSet_2.play(animator_1).with(animator_2).with(animator_3);
                animatorSet_2.start();
                animatorSet_2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        count++;
                        show_text();
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }
                });
            }
        }, 1800);
        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(FloatAnimationActivity.this);
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

    private void clear(Context context) {
        size = killAll(context);
    }

    public long killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final long beforeMem = getAvailMemory(am);
        int count = 0;
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            if (onPause) {
                break;
            }
            try {
                if (packageInfo.packageName == null || packageInfo.packageName.equals(context.getPackageName()) || packageInfo.packageName.contains("com.eosmobi")
                        || packageInfo.packageName.contains("com.google") || packageInfo.packageName.contains("com.android.vending")) {
                    continue;
                }
                am.killBackgroundProcesses(packageInfo.packageName);
                ++count;
            } catch (Exception e) {
            }
        }
        final long afterMem = getAvailMemory(am);
        final long M = (afterMem - beforeMem);
        final int clearedCount = count;
        return M;
    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    private void show_text() {
        if (count == 2) {
            count = 0;
            dialog = new Dialog(FloatAnimationActivity.this, R.style.add_dialog);
            dialog.show();
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.dialog_anim);
//            dialog.setCanceledOnTouchOutside(true);
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = dm.widthPixels; //设置宽度
            lp.height = dm.heightPixels; //设置高度
            window.setAttributes(lp);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    FloatAnimationActivity.this.finish();
                }
            });
            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
            LinearLayout dialog_a = (LinearLayout) view.findViewById(R.id.dialog_a);
            dialog_a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
            loadAd();
            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
            if (size < 0) {
                size = 0;
            }
            short_clean_szie.setText(Util.convertStorage(size, true));
            window.setContentView(view);
        }
    }

    private void loadAd() {
        View nativeView = AdUtil.getNativeAdView(this, "", R.layout.native_ad1);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (animationDrawable != null) {
            animationDrawable.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        count = 0;
        if (animationDrawable != null) {
            animationDrawable.stop();
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawable.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawable.setCallback(null);
            animationDrawable = null;
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (animatorSet_2 != null) {
            animatorSet_2.cancel();
        }
    }
}
