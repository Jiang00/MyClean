package com.security.cleaner.view;

import com.security.mcleaner.entity.JunkInfo;

import java.util.List;

/**
 */

public interface AppManagerView extends BaseView {
    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void initData(long allSize);


}
