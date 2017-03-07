package com.supers.clean.junk.presenter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.supers.clean.junk.View.JunkRamView;
import com.supers.clean.junk.View.JunkView;
import com.supers.clean.junk.activity.JunkAndRamActivity;
import com.supers.clean.junk.activity.MyApplication;
import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/3/2.
 */

public class JunkPresenter extends BasePresenter<JunkView> {
    private MyApplication cleanApplication;
    private long allSize;
    private long cleanSize;
    private ArrayList<JunkInfo> clearList;

    public JunkPresenter(JunkView iView, Context context) {
        super(iView, context);
        cleanApplication = (MyApplication) ((Activity) context).getApplication();
    }

    @Override
    public void init() {
        super.init();
        iView.loadFullAd();
        cleanSize = allSize = cleanApplication.getApkSize() + cleanApplication.getCacheSize() + cleanApplication.getUnloadSize() + cleanApplication.getLogSize() + cleanApplication.getDataSize();
        cleanApplication.getSystemCache();
        iView.initData(allSize);
        iView.setColor(allSize);
        iView.onClick();
    }

    public void changeColor(long size) {
        iView.setColor(size);
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

        addSystemAdapterData();
        addApkAdapterData();
        addUnloadAdapterData();
        addLogAdapterData();
        addUserAdapterData();
    }

    public void addSystemAdapterData() {
        iView.addSystemdata(cleanApplication.getCacheSize(), cleanApplication.getSystemCache());
    }

    public void addApkAdapterData() {
        iView.addApkdata(cleanApplication.getApkSize(), cleanApplication.getApkFiles());
    }

    public void addUnloadAdapterData() {
        iView.addUnloaddata(cleanApplication.getUnloadSize(), cleanApplication.getFilesOfUnintalledApk());
    }

    public void addLogAdapterData() {
        iView.addLogdata(cleanApplication.getLogSize(), cleanApplication.getAppJunk());
    }

    public void addUserAdapterData() {
        iView.addUserdata(cleanApplication.getDataSize(), cleanApplication.getAppCache());
    }

    @Override
    public void setUnit(long size, TextView textView) {
        super.setUnit(size, textView);
        iView.setUnit(size, textView);

    }

    public void bleachFile(boolean isZhankai, List<JunkInfo> systemCache, List<JunkInfo> apkFiles, List<JunkInfo> filesOfUnintalledApk,
                           List<JunkInfo> appJunk, List<JunkInfo> appCache) {
        clearList = new ArrayList<>();
        if (isZhankai) {
            clearList.addAll(cleanApplication.getSystemCache());
        }

//                adapter1.clear();
        cleanApplication.clearSystemCache();
        for (JunkInfo fileListInfo : apkFiles) {
            if (fileListInfo.isChecked) {
                cleanApplication.removeApkFiles(fileListInfo);
                if (isZhankai) {
                    clearList.add(fileListInfo);
                }
            }
        }

        for (JunkInfo clearListInfo : filesOfUnintalledApk) {
            if (clearListInfo.isChecked) {
                cleanApplication.removeFilesOfUnintalledApk(clearListInfo);
                if (isZhankai) {
                    clearList.add(clearListInfo);
                }
            }
        }

        for (JunkInfo fileListInfo : appJunk) {
            if (fileListInfo.isChecked) {
                cleanApplication.removeAppJunk(fileListInfo);
                if (isZhankai) {
                    clearList.add(fileListInfo);
                }
            }
        }

        for (JunkInfo fileListInfo : appCache) {
            if (fileListInfo.isChecked) {
                cleanApplication.removeAppCache(fileListInfo);
                if (isZhankai) {
                    clearList.add(fileListInfo);
                }
            }
        }
        changeColor(allSize - cleanSize);
        iView.cleanAnimation(isZhankai, clearList, cleanSize);
    }

}
