package com.froumobic.clean.junk.view;

import com.android.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface AppManagerView extends IView {
    void initData(long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
