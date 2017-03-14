package com.supers.clean.junk.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.fakeView.FlakeView;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class CoolingActivity extends BaseActivity {
    private static final int FLAKE_NUM = 5;
    FrameLayout title_left;
    TextView title_name;
    LinearLayout cooling_piao;
    ImageView cooling_zhuan, cooling_xuehua;
    LinearLayout cooling_text;
    TextView cooling_wendu;

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
    private Animation rotate_zheng;
    private Animation rotate_ni;
    private Animation suo;
    private Animation fang;
    private int time;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_piao = (LinearLayout) findViewById(R.id.cooling_piao);
        cooling_zhuan = (ImageView) findViewById(R.id.cooling_zhuan);
        cooling_xuehua = (ImageView) findViewById(R.id.cooling_xuehua);
        cooling_text = (LinearLayout) findViewById(R.id.cooling_text);
        cooling_wendu = (TextView) findViewById(R.id.cooling_wendu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cooling);

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
        fang = AnimationUtils.loadAnimation(this, R.anim.fang);
        mHandler = new Handler();
        cooling_zhuan.startAnimation(rotate_ni);
        cooling_xuehua.startAnimation(rotate_zheng);
        startCoolingAni();
        suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                cooling_xuehua.setVisibility(View.INVISIBLE);
                cooling_text.startAnimation(fang);
                cooling_text.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }
        });
    }

    private void startCoolingAni() {
        random = new Random();
        time = random.nextInt(5) + 1;
        cooling_wendu.setText(time + "℃");
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 20);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value == 20) {
                    cooling_zhuan.clearAnimation();
                    cooling_zhuan.setVisibility(View.INVISIBLE);
                    cooling_xuehua.startAnimation(suo);
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
        setResult(2, new Intent().putExtra("wendu", time));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null && runnable != null) {
            mHandler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }
}
