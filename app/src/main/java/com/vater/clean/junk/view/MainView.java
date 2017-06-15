package com.vater.clean.junk.view;

/**
 */

public interface MainView extends BaseView {
    void initCpu(int temp);

    void initQiu(int fenshu,boolean isReStart);

    void setRotateGone();

    void initSideData();

    void openDrawer();

    void closeDrawer();
}
