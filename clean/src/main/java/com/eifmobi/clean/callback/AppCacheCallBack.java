package com.eifmobi.clean.callback;

import com.eifmobi.clean.entity.JunkInfo;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/16.
 */

public abstract class AppCacheCallBack {
    public abstract void loadFinished(ArrayList<JunkInfo> appCaches, long totalSize);
}