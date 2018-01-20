package com.eifmobi.clean.junk.view;

import android.view.animation.TranslateAnimation;

/**
 */

public interface MainView extends IView {
    void initRam(int percent, String size);

    void initCpu(int temp);

    void initSd(int perent, String size, long sd_kongxian);


    void loadAirAnimator(TranslateAnimation translateAnimation);

    void setRotateGone();


    void openDrawer();

    void closeDrawer();
}
