package com.mutter.clean.junk.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.mutter.clean.util.Util;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.view.MainView;
import com.mutter.clean.junk.util.CpuTempReader;
import com.mutter.clean.util.MemoryManager;
import com.mutter.clean.util.PreData;

import java.lang.reflect.Field;

/**
 * Created by on 2017/3/2.
 */

public class MainPresenter extends BasePresenter<MainView> {
    private int cpuTemp = 40;
    Handler handler;

    public MainPresenter(MainView iView, Context context, Handler handler) {
        super(iView, context);
        this.iView = iView;
        this.context = context;
        this.handler = handler;

    }


    public void reStart() {
        //SD卡储存
        long sd_all = MemoryManager.getPhoneAllSize();
        long sd_kongxian = MemoryManager.getPhoneAllFreeSize();
        long sd_shiyong = sd_all - sd_kongxian;
        int sd_me = (int) (sd_shiyong * 100 / sd_all);
        String sd_size = Util.convertStorage(sd_shiyong, true) + "/" + Util.convertStorage(sd_all, true);
        iView.initSd(sd_me, sd_size, sd_kongxian);
        //ram使用
        long ram_kongxian = MemoryManager.getPhoneFreeRamMemory(context);
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        long ram_shiyong = ram_all - ram_kongxian;
        int memo = (int) (ram_shiyong * 100 / ram_all);
        String ram_size = Util.convertStorage(ram_shiyong, true) + "/" + Util.convertStorage(ram_all, true);
        iView.initRam(memo, ram_size);
        setRotateGone();
    }


    public void clickRotate(boolean isGood) {
        if (isGood) {
            goToGooglePlay();
        }
        PreData.putDB(context, Constant.IS_ROTATE, true);
        setRotateGone();
    }

    public void deleteRotate() {
        PreData.putDB(context, Constant.IS_ROTATE_MAIN, true);
        setRotateGone();
    }

    public void init() {
        iView.loadFullAd();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reStart();
                //cpu温度
                CpuTempReader.getCPUTemp(new CpuTempReader.TemperatureResultCallback() {
                    @Override
                    public void callbackResult(CpuTempReader.ResultCpuTemperature result) {
                        if (result != null) {
                            cpuTemp = (int) result.getTemperature();
                        } else {
                            cpuTemp = 40;
                        }
                        if (cpuTemp == 0) {
                            cpuTemp = 40;
                        }
                        iView.initCpu(cpuTemp);
                    }
                });
            }
        }, 1000);
        TranslateAnimation translate = new TranslateAnimation(0, 0, 10, 2);
        translate.setInterpolator(new AccelerateInterpolator());//OvershootInterpolator
        translate.setDuration(300);
        translate.setRepeatCount(-1);
        translate.setRepeatMode(Animation.REVERSE);
        iView.loadAirAnimator(translate);
        setRotateGone();

    }

    public void setRotateGone() {
        if (PreData.getDB(context, Constant.IS_ROTATE, false) || PreData.getDB(context, Constant.IS_ROTATE_MAIN, false)) {
            iView.setRotateGone();
        }
    }

    public void openDrawer() {
        iView.openDrawer();
    }

    public void closeDrawer() {
        iView.closeDrawer();
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
