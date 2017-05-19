package com.supers.clean.junk.presenter;

import android.app.Activity;
import android.content.Context;

import com.supers.clean.junk.view.RamView;
import com.supers.clean.junk.activity.MyApplication;
import com.android.clean.util.MemoryManager;
import com.supers.clean.junk.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class RamPresenter extends BasePresenter<RamView> {
    private MyApplication cleanApplication;
    private long allSize;
    private long cleanSize;
    private ArrayList<JunkInfo> clearList;

    public RamPresenter(RamView iView, Context context) {
        super(iView, context);
        cleanApplication = (MyApplication) ((Activity) context).getApplication();
    }

    @Override
    public void init() {
        super.init();
        iView.loadFullAd();
        allSize = cleanApplication.getRamSize();
        for (JunkInfo info : cleanApplication.getAppRam()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        iView.initData(allSize);
        iView.setCleanDAta(true, cleanSize);
        changeColor(allSize);
        iView.onClick();
    }

    public void changeColor(long size) {
        //ram使用
        long ram_kongxian = MemoryManager.getPhoneFreeRamMemory(context);
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        long ram_shiyong = ram_all - ram_kongxian;
        int memo = (int) (ram_shiyong * 100 / ram_all);
        iView.setColor(memo, size);
    }

    public void addCleandata(boolean isAdd, long size) {
        if (isAdd) {
            cleanSize += size;
        } else {
            cleanSize -= size;
        }
        iView.setCleanDAta(false, cleanSize);
    }

    public void addWhiteList(String pkg) {
        for (JunkInfo junkInfo : cleanApplication.getListMng()) {
            if (pkg.equals(junkInfo.packageName)) {
                junkInfo.isWhiteList = true;
            }
        }
    }

    public void addAdapterData() {

        addRamAdapterData();
    }


    public void addRamAdapterData() {
        iView.addRamdata(cleanApplication.getRamSize(), cleanApplication.getAppRam());
    }

    public void bleachFile(List<JunkInfo> appRam) {
        clearList = new ArrayList<>();
        for (JunkInfo speedUpInfo : appRam) {
            if (speedUpInfo.isChecked) {
                clearList.add(speedUpInfo);
            }
        }
        for (JunkInfo info : clearList) {
            cleanApplication.removeRam(info);
        }
        changeColor(allSize - cleanSize);
        iView.cleanAnimation(clearList, cleanSize);
    }

}
