package com.easy.junk.easyactivity;

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
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easycustomview.ImageAccessor;
import com.easy.junk.easytools.MyConstant;
import com.easy.junk.easytools.SetAdUtil;
import com.twee.module.tweenengine.Tween;
import com.twee.module.tweenengine.TweenManager;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class EasyShortCutingActivity extends BaseActivity {
    private String TAG_SHORTCUT = "easy_shortcut";
    private static final int FLAKE_NUM = 3;
    FrameLayout short_backg;
    private int count;
    private Handler myHandler;
    ImageView short_huojian;
    ImageView short_b;
    private boolean isdoudong;
    private long size;
    private Dialog dialog;
    private Animation rotate;
    private Animation fang;
    private TweenManager tweenManager;
    private boolean istween;
    private Animation suo;
    LinearLayout ll_ad;
    FrameLayout short_fl;
    private View nativeView;


    public long killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final long beforeMem = getAvailMemory(am);
        int count = 0;
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            if (onPause) {
                break;
            }
            if (packageInfo.packageName.equals(context.getPackageName()) || packageInfo.packageName.contains("com.icleaner")) {
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

    private void clear(Context context) {
        size = killAll(context);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(EasyShortCutingActivity.this);
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

    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        short_b = (ImageView) findViewById(R.id.short_b);
        short_huojian = (ImageView) findViewById(R.id.short_huojian);
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

    private void show_text() {
        if (count == 2) {
            count = 0;
            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
            loadAd();
            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
            if (size < 0) {
                size = 0;
            }
            short_clean_szie.setText(MyUtils.convertStorage(size, true));
            dialog = new Dialog(EasyShortCutingActivity.this, R.style.add_dialog);
            dialog.show();
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.dialog_anim);
//		dialog.setCanceledOnTouchOutside(false);
            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = dm.widthPixels; //设置宽度
            lp.height = dm.heightPixels; //设置高度
            if (PreData.getDB(this, MyConstant.IS_ACTION_BAR, true)) {
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
                    EasyShortCutingActivity.this.finish();
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

    private void loadAd() {
        if (PreData.getDB(this, MyConstant.FULL_SHORTCUT, 0) == 1) {
        } else {
            nativeView = SetAdUtil.getNativeAdView(TAG_SHORTCUT, R.layout.native_ad1);
            if (ll_ad != null && nativeView != null) {
                ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
                if (nativeView.getHeight() == MyUtils.dp2px(250)) {
                    layout_ad.height = MyUtils.dp2px(250);
                }
                ll_ad.setLayoutParams(layout_ad);
                ll_ad.addView(nativeView);
                ll_ad.setVisibility(View.VISIBLE);
            } else {
            }
        }
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
                   /* Tween.to(short_huojian, ImageAccessor.BOUNCE_EFFECT, 0.08f).target(hx + x, hy + y, 1f, 1)
                            .ease(TweenEquations.easeInQuad).delay(0)
                            .start(tweenManager);*/
                }
            }
        }).start();

    }


    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
        istween = false;
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        LinearInterpolator lir = new LinearInterpolator();
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator rotation = ObjectAnimator.ofFloat(short_huojian, "rotation", 0, 360f);
        ObjectAnimator rotation1 = ObjectAnimator.ofFloat(short_b, "rotation", 0, 360f);
        set.setInterpolator(lir);
        rotation.setRepeatCount(3);
        rotation1.setRepeatCount(3);
        set.setDuration(500);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                short_fl.startAnimation(suo);
//                count++;
//                isdoudong = false;
//                show_text();
//                short_huojian.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
        set.play(rotation).with(rotation1);
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
//        ObjectAnimator rotation = ObjectAnimator.ofFloat(short_b, "rotation", 0, 360);
//        rotation.setDuration(1200);
//        rotation.setInterpolator(new LinearInterpolator());
//        rotation.setRepeatCount(2);
//        rotation.start();
        /*rotation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });*/

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
