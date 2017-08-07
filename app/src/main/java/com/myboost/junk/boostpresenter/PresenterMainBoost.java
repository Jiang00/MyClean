package com.myboost.junk.boostpresenter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.DisplayMetrics;

import com.myboost.clean.core.CleanManager;
import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.boosttools.GetCpuTempReaderBoost;
import com.myboost.junk.boostinterfaceview.BoostMainView;
import com.myboost.clean.utilsprivacy.MemoryManager;
import com.myboost.clean.utilsprivacy.PreData;

import java.lang.reflect.Field;

/**
 * Created by on 2017/3/2.
 */

public class PresenterMainBoost extends BoostPresenterBase<BoostMainView> {
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
                GetCpuTempReaderBoost.getCPUTemp(new GetCpuTempReaderBoost.TemperatureResultCallback() {
                    @Override
                    public void callbackResult(GetCpuTempReaderBoost.ResultCpuTemperature result) {
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

    public PresenterMainBoost(BoostMainView iView, Context context) {
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
            function -= MyUtils.getMemory(context) * 0.7;
        }
        iView.initQiu(function, isReStart);

    }

    public void reStart(boolean isReStart) {
        startFenshu(isReStart);
        setRotateGone();

        //SD卡储存
        long sd_all = MemoryManager.getPhoneAllSize();
        long sd_kongxian = MemoryManager.getPhoneAllFreeSize();
        long sd_shiyong = sd_all - sd_kongxian;
        int sd_me = (int) (sd_shiyong * 100 / sd_all);
        String sd_size = MyUtils.convertStorage(sd_shiyong, true) + "/" + MyUtils.convertStorage(sd_all, true);
        iView.initSd(sd_me, sd_size, sd_kongxian);
        //ram使用
        long ram_kongxian = MemoryManager.getPhoneFreeRamMemory(context);
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        long ram_shiyong = ram_all - ram_kongxian;
        int memo = (int) (ram_shiyong * 100 / ram_all);
        String ram_size = MyUtils.convertStorage(ram_shiyong, true) + "/" + MyUtils.convertStorage(ram_all, true);
        iView.initRam(memo, ram_size);
        setRotateGone();
    }

    public void clickRotate(boolean isGood) {
        if (isGood) {
            goToGooglePlay();
        }
        PreData.putDB(context, BoostMyConstant.IS_ROTATE, true);
        setRotateGone();
    }

    public void setRotateGone() {
        if (PreData.getDB(context, BoostMyConstant.IS_ROTATE, false)) {
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
