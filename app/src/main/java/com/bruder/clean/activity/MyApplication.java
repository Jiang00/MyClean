package com.bruder.clean.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import com.bruder.clean.junk.R;
import com.bruder.clean.myservice.FloatingService;
import com.bruder.clean.myservice.NotificationingService;
import com.bruder.clean.util.Constant;
import com.brudering.kpa.DaemonClient;
import com.brudering.kpa.DaemonConfigurations;
import com.brudering.kpa.KeepLiveManager;
import com.brudering.kpa.PersistService;
import com.cleaner.heart.CleanManager;
import com.cleaner.notification.NotificationsMonitorService;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;
import com.my.bruder.charge.saver.Util.Constants;
import com.my.bruder.charge.saver.Util.Utils;
import com.my.bruder.charge.saver.service.BatteringService;

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
//        AndroidSdk.onCreate(this);
        //charging
        // 启动充电服务
        startService(new Intent(this, BatteringService.class));
//        Utils.writeData(this, Constants.CHARGE_ON_NOTIFICATION_SWITCH, false);//
//        Utils.writeData(this, Constants.CHARGE_STATE_NOTIFICATION_SWITCH, false);//
        Utils.writeData(this, Constants.CHARGE_SAVER_TITLE, getString(R.string.app_name));
        Utils.writeData(this, Constants.CHARGE_SAVER_ICON, R.mipmap.loading_icon);

        CleanManager.getInstance(this).startLoad();//CleanManager.getInstance(this)得到CleanManager对象，查看加载内容

        if (DataPre.getDB(this, Constant.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, NotificationingService.class);
            intent.putExtra("from","notification");
            startService(intent);
        }

        if (DataPre.getDB(this, Constant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, FloatingService.class);
            startService(intent1);
        }

        //启动通知兰清理
        if (Util.isNotificationListenEnabled(this) && DataPre.getDB(this, Constant.KEY_NOTIFI, false)) {
            startService(new Intent(this, NotificationsMonitorService.class));
        }

        String name = Util.getProcessName(this);
        if (!TextUtils.equals(name, getPackageName())) {
            return;
        }
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


        mThread = new HandlerThread("scan");
        mThread.start();

        if (DataPre.getDB(this, Constant.FIRST_INSTALL, true)) {
            DataPre.putDB(this, Constant.IS_ACTION_BAR, Util.checkDeviceHasNavigationBar(this));
            DataPre.putDB(this, Constant.FIRST_INSTALL, false);
        }
        //保活
        startService(new Intent(this, PersistService.class));
        KeepLiveManager.startJobScheduler(this, 1000);

       /* if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /*   DaemonClient mDaemonClient = new DaemonClient(base, null);
        mDaemonClient.onAttachBaseContext(base);*/
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
