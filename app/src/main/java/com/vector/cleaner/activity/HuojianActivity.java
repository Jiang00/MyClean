package com.vector.cleaner.activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.sample.lottie.LottieAnimationView;
import com.vector.cleaner.utils.AdUtil;
import com.vector.cleaner.utils.Constant;
import com.vector.cleaner.R;
import com.vector.mcleaner.mutil.PreData;
import com.vector.mcleaner.mutil.Util;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class HuojianActivity extends BaseActivity {

    private static final int FLAKE_NUM = 3;

    FrameLayout short_backg;
    private int count;
    private Handler myHandler;
    private boolean isdoudong;
    private long size;
    private View nativeView;
    LottieAnimationView short_b;
    LinearLayout ll_ad;
    FrameLayout short_fl;
    private Animation rotate;
    private Animation fang;
    private Dialog dialog;
    private String TAG_SHORTCUT = "vector_shortcut";
    private Animation suo;
    private ObjectAnimator rotation;

    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        short_b = (LottieAnimationView) findViewById(R.id.short_b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_cut);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_zheng);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        myHandler = new Handler();
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);

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

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    private void clear(Context context) {
        size = killAll(context);
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
            if (packageInfo.packageName.equals(context.getPackageName()) || packageInfo.packageName.contains("com.vectorapps")) {
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
        short_b.setImageAssetsFolder("images/short");
        short_b.setAnimation("jiasu.json");
        short_b.loop(true);
        short_b.playAnimation();
//        rotation = ObjectAnimator.ofFloat(short_b, "rotation", 0, 850);
//        rotation.setDuration(3000);
//        rotation.setInterpolator(new AccelerateDecelerateInterpolator());
//        rotation.setRepeatCount(1);
//        rotation.start();
        myHandler.postDelayed(runnable, 4000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            short_fl.startAnimation(suo);
            suo.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
//                    rotation.cancel();
                    if (short_b != null) {
                        short_b.pauseAnimation();
                    }
                    short_b.clearAnimation();
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
        if (short_b != null) {
            short_b.pauseAnimation();
        }
        count = 0;
        finish();
    }
}
