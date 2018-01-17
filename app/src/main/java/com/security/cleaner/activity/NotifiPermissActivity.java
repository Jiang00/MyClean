package com.security.cleaner.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.security.mcleaner.mutil.PreData;
import com.security.mcleaner.mutil.Util;
import com.security.cleaner.R;
import com.security.cleaner.utils.Constant;

/**
 */

public class NotifiPermissActivity extends BaseActivity {
    private static final int REQUSETSET = 110;
    Button notifi_info_text;
    Handler myHandler;
    TextView title_name;
    FrameLayout title_left;
    ImageView notifi_animation_1, notifi_animation_2, notifi_animation_3, notifi_animation_4, notifi_animation_5;
    LinearLayout notifi_animation;
    private AnimatorSet animatorSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi_info);
        title_name.setText(R.string.side_notifi);
        myHandler = new Handler();
//        Animation animation1 = AnimationUtils.loadAnimation(NotifiPermissActivity.this, R.anim.translate_notifi);
//        notifi_info_text.startAnimation(animation1);
        notifi_info_text.setVisibility(View.VISIBLE);
        if (Util.isNotificationListenEnabled(NotifiPermissActivity.this) && PreData.getDB(NotifiPermissActivity.this, Constant.KEY_NOTIFI, true)) {
            PreData.putDB(NotifiPermissActivity.this, Constant.KEY_NOTIFI, true);
            startActivity(new Intent(NotifiPermissActivity.this, NotificationActivity.class));
            onBackPressed();
        }
        notifi_info_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isNotificationListenEnabled(NotifiPermissActivity.this)) {
                    try {
                        startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUSETSET);
                        myHandler.postDelayed(runnable_ac, 1500);
                        myHandler.post(runnable_per);
                    } catch (Exception e) {
                    }
                } else {
                    PreData.putDB(NotifiPermissActivity.this, Constant.KEY_NOTIFI, true);
                    startActivity(new Intent(NotifiPermissActivity.this, NotificationActivity.class));
                    onBackPressed();
                }

            }
        });

        myHandler.postDelayed(runnable_animation, 1000);
    }

    Runnable runnable_animation = new Runnable() {
        @Override
        public void run() {
            startAnimation();
            myHandler.postDelayed(this, 2000);
        }
    };
    Runnable runnable_ac = new Runnable() {
        @Override
        public void run() {
            Intent transintent = new Intent(NotifiPermissActivity.this, PermissionActivity.class);
            startActivity(transintent);
        }
    };
    Runnable runnable_per = new Runnable() {
        @Override
        public void run() {
            if (Util.isNotificationListenEnabled(NotifiPermissActivity.this)) {
                startActivity(new Intent(NotifiPermissActivity.this, NotifiPermissActivity.class));
            } else {
                myHandler.postDelayed(this, 1000);
            }
        }
    };

    private void startAnimation() {
        int hight = notifi_animation_3.getHeight() + getResources().getDimensionPixelOffset(R.dimen.d6);
        notifi_animation_3.setTranslationY(0);
        notifi_animation_4.setTranslationY(0);
        notifi_animation_4.setTranslationY(0);
        notifi_animation.setVisibility(View.VISIBLE);
        notifi_animation_1.setVisibility(View.INVISIBLE);
        animatorSet = new AnimatorSet();
        ObjectAnimator objectanimator_1 = ObjectAnimator.ofFloat(notifi_animation_3, View.TRANSLATION_Y, 0,
                -hight);
        ObjectAnimator objectanimator_2 = ObjectAnimator.ofFloat(notifi_animation_4, View.TRANSLATION_Y, 0,
                -hight * 2);
        ObjectAnimator objectanimator_3 = ObjectAnimator.ofFloat(notifi_animation_5, View.TRANSLATION_Y, 0,
                -hight * 3);
        animatorSet.play(objectanimator_1).with(objectanimator_2).with(objectanimator_3);
        animatorSet.setDuration(1100);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                notifi_animation_1.setVisibility(View.VISIBLE);
                notifi_animation.setVisibility(View.INVISIBLE);
                animatorSet = new AnimatorSet();
                ObjectAnimator animator_1 = ObjectAnimator.ofFloat(notifi_animation_1, View.SCALE_X, 1, 1.1f, 1);
                ObjectAnimator animator_2 = ObjectAnimator.ofFloat(notifi_animation_1, View.SCALE_Y, 1, 1.1f, 1);
                animatorSet.playTogether(animator_1, animator_2);
                animatorSet.setDuration(500);
                animatorSet.start();
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
    protected void findId() {
        super.findId();
        notifi_info_text = (Button) findViewById(R.id.notifi_info_text);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        notifi_animation_1 = (ImageView) findViewById(R.id.notifi_animation_1);
        notifi_animation_2 = (ImageView) findViewById(R.id.notifi_animation_2);
        notifi_animation_3 = (ImageView) findViewById(R.id.notifi_animation_3);
        notifi_animation_4 = (ImageView) findViewById(R.id.notifi_animation_4);
        notifi_animation_5 = (ImageView) findViewById(R.id.notifi_animation_5);
        notifi_animation = (LinearLayout) findViewById(R.id.notifi_animation);
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSETSET) {
            if (myHandler != null) {
                myHandler.removeCallbacks(runnable_ac);
            }
            if (Util.isNotificationListenEnabled(NotifiPermissActivity.this)) {
                PreData.putDB(NotifiPermissActivity.this, Constant.KEY_NOTIFI, true);
                startActivity(new Intent(NotifiPermissActivity.this, NotificationActivity.class));
                onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
        if (animatorSet != null) {
            animatorSet.removeAllListeners();
            animatorSet.cancel();
        }
    }
}
