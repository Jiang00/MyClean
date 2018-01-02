package com.security.mcleaner.notification;

import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;

/**
 */

public class NotificationInfo {

    public String pkg;
    public Drawable icon;
    public String title;
    public String subTitle;
    public long time;
    public int id;
    public RemoteViews remoteViews;

    public NotificationInfo(String pkg, Drawable icon, String title, String subTitle, long time, int id, RemoteViews remoteViews) {
        this.pkg = pkg;
        this.icon = icon;
        this.title = title;
        this.subTitle = subTitle;
        this.time = time;
        this.id = id;
        this.remoteViews = remoteViews;
    }

    public NotificationInfo(String pkg, RemoteViews remoteViews) {
        this.pkg = pkg;
        this.remoteViews = remoteViews;

    }

}
