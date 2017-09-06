package com.mutter.clean.junk.myActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.myview.FlakeView;
import com.mutter.clean.util.PreData;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class JiangwenActivity extends BaseActivity {
    private static final int FLAKE_NUM = 5;
    ImageView cooling_xuehua;
    ImageView cooling_kuo;
    LinearLayout cooling_text;
    FrameLayout cooling_fl;
    TextView cooling_wendu;
    FrameLayout title_left;
    TextView title_name;
    LinearLayout cooling_piao;

    private FlakeView flakeView;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (flakeView != null) {
                flakeView.addFlakes(FLAKE_NUM);
            }
        }
    };
    private Random random;
    private Animation rotate_ni;
    private Animation suo;
    private Animation rotate_zheng;
    private AnimatorSet animationLine_1;
    private int time;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_piao = (LinearLayout) findViewById(R.id.cooling_piao);
        cooling_xuehua = (ImageView) findViewById(R.id.cooling_xuehua);
        cooling_kuo = (ImageView) findViewById(R.id.cooling_kuo);
        cooling_fl = (FrameLayout) findViewById(R.id.cooling_fl);
        cooling_text = (LinearLayout) findViewById(R.id.cooling_text);
        cooling_wendu = (TextView) findViewById(R.id.cooling_wendu);
    }

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

        rotate_zheng = AnimationUtils.loadAnimation(this, R.anim.rotate_zheng);
        rotate_ni = AnimationUtils.loadAnimation(this, R.anim.rotate_ni);
        suo = AnimationUtils.loadAnimation(this, R.anim.suo);
        mHandler = new Handler();
        cooling_xuehua.startAnimation(rotate_zheng);
        startCoolingAni();

        animationLine_1 = new AnimatorSet();
        ObjectAnimator animator_ine_1_x = ObjectAnimator.ofFloat(cooling_kuo, "scaleX", 0, 2f);
        animator_ine_1_x.setRepeatCount(-1);
        ObjectAnimator animator_ine_1_y = ObjectAnimator.ofFloat(cooling_kuo, "scaleY", 0, 2f);
        ObjectAnimator animator_ine_1_r = ObjectAnimator.ofFloat(cooling_kuo, "alpha", 1, 0f);
        animator_ine_1_y.setRepeatCount(-1);
        animator_ine_1_r.setRepeatCount(-1);
        animationLine_1.setDuration(1000);
        animationLine_1.setInterpolator(new AccelerateDecelerateInterpolator());
        animationLine_1.playTogether(animator_ine_1_x, animator_ine_1_y, animator_ine_1_r);
        animationLine_1.start();

        suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                cooling_fl.setVisibility(View.INVISIBLE);
                cooling_xuehua.clearAnimation();
                if (PreData.getDB(JiangwenActivity.this, Constant.FULL_COOL, 0) == 1) {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("wendu", time);
                bundle.putString("from", "cooling");
                bundle.putString("name", getString(R.string.cooling_succ));
                jumpToActivity(SuccessActivity.class, bundle, 1);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
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
    protected void onPause() {
        super.onPause();
        hideSnow();
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            jumpTo(MainActivity.class);
        } else {
            setResult(Constant.COOLING_RESUIL, new Intent().putExtra("wendu", time));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }

    private void startCoolingAni() {
        random = new Random();
        time = random.nextInt(5) + 1;
//        cooling_wendu.setText(time + "℃");
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 20);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value == 20) {
                    if (animationLine_1 != null) {
                        animationLine_1.cancel();
                    }
                    if (cooling_fl != null) {
                        cooling_fl.startAnimation(suo);
                    }
                    hideSnow();
                }
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(3000);
        valueAnimator.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView = new FlakeView(this);
        cooling_piao.addView(flakeView);
        mHandler.post(runnable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
    }
}
