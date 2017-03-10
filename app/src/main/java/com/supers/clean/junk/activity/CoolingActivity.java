package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.os.Handler;

import com.supers.clean.junk.R;
import com.supers.clean.junk.fakeView.FlakeView;


/**
 * Created by Ivy on 2017/3/2.
 */

public class CoolingActivity extends BaseActivity {
    private static final int FLAKE_NUM = 5;

    private FlakeView flakeView;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (flakeView != null) {
                flakeView.addFlakes(FLAKE_NUM);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cooling);
        mHandler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView = new FlakeView(this);
    }
}
