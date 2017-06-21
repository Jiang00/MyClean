package com.fast.module.charge.saver;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.fast.module.charge.saver.Util.ADRequest;

public class ADActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

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
        ADRequest.showFullAD();
    }
}
