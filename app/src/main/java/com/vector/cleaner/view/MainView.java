package com.vector.cleaner.view;

/**
 */

public interface MainView extends BaseView {
    void initCpu(int temp);

    void initSd(int perent, String size, long sd_kongxian);

    void initRam(int percent, String size);

    void initHuojian();


    void setRotateGone();

    void initSideData();

    void openDrawer();

    void closeDrawer();
}
