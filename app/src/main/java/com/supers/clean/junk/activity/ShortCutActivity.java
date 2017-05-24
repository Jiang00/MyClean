package com.supers.clean.junk.activity;

import android.animation.Animator;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.eos.module.tweenengine.Tween;
import com.eos.module.tweenengine.TweenEquations;
import com.eos.module.tweenengine.TweenManager;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.FlakeViewOnShort;
import com.supers.clean.junk.customeview.ImageAccessor;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class ShortCutActivity extends BaseActivity {

    private static final int FLAKE_NUM = 3;

    FrameLayout short_backg;
    ImageView short_huojian;
    LinearLayout ll_ad;
    LinearLayout short_xian;
    FrameLayout short_fl;
    private Animation rotate;
    private Animation fang;
    private TweenManager tweenManager;
    private boolean istween;
    private boolean isdoudong;
    private long size;
    private int count;
    private Handler myHandler;
    private View nativeView;
    private String TAG_SHORTCUT = "eos_shortcut";
    private FlakeViewOnShort flakeView;
    private Animation suo;
    private Dialog dialog;

    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        short_huojian = (ImageView) findViewById(R.id.short_huojian);
        short_xian = (LinearLayout) findViewById(R.id.short_xian);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_cut);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_zheng);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        tweenManager = new TweenManager();
        myHandler = new Handler();
        Tween.registerAccessor(ImageView.class, new ImageAccessor());
        istween = true;
        setAnimationThread();
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);

        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTween();
            }
        }, 500);
        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(ShortCutActivity.this);
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
        if (PreData.getDB(this, Constant.FULL_SHORTCUT, 0) == 1) {
        } else {
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
    }


    private void show_text() {
        if (count == 2) {
            count = 0;
            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
            loadAd();
            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
            short_clean_szie.setText(Util.convertStorage(size, true));
            dialog = new Dialog(ShortCutActivity.this, R.style.add_dialog);
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
                    ShortCutActivity.this.finish();
                }
            });
        }
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
            if (packageInfo.packageName.equals(context.getPackageName()) || packageInfo.packageName.contains("com.eosmobi")) {
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

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    private void startTween() {
        final float hy = short_huojian.getTop();
        final float hx = short_huojian.getLeft();
        isdoudong = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isdoudong) {
                    int x = (int) (Math.random() * (10)) - 5;
                    int y = (int) (Math.random() * (10)) - 5;
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Tween.to(short_huojian, ImageAccessor.BOUNCE_EFFECT, 0.08f).target(hx + x, hy + y, 1f, 1)
                            .ease(TweenEquations.easeInQuad).delay(0)
                            .start(tweenManager);
                }
            }
        }).start();

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
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        flakeView = new FlakeViewOnShort(this);
        short_xian.addView(flakeView);
        flakeView.setRotation(35);
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                flakeView.addFlakes(FLAKE_NUM);
            }
        });
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator translationY = ObjectAnimator.ofFloat(short_huojian, "translationY", 0, -Util.dp2px(90));
                ObjectAnimator translationX = ObjectAnimator.ofFloat(short_huojian, "translationX", 0, Util.dp2px(90));
                set.setDuration(100);
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        short_fl.startAnimation(suo);
                        short_huojian.setVisibility(View.GONE);
                        short_xian.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }
                });
                set.play(translationX).with(translationY);
                set.start();
                suo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
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
        }, 4000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (flakeView != null) {
            flakeView.subtractFlakes(FLAKE_NUM);
            flakeView.pause();
            flakeView = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
        istween = false;
        finish();
    }
}
