package com.mutter.clean.junk.myActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutter.clean.junk.R;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.ShortCutUtils;
import com.mutter.clean.util.PreData;

/**
 * Created by ${} on 2018/1/19.
 */

public class ShortCutAddActivity extends BaseActivity {
    LinearLayout ll_ad;
    FrameLayout title_left;
    TextView title_name;
    FrameLayout ad_fl;
    RelativeLayout setting_cut_jiasu, setting_cut_jiangwen;
    private View nativeView;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_fl = (FrameLayout) findViewById(R.id.ad_fl);
        setting_cut_jiasu = (RelativeLayout) findViewById(R.id.setting_cut_jiasu);
        setting_cut_jiangwen = (RelativeLayout) findViewById(R.id.setting_cut_jiangwen);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_short_add);
        title_name.setText(R.string.setting_short);
        title_left.setOnClickListener(onClickListener);
        setting_cut_jiasu.setOnClickListener(onClickListener);
        setting_cut_jiangwen.setOnClickListener(onClickListener);
        addAd();
    }

    private void addAd() {
        nativeView = AdUtil.getNativeAdView("", R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            AdUtil.startBannerAnimation(this, ad_fl);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.setting_cut_jiasu:
                    AdUtil.track("设置页面", "添加桌面快捷方式", "", 1);
                    PreData.putDB(ShortCutAddActivity.this, Constant.KEY_SHORTCUT, true);
                    ShortCutUtils.addShortcut(ShortCutAddActivity.this);
                    break;
                case R.id.setting_cut_jiangwen:
                    AdUtil.track("设置页面", "添加桌面快捷方式", "", 1);
                    PreData.putDB(ShortCutAddActivity.this, Constant.KEY_SHORTCUT_JIANGWEN, true);
                    ShortCutUtils.addShortcutJiang(ShortCutAddActivity.this);
                    break;
            }
        }
    };
}
