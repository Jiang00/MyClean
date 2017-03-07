package com.supers.clean.junk.View;

import android.widget.TextView;

import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.List;

/**
 * Created by Ivy on 2017/3/2.
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

    void setCleanDAta(long size);

    void cleanAnimation(boolean isZhankai, List<JunkInfo> cleanList, long cleanSize);


}
