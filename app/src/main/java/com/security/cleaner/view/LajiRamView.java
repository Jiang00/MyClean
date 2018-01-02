package com.security.cleaner.view;

import com.security.mcleaner.entity.JunkInfo;

import java.util.List;

/**
 */

public interface LajiRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
