package com.supers.clean.junk.View;

import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.List;

/**
 * Created by Ivy on 2017/3/2.
 */

public interface AppManagerView extends IView {
    void initData(long allSize);

    void addAppManagerdata(List<JunkInfo> list);

    void setCleanDAta(long size);

}
