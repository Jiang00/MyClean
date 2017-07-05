package com.vater.clean.junk.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;

import com.vater.clean.core.CleanManager;
import com.vater.clean.util.Util;
import com.vater.clean.junk.gongju.Constant;
import com.vater.clean.junk.view.MainView;
import com.vater.clean.junk.gongju.RCpuTempReader;
import com.vater.clean.util.PreData;

import java.lang.reflect.Field;

/**
 * Created by on 2017/3/2.
 */

public class MainPresenter extends BasePresenter<MainView> {
    private int cpuTemp = 40;


    public void openDrawer() {
        iView.openDrawer();
    }

    public void closeDrawer() {
        iView.closeDrawer();
    }

    public void init() {
        iView.loadFullAd();
        iView.initSideData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //cpu温度
                RCpuTempReader.getCPUTemp(new RCpuTempReader.TemperatureResultCallback() {
                    @Override
                    public void callbackResult(RCpuTempReader.ResultCpuTemperature result) {
                        if (result != null) {
                            cpuTemp = (int) result.getTemperature();
                        } else {
                            cpuTemp = 40;
                        }
                        iView.initCpu(cpuTemp);
                    }
                });
            }
        }).start();
        reStart(false);
        setRotateGone();
        iView.onClick();

    }

    public MainPresenter(MainView iView, Context context) {
        super(iView, context);
        this.iView = iView;
        this.context = context;
    }


    public void startFenshu(boolean isReStart) {
        int function = 100;
        if (CleanManager.getInstance(context).getSystemCaches().size() != 0) {
            function -= 6;
        }
        if (CleanManager.getInstance(context).getAppCaches().size() != 0) {
            function -= 6;
        }
        if (CleanManager.getInstance(context).getLogFiles().size() != 0) {
            function -= 6;
        }
        if (CleanManager.getInstance(context).getUninstallResiduals().size() != 0) {
            function -= 6;
        }
        if (CleanManager.getInstance(context).getApkFiles().size() != 0) {
            function -= 6;
        }
        if (CleanManager.getInstance(context).getAppRamList().size() != 0) {
            function -= Util.getMemory(context) * 0.7;
        }
        iView.initQiu(function, isReStart);

    }

    public void reStart(boolean isReStart) {
        startFenshu(isReStart);
        setRotateGone();
    }

    public void clickRotate(boolean isGood) {
        if (isGood) {
            goToGooglePlay();
        }
        PreData.putDB(context, Constant.IS_ROTATE, true);
        setRotateGone();
    }

    public void setRotateGone() {
        if (PreData.getDB(context, Constant.IS_ROTATE, false)) {
            iView.setRotateGone();
        }
    }

    //设置侧边栏滑出距离,从屏幕哪里可以滑出
    public void setDrawerLeftEdgeSize(DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (context == null || drawerLayout == null) return;
        try {
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (dm.widthPixels * displayWidthPercentage)));
        } catch (Exception e) {
        }
    }
}