package com.myboost.module.charge.saver;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.android.client.AndroidSdk;
import com.myboost.module.charge.saver.privacyutils.PrivacyADRequest;

public class PrivacySetADActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResume(this);
        PrivacyADRequest.showFullAD();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        AndroidSdk.onCreate(this, new AndroidSdk.Builder());
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                PrivacySetADActivity.this.finish();
                return true;
            }
        });
    }
}
