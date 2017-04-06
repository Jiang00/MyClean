package com.supers.clean.junk.view;

import com.supers.clean.junk.entity.JunkInfo;

import java.util.List;

/**
 */

public interface AppManagerView extends IView {
    void initData(long allSize);

    void addAppManagerdata(List<JunkInfo> list);

    void setCleanDAta(long size);

    void reStart();
}
