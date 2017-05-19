package com.android.clean.entity;

/**
 * Created by renqingyou on 2017/5/15.
 */

public class AppCache {
    public String filePath;
    public String pkg;
    public long size;

    public AppCache() {
    }

    public AppCache(String path, String packageName, long size) {
        this.filePath = path;
        this.pkg = packageName;
        this.size = size;
    }

    @Override
    public String toString() {
        return "AppCache{" +
                "filePath='" + filePath + '\'' +
                ", pkg='" + pkg + '\'' +
                ", size=" + size +
                '}';
    }
}
