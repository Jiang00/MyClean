package com.supers.clean.junk.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.eos.manager.App;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.module.charge.saver.service.BatteryService;
import com.ivy.kpa.DaemonClient;
import com.squareup.leakcanary.LeakCanary;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.service.FloatService;
import com.supers.clean.junk.service.NotificationService;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.PreData;
import com.supers.clean.junk.util.TopActivityPkg;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.task.ApkFileAndAppJunkTask;
import com.supers.clean.junk.task.AppCacheTask;
import com.supers.clean.junk.task.AppManagerTask;
import com.supers.clean.junk.task.FilesOfUninstalledAppTask;
import com.supers.clean.junk.task.RamTask;
import com.supers.clean.junk.task.SimpleTask;
import com.supers.clean.junk.task.SystemCacheTask;
import com.supers.clean.junk.service.ReStarService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by on 2016/11/29.
 */
public class MyApplication extends App {

    private static final int SCAN_TIME_INTERVAL = 1000 * 60 * 5;

    private final static int CWJ_HEAP_SIZE = 6 * 1024 * 1024;

    private ArrayList<JunkInfo> systemCache, filesOfUnintalledApk, apkFiles, appJunk, appCache, appRam, listMng;

    private ArrayList<JunkInfo> white_ram;

    private long cacheSize, apkSize, unloadSize, logSize, dataSize, ramSize, appMngSize;

    private boolean saomiaoSuccess;

    private ActivityManager am;

    private int count;

    private Handler myHandler;
    private Runnable runnable;

    public boolean isSaomiaoSuccess() {
        return saomiaoSuccess;
    }

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

    public long getAppMngSize() {
        return appMngSize;
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

    public ArrayList<JunkInfo> getFilesOfUnintalledApk() {
        return filesOfUnintalledApk;
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
        appMngSize -= f.size;
        if (listMng != null) {
            listMng.remove(f);
        }
    }

    public void removeRam(JunkInfo fileListInfo) {
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
        if (filesOfUnintalledApk != null) {
            filesOfUnintalledApk.remove(fileListInfo);
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

        setMinHeapSize(CWJ_HEAP_SIZE);

        ReStarService.start(this);
        Intent serviceIntent = new Intent(this, ReStarService.class);
        startService(serviceIntent);
        //charging
        startService(new Intent(this, BatteryService.class));
//        Utils.writeData(this, Constants.CHARGE_ON_NOTIFICATION_SWITCH, false);//
//        Utils.writeData(this, Constants.CHARGE_STATE_NOTIFICATION_SWITCH, false);//
        Utils.writeData(this, Constants.CHARGE_SAVER_TITLE, getString(R.string.app_name));
        Utils.writeData(this, Constants.CHARGE_SAVER_ICON, R.mipmap.loading_icon);

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

        myHandler = new Handler();
        myHandler.postDelayed(runnable, SCAN_TIME_INTERVAL);
        asyncInitData();
        saomiaoSuccess = false;
        saomiaoSuccess = true;

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

    public static void setMinHeapSize(long size) {
        try {
            Class<?> cls = Class.forName("dalvik.system.VMRuntime");
            Method getRuntime = cls.getMethod("getRuntime");
            Object obj = getRuntime.invoke(null);// obj就是Runtime
            if (obj == null) {
                System.err.println("obj is null");
            } else {
                System.out.println(obj.getClass().getName());
                Class<?> runtimeClass = obj.getClass();
                Method setMinimumHeapSize = runtimeClass.getMethod(
                        "setMinimumHeapSize", long.class);

                setMinimumHeapSize.invoke(obj, size);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Runnable getScanIntervalRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                String pkg = TopActivityPkg.getTopPackageName(MyApplication.this);
                if (!TextUtils.equals(MyApplication.this.getPackageName(), pkg)) {
                    asyncInitData();
                    saomiaoSuccess = false;
                }
//                if (launcherPkg.contains(pkg)) {
//                    asyncInitData();
//                    saomiaoSuccess = false;
//                } else {
//                    if (MainActivity.instance == null) {
//
//                    }
//                }
                myHandler.postDelayed(runnable, SCAN_TIME_INTERVAL);
            }
        };
    }


    @Override
    public void onTerminate() {
        myHandler.removeCallbacks(runnable);
        super.onTerminate();
    }

    private void asyncInitData() {
        resetLists();

        resetSizes();

        loadSystemCache();

        loadFilesOfUninstallApk();

        loadApkFileAndAppJunk();

        loadAppCache();

        loadAppRam();

        loadWhiteListAndAppManager();

    }

    private void initLists() {
        systemCache = new ArrayList<>();
        apkFiles = new ArrayList<>();
        filesOfUnintalledApk = new ArrayList<>();
        appJunk = new ArrayList<>();
        appCache = new ArrayList<>();
        appRam = new ArrayList<>();
        white_ram = new ArrayList<>();
        listMng = new ArrayList<>();
    }

    private void resetLists() {
        systemCache.clear();
        apkFiles.clear();
        filesOfUnintalledApk.clear();
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
        appMngSize = 0;
        count = 0;
    }

    private void loadWhiteListAndAppManager() {
        AppManagerTask appManagerTask = new AppManagerTask(this, new SimpleTask.SimpleTaskListener() {
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
                appMngSize = dataSize;
                listMng = dataList;
                count++;
                saoMiaoOver();
            }
        });
        appManagerTask.start();
    }

    private void loadAppRam() {
        RamTask ramTask = new RamTask(this, new SimpleTask.SimpleTaskListener() {
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
                count++;
                saoMiaoOver();
            }
        });
        ramTask.start();
    }

    public void loadAppCache() {
        AppCacheTask appCacheTask = new AppCacheTask(this, new AppCacheTask.SimpleTaskListener() {
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
                count++;
                saoMiaoOver();
            }
        });
        appCacheTask.start();
    }

    public void loadApkFileAndAppJunk() {
        ApkFileAndAppJunkTask fileTask = new ApkFileAndAppJunkTask(this,
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
                        count += 2;
                        saoMiaoOver();
                    }
                });
        fileTask.start();
    }

    public void loadSystemCache() {
        SystemCacheTask cacheTask = new SystemCacheTask(this, new SimpleTask.SimpleTaskListener() {
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
                count++;
                saoMiaoOver();
            }
        });
        cacheTask.start();
    }

    public void loadFilesOfUninstallApk() {
        FilesOfUninstalledAppTask task = new FilesOfUninstalledAppTask(this, new SimpleTask.SimpleTaskListener() {
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
                filesOfUnintalledApk = dataList;
                count++;
                saoMiaoOver();
            }
        });
        task.start();
    }

    public void saoMiaoOver() {
        Log.e("aaa", "=====" + count);
        if (count >= 7) {
            saomiaoSuccess = true;
        }
    }


}

