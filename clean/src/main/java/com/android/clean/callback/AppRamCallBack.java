package com.android.clean.callback;

import com.android.clean.entity.AppRam;

import java.util.List;

/**
 * Created by renqingyou on 2017/5/16.
 */

public abstract class AppRamCallBack {
    public abstract void loadFinished(List<AppRam> appRamList, List<String> whiteList, long totalSize);
}
