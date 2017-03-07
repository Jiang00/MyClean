package com.supers.clean.junk.View;

import android.view.View;
import android.widget.TextView;

import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.List;

/**
 * Created by Ivy on 2017/3/2.
 */

public interface JunkRamView extends JunkView {
    void addRamdata(long size, List<JunkInfo> list);
}
