package com.eifmobi.clean.junk.view;

import com.eifmobi.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
