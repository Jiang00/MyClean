package com.eifmobi.clean.junk.view;

import com.eifmobi.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface AppManagerView extends IView {
    void initData(long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
