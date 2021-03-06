package com.supers.clean.junk.view;

import android.widget.TextView;


import com.android.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkView extends IView {
    void initData(long allSize);

    void setColor(long allSize);

    void addSystemdata(long size, List<JunkInfo> list);

    void addApkdata(long size, List<JunkInfo> list);

    void addUnloaddata(long size, List<JunkInfo> list);

    void addLogdata(long size, List<JunkInfo> list);

    void addUserdata(long size, List<JunkInfo> list);

    void setUnit(long size, TextView textView);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(boolean isZhankai, List<JunkInfo> cleanList, long cleanSize);

}
