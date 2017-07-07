package com.supers.clean.junk.activity;

import android.content.Intent;
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

import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.ui.demo.cross.CrossManager;
import com.eos.ui.demo.dialog.DialogManager;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.service.FloatService;
import com.supers.clean.junk.service.NotificationService;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.ShortCutUtils;
import com.supers.clean.junk.util.UtilGp;

/**
 * Created by on 2017/3/2.
 */

public class SettingActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    RelativeLayout setting_tongzhi, setting_tongzhilan, setting_float, setting_battery, setting_detect, setting_unload, setting_power, setting_file,
            setting_picture, setting_wifi, setting_gboost, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate;
    ImageView setting_tongzhi_check, setting_tongzhilan_check, setting_float_check, setting_battery_check, setting_detect_check, setting_unload_check;
    LinearLayout ll_ad;
    ScrollView setting_scroll;
    FrameLayout fl_lot_setting;
    private View nativeView;

    private String TAG_SETTING = "eos_setting";
    private Handler myHandler;
    private LottieAnimationView lot_setting;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        setting_tongzhi = (RelativeLayout) findViewById(R.id.setting_tongzhi);
        setting_tongzhilan = (RelativeLayout) findViewById(R.id.setting_tongzhilan);
        setting_float = (RelativeLayout) findViewById(R.id.setting_float);
        setting_battery = (RelativeLayout) findViewById(R.id.setting_battery);
        setting_detect = (RelativeLayout) findViewById(R.id.setting_detect);
        setting_unload = (RelativeLayout) findViewById(R.id.setting_unload);
        setting_white = (RelativeLayout) findViewById(R.id.setting_white);
        setting_short = (RelativeLayout) findViewById(R.id.setting_short);
        setting_power = (RelativeLayout) findViewById(R.id.setting_power);
        setting_notifi = (RelativeLayout) findViewById(R.id.setting_notifi);
        setting_file = (RelativeLayout) findViewById(R.id.setting_file);
        setting_picture = (RelativeLayout) findViewById(R.id.setting_picture);
        setting_wifi = (RelativeLayout) findViewById(R.id.setting_wifi);
        setting_gboost = (RelativeLayout) findViewById(R.id.setting_gboost);
        setting_hui = (RelativeLayout) findViewById(R.id.setting_hui);
        setting_rotate = (RelativeLayout) findViewById(R.id.setting_rotate);
        setting_tongzhi_check = (ImageView) findViewById(R.id.setting_tongzhi_check);
        setting_tongzhilan_check = (ImageView) findViewById(R.id.setting_tongzhilan_check);
        setting_float_check = (ImageView) findViewById(R.id.setting_float_check);
        setting_battery_check = (ImageView) findViewById(R.id.setting_battery_check);
        setting_detect_check = (ImageView) findViewById(R.id.setting_detect_check);
        setting_unload_check = (ImageView) findViewById(R.id.setting_unload_check);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        setting_scroll = (ScrollView) findViewById(R.id.setting_scroll);
        fl_lot_setting = (FrameLayout) findViewById(R.id.fl_lot_setting);
        findViewById(R.id.setting_privacy).setOnClickListener(onClickListener);
        findViewById(R.id.setting_language).setOnClickListener(onClickListener);
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
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
                }
            }, 1000);
            tuiGuang();
        } else {
            addAd();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setting_notifi.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lot_setting != null) {
            lot_setting.playAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lot_setting != null) {
            lot_setting.pauseAnimation();
        }
    }

    public void tuiGuang() {
        super.tuiGuang();
        DialogManager.getCrossView(getApplicationContext(), extraData, "list1", "Setting", true, new CrossManager.onCrossViewClickListener() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void onLoadView(View view) {
                if (view != null) {
                    ((ImageView) view.findViewById(R.id.cross_default_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    lot_setting = ((LottieAnimationView) view.findViewById(R.id.cross_default_lottie));
                    lot_setting.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    fl_lot_setting.setVisibility(View.VISIBLE);
                    fl_lot_setting.addView(view, 0);
                    if (onPause) {
                        lot_setting.pauseAnimation();
                    }
                } else {
                    fl_lot_setting.setVisibility(View.GONE);
                }
            }
        });
    }

    private void addAd() {
        nativeView = AdUtil.getNativeAdView(TAG_SETTING, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            Log.e("aaa", "=====" + layout_ad.height);
            if (nativeView.getHeight() == Util.dp2px(250)) {
                layout_ad.height = Util.dp2px(250);
            }
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
//            setting_scroll.fullScroll(ScrollView.FOCUS_UP);
            setting_scroll.setScrollY(0);
        } else {
            tuiGuang();
        }
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
        if ((boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, false)) {
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Constant.DETECT_KAIGUAN, true)) {
            setting_detect_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_detect_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(this, Constant.KEY_UNLOAD, true)) {
            setting_unload_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_unload_check.setImageResource(R.mipmap.side_check_normal);
        }
    }

    private void initListener() {
        setting_tongzhi.setOnClickListener(onClickListener);
        setting_tongzhilan.setOnClickListener(onClickListener);
        setting_float.setOnClickListener(onClickListener);
        setting_battery.setOnClickListener(onClickListener);
        setting_detect.setOnClickListener(onClickListener);
        setting_unload.setOnClickListener(onClickListener);
        setting_white.setOnClickListener(onClickListener);
        setting_short.setOnClickListener(onClickListener);
        setting_power.setOnClickListener(onClickListener);
        setting_notifi.setOnClickListener(onClickListener);
        setting_file.setOnClickListener(onClickListener);
        setting_picture.setOnClickListener(onClickListener);
        setting_wifi.setOnClickListener(onClickListener);
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
                    } else {
                        PreData.putDB(SettingActivity.this, Constant.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        Intent intent = new Intent(SettingActivity.this, NotificationService.class);
                        intent.setAction("notification");
                        startService(intent);
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
                case R.id.setting_detect:
                    if (PreData.getDB(SettingActivity.this, Constant.DETECT_KAIGUAN, true)) {
                        AdUtil.track("侧边栏", "点击关闭充电检测", "", 1);
                        PreData.putDB(SettingActivity.this, Constant.DETECT_KAIGUAN, false);
                    } else {
                        AdUtil.track("侧边栏", "点击开启充电检测", "", 1);
                        PreData.putDB(SettingActivity.this, Constant.DETECT_KAIGUAN, true);
                    }
                    break;
                case R.id.setting_unload:
                    //chongdian
                    if (PreData.getDB(SettingActivity.this, Constant.KEY_UNLOAD, true)) {
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
                    Intent intent = new Intent(SettingActivity.this, WhiteListAvtivity.class);
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
                    Intent intentF = new Intent(SettingActivity.this, FileActivity.class);
                    startActivity(intentF);
                    break;
                case R.id.setting_wifi:
                    AdUtil.track("设置页面", "进入网络管理", "", 1);
                    Intent intentwifi = new Intent(SettingActivity.this, NetMonitor.class);
                    startActivity(intentwifi);
                    break;
                case R.id.setting_picture:
                    AdUtil.track("设置页面", "进入相似图片", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.PHOTO_CLEAN, true);
                    Intent intentPic = new Intent(SettingActivity.this, PictureActivity.class);
                    startActivity(intentPic);
                    break;
                case R.id.setting_gboost:
                    AdUtil.track("设置页面", "进入游戏加速", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.GBOOST_CLEAN, true);
                    Intent intentGB = new Intent(SettingActivity.this, GBoostActivity.class);
                    startActivity(intentGB);
                    break;
                case R.id.setting_hui:
                    AdUtil.track("设置页面", "进入回收站", "", 1);
                    Intent intenth = new Intent(SettingActivity.this, PictureHuiActivity.class);
                    startActivity(intenth);
                    break;
                case R.id.setting_notifi:
                    AdUtil.track("设置页面", "进入通知栏清理", "", 1);
                    PreData.putDB(SettingActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!Util.isNotificationListenEnabled(SettingActivity.this)) {
                        startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 100);
                    } else if (!PreData.getDB(SettingActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SettingActivity.this, NotifiInfoActivity.class);
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
                case R.id.setting_privacy:
                    AdUtil.track("设置页面", "隐私清理", "", 1);
                    Intent privacyIntent = new Intent(SettingActivity.this, PrivacyActivity.class);
                    startActivity(privacyIntent);
                    break;
                case R.id.setting_language:
                    AdUtil.track("设置页面", "语言", "", 1);
                    Intent lagIntent = new Intent(SettingActivity.this, LanguageSettingActivity.class);
                    startActivity(lagIntent);
                    break;
                default:
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
                Intent intent = new Intent(this, NotifiInfoActivity.class);
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
