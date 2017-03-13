package com.supers.clean.junk.View;

import android.view.View;
import android.widget.TextView;

import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
