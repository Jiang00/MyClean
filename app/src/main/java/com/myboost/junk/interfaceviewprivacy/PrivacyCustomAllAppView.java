package com.myboost.junk.interfaceviewprivacy;

import com.myboost.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface PrivacyCustomAllAppView extends BaseView {
    void initData(long cleanSize, long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
