package com.easy.junk.easyactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easytools.MUtilGp;
import com.easy.junk.easytools.MyConstant;
import com.easy.junk.easytools.SetAdUtil;
import com.easy.junk.easytools.ShortCutUtils;
import com.easy.junk.easyservices.EasyNotificationService;
import com.easy.junk.easyservices.EasySuspensionBallService;
import com.easy.module.charge.saver.easyutils.BatteryConstants;
import com.easy.module.charge.saver.easyutils.Utils;

/**
 * Created by on 2017/3/2.
 */

public class EasySettingActivity extends BaseActivity {
    private String TAG_SETTING = "icleaner_setting";
    ImageView setting_tongzhi_check, setting_float_check, setting_unload_check, setting_battery_check, setting_tongzhilan_check;
    FrameLayout title_left;
    ScrollView setting_scroll;
    RelativeLayout setting_tongzhi, setting_float, setting_battery, setting_power, setting_file, setting_tongzhilan,
            setting_picture, setting_gboost, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate, setting_unload;
    LinearLayout ll_ad;
    private Handler myHandler;
    private View nativeView;
    TextView title_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        myHandler = new Handler();
        title_name.setText(R.string.setting_name);
        title_left.setOnClickListener(onClickListener);
        initData();
        initListener();

        //深度清理
        if (PreData.getDB(this, MyConstant.POWERACTIVITY, 1) == 0) {
            setting_power.setVisibility(View.GONE);
        }
        //文件
        if (PreData.getDB(this, MyConstant.FILEACTIVITY, 1) == 0) {
            setting_file.setVisibility(View.GONE);
        }
        //通知栏
        if (PreData.getDB(this, MyConstant.NOTIFIACTIVITY, 1) == 0) {
            setting_notifi.setVisibility(View.GONE);
        }
        //通知栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setting_notifi.setVisibility(View.GONE);
        }


        if (PreData.getDB(this, MyConstant.FULL_SETTING, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
            }, 1000);
        } else {
            addAd();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setting_notifi.setVisibility(View.GONE);
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        setting_tongzhi = (RelativeLayout) findViewById(R.id.setting_tongzhi);
        setting_tongzhilan = (RelativeLayout) findViewById(R.id.setting_tongzhilan);
        setting_float = (RelativeLayout) findViewById(R.id.setting_float);
        setting_unload = (RelativeLayout) findViewById(R.id.setting_unload);
        setting_battery = (RelativeLayout) findViewById(R.id.setting_battery);
        setting_white = (RelativeLayout) findViewById(R.id.setting_white);
        setting_short = (RelativeLayout) findViewById(R.id.setting_short);
        setting_power = (RelativeLayout) findViewById(R.id.setting_power);
        setting_notifi = (RelativeLayout) findViewById(R.id.setting_notifi);
        setting_file = (RelativeLayout) findViewById(R.id.setting_file);
        setting_picture = (RelativeLayout) findViewById(R.id.setting_picture);
        setting_gboost = (RelativeLayout) findViewById(R.id.setting_gboost);
        setting_hui = (RelativeLayout) findViewById(R.id.setting_hui);
        setting_rotate = (RelativeLayout) findViewById(R.id.setting_rotate);
        setting_tongzhi_check = (ImageView) findViewById(R.id.setting_tongzhi_check);
        setting_tongzhilan_check = (ImageView) findViewById(R.id.setting_tongzhilan_check);
        setting_float_check = (ImageView) findViewById(R.id.setting_float_check);
        setting_unload_check = (ImageView) findViewById(R.id.setting_unload_check);
        setting_battery_check = (ImageView) findViewById(R.id.setting_battery_check);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        setting_scroll = (ScrollView) findViewById(R.id.setting_scroll);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initListener() {
        setting_tongzhi.setOnClickListener(onClickListener);
        setting_tongzhilan.setOnClickListener(onClickListener);
        setting_float.setOnClickListener(onClickListener);
        setting_unload.setOnClickListener(onClickListener);
        setting_battery.setOnClickListener(onClickListener);
        setting_white.setOnClickListener(onClickListener);
        setting_short.setOnClickListener(onClickListener);
        setting_power.setOnClickListener(onClickListener);
        setting_notifi.setOnClickListener(onClickListener);
        setting_file.setOnClickListener(onClickListener);
        setting_picture.setOnClickListener(onClickListener);
        setting_gboost.setOnClickListener(onClickListener);
        setting_hui.setOnClickListener(onClickListener);
        setting_rotate.setOnClickListener(onClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();//刷新
    }

    private void initData() {
        if (PreData.getDB(EasySettingActivity.this, MyConstant.TONGZHI_SWITCH, true)) {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(EasySettingActivity.this, MyConstant.TONGZHILAN_SWITCH, true)) {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(EasySettingActivity.this, MyConstant.FlOAT_SWITCH, true)) {
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(EasySettingActivity.this, MyConstant.UNLOAD_SWITCH, true)) {
            setting_unload_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_unload_check.setImageResource(R.mipmap.side_check_normal);
        }
        if ((boolean) Utils.readData(this, BatteryConstants.CHARGE_SAVER_SWITCH, true)) {
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.setting_tongzhi:
                    SetAdUtil.track("设置页面", "点击通知开关", "", 1);
                    if (PreData.getDB(EasySettingActivity.this, MyConstant.TONGZHI_SWITCH, true)) {
                        PreData.putDB(EasySettingActivity.this, MyConstant.TONGZHI_SWITCH, false);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        PreData.putDB(EasySettingActivity.this, MyConstant.TONGZHI_SWITCH, true);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_tongzhilan:
                    SetAdUtil.track("设置页面", "点击通知栏开关", "", 1);
                    if (PreData.getDB(EasySettingActivity.this, MyConstant.TONGZHILAN_SWITCH, true)) {
                        PreData.putDB(EasySettingActivity.this, MyConstant.TONGZHILAN_SWITCH, false);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
                        Intent intent = new Intent(EasySettingActivity.this, EasyNotificationService.class);
                        stopService(intent);
                    } else {
                        PreData.putDB(EasySettingActivity.this, MyConstant.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        Intent intent = new Intent(EasySettingActivity.this, EasyNotificationService.class);
                        intent.setAction("notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_float:
                    SetAdUtil.track("设置页面", "点击悬浮球开关", "", 1);
                    if (PreData.getDB(EasySettingActivity.this, MyConstant.FlOAT_SWITCH, true)) {
                        PreData.putDB(EasySettingActivity.this, MyConstant.FlOAT_SWITCH, false);
                        Intent intent1 = new Intent(EasySettingActivity.this, EasySuspensionBallService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        PreData.putDB(EasySettingActivity.this, MyConstant.FlOAT_SWITCH, true);
                        Intent intent1 = new Intent(EasySettingActivity.this, EasySuspensionBallService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_passed);
                        startService(intent1);
                    }
                    break;
                case R.id.setting_unload:
                    SetAdUtil.track("设置页面", "点击卸载残余开关", "", 1);
                    if (PreData.getDB(EasySettingActivity.this, MyConstant.UNLOAD_SWITCH, true)) {
                        PreData.putDB(EasySettingActivity.this, MyConstant.UNLOAD_SWITCH, false);
//                        Intent intent1 = new Intent(EasySettingActivity.this, EasyUnloadResidualBroadcast.class);
                        setting_unload_check.setImageResource(R.mipmap.side_check_normal);
//                        stopService(intent1);
                    } else {
                        PreData.putDB(EasySettingActivity.this, MyConstant.UNLOAD_SWITCH, true);
//                        Intent intent1 = new Intent(EasySettingActivity.this, EasyUnloadResidualBroadcast.class);
                        setting_unload_check.setImageResource(R.mipmap.side_check_passed);
//                        sendBroadcast(intent1);
                    }
                    break;
                case R.id.setting_battery:
                    if ((boolean) Utils.readData(EasySettingActivity.this, BatteryConstants.CHARGE_SAVER_SWITCH, true)) {
                        Utils.writeData(EasySettingActivity.this, BatteryConstants.CHARGE_SAVER_SWITCH, false);
                        SetAdUtil.track("设置页面", "点击充电屏保开关", "关", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        Utils.writeData(EasySettingActivity.this, BatteryConstants.CHARGE_SAVER_SWITCH, true);
                        SetAdUtil.track("设置页面", "点击充电屏保开关", "开", 1);
                        PreData.putDB(EasySettingActivity.this, MyConstant.FIRST_BATTERY, false);
                        setting_battery_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_white:
                    SetAdUtil.track("设置页面", "进入白名单", "", 1);
                    Intent intent = new Intent(EasySettingActivity.this, EasyWhiteAvtivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting_short:
                    SetAdUtil.track("设置页面", "添加桌面快捷方式", "", 1);
                    PreData.putDB(EasySettingActivity.this, MyConstant.KEY_SHORTCUT, true);
                    ShortCutUtils.addShortcut(EasySettingActivity.this);
                    break;
                case R.id.setting_power:
                    SetAdUtil.track("设置页面", "进入深度清理", "", 1);
                    Intent intentP = new Intent(EasySettingActivity.this, EasyDeepingActivity.class);
                    startActivity(intentP);
                    break;
                case R.id.setting_file:
                    SetAdUtil.track("设置页面", "进入文件管理", "", 1);
                    PreData.putDB(EasySettingActivity.this, MyConstant.FILE_CLEAN, true);
                    Intent intentF = new Intent(EasySettingActivity.this, EasyFileManagerActivity.class);
                    startActivity(intentF);
                    break;
                case R.id.setting_picture:
                    SetAdUtil.track("设置页面", "进入相似图片", "", 1);
                    PreData.putDB(EasySettingActivity.this, MyConstant.PHOTO_CLEAN, true);
                    Intent intentPic = new Intent(EasySettingActivity.this, EasyPictActivity.class);
                    startActivity(intentPic);
                    break;
                case R.id.setting_gboost:
                    SetAdUtil.track("设置页面", "进入游戏加速", "", 1);
                    PreData.putDB(EasySettingActivity.this, MyConstant.GBOOST_CLEAN, true);
                    Intent intentGB = new Intent(EasySettingActivity.this, EasyGoodGameActivity.class);
                    startActivity(intentGB);
                    break;
                case R.id.setting_hui:
                    SetAdUtil.track("设置页面", "进入回收站", "", 1);
                    Intent intenth = new Intent(EasySettingActivity.this, EasyPictHuiActivity.class);
                    startActivity(intenth);
                    break;
                case R.id.setting_notifi:
                    SetAdUtil.track("设置页面", "进入通知栏清理", "", 1);
                    PreData.putDB(EasySettingActivity.this, MyConstant.NOTIFI_CLEAN, true);
                    if (PreData.getDB(EasySettingActivity.this, MyConstant.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(EasySettingActivity.this)) {
                        //通知栏动画
                        Intent intent6 = new Intent(EasySettingActivity.this, EasyNotifingAnimationActivity.class);
                        startActivity(intent6);
                    } else {
                        //通知栏
                        Intent intent6 = new Intent(EasySettingActivity.this, EasyNotifingActivity.class);
                        startActivity(intent6);
                    }
                    break;
                case R.id.setting_rotate:
                    SetAdUtil.track("设置页面", "好评", "", 1);
                    MUtilGp.rate(EasySettingActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (MyUtils.isNotificationListenEnabled(this)) {
                PreData.putDB(this, MyConstant.KEY_NOTIFI, true);
                Intent intent = new Intent(this, EasyNotifingActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, EasyNotifingAnimationActivity.class);
                startActivity(intent);
            }
        }
    }

    private void addAd() {
        nativeView = SetAdUtil.getNativeAdView(TAG_SETTING, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            if (nativeView.getHeight() == MyUtils.dp2px(250)) {
                layout_ad.height = MyUtils.dp2px(250);
            }
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
            setting_scroll.setScrollY(0);
        } else {
        }
    }

    @Override
    public void onBackPressed() {
        setResult(MyConstant.SETTING_RESUIL);
        finish();
    }
}
