package com.vater.clean.junk.view;

import com.vater.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface MRamView extends BaseView {
    void initData(long allSize);

    void setColor(int memory, long allSize);

    void addRamdata(long size, List<JunkInfo> list);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(List<JunkInfo> cleanList, long cleanSize);


}
