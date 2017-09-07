package com.vater.clean.junk.jiemian;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.vater.clean.core.CleanManager;
import com.vater.clean.junk.fuwu.XuanfuService;
import com.vater.clean.notification.NotificationMonitorService;
import com.vater.clean.util.PreData;
import com.vater.clean.util.Util;
import com.vatermobi.kpa.DaemonClient;
import com.android.client.AndroidSdk;
import com.vater.module.charge.saver.Util.Constants;
import com.vater.module.charge.saver.Util.Utils;
import com.vater.module.charge.saver.service.BatteryService;
import com.squareup.leakcanary.LeakCanary;
import com.vater.clean.junk.R;
import com.vater.clean.junk.fuwu.NotificationService;
import com.vater.clean.junk.gongju.Constant;

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
        //charging
        startService(new Intent(this, BatteryService.class));
        Utils.writeData(this, Constants.CHARGE_SAVER_TITLE, getString(R.string.app_name));
        Utils.writeData(this, Constants.CHARGE_SAVER_ICON, R.mipmap.loading_icon_1);

        CleanManager.getInstance(this).startLoad();

        if (PreData.getDB(this, Constant.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra("from","notification");
            startService(intent);
        }

        if (PreData.getDB(this, Constant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, XuanfuService.class);
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
            return;
        }
        LeakCanary.install(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}

