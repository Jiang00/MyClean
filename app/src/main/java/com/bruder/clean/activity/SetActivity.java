package com.bruder.clean.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.junk.R;
import com.bruder.clean.myservice.FloatingService;
import com.bruder.clean.myservice.NotificationingService;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.ShortCutUtils;
import com.bruder.clean.util.UtilAd;
import com.bruder.clean.util.UtilGp;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;
import com.my.bruder.charge.saver.Util.Constants;
import com.my.bruder.charge.saver.Util.Utils;

/**
 * Created by on 2017/3/2.
 */

public class SetActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    private String TAG_SETTING = "bruder_setting";//
    private Handler myHandler;
    RelativeLayout setting_tongzhi, setting_tongzhilan, setting_float, setting_battery, setting_unload, setting_power, setting_file,
            setting_picture, setting_gboost, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate;
    LinearLayout ll_ad;
    ScrollView setting_scroll;
    private View nativeView;
    ImageView setting_tongzhi_check, setting_tongzhilan_check, setting_float_check, setting_battery_check, setting_unload_check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        myHandler = new Handler();
        title_name.setText(R.string.setting_name);
        title_left.setOnClickListener(onClickListener);
        initData();
        initListener();
        if (DataPre.getDB(this, Constant.FULL_SETTING, 0) == 1) {
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
        setting_battery_check = (ImageView) findViewById(R.id.setting_battery_check);
        setting_unload_check = (ImageView) findViewById(R.id.setting_unload_check);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        setting_scroll = (ScrollView) findViewById(R.id.setting_scroll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        if (DataPre.getDB(this, Constant.FILEACTIVITY, 0) == 0) {
            setting_file.setVisibility(View.GONE);
        }
        if (DataPre.getDB(this, Constant.POWERACATIVITY, 0) == 0) {
            setting_power.setVisibility(View.GONE);
        }
        if (DataPre.getDB(this, Constant.NOTIFIACTIVITY, 0) == 0) {
            setting_notifi.setVisibility(View.GONE);
        }
    }


    private void initData() {
        if (DataPre.getDB(SetActivity.this, Constant.TONGZHI_SWITCH, true)) {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (DataPre.getDB(SetActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (DataPre.getDB(SetActivity.this, Constant.FlOAT_SWITCH, true)) {
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
        }
        if ((boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, false)) {
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (DataPre.getDB(this, Constant.KEY_UNLOAD, true)) {
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void addAd() {
        nativeView = UtilAd.getNativeAdView(TAG_SETTING, R.layout.native_ad_3);
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
                    UtilAd.track("设置页面", "点击通知开关", "", 1);
                    if (DataPre.getDB(SetActivity.this, Constant.TONGZHI_SWITCH, true)) {
                        DataPre.putDB(SetActivity.this, Constant.TONGZHI_SWITCH, false);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        DataPre.putDB(SetActivity.this, Constant.TONGZHI_SWITCH, true);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_tongzhilan:
                    UtilAd.track("设置页面", "点击通知栏开关", "", 1);
                    if (DataPre.getDB(SetActivity.this, Constant.TONGZHILAN_SWITCH, true)) {
                        DataPre.putDB(SetActivity.this, Constant.TONGZHILAN_SWITCH, false);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
                        Intent intent = new Intent(SetActivity.this, NotificationingService.class);
                        stopService(intent);
                    } else {
                        DataPre.putDB(SetActivity.this, Constant.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        Intent intent = new Intent(SetActivity.this, NotificationingService.class);
                        intent.setAction("notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_float:
                    UtilAd.track("设置页面", "点击悬浮球开关", "", 1);
                    if (DataPre.getDB(SetActivity.this, Constant.FlOAT_SWITCH, true)) {
                        DataPre.putDB(SetActivity.this, Constant.FlOAT_SWITCH, false);
                        Intent intent1 = new Intent(SetActivity.this, FloatingService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        DataPre.putDB(SetActivity.this, Constant.FlOAT_SWITCH, true);
                        Intent intent1 = new Intent(SetActivity.this, FloatingService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_passed);
                        startService(intent1);
                    }
                    break;
                case R.id.setting_battery:
                    //chongdian
                    if ((boolean) Utils.readData(SetActivity.this, Constants.CHARGE_SAVER_SWITCH, false)) {
                        Utils.writeData(SetActivity.this, Constants.CHARGE_SAVER_SWITCH, false);
                        UtilAd.track("设置页面", "点击充电屏保开关", "关", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        Utils.writeData(SetActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                        UtilAd.track("设置页面", "点击充电屏保开关", "开", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_unload:
                    //chongdian
                    if (DataPre.getDB(SetActivity.this, Constant.KEY_UNLOAD, true)) {
                        DataPre.putDB(SetActivity.this, Constant.KEY_UNLOAD, false);
                        setting_unload_check.setImageResource(R.mipmap.side_check_normal);
                        UtilAd.track("设置页面", "点击卸载残余开关", "关", 1);
                    } else {
                        DataPre.putDB(SetActivity.this, Constant.KEY_UNLOAD, true);
                        UtilAd.track("设置页面", "点击卸载残余开关", "开", 1);
                        setting_unload_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_white:
                    UtilAd.track("设置页面", "进入白名单", "", 1);
                    Intent intent = new Intent(SetActivity.this, WhiteAvtivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting_short:
                    UtilAd.track("设置页面", "添加桌面快捷方式", "", 1);
                    DataPre.putDB(SetActivity.this, Constant.KEY_SHORTCUT, true);
                    ShortCutUtils.addShortcut(SetActivity.this);
                    break;
                case R.id.setting_power:
                    UtilAd.track("设置页面", "进入深度清理", "", 1);
                    DataPre.putDB(SetActivity.this, Constant.DEEP_CLEAN, true);
                    Intent intentP = new Intent(SetActivity.this, PoweringActivity.class);
                    startActivity(intentP);
                    break;
                case R.id.setting_file:
                    UtilAd.track("设置页面", "进入文件管理", "", 1);
                    DataPre.putDB(SetActivity.this, Constant.FILE_CLEAN, true);
                    Intent intentF = new Intent(SetActivity.this, FilesActivity.class);
                    startActivity(intentF);
                    break;
                case R.id.setting_picture:
                    UtilAd.track("设置页面", "进入相似图片", "", 1);
                    DataPre.putDB(SetActivity.this, Constant.PHOTO_CLEAN, true);
                    Intent intentPic = new Intent(SetActivity.this, PicturesActivity.class);
                    startActivity(intentPic);
                    break;
                case R.id.setting_gboost:
                    UtilAd.track("设置页面", "进入游戏加速", "", 1);
                    DataPre.putDB(SetActivity.this, Constant.GBOOST_CLEAN, true);
                    Intent intentGB = new Intent(SetActivity.this, GBoostingActivity.class);
                    startActivity(intentGB);
                    break;
                case R.id.setting_hui:
                    UtilAd.track("设置页面", "进入回收站", "", 1);
                    Intent intenth = new Intent(SetActivity.this, PicturesHuiActivity.class);
                    startActivity(intenth);
                    break;
                case R.id.setting_notifi:
                    UtilAd.track("设置页面", "进入通知栏清理", "", 1);
                    DataPre.putDB(SetActivity.this, Constant.NOTIFI_CLEAN, true);
                    if (!DataPre.getDB(SetActivity.this, Constant.KEY_NOTIFI, true) || !Util.isNotificationListenEnabled(SetActivity.this)) {
                        Intent intent6 = new Intent(SetActivity.this, NotifiIfActivity.class);
                        startActivity(intent6);
                    } else {
                        Intent intent6 = new Intent(SetActivity.this, NotifingActivity.class);
                        startActivity(intent6);
                    }
                    break;
                case R.id.setting_rotate:
                    UtilAd.track("设置页面", "好评", "", 1);
                    UtilGp.rate(SetActivity.this);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        setResult(Constant.SETTING_RESUIL);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (Util.isNotificationListenEnabled(this)) {
                DataPre.putDB(this, Constant.KEY_NOTIFI, true);
                Intent intent = new Intent(this, NotifingActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, NotifiIfActivity.class);
                startActivity(intent);
            }
        }
    }
}
