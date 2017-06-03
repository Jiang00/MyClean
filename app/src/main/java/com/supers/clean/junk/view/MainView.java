package com.supers.clean.junk.view;

import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 */

public interface MainView extends IView {
    void initCpu(int temp);

    void initSd(int perent, String size, long sd_kongxian);

    void initRam(int percent, String size);



    void setRotateGone();

    void initSideData();

    void openDrawer();

    void closeDrawer();
}
