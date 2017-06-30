package com.supers.clean.junk.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.util.PreData;
import com.eos.manager.AppLockPatternEosActivity;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.UtilGp;


/**
 * Created by  on 2017/3/29.
 */

public class ApplockActivity extends BaseActivity {
    TextView tv_2, tv_3, tv_4, tv_5;
    RelativeLayout bt_1rl;
    TextView bt_2;
    FrameLayout title_left;
    TextView title_name;

    @Override
    protected void findId() {
        super.findId();
        title_left = $(R.id.title_left);
        title_name = $(R.id.title_name);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        bt_1rl = (RelativeLayout) findViewById(R.id.bt_1rl);
        bt_2 = (TextView) findViewById(R.id.bt_2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_applock);

        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title_name.setText(R.string.applock_9);

        tv_2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_4.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_5.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        bt_1rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdUtil.track("applock", "跳转GP下载pro版", "", 1);
                UtilGp.openPlayStore(ApplockActivity.this, "com.eosmobi.applock");
                finish();
            }
        });
        bt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreData.putDB(ApplockActivity.this, Constant.FIRST_APPLOCK, 2);
                AdUtil.track("applock", "选择简版", "", 1);
                Intent intent = new Intent(ApplockActivity.this, AppLockPatternEosActivity.class);
                intent.putExtra("is_main", true);
                startActivity(intent);
                finish();
            }
        });
    }
}
