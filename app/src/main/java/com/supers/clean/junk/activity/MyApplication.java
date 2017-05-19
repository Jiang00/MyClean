package com.supers.clean.junk.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.android.clean.core.CleanManager;
import com.eos.kpa.DaemonClient;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.module.charge.saver.service.BatteryService;
import com.squareup.leakcanary.LeakCanary;
import com.supers.clean.junk.R;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.entity.NotifiInfo;
import com.supers.clean.junk.service.FloatService;
import com.supers.clean.junk.service.NotificationService;
import com.supers.clean.junk.task.ApkFileAndAppJunkTask;
import com.supers.clean.junk.task.AppCacheTask;
import com.supers.clean.junk.task.AppManagerTask;
import com.supers.clean.junk.task.FilesOfUninstalledAppTask;
import com.supers.clean.junk.task.RamTask;
import com.supers.clean.junk.task.SimpleTask;
import com.supers.clean.junk.task.SystemCacheTask;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;
import com.supers.clean.junk.util.TopActivityPkg;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends Application {

    private static final int SCAN_TIME_INTERVAL = 1000 * 60 * 5;


    private int THREAD_POOL_COUNT = 3;

    private ArrayList<JunkInfo> systemCache, filesOfUnintallApk, apkFiles, appJunk, appCache, appRam, listMng;

    private ArrayList<JunkInfo> white_ram;

    private static ArrayList<NotifiInfo> notifiList;

    private long cacheSize, apkSize, unloadSize, logSize, dataSize, ramSize;

    private ActivityManager am;

    private HandlerThread mThread;
    private Handler threadHandler;

    private Runnable runnable;

    private SimpleTask appManagerTask, ramTask, appCacheTask, filesOfUninstalledAppTask, cacheTask;

    private ApkFileAndAppJunkTask fileTask;

    private ExecutorService mThreadPool;

    public long getCacheSize() {
        return cacheSize;
    }

    public long getApkSize() {
        return apkSize;
    }

    public long getUnloadSize() {
        return unloadSize;
    }

    public long getLogSize() {
        return logSize;
    }

    public long getDataSize() {
        return dataSize;
    }

    public long getRamSize() {
        return ramSize;
    }

    public ArrayList<JunkInfo> getListMng() {
        return listMng;
    }

    public ArrayList<JunkInfo> getAppRam() {
        return appRam;
    }

    public ArrayList<JunkInfo> getWhiteRam() {
        return white_ram;
    }

    public ArrayList<JunkInfo> getAppJunk() {
        return appJunk;
    }

    public ArrayList<JunkInfo> getAppCache() {
        return appCache;
    }

    public ArrayList<JunkInfo> getSystemCache() {
        return systemCache;
    }

    public ArrayList<NotifiInfo> getNotifiList() {
        return notifiList;
    }

    public ArrayList<JunkInfo> getFilesOfUnintalledApk() {
        return filesOfUnintallApk;
    }

    public ArrayList<JunkInfo> getApkFiles() {
        return apkFiles;
    }

    public void clearSystemCache() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        try {
            Method mFreeStorageAndNotifyMethod = getPackageManager().getClass().getMethod(
                    "freeStorageAndNotify", long.class, IPackageDataObserver.class);
            mFreeStorageAndNotifyMethod.invoke(getPackageManager(),
                    (long) stat.getBlockCount() * (long) stat.getBlockSize(),
                    new IPackageDataObserver.Stub() {
                        @Override
                        public void onRemoveCompleted(String packageName, boolean succeeded)
                                throws RemoteException {
                            Log.e("rqy", "clearsystemCache");
                        }
                    }
            );
        } catch (Exception e) {
            Log.e("rqy", "clearsystemCache has exception=" + e.getMessage());
            e.printStackTrace();
        }
        if (systemCache != null) {
            systemCache.clear();
            cacheSize = 0;
        }
    }

    public void removeAppManager(JunkInfo f) {
        if (listMng != null) {
            listMng.remove(f);
        }
    }

    public void removeNotifi(NotifiInfo f) {
        if (notifiList != null) {
            notifiList.remove(f);
        }
    }

    public void removeRam(JunkInfo fileListInfo) {
        am.killBackgroundProcesses(fileListInfo.packageName);
        if (fileListInfo.isStartSelf) {
            return;
        }
        ramSize -= fileListInfo.size;
        if (appRam != null) {
            appRam.remove(fileListInfo);
        }
    }

    public void removeRamStartSelf(JunkInfo fileListInfo) {
        am.killBackgroundProcesses(fileListInfo.packageName);
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

    public void clearNotifi() {
        if (notifiList != null) {
            notifiList.clear();
        }
    }

    public void removeAppCache(JunkInfo fileListInfo) {
        CommonUtil.deleteFile(fileListInfo.path);
        dataSize -= fileListInfo.size;
        if (appCache != null) {
            appCache.remove(fileListInfo);
        }
    }

    public void removeAppJunk(JunkInfo fileListInfo) {
        CommonUtil.deleteFile(fileListInfo.path);
        logSize -= fileListInfo.size;
        if (appJunk != null) {
            appJunk.remove(fileListInfo);
        }
    }

    public void removeFilesOfUnintalledApk(JunkInfo fileListInfo) {
        CommonUtil.deleteFile(fileListInfo.path);
        unloadSize -= fileListInfo.size;
        if (filesOfUnintallApk != null) {
            filesOfUnintallApk.remove(fileListInfo);
        }
    }

    public void removeApkFiles(JunkInfo fileListInfo) {
        CommonUtil.deleteFile(fileListInfo.path);
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

        String name = CommonUtil.getProcessName(this);
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
            PreData.putDB(this, Constant.IS_ACTION_BAR, CommonUtil.checkDeviceHasNavigationBar(this));
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

        mThreadPool.execute(loadSystemCache());

        mThreadPool.execute(loadFilesOfUninstallApk());

        mThreadPool.execute(loadApkFileAndAppJunk());

        mThreadPool.execute(loadAppCache());

        mThreadPool.execute(loadAppRam());

        mThreadPool.execute(loadWhiteListAndAppManager());

    }

    private void initLists() {
        systemCache = new ArrayList<>();
        apkFiles = new ArrayList<>();
        filesOfUnintallApk = new ArrayList<>();
        appJunk = new ArrayList<>();
        appCache = new ArrayList<>();
        appRam = new ArrayList<>();
        white_ram = new ArrayList<>();
        listMng = new ArrayList<>();
        notifiList = new ArrayList<>();
    }

    private void resetLists() {
        systemCache.clear();
        apkFiles.clear();
        filesOfUnintallApk.clear();
        appJunk.clear();
        appCache.clear();
        appRam.clear();
        white_ram.clear();
        listMng.clear();
    }

    private void resetSizes() {
        cacheSize = 0;
        apkSize = 0;
        unloadSize = 0;
        logSize = 0;
        dataSize = 0;
        ramSize = 0;
    }

    private SimpleTask loadWhiteListAndAppManager() {
        if (appManagerTask == null) {
            appManagerTask = new AppManagerTask(this, new SimpleTask.SimpleTaskListener() {
                @Override
                public void startLoad() {

                }

                @Override
                public void loading(JunkInfo JunkInfo, long size) {

                }

                @Override
                public void loadingW(JunkInfo fileInfo) {

                }

                @Override
                public void cancelLoading() {

                }

                @Override
                public void finishLoading(long dataSize, ArrayList<JunkInfo> dataList) {
                    listMng = dataList;
                }
            });
        }
        return appManagerTask;
    }

    private SimpleTask loadAppRam() {
        if (ramTask == null) {
            ramTask = new RamTask(this, new SimpleTask.SimpleTaskListener() {
                @Override
                public void startLoad() {

                }

                @Override
                public void loading(JunkInfo JunkInfo, long size) {
                    ramSize += size;
                    appRam.add(JunkInfo);
                }

                @Override
                public void loadingW(JunkInfo fileInfo) {
                    white_ram.add(fileInfo);
                }

                @Override
                public void cancelLoading() {

                }

                @Override
                public void finishLoading(long dataSize, ArrayList<JunkInfo> dataList) {
                }
            });
        }
        return ramTask;
    }

    public SimpleTask loadAppCache() {
        if (appCacheTask == null) {
            appCacheTask = new AppCacheTask(this, new AppCacheTask.SimpleTaskListener() {
                @Override
                public void startLoad() {

                }

                @Override
                public void loading(JunkInfo JunkInfo, long size) {

                }

                @Override
                public void loadingW(JunkInfo fileInfo) {

                }

                @Override
                public void cancelLoading() {

                }

                @Override
                public void finishLoading(long dataSize, ArrayList<JunkInfo> dataList) {
                    MyApplication.this.dataSize = dataSize;
                    appCache = dataList;
                }
            });
        }
        return appCacheTask;
    }

    public ApkFileAndAppJunkTask loadApkFileAndAppJunk() {
        if (fileTask == null) {
            fileTask = new ApkFileAndAppJunkTask(this,
                    new ApkFileAndAppJunkTask.FileTaskListener() {
                        @Override
                        public void loadFinish(long apkSize, long logSize, ArrayList<JunkInfo> apkList, ArrayList<JunkInfo> logList) {
                            MyApplication.this.apkSize = apkSize;
                            MyApplication.this.logSize = logSize;
                            apkFiles = apkList;
                            int i = 0;
                            for (JunkInfo f : logList) {
                                if (++i <= 25) {
                                    appJunk.add(f);
                                }
                            }
                        }
                    });
        }
        return fileTask;

    }

    public SimpleTask loadSystemCache() {
        if (cacheTask == null) {
            cacheTask = new SystemCacheTask(this, new SimpleTask.SimpleTaskListener() {
                @Override
                public void startLoad() {

                }

                @Override
                public void loading(JunkInfo JunkInfo, long size) {
                    cacheSize += size;
                    systemCache.add(JunkInfo);
                }

                @Override
                public void loadingW(JunkInfo fileInfo) {

                }

                @Override
                public void cancelLoading() {

                }

                @Override
                public void finishLoading(long dataSize, ArrayList<JunkInfo> dataList) {
                    Log.e("rqy", "finishLoading--" + "end threadName=" + Thread.currentThread().getName() + "");
                }
            });
        }
        return cacheTask;
    }

    public SimpleTask loadFilesOfUninstallApk() {
        if (filesOfUninstalledAppTask == null) {
            filesOfUninstalledAppTask = new FilesOfUninstalledAppTask(this, new SimpleTask.SimpleTaskListener() {
                @Override
                public void startLoad() {

                }

                @Override
                public void loading(JunkInfo JunkInfo, long size) {

                }

                @Override
                public void loadingW(JunkInfo fileInfo) {

                }

                @Override
                public void cancelLoading() {

                }

                @Override
                public void finishLoading(long dataSize, ArrayList<JunkInfo> dataList) {
                    unloadSize = dataSize;
                    filesOfUnintallApk = dataList;
                }
            });
        }
        return filesOfUninstalledAppTask;
    }


}

