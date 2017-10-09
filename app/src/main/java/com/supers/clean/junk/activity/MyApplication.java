package com.supers.clean.junk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.android.clean.core.CleanManager;
import com.android.clean.notification.NotificationMonitorService;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.eos.kpa.DaemonClient;
import com.eos.manager.App;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.module.charge.saver.service.BatteryService;
import com.fraumobi.call.service.PhoneService;
import com.supers.clean.junk.R;
import com.supers.clean.junk.service.AutoService;
import com.supers.clean.junk.service.FloatService;
import com.supers.clean.junk.service.NotificationService;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.BadgerCount;

import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends App {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DaemonClient mDaemonClient = new DaemonClient(base, null);
        mDaemonClient.onAttachBaseContext(base);
    }

    private void changeAppLanguage(String language) {
        // 本地语言设置
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();
        if (TextUtils.equals(language, "cn")) {
            conf.locale = new Locale("zh", "CN");
        } else if (TextUtils.equals(language, "in")) {
            conf.locale = new Locale("in", "ID");
        } else {
            Locale myLocale = new Locale(language);
            conf.locale = myLocale;
        }
        res.updateConfiguration(conf, dm);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String language = PreData.getDB(this, BaseActivity.DEFAULT_SYSTEM_LANGUAGE, BaseActivity.DEFAULT_SYSTEM_LANGUAGE);

        if (!TextUtils.equals(language, BaseActivity.DEFAULT_SYSTEM_LANGUAGE)) {
            changeAppLanguage(language);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String language = PreData.getDB(this, BaseActivity.DEFAULT_SYSTEM_LANGUAGE, BaseActivity.DEFAULT_SYSTEM_LANGUAGE);

        if (!TextUtils.equals(language, BaseActivity.DEFAULT_SYSTEM_LANGUAGE)) {
            changeAppLanguage(language);
        }
        ShortcutBadger.applyCount(this, 0);
        AndroidSdk.onCreate(this);
       /* ReStarService.start(this);
        Intent serviceIntent = new Intent(this, ReStarService.class);
        startService(serviceIntent);*/
        //charging
        startService(new Intent(this, BatteryService.class));
//        Utils.writeData(this, Constants.CHARGE_ON_NOTIFICATION_SWITCH, false);//
//        Utils.writeData(this, Constants.CHARGE_STATE_NOTIFICATION_SWITCH, false);//
        Utils.writeData(this, Constants.CHARGE_SAVER_TITLE, getString(R.string.app_name));
        Utils.writeData(this, Constants.CHARGE_SAVER_ICON, R.mipmap.loading_icon);

        CleanManager.getInstance(this).startLoad();
//
        startService(new Intent(this, PhoneService.class));

        if (PreData.getDB(this, Constant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, FloatService.class);
            startService(intent1);
        }
        if (PreData.getDB(this, Constant.AUTO_KAIGUAN, false) || !PreData.hasDB(this, Constant.AUTO_KAIGUAN)) {
            Intent intent = new Intent(this, AutoService.class);
            startService(intent);
        }
        BadgerCount.setCount(this);
        //启动通知兰清理
        if (Util.isNotificationListenEnabled(this) && PreData.getDB(this, Constant.KEY_NOTIFI, false)) {
            startService(new Intent(this, NotificationMonitorService.class));
        }

        String name = Util.getProcessName(this);
        if (!TextUtils.equals(name, getPackageName())) {
            return;
        }

        if (PreData.getDB(this, Constant.FIRST_INSTALL, true)) {
            PreData.putDB(this, Constant.IS_ACTION_BAR, Util.checkDeviceHasNavigationBar(this));
            PreData.putDB(this, Constant.FIRST_INSTALL, false);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                PreData.putDB(this, Constant.TONGZHILAN_SWITCH, false);
            }
        }
        if (PreData.getDB(this, Constant.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra("from", "notification");
            startService(intent);
        }

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}

