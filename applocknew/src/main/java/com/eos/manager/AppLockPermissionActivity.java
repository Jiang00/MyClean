package com.eos.manager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.privacy.lock.R;

/**
 * Created by SongHualin on 6/12/2015.
 */
public class AppLockPermissionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.permission_translate);
        TextView find_appname = (TextView) findViewById(R.id.find_appname);
        find_appname.setText(getString(R.string.security_apps_open_permission, getString(R.string.app_name)));
        this.findViewById(R.id.onclick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
