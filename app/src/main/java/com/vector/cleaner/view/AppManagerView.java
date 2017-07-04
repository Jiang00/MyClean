package com.vector.cleaner.view;

import com.vector.mcleaner.entity.JunkInfo;

import java.util.List;

/**
 */

public interface AppManagerView extends BaseView {
    void initData(long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
