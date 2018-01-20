package com.mutter.clean.junk.myActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myview.BubbleLineLayout;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class HuojianJiangwenActivity extends BaseActivity {

    private static final int FLAKE_NUM = 3;

    FrameLayout short_backg;
    LinearLayout ll_ad;
    ImageView float_zhuan;
    BubbleLineLayout bubble_line;
    FrameLayout clean_button;

    private long size;
    private int count;
    private Handler myHandler;
    private View nativeView;
    private String TAG_SHORTCUT = "mutter_shortcut";
    private Animation suo;
    private Dialog dialog;
    private ObjectAnimator rotate_x;
    private ObjectAnimator translate;
    private long junk_size;
    private int num;
    private boolean ischeck;
    private AnimatorSet animatorSet;


    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        bubble_line = (BubbleLineLayout) findViewById(R.id.bubble_line);
        float_zhuan = (ImageView) findViewById(R.id.float_zhuan);
        clean_button = (FrameLayout) findViewById(R.id.clean_button);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("autoservice", "HuojianonCreate");
        setContentView(R.layout.layout_short_cut_jiangwen);

        myHandler = new Handler();
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);
        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(HuojianJiangwenActivity.this);
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
            dialog = new Dialog(HuojianJiangwenActivity.this, R.style.add_dialog);
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
                    HuojianJiangwenActivity.this.finish();
                }
            });
            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
            LinearLayout doalig_result = (LinearLayout) view.findViewById(R.id.doalig_result);
            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
            if (size < 0) {
                size = 0;
            }
            short_clean_szie.setText(getString(R.string.success_5, (Math.random() * 3 + 1)) + "℃");
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

    private void loadAd() {

    }


    public long killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                clean_button.startAnimation(suo);
                suo.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        clean_button.setVisibility(View.INVISIBLE);
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
        }, 3000);
        startCleanAnimation();
    }

    private void startCleanAnimation() {
        bubble_line.setParticleBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.cooling_0));
        bubble_line.reStart();
        animatorSet = new AnimatorSet();
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(float_zhuan, View.ROTATION, 0, 3600);
        objectAnimator_2.setDuration(3500);
        animatorSet.play(objectAnimator_2);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bubble_line.pause();
                bubble_line.destroy();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
        if (rotate_x != null) {
            rotate_x.removeAllListeners();
            rotate_x.cancel();
        }
        if (translate != null) {
            translate.cancel();
        }
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (bubble_line != null) {
            bubble_line.pause();
            bubble_line.destroy();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("autoservice", "HuojianonDestroy");
    }
}
