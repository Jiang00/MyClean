package com.supers.clean.junk.task;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.RemoteException;
import android.os.UserHandle;
import android.text.TextUtils;


import com.android.clean.whitelist.WhiteListHelper;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.util.Constant;
import com.android.clean.util.PreData;
import com.supers.clean.junk.entity.Sizesort;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/*
 * 设备上的程序包
 */
public class AppManagerTask extends SimpleTask {
    private List<String> whiteList;
    private List<String> notifi_whiteList;
    private long allSize = 0;
    private ArrayList<JunkInfo> appList = new ArrayList<>();
    private List<UsageStats> usageStatsList;
    private List<ActivityManager.RecentTaskInfo> recentTasks;

    public AppManagerTask(Context context, SimpleTaskListener simpleTaskListener) {
        super(context, simpleTaskListener);
        whiteList = WhiteListHelper.getInstance(context).getWhiteList(WhiteListHelper.TableType.Ram);
        notifi_whiteList = WhiteListHelper.getInstance(context).getWhiteList(WhiteListHelper.TableType.Notification);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    void loadData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStatsList = getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(usageStatsList, new Sizesort());
        } else {
            recentTasks = am.getRecentTasks(10, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
        }

        List<PackageInfo> packages;
        // 获取所有package,
        synchronized (lock) {
            packages = pm
                    .getInstalledPackages(0);
        }
        if (packages == null || packages.isEmpty()) {
            return;
        }
        Method mGetPackageSizeInfoMethod = null;
        boolean version = Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M;
        try {
            if (version) {
                mGetPackageSizeInfoMethod = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class, int.class, IPackageStatsObserver.class);

            } else {
                mGetPackageSizeInfoMethod = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (final PackageInfo packageInfo : packages) {
            final String packageName = packageInfo.packageName;
            if (packageName.equals(mContext.getPackageName()) || (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            if (mGetPackageSizeInfoMethod == null) {
                loadAppSizeCompleted(false, null, packageInfo);
                continue;
            }
            try {
                if (version) {
                    Method myUserId = UserHandle.class.getDeclaredMethod("myUserId");
                    int userID = (Integer) myUserId.invoke(pm, (Object[]) null);
                    mGetPackageSizeInfoMethod.invoke(pm, packageName, userID, new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                    loadAppSizeCompleted(true, pStats, packageInfo);
                                }
                            }
                    );
                } else {
                    mGetPackageSizeInfoMethod.invoke(pm, packageName, new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                        throws RemoteException {
                                    loadAppSizeCompleted(true, pStats, packageInfo);
                                }
                            }
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
                loadAppSizeCompleted(false, null, packageInfo);
            }
            if (isCancelTask) {
                if (mSimpleTaskListener != null) {
                    mSimpleTaskListener.cancelLoading();
                }
                break;
            }
        }

        if (mSimpleTaskListener != null) {
            mSimpleTaskListener.finishLoading(allSize, appList);
        }
    }

    public void loadAppSizeCompleted(boolean loadSuc, PackageStats pStats, PackageInfo packageInfo) {
        long time = packageInfo.lastUpdateTime;
        long lastRunTime = 1000;
        long size = 0;
        if (loadSuc) {
            size = pStats.codeSize + pStats.dataSize;
        }
        Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
        String label = (String) packageInfo.applicationInfo.loadLabel(pm);
        String packageName = packageInfo.packageName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (int i = 0; i < usageStatsList.size(); i++) {
                if (TextUtils.equals(packageName, usageStatsList.get(i).getPackageName())) {
                    lastRunTime = i;
                }
            }
        } else {
            for (int i = 0; i < recentTasks.size(); i++) {
                if (TextUtils.equals(packageName, recentTasks.get(i).baseIntent.getComponent().getPackageName())) {
                    lastRunTime = i;
                }
            }
        }
        JunkInfo info = new JunkInfo(false, icon, label, packageName, time, size, whiteList.contains(packageName), notifi_whiteList.contains(packageName), lastRunTime);
        allSize += size;
        appList.add(info);
        if (mSimpleTaskListener != null) {
            mSimpleTaskListener.loading(info, size);
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
}

