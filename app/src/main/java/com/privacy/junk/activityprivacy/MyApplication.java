package com.privacy.junk.activityprivacy;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.android.client.AndroidSdk;
import com.privacy.clean.core.CleanManager;
import com.privacy.clean.cleannotification.MyServiceNotificationMonitor;
import com.privacy.clean.utilsprivacy.MyUtils;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.junk.R;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.privacyservices.PrivacyNotificationService;
import com.privacy.junk.privacyservices.PrivacySuspensionBallService;
import com.privacy.module.charge.saver.privacyprotectservice.PrivacyServiceBattery;
import com.privacy.module.charge.saver.privacyutils.BatteryConstantsPrivacy;
import com.privacy.module.charge.saver.privacyutils.UtilsPrivacy;
import com.squareup.leakcanary.LeakCanary;
//import com.vatermobi.kpa.DaemonClient;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {

    private static final int SCAN_TIME_INTERVAL = 1000 * 60 * 5;
    private HandlerThread mThread;
    private ActivityManager am;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidSdk.onCreate(this);
        //charging
        startService(new Intent(this, PrivacyServiceBattery.class));
        UtilsPrivacy.writeData(this, BatteryConstantsPrivacy.CHARGE_SAVER_TITLE, getString(R.string.app_name));
        UtilsPrivacy.writeData(this, BatteryConstantsPrivacy.CHARGE_SAVER_ICON, R.mipmap.loading_icon_1);

        CleanManager.getInstance(this).startLoad();

        if (PreData.getDB(this, MyConstantPrivacy.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, PrivacyNotificationService.class);
            intent.setAction("notification");
            startService(intent);
        }

        if (PreData.getDB(this, MyConstantPrivacy.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, PrivacySuspensionBallService.class);
            startService(intent1);
        }

        //启动通知兰清理
        if (MyUtils.isNotificationListenEnabled(this) && PreData.getDB(this, MyConstantPrivacy.KEY_NOTIFI, false)) {
            startService(new Intent(this, MyServiceNotificationMonitor.class));
        }

        String name = MyUtils.getProcessName(this);
        if (!TextUtils.equals(name, getPackageName())) {
            return;
        }
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);


        mThread = new HandlerThread("scan");
        mThread.start();

        if (PreData.getDB(this, MyConstantPrivacy.FIRST_INSTALL, true)) {
            PreData.putDB(this, MyConstantPrivacy.IS_ACTION_BAR, MyUtils.checkDeviceHasNavigationBar(this));
            PreData.putDB(this, MyConstantPrivacy.FIRST_INSTALL, false);
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

