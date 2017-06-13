package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.mingle.widget.CheckBox;
import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/5/9.
 */

public class PrivacyCleanActivity extends BaseActivity {

    private CheckBox cut_checkbox, sms_checkout, read_sms_checkout, strange_sms_checkbox, call_checkbox, dismiss_call_checkbox, strange_call_checkbox;
    private ListView

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_privacy_clean);
    }

    @Override
    protected void findId() {
        super.findId();
        findViewById(R.id.title_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
