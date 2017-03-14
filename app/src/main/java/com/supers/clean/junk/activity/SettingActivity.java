package com.supers.clean.junk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.UtilGp;
import com.supers.clean.junk.modle.entity.Contents;

/**
 * Created by on 2017/3/2.
 */

public class SettingActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    RelativeLayout setting_tongzhi, setting_tongzhilan, setting_float, setting_battery, setting_white, setting_rotate;
    ImageView setting_tongzhi_check, setting_tongzhilan_check, setting_float_check, setting_battery_check;
    LinearLayout ll_ad;
    private View nativeView;

    private String TAG_SETTING = "eos_setting";
    private Handler myHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        setting_tongzhi = (RelativeLayout) findViewById(R.id.setting_tongzhi);
        setting_tongzhilan = (RelativeLayout) findViewById(R.id.setting_tongzhilan);
        setting_float = (RelativeLayout) findViewById(R.id.setting_float);
        setting_battery = (RelativeLayout) findViewById(R.id.setting_battery);
        setting_white = (RelativeLayout) findViewById(R.id.setting_white);
        setting_rotate = (RelativeLayout) findViewById(R.id.setting_rotate);
        setting_tongzhi_check = (ImageView) findViewById(R.id.setting_tongzhi_check);
        setting_tongzhilan_check = (ImageView) findViewById(R.id.setting_tongzhilan_check);
        setting_float_check = (ImageView) findViewById(R.id.setting_float_check);
        setting_battery_check = (ImageView) findViewById(R.id.setting_battery_check);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        myHandler = new Handler();
        title_name.setText(R.string.setting_name);
        title_left.setOnClickListener(onClickListener);
        initData();
        initListener();
        if (PreData.getDB(this, Contents.FULL_SETTING, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
            }, 1000);
        }
        addAd();
    }

    private void addAd() {
        nativeView = CommonUtil.getNativeAdView(TAG_SETTING, R.layout.native_ad_full);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            layout_ad.height = nativeView.getMeasuredHeight();
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
        }
    }

    private void initData() {
        if (PreData.getDB(SettingActivity.this, Contents.TONGZHI_SWITCH, true)) {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Contents.TONGZHILAN_SWITCH, true)) {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Contents.FlOAT_SWITCH, true)) {
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
        }
        if ((boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, true)) {
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
        }
    }

    private void initListener() {
        setting_tongzhi.setOnClickListener(onClickListener);
        setting_tongzhilan.setOnClickListener(onClickListener);
        setting_float.setOnClickListener(onClickListener);
        setting_battery.setOnClickListener(onClickListener);
        setting_white.setOnClickListener(onClickListener);
        setting_rotate.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.setting_tongzhi:
                    AndroidSdk.track("设置页面", "点击通知开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Contents.TONGZHI_SWITCH, true)) {
                        PreData.putDB(SettingActivity.this, Contents.TONGZHI_SWITCH, false);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        PreData.putDB(SettingActivity.this, Contents.TONGZHI_SWITCH, true);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_tongzhilan:
                    AndroidSdk.track("设置页面", "点击通知栏开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Contents.TONGZHILAN_SWITCH, true)) {
                        PreData.putDB(SettingActivity.this, Contents.TONGZHILAN_SWITCH, false);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
                        Intent intent = new Intent(SettingActivity.this, NotifactionService.class);
                        stopService(intent);
                    } else {
                        PreData.putDB(SettingActivity.this, Contents.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        Intent intent = new Intent(SettingActivity.this, NotifactionService.class);
                        intent.setAction("notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_float:
                    AndroidSdk.track("设置页面", "点击悬浮球开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Contents.FlOAT_SWITCH, true)) {
                        PreData.putDB(SettingActivity.this, Contents.FlOAT_SWITCH, false);
                        Intent intent1 = new Intent(SettingActivity.this, FloatService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        PreData.putDB(SettingActivity.this, Contents.FlOAT_SWITCH, true);
                        Intent intent1 = new Intent(SettingActivity.this, FloatService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_passed);
                        startService(intent1);
                    }
                    break;
                case R.id.setting_battery:
                    AndroidSdk.track("设置页面", "点击充电屏保开关", "", 1);
                    //chongdian
                    if ((boolean) Utils.readData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, true)) {
                        Utils.writeData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, false);
                        setting_battery_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        Utils.writeData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                        setting_battery_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_white:
                    AndroidSdk.track("设置页面", "进入白名单", "", 1);
                    Intent intent = new Intent(SettingActivity.this, WhiteListAvtivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting_rotate:
                    AndroidSdk.track("设置页面", "好评", "", 1);
                    UtilGp.openPlayStore(SettingActivity.this, SettingActivity.this.getPackageName());
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        setResult(50);
        finish();
    }
}
