package com.android.clean.entity;

/**
 * Created by renqingyou on 2017/5/12.
 */

public class AppInfo {
    public String pkgName;
    public long pkgCacheSize;

    @Override
    public String toString() {
        return "AppInfo{" +
                "pkgName='" + pkgName + '\'' +
                ", pkgCacheSize=" + pkgCacheSize +
                '}';
    }
}
