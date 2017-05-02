package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateYAnimation;
import android.widget.TextView;

import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;

/**
 * Created by Ivy on 2017/4/13.
 */

public class NotifiInfoActivity extends BaseActivity {
    private static final int REQUSETSET = 110;
    LottieAnimationView notifi_info_lot;
    TextView notifi_info_text;

    @Override
    protected void findId() {
        super.findId();
        notifi_info_lot = (LottieAnimationView) findViewById(R.id.notifi_info_lot);
        notifi_info_text = (TextView) findViewById(R.id.notifi_info_text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi_info);
        notifi_info_lot.setImageAssetsFolder("images/notifi/");
        notifi_info_lot.setAnimation("notifi.json");
        notifi_info_lot.loop(false);
        notifi_info_lot.setSpeed(0.7f);
        notifi_info_lot.playAnimation();
        notifi_info_lot.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (notifi_info_text == null) {
                    return;
                }
                Animation animation1 = AnimationUtils.loadAnimation(NotifiInfoActivity.this, R.anim.translate_notifi);
                notifi_info_text.startAnimation(animation1);
                notifi_info_text.setVisibility(View.VISIBLE);
            }
        }, 4000);
        notifi_info_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isNotificationListenEnabled(NotifiInfoActivity.this)) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUSETSET);
                } else {
                    PreData.putDB(NotifiInfoActivity.this, Constant.KEY_NOTIFI, true);
                    startActivity(new Intent(NotifiInfoActivity.this, NotifiActivity.class));
                    onBackPressed();
                }

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
            if (CommonUtil.isNotificationListenEnabled(NotifiInfoActivity.this)) {
                PreData.putDB(NotifiInfoActivity.this, Constant.KEY_NOTIFI, true);
                startActivity(new Intent(NotifiInfoActivity.this, NotifiActivity.class));
                onBackPressed();
            }
        }
    }
}