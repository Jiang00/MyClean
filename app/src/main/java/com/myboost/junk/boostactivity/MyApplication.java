package com.myboost.junk.boostactivity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.flashcleaning.kpa.DaemonClient;
import com.flashcleaning.kpa.DaemonConfigurations;
import com.flashcleaning.kpa.KeepLiveManager;
import com.flashcleaning.kpa.PersistService;
import com.myboost.clean.cleannotification.MyServiceNotificationMonitor;
import com.myboost.clean.core.CleanManager;
import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.servicesboost.BoostSuspensionBallService;
import com.myboost.junk.servicesboost.NotificationServiceBoost;
import com.myboost.module.charge.saver.boostprotectservice.ServiceBatteryBoost;
import com.myboost.module.charge.saver.boostutils.BatteryUtils;
import com.myboost.module.charge.saver.boostutils.BoostBatteryConstants;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {

    private static final int SCAN_TIME_INTERVAL = 1000 * 60 * 5;
    private ActivityManager am;
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        long startTime = System.currentTimeMillis();
//        AndroidSdk.onCreate(this);

//        charging
        startService(new Intent(this, ServiceBatteryBoost.class));
        BatteryUtils.writeData(this, BoostBatteryConstants.CHARGE_SAVER_TITLE, getString(R.string.app_name));
        BatteryUtils.writeData(this, BoostBatteryConstants.CHARGE_SAVER_ICON, R.mipmap.loading_icon_1);
        long endTime1 = System.currentTimeMillis();
        Log.e(TAG, "=====time1===" + (endTime1 - startTime));
        CleanManager.getInstance(this).startLoad();
        if (PreData.getDB(this, BoostMyConstant.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, NotificationServiceBoost.class);
            intent.putExtra("from","notification");//改了
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


        if (PreData.getDB(this, BoostMyConstant.FIRST_INSTALL, true)) {
            PreData.putDB(this, BoostMyConstant.IS_ACTION_BAR, MyUtils.checkDeviceHasNavigationBar(this));
            PreData.putDB(this, BoostMyConstant.FIRST_INSTALL, false);
        }
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
//        long endTime = System.currentTimeMillis();
//        Log.e(TAG, "=====time===" + (endTime - startTime));
        startService(new Intent(this, PersistService.class));
        KeepLiveManager.startJobScheduler(this, 1000);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
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

}

