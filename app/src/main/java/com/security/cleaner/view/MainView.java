package com.security.cleaner.view;

/**
 */

public interface MainView extends BaseView {
    void initCpu(int temp);

    void initSd(int perent, String size, long sd_kongxian);

    void initSideData();

    void initRam(int percent, String size);

    void openDrawer();

    void closeDrawer();
}
