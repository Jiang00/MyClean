package com.bruder.clean.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.bruder.clean.junk.R;
import com.bruder.clean.util.Constant;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;
import com.sample.lottie.LottieAnimationView;

/**
 * Created by Ivy on 2017/4/13.
 */

public class NotifiIfActivity extends BaseActivity {
    Button notifi_info_text;
    private static final int REQUSETSET = 110;
    LottieAnimationView notifi_info_lot;

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
                Animation animation1 = AnimationUtils.loadAnimation(NotifiIfActivity.this, R.anim.translate_notifi);
                notifi_info_text.startAnimation(animation1);
                notifi_info_text.setVisibility(View.VISIBLE);
            }
        }, 4000);
        notifi_info_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isNotificationListenEnabled(NotifiIfActivity.this)) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUSETSET);
                } else {
                    DataPre.putDB(NotifiIfActivity.this, Constant.KEY_NOTIFI, true);
                    startActivity(new Intent(NotifiIfActivity.this, NotifingActivity.class));
                    onBackPressed();
                }
            }
        });
    }

    @Override
    protected void findId() {
        super.findId();
        notifi_info_lot = (LottieAnimationView) findViewById(R.id.notifi_info_lot);
        notifi_info_text = (Button) findViewById(R.id.notifi_info_text);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSETSET) {
            if (Util.isNotificationListenEnabled(NotifiIfActivity.this)) {
                DataPre.putDB(NotifiIfActivity.this, Constant.KEY_NOTIFI, true);
                startActivity(new Intent(NotifiIfActivity.this, NotifingActivity.class));
                onBackPressed();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.e("sdsf", "========1========");
        finish();
    }
}
