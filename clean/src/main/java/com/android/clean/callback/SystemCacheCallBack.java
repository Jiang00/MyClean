package com.android.clean.callback;

import com.android.clean.entity.AppInfo;

import java.util.Vector;

/**
 * Created by renqingyou on 2017/5/12.
 */

public abstract class SystemCacheCallBack {
    public abstract void loadFinished(Vector<AppInfo> appInfoList, long totalSize);
}
