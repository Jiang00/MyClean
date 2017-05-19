package com.android.clean.callback;

import com.android.clean.entity.AppInfo;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/12.
 */

public abstract class SystemCacheCallBack {
    public abstract void loadFinished(ArrayList<AppInfo> appInfoList, long totalSize);
}
