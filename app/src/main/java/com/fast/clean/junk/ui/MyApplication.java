package com.fast.clean.junk.ui;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.fast.clean.core.CleanManager;
import com.fast.clean.notification.MyNotificationMonitorService;
import com.fast.clean.mutil.PreData;
import com.fast.clean.mutil.Util;
import com.achtapps.kpa.DaemonClient;
import com.android.client.AndroidSdk;
import com.fast.clean.junk.aservice.MyFloatService;
import com.fast.module.charge.saver.Util.Constants;
import com.fast.module.charge.saver.Util.Utils;
import com.fast.module.charge.saver.service.BatteryService;
import com.squareup.leakcanary.LeakCanary;
import com.fast.clean.junk.R;
import com.fast.clean.junk.aservice.NotificationService;
import com.fast.clean.junk.util.Constant;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {

    private static final int SCAN_TIME_INTERVAL = 1000 * 60 * 5;


    private HandlerThread mThread;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DaemonClient mDaemonClient = new DaemonClient(base, null);
        mDaemonClient.onAttachBaseContext(base);
    }


    private ActivityManager am;

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
            intent.putExtra("from", "notification");
            startService(intent);
        }

        if (PreData.getDB(this, Constant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, MyFloatService.class);
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
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


        mThread = new HandlerThread("scan");
        mThread.start();

        if (PreData.getDB(this, Constant.FIRST_INSTALL, true)) {
            PreData.putDB(this, Constant.IS_ACTION_BAR, Util.checkDeviceHasNavigationBar(this));
            PreData.putDB(this, Constant.FIRST_INSTALL, false);
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}

