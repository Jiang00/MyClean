package com.supers.clean.junk.presenter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.supers.clean.junk.View.JunkRamView;
import com.supers.clean.junk.View.RamView;
import com.supers.clean.junk.activity.MyApplication;
import com.supers.clean.junk.modle.MemoryManager;
import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/3/2.
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
        cleanSize = allSize = cleanApplication.getRamSize();

        iView.initData(allSize);
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
        iView.setCleanDAta(cleanSize);
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
                cleanApplication.removeRam(speedUpInfo);
                clearList.add(speedUpInfo);

            }
        }
        changeColor(allSize - cleanSize);
        iView.cleanAnimation(clearList, cleanSize);
    }

}
