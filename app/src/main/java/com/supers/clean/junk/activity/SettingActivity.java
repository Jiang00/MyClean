package com.supers.clean.junk.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.supers.call.activity.CallActivity;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.service.AutoService;
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
    RelativeLayout setting_tongzhi, setting_tongzhilan, setting_cooling, setting_auto, setting_float, setting_battery, setting_detect, setting_unload, setting_power, setting_file,
            setting_picture, setting_wifi, setting_gboost, setting_hui, setting_notifi, setting_white, setting_short, setting_rotate;
    ImageView setting_tongzhi_check, setting_tongzhilan_check, setting_cooling_check, setting_auto_check, setting_float_check, setting_battery_check, setting_detect_check, setting_unload_check;
    LinearLayout ll_ad;
    ScrollView setting_scroll;
    FrameLayout fl_lot_setting;
    private View nativeView;

    private String TAG_SETTING = "eos_setting";
    private Handler myHandler;
    private LottieAnimationView lot_setting;
    private AlertDialog dialog;
    private AnimatorSet animatorSet_rotate;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        setting_tongzhi = (RelativeLayout) findViewById(R.id.setting_tongzhi);
        setting_tongzhilan = (RelativeLayout) findViewById(R.id.setting_tongzhilan);
        setting_cooling = (RelativeLayout) findViewById(R.id.setting_cooling);
        setting_float = (RelativeLayout) findViewById(R.id.setting_float);
        setting_auto = (RelativeLayout) findViewById(R.id.setting_auto);
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
        setting_cooling_check = (ImageView) findViewById(R.id.setting_cooling_check);
        setting_float_check = (ImageView) findViewById(R.id.setting_float_check);
        setting_auto_check = (ImageView) findViewById(R.id.setting_auto_check);
        setting_battery_check = (ImageView) findViewById(R.id.setting_battery_check);
        setting_detect_check = (ImageView) findViewById(R.id.setting_detect_check);
        setting_unload_check = (ImageView) findViewById(R.id.setting_unload_check);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        setting_scroll = (ScrollView) findViewById(R.id.setting_scroll);
        fl_lot_setting = (FrameLayout) findViewById(R.id.fl_lot_setting);
        findViewById(R.id.setting_privacy).setOnClickListener(onClickListener);
        findViewById(R.id.setting_call).setOnClickListener(onClickListener);
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
            if (!PreData.getDB(this, Constant.BILL_YOUXIAO, true)) {
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AndroidSdk.showFullAd(AdUtil.DEFAULT);
                    }
                }, 1000);
                tuiGuang();
            }
        } else {
            addAd();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setting_notifi.setVisibility(View.GONE);
        }
        Log.e("lange", "===2");
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
        nativeView = AdUtil.getNativeAdView(this, TAG_SETTING, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
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
        if (PreData.getDB(SettingActivity.this, Constant.TAN_COOLING, true)) {
            setting_cooling_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_cooling_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Constant.FlOAT_SWITCH, true)) {
            setting_float_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_float_check.setImageResource(R.mipmap.side_check_normal);
        }
        if (PreData.getDB(SettingActivity.this, Constant.AUTO_KAIGUAN, false)) {
            setting_auto_check.setImageResource(R.mipmap.side_check_passed);
        } else {
            setting_auto_check.setImageResource(R.mipmap.side_check_normal);
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
        setting_cooling.setOnClickListener(onClickListener);
        setting_float.setOnClickListener(onClickListener);
        setting_auto.setOnClickListener(onClickListener);
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
                        intent.putExtra("from", "notification");
                        startService(intent);
                    }
                    break;
                case R.id.setting_cooling:
                    AdUtil.track("设置页面", "点击通知栏开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Constant.TAN_COOLING, true)) {
                        PreData.putDB(SettingActivity.this, Constant.TAN_COOLING, false);
                        setting_cooling_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        PreData.putDB(SettingActivity.this, Constant.TAN_COOLING, true);
                        setting_cooling_check.setImageResource(R.mipmap.side_check_passed);
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
                case R.id.setting_auto:
                    AdUtil.track("设置页面", "点击自动清理开关", "", 1);
                    if (PreData.getDB(SettingActivity.this, Constant.AUTO_KAIGUAN, false)) {
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
                        setting_detect_check.setImageResource(R.mipmap.side_check_normal);
                    } else {
                        setting_detect_check.setImageResource(R.mipmap.side_check_passed);
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
                    if (!Util.isNotificationListenEnabled(SettingActivity.this) || !PreData.getDB(SettingActivity.this, Constant.KEY_NOTIFI, true)) {
                        Intent intent6 = new Intent(SettingActivity.this, NotifiInfoActivity.class);
                        startActivity(intent6);
                    } else {
                        Intent intent6 = new Intent(SettingActivity.this, NotifiActivity.class);
                        startActivity(intent6);
                    }
                    break;
                case R.id.setting_rotate:
                    AdUtil.track("设置页面", "好评", "", 1);

                    showExitDialog();
                    break;
                case R.id.setting_privacy:
                    AdUtil.track("设置页面", "隐私清理", "", 1);
                    Intent privacyIntent = new Intent(SettingActivity.this, PrivacyActivity.class);
                    startActivity(privacyIntent);
                    break;
                case R.id.setting_call:
                    AdUtil.track("设置页面", "骚扰拦截", "", 1);
                    Intent callIntent = new Intent(SettingActivity.this, CallActivity.class);
                    startActivity(callIntent);
                    break;
                case R.id.setting_language:
                    AdUtil.track("设置页面", "语言", "", 1);
                    Intent lagIntent = new Intent(SettingActivity.this, LanguageSettingActivity.class);
                    startActivityForResult(lagIntent, Constant.LANGUAGE_RESUIL);

                    break;
                default:
                    break;
            }
        }
    };

    private void showExitDialog() {
        View view = View.inflate(this, R.layout.dialog_rotate, null);
        ImageView rotate_delete = (ImageView) view.findViewById(R.id.rotate_delete);
        ImageView rotate_ic = (ImageView) view.findViewById(R.id.rotate_ic);
        Button main_rotate_good = (Button) view.findViewById(R.id.main_rotate_good);
        animatorSet_rotate = new AnimatorSet();
        ObjectAnimator objectAnimator_0 = ObjectAnimator.ofFloat(rotate_ic, View.ALPHA, 1, 0);
        objectAnimator_0.setDuration(800);
        ObjectAnimator objectAnimator_1 = ObjectAnimator.ofFloat(rotate_ic, View.SCALE_X, 0, 1);
        objectAnimator_1.setDuration(700);
        ObjectAnimator objectAnimator_2 = ObjectAnimator.ofFloat(rotate_ic, View.SCALE_Y, 0, 1);
        objectAnimator_2.setDuration(700);
        ObjectAnimator objectAnimator_3 = ObjectAnimator.ofFloat(rotate_ic, View.ALPHA, 0, 1);
        objectAnimator_3.setDuration(700);
        animatorSet_rotate.play(objectAnimator_1).with(objectAnimator_2).with(objectAnimator_3).after(objectAnimator_0);
        myHandler.postDelayed(runnable_rotate, 1000);
        main_rotate_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UtilGp.rate(SettingActivity.this);
            }
        });
        rotate_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog = new AlertDialog.Builder(this, R.style.exit_dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (myHandler != null) {
                    myHandler.removeCallbacks(runnable_rotate);
                    animatorSet_rotate.cancel();
                }
            }
        });
    }

    Runnable runnable_rotate = new Runnable() {
        @Override
        public void run() {
            if (animatorSet_rotate != null) {
                animatorSet_rotate.start();
                myHandler.postDelayed(this, 3000);
            }
        }
    };
    boolean languahe;

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
        } else if (requestCode == Constant.LANGUAGE_RESUIL) {
            recreate();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        languahe = true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        languahe = true;

    }

    @Override
    public void onBackPressed() {
        if (languahe) {
            setResult(Constant.SETTING_RESUIL);
            finish();
        } else {
            finish();
        }

    }
}
