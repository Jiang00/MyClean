package com.android.clean.entity;

/**
 * Created by renqingyou on 2017/5/16.
 */

public class AppRam {
    public String pkg;
    public long size;
    public boolean isSelfBoot;

    @Override
    public String toString() {
        return "AppRam{" +
                "pkg='" + pkg + '\'' +
                ", size=" + size +
                ", isSelfBoot=" + isSelfBoot +
                '}';
    }
}
