package com.mutter.clean.junk.view;

import com.mutter.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
