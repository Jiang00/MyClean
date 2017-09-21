package com.froumobic.clean.junk.mactivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.froumobic.clean.junk.R;
import com.froumobic.clean.junk.mview.CoolongView;
import com.froumobic.clean.junk.util.AdUtil;
import com.froumobic.clean.junk.util.Constant;
import com.froumobic.clean.junk.mview.FlakeView;
import com.android.clean.util.PreData;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class CoolingActivity extends MBaseActivity {
    private static final int FLAKE_NUM = 5;
    FrameLayout title_left;
    TextView title_name;
    CoolongView cooling_piao;
    ImageView cooling_xuehua;
    FrameLayout fl_lot_cooling;
    RelativeLayout cooling_fl;

    private Handler mHandler = new Handler();
    private Random random;
    private Animation suo;
    private int time;
    private ObjectAnimator objectAnimator;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_piao = (CoolongView) findViewById(R.id.cooling_piao);
        cooling_xuehua = (ImageView) findViewById(R.id.cooling_xuehua);
        cooling_fl = (RelativeLayout) findViewById(R.id.cooling_fl);
        fl_lot_cooling = (FrameLayout) findViewById(R.id.fl_lot_cooling);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cooling);
        AndroidSdk.loadFullAd(AdUtil.DEFAULT_FULL,null);

        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        title_name.setText(R.string.main_cooling_name);

        suo = AnimationUtils.loadAnimation(this, R.anim.suo);
        mHandler = new Handler();
        objectAnimator = ObjectAnimator.ofFloat(cooling_xuehua, View.ROTATION, 0, 3600);
        objectAnimator.setDuration(3600);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
        startCoolingAni();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (PreData.getDB(CoolingActivity.this, Constant.FULL_COOL, 0) == 1) {
                    AndroidSdk.showFullAd(AdUtil.DEFAULT_FULL);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("wendu", time);
                bundle.putString("from", "cooling");
                jumpToActivity(SuccessActivity.class, bundle, 1);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });

        suo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                cooling_fl.setVisibility(View.INVISIBLE);
                objectAnimator.cancel();
                if (PreData.getDB(CoolingActivity.this, Constant.FULL_COOL, 0) == 1) {
                    AndroidSdk.showFullAd(AdUtil.DEFAULT_FULL);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("wendu", time);
                bundle.putString("from", "cooling");
                jumpToActivity(SuccessActivity.class, bundle, 1);

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
//        cooling_wendu.setText(time + "℃");
//        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 20);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int value = (int) animation.getAnimatedValue();
//                if (value == 20) {
//                    cooling_fl.startAnimation(suo);
//
//                }
//            }
//        });
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.setDuration(3000);
//        valueAnimator.start();

    }


    @Override
    protected void onResume() {
        super.onResume();
        cooling_piao.reStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cooling_piao != null) {
            cooling_piao.pause();
        }
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
        super.onDestroy();
        if (cooling_piao != null) {
            cooling_piao.destroy();
        }
    }


}
