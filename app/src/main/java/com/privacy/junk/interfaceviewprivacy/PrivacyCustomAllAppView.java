package com.privacy.junk.interfaceviewprivacy;

import com.privacy.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface PrivacyCustomAllAppView extends BaseView {
    void initData(long cleanSize, long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
