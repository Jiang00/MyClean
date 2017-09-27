package com.icleaner.junk.icleaneractivity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import com.android.client.AndroidSdk;
import com.icleaner.clean.core.CleanManager;
import com.icleaner.clean.notification.MyServiceNotificationMonitor;
import com.icleaner.clean.utils.MyUtils;
import com.icleaner.clean.utils.PreData;
import com.icleaner.junk.R;
import com.icleaner.junk.mytools.MyConstant;
import com.icleaner.junk.services.MyNotificationService;
import com.icleaner.junk.services.SuspensionBallService;
import com.icleaner.module.charge.saver.Utils.BatteryConstants;
import com.icleaner.module.charge.saver.Utils.Utils;
import com.icleaner.module.charge.saver.protectservice.ServiceBattery;
import com.icleanering.kpa.DaemonClient;
import com.icleanering.kpa.DaemonConfigurations;
import com.icleanering.kpa.KeepLiveManager;
import com.icleanering.kpa.PersistService;
//import com.vatermobi.kpa.DaemonClient;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {

    private HandlerThread mThread;
    private static final int SCAN_TIME_INTERVAL = 1000 * 60 * 5;
    private ActivityManager am;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidSdk.onCreate(this);
        //charging
        startService(new Intent(this, ServiceBattery.class));
        Utils.writeData(this, BatteryConstants.CHARGE_SAVER_TITLE, getString(R.string.app_name));
        Utils.writeData(this, BatteryConstants.CHARGE_SAVER_ICON, R.mipmap.loading_icon_1);

        CleanManager.getInstance(this).startLoad();

        if (PreData.getDB(this, MyConstant.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, MyNotificationService.class);
            intent.putExtra("from", "notification");
            startService(intent);
        }

        if (PreData.getDB(this, MyConstant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, SuspensionBallService.class);
            startService(intent1);
        }

        //启动通知兰清理
        if (MyUtils.isNotificationListenEnabled(this) && PreData.getDB(this, MyConstant.KEY_NOTIFI, false)) {
            startService(new Intent(this, MyServiceNotificationMonitor.class));
        }

        String name = MyUtils.getProcessName(this);
        if (!TextUtils.equals(name, getPackageName())) {
            return;
        }
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


        mThread = new HandlerThread("scan");
        mThread.start();

        if (PreData.getDB(this, MyConstant.FIRST_INSTALL, true)) {
            PreData.putDB(this, MyConstant.IS_ACTION_BAR, MyUtils.checkDeviceHasNavigationBar(this));
            PreData.putDB(this, MyConstant.FIRST_INSTALL, false);
        }
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        startService(new Intent(this, PersistService.class));
        KeepLiveManager.startJobScheduler(this, 1000);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DaemonClient mDaemonClient = new DaemonClient(base, new DaemonConfigurations.DaemonListener() {
            @Override
            public void onPersistentStart(Context context) {
                Log.e("rqy", "onPersistentStart");
            }

            @Override
            public void onDaemonAssistantStart(Context context) {
                Log.e("rqy", "onDaemonAssistantStart");
            }

            @Override
            public void onWatchDaemonDead() {
                Log.e("rqy", "onWatchDaemonDead");
            }
        });

        mDaemonClient.onAttachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}

