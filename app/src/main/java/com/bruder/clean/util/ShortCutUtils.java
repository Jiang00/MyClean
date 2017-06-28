package com.bruder.clean.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;

import com.cleaner.util.DataPre;
import com.bruder.clean.junk.R;
import com.bruder.clean.activity.GBoostingActivity;


/**
 */
public class ShortCutUtils {

    // Action 添加Shortcut
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    // Action 移除Shortcut
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

    /**
     * 添加当前应用的桌面快捷方式
     *
     * @param cx
     */
    public static void addShortcut(Activity cx) {
        final Intent launchIntent = cx.getIntent();
        final String action = launchIntent.getAction();
        Intent shortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        Intent shortcutIntent = new Intent();
        shortcutIntent.setComponent(new ComponentName(cx.getPackageName(),
                "com.supers.clean.activity.ShortCutingActivity"));
        //设置启动的模式
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        String title = cx.getResources().getString(R.string.short_cut);

        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 不允许重复创建（不一定有效）
        shortcut.putExtra("duplicate", false);
        // 快捷方式的图标
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(cx,
                R.mipmap.short_cut_icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
        if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
            cx.setResult(cx.RESULT_OK, shortcut);
            cx.finish();
        } else {
            if (DataPre.getDB(cx, Constant.KEY_SHORTCUT, true)) {
                cx.sendBroadcast(shortcut);
                DataPre.putDB(cx, Constant.KEY_SHORTCUT, false);
            }
        }
    }

    /**
     * 添加当前应用的桌面快捷方式
     *
     * @param cx
     */
    public static void addGBoost(Activity cx, Bitmap bitmap) {
        final Intent launchIntent = cx.getIntent();
        final String action = launchIntent.getAction();
        Intent shortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
//        shortcutIntent.setComponent(new ComponentName(cx.getPackageName(),
//                "com.supers.clean.activity.GBoostingActivity"));
        shortcutIntent.setClass(cx, GBoostingActivity.class);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //设置启动的模式
        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_NEW_TASK);

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        String title = cx.getResources().getString(R.string.gboost_0);

        // 快捷方式名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 不允许重复创建（不一定有效）
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
        cx.sendBroadcast(shortcut);
    }

    public static void addShortcut(Activity context, Intent actionIntent, String name,
                                   boolean allowRepeat, Bitmap iconBitmap) {
        Intent addShortcutIntent = new Intent(ACTION_ADD_SHORTCUT);
        // 是否允许重复创建
        addShortcutIntent.putExtra("duplicate", false);
        // 快捷方式的标题
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // 快捷方式的图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, iconBitmap);
        // 快捷方式的动作
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
        context.getApplicationContext().sendBroadcast(addShortcutIntent);
    }

    public static void removeShortcut(Context context, Intent actionIntent, String name) {
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("duplicate", false);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
        context.getApplicationContext().sendBroadcast(intent);
    }


    /**
     * 删除当前应用的桌面快捷方式
     *
     * @param cx
     */
//    public static void delShortcut(Context cx) {
//        Intent shortcut = new Intent(
//                "com.android.launcher.action.UNINSTALL_SHORTCUT");
//
//
//        String title = cx.getResources().getString(R.string.lab);
//
//        // 快捷方式名称
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
//        Intent shortcutIntent=new Intent();
//        shortcutIntent.setComponent(new ComponentName(cx.getPackageName(),
//                cx.getPackageName() + "."
//                        + AnimationActivity.class.getSimpleName()));
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//        cx.sendBroadcast(shortcut);
//    }
//
//    /**
//     * 判断当前应用在桌面是否有桌面快捷方式
//     *
//     * @param
//     */
//
//    private static String AUTHORITY = null;
//
//    public static boolean isShortCutExist(Context context, String title) {
//
//        boolean isInstallShortcut = false;
//
//        if (null == context || TextUtils.isEmpty(title))
//            return isInstallShortcut;
//
//        if (TextUtils.isEmpty(AUTHORITY))
//            AUTHORITY = getAuthorityFromPermission(context);
//
//        final ContentResolver cr = context.getContentResolver();
//
//        if (!TextUtils.isEmpty(AUTHORITY)) {
//            try {
//                final Uri CONTENT_URI = Uri.parse(AUTHORITY);
//
//                Cursor qiantai = cr.query(CONTENT_URI, new String[] { "title",
//                                "icon" }, "title=?", new String[] { title },
//                        null);
//
//                // XXX表示应用名称。
//                if (qiantai != null && qiantai.getCount() > 0) {
//                    isInstallShortcut = true;
//                }
//                if (null != qiantai && !qiantai.isClosed())
//                    qiantai.close();
//            } catch (Exception e) {
//                // TODO: handle exception
//                Log.e("isShortCutExist", e.getMessage());
//            }
//
//        }
//        return isInstallShortcut;
//
//    }
//
//    public static String getCurrentLauncherPackageName(Context context) {
//
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        ResolveInfo res = context.getPackageManager()
//                .resolveActivity(intent, 0);
//        if (res == null || res.activityInfo == null) {
//            // should not happen. A home is always installed, isn't it?
//            return "";
//        }
//        if (res.activityInfo.packageName.equals("android")) {
//            return "";
//        } else {
//            return res.activityInfo.packageName;
//        }
//    }
//
//    public static String getAuthorityFromPermissionDefault(Context context) {
//
//        return getThirdAuthorityFromPermission(context,
//                "com.android.launcher.permission.READ_SETTINGS");
//    }
//
//    public static String getThirdAuthorityFromPermission(Context context,
//                                                         String permission) {
//        if (TextUtils.isEmpty(permission)) {
//            return "";
//        }
//
//        try {
//            List<PackageInfo> packs = context.getPackageManager()
//                    .getInstalledPackages(PackageManager.GET_PROVIDERS);
//            if (packs == null) {
//                return "";
//            }
//            for (PackageInfo pack : packs) {
//                ProviderInfo[] providers = pack.providers;
//                if (providers != null) {
//                    for (ProviderInfo provider : providers) {
//                        if (permission.equals(provider.readPermission)
//                                || permission.equals(provider.writePermission)) {
//                            if (!TextUtils.isEmpty(provider.authority)// 精准匹配launcher.settings，再一次验证
//                                    && (provider.authority)
//                                    .contains(".launcher.settings"))
//                                return provider.authority;
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    public static String getAuthorityFromPermission(Context context) {
//        // 获取默认
//        String authority = getAuthorityFromPermissionDefault(context);
//        // 获取特殊第三方
//        if (authority == null || authority.trim().equals("")) {
//            String packageName = getCurrentLauncherPackageName(context);
//            packageName += ".permission.READ_SETTINGS";
//            authority = getThirdAuthorityFromPermission(context, packageName);
//        }
//        // 还是获取不到，直接写死
//        if (TextUtils.isEmpty(authority)) {
//            int sdkInt = android.os.Build.VERSION.SDK_INT;
//            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
//                authority = "com.android.launcher.settings";
//            } else if (sdkInt < 19) {// Android 4.4以下
//                authority = "com.android.launcher2.settings";
//            } else {// 4.4以及以上
//                authority = "com.android.launcher3.settings";
//            }
//        }
//        authority = "content://" + authority + "/favorites?notify=true";
//        return authority;
//
//    }

//    public static boolean hasShortcut(Context context, String lableName,String launcherPkgName) {
//
//        String url = "";
//        url = "content://" + launcherPkgName + ".settings/favorites?notify=true";
//
//        ContentResolver resolver = context.getContentResolver();
//        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
//                new String[] { lableName }, null);
//
//        if (cursor == null) {
//            return false;
//        }
//
//        if (cursor.getCount()>0) {
//            cursor.close();
//            return true;
//        }else {
//            cursor.close();
//            return false;
//        }
//    }
//    public static String getLauncherPkgName(Context context) {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
//        for (ActivityManager.RunningAppProcessInfo info: list) {
//            String pkgName = info.processName;
//            if (pkgName.contains("launcher") && pkgName.contains("android")) {
//                return pkgName;
//            }
//
//        }
//        return null;
//    }


    //快捷方式
//    private void shortcut() {
//
//        final Intent launchIntent = getIntent();
//        final String action = launchIntent.getAction();
//
//        Intent shortcutIntent = new Intent();
//        //设置点击快捷方式时启动的Activity,因为是从Lanucher中启动，所以包名类名要写全。
//        shortcutIntent.setComponent(new ComponentName(getPackageName(),
//                getPackageName() + "."
//                        + AnimationActivity.class.getSimpleName()));
//        //设置启动的模式
//        shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//                | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        Intent resultIntent = new Intent();
//        //设置快捷方式图标
//        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
//                Intent.ShortcutIconResource.fromContext(this,
//                        R.drawable.shortcut_proc_clean));
//        //启动的Intent
//        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
//        //设置快捷方式的名称
//        resultIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
//                getString(R.string.lab));
//        resultIntent.putExtra("duplicate",false);
//        // 长按方式添加快捷方式----->即被动的Action方式。
//        if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
//            setResult(RESULT_OK, resultIntent);
//            finish();
//        } else {
//            // 发送广播方式添加快捷方式----->即主动的发广播方式
//            resultIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//            sendBroadcast(resultIntent);
//
//        }
//    }
}
