package com.privacy.junk.activityprivacy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.privacy.clean.utilsprivacy.MyUtils;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.junk.R;
import com.privacy.junk.privacybroadcasts.UnloadResidualBroadcastPrivacy;
import com.privacy.junk.privacyservices.PrivacyNotificationService;
import com.privacy.junk.privacyservices.PrivacySuspensionBallService;
import com.privacy.junk.toolsprivacy.PrivacyUtilGp;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.toolsprivacy.SetAdUtilPrivacy;
import com.privacy.junk.toolsprivacy.PrivacyShortCutUtils;
import com.privacy.module.charge.saver.privacyutils.BatteryConstantsPrivacy;
import com.privacy.module.charge.saver.privacyutils.UtilsPrivacy;
import com.privacy.module.charge.saver.protectview.PrivacyCustomerTypefaceTextView;

/**
 * Created by on 2017/3/2.
 */

public class SetActivityPrivacy extends BaseActivity {
    private String TAG_SETTING = "cprivacy_setting";
    ImageView setting_tongzhi_check, setting_float_check, setting_unload_check, setting_battery_check, setting_tongzhilan_check;
    FrameLayout title_left;
    ScrollView setting_scroll;
    private Handler myHandler;
    RelativeLayout setting_tongzhi, setting_float, setting_battery, setting_power, setting_file, setting_tongzhilan,
            setting_picture, setting_gboost, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate, setting_unload;
    LinearLayout ll_ad;
    private View nativeView;
    TextView title_name;
    PrivacyCustomerTypefaceTextView setting_battery_tv, setting_float_tv, setting_tongzhilan_tv, setting_tongzhi_tv, setting_unload_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        myHandler = new Handler();
        title_name.setText(R.string.setting_name);
        title_left.setOnClickListener(onClickListener);
        initData();
        initListener();
        //游戏
        if (PreData.getDB(this, MyConstantPrivacy.GOODGAME, 1) == 0) {
            setting_gboost.setVisibility(View.GONE);
        }
        //相似图片
        if (PreData.getDB(this, MyConstantPrivacy.PICTUREX, 1) == 0) {
            setting_picture.setVisibility(View.GONE);
        }
        //深度清理
        if (PreData.getDB(this, MyConstantPrivacy.POWERACTIVITY, 1) == 0) {
            setting_power.setVisibility(View.GONE);
        }
        //文件
        if (PreData.getDB(this, MyConstantPrivacy.FILEACTIVITY, 1) == 0) {
            setting_file.setVisibility(View.GONE);
        }
        //通知栏
        if (PreData.getDB(this, MyConstantPrivacy.NOTIFIACTIVITY, 1) == 0) {
            setting_notifi.setVisibility(View.GONE);
        }
        //通知栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setting_notifi.setVisibility(View.GONE);
        }


        if (PreData.getDB(this, MyConstantPrivacy.FULL_SETTING, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(SetAdUtilPrivacy.DEFAULT_FULL);
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
        if (PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.TONGZHI_SWITCH, true)) {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
            setting_tongzhi_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
        } else {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
            setting_tongzhi_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
        }
        if (PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.TONGZHILAN_SWITCH, true)) {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
            setting_tongzhilan_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
        } else {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
            setting_tongzhilan_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
        }
        if (PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.FlOAT_SWITCH, true)) {
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
            setting_float_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
        } else {
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
            setting_float_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
        }
        if (PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.UNLOAD_SWITCH, true)) {
            setting_unload_check.setImageResource(R.mipmap.side_check_passed);
            setting_unload_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
        } else {
            setting_unload_check.setImageResource(R.mipmap.side_check_normal);
            setting_unload_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
        }
        if ((boolean) UtilsPrivacy.readData(this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true)) {
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
            setting_battery_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
        } else {
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
            setting_battery_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
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

        setting_battery_tv = (PrivacyCustomerTypefaceTextView) findViewById(R.id.setting_battery_tv);
        setting_float_tv = (PrivacyCustomerTypefaceTextView) findViewById(R.id.setting_float_tv);
        setting_tongzhilan_tv = (PrivacyCustomerTypefaceTextView) findViewById(R.id.setting_tongzhilan_tv);
        setting_tongzhi_tv = (PrivacyCustomerTypefaceTextView) findViewById(R.id.setting_tongzhi_tv);
        setting_unload_tv = (PrivacyCustomerTypefaceTextView) findViewById(R.id.setting_unload_tv);
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
                    if (PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.TONGZHI_SWITCH, true)) {
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.TONGZHI_SWITCH, false);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
                        setting_tongzhi_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
                    } else {
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.TONGZHI_SWITCH, true);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
                        setting_tongzhi_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
                    }
                    break;
                case R.id.setting_tongzhilan:
                    SetAdUtilPrivacy.track("设置页面", "点击通知栏开关", "", 1);
                    if (PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.TONGZHILAN_SWITCH, true)) {
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.TONGZHILAN_SWITCH, false);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
                        Intent intent = new Intent(SetActivityPrivacy.this, PrivacyNotificationService.class);
                        stopService(intent);
                        setting_tongzhilan_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
                    } else {
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        setting_tongzhilan_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
                        Intent intent = new Intent(SetActivityPrivacy.this, PrivacyNotificationService.class);
                        intent.setAction("notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_float:
                    SetAdUtilPrivacy.track("设置页面", "点击悬浮球开关", "", 1);
                    if (PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.FlOAT_SWITCH, true)) {
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.FlOAT_SWITCH, false);
                        Intent intent1 = new Intent(SetActivityPrivacy.this, PrivacySuspensionBallService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                        setting_float_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
                    } else {
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.FlOAT_SWITCH, true);
                        Intent intent1 = new Intent(SetActivityPrivacy.this, PrivacySuspensionBallService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_passed);
                        setting_float_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
                        startService(intent1);
                    }
                    break;
                case R.id.setting_unload:
                    SetAdUtilPrivacy.track("设置页面", "点击卸载残余开关", "", 1);
                    if (PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.UNLOAD_SWITCH, true)) {
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.UNLOAD_SWITCH, false);
                        setting_unload_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
                        Intent intent1 = new Intent(SetActivityPrivacy.this, UnloadResidualBroadcastPrivacy.class);
                        setting_unload_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.UNLOAD_SWITCH, true);
                        Intent intent1 = new Intent("com.privacy.junk.privacybroadcasts.UnloadResidualBroadcastPrivacy");
                        setting_unload_check.setImageResource(R.mipmap.side_check_passed);
                        setting_unload_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
                        sendBroadcast(intent1);
                    }
                    break;
                case R.id.setting_battery:
                    if ((boolean) UtilsPrivacy.readData(SetActivityPrivacy.this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true)) {
                        UtilsPrivacy.writeData(SetActivityPrivacy.this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, false);
                        SetAdUtilPrivacy.track("设置页面", "点击充电屏保开关", "关", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_normal);
                        setting_battery_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4_54));
                    } else {
                        UtilsPrivacy.writeData(SetActivityPrivacy.this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true);
                        SetAdUtilPrivacy.track("设置页面", "点击充电屏保开关", "开", 1);
                        PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.FIRST_BATTERY, false);
                        setting_battery_check.setImageResource(R.mipmap.side_check_passed);
                        setting_battery_tv.setTextColor(ContextCompat.getColor(SetActivityPrivacy.this, R.color.A4));
                    }
                    break;
                case R.id.setting_white:
                    SetAdUtilPrivacy.track("设置页面", "进入白名单", "", 1);
                    Intent intent = new Intent(SetActivityPrivacy.this, IgnoresAvtivityPrivacy.class);
                    startActivity(intent);
                    break;
                case R.id.setting_short:
                    SetAdUtilPrivacy.track("设置页面", "添加桌面快捷方式", "", 1);
                    PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.KEY_SHORTCUT, true);
                    PrivacyShortCutUtils.addShortcut(SetActivityPrivacy.this);
                    break;
                case R.id.setting_power:
                    SetAdUtilPrivacy.track("设置页面", "进入深度清理", "", 1);
                    Intent intentP = new Intent(SetActivityPrivacy.this, DeepingActivityPrivacy.class);
                    startActivity(intentP);
                    break;
                case R.id.setting_file:
                    SetAdUtilPrivacy.track("设置页面", "进入文件管理", "", 1);
                    PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.FILE_CLEAN, true);
                    Intent intentF = new Intent(SetActivityPrivacy.this, FileManagerActivityPrivacy.class);
                    startActivity(intentF);
                    break;
                case R.id.setting_picture:
                    SetAdUtilPrivacy.track("设置页面", "进入相似图片", "", 1);
                    PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.PHOTO_CLEAN, true);
                    Intent intentPic = new Intent(SetActivityPrivacy.this, PictActivityPrivacy.class);
                    startActivity(intentPic);
                    break;
                case R.id.setting_gboost:
                    SetAdUtilPrivacy.track("设置页面", "进入游戏加速", "", 1);
                    PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.GBOOST_CLEAN, true);
                    Intent intentGB = new Intent(SetActivityPrivacy.this, GoodGameActivityPrivacy.class);
                    startActivity(intentGB);
                    break;
                case R.id.setting_hui:
                    SetAdUtilPrivacy.track("设置页面", "进入回收站", "", 1);
                    Intent intenth = new Intent(SetActivityPrivacy.this, PrivacyPictHuiActivity.class);
                    startActivity(intenth);
                    break;
                case R.id.setting_notifi:
                    SetAdUtilPrivacy.track("设置页面", "进入通知栏清理", "", 1);
                    PreData.putDB(SetActivityPrivacy.this, MyConstantPrivacy.NOTIFI_CLEAN, true);
                    if (!PreData.getDB(SetActivityPrivacy.this, MyConstantPrivacy.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(SetActivityPrivacy.this)) {
                        //通知栏动画
                        Intent intent6 = new Intent(SetActivityPrivacy.this, PrivacyNotifingAnimationActivity.class);
                        startActivity(intent6);
                    } else {
                        //通知栏
                        Intent intent6 = new Intent(SetActivityPrivacy.this, MyNotifingActivityPrivacy.class);
                        startActivity(intent6);
                    }
                    break;
                case R.id.setting_rotate:
                    SetAdUtilPrivacy.track("设置页面", "好评", "", 1);
                    PrivacyUtilGp.rate(SetActivityPrivacy.this);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (MyUtils.isNotificationListenEnabled(this)) {
                PreData.putDB(this, MyConstantPrivacy.KEY_NOTIFI, true);
                Intent intent = new Intent(this, MyNotifingActivityPrivacy.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, PrivacyNotifingAnimationActivity.class);
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
        setResult(MyConstantPrivacy.SETTING_RESUIL);
        finish();
    }
}
