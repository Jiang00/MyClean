package com.android.clean.callback;

import com.android.clean.entity.JunkInfo;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/16.
 */

public abstract class AppCacheCallBack {
    public abstract void loadFinished(ArrayList<JunkInfo> appCaches, long totalSize);
}
