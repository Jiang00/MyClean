package com.privacy.junk.interfaceviewprivacy;

import android.widget.TextView;


import com.privacy.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface GarbageViewPrivacy extends BaseView {
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