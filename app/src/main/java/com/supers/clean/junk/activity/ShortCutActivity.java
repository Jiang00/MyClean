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
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.entity.JunkInfo;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.eos.module.tweenengine.Tween;
import com.eos.module.tweenengine.TweenEquations;
import com.eos.module.tweenengine.TweenManager;
import com.supers.clean.junk.R;
import com.supers.clean.junk.customeview.BubbleCenterLayout;
import com.supers.clean.junk.customeview.ImageAccessor;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class ShortCutActivity extends BaseActivity {

    private static final int FLAKE_NUM = 3;

    FrameLayout short_backg;
    LinearLayout ll_ad;
    FrameLayout short_fl;
    ImageView rotate_ni;
    ImageView rotate_zheng;
    BubbleCenterLayout bubble_center;
    private Animation rotate;
    private Animation fang;
    private TweenManager tweenManager;
    private boolean istween;
    private boolean isdoudong;
    private long size;
    private long junk_size;
    private int count;
    private Handler myHandler;
    private View nativeView;
    private String TAG_SHORTCUT = "eos_shortcut";
    private Animation suo;
    private Dialog dialog;
    private AnimatorSet animatorSet;

    private LinearLayout auto_ram_l, auto_junk_l, auto_battery_l;
    Animation animation_ram, animation_junk, animation_battery;

    @Override
    protected void findId() {
        super.findId();
        short_backg = (FrameLayout) findViewById(R.id.short_backg);
        short_fl = (FrameLayout) findViewById(R.id.short_fl);
        rotate_ni = (ImageView) findViewById(R.id.rotate_ni);
        rotate_zheng = (ImageView) findViewById(R.id.rotate_zheng);
        bubble_center = (BubbleCenterLayout) findViewById(R.id.bubble_center);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_cut);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_zheng);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        animation_ram = AnimationUtils.loadAnimation(ShortCutActivity.this, R.anim.short_ram);
        animation_junk = AnimationUtils.loadAnimation(ShortCutActivity.this, R.anim.short_junk);
        animation_battery = AnimationUtils.loadAnimation(ShortCutActivity.this, R.anim.short_battery);
        tweenManager = new TweenManager();
        myHandler = new Handler();
        Tween.registerAccessor(ImageView.class, new ImageAccessor());
        istween = true;
        suo = AnimationUtils.loadAnimation(this, R.anim.suo_short);
        startTween();
        new Thread(new Runnable() {
            @Override
            public void run() {
                clear(ShortCutActivity.this);
                try {
                    List<JunkInfo> deletelist = new ArrayList<>();
                    deletelist.addAll(CleanManager.getInstance(ShortCutActivity.this).getApkFiles());

                    for (JunkInfo fileListInfo : deletelist) {
                        CleanManager.getInstance(ShortCutActivity.this).removeApkFiles(fileListInfo);
                        junk_size += fileListInfo.size;
                    }
                    deletelist.clear();
                    deletelist.addAll(CleanManager.getInstance(ShortCutActivity.this).getLogFiles());
                    for (JunkInfo fileListInfo : deletelist) {
                        CleanManager.getInstance(ShortCutActivity.this).removeAppLog(fileListInfo);
                        junk_size += fileListInfo.size;
                    }
                    deletelist.clear();
                    deletelist.addAll(CleanManager.getInstance(ShortCutActivity.this).getAppCaches());
                    for (JunkInfo fileListInfo : deletelist) {
                        CleanManager.getInstance(ShortCutActivity.this).removeAppCache(fileListInfo);
                        junk_size += fileListInfo.size;
                    }
                } catch (Exception e) {
                }
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
        nativeView = AdUtil.getNativeAdView(this, TAG_SHORTCUT, R.layout.native_ad1);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }

    boolean ischeck;

    private void show_text() {
        if (count == 2) {
            ischeck = true;
            count = 0;
            dialog = new Dialog(ShortCutActivity.this, R.style.add_dialog);
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
                    if (ischeck) {
                        PreData.putDB(ShortCutActivity.this, Constant.AUTO_KAIGUAN, true);
                    } else {
                        PreData.putDB(ShortCutActivity.this, Constant.AUTO_KAIGUAN, false);
                    }
                    ShortCutActivity.this.finish();
                }
            });

            if (TextUtils.equals("auto", getIntent().getStringExtra("from"))) {
                View view = getLayoutInflater().inflate(R.layout.layout_auto, null);
                ImageView auto_cha = (ImageView) view.findViewById(R.id.auto_cha);
                final ImageView auto_check = (ImageView) view.findViewById(R.id.auto_check);
                FrameLayout dialog_a = (FrameLayout) view.findViewById(R.id.dialog_a);
                auto_ram_l = (LinearLayout) view.findViewById(R.id.auto_ram_l);
                auto_junk_l = (LinearLayout) view.findViewById(R.id.auto_junk_l);
                auto_battery_l = (LinearLayout) view.findViewById(R.id.auto_battery_l);
                LinearLayout auto_is_first = (LinearLayout) view.findViewById(R.id.auto_is_first);
                TextView auto_ram_size = (TextView) view.findViewById(R.id.auto_ram_size);
                TextView auto_junk_size = (TextView) view.findViewById(R.id.auto_junk_size);
                TextView auto_battery_size = (TextView) view.findViewById(R.id.auto_battery_size);
                ll_ad = (LinearLayout) view.findViewById(R.id.ll_ad);
                Button auto_ok = (Button) view.findViewById(R.id.auto_ok);
                loadAd();
                if (size <= 0) {
                    auto_ram_size.setText(R.string.success_normal);
                } else {
                    auto_ram_size.setText(Util.convertStorage(size, true));
                }
                auto_junk_size.setText(Util.convertStorage(junk_size, true));
                long time_diff = getIntent().getLongExtra("time", 0);
                if (time_diff > 24 * 60 * 60 * 1000) {
                    time_diff = 24 * 60 * 60 * 1000;
                }
                auto_battery_size.setText(Util.millTransFate2(time_diff / 10));

                myHandler.postDelayed(runnable_r, 300);
                myHandler.postDelayed(runnable_j, 500);
                myHandler.postDelayed(runnable_b, 700);

                dialog_a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                auto_cha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                if (PreData.hasDB(this, Constant.AUTO_KAIGUAN)) {
                    auto_is_first.setVisibility(View.GONE);
                    int x = (int) (Math.random() * (10));
                    if (x > 4) {
                        auto_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        auto_ok.setText(R.string.side_power);
                        auto_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                jumpTo(PowerActivity.class);
                                dialog.dismiss();
                            }
                        });
                    }

                } else {
                    auto_check.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ischeck = !ischeck;
                            if (ischeck) {
                                auto_check.setImageResource(R.mipmap.auto_passed);
                            } else {
                                auto_check.setImageResource(R.mipmap.auto_normal);
                            }
                        }
                    });
                    auto_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ischeck) {
                                PreData.putDB(ShortCutActivity.this, Constant.AUTO_KAIGUAN, true);
                            } else {
                                PreData.putDB(ShortCutActivity.this, Constant.AUTO_KAIGUAN, false);
                            }
                            dialog.dismiss();
                        }
                    });
                }

                window.setContentView(view);
            } else {
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
    }

    Runnable runnable_r = new Runnable() {
        @Override
        public void run() {
            if (auto_ram_l != null) {
                auto_ram_l.startAnimation(animation_ram);
                auto_ram_l.setVisibility(View.VISIBLE);
            }
        }
    };
    Runnable runnable_j = new Runnable() {
        @Override
        public void run() {
            if (auto_junk_l != null) {
                auto_junk_l.startAnimation(animation_junk);
                auto_junk_l.setVisibility(View.VISIBLE);
            }
        }
    };
    Runnable runnable_b = new Runnable() {
        @Override
        public void run() {
            if (auto_battery_l != null) {
                auto_battery_l.startAnimation(animation_battery);
                auto_battery_l.setVisibility(View.VISIBLE);
            }
        }
    };

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
                    if (bubble_center != null) {
                        bubble_center.pause();
                        bubble_center.destroy();
                    }
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        count = 0;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (myHandler != null) {
            myHandler.removeCallbacks(run_suo);
            myHandler.removeCallbacks(runnable_r);
            myHandler.removeCallbacks(runnable_j);
            myHandler.removeCallbacks(runnable_b);

        }
        if (suo != null) {
            suo.setAnimationListener(null);
            suo.cancel();
        }
        if (bubble_center != null) {
            bubble_center.pause();
            bubble_center.destroy();
        }
        istween = false;
        finish();
    }
}
