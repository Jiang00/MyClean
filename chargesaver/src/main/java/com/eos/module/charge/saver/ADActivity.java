package com.eos.module.charge.saver;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.eos.module.charge.saver.Util.ADRequest;

public class ADActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        AndroidSdk.onCreate(this, new AndroidSdk.Builder());
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ADActivity.this.finish();
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResume(this);
        if (!PreData.getDB(this, Constant.BILL_YOUXIAO, true)) {
            AndroidSdk.showFullAd("result_full");
        }
    }
}
