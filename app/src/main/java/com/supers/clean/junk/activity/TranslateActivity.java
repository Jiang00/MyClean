package com.supers.clean.junk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/3/27.
 */

public class TranslateActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_translate);
        finish();
    }
}
