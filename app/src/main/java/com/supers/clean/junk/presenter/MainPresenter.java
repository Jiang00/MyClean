package com.supers.clean.junk.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

import com.supers.clean.junk.View.MainView;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.CpuTempReader;
import com.supers.clean.junk.modle.MemoryManager;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.entity.Contents;

import java.lang.reflect.Field;

/**
 * Created by Ivy on 2017/3/2.
 */

public class MainPresenter extends BasePresenter<MainView> {
    private int cpuTemp = 40;

    public MainPresenter(MainView iView, Context context) {
        super(iView, context);
        this.iView = iView;
        this.context = context;
    }

    public void init() {
        iView.loadFullAd();
        iView.initCercleHight();
        iView.initSideData();
        reStart();

        TranslateAnimation translate = new TranslateAnimation(0, 0, 10, 2);
        translate.setInterpolator(new AccelerateInterpolator());//OvershootInterpolator
        translate.setDuration(300);
        translate.setRepeatCount(-1);
        translate.setRepeatMode(Animation.REVERSE);
        iView.loadAirAnimator(translate);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, CommonUtil.dp2px(115), CommonUtil.dp2px(115));
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(-1);
        iView.initGuard(MemoryManager.getInstallNum(context), rotateAnimation);
        setRotateGone();
        iView.onClick();

    }

    public void reStart() {
        //cpu温度
        CpuTempReader.getCPUTemp(new CpuTempReader.TemperatureResultCallback() {
            @Override
            public void callbackResult(CpuTempReader.ResultCpuTemperature result) {
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();
                } else {
                    cpuTemp = 40;
                }
                iView.initCpu(cpuTemp);
            }
        });
        //SD卡储存
        long sd_all = MemoryManager.getPhoneAllSize();
        long sd_kongxian = MemoryManager.getPhoneAllFreeSize();
        long sd_shiyong = sd_all - sd_kongxian;
        int sd_me = (int) (sd_shiyong * 100 / sd_all);
        String sd_size = CommonUtil.getFileSize1(sd_shiyong) + "/" + CommonUtil.getFileSize1(sd_all);
        iView.initSd(sd_me, sd_size, sd_kongxian);
        //ram使用
        long ram_kongxian = MemoryManager.getPhoneFreeRamMemory(context);
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        long ram_shiyong = ram_all - ram_kongxian;
        int memo = (int) (ram_shiyong * 100 / ram_all);
        String ram_size = CommonUtil.getFileSize1(ram_shiyong) + "/" + CommonUtil.getFileSize1(ram_all);
        iView.initRam(memo, ram_size);
    }


    public void clickRotate(boolean isGood) {
        if (isGood) {
            goToGooglePlay();
        }
        PreData.putDB(context, Contents.IS_ROTATE, true);
        setRotateGone();
    }

    public void setRotateGone() {
        if (PreData.getDB(context, Contents.IS_ROTATE, false)) {
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
