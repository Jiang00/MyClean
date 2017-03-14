package com.supers.clean.junk.modle;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.client.AndroidSdk;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonUtil {


    public static String getStrTime(long time) {
        long a = System.currentTimeMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yy-MM-dd HH:mm:ss ");
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

    public static int millTransFate(long millisecond) {
        String str = "";
        long day = millisecond / 86400000;
//        long hour = (millisecond % 86400000) / 3600000;
//        long minute = (millisecond % 86400000 % 3600000) / 60000;
//        if (day > 0) {
//            str = String.valueOf(day) + "天";
//        }
//        if (hour > 0) {
//            str += String.valueOf(hour) + "小时";
//        }
//        if (minute > 0) {
//            str += String.valueOf(minute) + "分钟";
//        }
        return (int) day;
    }

    public static String millTransFate2(long millisecond) {
        String str = "";
        long day = millisecond / 86400000;
        long hour = (millisecond % 86400000) / 3600000;
        long minute = (millisecond % 86400000 % 3600000) / 60000;
        if (day > 0) {
            str = String.valueOf(day) + " d ";
        }
        if (hour > 0) {
            str += String.valueOf(hour) + " h ";
        }
        if (minute > 0) {
            str += String.valueOf(minute) + " m ";
        }
        return str;
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

    public static String getFileSizeKongge(long filesize) {
        StringBuffer mstrbuf = new StringBuffer();
        if (filesize <= 0) {
            return " 0 ";
        }
        if (filesize < 1024) {
            mstrbuf.append(filesize);
        } else if (filesize < 1048576) {
            mstrbuf.append(" " + df2.format((double) filesize / 1024) + " ");
        } else if (filesize < 1073741824) {
            mstrbuf.append(" " + df2.format((double) filesize / 1048576) + " ");
        } else {
            mstrbuf.append(" " + df.format((double) filesize / 1073741824) + " ");
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
            mstrbuf.append(df1.format((double) filesize / 1024));
        } else if (filesize < 1073741824) {
            mstrbuf.append(df1.format((double) filesize / 1048576));
        } else {
            mstrbuf.append(df1.format((double) filesize / 1073741824));
        }
        return mstrbuf.toString();
    }

    public static String getFileSize4(long filesize) {
        StringBuffer mstrbuf = new StringBuffer();
        if (filesize <= 0) {
            return "";
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

    public static long getFirstInstallTime(Context context) {
        long firstInstallTime = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            //应用装时间
            firstInstallTime = packageInfo.firstInstallTime;
            //应用最后一次更新时间
            long lastUpdateTime = packageInfo.lastUpdateTime;
//            Log.e("aaa", "===first install time : " + CommonUtil.getStrTime(firstInstallTime) + " last update time :" + CommonUtil.getStrTime(lastUpdateTime));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return firstInstallTime;
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

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd(tag, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
            Log.e("rqy", "getAdView null,because not configuration tag =" + tag);
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout(tag, AndroidSdk.NATIVE_AD_TYPE_ALL, layout, null);
        if (nativeView == null) {
            Log.e("rqy", "getAdView null,because peek native ad = null");
            return null;
        }

        if (nativeView != null) {
            ViewParent viewParent = nativeView.getParent();
            if (viewParent != null && viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeAllViews();
            }
        }
        return nativeView;
    }

}
