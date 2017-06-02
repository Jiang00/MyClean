package com.supers.clean.junk.mactivity;

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

import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class ShortCutActivity extends MBaseActivity {


    ImageView short_backg;
    ImageView short_huojian;
    ImageView short_zhuan;
    LinearLayout ll_ad;
    FrameLayout short_fl;
    private long size;
    private int count;
    private Handler myHandler;
    private View nativeView;
    private String TAG_SHORTCUT = "eos_shortcut";
    private Animation suo;
    private Dialog dialog;

    @Override
    protected void findId() {
        super.findId();
        short_backg = (ImageView) findViewById(R.id.short_backg);
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        short_huojian = (ImageView) findViewById(R.id.short_huojian);
        short_zhuan = (ImageView) findViewById(R.id.short_zhuan);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
        finish();
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

        startAnimation();
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
            if (size < 0) {
                size = 0;
            }
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
            if (onPause) {
                break;
            }
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

    private void startAnimation() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(short_huojian, "rotation", 0, -5, 0, 5, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        short_huojian.setPivotX(getResources().getDimensionPixelOffset(R.dimen.d84) / 2);
        short_huojian.setPivotY(0);
        animator.setDuration(500);
        animator.start();
        ObjectAnimator animator_backg = ObjectAnimator.ofFloat(short_backg, "rotation", 0, 360);
        animator_backg.setInterpolator(new LinearInterpolator());
        animator_backg.setRepeatCount(-1);
        animator_backg.setDuration(1000);
        animator_backg.start();
        ObjectAnimator animator_zhuan = ObjectAnimator.ofFloat(short_zhuan, "rotation", 0, 360);
        animator_zhuan.setInterpolator(new LinearInterpolator());
        animator_zhuan.setRepeatCount(-1);
        animator_zhuan.setDuration(1000);
        animator_zhuan.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                short_fl.startAnimation(suo);
                short_huojian.setVisibility(View.GONE);
                suo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        short_fl.setVisibility(View.GONE);
                        short_huojian.clearAnimation();
                        short_zhuan.clearAnimation();
                        short_backg.clearAnimation();
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
        }, 4000);

    }

}
