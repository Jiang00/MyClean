package com.security.cleaner.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.security.cleaner.aservice.NeicunService;
import com.security.cleaner.utils.ShortCutUtils;
import com.security.cleaner.utils.UtilGp;
import com.security.mcleaner.mutil.PreData;
import com.security.mcleaner.mutil.Util;
import com.android.client.AndroidSdk;
import com.security.module.Util.Constants;
import com.security.module.Util.Utils;
import com.security.cleaner.R;
import com.security.cleaner.aservice.NotificationService;
import com.security.cleaner.utils.AdUtil;
import com.security.cleaner.utils.Constant;

/**
 * Created by on 2017/3/2.
 */

public class SettingActivity extends BaseActivity {
    RelativeLayout setting_tongzhi, setting_tongzhilan, setting_float, setting_battery, setting_power, setting_file,
            setting_picture, main_msg_title, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate;
    ImageView setting_tongzhi_check, setting_tongzhilan_check, setting_float_check, setting_battery_check;
    FrameLayout title_left;
    TextView title_name;
    TextView tongzhi_tv, tongzhilan_tv, float_tv, battery_tv;
    LinearLayout ll_ad;
    ScrollView setting_scroll;
    private View nativeView;

    private String TAG_SETTING = "my_setting";
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
        setting_short = (RelativeLayout) findViewById(R.id.setting_short);
        setting_power = (RelativeLayout) findViewById(R.id.setting_power);
        setting_notifi = (RelativeLayout) findViewById(R.id.setting_notifi);
        setting_file = (RelativeLayout) findViewById(R.id.setting_file);
        setting_picture = (RelativeLayout) findViewById(R.id.setting_picture);
        main_msg_title = (RelativeLayout) findViewById(R.id.main_msg_title);
        setting_hui = (RelativeLayout) findViewById(R.id.setting_hui);
        setting_rotate = (RelativeLayout) findViewById(R.id.setting_rotate);
        setting_tongzhi_check = (ImageView) findViewById(R.id.setting_tongzhi_check);
        setting_tongzhilan_check = (ImageView) findViewById(R.id.setting_tongzhilan_check);
        setting_float_check = (ImageView) findViewById(R.id.setting_float_check);
        setting_battery_check = (ImageView) findViewById(R.id.setting_battery_check);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        setting_scroll = (ScrollView) findViewById(R.id.setting_scroll);
        tongzhi_tv = (TextView) findViewById(R.id.tongzhi_tv);
        tongzhilan_tv = (TextView) findViewById(R.id.tongzhilan_tv);
        float_tv = (TextView) findViewById(R.id.float_tv);
        battery_tv = (TextView) findViewById(R.id.battery_tv);
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
        addAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void addAd() {
        nativeView = AdUtil.getNativeAdView(TAG_SETTING, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
//            setting_scroll.fullScroll(ScrollView.FOCUS_UP);
            setting_scroll.setScrollY(0);
        } else {

        }
    }


    private void initData() {
        if (PreData.getDB(SettingActivity.this, Constant.TONGZHI_SWITCH, true)) {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
            setAlphaAndColor(tongzhi_tv, true);
        } else {
            setAlphaAndColor(tongzhi_tv, false);
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
            setAlphaAndColor(tongzhilan_tv, true);
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setAlphaAndColor(tongzhilan_tv, false);
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Constant.FlOAT_SWITCH, true)) {
            setAlphaAndColor(float_tv, true);
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setAlphaAndColor(float_tv, false);
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
        }
        if ((boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, true)) {
            setAlphaAndColor(battery_tv, true);
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setAlphaAndColor(battery_tv, false);
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
        }
    }

    public void setAlphaAndColor(TextView tv, boolean isKai) {
        if (isKai) {
            tv.setTextColor(ContextCompat.getColor(this, R.color.Z1));
        } else {
            tv.setTextColor(ContextCompat.getColor(this, R.color.Z2));

        }

    }

    private void initListener() {
        setting_tongzhi.setOnClickListener(onClickListener);
        setting_tongzhilan.setOnClickListener(onClickListener);
        setting_float.setOnClickListener(onClickListener);
        setting_battery.setOnClickListener(onClickListener);
        setting_white.setOnClickListener(onClickListener);
        setting_short.setOnClickListener(onClickListener);
        setting_power.setOnClickListener(onClickListener);
        setting_notifi.setOnClickListener(onClickListener);
        setting_file.setOnClickListener(onClickListener);
        setting_picture.setOnClickListener(onClickListener);
        main_msg_title.setOnClickListener(onClickListener);
        setting_hui.setOnClickListener(onClickListener);
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
                    AdUtil.track("设置页面", "点击通知开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Constant.TONGZHI_SWITCH, true)) {
                        PreData.putDB(SettingActivity.this, Constant.TONGZHI_SWITCH, false);
                        setAlphaAndColor(tongzhi_tv, false);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        setAlphaAndColor(tongzhi_tv, true);
                        PreData.putDB(SettingActivity.this, Constant.TONGZHI_SWITCH, true);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_tongzhilan:
                    AdUtil.track("设置页面", "点击通知栏开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
                        PreData.putDB(SettingActivity.this, Constant.TONGZHILAN_SWITCH, false);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
                        Intent intent = new Intent(SettingActivity.this, NotificationService.class);
                        stopService(intent);
                        setAlphaAndColor(tongzhilan_tv, false);
                    } else {
                        setAlphaAndColor(tongzhilan_tv, true);
                        PreData.putDB(SettingActivity.this, Constant.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        Intent intent = new Intent(SettingActivity.this, NotificationService.class);
                        intent.putExtra("from", "notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_float:
                    AdUtil.track("设置页面", "点击悬浮球开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Constant.FlOAT_SWITCH, true)) {
                        PreData.putDB(SettingActivity.this, Constant.FlOAT_SWITCH, false);
                        Intent intent1 = new Intent(SettingActivity.this, NeicunService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                        setAlphaAndColor(float_tv, false);
                    } else {
                        PreData.putDB(SettingActivity.this, Constant.FlOAT_SWITCH, true);
                        Intent intent1 = new Intent(SettingActivity.this, NeicunService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_passed);
                        startService(intent1);
                        setAlphaAndColor(float_tv, true);
                    }
                    break;
                case R.id.setting_battery:

                    //chongdian
                    if ((boolean) Utils.readData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, true)) {
                        Utils.writeData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, false);
                        AdUtil.track("设置页面", "点击充电屏保开关", "关", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_normal);
                        setAlphaAndColor(battery_tv, false);
                    } else {
                        Utils.writeData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                        AdUtil.track("设置页面", "点击充电屏保开关", "开", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_passed);
                        setAlphaAndColor(battery_tv, true);
                    }
                    break;
                case R.id.setting_white:
                    AdUtil.track("设置页面", "进入白名单", "", 1);
                    Intent intent = new Intent(SettingActivity.this, IgnoreListAvtivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting_short:
                    AdUtil.track("设置页面", "添加桌面快捷方式", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.KEY_SHORTCUT, true);
                    ShortCutUtils.addShortcut(SettingActivity.this);
                    break;
                case R.id.setting_power:
                    AdUtil.track("设置页面", "进入深度清理", "", 1);
                    Intent intentP = new Intent(SettingActivity.this, DeepActivity.class);
                    startActivity(intentP);
                    break;
                case R.id.setting_file:
                    AdUtil.track("设置页面", "进入文件管理", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.FILE_CLEAN, true);
                    Intent intentF = new Intent(SettingActivity.this, FilesManagerActivity.class);
                    startActivity(intentF);
                    break;
                case R.id.setting_picture:
                    AdUtil.track("设置页面", "进入相似图片", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.PHOTO_CLEAN, true);
                    Intent intentPic = new Intent(SettingActivity.this, SimilarPhotoActivity.class);
                    startActivity(intentPic);
                    break;
                case R.id.main_msg_title:
                    AdUtil.track("设置页面", "进入硬件信息", "", 1);
                    Intent intentGB = new Intent(SettingActivity.this, ShoujiMsgActivity.class);
                    startActivity(intentGB);
                    break;
                case R.id.setting_hui:
                    AdUtil.track("设置页面", "进入回收站", "", 1);
                    Intent intenth = new Intent(SettingActivity.this, AsbHuishouActivity.class);
                    startActivity(intenth);
                    break;
                case R.id.setting_notifi:
                    AdUtil.track("设置页面", "进入通知栏清理", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(SettingActivity.this) || !PreData.getDB(SettingActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SettingActivity.this, NotifiPermissActivity.class);
                        startActivity(intent6);
                    } else {
                        Intent intent6 = new Intent(SettingActivity.this, NotificationActivity.class);
                        startActivity(intent6);
                    }
                    break;
                case R.id.setting_rotate:
                    AdUtil.track("设置页面", "好评", "", 1);
                    UtilGp.rate(SettingActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (Util.isNotificationListenEnabled(this)) {
                PreData.putDB(this, Constant.KEY_NOTIFI, true);
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, NotifiPermissActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Constant.SETTING_RESUIL);
        finish();
    }
}
