package com.supers.clean.junk.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.supers.call.activity.CallActivity;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;

/**
 * Created by ${} on 2017/11/9.
 */

public class ToolActivity extends BaseActivity {
    LinearLayout deep_button, file_button,
            gboost_button, picture_button,
            privacy_button, wifi_button,
            call_button, notifi_button;
    LinearLayout ll_ad;
    FrameLayout title_left;
    TextView title_name;

    @Override
    protected void findId() {
        super.findId();
        deep_button = (LinearLayout) findViewById(R.id.deep_button);
        file_button = (LinearLayout) findViewById(R.id.file_button);
        gboost_button = (LinearLayout) findViewById(R.id.gboost_button);
        picture_button = (LinearLayout) findViewById(R.id.picture_button);
        privacy_button = (LinearLayout) findViewById(R.id.privacy_button);
        wifi_button = (LinearLayout) findViewById(R.id.wifi_button);
        call_button = (LinearLayout) findViewById(R.id.call_button);
        notifi_button = (LinearLayout) findViewById(R.id.notifi_button);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tool);
        title_name.setText(R.string.main_tool_name);
        View nativeView = AdUtil.getNativeAdView(this,"", R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            notifi_button.setVisibility(View.GONE);
        }
        deep_button.setOnClickListener(onClickListener);
        file_button.setOnClickListener(onClickListener);
        gboost_button.setOnClickListener(onClickListener);
        picture_button.setOnClickListener(onClickListener);
        privacy_button.setOnClickListener(onClickListener);
        wifi_button.setOnClickListener(onClickListener);
        call_button.setOnClickListener(onClickListener);
        notifi_button.setOnClickListener(onClickListener);
        title_left.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.deep_button) {
                PreData.putDB(ToolActivity.this, Constant.DEEP_CLEAN, true);
                AdUtil.track("工具箱", "点击进入深度清理页面", "", 1);
                Intent intent5 = new Intent(ToolActivity.this, PowerActivity.class);
                startActivityForResult(intent5, 1);

            } else if (i == R.id.file_button) {
                AdUtil.track("工具箱", "点击进入文件管理页面", "", 1);
                PreData.putDB(ToolActivity.this, Constant.FILE_CLEAN, true);
                Intent intent5 = new Intent(ToolActivity.this, FileActivity.class);
                startActivityForResult(intent5, 1);

            } else if (i == R.id.gboost_button) {
                AdUtil.track("工具箱", "点击进入游戏加速", "", 1);
                PreData.putDB(ToolActivity.this, Constant.GBOOST_CLEAN, true);
                Intent intent = new Intent(ToolActivity.this, GBoostActivity.class);
                startActivityForResult(intent, 1);

            } else if (i == R.id.picture_button) {
                AdUtil.track("工具箱", "点击进入相似图片", "", 1);
                PreData.putDB(ToolActivity.this, Constant.PHOTO_CLEAN, true);
                Intent intent = new Intent(ToolActivity.this, PictureActivity.class);
                startActivityForResult(intent, 1);

            } else if (i == R.id.privacy_button) {
                AdUtil.track("工具箱", "点击进入隐私清理页面", "", 1);
                Intent intent5 = new Intent(ToolActivity.this, PrivacyActivity.class);
                startActivityForResult(intent5, 1);

            } else if (i == R.id.wifi_button) {
                AdUtil.track("工具箱", "点击进入WIFI", "", 1);
                Intent intent = new Intent(ToolActivity.this, NetMonitor.class);
                startActivityForResult(intent, 1);

            } else if (i == R.id.call_button) {
                AdUtil.track("工具箱", "点击进入骚扰拦截页面", "", 1);
                Intent intent5 = new Intent(ToolActivity.this, CallActivity.class);
                startActivityForResult(intent5, 1);

            } else if (i == R.id.notifi_button) {
                AdUtil.track("工具箱", "点击进入通知栏清理页面", "", 1);
                PreData.putDB(ToolActivity.this, Constant.NOTIFI_CLEAN, true);
                if (!Util.isNotificationListenEnabled(ToolActivity.this) || !PreData.getDB(ToolActivity.this, Constant.KEY_NOTIFI, true)) {
                    Intent intent6 = new Intent(ToolActivity.this, NotifiInfoActivity.class);
                    startActivityForResult(intent6, 1);
                } else {
                    Intent intent6 = new Intent(ToolActivity.this, NotifiActivity.class);
                    startActivityForResult(intent6, 1);
                }

             } else if (i == R.id.title_left) {
                onBackPressed();
            }
        }
    };
}
