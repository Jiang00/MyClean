package com.android.clean.callback;

import com.android.clean.filemanager.FileInfo;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/15.
 */

public abstract class FileInfoCallBack {
    public abstract void loadFinished(ArrayList<FileInfo> appInfoList, long totalSize) ;
}
