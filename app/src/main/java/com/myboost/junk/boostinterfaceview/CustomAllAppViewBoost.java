package com.myboost.junk.boostinterfaceview;

import com.myboost.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface CustomAllAppViewBoost extends BaseView {
    void initData(long cleanSize, long allSize);

    void updateAdapter(List<JunkInfo> listsize, List<JunkInfo> listtime, List<JunkInfo> listpinlv);

    void setCleanDAta(long size);

}
