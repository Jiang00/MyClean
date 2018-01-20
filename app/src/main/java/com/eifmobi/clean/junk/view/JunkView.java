package com.eifmobi.clean.junk.view;

import android.widget.TextView;


import com.eifmobi.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkView extends IView {
    void addSystemdata(long size, List<JunkInfo> list);

    void initData(long allSize);

    void setColor(long allSize);

    void addApkdata(long size, List<JunkInfo> list);

    void addUnloaddata(long size, List<JunkInfo> list);

    void addLogdata(long size, List<JunkInfo> list);

    void addUserdata(long size, List<JunkInfo> list);

    void setUnit(long size, TextView textView);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(boolean isZhankai, List<JunkInfo> cleanList, long cleanSize);

}
