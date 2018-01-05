package com.upupup.clean.junk.myActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.upupup.clean.util.PreData;
import com.upupup.clean.util.Util;
import com.sample.lottie.LottieAnimationView;
import com.upupup.clean.junk.R;
import com.upupup.clean.junk.util.Constant;

/**
 */

public class NotifiAnimationActivity extends BaseActivity {
    private static final int REQUSETSET = 110;
    Button notifi_info_text;
    LottieAnimationView notifi_info_lot;
    Handler handler;

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
        handler = new Handler();
        notifi_info_lot.loop(true);
        notifi_info_lot.setSpeed(0.5f);
        notifi_info_lot.playAnimation();
        notifi_info_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isNotificationListenEnabled(NotifiAnimationActivity.this)) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUSETSET);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent transintent = new Intent(NotifiAnimationActivity.this, ShowPermissionActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);
                } else {
                    PreData.putDB(NotifiAnimationActivity.this, Constant.KEY_NOTIFI, true);
                    startActivity(new Intent(NotifiAnimationActivity.this, NotifiActivity.class));
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
            if (Util.isNotificationListenEnabled(NotifiAnimationActivity.this)) {
                PreData.putDB(NotifiAnimationActivity.this, Constant.KEY_NOTIFI, true);
                startActivity(new Intent(NotifiAnimationActivity.this, NotifiActivity.class));
                onBackPressed();
            }
        }
    }
}
