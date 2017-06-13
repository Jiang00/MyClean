package com.fast.clean.junk.view;

import com.fast.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface AppManagerView extends BaseView {
    void initData(long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
