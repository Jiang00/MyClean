package com.myboost.junk.interfaceviewprivacy;

import com.myboost.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface PrivacyLogHeRamView extends GarbageViewPrivacy {
    void addRamdata(long size, List<JunkInfo> list);
}
