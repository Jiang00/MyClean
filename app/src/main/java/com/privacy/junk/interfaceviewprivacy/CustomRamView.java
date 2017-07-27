package com.privacy.junk.interfaceviewprivacy;

import com.privacy.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface CustomRamView extends BaseView {
    void initData(long allSize);

    void setColor(int memory, long allSize);

    void addRamdata(long size, List<JunkInfo> list);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(List<JunkInfo> cleanList, long cleanSize);


}
