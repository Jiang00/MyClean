package com.icleaner.clean.zcallback;

import com.icleaner.clean.entity.JunkInfo;

import java.util.List;

/**
 * Created by renqingyou on 2017/5/16.
 */

public abstract class AppRamCallBack {
    public abstract void loadFinished(List<JunkInfo> appRamList, List<String> whiteList, long totalSize);
}