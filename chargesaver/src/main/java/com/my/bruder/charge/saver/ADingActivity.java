package com.my.bruder.charge.saver;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.android.client.AndroidSdk;
import com.my.bruder.charge.saver.Util.ADRequest;

public class ADingActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResume(this);
        ADRequest.showFullAD();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        AndroidSdk.onCreate(this, new AndroidSdk.Builder());
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ADingActivity.this.finish();
                return true;
            }
        });
    }

}
