package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.View.adapter.SettingAdapter;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.entity.Contents;
import com.supers.clean.junk.modle.entity.JunkInfo;

/**
 * Created by Ivy on 2017/3/2.
 */

public class SettingActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ListView list_si;

    SettingAdapter adapter;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        list_si = (ListView) findViewById(R.id.list_si);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_ignore);
        title_name.setText(R.string.setting_name);
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new SettingAdapter(this);
        list_si.setAdapter(adapter);
        initData();
    }

    private void initData() {
        adapter.addData(new JunkInfo(R.string.setting_tongzhi, R.mipmap.setting_tongzhi, true));
        adapter.addData(new JunkInfo(R.string.setting_tongzhilan, R.mipmap.setting_tongzhilan, true));
        adapter.addData(new JunkInfo(R.string.setting_float, R.mipmap.setting_float, PreData.getDB(this, Contents.FlOAT_SWITCH, true)));
        adapter.addData(new JunkInfo(R.string.setting_white, R.mipmap.setting_white));
        adapter.addData(new JunkInfo(R.string.setting_battery, R.mipmap.setting_battery, true));
        adapter.addData(new JunkInfo(R.string.setting_rotate, R.mipmap.setting_rotate));
    }
}
