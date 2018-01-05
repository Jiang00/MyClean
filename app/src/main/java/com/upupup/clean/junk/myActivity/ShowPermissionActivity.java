package com.upupup.clean.junk.myActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.upupup.clean.junk.R;

/**
 */

public class ShowPermissionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_permission);
        TextView tishi = (TextView) findViewById(R.id.tishi);
        tishi.setText(getString(R.string.permiss_tishi, getString(R.string.app_name)));
        this.findViewById(R.id.onclick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
