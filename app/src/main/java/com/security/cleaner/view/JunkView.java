package com.security.cleaner.view;

import android.widget.TextView;


import com.security.mcleaner.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkView extends BaseView {
    void initData(long allSize);

    void addSystemdata(long size, List<JunkInfo> list);

    void setColor(long allSize);

    void addApkdata(long size, List<JunkInfo> list);

    void addUnloaddata(long size, List<JunkInfo> list);

    void addLogdata(long size, List<JunkInfo> list);

    void setUnit(long size, TextView textView);

    void addUserdata(long size, List<JunkInfo> list);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(boolean isZhankai, List<JunkInfo> cleanList, long cleanSize);

}
