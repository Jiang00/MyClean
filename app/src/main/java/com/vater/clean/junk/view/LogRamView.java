package com.vater.clean.junk.view;

import com.vater.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface LogRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
