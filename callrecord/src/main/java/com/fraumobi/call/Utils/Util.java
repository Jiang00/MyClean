package com.fraumobi.call.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Ivy on 2017/4/26.
 */

public class Util {

    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info;
        try {
            info = packageManager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static boolean isChinese() {
        if (Locale.getDefault().getLanguage().equals("zh") || Locale.getDefault().getLanguage().equals("ZH")) {
            return true;
        }
        return false;
    }

    public static boolean isEnglish() {
        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("EN")) {
            return true;
        }
        return false;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void writeData(Context context, String key, Object value) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(Constants.SHARE_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            if (value instanceof Integer) {
                editor.putInt(key, (int) value).apply();
            } else if (value instanceof String) {
                editor.putString(key, String.valueOf(value)).apply();
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value).apply();
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value).apply();
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value).apply();
            }
        }
    }

    public static Object readData(Context context, String key, Object defaultValue) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(Constants.SHARE_FILE_NAME, Context.MODE_PRIVATE);
            Map<String, ?> map = sp.getAll();
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return defaultValue;
    }

    public static void deleteShareData(Context context, String key) {
        if (context != null) {
            SharedPreferences sp = context.getSharedPreferences(Constants.SHARE_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
        }
    }

    public static String getEnvironmentDirPath() {
        String sdDirPath = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();//获取跟目录
        }
        return sdDirPath;
    }

    public static String resetPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return null;
        }
        String num = phoneNum.replaceAll("^(\\+86)", "");
//        phoneNum = phoneNum.replaceAll("^(86)", "");
        num = num.replaceAll("^(\\+0086)", "");
        num = num.replaceAll("^(\\+01)", "");
//        phoneNum = phoneNum.replaceAll("^(01)", "");
        num = num.replaceAll("^(\\+0001)", "");
        num = num.replaceAll("^(\\+66)", "");
//        phoneNum = phoneNum.replaceAll("^(66)", "");
        num = num.replaceAll("^(\\+0066)", "");
        num = num.replaceAll("^(\\+55)", "");
//        phoneNum = phoneNum.replaceAll("^(55)", "");
        num = num.replaceAll("^(\\+0055)", "");
        num = num.replaceAll("^(\\+52)", "");
//        phoneNum = phoneNum.replaceAll("^(52)", "");
        num = num.replaceAll("^(\\+0052)", "");
        num = num.replaceAll("^(\\+91)", "");
//        phoneNum = phoneNum.replaceAll("^(91)", "");
        num = num.replaceAll("^(\\+0091)", "");
        num = num.replaceAll("-", "");
        num = num.replaceAll(" ", "");
        num = num.trim();
        return num;
    }

    public static String resetPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return null;
        }
        String num = phoneNumber.replaceAll("-", "");
        num = num.replaceAll(" ", "");
        num = num.trim();
        return num;
    }

    public static String getDistanceTime(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
//        String flag;
        if (time1 < time2) {
            diff = time2 - time1;
//            flag="前";
        } else {
            diff = time1 - time2;
//            flag="后";
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        return hour + "h:" + min + "m:" + sec + "s";
    }

    public static String changeToTime(int position) {
        int second = (position % (1000 * 60)) / 1000;
        int min = (position % (1000 * 60 * 60)) / (1000 * 60);
        int hour = (position % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        String strSecond = "00";
        String strMin = "00";
        String strHour = "00";
        if (second != 0) {
            if (second < 10) {
                strSecond = "0" + second;
            } else {
                strSecond = String.valueOf(second);
            }
        }
        if (min != 0) {
            if (min < 10) {
                strMin = "0" + min;
            } else {
                strMin = String.valueOf(min);
            }
        }
        if (hour != 0) {
            if (hour < 10) {
                strHour = "0" + hour;
            } else {
                strHour = String.valueOf(hour);
            }
        }
        return strHour + "h:" + strMin + "m:" + strSecond + "s";
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
