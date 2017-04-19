package com.supers.clean.junk.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.supers.clean.junk.activity.MyApplication;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.entity.NotifiInfo;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/4/14.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationMonitor extends NotificationListenerService {
    MyApplication myApplication;
    protected PackageManager pm;

    public static final String NOTIFI_ACTION = "action_notifi_postd";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.e("notifi", "nitifilistenerPosted");
        analysisSbn(sbn);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void analysisSbn(StatusBarNotification sbn) {
        if (!PreData.getDB(this, Constant.KEY_NOTIFI, true)) {
            return;
        }
        Notification notification = sbn.getNotification();
        RemoteViews re = notification.contentView;
        String pkg = sbn.getPackageName();
        List<String> notifi_white = PreData.getNameList(this, Constant.NOTIFI_WHILT_LIST);
        if (!sbn.isClearable() || null == pkg || notifi_white.contains(pkg)) {
            return;
        }
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
        String notificationText = (String) extras.getCharSequence(Notification.EXTRA_TEXT);
        String notificationSubText = (String) extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        if (TextUtils.isEmpty(notificationTitle)) {
            try {
                notificationTitle = (String) pm.getApplicationLabel(pm.getApplicationInfo(pkg, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        ArrayList<NotifiInfo> list = myApplication.getNotifiList();
        for (NotifiInfo info : list) {
            if (TextUtils.equals(info.pkg, pkg) && info.id == id) {
                Log.e("notifi", "break");
                list.remove(info);
                break;
            }
        }
        list.add(new NotifiInfo(pkg, icon, notificationTitle, notificationText, time, id, re));
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(NOTIFI_ACTION));
        Log.e("notifi", notificationTitle + "=" + notificationText + "=" + notificationSubText);
        if (Build.VERSION.SDK_INT > 21) {
            cancelNotification(sbn.getKey());
        } else {
            cancelNotification(pkg, tag, id);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("notifi", "nitifilistenerStartCommand");
        try {
            StatusBarNotification[] sbns = new StatusBarNotification[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                sbns = getActiveNotifications();
            }
            if (sbns == null || sbns.length == 0 || PreData.getDB(this, Constant.KEY_NOTIFI, true)) {
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
    public void onCreate() {
        super.onCreate();
        myApplication = (MyApplication) getApplication();
        Log.e("notifi", "nitifilistenerCreate");
        pm = getPackageManager();

    }

    @Override
    public void onDestroy() {
        Log.e("notifi", "nitifilistenerDestroy");
        super.onDestroy();
    }
}
