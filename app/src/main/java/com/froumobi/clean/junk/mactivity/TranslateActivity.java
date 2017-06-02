package com.supers.clean.junk.mactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.clean.util.LoadManager;
import com.supers.clean.junk.R;
import com.android.clean.util.Util;

/**
 * Created by Ivy on 2017/3/27.
 */

public class TranslateActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_translate);

        if (LoadManager.getInstance(this).isPkgInstalled("com.eosmobi.flashlight.free")) {
            Util.doStartApplicationWithPackageName(this, "com.eosmobi.flashlight.free");
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("from", "translate");
            startActivity(intent);
        }
        finish();
    }
}
