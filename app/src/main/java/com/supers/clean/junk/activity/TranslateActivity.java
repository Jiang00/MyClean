package com.supers.clean.junk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.client.AndroidSdk;
import com.eos.ui.demo.dialog.DialogManager;
import com.eos.ui.demo.view.CrossView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;

/**
 * Created by Ivy on 2017/3/27.
 */

public class TranslateActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_translate);
        if (CommonUtil.isPkgInstalled("com.eosmobi.flashlight.free", getPackageManager())) {
            CommonUtil.doStartApplicationWithPackageName(this, "com.eosmobi.flashlight.free");
            finish();
        } else {
            Intent intent = new Intent(this, TranslateActivity.class);
            intent.putExtra("from", "notifi");
            startActivity(intent);
        }

    }
}
