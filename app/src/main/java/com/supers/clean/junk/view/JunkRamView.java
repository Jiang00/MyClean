package com.supers.clean.junk.view;

import com.supers.clean.junk.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
