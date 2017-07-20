package com.easy.junk.easyinterfaceview;

import com.easy.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface CustomRamView extends EasyBaseView {
    void initData(long allSize);

    void setColor(int memory, long allSize);

    void addRamdata(long size, List<JunkInfo> list);

    void setCleanDAta(boolean isFirst, long size);

    void cleanAnimation(List<JunkInfo> cleanList, long cleanSize);


}
