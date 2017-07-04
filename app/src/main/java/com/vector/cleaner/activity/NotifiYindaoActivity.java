package com.vector.cleaner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.vector.mcleaner.mutil.PreData;
import com.vector.mcleaner.mutil.Util;
import com.vector.cleaner.R;
import com.vector.cleaner.utils.Constant;

/**
 */

public class NotifiYindaoActivity extends BaseActivity {
    private static final int REQUSETSET = 110;
    Button notifi_info_text;
    Handler myHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi_info);
        myHandler = new Handler();
        Animation animation1 = AnimationUtils.loadAnimation(NotifiYindaoActivity.this, R.anim.translate_notifi);
        notifi_info_text.startAnimation(animation1);
        notifi_info_text.setVisibility(View.VISIBLE);
        notifi_info_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isNotificationListenEnabled(NotifiYindaoActivity.this)) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUSETSET);
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent transintent = new Intent(NotifiYindaoActivity.this, PermissionActivity.class);
                            startActivity(transintent);
                        }
                    }, 1500);

                } else {
                    PreData.putDB(NotifiYindaoActivity.this, Constant.KEY_NOTIFI, true);
                    startActivity(new Intent(NotifiYindaoActivity.this, NotificationActivity.class));
                    onBackPressed();
                }

            }
        });
    }

    @Override
    protected void findId() {
        super.findId();
        notifi_info_text = (Button) findViewById(R.id.notifi_info_text);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSETSET) {
            if (Util.isNotificationListenEnabled(NotifiYindaoActivity.this)) {
                PreData.putDB(NotifiYindaoActivity.this, Constant.KEY_NOTIFI, true);
                startActivity(new Intent(NotifiYindaoActivity.this, NotificationActivity.class));
                onBackPressed();
            }
        }
    }
}
