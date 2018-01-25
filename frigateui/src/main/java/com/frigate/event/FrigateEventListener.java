package com.frigate.event;

import com.frigate.layout.AutoDrawerLayout;
import com.frigate.layout.FrigateFrameLayout;
import com.frigate.layout.FrigateLinearLayout;
import com.frigate.layout.FrigateRelativeLayout;

/**
 * Created by renqingyou on 2017/2/17.
 */

public interface FrigateEventListener extends FrigateFrameLayout.AutoLayoutEventListener, AutoDrawerLayout.AutoLayoutEventListener, FrigateLinearLayout.AutoLayoutEventListener, FrigateRelativeLayout.AutoLayoutEventListener {
}
