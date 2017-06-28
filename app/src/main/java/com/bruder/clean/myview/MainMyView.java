package com.bruder.clean.myview;

import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 */

public interface MainMyView extends Bruderiew {
    void initCpu(int temp);

    void initSd(int perent, String size, long sd_kongxian);

    void initRam(int percent, String size);

    void initGuard(int num, RotateAnimation rotateAnimation);

    void loadAirAnimator(TranslateAnimation translateAnimation);

    void setRotateGone();

    void initSideData();

    void openDrawer();

    void closeDrawer();
}
