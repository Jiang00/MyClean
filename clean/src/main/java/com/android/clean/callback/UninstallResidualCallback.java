package com.android.clean.callback;

import com.android.clean.entity.UninstallResidual;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/15.
 */

public abstract class UninstallResidualCallback {
    public abstract void loadFinished(ArrayList<UninstallResidual> uninstallResiduals, long totalSize);
}
