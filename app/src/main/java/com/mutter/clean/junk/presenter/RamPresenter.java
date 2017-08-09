package com.mutter.clean.junk.presenter;

import android.content.Context;

import com.mutter.clean.core.CleanManager;
import com.mutter.clean.junk.view.RamView;
import com.mutter.clean.util.MemoryManager;
import com.mutter.clean.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class RamPresenter extends BasePresenter<RamView> {
    private long cleanSize;
    private ArrayList<JunkInfo> clearList;
    private long allSize;

    public RamPresenter(RamView iView, Context context) {
        super(iView, context);
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

}