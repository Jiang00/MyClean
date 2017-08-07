package com.myboost.junk.boostinterfaceview;

import com.myboost.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface BoostLogHeRamView extends BoostGarbageView {
    void addRamdata(long size, List<JunkInfo> list);
}
