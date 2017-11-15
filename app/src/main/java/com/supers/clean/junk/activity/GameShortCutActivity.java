package com.supers.clean.junk.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.eos.module.tweenengine.Tween;
import com.eos.module.tweenengine.TweenManager;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.ImageAccessor;
import com.supers.clean.junk.util.AdUtil;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class GameShortCutActivity extends BaseActivity {

    private static final int FLAKE_NUM = 3;

    FrameLayout short_backg;
    LinearLayout ll_ad;
    FrameLayout short_fl;
    ImageView rotate_ni;
    ImageView short_icon;
    ImageView rotate_zheng;
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
    private Animation suo;
    private Dialog dialog;
    private AnimatorSet animatorSet;

    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        rotate_ni = (ImageView) findViewById(R.id.rotate_ni);
        short_icon = (ImageView) findViewById(R.id.short_icon);
        rotate_zheng = (ImageView) findViewById(R.id.rotate_zheng);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_cut);
        short_icon.setImageResource(R.mipmap.short_2);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_zheng);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        tweenManager = new TweenManager();
        myHandler = new Handler();
        Tween.registerAccessor(ImageView.class, new ImageAccessor());
        istween = true;
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);
        startTween();
        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(GameShortCutActivity.this);
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
            nativeView = AdUtil.getNativeAdView(this,TAG_SHORTCUT, R.layout.native_ad1);
            if (ll_ad != null && nativeView != null) {
                ll_ad.addView(nativeView);
                ll_ad.setVisibility(View.VISIBLE);
            } else {
            }
        }
    }


    private void show_text() {
        if (count == 2) {
            count = 0;
            Bundle bundle = new Bundle();
            bundle.putString("name", (String) getText(R.string.gboost_8));
            bundle.putString("from", "GBoost");
            bundle.putInt("game", 1);
            jumpToActivity(SuccessActivity.class, bundle);
            finish();
//            View view = getLayoutInflater().inflate(R.layout.layout_short_dialog, null);
//            ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
//            loadAd();
//            TextView short_clean_szie = (TextView) view.findViewById(R.id.short_clean_szie);
//            if (size < 0) {
//                size = 0;
//            }
//            short_clean_szie.setText(Util.convertStorage(size, true));
//            dialog = new Dialog(ShortCutActivity.this, R.style.add_dialog);
//            dialog.show();
//            Window window = dialog.getWindow();
//            window.setWindowAnimations(R.style.dialog_anim);
////		dialog.setCanceledOnTouchOutside(false);
//            DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//            lp.width = dm.widthPixels; //设置宽度
//            lp.height = dm.heightPixels; //设置高度
//            window.setAttributes(lp);
//            window.setContentView(view);
//            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    ShortCutActivity.this.finish();
//                }
//            });
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
            if (packageInfo.packageName.equals(context.getPackageName()) || packageInfo.packageName.contains("com.eosmobi")
                    || packageInfo.packageName.contains("com.google") || packageInfo.packageName.contains("com.android.vending")) {
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
        animatorSet = new AnimatorSet();
        ObjectAnimator ni = ObjectAnimator.ofFloat(rotate_ni, View.ROTATION, 0, -3600);
        ObjectAnimator zheng = ObjectAnimator.ofFloat(rotate_zheng, View.ROTATION, 0, 3600);
        animatorSet.setDuration(4500);
        animatorSet.play(ni).with(zheng);
        animatorSet.start();
        myHandler.postDelayed(run_suo, 4000);
    }

    Runnable run_suo = new Runnable() {
        @Override
        public void run() {
            short_fl.startAnimation(suo);
            suo.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    short_fl.setVisibility(View.GONE);
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
    };

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (myHandler != null) {
            myHandler.removeCallbacks(run_suo);
        }
        if (suo != null) {
            suo.setAnimationListener(null);
            suo.cancel();
        }
        istween = false;
        finish();
    }
}
