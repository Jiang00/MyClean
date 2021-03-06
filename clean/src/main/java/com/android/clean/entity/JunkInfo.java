package com.android.clean.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/5/19.
 */

public class JunkInfo {

    public enum TableType {
        APKFILE, LOGFILE, APP
    }

    public String path;
    public String pkg;
    public String processName;
    public int pid;
    public long size;
    public long allSize;
    public long date;
    public long lastRunTime;
    public List<Integer> pids = new ArrayList<>();

    public String label;
    public boolean isChecked = true;
    public boolean isSelfBoot;
    public boolean isnotifiWhiteList;
    public boolean isWhiteList;
    public TableType type;

    public JunkInfo() {
    }

    public JunkInfo(String path, String packageName, long size) {
        this.path = path;
        this.pkg = packageName;
        this.size = size;
    }

    public JunkInfo(String path, String label, long time, long size) {
        super();
        this.path = path;
        this.label = label;
        this.date = time;
        this.size = size;
    }

    public JunkInfo(String pkg, String label, String path, long size) {
        super();
        this.pkg = pkg;
        this.label = label;
        this.path = path;
        this.size = size;
    }
}
