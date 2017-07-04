package com.vector.cleaner.view;

import com.vector.mcleaner.entity.JunkInfo;

import java.util.List;

/**
 */

public interface LajiRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
