package com.bruder.clean.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.junk.R;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.PreData;
import com.bruder.clean.util.UtilAd;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;
import com.twee.module.tweenengine.TweenManager;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class ShortCutingActivity extends BaseActivity {
    private TweenManager tweenManager;
    private boolean istween;
    private static final int FLAKE_NUM = 3;
    private View nativeView;
    private String TAG_SHORTCUT = "bruder_shortcut";//
    private Animation suo;
    private Dialog dialog;
    FrameLayout short_backg;
    LinearLayout ll_ad;
    private long size;
    private int count;
    private Handler myHandler;
    ImageView short_cut2;
    RelativeLayout short_cut_beijing;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_cut);
        tweenManager = new TweenManager();
        myHandler = new Handler();
//        Tween.registerAccessor(ImageView.class, new ImageAccessor());
        istween = true;
        setAnimationThread();
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);

        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(ShortCutingActivity.this);
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
        if (DataPre.getDB(this, Constant.FULL_SHORTCUT, 0) == 1) {
        } else {
            if (TextUtils.equals(UtilAd.NATIVE_SMALL, DataPre.getDB(this, Constant.SHORTCUT_NATIVE_SIZE, UtilAd.NATIVE_SMALL))) {
                nativeView = UtilAd.getNativeAdView(TAG_SHORTCUT, R.layout.native_ad_3);
            } else {
                nativeView = UtilAd.getNativeAdView(TAG_SHORTCUT, R.layout.native_ad1);
            }
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
                ll_ad.setVisibility(View.VISIBLE);
            } else {
                UtilAd.showBanner();
            }
        }
    }

    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        short_cut_beijing = (RelativeLayout) findViewById(R.id.short_cut_beijing);
        short_cut2 = (ImageView) findViewById(R.id.short_cut2);
    }

    private void clear(Context context) {
        size = killAll(context);
    }

    private void show_text() {
        if (count == 2) {
            count = 0;
            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
            loadAd();
            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
            ImageView short_delete = (ImageView) view.findViewById(R.id.short_delete);
            if (size < 0) {
                size = 0;
            }
            short_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            short_clean_szie.setText(Util.convertStorage(size, true));
            dialog = new Dialog(ShortCutingActivity.this, R.style.add_dialog);
            dialog.show();
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.dialog_anim);
//		dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = dm.widthPixels; //设置宽度
            lp.height = dm.heightPixels; //设置高度
            if (DataPre.getDB(this, Constant.IS_ACTION_BAR, true)) {
                int uiOptions =
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                //布局位于状态栏下方
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                //隐藏导航栏
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
            window.setAttributes(lp);
            window.setContentView(view);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ShortCutingActivity.this.finish();
                }
            });
        }
    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
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
            if (packageInfo.packageName.equals(context.getPackageName()) || packageInfo.packageName.contains("com.brudermobi")) {
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
        istween = false;
        UtilAd.closeBanner();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        final ImageView short_saoba = (ImageView) findViewById(R.id.short_saoba);

        RotateAnimation animation = new RotateAnimation(0f, -10f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        animation.setDuration(750);
        animation.setRepeatCount(0);
        short_saoba.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RotateAnimation animation2 = new RotateAnimation(-10f, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
                animation2.setDuration(750);
                animation2.setRepeatCount(0);
                short_saoba.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(short_cut2, "rotation", 0f, -359f);
        set.setDuration(300);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatorSet set1 = new AnimatorSet();
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(short_cut2, "rotation", 0f, 359f);
                set1.setDuration(450);
                LinearInterpolator lin = new LinearInterpolator();
                set1.setInterpolator(lin);
                animator1.setRepeatCount(1);//设置重复次数
                set1.play(animator1);
                set1.start();
                set1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        short_cut_beijing.startAnimation(suo);
                        short_cut2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
        LinearInterpolator lin = new LinearInterpolator();
        set.setInterpolator(lin);
        animator1.setRepeatCount(2);//设置重复次数
        set.play(animator1);
        set.start();

        suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                short_cut_beijing.setVisibility(View.GONE);
                count++;
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
}
