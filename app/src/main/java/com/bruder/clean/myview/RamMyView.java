package com.bruder.clean.myview;

import com.cleaner.entity.JunkInfo;

import java.util.List;

/**
 */

public interface RamMyView extends Bruderiew {
    void initData(long allSize);

    void setColor(int memory, long allSize);

    void addRamdata(long size, List<JunkInfo> list);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(List<JunkInfo> cleanList, long cleanSize);


}
