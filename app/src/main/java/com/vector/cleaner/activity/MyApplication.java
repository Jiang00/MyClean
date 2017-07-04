package com.vector.cleaner.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.vector.cleaner.aservice.MemoryService;
import com.vector.cleaner.aservice.NotificationService;
import com.vector.cleaner.utils.Constant;
import com.vector.mcleaner.manager.CleanManager;
import com.vector.mcleaner.notification.MyNotificationMonitorService;
import com.vector.mcleaner.mutil.PreData;
import com.vector.mcleaner.mutil.Util;
import com.vectorapps.kpa.DaemonClient;
import com.android.client.AndroidSdk;
import com.vector.module.Util.Constants;
import com.vector.module.Util.Utils;
import com.vector.module.service.BatteryService;
import com.squareup.leakcanary.LeakCanary;
import com.vector.cleaner.R;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {


    private HandlerThread mThread;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DaemonClient mDaemonClient = new DaemonClient(base, null);
        mDaemonClient.onAttachBaseContext(base);
    }


    @Override
    public void onCreate() {
        super.onCreate();
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

        if (PreData.getDB(this, Constant.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, NotificationService.class);
            intent.setAction("notification");
            startService(intent);
        }

        if (PreData.getDB(this, Constant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, MemoryService.class);
            startService(intent1);
        }

        //启动通知兰清理
        if (Util.isNotificationListenEnabled(this) && PreData.getDB(this, Constant.KEY_NOTIFI, false)) {
            startService(new Intent(this, MyNotificationMonitorService.class));
        }

        String name = Util.getProcessName(this);
        if (!TextUtils.equals(name, getPackageName())) {
            return;
        }

        mThread = new HandlerThread("scan");
        mThread.start();

        if (PreData.getDB(this, Constant.FIRST_INSTALL, true)) {
            PreData.putDB(this, Constant.IS_ACTION_BAR, Util.checkDeviceHasNavigationBar(this));
            PreData.putDB(this, Constant.FIRST_INSTALL, false);
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }


}

