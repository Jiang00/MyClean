package com.vater.clean.junk.view;

import com.vater.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface AllAppView extends BaseView {
    void initData(long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
