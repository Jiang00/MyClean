package com.icleaner.junk.icleaneractivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.icleaner.clean.utils.PreData;
import com.icleaner.junk.R;
import com.icleaner.junk.mycustomview.BatteryScanView;
import com.icleaner.junk.mycustomview.FlakeView;
import com.icleaner.junk.mytools.MyConstant;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class BatteriesActivity extends BaseActivity {
    private static final int FLAKE_NUM = 5;
    TextView cooling_wendu;
    FrameLayout title_left;
    LinearLayout cooling_piao;
    //    FrameLayout cooling_fl;
    private Animation suo;
    private Animation rotate_zheng;
    private Random random;
    private int time;
    private FlakeView flakeView;
    LinearLayout cooling_text;
    ImageView cooling_xuehua;
    ImageView cooling_2;
    TextView title_name;
    BatteryScanView cooling_1;

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (flakeView != null) {
                flakeView.addFlakes(FLAKE_NUM);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cooling);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);

        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title_name.setText(R.string.main_cooling_name);

        rotate_zheng = AnimationUtils.loadAnimation(this, R.anim.rotate_cooling);
        suo = AnimationUtils.loadAnimation(this, R.anim.suo);
        mHandler = new Handler();
        cooling_xuehua.startAnimation(rotate_zheng);
        startCoolingAni();



     /*   suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                cooling_fl.setVisibility(View.INVISIBLE);
                cooling_xuehua.clearAnimation();
                if (PreData.getDB(BatteriesActivity.this, MyConstant.FULL_COOL, 0) == 1) {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("wendu", time);
                bundle.putString("from", "cooling");
                jumpToActivity(SucceedActivity.class, bundle, 1);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });*/
        if (TextUtils.equals("main", getIntent().getStringExtra("from"))) {
            cooling_text.setVisibility(View.VISIBLE);
            final int wendu = getIntent().getIntExtra("wendu", 40);
            cooling_wendu.setText(wendu + "℃");
            for (int i = 0; i <= time; i++) {
                cooling_wendu.setText((wendu - i) + "℃");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {

                    for (int i = 0; i <= time; i++) {
                        if (onPause) {
                            return;
                        }
                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cooling_wendu.setText((wendu - finalI) + "℃");
                            }
                        });
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_piao = (LinearLayout) findViewById(R.id.cooling_piao);
        cooling_xuehua = (ImageView) findViewById(R.id.cooling_xuehua);
//        cooling_fl = (FrameLayout) findViewById(R.id.cooling_fl);
        cooling_text = (LinearLayout) findViewById(R.id.cooling_text);
        cooling_2 = (ImageView) findViewById(R.id.cooling_2);
        cooling_wendu = (TextView) findViewById(R.id.cooling_wendu);
        cooling_1 = (BatteryScanView) findViewById(R.id.cooling_1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView = new FlakeView(this);
        cooling_piao.addView(flakeView);
        mHandler.post(runnable);
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            jumpTo(MainActivity.class);
        } else {
            setResult(MyConstant.COOLING_RESUIL, new Intent().putExtra("wendu", time));
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSnow();
    }

    private void startCoolingAni() {
        random = new Random();
        time = random.nextInt(5) + 1;
//        cooling_wendu.setText(time + "℃");
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 20);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(cooling_1, "scaleY", 1f, 0f);
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(cooling_1, "scaleX", 1f, 0f);
//        AnimatorSet animSet = new AnimatorSet();
//        animSet.setDuration(2000);//设置动画持续时间
//        animSet.play(animator).with(animator2);
//        animSet.start();

        ScaleAnimation s = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);//从上往下
        cooling_2.setAnimation(s);
        s.setDuration(2000);
        s.startNow();//开始动画
        ScaleAnimation s1 = new ScaleAnimation(1f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 1f);//从上往下
        cooling_1.setAnimation(s);
        s1.setDuration(2000);
//        s1.startNow();//开始动画
        mHandler.postDelayed(new Runnable() {
            public void run() {
                ScaleAnimation s = new ScaleAnimation(1f, 1f, 1f, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);//从下往上
//                ObjectAnimator animator = ObjectAnimator.ofFloat(cooling_1, "scaleY", 0f, 1f);
//                ObjectAnimator animator2 = ObjectAnimator.ofFloat(cooling_1, "scaleX", 0f, 1f);
//                AnimatorSet animSet = new AnimatorSet();
//                animSet.setDuration(2000);//设置动画持续时间
                cooling_2.setAnimation(s);
//                animSet.play(animator).with(animator2);
//                animSet.start();
                s.setDuration(2000);//设置动画持续时间
                cooling_2.setAnimation(s);
                s.startNow();//开始动画

            }
        }, 2000); //延迟2秒跳转
        mHandler.postDelayed(new Runnable() {
            public void run() {
//                cooling_1.setVisibility(View.VISIBLE);
            }
        }, 3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value == 20) {
//                    cooling_fl.startAnimation(suo);
                    hideSnow();
                }
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(2000);
        valueAnimator.start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                cooling_fl.setVisibility(View.INVISIBLE);
//                cooling_xuehua.clearAnimation();
                if (PreData.getDB(BatteriesActivity.this, MyConstant.FULL_COOL, 0) == 1) {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("wendu", time);
                bundle.putString("from", "cooling");
                jumpToActivity(SucceedActivity.class, bundle, 1);
            }
        }, 4000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
    }

    private void hideSnow() {
        if (flakeView != null) {
            flakeView.subtractFlakes(FLAKE_NUM);
            flakeView.pause();
            flakeView = null;
            mHandler.removeCallbacks(runnable);
        }
        if (cooling_piao != null) {
            cooling_piao.removeAllViews();
            cooling_piao.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }
}
