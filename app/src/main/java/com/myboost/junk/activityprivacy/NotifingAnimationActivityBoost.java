package com.myboost.junk.activityprivacy;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.toolsprivacy.MyConstantPrivacy;

public class NotifingAnimationActivityBoost extends BaseActivity {
    Button notifi_info_text;
    private static final int REQUSETSET = 110;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi_info);
        notifi_info_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyUtils.isNotificationListenEnabled(NotifingAnimationActivityBoost.this)) {
                    startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), REQUSETSET);
                } else {
                    PreData.putDB(NotifingAnimationActivityBoost.this, MyConstantPrivacy.KEY_NOTIFI, true);
                    startActivity(new Intent(NotifingAnimationActivityBoost.this, BoostNotifingActivity.class));
                    onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUSETSET) {
            if (MyUtils.isNotificationListenEnabled(NotifingAnimationActivityBoost.this)) {
                PreData.putDB(NotifingAnimationActivityBoost.this, MyConstantPrivacy.KEY_NOTIFI, true);
                startActivity(new Intent(NotifingAnimationActivityBoost.this, BoostNotifingActivity.class));
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
