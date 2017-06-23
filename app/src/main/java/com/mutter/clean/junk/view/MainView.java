package com.mutter.clean.junk.view;

import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 */

public interface MainView extends IView {
    void initRam(int percent, String size);

    void initCpu(int temp);

    void initSd(int perent, String size, long sd_kongxian);

    void initGuard(int num, RotateAnimation rotateAnimation);

    void loadAirAnimator(TranslateAnimation translateAnimation);

    void setRotateGone();

    void initSideData();

    void openDrawer();

    void closeDrawer();
}
