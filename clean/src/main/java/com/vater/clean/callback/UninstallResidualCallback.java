package com.vater.clean.callback;

import com.vater.clean.entity.JunkInfo;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/15.
 */

public abstract class UninstallResidualCallback {
    public abstract void loadFinished(ArrayList<JunkInfo> uninstallResiduals, long totalSize);
}