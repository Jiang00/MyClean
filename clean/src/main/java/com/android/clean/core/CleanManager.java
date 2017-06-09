package com.android.clean.core;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.database.Cursor;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;

import com.android.clean.callback.AppCacheCallBack;
import com.android.clean.callback.AppRamCallBack;
import com.android.clean.callback.FileInfoCallBack;
import com.android.clean.callback.SystemCacheCallBack;
import com.android.clean.callback.UninstallResidualCallback;
import com.android.clean.entity.JunkInfo;
import com.android.clean.entity.Sizesort;
import com.android.clean.filemanager.FileCategoryHelper;
import com.android.clean.filemanager.FileInfo;
import com.android.clean.filemanager.FileSortHelper;
import com.android.clean.notification.NotificationCallBack;
import com.android.clean.notification.NotificationInfo;
import com.android.clean.notification.NotificationMonitorService;
import com.android.clean.util.Util;
import com.android.clean.util.MemoryManager;
import com.android.clean.db.CleanDBHelper;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by renqingyou on 2017/5/12.
 */

public class CleanManager {

    public static final String TAG = "CleanManager";
    private static CleanManager mInstance;
    private Context mContext;
    private ActivityManager am;
    private PackageManager pm;
    private List<UsageStats> usageStatsList;
    private List<ActivityManager.RecentTaskInfo> recentTasks;

    private long systemCacheSize, apkSize, uninstallSize, logSize, appCacheSize, ramSize, appListSize;

    private ArrayList<NotificationInfo> notificationList;
    private ArrayList<NotificationCallBack> notificationCallBackList;

    private final ArrayList<JunkInfo> appRamList = new ArrayList<>();
    private final ArrayList<JunkInfo> uninstallResiduals = new ArrayList<>();
    private final ArrayList<JunkInfo> appCaches = new ArrayList<>();

    private ArrayList<JunkInfo> appList = new ArrayList<>();


    private ArrayList<JunkInfo> apkFiles = new ArrayList<>();

    private ArrayList<JunkInfo> logFiles = new ArrayList<>();

    private final ArrayList<JunkInfo> systemCaches = new ArrayList<>();

    private CleanManager(Context context) {
        mContext = context.getApplicationContext();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        pm = context.getPackageManager();
    }

    public static CleanManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CleanManager(context);
        }
        return mInstance;
    }

    public void startNotificationCleanService() {
        mContext.startService(new Intent(mContext, NotificationMonitorService.class));
    }

    public void closeNotificationCleanService() {
        mContext.stopService(new Intent(mContext, NotificationMonitorService.class));
    }

    public void startLoad() {
        Intent intent = new Intent(mContext, CleanService.class);
        mContext.startService(intent);
    }

    void load() {
        long startTime = System.currentTimeMillis();
        loadAppCache(new AppCacheCallBack() {
            @Override
            public void loadFinished(ArrayList<JunkInfo> appCaches, long totalSize) {
                Log.e("rqy", "loadAppCache--" + totalSize);
                for (JunkInfo appCache : appCaches) {
                    Log.e("rqy", appCache + "");
                }
            }
        });
        loadAppRam(new AppRamCallBack() {
            @Override
            public void loadFinished(List<JunkInfo> appRamList, List<String> whiteList, long totalSize) {
                Log.e("rqy", "loadAppRam--" + totalSize);
                for (JunkInfo appRam : appRamList) {
                    Log.e("rqy", appRam + "");
                }
            }
        });
        loadApkFile(new FileInfoCallBack() {
            @Override
            public void loadFinished(ArrayList<JunkInfo> fileInfos, long totalSize) {
                Log.e("rqy", "loadApkFile--" + totalSize);
                for (JunkInfo fileInfo : fileInfos) {
                    Log.e("rqy", fileInfo + "");
                }
            }
        });
        loadLogFile(new FileInfoCallBack() {
            @Override
            public void loadFinished(ArrayList<JunkInfo> fileInfos, long totalSize) {
                Log.e("rqy", "loadLogFile--" + totalSize);
                for (JunkInfo fileInfo : fileInfos) {
                    Log.e("rqy", fileInfo + "");
                }
            }
        });
        loadUninstallResidual(new UninstallResidualCallback() {
            @Override
            public void loadFinished(ArrayList<JunkInfo> uninstallResiduals, long totalSize) {
                Log.e("rqy", "loadUninstallResidual--" + totalSize);
                for (JunkInfo uninstallResidual : uninstallResiduals) {
                    Log.e("rqy", uninstallResidual + "");
                }
            }
        });
        //注意，这个要放在最后执行
        loadSystemCache(new SystemCacheCallBack() {
            @Override
            public void loadFinished(ArrayList<JunkInfo> appInfoList, long totalSize) {
                Log.e("rqy", "loadSystemCache--" + totalSize);
                for (JunkInfo appInfo : appInfoList) {
                    Log.e("rqy", appInfo + "");
                }
            }
        });
        long endTime = System.currentTimeMillis();
        Log.e("rqy", "time=" + (endTime - startTime));
    }

    public void loadAppRam(AppRamCallBack appRamCallBack) {
        List<String> ignoreApp = CleanDBHelper.getInstance(mContext).getWhiteList(CleanDBHelper.TableType.Ram);
        List<String> whiteList = new ArrayList<>();
        List<AndroidAppProcess> listInfo = AndroidProcesses.getRunningAppProcesses();
        ramSize = 0;
        appRamList.clear();
        if (listInfo != null) {
            for (AndroidAppProcess info : listInfo) {
                int pid = info.pid;
                String packageName = info.name;
                try {
                    ApplicationInfo otherInfo = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
                    if (!Util.isThirdApp(otherInfo) || packageName.equals(mContext.getPackageName())) {
                        continue;
                    }
                    if (ignoreApp.contains(packageName)) {
                        whiteList.add(packageName);
                    } else {
                        JunkInfo appRam = new JunkInfo();
                        Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
                        appRam.size = (long) processMemoryInfo[0].getTotalPrivateDirty() * 1024;
                        appRam.isSelfBoot = Util.isStartSelf(mContext.getPackageManager(), packageName);
                        appRam.pkg = packageName;
                        appRam.type = JunkInfo.TableType.APP;
                        appRamList.add(appRam);
                        ramSize += appRam.size;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        appRamCallBack.loadFinished(appRamList, whiteList, ramSize);
    }

    public void loadUninstallResidual(UninstallResidualCallback uninstallResidualCallback) {
        String data = Util.readFileFromAssets(mContext, "/raw/");
        uninstallSize = 0;
        uninstallResiduals.clear();
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    String path = object.getString("path");
                    String sdpath = MemoryManager.getPhoneInSDCardPath();
                    File file = new File(sdpath + path);
                    if (!file.exists()) {
                        continue;
                    }
                    long size = Util.getFileSize(file);
                    String pkg = object.getString("pkg");
                    String name = object.getString("name");
                    JunkInfo info = new JunkInfo(pkg, name, path, size);
                    info.type = JunkInfo.TableType.APP;
                    uninstallResiduals.add(info);
                    uninstallSize += size;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        uninstallResidualCallback.loadFinished(uninstallResiduals, uninstallSize);
    }

    public void loadAppCache(AppCacheCallBack appCacheCallBack) {

        List<PackageInfo> packages = mContext.getPackageManager().getInstalledPackages(0);
        String cacheFilePath = null;
        try {
            cacheFilePath = mContext.getExternalCacheDir().getAbsolutePath();
        } catch (Exception e) {

        }
        if (cacheFilePath == null) {
            if (appCacheCallBack != null) {
                appCacheCallBack.loadFinished(appCaches, appCacheSize);
            }
            return;
        }
        appCacheSize = 0;
        appCaches.clear();

        for (final PackageInfo packageInfo : packages) {
            final String packageName = packageInfo.packageName;
            if (packageName.equals(mContext.getPackageName())) {
                continue;
            }
            String path = cacheFilePath.replace(mContext.getPackageName(), packageName);
            File file = new File(path);
            if (!file.exists()) {
                continue;
            }
            long size = Util.getFileSize(file);
            if (size > 0) {
                JunkInfo appCache = new JunkInfo(path, packageName, size);
                appCache.type = JunkInfo.TableType.APP;
                appCacheSize += size;
                appCaches.add(appCache);
            }
        }
        if (appCacheCallBack != null) {
            appCacheCallBack.loadFinished(appCaches, appCacheSize);
        }

    }

    public void loadApkFile(FileInfoCallBack fileInfoCallBack) {
        FileCategoryHelper fileCategoryHelper = new FileCategoryHelper(mContext);
        Cursor cursor = fileCategoryHelper.query(FileCategoryHelper.FileCategory.Apk, FileSortHelper.SortMethod.size);
        apkSize = 0;
        apkFiles.clear();
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                if (cursor.getString(FileCategoryHelper.COLUMN_SIZE) == null) {
                    continue;
                }
                long size = Long.parseLong(cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                String path = cursor.getString(FileCategoryHelper.COLUMN_PATH);
                String name = com.android.clean.filemanager.Util.getNameFromFilepath(path);
                long date = cursor.getLong(FileCategoryHelper.COLUMN_DATE);
                apkSize += size;
                JunkInfo info = new JunkInfo(path, name, date, size);
                info.type = JunkInfo.TableType.APKFILE;
                apkFiles.add(info);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (fileInfoCallBack != null) {
            fileInfoCallBack.loadFinished(apkFiles, apkSize);
        }
    }

    public void loadLogFile(FileInfoCallBack fileInfoCallBack) {
        FileCategoryHelper fileCategoryHelper = new FileCategoryHelper(mContext);
        Cursor cursor = fileCategoryHelper.query(FileCategoryHelper.FileCategory.Log, FileSortHelper.SortMethod.size);
        logSize = 0;
        logFiles.clear();
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                if (cursor.getString(FileCategoryHelper.COLUMN_SIZE) == null) {
                    continue;
                }
                long size = Long.parseLong(cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                String path = cursor.getString(FileCategoryHelper.COLUMN_PATH);
                String name = com.android.clean.filemanager.Util.getNameFromFilepath(path);
                long date = cursor.getLong(FileCategoryHelper.COLUMN_DATE);
                logSize += size;
                JunkInfo info = new JunkInfo(path, name, date, size);
                info.type = JunkInfo.TableType.LOGFILE;
                logFiles.add(info);
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (fileInfoCallBack != null) {
            fileInfoCallBack.loadFinished(logFiles, logSize);
        }
    }

    //SystemCache和应用管理用的同一个
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void loadSystemCache(final SystemCacheCallBack systemCacheCallBack) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStatsList = getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(usageStatsList, new Sizesort());
        } else {
            recentTasks = am.getRecentTasks(10, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
        }

        systemCacheSize = 0;
        appListSize = 0;
        systemCaches.clear();
        appList.clear();
        if (systemCacheCallBack == null) {
            throw new Error("systemCacheCallBack can not be null");
        }

        List<PackageInfo> packages = pm.getInstalledPackages(0);

        if (packages == null || packages.isEmpty()) {
            if (systemCacheCallBack != null) {
                systemCacheCallBack.loadFinished(systemCaches, systemCacheSize);
            }
            return;
        }

        ArrayList<PackageInfo> ignoreApps = new ArrayList<>();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (!Util.isThirdApp(packageInfo.applicationInfo) || TextUtils.equals(packageInfo.packageName, mContext.getPackageName())) {
                ignoreApps.add(packageInfo);
            }
        }
        packages.removeAll(ignoreApps);
        final int size = packages.size();
        if (size == 0) {
            if (systemCacheCallBack != null) {
                systemCacheCallBack.loadFinished(systemCaches, 0);
            }
            return;
        }

        Method mGetPackageSizeInfoMethod = getPackageSizeInfoMethod(mContext);

        if (mGetPackageSizeInfoMethod == null) {
            Log.e("rqy", "mGetPackageSizeInfoMethod is null");
            if (systemCacheCallBack != null) {
                systemCacheCallBack.loadFinished(systemCaches, 0);
            }
            return;
        }
        final CountDownLatch countDownLatch = new CountDownLatch(size);
        for (final PackageInfo packageInfo : packages) {
            final String packageName = packageInfo.packageName;
            boolean version = Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M;
            try {
                if (version) {
                    Method myUserId = UserHandle.class.getDeclaredMethod("myUserId");
                    int userID = (Integer) myUserId.invoke(pm, (Object[]) null);
                    mGetPackageSizeInfoMethod.invoke(pm, packageName, userID, new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                    //loadAppSizeCompleted(true, pStats, packageInfo);
                                    getStatsCompleted(packageInfo, pStats, countDownLatch);
                                }
                            }
                    );
                } else {
                    mGetPackageSizeInfoMethod.invoke(pm, packageName, new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                        throws RemoteException {
                                    getStatsCompleted(packageInfo, pStats, countDownLatch);
                                }
                            }
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                getStatsCompleted(packageInfo, null, countDownLatch);
            }
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {

        }
        systemCacheCallBack.loadFinished(systemCaches, systemCacheSize);
    }

    private void getStatsCompleted(PackageInfo packageInfo, PackageStats pStats, CountDownLatch countDownLatch) {

        synchronized (countDownLatch) {
            JunkInfo appInfo = new JunkInfo();
            appInfo.pkg = packageInfo.packageName;
            appInfo.date = packageInfo.lastUpdateTime;
            appInfo.label = packageInfo.applicationInfo.loadLabel(pm).toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                for (int i = 0; i < usageStatsList.size(); i++) {
                    if (TextUtils.equals(packageInfo.packageName, usageStatsList.get(i).getPackageName())) {
                        appInfo.lastRunTime = i;
                    }
                }
            } else {
                for (int i = 0; i < recentTasks.size(); i++) {
                    if (TextUtils.equals(packageInfo.packageName, recentTasks.get(i).baseIntent.getComponent().getPackageName())) {
                        appInfo.lastRunTime = i;
                    }
                }
            }

            if (pStats != null) {
                appInfo.size = pStats.cacheSize;
                appInfo.allSize = pStats.codeSize + pStats.dataSize;
                systemCacheSize += appInfo.size;
                appListSize += appInfo.allSize;
            }
            appInfo.type = JunkInfo.TableType.APP;
            appList.add(appInfo);
            if (appInfo.size > 0) {
                systemCaches.add(appInfo);
            }
            countDownLatch.countDown();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        UsageStatsManager mUsageStatsManager = (UsageStatsManager) mContext
                .getSystemService(Context.USAGE_STATS_SERVICE); //Context.USAGE_STATS_SERVICE
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        List<UsageStats> queryUsageStats = mUsageStatsManager
                .queryUsageStats(intervalType, cal.getTimeInMillis(),
                        System.currentTimeMillis());

        if (queryUsageStats.size() == 0) {
//            mContext.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        return queryUsageStats;
    }


    public void clearSystemCache() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        try {
            systemCaches.clear();
            systemCacheSize = 0;
            Method mFreeStorageAndNotifyMethod = mContext.getPackageManager().getClass().getMethod(
                    "freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
            mFreeStorageAndNotifyMethod.invoke(mContext.getPackageManager(),
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
    }


    public Method getPackageSizeInfoMethod(Context context) {
        PackageManager pm = context.getPackageManager();
        Method packageSizeInfoMethod = null;
        boolean version = Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M;
        try {
            if (version) {
                packageSizeInfoMethod = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class, int.class, IPackageStatsObserver.class);

            } else {
                packageSizeInfoMethod = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            }

        } catch (Exception e) {
            Util.log(TAG, "loadSystemCache error mGetPackageSizeInfoMethod=null");
            e.printStackTrace();
        }
        return packageSizeInfoMethod;
    }


    public void notificationChanged(NotificationInfo notifiInfo, boolean isAdd) {
        if (notificationList == null) {
            notificationList = new ArrayList<>();
        }
        for (NotificationInfo info : notificationList) {
            if (TextUtils.equals(info.pkg, notifiInfo.pkg) && info.id == notifiInfo.id) {
                notificationList.remove(info);
                break;
            }
        }
        if (isAdd) {
            notificationList.add(notifiInfo);
        }
        for (NotificationCallBack callBack : notificationCallBackList) {
            callBack.notificationChanged(notificationList);
        }
    }

    public void clearNotificationInfo() {
        if (notificationList == null) {
            notificationList = new ArrayList<>();
        }
        notificationList.clear();
        for (NotificationCallBack callBack : notificationCallBackList) {
            callBack.notificationChanged(notificationList);
        }
    }

    public void addNotificationCallBack(NotificationCallBack notificationCallBack) {
        if (notificationCallBack == null) {
            return;
        }
        if (notificationCallBackList == null) {
            notificationCallBackList = new ArrayList<>();
        }
        if (!notificationCallBackList.contains(notificationCallBack)) {
            notificationCallBackList.add(notificationCallBack);
        }
        notificationCallBack.notificationChanged(notificationList);
    }

    public void removeNotificatioCallBack(NotificationCallBack notificationCallBack) {
        if (notificationCallBack == null) {
            return;
        }
        if (notificationCallBackList.contains(notificationCallBack)) {
            notificationCallBackList.remove(notificationCallBack);
        }
    }


    public long getCacheSize() {
        return systemCacheSize;
    }

    public long getAppListSize() {
        return appListSize;
    }

    public long getApkSize() {
        return apkSize;
    }

    public long getUnloadSize() {
        return uninstallSize;
    }

    public long getLogSize() {
        return logSize;
    }

    public long getDataSize() {
        return appCacheSize;
    }

    public long getRamSize() {
        return ramSize;
    }

    public ArrayList<JunkInfo> getAppRamList() {
        return (ArrayList<JunkInfo>) appRamList;
    }

    public ArrayList<JunkInfo> getUninstallResiduals() {
        return (ArrayList<JunkInfo>) uninstallResiduals;
    }

    public ArrayList<JunkInfo> getAppCaches() {
        return (ArrayList<JunkInfo>) appCaches;
    }

    public ArrayList<JunkInfo> getSystemCaches() {
        return (ArrayList<JunkInfo>) systemCaches;
    }

    public ArrayList<JunkInfo> getAppList() {
        return (ArrayList<JunkInfo>) appList;
    }

    public ArrayList<JunkInfo> getApkFiles() {
        return (ArrayList<JunkInfo>) apkFiles;
    }

    public ArrayList<JunkInfo> getLogFiles() {
        return (ArrayList<JunkInfo>) logFiles;
    }


    public void removeRam(JunkInfo appRam) {
        am.killBackgroundProcesses(appRam.pkg);
//        if (appRam.isSelfBoot) {
//            return;
//        }
        ramSize -= appRam.size;
        if (appRamList != null) {
            appRamList.remove(appRam);
        }
    }

    public void removeRamSelfBoot(JunkInfo appRam) {
        am.killBackgroundProcesses(appRam.pkg);
        ramSize -= appRam.size;
        if (appRamList != null) {
            appRamList.remove(appRam);
        }
    }

    public void clearRam() {
        if (appRamList != null) {
            appRamList.clear();
            ramSize = 0;
        }
    }

    public void removeAppCache(JunkInfo appCache) {
        Util.deleteFile(appCache.path);
        appCacheSize -= appCache.size;
        if (appCaches != null) {
            appCaches.remove(appCache);
        }
    }

    public void removeAppList(JunkInfo appInfo) {
        appListSize -= appInfo.size;
        if (appList != null) {
            appList.remove(appInfo);
        }
    }

    public void removeFilesOfUnintalledApk(JunkInfo uninstallResidual) {
        Util.deleteFile(uninstallResidual.path);
        uninstallSize -= uninstallResidual.size;
        if (uninstallResiduals != null) {
            uninstallResiduals.remove(uninstallResidual);
        }
    }

    public void removeApkFiles(JunkInfo fileInfo) {
        Util.deleteFile(fileInfo.path);
        apkSize -= fileInfo.size;
        if (apkFiles != null) {
            apkFiles.remove(fileInfo);
        }
    }

    public void removeAppLog(JunkInfo fileInfo) {
        Util.deleteFile(fileInfo.path);
        logSize -= fileInfo.size;
        if (logFiles != null) {
            logFiles.remove(fileInfo);
        }
    }
}
