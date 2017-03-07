package com.supers.clean.junk.View;

import android.widget.TextView;

import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.List;

/**
 * Created by Ivy on 2017/3/2.
 */

public interface RamView extends IView {
    void initData( long allSize);

    void setColor(int memory,long allSize);

    void addRamdata(long size, List<JunkInfo> list);

    void setCleanDAta(long size);

    void cleanAnimation(List<JunkInfo> cleanList, long cleanSize);


}
