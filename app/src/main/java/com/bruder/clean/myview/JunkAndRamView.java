package com.bruder.clean.myview;

import com.cleaner.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkAndRamView extends JunkMyView {
    void addRamdata(long size, List<JunkInfo> list);
}
