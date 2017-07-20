package com.easy.junk.easyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easytools.EasyConstant;

/**
 */

public class EasyNotifingAnimationActivity extends BaseActivity {
    private static final int REQUSETSET = 110;
    Button notifi_info_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi_info);
        notifi_info_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyUtils.isNotificationListenEnabled(EasyNotifingAnimationActivity.this)) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUSETSET);
                } else {
                    PreData.putDB(EasyNotifingAnimationActivity.this, EasyConstant.KEY_NOTIFI, true);
                    startActivity(new Intent(EasyNotifingAnimationActivity.this, EasyNotifingActivity.class));
                    onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSETSET) {
            if (MyUtils.isNotificationListenEnabled(EasyNotifingAnimationActivity.this)) {
                PreData.putDB(EasyNotifingAnimationActivity.this, EasyConstant.KEY_NOTIFI, true);
                startActivity(new Intent(EasyNotifingAnimationActivity.this, EasyNotifingActivity.class));
                onBackPressed();
            }
        }
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
}
