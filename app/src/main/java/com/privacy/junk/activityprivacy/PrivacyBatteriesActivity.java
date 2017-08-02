package com.privacy.junk.activityprivacy;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.junk.R;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;

import java.util.Random;


/**
 * Created by on 2017/3/2.
 */

public class PrivacyBatteriesActivity extends BaseActivity {
    ImageView cooling_dianyuan, cooling_dianyuan1;
    LinearLayout cooling_text;
    TextView title_name;
    private static final int FLAKE_NUM = 5;
    TextView cooling_wendu;
    FrameLayout title_left;
    private Random random;
    private int time;
    ObjectAnimator animator1, animator2;
    private Animation suo;
    private Animation rotate_zheng;
    AnimatorSet animatorSet;
    RelativeLayout cooling_fl;

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

        startCoolingAni();

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

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        cooling_dianyuan = (ImageView) findViewById(R.id.cooling_dianyuan);
        cooling_dianyuan1 = (ImageView) findViewById(R.id.cooling_dianyuan1);
        cooling_text = (LinearLayout) findViewById(R.id.cooling_text);
        cooling_wendu = (TextView) findViewById(R.id.cooling_wendu);
        cooling_fl = (RelativeLayout) findViewById(R.id.cooling_fl);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSnow();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startCoolingAni() {
        random = new Random();
        time = random.nextInt(5) + 1;
//        cooling_wendu.setText(time + "℃");
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        animator1 = ObjectAnimator.ofFloat(cooling_dianyuan, "rotation", 0f, 359f);
        animator2 = ObjectAnimator.ofFloat(cooling_dianyuan1, "rotation", 0f, 359f);

        animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(linearInterpolator);
        animator1.setDuration(500);
        animator2.setDuration(600);
        animator1.setRepeatCount(5);
        animator2.setRepeatCount(5);
        animatorSet.play(animator1).with(animator2);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                cooling_fl.startAnimation(suo);
                if (PreData.getDB(PrivacyBatteriesActivity.this, MyConstantPrivacy.FULL_COOL, 0) == 1) {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("wendu", time);
                bundle.putString("from", "cooling");
                jumpToActivity(PrivacySucceedActivity.class, bundle, 1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            jumpTo(MainActivity.class);
        } else {
            setResult(MyConstantPrivacy.COOLING_RESUIL, new Intent().putExtra("wendu", time));
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            onBackPressed();
        }
    }

    private void hideSnow() {
        if (animator1 != null) {
            animator1.cancel();
        }
        if (animator2 != null) {
            animator2.cancel();
        }
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
