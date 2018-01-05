package com.mutter.clean.junk.myActivity;

import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mutter.clean.junk.service.AutoService;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.android.client.AndroidSdk;
import com.mutter.module.charge.saver.Util.Constants;
import com.mutter.module.charge.saver.Util.Utils;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.service.FloatService;
import com.mutter.clean.junk.service.NotificationService;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.ShortCutUtils;
import com.mutter.clean.junk.util.UtilGp;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by on 2017/3/2.
 */

public class SettingActivity extends BaseActivity {
    RelativeLayout setting_tongzhi, setting_tongzhilan, setting_auto, setting_float, setting_battery, setting_unload, setting_power, setting_file,
            setting_picture, setting_gboost, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate;
    ImageView setting_tongzhi_check, setting_tongzhilan_check, setting_auto_check, setting_float_check, setting_battery_check, setting_unload_check;
    FrameLayout title_left;
    TextView title_name;
    private View nativeView;
    LinearLayout ll_ad;
    FrameLayout ad_fl;
    ScrollView setting_scroll;

    private String TAG_SETTING = "mutter_setting";
    private Handler myHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        setting_tongzhi = (RelativeLayout) findViewById(R.id.setting_tongzhi);
        setting_tongzhilan = (RelativeLayout) findViewById(R.id.setting_tongzhilan);
        setting_auto = (RelativeLayout) findViewById(R.id.setting_auto);
        setting_float = (RelativeLayout) findViewById(R.id.setting_float);
        setting_battery = (RelativeLayout) findViewById(R.id.setting_battery);
        setting_unload = (RelativeLayout) findViewById(R.id.setting_unload);
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
        setting_auto_check = (ImageView) findViewById(R.id.setting_auto_check);
        setting_battery_check = (ImageView) findViewById(R.id.setting_battery_check);
        setting_unload_check = (ImageView) findViewById(R.id.setting_unload_check);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        ad_fl = (FrameLayout) findViewById(R.id.ad_fl);
        setting_scroll = (ScrollView) findViewById(R.id.setting_scroll);
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
        if (PreData.getDB(this, Constant.FULL_SETTING, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AdUtil.FULL_DEFAULT);
                }
            }, 1000);
        } else {
            addAd();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setting_notifi.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.NOTIFI_KAIGUAN, 1) == 0) {
            setting_notifi.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.DEEP_KAIGUAN, 1) == 0) {
            setting_power.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.FILE_KAIGUAN, 1) == 0) {
            setting_file.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.GBOOST_KAIGUAN, 1) == 0) {
            setting_gboost.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, Constant.PICTURE_KAIGUAN, 1) == 0) {
            setting_picture.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void initData() {
        if (PreData.getDB(SettingActivity.this, Constant.TONGZHI_SWITCH, true)) {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Constant.FlOAT_SWITCH, true)) {
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Constant.AUTO_KAIGUAN)) {
            setting_auto_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_auto_check.setImageResource(R.mipmap.side_check_normal);
        }
        if ((boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, false)) {
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(this, Constant.KEY_UNLOAD, false)) {
            setting_unload_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_unload_check.setImageResource(R.mipmap.side_check_normal);
        }
    }

    private void initListener() {
        setting_tongzhi.setOnClickListener(onClickListener);
        setting_tongzhilan.setOnClickListener(onClickListener);
        setting_auto.setOnClickListener(onClickListener);
        setting_float.setOnClickListener(onClickListener);
        setting_battery.setOnClickListener(onClickListener);
        setting_unload.setOnClickListener(onClickListener);
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

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(TAG_SETTING, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            setting_scroll.setScrollY(0);
            ad_fl.setVisibility(View.VISIBLE);
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
                    AdUtil.track("设置页面", "点击通知开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Constant.TONGZHI_SWITCH, true)) {
                        PreData.putDB(SettingActivity.this, Constant.TONGZHI_SWITCH, false);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
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
                        ShortcutBadger.removeCount(SettingActivity.this);
                    } else {
                        PreData.putDB(SettingActivity.this, Constant.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        Intent intent = new Intent(SettingActivity.this, NotificationService.class);
                        intent.putExtra("from", "notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_auto:
                    if (PreData.getDB(SettingActivity.this, Constant.AUTO_KAIGUAN)) {
                        PreData.putDB(SettingActivity.this, Constant.AUTO_KAIGUAN, false);
                        Intent intent1 = new Intent(SettingActivity.this, AutoService.class);
                        setting_auto_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        PreData.putDB(SettingActivity.this, Constant.AUTO_KAIGUAN, true);
                        Intent intent1 = new Intent(SettingActivity.this, AutoService.class);
                        setting_auto_check.setImageResource(R.mipmap.side_check_passed);
                        startService(intent1);
                    }
                    break;
                case R.id.setting_float:
                    AdUtil.track("设置页面", "点击悬浮球开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Constant.FlOAT_SWITCH, true)) {
                        PreData.putDB(SettingActivity.this, Constant.FlOAT_SWITCH, false);
                        Intent intent1 = new Intent(SettingActivity.this, FloatService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        PreData.putDB(SettingActivity.this, Constant.FlOAT_SWITCH, true);
                        Intent intent1 = new Intent(SettingActivity.this, FloatService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_passed);
                        startService(intent1);
                    }
                    break;
                case R.id.setting_battery:
                    //chongdian
                    if ((boolean) Utils.readData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, false)) {
                        Utils.writeData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, false);
                        AdUtil.track("设置页面", "点击充电屏保开关", "关", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        Utils.writeData(SettingActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                        AdUtil.track("设置页面", "点击充电屏保开关", "开", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_unload:
                    //chongdian
                    if (PreData.getDB(SettingActivity.this, Constant.KEY_UNLOAD, false)) {
                        PreData.putDB(SettingActivity.this, Constant.KEY_UNLOAD, false);
                        setting_unload_check.setImageResource(R.mipmap.side_check_normal);
                        AdUtil.track("设置页面", "点击卸载残余开关", "关", 1);
                    } else {
                        PreData.putDB(SettingActivity.this, Constant.KEY_UNLOAD, true);
                        AdUtil.track("设置页面", "点击卸载残余开关", "开", 1);
                        setting_unload_check.setImageResource(R.mipmap.side_check_passed);
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
                    PreData.putDB(SettingActivity.this, Constant.DEEP_CLEAN, true);
                    Intent intentP = new Intent(SettingActivity.this, PowerActivity.class);
                    startActivity(intentP);
                    break;
                case R.id.setting_file:
                    AdUtil.track("设置页面", "进入文件管理", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.FILE_CLEAN, true);
                    Intent intentF = new Intent(SettingActivity.this, FileManaActivity.class);
                    startActivity(intentF);
                    break;
                case R.id.setting_picture:
                    AdUtil.track("设置页面", "进入相似图片", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.PHOTO_CLEAN, true);
                    Intent intentPic = new Intent(SettingActivity.this, SimilarActivity.class);
                    startActivity(intentPic);
                    break;
                case R.id.setting_gboost:
                    AdUtil.track("设置页面", "进入游戏加速", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.GBOOST_CLEAN, true);
                    Intent intentGB = new Intent(SettingActivity.this, GameActivity.class);
                    startActivity(intentGB);
                    break;
                case R.id.setting_hui:
                    AdUtil.track("设置页面", "进入回收站", "", 1);
                    Intent intenth = new Intent(SettingActivity.this, PhotoHuiActivity.class);
                    startActivity(intenth);
                    break;
                case R.id.setting_notifi:
                    AdUtil.track("设置页面", "进入通知栏清理", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(SettingActivity.this) || !PreData.getDB(SettingActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SettingActivity.this, NotifiAnimationActivity.class);
                        startActivity(intent6);
                    } else {
                        Intent intent6 = new Intent(SettingActivity.this, NotifiActivity.class);
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
                Intent intent = new Intent(this, NotifiActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, NotifiAnimationActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Constant.SETTING_RESUIL);
        finish();
    }
}
