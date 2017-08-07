package com.myboost.junk.boostactivity;

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
import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.boostbroadcasts.BoostUnloadResidualBroadcast;
import com.myboost.junk.servicesboost.NotificationServiceBoost;
import com.myboost.junk.servicesboost.BoostSuspensionBallService;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.boosttools.ShortCutUtilsBoost;
import com.myboost.junk.boosttools.UtilGpBoost;
import com.myboost.junk.boosttools.SetAdUtilPrivacy;
import com.myboost.module.charge.saver.boostutils.BoostBatteryConstants;
import com.myboost.module.charge.saver.boostutils.BatteryUtils;

/**
 * Created by on 2017/3/2.
 */

public class BoostSetActivity extends BaseActivity {
    private String TAG_SETTING = "flashclean_setting";
    ImageView setting_tongzhi_check, setting_float_check, setting_unload_check, setting_battery_check, setting_tongzhilan_check;
    FrameLayout title_left;
    ScrollView setting_scroll;
    private Handler myHandler;
    RelativeLayout setting_tongzhi, setting_float, setting_battery, setting_power, setting_file, setting_tongzhilan,
            setting_picture, setting_gboost, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate, setting_unload;
    LinearLayout ll_ad;
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
        if (PreData.getDB(this, BoostMyConstant.POWERACTIVITY, 1) == 0) {
            setting_power.setVisibility(View.GONE);
        }
        //文件
        if (PreData.getDB(this, BoostMyConstant.FILEACTIVITY, 1) == 0) {
            setting_file.setVisibility(View.GONE);
        }
        //通知栏
        if (PreData.getDB(this, BoostMyConstant.NOTIFIACTIVITY, 1) == 0) {
            setting_notifi.setVisibility(View.GONE);
        }
        //通知栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setting_notifi.setVisibility(View.GONE);
        }


        if (PreData.getDB(this, BoostMyConstant.FULL_SETTING, 0) == 1) {
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

    private void initData() {
        if (PreData.getDB(BoostSetActivity.this, BoostMyConstant.TONGZHI_SWITCH, true)) {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(BoostSetActivity.this, BoostMyConstant.TONGZHILAN_SWITCH, true)) {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(BoostSetActivity.this, BoostMyConstant.FlOAT_SWITCH, true)) {
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(BoostSetActivity.this, BoostMyConstant.UNLOAD_SWITCH, true)) {
            setting_unload_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_unload_check.setImageResource(R.mipmap.side_check_normal);
        }
        if ((boolean) BatteryUtils.readData(this, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true)) {
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    protected void onResume() {
        super.onResume();
        initData();//刷新
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


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.setting_tongzhi:
                    SetAdUtilPrivacy.track("设置页面", "点击通知开关", "", 1);
                    if (PreData.getDB(BoostSetActivity.this, BoostMyConstant.TONGZHI_SWITCH, true)) {
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.TONGZHI_SWITCH, false);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.TONGZHI_SWITCH, true);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_tongzhilan:
                    SetAdUtilPrivacy.track("设置页面", "点击通知栏开关", "", 1);
                    if (PreData.getDB(BoostSetActivity.this, BoostMyConstant.TONGZHILAN_SWITCH, true)) {
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.TONGZHILAN_SWITCH, false);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
                        Intent intent = new Intent(BoostSetActivity.this, NotificationServiceBoost.class);
                        stopService(intent);
                    } else {
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        Intent intent = new Intent(BoostSetActivity.this, NotificationServiceBoost.class);
                        intent.setAction("notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_float:
                    SetAdUtilPrivacy.track("设置页面", "点击悬浮球开关", "", 1);
                    if (PreData.getDB(BoostSetActivity.this, BoostMyConstant.FlOAT_SWITCH, true)) {
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.FlOAT_SWITCH, false);
                        Intent intent1 = new Intent(BoostSetActivity.this, BoostSuspensionBallService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.FlOAT_SWITCH, true);
                        Intent intent1 = new Intent(BoostSetActivity.this, BoostSuspensionBallService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_passed);
                        startService(intent1);
                    }
                    break;
                case R.id.setting_unload:
                    SetAdUtilPrivacy.track("设置页面", "点击卸载残余开关", "", 1);
                    if (PreData.getDB(BoostSetActivity.this, BoostMyConstant.UNLOAD_SWITCH, true)) {
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.UNLOAD_SWITCH, false);
                        Intent intent1 = new Intent(BoostSetActivity.this, BoostUnloadResidualBroadcast.class);
                        setting_unload_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.UNLOAD_SWITCH, true);
                        Intent intent1 = new Intent("com.myboost.junk.boostbroadcasts.BoostUnloadResidualBroadcast");
                        setting_unload_check.setImageResource(R.mipmap.side_check_passed);
                        sendBroadcast(intent1);
                    }
                    break;
                case R.id.setting_battery:
                    if ((boolean) BatteryUtils.readData(BoostSetActivity.this, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true)) {
                        BatteryUtils.writeData(BoostSetActivity.this, BoostBatteryConstants.CHARGE_SAVER_SWITCH, false);
                        SetAdUtilPrivacy.track("设置页面", "点击充电屏保开关", "关", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        BatteryUtils.writeData(BoostSetActivity.this, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true);
                        SetAdUtilPrivacy.track("设置页面", "点击充电屏保开关", "开", 1);
                        PreData.putDB(BoostSetActivity.this, BoostMyConstant.FIRST_BATTERY, false);
                        setting_battery_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_white:
                    SetAdUtilPrivacy.track("设置页面", "进入白名单", "", 1);
                    Intent intent = new Intent(BoostSetActivity.this, BoostIgnoresAvtivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting_short:
                    SetAdUtilPrivacy.track("设置页面", "添加桌面快捷方式", "", 1);
                    PreData.putDB(BoostSetActivity.this, BoostMyConstant.KEY_SHORTCUT, true);
                    ShortCutUtilsBoost.addShortcut(BoostSetActivity.this);
                    break;
                case R.id.setting_power:
                    SetAdUtilPrivacy.track("设置页面", "进入深度清理", "", 1);
                    Intent intentP = new Intent(BoostSetActivity.this, BoostDeepingActivity.class);
                    startActivity(intentP);
                    break;
                case R.id.setting_file:
                    SetAdUtilPrivacy.track("设置页面", "进入文件管理", "", 1);
                    PreData.putDB(BoostSetActivity.this, BoostMyConstant.FILE_CLEAN, true);
                    Intent intentF = new Intent(BoostSetActivity.this, BoostFileManagerActivity.class);
                    startActivity(intentF);
                    break;
                case R.id.setting_picture:
                    SetAdUtilPrivacy.track("设置页面", "进入相似图片", "", 1);
                    PreData.putDB(BoostSetActivity.this, BoostMyConstant.PHOTO_CLEAN, true);
                    Intent intentPic = new Intent(BoostSetActivity.this, BoostPictActivity.class);
                    startActivity(intentPic);
                    break;
                case R.id.setting_gboost:
                    SetAdUtilPrivacy.track("设置页面", "进入游戏加速", "", 1);
                    PreData.putDB(BoostSetActivity.this, BoostMyConstant.GBOOST_CLEAN, true);
                    Intent intentGB = new Intent(BoostSetActivity.this, BoostGoodGameActivity.class);
                    startActivity(intentGB);
                    break;
                case R.id.setting_hui:
                    SetAdUtilPrivacy.track("设置页面", "进入回收站", "", 1);
                    Intent intenth = new Intent(BoostSetActivity.this, PictHuiActivityBoost.class);
                    startActivity(intenth);
                    break;
                case R.id.setting_notifi:
                    SetAdUtilPrivacy.track("设置页面", "进入通知栏清理", "", 1);
                    PreData.putDB(BoostSetActivity.this, BoostMyConstant.NOTIFI_CLEAN, true);
                    if (!PreData.getDB(BoostSetActivity.this, BoostMyConstant.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(BoostSetActivity.this)) {
                        //通知栏动画
                        Intent intent6 = new Intent(BoostSetActivity.this, NotifingAnimationActivityBoost.class);
                        startActivity(intent6);
                    } else {
                        //通知栏
                        Intent intent6 = new Intent(BoostSetActivity.this, BoostNotifingActivity.class);
                        startActivity(intent6);
                    }
                    break;
                case R.id.setting_rotate:
                    SetAdUtilPrivacy.track("设置页面", "好评", "", 1);
                    UtilGpBoost.rate(BoostSetActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (MyUtils.isNotificationListenEnabled(this)) {
                PreData.putDB(this, BoostMyConstant.KEY_NOTIFI, true);
                Intent intent = new Intent(this, BoostNotifingActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, NotifingAnimationActivityBoost.class);
                startActivity(intent);
            }
        }
    }

    private void addAd() {
        nativeView = SetAdUtilPrivacy.getNativeAdView(TAG_SETTING, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            if (nativeView.getHeight() == MyUtils.dp2px(250)) {
                layout_ad.height = MyUtils.dp2px(250);
            }
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
//            setting_scroll.fullScroll(ScrollView.FOCUS_UP);
            setting_scroll.setScrollY(0);
        } else {
        }
    }

    @Override
    public void onBackPressed() {
        setResult(BoostMyConstant.SETTING_RESUIL);
        finish();
    }
}
