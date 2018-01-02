package com.security.cleaner.view;

import com.security.mcleaner.entity.JunkInfo;

import java.util.List;

/**
 */

public interface MRamView extends BaseView {
    void initData(long allSize);

    void addRamdata(long size, List<JunkInfo> list);

    void setColor(int memory, long allSize);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(List<JunkInfo> cleanList, long cleanSize);


}
