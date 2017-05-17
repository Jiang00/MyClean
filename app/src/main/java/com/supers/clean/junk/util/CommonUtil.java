package com.supers.clean.junk.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.client.AndroidSdk;
import com.eos.manager.AccessibilityService;
import com.supers.clean.junk.R;

import java.io.DataOutputStream;
import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonUtil {

    public static void track(String category, String action, String label, int value) {
        if (com.supers.clean.junk.BuildConfig.TRACK) {
            AndroidSdk.track(category, action, label, value);
        }
    }

    public static void log(String tag, String msg) {
        if (com.supers.clean.junk.BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

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

    //多少天
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


    public static String convertStorage(long size, boolean danwei) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return danwei ? String.format("%.1f", (float) size / gb) + "GB" : String.format("%.1f", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return danwei ? String.format(f > 100 ? "%.0f" : "%.1f", f) + "MB" : String.format(f > 100 ? "%.0f" : "%.1f", f);
        } else {
            float f = (float) size / kb;
            return danwei ? String.format(f > 100 ? "%.0f" : "%.1f", f) + "KB" : String.format(f > 100 ? "%.0f" : "%.1f", f);
        }
//        else {
//            float f = (float) size;
//            return danwei ? String.format(f > 100 ? "%.0f" : "%.1f", f) + "B" : String.format(f > 100 ? "%.0f" : "%.1f", f);
//        }
    }

    public static String convertStorageWifi(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f", (float) size / gb) + "GB/s";
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f" : "%.1f", f) + "MB/s";
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f" : "%.1f", f) + "KB/s";
        } else {
            return String.format("%d", size) + "B/s";
        }
    }

    public static String convertStorageDanwei(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return "GB";
        } else if (size >= mb) {
            return "MB";
        } else {
            float f = (float) size / kb;
            return "KB";
        }
//        else
//            return "B";
    }

    public static int dp2px(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }

    public static Bitmap getBitmap(Drawable drawable) {
        // TODO Auto-generated method stub
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
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

    //判断应用是否自动重启(开机自启)
    public static boolean isStartSelf(PackageManager pm, String packageName) {
        if (TextUtils.equals(packageName, "com.facebook.katana") ||
                TextUtils.equals(packageName, "com.android.vending") ||
                packageName.contains("com.google.android")) {
            return false;
        }
        final Intent intent = new Intent("android.intent.action.BOOT_COMPLETED");
        intent.setPackage(packageName);
        //检索所有可用于给定的意图进行的活动。如果没有匹配的活动，则返回一个空列表。
        List<ResolveInfo> list = pm.queryBroadcastReceivers(intent,
                0);
        return list.size() > 0;
    }

    //根据包名启动程序
    public static void doStartApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveinfoList.size() == 0) {
            return;
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
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

    //布局转bitmap
    public static Bitmap getViewBitmap(View view) {
        if (null == view) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, CommonUtil.dp2px(60), CommonUtil.dp2px(60));
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
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

    //获取程序 图标
    public static Drawable getAppIcon(String packname, PackageManager pm) {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //获取程序 label
    public static String getAppLabel(String packname, PackageManager pm) {
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    //是否获取通知权限
    public static boolean isNotificationListenEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // To check if service is enabled是否获取无障碍权限
    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + AccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        } else {
        }
        return false;
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
        return ContextCompat.getDrawable(context, R.mipmap.file_apk_icon);
    }

    //是否安装该应用
    public static boolean isPkgInstalled(String pkgName, PackageManager pm) {
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
//            ViewParent viewParent = nativeView.getParent();
//            if (viewParent != null && viewParent instanceof ViewGroup) {
//                ((ViewGroup) viewParent).removeAllViews();
//            }
            ViewGroup viewParent = (ViewGroup) nativeView.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
            }
        }
        return nativeView;
    }

    /**
     * 判断手机是否ROOT
     */
    public static boolean isRoot() {

        boolean root = false;

        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                root = false;
            } else {
                root = true;
            }

        } catch (Exception e) {
        }

        Log.e("rqy", "get root--" + root);
        return root;
    }

    /**
     * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限)
     *
     * @param command 命令： String apkRoot="chmod 777 "+getPackageCodePath();
     *                RootCommand(apkRoot);
     * @return 应用程序是/否获取Root权限
     */
    public static boolean RootCommand(String command) {

        Process process = null;
        DataOutputStream os = null;

        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Log.e("rqy", "ROOT REE" + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }

        Log.e("rqy", "Root SUC ");
        return true;

    }


}
