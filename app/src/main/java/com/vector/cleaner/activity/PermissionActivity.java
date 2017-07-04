package com.vector.cleaner.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.vector.cleaner.R;

/**
 */

public class PermissionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_permission);
        this.findViewById(R.id.onclick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}