package com.easy.clean.easyutils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by on 2017/5/19.
 */

public class LoadManager {
    private PackageManager pm;
    private static LoadManager loadManager;

    private LoadManager(Context context) {
        pm = context.getPackageManager();
    }

    public static LoadManager getInstance(Context context) {
        if (loadManager == null) {
            loadManager = new LoadManager(context.getApplicationContext());
        }
        return loadManager;
    }

    //获取程序 图标
    public Drawable getAppIcon(String packname) {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //是否安装该应用
    public boolean isPkgInstalled(String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public Drawable getApkIconforPath(String apkPath) {
        PackageInfo info = null;
        try {
            info = pm.getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
        } catch (Exception e) {

        }
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e("ApkIconLoader", e.toString());
            }
        }
        return null;
    }


    //获取程序 label
    public String getAppLabel(String packname) {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
