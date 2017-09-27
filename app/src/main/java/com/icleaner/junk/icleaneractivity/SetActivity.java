package com.icleaner.junk.icleaneractivity;

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
import com.icleaner.clean.utils.MyUtils;
import com.icleaner.clean.utils.PreData;
import com.icleaner.junk.R;
import com.icleaner.junk.mytools.MUtilGp;
import com.icleaner.junk.mytools.MyConstant;
import com.icleaner.junk.mytools.SetAdUtil;
import com.icleaner.junk.mytools.ShortCutUtils;
import com.icleaner.junk.services.MyNotificationService;
import com.icleaner.junk.services.SuspensionBallService;
import com.icleaner.module.charge.saver.Utils.BatteryConstants;
import com.icleaner.module.charge.saver.Utils.Utils;

/**
 * Created by on 2017/3/2.
 */

public class SetActivity extends BaseActivity {
    ImageView setting_tongzhi_check, setting_float_check, setting_battery_check, setting_tongzhilan_check;
    FrameLayout title_left;
    ScrollView setting_scroll;
    LinearLayout ll_ad;
    private View nativeView;
    TextView title_name;
    private String TAG_SETTING = "icleaner_setting";
    private Handler myHandler;
    RelativeLayout setting_tongzhi, setting_float, setting_battery, setting_power, setting_file,
            setting_picture, setting_gboost, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate, setting_tongzhilan;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);

        myHandler = new Handler();
        title_name.setText(R.string.setting_name);
        title_left.setOnClickListener(onClickListener);
        initData();
        initListener();
        if (PreData.getDB(this, MyConstant.FULL_SETTING, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(SetAdUtil.DEFAULT_FULL);
                }
            }, 1000);
        } else {
            addAd();
        }
        if (PreData.getDB(this, MyConstant.GOODGAME, 1) == 0) {
            setting_gboost.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.PICTUREX, 1) == 0) {
            setting_picture.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.POWERACTIVITY, 1) == 0) {
            setting_power.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.FILEACTIVITY, 1) == 0) {
            setting_file.setVisibility(View.GONE);
        }
        if (PreData.getDB(this, MyConstant.NOTIFIACTIVITY, 1) == 0) {
            setting_notifi.setVisibility(View.GONE);
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
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        setting_scroll = (ScrollView) findViewById(R.id.setting_scroll);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        if (PreData.getDB(SetActivity.this, MyConstant.TONGZHI_SWITCH, true)) {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SetActivity.this, MyConstant.TONGZHILAN_SWITCH, true)) {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SetActivity.this, MyConstant.FlOAT_SWITCH, true)) {
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
        }
        if ((boolean) Utils.readData(this, BatteryConstants.CHARGE_SAVER_SWITCH, true)) {
            setting_battery_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_battery_check.setImageResource(R.mipmap.side_check_normal);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                    SetAdUtil.track("设置页面", "点击通知开关", "", 1);
                    if (PreData.getDB(SetActivity.this, MyConstant.TONGZHI_SWITCH, true)) {
                        PreData.putDB(SetActivity.this, MyConstant.TONGZHI_SWITCH, false);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        PreData.putDB(SetActivity.this, MyConstant.TONGZHI_SWITCH, true);
                        setting_tongzhi_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_tongzhilan:
                    SetAdUtil.track("设置页面", "点击通知栏开关", "", 1);
                    if (PreData.getDB(SetActivity.this, MyConstant.TONGZHILAN_SWITCH, true)) {
                        PreData.putDB(SetActivity.this, MyConstant.TONGZHILAN_SWITCH, false);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_normal);
                        Intent intent = new Intent(SetActivity.this, MyNotificationService.class);
                        stopService(intent);
                    } else {
                        PreData.putDB(SetActivity.this, MyConstant.TONGZHILAN_SWITCH, true);
                        setting_tongzhilan_check.setImageResource(R.mipmap.side_check_passed);
                        Intent intent = new Intent(SetActivity.this, MyNotificationService.class);
                        intent.putExtra("from", "notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_float:
                    SetAdUtil.track("设置页面", "点击悬浮球开关", "", 1);
                    if (PreData.getDB(SetActivity.this, MyConstant.FlOAT_SWITCH, true)) {
                        PreData.putDB(SetActivity.this, MyConstant.FlOAT_SWITCH, false);
                        Intent intent1 = new Intent(SetActivity.this, SuspensionBallService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_normal);
                        stopService(intent1);
                    } else {
                        PreData.putDB(SetActivity.this, MyConstant.FlOAT_SWITCH, true);
                        Intent intent1 = new Intent(SetActivity.this, SuspensionBallService.class);
                        setting_float_check.setImageResource(R.mipmap.side_check_passed);
                        startService(intent1);
                    }
                    break;
                case R.id.setting_battery:
                    if ((boolean) Utils.readData(SetActivity.this, BatteryConstants.CHARGE_SAVER_SWITCH, true)) {
                        Utils.writeData(SetActivity.this, BatteryConstants.CHARGE_SAVER_SWITCH, false);
                        SetAdUtil.track("设置页面", "点击充电屏保开关", "关", 1);
                        setting_battery_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        Utils.writeData(SetActivity.this, BatteryConstants.CHARGE_SAVER_SWITCH, true);
                        SetAdUtil.track("设置页面", "点击充电屏保开关", "开", 1);
                        PreData.putDB(SetActivity.this, MyConstant.FIRST_BATTERY, false);
                        setting_battery_check.setImageResource(R.mipmap.side_check_passed);
                    }
                    break;
                case R.id.setting_white:
                    SetAdUtil.track("设置页面", "进入白名单", "", 1);
                    Intent intent = new Intent(SetActivity.this, IgnoresAvtivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting_short:
                    SetAdUtil.track("设置页面", "添加桌面快捷方式", "", 1);
                    PreData.putDB(SetActivity.this, MyConstant.KEY_SHORTCUT, true);
                    ShortCutUtils.addShortcut(SetActivity.this);
                    break;
                case R.id.setting_power:
                    SetAdUtil.track("设置页面", "进入深度清理", "", 1);
                    Intent intentP = new Intent(SetActivity.this, DeepingActivity.class);
                    startActivity(intentP);
                    break;
                case R.id.setting_file:
                    SetAdUtil.track("设置页面", "进入文件管理", "", 1);
                    PreData.putDB(SetActivity.this, MyConstant.FILE_CLEAN, true);
                    Intent intentF = new Intent(SetActivity.this, PhoneFileManagerActivity.class);
                    startActivity(intentF);
                    break;
                case R.id.setting_picture:
                    SetAdUtil.track("设置页面", "进入相似图片", "", 1);
                    PreData.putDB(SetActivity.this, MyConstant.PHOTO_CLEAN, true);
                    Intent intentPic = new Intent(SetActivity.this, PictActivity.class);
                    startActivity(intentPic);
                    break;
                case R.id.setting_gboost:
                    SetAdUtil.track("设置页面", "进入游戏加速", "", 1);
                    PreData.putDB(SetActivity.this, MyConstant.GBOOST_CLEAN, true);
                    Intent intentGB = new Intent(SetActivity.this, GoodGameActivity.class);
                    startActivity(intentGB);
                    break;
                case R.id.setting_hui:
                    SetAdUtil.track("设置页面", "进入回收站", "", 1);
                    Intent intenth = new Intent(SetActivity.this, PictHuiActivity.class);
                    startActivity(intenth);
                    break;
                case R.id.setting_notifi:
                    SetAdUtil.track("设置页面", "进入通知栏清理", "", 1);
                    PreData.putDB(SetActivity.this, MyConstant.NOTIFI_CLEAN, true);
                    if (!PreData.getDB(SetActivity.this, MyConstant.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(SetActivity.this)) {
                        //通知栏动画
                        Intent intent6 = new Intent(SetActivity.this, NotifingAnimationActivity.class);
                        startActivity(intent6);
                    } else {
                        //通知栏
                        Intent intent6 = new Intent(SetActivity.this, MyNotifingActivity.class);
                        startActivity(intent6);
                    }
                    break;
                case R.id.setting_rotate:
                    SetAdUtil.track("设置页面", "好评", "", 1);
                    MUtilGp.rate(SetActivity.this);
                    break;
            }
        }
    };

    private void addAd() {
        nativeView = SetAdUtil.getNativeAdView(TAG_SETTING, R.layout.native_ad_3);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (MyUtils.isNotificationListenEnabled(this)) {
                PreData.putDB(this, MyConstant.KEY_NOTIFI, true);
                Intent intent = new Intent(this, MyNotifingActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, NotifingAnimationActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(MyConstant.SETTING_RESUIL);
        finish();
    }
}
