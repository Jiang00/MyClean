package com.supers.clean.junk.modle;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangqi on 16/4/13.
 */
public class UtilGp {

    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static boolean checkAPP(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static void openPlayStore(Context context, String pkg) {
        Intent i = new Intent("android.intent.action.VIEW");
        String url = fixUrl(pkg);
        boolean isGooglePlay = isPlayStoreUrl(url);
        if (isGooglePlay) {
            if (hasPlayStore(context)) {
                launchPlayStore(context, url, i);
            } else {
                launchBrowser(context, url, i);
            }
        } else {
            launchBrowser(context, url, i);
        }

    }


    public static String fixUrl(String url) {
        return url.startsWith("http") ? url : "https://play.google.com/store/apps/details?id=" + url;
    }

    public static boolean isPlayStoreUrl(String url) {
        return url.startsWith("https://play.google.com/store/apps/details?id=");
    }

    public static boolean hasPlayStore(Context context) {
        try {
            int ignore = context.getPackageManager().getApplicationEnabledSetting("com.android.vending");
            return ignore == 0 || ignore == 1;
        } catch (Exception var2) {
            return false;
        }
    }

    public static void launchPlayStore(Context context, String url, Intent i) {
        String marketUrl = "market://details?id=";
        url = url.replace("https://play.google.com/store/apps/details?id=", "market://details?id=");
        i.setPackage("com.android.vending");
        i.setData(Uri.parse(url));
        launchApp(context, i);
    }

    public static void launchApp(Context context, Intent i) {
        try {
            if (context instanceof Activity) {
                context.startActivity(i);
            } else {
                context.startActivity(i.setFlags(268435456));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
//            Toast.makeText(context, var3.getMessage(), 0).show();
        }

    }

    public static void launchBrowser(Context context, String url, Intent i) {
        i.setData(Uri.parse(url));
        String browserPackageName = getDefaultBrowserPackageName(context, i);
        if (browserPackageName != null) {
            i.setPackage(browserPackageName);
        }

        launchApp(context, i);
    }

    public static String getDefaultBrowserPackageName(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List resolveInfos = packageManager.queryIntentActivities(intent, 0);
        if (resolveInfos.size() > 0) {
            ResolveInfo info = (ResolveInfo) resolveInfos.get(0);
            return info.activityInfo.packageName;
        } else {
            return null;
        }
    }

    private void disableThemePackageIfNecessary(ArrayList<String> themes, PackageManager packageManager, String pkg) {
        themes.add(pkg);
        try {
            if (packageManager.getComponentEnabledSetting(new ComponentName(pkg, pkg + ".Explore")) == PackageManager.COMPONENT_ENABLED_STATE_DISABLED)
                return;
            packageManager.setComponentEnabledSetting(new ComponentName(pkg, pkg + ".Explore"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        } catch (Exception | Error ignore) {
        }
    }


}
