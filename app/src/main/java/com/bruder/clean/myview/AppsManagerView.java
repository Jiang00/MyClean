package com.bruder.clean.myview;

import com.cleaner.entity.JunkInfo;

import java.util.List;

public interface AppsManagerView extends Bruderiew {
    void initData(long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
