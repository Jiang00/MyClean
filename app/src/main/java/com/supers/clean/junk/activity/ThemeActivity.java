package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eos.module.charge.saver.Util.Contants;
import com.eos.module.charge.saver.Util.Utils;
import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/3/2.
 */

public class ThemeActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    LinearLayout battery_theme_0, battery_theme_1;
    ImageView theme_0_check, theme_1_check;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        battery_theme_0 = (LinearLayout) findViewById(R.id.battery_theme_0);
        battery_theme_1 = (LinearLayout) findViewById(R.id.battery_theme_1);
        theme_0_check = (ImageView) findViewById(R.id.theme_0_check);
        theme_1_check = (ImageView) findViewById(R.id.theme_1_check);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_theme);
        title_name.setText(R.string.main_theme_name);
        title_left.setOnClickListener(onClickListener);
        battery_theme_0.setOnClickListener(onClickListener);
        battery_theme_1.setOnClickListener(onClickListener);
        if (Contants.TYPE_DUCK.equals(Utils.readData(this, Contants.KEY_SAVER_TYPE, Contants.TYPE_HOR_BAR))) {
            theme_0_check.setImageResource(R.mipmap.battery_normal);
            theme_1_check.setImageResource(R.mipmap.battery_passed);
        } else {
            theme_0_check.setImageResource(R.mipmap.battery_passed);
            theme_1_check.setImageResource(R.mipmap.battery_normal);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.battery_theme_0:
                    theme_0_check.setImageResource(R.mipmap.battery_passed);
                    theme_1_check.setImageResource(R.mipmap.battery_normal);
                    Utils.writeData(ThemeActivity.this, Contants.KEY_SAVER_TYPE, Contants.TYPE_HOR_BAR);
                    break;
                case R.id.battery_theme_1:
                    theme_0_check.setImageResource(R.mipmap.battery_normal);
                    theme_1_check.setImageResource(R.mipmap.battery_passed);
                    Utils.writeData(ThemeActivity.this, Contants.KEY_SAVER_TYPE, Contants.TYPE_DUCK);
                    break;
            }
            Utils.writeData(ThemeActivity.this, Contants.CHARGE_SAVER_SWITCH, true);
        }
    };

    @Override
    public void onBackPressed() {
        setResult(50);
        finish();
    }
}
