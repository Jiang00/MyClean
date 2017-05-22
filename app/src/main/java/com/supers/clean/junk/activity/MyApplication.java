package com.supers.clean.junk.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.android.clean.core.CleanManager;
import com.android.clean.notification.NotificationMonitorService;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.eos.kpa.DaemonClient;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.module.charge.saver.service.BatteryService;
import com.squareup.leakcanary.LeakCanary;
import com.supers.clean.junk.R;
import com.android.clean.entity.JunkInfo;
import com.supers.clean.junk.service.FloatService;
import com.supers.clean.junk.service.NotificationService;
import com.supers.clean.junk.task.ApkFileAndAppJunkTask;
import com.supers.clean.junk.task.AppCacheTask;
import com.supers.clean.junk.task.AppManagerTask;
import com.supers.clean.junk.task.FilesOfUninstalledAppTask;
import com.supers.clean.junk.task.RamTask;
import com.supers.clean.junk.task.SimpleTask;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.TopActivityPkg;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {

    private static final int SCAN_TIME_INTERVAL = 1000 * 60 * 5;


    private int THREAD_POOL_COUNT = 3;

    private ArrayList<JunkInfo>  filesOfUnintallApk, apkFiles, appJunk, appCache, appRam, listMng;

    private ArrayList<JunkInfo> white_ram;


    private long  apkSize, unloadSize, logSize, dataSize, ramSize;

    private ActivityManager am;

    private HandlerThread mThread;
    private Handler threadHandler;

    private Runnable runnable;

    private SimpleTask appManagerTask, ramTask, appCacheTask, filesOfUninstalledAppTask, cacheTask;

    private ApkFileAndAppJunkTask fileTask;

    private ExecutorService mThreadPool;





    public void removeRam(JunkInfo fileListInfo) {
        am.killBackgroundProcesses(fileListInfo.pkg);
        if (fileListInfo.isSelfBoot) {
            return;
        }
        ramSize -= fileListInfo.size;
        if (appRam != null) {
            appRam.remove(fileListInfo);
        }
    }

    public void removeRamStartSelf(JunkInfo fileListInfo) {
        am.killBackgroundProcesses(fileListInfo.pkg);
        ramSize -= fileListInfo.size;
        if (appRam != null) {
            appRam.remove(fileListInfo);
        }
    }

    public void clearRam() {
        if (appRam != null) {
            appRam.clear();
            ramSize = 0;
        }
    }


    public void removeAppCache(JunkInfo fileListInfo) {
        Util.deleteFile(fileListInfo.path);
        dataSize -= fileListInfo.size;
        if (appCache != null) {
            appCache.remove(fileListInfo);
        }
    }

    public void removeAppJunk(JunkInfo fileListInfo) {
        Util.deleteFile(fileListInfo.path);
        logSize -= fileListInfo.size;
        if (appJunk != null) {
            appJunk.remove(fileListInfo);
        }
    }

    public void removeFilesOfUnintalledApk(JunkInfo fileListInfo) {
        Util.deleteFile(fileListInfo.path);
        unloadSize -= fileListInfo.size;
        if (filesOfUnintallApk != null) {
            filesOfUnintallApk.remove(fileListInfo);
        }
    }

    public void removeApkFiles(JunkInfo fileListInfo) {
        Util.deleteFile(fileListInfo.path);
        apkSize -= fileListInfo.size;
        if (apkFiles != null) {
            apkFiles.remove(fileListInfo);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DaemonClient mDaemonClient = new DaemonClient(base, null);
        mDaemonClient.onAttachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

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
        runnable = getScanIntervalRunnable();

        initLists();

        mThreadPool = Executors.newFixedThreadPool(THREAD_POOL_COUNT);

        mThread = new HandlerThread("scan");
        mThread.start();
        threadHandler = new Handler(mThread.getLooper());
        threadHandler.postDelayed(runnable, SCAN_TIME_INTERVAL);
        asyncInitData();

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


    private Runnable getScanIntervalRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                String pkg = TopActivityPkg.getTopPackageName(MyApplication.this);
                if (!TextUtils.equals(MyApplication.this.getPackageName(), pkg)) {
                    asyncInitData();
                }
                threadHandler.postDelayed(runnable, SCAN_TIME_INTERVAL);
            }
        };
    }


    @Override
    public void onTerminate() {
        threadHandler.removeCallbacks(runnable);
        super.onTerminate();
    }

    private void asyncInitData() {
        resetLists();

        resetSizes();

    }

    private void initLists() {
        apkFiles = new ArrayList<>();
        filesOfUnintallApk = new ArrayList<>();
        appJunk = new ArrayList<>();
        appCache = new ArrayList<>();
        appRam = new ArrayList<>();
        white_ram = new ArrayList<>();
        listMng = new ArrayList<>();
    }

    private void resetLists() {
        apkFiles.clear();
        filesOfUnintallApk.clear();
        appJunk.clear();
        appCache.clear();
        appRam.clear();
        white_ram.clear();
        listMng.clear();
    }

    private void resetSizes() {
        apkSize = 0;
        unloadSize = 0;
        logSize = 0;
        dataSize = 0;
        ramSize = 0;
    }


}

