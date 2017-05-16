package com.android.clean.core;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.database.Cursor;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;

import com.android.clean.entity.AppInfo;
import com.android.clean.callback.AppCacheCallBack;
import com.android.clean.callback.AppRamCallBack;
import com.android.clean.callback.FileInfoCallBack;
import com.android.clean.callback.SystemCacheCallBack;
import com.android.clean.callback.UninstallResidualCallback;
import com.android.clean.entity.AppCache;
import com.android.clean.entity.AppRam;
import com.android.clean.entity.UninstallResidual;
import com.android.clean.filemanager.FileCategoryHelper;
import com.android.clean.filemanager.FileInfo;
import com.android.clean.filemanager.FileSortHelper;
import com.android.clean.filemanager.Util;
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

/**
 * Created by renqingyou on 2017/5/12.
 */

public class CleanManager {

    public static final String TAG = "CleanManager";
    private static CleanManager mInstance;
    private static Context mContext;
    private static ActivityManager am;

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

    public void loadAppRam(AppRamCallBack appRamCallBack) {
        List<String> ignoreApp = PreData.getWhiteList(mContext, PreData.WHILT_LIST);
        List<String> whiteList = new ArrayList<>();
        List<AppRam> appRamList = new ArrayList<>();
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
        final ArrayList<UninstallResidual> uninstallResiduals = new ArrayList<>();
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

        ArrayList<AppCache> appCaches = new ArrayList<>();

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
        appCacheCallBack.loadFinished(appCaches,totalSize);

    }

    public void loadFile(FileCategoryHelper.FileCategory fileCategory, FileSortHelper.SortMethod sortMethod, FileInfoCallBack fileInfoCallBack) {
        ArrayList<FileInfo> fileInfos = new ArrayList<>();
        FileCategoryHelper fileCategoryHelper = new FileCategoryHelper(mContext);
        Cursor cursor = fileCategoryHelper.query(fileCategory, sortMethod);
        long totalSize = 0;
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                long size = Long.parseLong(cursor.getString(FileCategoryHelper.COLUMN_SIZE));
                String path = cursor.getString(FileCategoryHelper.COLUMN_PATH);
                String name = Util.getNameFromFilepath(path);
                long date = cursor.getLong(FileCategoryHelper.COLUMN_DATE);
                totalSize += size;
                fileInfos.add(new FileInfo(path, name, date, size));
            } while (cursor.moveToNext());
            cursor.close();
        }

        fileInfoCallBack.loadFinished(fileInfos, totalSize);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void loadSystemCache(final SystemCacheCallBack systemCacheCallBack) {
        if (systemCacheCallBack == null) {
            throw new Error("systemCacheCallBack can not be null");
        }
        final ArrayList<AppInfo> appInfoList = new ArrayList<>();

        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);

        if (packages == null || packages.isEmpty()) {
            systemCacheCallBack.loadFinished(appInfoList, 0);
            return;
        }

        for (final PackageInfo packageInfo : packages) {
            if (!CommonUtil.isThirdApp(packageInfo.applicationInfo) || TextUtils.equals(packageInfo.packageName, mContext.getPackageName())) {
                packages.remove(packageInfo);
            }
        }
        final int size = packages.size();
        if (size == 0) {
            systemCacheCallBack.loadFinished(appInfoList, 0);
            return;
        }

        Method mGetPackageSizeInfoMethod = getPackageSizeInfoMethod(mContext);

        if (mGetPackageSizeInfoMethod == null) {
            Log.e("rqy", "mGetPackageSizeInfoMethod is null");
            systemCacheCallBack.loadFinished(appInfoList, 0);
            return;
        }

        final int MSG_GET_STATS = 0;
        final int MSG_GET_STATS_EXCEPTION = 1;


        final Handler handler = new Handler(Looper.getMainLooper()) {
            int i = 0;
            long totalSize = 0;

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_GET_STATS) {
                    i++;
                    int size = msg.arg1;
                    totalSize += size;
                } else if (msg.what == MSG_GET_STATS_EXCEPTION) {
                    i++;
                }
                if (i == size - 1) {
                    systemCacheCallBack.loadFinished(appInfoList, totalSize);
                }
            }
        };

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
                                    Message msg = new Message();
                                    if (succeeded) {
                                        AppInfo appInfo = new AppInfo();
                                        appInfo.pkgCacheSize = pStats.dataSize + pStats.codeSize;
                                        appInfo.pkgName = packageName;
                                        msg.arg1 = (int) appInfo.pkgCacheSize;
                                        appInfoList.add(appInfo);
                                    }
                                    msg.what = MSG_GET_STATS;
                                    handler.sendMessage(msg);
                                }
                            }
                    );
                } else {
                    mGetPackageSizeInfoMethod.invoke(pm, packageName, new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                        throws RemoteException {
                                    Message msg = new Message();
                                    if (succeeded) {
                                        AppInfo appInfo = new AppInfo();
                                        appInfo.pkgCacheSize = pStats.dataSize + pStats.codeSize;
                                        appInfo.pkgName = packageName;
                                        msg.arg1 = (int) appInfo.pkgCacheSize;
                                        appInfoList.add(appInfo);
                                    }
                                    msg.what = MSG_GET_STATS;
                                    handler.sendMessage(msg);
                                }
                            }
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = MSG_GET_STATS_EXCEPTION;
                msg.obj = packageName;
                handler.sendMessage(msg);
            }
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


}
