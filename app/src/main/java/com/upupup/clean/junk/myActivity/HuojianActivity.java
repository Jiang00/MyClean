package com.upupup.clean.junk.myActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upupup.clean.util.PreData;
import com.upupup.clean.util.Util;
import com.android.client.AndroidSdk;
import com.upupup.clean.junk.R;
import com.upupup.clean.junk.util.AdUtil;
import com.upupup.clean.junk.util.Constant;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class HuojianActivity extends BaseActivity {

    private static final int FLAKE_NUM = 3;

    FrameLayout short_backg;
    LinearLayout ll_ad;
    FrameLayout short_fl;
    ImageView short_rotate, short_rotate_2;
    private long size;
    private int count;
    private Handler myHandler;
    private View nativeView;
    private String TAG_SHORTCUT = "mutter_shortcut";
    private Animation suo;
    private Dialog dialog;
    private ObjectAnimator translate;
    private long junk_size;
    private int num;
    private boolean ischeck;
    private AnimatorSet animatorSet;

    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        short_rotate = (ImageView) findViewById(R.id.short_rotate);
        short_rotate_2 = (ImageView) findViewById(R.id.short_rotate_2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("autoservice", "HuojianonCreate");
        setContentView(R.layout.layout_short_cut);
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

    private void show_text() {
        if (count == 2) {
            count = 0;
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
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    HuojianActivity.this.finish();
                }
            });
            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
            if (size < 0) {
                size = 0;
            }
            short_clean_szie.setText(getString(R.string.success_3, Util.convertStorage(size, true)));
            if (PreData.getDB(this, Constant.FULL_SHORTCUT, 0) != 1) {
                nativeView = AdUtil.getNativeAdView(TAG_SHORTCUT, R.layout.native_ad_2);
                if (ll_ad != null && nativeView != null) {
                    ll_ad.addView(nativeView);
//                    ll_ad.setVisibility(View.VISIBLE);
                    ll_ad.setVisibility(View.VISIBLE);
                }
            }
            window.setContentView(view);
        }


    }

    private void clear(Context context) {
        size = killAll(context);
    }


    public long killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        final long beforeMem = getAvailMemory(am);
        num = 0;
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packageInfo : installedPackages) {
            if (onPause) {
                break;
            }
            if (packageInfo.packageName.equals(context.getPackageName())) {
                continue;
            }
            am.killBackgroundProcesses(packageInfo.packageName);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                continue;
            }
            ++num;
        }
        final long afterMem = getAvailMemory(am);
        final long M = (afterMem - beforeMem);
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
        animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(short_rotate, View.ROTATION, 0, 3600);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(short_rotate_2, View.ROTATION, 0, 3600);
        animatorSet.play(objectAnimator).with(objectAnimator_2);
        animatorSet.setDuration(4500);
        animatorSet.start();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                short_fl.startAnimation(suo);
                suo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        short_fl.setVisibility(View.GONE);
                        short_rotate.clearAnimation();
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
        if (translate != null) {
            translate.cancel();
        }
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("autoservice", "HuojianonDestroy");
    }
}
