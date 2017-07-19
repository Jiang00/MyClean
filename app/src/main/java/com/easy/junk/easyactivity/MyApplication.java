package com.easy.junk.easyactivity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.android.client.AndroidSdk;
import com.easy.clean.core.CleanManager;
import com.easy.clean.cleannotification.MyServiceNotificationMonitor;
import com.easy.clean.easyutils.MyUtils;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easytools.MyConstant;
import com.easy.junk.easyservices.EasyNotificationService;
import com.easy.junk.easyservices.EasySuspensionBallService;
import com.easy.module.charge.saver.easyutils.BatteryConstants;
import com.easy.module.charge.saver.easyutils.Utils;
import com.easy.module.charge.saver.easyprotectservice.ServiceBattery;
import com.squareup.leakcanary.LeakCanary;
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
            Intent intent = new Intent(this, EasyNotificationService.class);
            intent.setAction("notification");
            startService(intent);
        }

        if (PreData.getDB(this, MyConstant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, EasySuspensionBallService.class);
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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        DaemonClient mDaemonClient = new DaemonClient(base, null);
//        mDaemonClient.onAttachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}

