package com.mutter.clean.junk.myActivity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.android.client.AndroidSdk;
import com.mutter.clean.core.CleanManager;
import com.mutter.clean.notifi.NotificationMonitorService;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.service.FloatService;
import com.mutter.clean.junk.service.NotificationService;
import com.mutter.clean.junk.util.Constant;
import com.mutter.kpa.DaemonClient;
import com.mutter.module.charge.saver.Util.Constants;
import com.mutter.module.charge.saver.Util.Utils;
import com.mutter.module.charge.saver.service.BatteryService;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {


    private ActivityManager am;

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
            Intent intent1 = new Intent(this, FloatService.class);
            startService(intent1);
        }

        //启动通知兰清理
        if (Util.isNotificationListenEnabled(this) && PreData.getDB(this, Constant.KEY_NOTIFI, false)) {
            startService(new Intent(this, NotificationMonitorService.class));
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
