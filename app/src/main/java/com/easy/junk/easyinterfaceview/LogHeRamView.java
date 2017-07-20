package com.easy.junk.easyinterfaceview;

import com.easy.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface LogHeRamView extends EasyGarbageView {
    void addRamdata(long size, List<JunkInfo> list);
}
