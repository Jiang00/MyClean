package com.supers.clean.junk.presenter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.supers.clean.junk.View.JunkRamView;
import com.supers.clean.junk.activity.MyApplication;
import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class JunkRamPresenter extends BasePresenter<JunkRamView> {
    private MyApplication cleanApplication;
    private long allSize;
    private long cleanSize;
    private ArrayList<JunkInfo> clearList;

    public JunkRamPresenter(JunkRamView iView, Context context) {
        super(iView, context);
        cleanApplication = (MyApplication) ((Activity) context).getApplication();
    }

    @Override
    public void init() {
        super.init();
        iView.loadFullAd();
        allSize = cleanApplication.getApkSize() + cleanApplication.getCacheSize() + cleanApplication.getRamSize() + cleanApplication.getUnloadSize() + cleanApplication.getLogSize() + cleanApplication.getDataSize();
        for (JunkInfo info : cleanApplication.getSystemCache()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : cleanApplication.getApkFiles()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : cleanApplication.getAppCache()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : cleanApplication.getAppJunk()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : cleanApplication.getFilesOfUnintalledApk()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : cleanApplication.getAppRam()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        iView.initData(allSize);
        iView.setCleanDAta(true,cleanSize);
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
        iView.setCleanDAta(false,cleanSize);
    }

    public void addAdapterData() {

        addSystemAdapterData();
        addApkAdapterData();
        addUnloadAdapterData();
        addLogAdapterData();
        addUserAdapterData();
        addRamAdapterData();
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

    public void addRamAdapterData() {
        iView.addRamdata(cleanApplication.getRamSize(), cleanApplication.getAppRam());
    }

    @Override
    public void setUnit(long size, TextView textView) {
        super.setUnit(size, textView);
        iView.setUnit(size, textView);

    }

    public void bleachFile(boolean isZhankai, List<JunkInfo> systemCache, List<JunkInfo> apkFiles, List<JunkInfo> filesOfUnintalledApk,
                           List<JunkInfo> appJunk, List<JunkInfo> appCache, List<JunkInfo> appRam) {
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

        for (JunkInfo speedUpInfo : appRam) {
            if (speedUpInfo.isChecked) {
                cleanApplication.removeRam(speedUpInfo);
                if (isZhankai) {
                    clearList.add(speedUpInfo);
                }

            }
        }
        changeColor(allSize - cleanSize);
        iView.cleanAnimation(isZhankai, clearList, cleanSize);
    }

}
