package com.android.clean.entity;


/**
 * Created by renqingyou on 2017/5/12.
 */

public class AppInfo {
    public String pkgName;
    public long pkgCacheSize;
    public String label;
    public boolean checked;

    @Override
    public String toString() {
        return "AppInfo{" +
                "pkgName='" + pkgName + '\'' +
                ", pkgCacheSize=" + pkgCacheSize +
                ", label=" + label +
                '}';
    }
}
