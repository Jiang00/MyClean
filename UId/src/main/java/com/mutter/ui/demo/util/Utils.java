package com.upupup.ui.demo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Utils {

    public static void track(String category, String action, String label, int value) {
        try {
            Class androidSdk = Class.forName("com.android.client.AndroidSdk");
            Method method = androidSdk.getMethod("track", String.class, String.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(androidSdk, category, action, label, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void reactionForAction(Context context, String serverData, String pkgName, String action) {

        if (checkoutISAppHasInstalled(context, pkgName)) {
            if (TextUtils.isEmpty(action)) {
                launchApp(context, pkgName);
            } else {
                launchAppByAction(context, action);
            }
        } else {
            openPlayStore(context, pkgName, serverData);
        }
    }

    public static void launchApp(Context context, String packageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void launchAppByAction(Context context, String action) {
        try {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkoutISAppHasInstalled(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo != null) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static SharedPreferences getSharePreferences(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(Constant.SHARE_FILE, Context.MODE_PRIVATE);
    }

    public static boolean isHavePraise(Context context) throws WrapNullPointException {
        SharedPreferences share = getSharePreferences(context);
        if (share == null) {
            throw new WrapNullPointException("context is null");
        }
        return share.getBoolean(Constant.IS_HAVE_PRAISE, false);
    }

    public static void setHavePraise(Context context, boolean state) throws WrapNullPointException {
        SharedPreferences share = getSharePreferences(context);
        if (share == null) {
            throw new WrapNullPointException("context is null");
        }
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(Constant.IS_HAVE_PRAISE, state);
        editor.apply();
    }

    public static int getCurrentCrossIndex(Context context, String tag) throws WrapNullPointException {
        SharedPreferences share = getSharePreferences(context);
        if (share == null) {
            throw new WrapNullPointException("context is null");
        }
        return share.getInt(tag, 0);
    }

    public static void updateCrossIndex(Context context, int index, String tag) throws WrapNullPointException {
        SharedPreferences share = getSharePreferences(context);
        if (share == null) {
            throw new WrapNullPointException("context is null");
        }
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(tag, index);
        editor.apply();
    }

    public static boolean isNewVersion(Context context, int serVersion) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            int curVersion = pi.versionCode;
            if (serVersion > curVersion) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkoutIsNewVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            int curVersion = pi.versionCode;
            SharedPreferences share = getSharePreferences(context);
            if (share == null) {
                return false;
            }
            SharedPreferences.Editor editor = share.edit();
            int preVersion = share.getInt(Constant.VERSION, 1);
            editor.putInt(Constant.VERSION, curVersion);
            editor.apply();
            if (curVersion > preVersion) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void openPlayStore(Context context, String pkg, String serverData) {
        if (serverData != null) {
            String tracker = "";
            try {
                JSONObject jsonObject = new JSONObject(serverData);
                tracker = jsonObject.optString("tracker");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pkg += tracker;
        }
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
            int var2 = context.getPackageManager().getApplicationEnabledSetting("com.android.vending");
            return var2 == 0 || var2 == 1;
        } catch (Exception var21) {
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

}
