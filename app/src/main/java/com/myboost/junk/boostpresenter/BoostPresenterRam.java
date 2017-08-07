package com.myboost.junk.presenterprivacy;

import android.app.Activity;
import android.content.Context;

import com.myboost.clean.core.CleanManager;
import com.myboost.clean.entity.JunkInfo;
import com.myboost.clean.utilsprivacy.MemoryManager;
import com.myboost.junk.activityprivacy.MyApplication;
import com.myboost.junk.interfaceviewprivacy.CustomRamView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class PresenterRamPrivacy extends PresenterBasePrivacy<CustomRamView> {
    private MyApplication cleanApplication;
    private long allSize;
    private ArrayList<JunkInfo> clearList;
    private long cleanSize;

    public PresenterRamPrivacy(CustomRamView iView, Context context) {
        super(iView, context);
        cleanApplication = (MyApplication) ((Activity) context).getApplication();
    }


    @Override
    public void init() {
        super.init();
        iView.loadFullAd();
        allSize = CleanManager.getInstance(context).getRamSize();
        for (JunkInfo info : CleanManager.getInstance(context).getAppRamList()) {
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

    public void bleachFile(List<JunkInfo> appRam) {
        clearList = new ArrayList<>();
        for (JunkInfo speedUpInfo : appRam) {
            if (speedUpInfo.isChecked) {
                clearList.add(speedUpInfo);
            }
        }
        for (JunkInfo info : clearList) {
            CleanManager.getInstance(context).removeRam(info);
        }
        changeColor(allSize - cleanSize);
        iView.cleanAnimation(clearList, cleanSize);
    }

    public void addWhiteList(String pkg) {
        for (JunkInfo junkInfo : CleanManager.getInstance(context).getAppList()) {
            if (pkg.equals(junkInfo.pkg)) {
                junkInfo.isWhiteList = true;
            }
        }
    }

    public void addAdapterData() {

        addRamAdapterData();
    }


    public void addRamAdapterData() {
        iView.addRamdata(CleanManager.getInstance(context).getRamSize(), CleanManager.getInstance(context).getAppRamList());
    }


}
