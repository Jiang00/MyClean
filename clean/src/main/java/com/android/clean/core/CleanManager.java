package com.android.clean.core;

import android.annotation.TargetApi;
import android.app.ActivityManager;
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
import com.android.clean.entity.AppCache;
import com.android.clean.entity.AppInfo;
import com.android.clean.entity.AppRam;
import com.android.clean.entity.UninstallResidual;
import com.android.clean.filemanager.FileCategoryHelper;
import com.android.clean.filemanager.FileInfo;
import com.android.clean.filemanager.FileSortHelper;
import com.android.clean.filemanager.Util;
import com.android.clean.notification.NotificationCallBack;
import com.android.clean.notification.NotificationInfo;
import com.android.clean.notification.NotificationMonitorService;
import com.android.clean.util.CommonUtil;
import com.android.clean.util.MemoryManager;
import com.android.clean.util.PreData;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by renqingyou on 2017/5/12.
 */

public class CleanManager {

    public static final String TAG = "CleanManager";
    private static CleanManager mInstance;
    private Context mContext;
    private ActivityManager am;
    private long totalSystemCacheSize = 0;

    private ArrayList<NotificationInfo> notificationList;
    private ArrayList<NotificationCallBack> notificationCallBackList;

    private final ArrayList<AppRam> appRamList = new ArrayList<>();
    private final ArrayList<UninstallResidual> uninstallResiduals = new ArrayList<>();
    private final ArrayList<AppCache> appCaches = new ArrayList<>();


    private ArrayList<FileInfo> apkFiles = new ArrayList<>();

    private ArrayList<FileInfo> logFiles = new ArrayList<>();

    private final Vector<AppInfo> systemCaches = new Vector<>();

    private CleanManager(Context context) {
        mContext = context.getApplicationContext();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public static CleanManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CleanManager(context);
        }
        return mInstance;
    }

    private ArrayList<AppRam> getAppRamList() {
        return (ArrayList<AppRam>) appRamList.clone();
    }

    private ArrayList<UninstallResidual> getUninstallResiduals() {
        return (ArrayList<UninstallResidual>) uninstallResiduals.clone();
    }

    private ArrayList<AppCache> getAppCaches() {
        return (ArrayList<AppCache>) appCaches.clone();
    }

    private Vector<AppInfo> getSystemCaches() {
        return (Vector<AppInfo>) systemCaches.clone();
    }

    public ArrayList<FileInfo> getApkFiles() {
        return (ArrayList<FileInfo>) apkFiles.clone();
    }

    public ArrayList<FileInfo> getLogFiles() {
        return (ArrayList<FileInfo>) apkFiles.clone();
    }

    public void startNotificationCleanService() {
        mContext.startService(new Intent(mContext, NotificationMonitorService.class));
    }

    public void closeNotificationCleanService() {
        mContext.stopService(new Intent(mContext, NotificationMonitorService.class));
    }

    public void startWorkLoad() {
        Intent intent = new Intent(mContext, CleanService.class);
        mContext.startService(intent);
    }

    public void startLoad() {
        load();
    }

    public void load() {
        long startTime = System.currentTimeMillis();
        loadSystemCache(new SystemCacheCallBack() {
            @Override
            public void loadFinished(Vector<AppInfo> appInfoList, long totalSize) {
                Log.e("rqy", "loadSystemCache--" + totalSize);
                for (AppInfo appInfo : appInfoList) {
                    Log.e("rqy", appInfo + "");
                }
            }
        });
        loadAppCache(new AppCacheCallBack() {
            @Override
            public void loadFinished(ArrayList<AppCache> appCaches, long totalSize) {
                Log.e("rqy", "loadAppCache--" + totalSize);
                for (AppCache appCache : appCaches) {
                    Log.e("rqy", appCache + "");
                }
            }
        });
        loadAppRam(new AppRamCallBack() {
            @Override
            public void loadFinished(List<AppRam> appRamList, List<String> whiteList, int totalSize) {
                Log.e("rqy", "loadAppRam--" + totalSize);
                for (AppRam appRam : appRamList) {
                    Log.e("rqy", appRam + "");
                }
            }
        });
        loadApkFile(new FileInfoCallBack() {
            @Override
            public void loadFinished(ArrayList<FileInfo> fileInfos, long totalSize) {
                Log.e("rqy", "loadApkFile--" + totalSize);
                for (FileInfo fileInfo : fileInfos) {
                    Log.e("rqy", fileInfo + "");
                }
            }
        });
        loadLogFile(new FileInfoCallBack() {
            @Override
            public void loadFinished(ArrayList<FileInfo> fileInfos, long totalSize) {
                Log.e("rqy", "loadLogFile--" + totalSize);
                for (FileInfo fileInfo : fileInfos) {
                    Log.e("rqy", fileInfo + "");
                }
            }
        });
        loadUninstallResidual(new UninstallResidualCallback() {
            @Override
            public void loadFinished(ArrayList<UninstallResidual> uninstallResiduals, long totalSize) {
                Log.e("rqy", "loadUninstallResidual--" + totalSize);
                for (UninstallResidual uninstallResidual : uninstallResiduals) {
                    Log.e("rqy", uninstallResidual + "");
                }
            }
        });
        long endTime = System.currentTimeMillis();
        Log.e("rqy", "time=" + (endTime - startTime));
    }

    public void loadAppRam(AppRamCallBack appRamCallBack) {
        List<String> ignoreApp = PreData.getWhiteList(mContext, PreData.WHILT_LIST);
        List<String> whiteList = new ArrayList<>();
        List<AndroidAppProcess> listInfo = AndroidProcesses.getRunningAppProcesses();
        int totalSize = 0;
        if (listInfo != null) {
            for (AndroidAppProcess info : listInfo) {
                int pid = info.pid;
                String packageName = info.name;
                try {
                    ApplicationInfo otherInfo = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
                    if (!CommonUtil.isThirdApp(otherInfo) || packageName.equals(mContext.getPackageName()) || packageName.contains("com.eosmobi")) {
                        continue;
                    }
                    if (ignoreApp.contains(packageName)) {
                        whiteList.add(packageName);
                    } else {
                        AppRam appRam = new AppRam();
                        Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
                        appRam.size = (long) processMemoryInfo[0].getTotalPrivateDirty() * 1024;
                        appRam.isSelfBoot = CommonUtil.isStartSelf(mContext.getPackageManager(), packageName);
                        appRam.pkg = packageName;
                        appRamList.add(appRam);
                        totalSize += appRam.size;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        appRamCallBack.loadFinished(appRamList, whiteList, totalSize);
    }

    public void loadUninstallResidual(UninstallResidualCallback uninstallResidualCallback) {
        String data = CommonUtil.readFileFromAssets(mContext, "/raw/");
        long totalSize = 0;
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
                    long size = CommonUtil.getFileSize(file);
                    String pkg = object.getString("pkg");
                    String name = object.getString("name");
                    uninstallResiduals.add(new UninstallResidual(pkg, name, path));
                    totalSize += size;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        uninstallResidualCallback.loadFinished(uninstallResiduals, totalSize);
    }

    public void loadAppCache(AppCacheCallBack appCacheCallBack) {

        List<PackageInfo> packages = mContext.getPackageManager().getInstalledPackages(0);

        String cacheFilePath = mContext.getExternalCacheDir().getAbsolutePath();
        Log.e("rqy", "cacheFile path=" + cacheFilePath);

        long totalSize = 0;
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
            long size = CommonUtil.getFileSize(file);
            if (size > 0) {
                AppCache appCache = new AppCache(path, packageName, size);
                totalSize += totalSize;
                appCaches.add(appCache);
            }
        }
        if (appCacheCallBack != null) {
            appCacheCallBack.loadFinished(appCaches, totalSize);
        }

    }

    public void loadApkFile(FileInfoCallBack fileInfoCallBack) {
        FileCategoryHelper fileCategoryHelper = new FileCategoryHelper(mContext);
        Cursor cursor = fileCategoryHelper.query(FileCategoryHelper.FileCategory.Apk, FileSortHelper.SortMethod.size);
        long totalSize = 0;
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                long size = Long.parseLong(cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                String path = cursor.getString(FileCategoryHelper.COLUMN_PATH);
                String name = Util.getNameFromFilepath(path);
                long date = cursor.getLong(FileCategoryHelper.COLUMN_DATE);
                totalSize += size;
                apkFiles.add(new FileInfo(path, name, date, size));
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (fileInfoCallBack != null) {
            fileInfoCallBack.loadFinished(apkFiles, totalSize);
        }
    }

    public void loadLogFile(FileInfoCallBack fileInfoCallBack) {
        FileCategoryHelper fileCategoryHelper = new FileCategoryHelper(mContext);
        Cursor cursor = fileCategoryHelper.query(FileCategoryHelper.FileCategory.Log, FileSortHelper.SortMethod.size);
        long totalSize = 0;
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                long size = Long.parseLong(cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                String path = cursor.getString(FileCategoryHelper.COLUMN_PATH);
                String name = Util.getNameFromFilepath(path);
                long date = cursor.getLong(FileCategoryHelper.COLUMN_DATE);
                totalSize += size;
                logFiles.add(new FileInfo(path, name, date, size));
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (fileInfoCallBack != null) {
            fileInfoCallBack.loadFinished(logFiles, totalSize);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void loadSystemCache(final SystemCacheCallBack systemCacheCallBack) {
        totalSystemCacheSize = 0;
        if (systemCacheCallBack == null) {
            throw new Error("systemCacheCallBack can not be null");
        }

        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        if (packages == null || packages.isEmpty()) {
            if (systemCacheCallBack != null) {
                systemCacheCallBack.loadFinished(systemCaches, totalSystemCacheSize);
            }
            return;
        }

        ArrayList<PackageInfo> ignoreApps = new ArrayList<>();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (!CommonUtil.isThirdApp(packageInfo.applicationInfo) || TextUtils.equals(packageInfo.packageName, mContext.getPackageName())) {
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
                                    if (succeeded) {
                                        AppInfo appInfo = new AppInfo();
                                        appInfo.pkgCacheSize = pStats.dataSize + pStats.codeSize;
                                        appInfo.pkgName = packageName;
                                        systemCaches.add(appInfo);
                                        totalSystemCacheSize += appInfo.pkgCacheSize;
                                        if (systemCaches.size() == size) {
                                            if (systemCacheCallBack != null) {
                                                systemCacheCallBack.loadFinished(systemCaches, totalSystemCacheSize);
                                            }
                                        }
                                    }

                                }
                            }
                    );
                } else {
                    mGetPackageSizeInfoMethod.invoke(pm, packageName, new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                        throws RemoteException {
                                    if (succeeded) {
                                        AppInfo appInfo = new AppInfo();
                                        appInfo.pkgCacheSize = pStats.dataSize + pStats.codeSize;
                                        appInfo.pkgName = packageName;
                                        systemCaches.add(appInfo);
                                        totalSystemCacheSize += appInfo.pkgCacheSize;
                                        if (systemCaches.size() == size) {
                                            if (systemCacheCallBack != null) {
                                                systemCacheCallBack.loadFinished(systemCaches, totalSystemCacheSize);
                                            }
                                        }
                                    }
                                }
                            }
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (systemCacheCallBack != null) {
                    systemCacheCallBack.loadFinished(systemCaches, totalSystemCacheSize);
                }
                break;
            }
        }

    }

    public void clearSystemCache() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        try {
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
            CommonUtil.log(TAG, "loadSystemCache error mGetPackageSizeInfoMethod=null");
            e.printStackTrace();
        }
        return packageSizeInfoMethod;
    }


    public void notificationChanged(NotificationInfo notifiInfo) {
        if (notificationList == null) {
            notificationList = new ArrayList<>();
        }
        for (NotificationInfo info : notificationList) {
            if (TextUtils.equals(info.pkg, notifiInfo.pkg) && info.id == notifiInfo.id) {
                notificationList.remove(info);
                break;
            }
        }
        notificationList.add(notifiInfo);
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
}
