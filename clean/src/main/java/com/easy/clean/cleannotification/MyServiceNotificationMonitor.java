package com.easy.clean.cleannotification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.easy.clean.core.CleanManager;
import com.easy.clean.easydb.CleanDBHelper;
import com.easy.clean.easyutils.PreData;

import java.util.List;

/**
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyServiceNotificationMonitor extends NotificationListenerService {
    protected PackageManager pm;
    public static final String NOTIFI_WHILT_LIST = "NOTIFI_WHILT_LIST";//通知栏清理白名单

    public static final String KEY_NOTIFI = "KEY_NOTIFI";//notifi


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        analysisSbn(sbn);
    }

    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationRemoved(sbn, rankingMap);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void analysisSbn(StatusBarNotification sbn) {
        if (!PreData.getDB(this, KEY_NOTIFI, true) || !sbn.isClearable()) {
            return;
        }
        String pkg = sbn.getPackageName();
        if (null == pkg) {
            return;
        }
        List<String> notifi_white = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.Notification);
        if (notifi_white.contains(pkg)) {
            return;
        }
        Notification notification = sbn.getNotification();
        RemoteViews re = notification.contentView;
        int id = sbn.getId();
        String tag = sbn.getTag();
        Bundle extras = sbn.getNotification().extras;
        long time = sbn.getPostTime();
        Drawable icon;
        icon = extras.getParcelable(Notification.EXTRA_SMALL_ICON);
        if (icon == null) {
            try {
                icon = pm.getApplicationIcon(pkg);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        String notificationTitle = extras.getString(Notification.EXTRA_TITLE);

        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

        String notificationText = null;
        String notificationSubText = null;

        if (text != null) {
            notificationText = text.toString();
        }

        if (subText != null) {
            notificationSubText = subText.toString();
        }

        if (TextUtils.isEmpty(notificationTitle)) {
            try {
                notificationTitle = (String) pm.getApplicationLabel(pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        NotificationInfo notifiInfo = new NotificationInfo(pkg, icon, notificationTitle, notificationText, time, id, re);
        CleanManager.getInstance(this).notificationChanged(notifiInfo, true);

        if (Build.VERSION.SDK_INT > 21) {
            cancelNotification(sbn.getKey());
        } else {
            cancelNotification(pkg, tag, id);
        }
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            StatusBarNotification[] sbns = new StatusBarNotification[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                sbns = getActiveNotifications();
            }
            if (sbns == null || sbns.length == 0 || PreData.getDB(this, KEY_NOTIFI, true)) {
                return super.onStartCommand(intent, flags, startId);
            }
            for (StatusBarNotification sbn : sbns) {
                analysisSbn(sbn);
            }
        } catch (Exception e) {

        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pm = getPackageManager();

    }
}
