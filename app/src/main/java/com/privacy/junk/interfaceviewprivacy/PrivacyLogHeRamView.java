package com.privacy.junk.interfaceviewprivacy;

import com.privacy.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface PrivacyLogHeRamView extends GarbageViewPrivacy {
    void addRamdata(long size, List<JunkInfo> list);
}
