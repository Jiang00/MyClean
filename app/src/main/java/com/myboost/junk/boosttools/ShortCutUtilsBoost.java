package com.myboost.junk.boosttools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;

import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.boostactivity.ShortCutingActivityBoost;

public class ShortCutUtilsBoost {

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
                ShortCutingActivityBoost.class.getName()));
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
            if (PreData.getDB(cx, BoostMyConstant.KEY_SHORTCUT, true)) {
                cx.sendBroadcast(shortcut);
                PreData.putDB(cx, BoostMyConstant.KEY_SHORTCUT, false);
            }
        }
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
}
