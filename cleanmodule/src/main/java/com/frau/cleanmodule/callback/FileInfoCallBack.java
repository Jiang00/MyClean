package com.frau.cleanmodule.callback;


import com.frau.cleanmodule.entity.JunkInfo;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/15.
 */

public abstract class FileInfoCallBack {
    public abstract void loadFinished(ArrayList<JunkInfo> appInfoList, long totalSize) ;
}
