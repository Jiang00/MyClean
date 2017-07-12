package com.icleaner.junk.interfaceview;

import com.icleaner.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface CustomRamView extends MyBaseView {
    void initData(long allSize);

    void setColor(int memory, long allSize);

    void addRamdata(long size, List<JunkInfo> list);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(List<JunkInfo> cleanList, long cleanSize);


}
