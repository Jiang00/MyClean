package com.supers.clean.junk.task;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Build;
import android.os.UserHandle;
import android.util.Log;


import com.supers.clean.junk.entity.JunkInfo;

import java.lang.reflect.Method;
import java.util.List;

/**
 */

public class SystemCacheTask extends SimpleTask {

    private static final String TAG = "SystemCacheTask";

    public SystemCacheTask(Context context, SimpleTaskListener simpleTaskListener) {
        super(context, simpleTaskListener, TAG);
    }

    @Override
    void loadData() {
        try {
            List<ApplicationInfo> packages = getInstalledApplications(pm);
            Method mGetPackageSizeInfoMethod;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                mGetPackageSizeInfoMethod = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class, int.class, IPackageStatsObserver.class);
            } else {
                mGetPackageSizeInfoMethod = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            }

            final int size = packages.size();
            for (int i = 0; i < size; i++) {
                ApplicationInfo info = packages.get(i);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    Method myUserId = UserHandle.class.getDeclaredMethod("myUserId");
                    int userID = (Integer) myUserId.invoke(pm, (Object[]) null);
                    mGetPackageSizeInfoMethod.invoke(pm, info.packageName, userID, new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
                                    if (pStats.cacheSize > 12 * 1024) {
                                        try {
                                            if (!pStats.packageName.equals(mContext.getPackageName())) {
                                                JunkInfo info = new JunkInfo(pStats.packageName,
                                                        mContext.getPackageManager().getApplicationLabel(
                                                                mContext.getPackageManager().getApplicationInfo(
                                                                        pStats.packageName,
                                                                        PackageManager.GET_META_DATA)
                                                        ).toString(), true,
                                                        mContext.getPackageManager().getApplicationIcon(
                                                                pStats.packageName),
                                                        pStats.cacheSize
                                                );
                                                if (mSimpleTaskListener != null) {
                                                    mSimpleTaskListener.loading(info, pStats.cacheSize);
                                                }

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e(TAG, "onGetStatsCompleted Exception --" + e.getMessage());
                                        }
                                    }
                                }
                            }
                    );
                } else {
                    mGetPackageSizeInfoMethod.invoke(pm, info.packageName, new IPackageStatsObserver.Stub() {
                                @Override
                                public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
                                    if (pStats.cacheSize > 12 * 1024) {
                                        try {
                                            if (!pStats.packageName.equals(mContext.getPackageName())) {
                                                JunkInfo info = new JunkInfo(pStats.packageName,
                                                        mContext.getPackageManager().getApplicationLabel(
                                                                mContext.getPackageManager().getApplicationInfo(
                                                                        pStats.packageName,
                                                                        PackageManager.GET_META_DATA)
                                                        ).toString(), true,
                                                        mContext.getPackageManager().getApplicationIcon(
                                                                pStats.packageName),
                                                        pStats.cacheSize
                                                );
                                                if (mSimpleTaskListener != null) {
                                                    mSimpleTaskListener.loading(info, pStats.cacheSize);
                                                }
                                            }
                                        } catch (PackageManager.NameNotFoundException e) {
                                            e.printStackTrace();
                                            Log.e(TAG, "onGetStatsCompleted Exception --" + e.getMessage());
                                        }
                                    }

                                }
                            }
                    );
                }
                if (isCancelTask) {
                    if (mSimpleTaskListener != null) {
                        mSimpleTaskListener.cancelLoading();
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "loadData Exception --" + e.getMessage());
        }

        if (mSimpleTaskListener != null) {
            mSimpleTaskListener.finishLoading(0, null);
        }

    }

}
