package com.supers.clean.junk.entity;

import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;

/**
 * Created by Ivy on 2017/4/14.
 */

public class NotifiInfo {

    public String pkg;
    public Drawable icon;
    public String title;
    public String subTitle;
    public long time;
    public int id;
    public RemoteViews remoteViews;

    public NotifiInfo(String pkg, Drawable icon, String title, String subTitle, long time, int id, RemoteViews remoteViews) {
        this.pkg = pkg;
        this.icon = icon;
        this.title = title;
        this.subTitle = subTitle;
        this.time = time;
        this.id = id;
        this.remoteViews = remoteViews;
    }

    public NotifiInfo(String pkg, RemoteViews remoteViews) {
        this.pkg = pkg;
        this.remoteViews = remoteViews;

    }

}
