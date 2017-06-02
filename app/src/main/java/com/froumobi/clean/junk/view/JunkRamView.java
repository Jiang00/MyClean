package com.froumobi.clean.junk.view;

import com.android.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
