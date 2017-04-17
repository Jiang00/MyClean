package com.supers.clean.junk.entity;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class JunkInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2130502743076062372L;
    public String path;
    public String name;
    public long time;
    public long lastRunTime;
    public boolean isChecked;
    public Drawable icon;
    public long size;
    public String packageName;
    public String label;
    public boolean isWhiteList;
    public boolean isnotifiWhiteList;
    public int drawableRid;
    public int textrid;
    public boolean isStartSelf;


    public JunkInfo(int textrid, int drawableRid, boolean isChecked) {
        this.textrid = textrid;
        this.drawableRid = drawableRid;
        this.isChecked = isChecked;
    }

    public JunkInfo(int textrid, int drawableRid) {
        this.textrid = textrid;
        this.drawableRid = drawableRid;
    }


    //time==packagename
    public JunkInfo(String packageName, String name,
                    boolean isChecked, Drawable icon, long size) {
        super();
        this.name = name;
        this.packageName = packageName;
        this.isChecked = isChecked;
        this.icon = icon;
        this.size = size;
    }

    public JunkInfo(String path, String name, long time,
                    boolean isChecked, Drawable icon, long size) {
        super();
        this.path = path;
        this.name = name;
        this.time = time;
        this.isChecked = isChecked;
        this.icon = icon;
        this.size = size;
    }

    public JunkInfo(Drawable icon, String path, String name, String packageName,
                    boolean isChecked, long size) {
        super();
        this.path = path;
        this.name = name;
        this.packageName = packageName;
        this.isChecked = isChecked;
        this.icon = icon;
        this.size = size;
    }

    public JunkInfo(boolean isChecked, boolean isWhiteList, Drawable drawable, String name,
                    long size, String packageName, boolean isStartSelf) {
        super();
        this.isChecked = isChecked;
        this.isWhiteList = isWhiteList;
        this.icon = drawable;
        this.name = name;
        this.size = size;
        this.packageName = packageName;
        this.isStartSelf = isStartSelf;
    }

    public JunkInfo(Drawable drawable, String lable, String packagename, boolean isWhiteList) {
        this.icon = drawable;
        this.label = lable;
        this.packageName = packagename;
        this.isWhiteList = isWhiteList;
    }

    public JunkInfo(boolean isChecked, Drawable drawable, String lable,
                    String packageName, long size) {
        super();
        this.isChecked = isChecked;
        this.icon = drawable;
        this.label = lable;
        this.packageName = packageName;
        this.size = size;
    }

    public JunkInfo(boolean isChecked, Drawable drawable, String lable,
                    String packageName) {
        super();
        this.isChecked = isChecked;
        this.icon = drawable;
        this.label = lable;
        this.packageName = packageName;
    }

    public JunkInfo(boolean isChecked, Drawable drawable, String label,
                    String packageName, long time, long size, boolean isWhiteList, boolean isnotifiWhiteList, long lastRunTime) {
        super();
        this.isChecked = isChecked;
        this.icon = drawable;
        this.label = label;
        this.packageName = packageName;
        this.time = time;
        this.lastRunTime = lastRunTime;
        this.size = size;
        this.isWhiteList = isWhiteList;
        this.isnotifiWhiteList = isnotifiWhiteList;
    }

}
