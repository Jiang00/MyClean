package com.supers.clean.junk.manager;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CommonUtil {


    public static String getStrTime(long time) {
        long a = System.currentTimeMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yy-MM-dd ");
        String times = sf.format(new Date(time));
        return times;
    }

    public static String getStrTime2(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm ");
        String times = sf.format(new Date(time));
        return times;
    }

    public static String getStrTime3(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyMMdd");
        String times = sf.format(new Date(time));
        return times;
    }

    public static String getStrTimeHH(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("HH");
        String times = sf.format(new Date(time));
        return times;
    }

    public static String getStrTimedd(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        String times = sf.format(new Date(time));
        return times;
    }

    /**
     * 得到字符串方式的文件大小
     *
     * @param filesize
     * ,单位b
     * @return
     */
    private static DecimalFormat df = new DecimalFormat("#.00");
    private static DecimalFormat df1 = new DecimalFormat("#.0");
    private static DecimalFormat df2 = new DecimalFormat("#");

    public static String getFileSize(long filesize) {
        StringBuffer mstrbuf = new StringBuffer();
        if (filesize <= 0) {
            return "0.00B";
        }
        if (filesize < 1024) {
            mstrbuf.append(filesize);
            mstrbuf.append(" B");
        } else if (filesize < 1048576) {
            mstrbuf.append(df.format((double) filesize / 1024));
            mstrbuf.append(" K");
        } else if (filesize < 1073741824) {
            mstrbuf.append(df.format((double) filesize / 1048576));
            mstrbuf.append(" M");
        } else {
            mstrbuf.append(df.format((double) filesize / 1073741824));
            mstrbuf.append(" G");
        }
        return mstrbuf.toString();
    }

    public static String getFileSize1(long filesize) {
        StringBuffer mstrbuf = new StringBuffer();
        if (filesize <= 0) {
            return "0.0B";
        }
        if (filesize < 1024) {
            mstrbuf.append(filesize);
            mstrbuf.append("B");
        } else if (filesize < 1048576) {
            mstrbuf.append(df1.format((double) filesize / 1024));
            mstrbuf.append("K");
        } else if (filesize < 1073741824) {
            mstrbuf.append(df1.format((double) filesize / 1048576));
            mstrbuf.append("M");
        } else {
            mstrbuf.append(df1.format((double) filesize / 1073741824));
            mstrbuf.append("G");
        }
        return mstrbuf.toString();
    }

    public static String getFileSize2(long filesize) {
        StringBuffer mstrbuf = new StringBuffer();
        if (filesize <= 0) {
            return "0";
        }
        if (filesize < 1024) {
            mstrbuf.append(filesize);
        } else if (filesize < 1048576) {
            mstrbuf.append(df2.format((double) filesize / 1024));
        } else if (filesize < 1073741824) {
            mstrbuf.append(df2.format((double) filesize / 1048576));
        } else {
            mstrbuf.append(df.format((double) filesize / 1073741824));
        }
        return mstrbuf.toString();
    }

    public static String getFileSize3(long filesize) {
        StringBuffer mstrbuf = new StringBuffer();
        if (filesize <= 0) {
            return "0.0";
        }
        if (filesize < 1024) {
            mstrbuf.append(filesize);
        } else if (filesize < 1048576) {
            if (((int) (filesize / 1024)) < 10) {
                mstrbuf.append(df.format((double) filesize / 1024));
            } else if (((int) (filesize / 1024)) < 100) {
                mstrbuf.append(df1.format((double) filesize / 1024));
            } else {
                mstrbuf.append(df2.format((double) filesize / 1024));
            }
        } else if (filesize < 1073741824) {
            if (((int) (filesize / 1048576)) < 10) {
                mstrbuf.append(df.format((double) filesize / 1048576));
            } else if (((int) (filesize / 1048576)) < 100) {
                mstrbuf.append(df1.format((double) filesize / 1048576));
            } else {
                mstrbuf.append(df2.format((double) filesize / 1048576));
            }
        } else {
            mstrbuf.append(df.format((double) filesize / 1073741824));
        }
        return mstrbuf.toString();
    }

    public static String getFileSize4(long filesize) {
        StringBuffer mstrbuf = new StringBuffer();
        if (filesize <= 0) {
            return "0B";
        }
        if (filesize < 1024) {
            mstrbuf.append(filesize);
            mstrbuf.append("B");
        } else if (filesize < 1048576) {
            mstrbuf.append(df2.format((double) filesize / 1024));
            mstrbuf.append("K");
        } else if (filesize < 1073741824) {
            mstrbuf.append(df2.format((double) filesize / 1048576));
            mstrbuf.append("M");
        } else {
            mstrbuf.append(df2.format((double) filesize / 1073741824));
            mstrbuf.append("G");
        }
        return mstrbuf.toString();
    }

    public static int dp2px(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }

    public static List<String> getLauncherPkgs(Context context) {
        List<String> packageNames = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null) {
                packageNames.add(resolveInfo.activityInfo.processName);
                packageNames.add(resolveInfo.activityInfo.packageName);
                packageNames.add("com.miui.core");
            }
        }
        return packageNames;
    }

    public static int getMemory(Context context) {
        long ram_kongxian = MemoryManager.getPhoneFreeRamMemory(context);
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        int memory = (int) ((ram_all - ram_kongxian) * 100 / ram_all);
        return memory;
    }

    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i].getAbsolutePath());
                }
                if (file.listFiles().length == 0) {
                    file.delete();
                }
            } else {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取进程name
    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            File fileList[] = file.listFiles();
            if (fileList != null) {
                int count = 0;
                for (int i = 0; i < fileList.length; i++) {
                    if (++count > 25) {
                        continue;
                    }
                    if (fileList[i].isDirectory()) {
                        Log.e("aaa", "bbb" + fileList[i].toString());
                        size = size + getFileSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }
        } else {
            size += file.length();
        }
        return size;
    }

    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
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

    //是否安装该应用
    public boolean isPkgInstalled(String pkgName, PackageManager pm) {
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

    //判断是否有虚拟按键
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }



}
