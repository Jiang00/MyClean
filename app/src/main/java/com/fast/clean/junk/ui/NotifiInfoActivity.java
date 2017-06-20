package com.fast.clean.junk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.fast.clean.mutil.PreData;
import com.fast.clean.mutil.Util;
import com.sample.lottie.LottieAnimationView;
import com.fast.clean.junk.R;
import com.fast.clean.junk.util.Constant;

/**
 */

public class NotifiInfoActivity extends BaseActivity {
    private static final int REQUSETSET = 110;
    Button notifi_info_text;
    LottieAnimationView notifi_info_lot;

    @Override
    protected void findId() {
        super.findId();
        notifi_info_lot = (LottieAnimationView) findViewById(R.id.notifi_info_lot);
        notifi_info_text = (Button) findViewById(R.id.notifi_info_text);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi_info);
        notifi_info_lot.setImageAssetsFolder("images/cation/");
        notifi_info_lot.setAnimation("cation.json");
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
                if (!Util.isNotificationListenEnabled(NotifiInfoActivity.this)) {
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
            if (Util.isNotificationListenEnabled(NotifiInfoActivity.this)) {
                PreData.putDB(NotifiInfoActivity.this, Constant.KEY_NOTIFI, true);
                startActivity(new Intent(NotifiInfoActivity.this, NotifiActivity.class));
                onBackPressed();
            }
        }
    }
}
