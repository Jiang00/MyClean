package com.mutter.clean.junk.myActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.mutter.clean.junk.R;

/**
 */

public class ShowPermissionActivity extends Activity {
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


    }
}
