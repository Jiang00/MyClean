package com.eifmobi.clean.callback;

import com.eifmobi.clean.entity.JunkInfo;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/12.
 */

public abstract class SystemCacheCallBack {
    public abstract void loadFinished(ArrayList<JunkInfo> appInfoList, long totalSize);
}
