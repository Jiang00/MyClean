package com.eifmobi.clean.junk.myActivity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import com.android.client.AdListener;
import com.android.client.AndroidSdk;
import com.eifmobi.kpa.DaemonClient;
import com.eifmobi.kpa.KeepLiveManager;
import com.eifmobi.kpa.PersistService;
import com.eifmobi.clean.core.CleanManager;
import com.eifmobi.clean.junk.util.BadgerCount;
import com.eifmobi.clean.notifi.NotificationMonitorService;
import com.eifmobi.clean.util.PreData;
import com.eifmobi.clean.util.Util;
import com.eifmobi.clean.junk.R;
import com.eifmobi.clean.junk.service.FloatService;
import com.eifmobi.clean.junk.service.NotificationService;
import com.eifmobi.clean.junk.util.Constant;
import com.eifmobi.charge.saver.Util.Constants;
import com.eifmobi.charge.saver.Util.Utils;
import com.eifmobi.charge.saver.service.BatteryService;
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
        if (PreData.getDB(this, Constant.FULL_START, 0) == 1) {
            AndroidSdk.loadFullAd("loading_full", new AdListener() {
                @Override
                public void onAdLoadSuccess() {
                    super.onAdLoadSuccess();
                    Log.e("adadad", "onAdLoadSuccess");
                }

                @Override
                public void onAdLoadFails() {
                    super.onAdLoadFails();
                    Log.e("adadad", "onAdLoadFails");
                }

                @Override
                public void onAdShow() {
                    super.onAdShow();
                    Log.e("adadad", "onAdShow");
                }

                @Override
                public void onAdShowFails() {
                    super.onAdShowFails();
                    Log.e("adadad", "onAdShowFails");
                }
            });
        }
        startService(new Intent(this, PersistService.class));
        KeepLiveManager.startJobScheduler(this, 1000);

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


        if (PreData.getDB(this, Constant.FlOAT_SWITCH, true)) {
            Intent intent1 = new Intent(this, FloatService.class);
            startService(intent1);
        }
        BadgerCount.setCount(this);
        //启动通知兰清理
        if (Util.isNotificationListenEnabled(this) && PreData.getDB(this, Constant.KEY_NOTIFI, false)) {
            startService(new Intent(this, NotificationMonitorService.class));
        }

        String name = Util.getProcessName(this);
        if (!TextUtils.equals(name, getPackageName())) {
            return;
        }
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        Log.e("auto", PreData.getDB(this, Constant.AUTO_KAIGUAN) + "==" + PreData.hasDB(this, Constant.AUTO_KAIGUAN));
        mThread = new HandlerThread("scan");
        mThread.start();

        if (PreData.getDB(this, Constant.FIRST_INSTALL, true)) {
            PreData.putDB(this, Constant.IS_ACTION_BAR, Util.checkDeviceHasNavigationBar(this));
            PreData.putDB(this, Constant.FIRST_INSTALL, false);
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//                PreData.putDB(this, Constant.TONGZHILAN_SWITCH, false);
//            }
        }
        if (PreData.getDB(this, Constant.TONGZHILAN_SWITCH, true)) {
            Intent intent = new Intent(this, NotificationService.class);
            intent.putExtra("from", "notification");
            startService(intent);
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
