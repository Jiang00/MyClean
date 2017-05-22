package com.supers.clean.junk.view;

import com.android.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface RamView extends IView {
    void initData(long allSize);

    void setColor(int memory, long allSize);

    void addRamdata(long size, List<JunkInfo> list);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(List<JunkInfo> cleanList, long cleanSize);


}
