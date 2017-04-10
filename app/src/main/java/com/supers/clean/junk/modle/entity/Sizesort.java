package com.supers.clean.junk.modle.entity;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;
import android.os.Build;

import java.util.Comparator;

/**
 * Created by renqingyou on 2017/4/5.
 */


public class Sizesort implements Comparator<UsageStats> {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public int compare(UsageStats file1, UsageStats file2) {
        //大的在上面
        return file1.getLastTimeUsed() == file2.getLastTimeUsed() ? 0 : (file1.getLastTimeUsed() > file2.getLastTimeUsed() ? -1 : 1);
    }
}