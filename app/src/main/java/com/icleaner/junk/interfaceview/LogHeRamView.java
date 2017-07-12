package com.icleaner.junk.interfaceview;

import com.icleaner.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface LogHeRamView extends MyGarbageView {
    void addRamdata(long size, List<JunkInfo> list);
}
