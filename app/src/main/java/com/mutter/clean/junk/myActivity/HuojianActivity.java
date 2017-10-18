package com.mutter.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import com.mutter.clean.junk.myview.BubbleCenterLayout;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class HuojianActivity extends BaseActivity {

    private static final int FLAKE_NUM = 3;

    FrameLayout short_backg;
    LinearLayout ll_ad;
    FrameLayout short_fl;
    BubbleCenterLayout bubble;
    ImageView short_rotate;
    private Animation rotate;
    private Animation fang;
    private long size;
    private int count;
    private Handler myHandler;
    private View nativeView;
    private String TAG_SHORTCUT = "mutter_shortcut";
    private Animation suo;
    private Dialog dialog;
    private ObjectAnimator rotate_x;
    private ObjectAnimator translate;

    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        short_rotate = (ImageView) findViewById(R.id.short_rotate);
        bubble = (BubbleCenterLayout) findViewById(R.id.bubble);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_cut);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_zheng);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        myHandler = new Handler();
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);
        short_rotate.startAnimation(rotate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(HuojianActivity.this);
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

    private void show_text() {
        if (count == 2) {
            count = 0;
            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
            LinearLayout doalig_result = (LinearLayout) view.findViewById(R.id.doalig_result);
            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
            if (size < 0) {
                size = 0;
            }
            short_clean_szie.setText(Util.convertStorage(size, true));
            if (PreData.getDB(this, Constant.FULL_SHORTCUT, 0) != 1) {
                nativeView = AdUtil.getNativeAdView(TAG_SHORTCUT, R.layout.native_ad_2);
                if (ll_ad != null && nativeView != null) {
                    ll_ad.addView(nativeView);
//                    ll_ad.setVisibility(View.VISIBLE);
                    ll_ad.setVisibility(View.INVISIBLE);
                }
            }
            rotate_x = ObjectAnimator.ofFloat(doalig_result, View.ROTATION_X, 0, 360);
            rotate_x.setDuration(1500);
            rotate_x.setStartDelay(500);
            rotate_x.start();
            rotate_x.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (nativeView != null) {
                        translate = ObjectAnimator.ofFloat(ll_ad, View.TRANSLATION_Y, -ll_ad.getHeight(), 0);
                        translate.setDuration(500);
                        translate.start();
                        ll_ad.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationStart(Animator animation) {

                }
            });
            dialog = new Dialog(HuojianActivity.this, R.style.add_dialog);
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
                    HuojianActivity.this.finish();
                }
            });
        }
    }

    private void clear(Context context) {
        size = killAll(context);
    }

    private void loadAd() {

    }


    public long killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final long beforeMem = getAvailMemory(am);
        int count = 0;
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packageInfo : installedPackages) {
            if (onPause) {
                break;
            }
            if (packageInfo.packageName.equals(context.getPackageName())) {
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


    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bubble.pause();
                short_fl.startAnimation(suo);
                suo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        short_fl.setVisibility(View.GONE);
                        short_rotate.clearAnimation();
                        bubble.destroy();
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        short_rotate.clearAnimation();
        count = 0;
        if (bubble != null) {
            bubble.pause();
            bubble.destroy();
        }
        if (rotate_x != null) {
            rotate_x.removeAllListeners();
            rotate_x.cancel();
        }
        if (translate != null) {
            translate.cancel();
        }
        finish();
    }
}
