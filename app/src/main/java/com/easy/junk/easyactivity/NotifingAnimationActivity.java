package com.easy.junk.easyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easytools.MyConstant;

/**
 */

public class NotifingAnimationActivity extends BaseActivity {
    Button notifi_info_text;
    private static final int REQUSETSET = 110;
//    LottieAnimationView notifi_info_lot;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi_info);
//        notifi_info_lot.setImageAssetsFolder("images/notice/");
//        notifi_info_lot.setAnimation("notifyanim.json");
//        notifi_info_lot.loop(false);
//        notifi_info_lot.setSpeed(0.7f);
//        notifi_info_lot.playAnimation();
//        notifi_info_lot.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (notifi_info_text == null) {
//                    return;
//                }
//                Animation animation1 = AnimationUtils.loadAnimation(NotifingAnimationActivity.this, R.anim.translate_notifi);
//                notifi_info_text.startAnimation(animation1);
//                notifi_info_text.setVisibility(View.VISIBLE);
//            }
//        }, 4000);
        notifi_info_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyUtils.isNotificationListenEnabled(NotifingAnimationActivity.this)) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUSETSET);
                } else {
                    PreData.putDB(NotifingAnimationActivity.this, MyConstant.KEY_NOTIFI, true);
                    startActivity(new Intent(NotifingAnimationActivity.this, MyNotifingActivity.class));
                    onBackPressed();
                }
            }
        });
    }

    @Override
    protected void findId() {
        super.findId();
//        notifi_info_lot = (LottieAnimationView) findViewById(R.id.notifi_info_lot);
        notifi_info_text = (Button) findViewById(R.id.notifi_info_text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSETSET) {
            if (MyUtils.isNotificationListenEnabled(NotifingAnimationActivity.this)) {
                PreData.putDB(NotifingAnimationActivity.this, MyConstant.KEY_NOTIFI, true);
                startActivity(new Intent(NotifingAnimationActivity.this, MyNotifingActivity.class));
                onBackPressed();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
