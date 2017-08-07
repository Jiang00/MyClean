package com.myboost.junk.boostactivity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.android.client.AndroidSdk;
import com.myboost.clean.core.CleanManager;
import com.myboost.clean.cleannotification.MyServiceNotificationMonitor;
import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.servicesboost.NotificationServiceBoost;
import com.myboost.junk.servicesboost.BoostSuspensionBallService;
import com.myboost.module.charge.saver.boostprotectservice.ServiceBatteryBoost;
import com.myboost.module.charge.saver.boostutils.BoostBatteryConstants;
import com.myboost.module.charge.saver.boostutils.BatteryUtils;
import com.squareup.leakcanary.LeakCanary;
//import com.vatermobi.kpa.DaemonClient;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {

    private static final int SCAN_TIME_INTERVAL = 1000 * 60 * 5;
    private ActivityManager am;
    private HandlerThread mThread;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidSdk.onCreate(this);
        //charging
        startService(new Intent(this, ServiceBatteryBoost.class));
        BatteryUtils.writeData(this, BoostBatteryConstants.CHARGE_SAVER_TITLE, getString(R.string.app_name));
        BatteryUtils.writeData(this, BoostBatteryConstants.CHARGE_SAVER_ICON, R.mipmap.loading_icon_1);

        CleanManager.getInstance(this).startLoad();

        if (PreData.getDB(this, BoostMyConstant.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, NotificationServiceBoost.class);
            intent.setAction("notification");
            startService(intent);
        }

        if (PreData.getDB(this, BoostMyConstant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, BoostSuspensionBallService.class);
            startService(intent1);
        }

        //启动通知兰清理
        if (MyUtils.isNotificationListenEnabled(this) && PreData.getDB(this, BoostMyConstant.KEY_NOTIFI, false)) {
            startService(new Intent(this, MyServiceNotificationMonitor.class));
        }

        String name = MyUtils.getProcessName(this);
        if (!TextUtils.equals(name, getPackageName())) {
            return;
        }
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


        mThread = new HandlerThread("scan");
        mThread.start();

        if (PreData.getDB(this, BoostMyConstant.FIRST_INSTALL, true)) {
            PreData.putDB(this, BoostMyConstant.IS_ACTION_BAR, MyUtils.checkDeviceHasNavigationBar(this));
            PreData.putDB(this, BoostMyConstant.FIRST_INSTALL, false);
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        DaemonClient mDaemonClient = new DaemonClient(base, null);
//        mDaemonClient.onAttachBaseContext(base);
    }

}

