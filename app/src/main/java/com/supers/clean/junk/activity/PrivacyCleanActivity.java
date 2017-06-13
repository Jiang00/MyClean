package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.privacy.SmsEntity;
import com.supers.clean.junk.privacy.PrivacyClean;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/5/9.
 */

public class PrivacyCleanActivity extends BaseActivity implements View.OnClickListener {

    private TextView privacy_all_size_text, privacy_cut_num_text, privacy_sms_text, privacy_read_sms_text, privacy_strange_sms, privacy_call_num_text, privacy_dismiss_call_text, privacy_strange_call_text;

    private CheckBox cut_checkbox, sms_checkbox, read_sms_checkbox, strange_sms_checkbox, call_checkbox, dismiss_call_checkbox, strange_call_checkbox;
    private ListView read_sms_listview, strange_sms_listview, dissmiss_call_listview, strange_call_listview;

    private Button privacy_clean_button;

    private PrivacyClean privacyClean;

    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_privacy_clean);
        privacyClean = PrivacyClean.getInstance(this);

        int cutNum = privacyClean.queryCut();
        privacy_cut_num_text.setText(cutNum + "");

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };

        new Thread() {
            @Override
            public void run() {
                ArrayList<SmsEntity> smsEntities = privacyClean.querySms();
                Log.e("rqy", smsEntities.size() + "");
                privacyClean.queryCall();
            }
        }.start();
    }

    @Override
    protected void findId() {
        super.findId();
        privacy_all_size_text = (TextView) findViewById(R.id.privacy_all_size_text);
        privacy_cut_num_text = (TextView) findViewById(R.id.privacy_cut_num_text);
        privacy_sms_text = (TextView) findViewById(R.id.privacy_sms_text);
        privacy_read_sms_text = (TextView) findViewById(R.id.privacy_read_sms_text);
        privacy_strange_sms = (TextView) findViewById(R.id.privacy_strange_sms);
        privacy_call_num_text = (TextView) findViewById(R.id.privacy_call_num_text);
        privacy_dismiss_call_text = (TextView) findViewById(R.id.privacy_dismiss_call_text);
        privacy_strange_call_text = (TextView) findViewById(R.id.privacy_strange_call_text);

        cut_checkbox = (CheckBox) findViewById(R.id.cut_checkbox);
        sms_checkbox = (CheckBox) findViewById(R.id.sms_checkbox);
        read_sms_checkbox = (CheckBox) findViewById(R.id.read_sms_checkbox);
        strange_sms_checkbox = (CheckBox) findViewById(R.id.strange_sms_checkbox);
        call_checkbox = (CheckBox) findViewById(R.id.call_checkbox);
        dismiss_call_checkbox = (CheckBox) findViewById(R.id.dismiss_call_checkbox);
        strange_call_checkbox = (CheckBox) findViewById(R.id.strange_call_checkbox);

        read_sms_listview = (ListView) findViewById(R.id.read_sms_listview);
        strange_sms_listview = (ListView) findViewById(R.id.strange_sms_listview);
        dissmiss_call_listview = (ListView) findViewById(R.id.dissmiss_call_listview);
        strange_call_listview = (ListView) findViewById(R.id.strange_call_listview);

        findViewById(R.id.title_left).setOnClickListener(this);
        findViewById(R.id.privacy_clean_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                finish();
                break;
            case R.id.privacy_clean_button:
                break;
            default:
                break;
        }
    }
}
