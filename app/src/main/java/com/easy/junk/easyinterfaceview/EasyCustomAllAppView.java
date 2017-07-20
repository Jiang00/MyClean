package com.easy.junk.easyinterfaceview;

import com.easy.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface EasyCustomAllAppView extends EasyBaseView {
    void initData(long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
