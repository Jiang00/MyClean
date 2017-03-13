package com.supers.clean.junk;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eos.module.tweenengine.Tween;
import com.eos.module.tweenengine.TweenEquations;
import com.eos.module.tweenengine.TweenManager;
import com.supers.clean.junk.activity.BaseActivity;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.myView.ImageAccessor;

import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class ShortCutActivity extends BaseActivity {
    ImageView short_zhuan, short_huojian;
    LinearLayout short_text;
    TextView short_size;
    private Animation rotate;
    private Animation fang;
    private TweenManager tweenManager;
    private boolean istween;
    private boolean isdoudong;
    private long size;
    private int count;
    private Handler myHandler;


    @Override
    protected void findId() {
        super.findId();
        short_zhuan = (ImageView) findViewById(R.id.short_zhuan);
        short_huojian = (ImageView) findViewById(R.id.short_huojian);
        short_text = (LinearLayout) findViewById(R.id.short_text);
        short_size = (TextView) findViewById(R.id.short_size);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_cut);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_zheng);
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        short_zhuan.startAnimation(rotate);
        tweenManager = new TweenManager();
        myHandler = new Handler();
        Tween.registerAccessor(ImageView.class, new ImageAccessor());
        istween = true;
        setAnimationThread();
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

    private void show_text() {
        if (count == 2) {
            short_zhuan.clearAnimation();
            short_zhuan.setVisibility(View.INVISIBLE);
            isdoudong = false;
            short_huojian.setScaleX(1);
            short_huojian.setScaleY(1);
            short_size.setText(CommonUtil.getFileSize4(size));
            ObjectAnimator a = ObjectAnimator.ofFloat(short_huojian, View.TRANSLATION_Y, 0, -1200);
            a.setDuration(300);
            a.start();
            short_text.startAnimation(fang);
            short_text.setVisibility(View.VISIBLE);
            fang.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
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

    private void clear(Context context) {
        size = killAll(context);
    }

    public long killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final long beforeMem = getAvailMemory(am);
        int count = 0;
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
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

    private void startTween() {
        final float hy = short_huojian.getTop();
        final float hx = short_huojian.getLeft();
        isdoudong = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        show_text();
                    }
                }, 2000);
                while (isdoudong) {
                    int x = (int) (Math.random() * (16)) - 8;
                    int y = (int) (Math.random() * (16)) - 8;
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Tween.to(short_huojian, ImageAccessor.BOUNCE_EFFECT, 0.08f).target(hx + x, hy + y, 0.8f, 1)
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


    }

    @Override
    protected void onStop() {
        super.onStop();
        count = 0;
        istween = false;
        finish();
    }
}
