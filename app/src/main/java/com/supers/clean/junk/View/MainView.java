package com.supers.clean.junk.View;

import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Ivy on 2017/3/2.
 */

public interface MainView extends IView {
    void initCercleHight();

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
