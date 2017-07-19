package com.easy.junk.easypresenter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.easy.clean.core.CleanManager;
import com.easy.junk.easyinterfaceview.LogHeRamView;
import com.easy.junk.easyactivity.MyApplication;
import com.easy.clean.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class LogRamPresenter extends PresenterBase<LogHeRamView> {
    private MyApplication cleanApplication;
    private ArrayList<JunkInfo> clearList;
    private long allSize;
    private long cleanSize;

    public LogRamPresenter(LogHeRamView iView, Context context) {
        super(iView, context);
        cleanApplication = (MyApplication) ((Activity) context).getApplication();
    }

    @Override
    public void init() {
        super.init();
        iView.loadFullAd();
        allSize = CleanManager.getInstance(context).getApkSize() + CleanManager.getInstance(context).getCacheSize() + CleanManager.getInstance(context).getUnloadSize() + CleanManager.getInstance(context).getLogSize()
                + CleanManager.getInstance(context).getDataSize() + CleanManager.getInstance(context).getRamSize();

        for (JunkInfo info : CleanManager.getInstance(context).getSystemCaches()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : CleanManager.getInstance(context).getApkFiles()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : CleanManager.getInstance(context).getAppCaches()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : CleanManager.getInstance(context).getLogFiles()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : CleanManager.getInstance(context).getUninstallResiduals()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        for (JunkInfo info : CleanManager.getInstance(context).getAppRamList()) {
            if (info.isChecked) {
                cleanSize += info.size;
            }
        }
        iView.initData(allSize);
        iView.setCleanDAta(true, cleanSize);
        iView.setColor(allSize);
        iView.onClick();
    }

    public void changeColor(long size) {
        iView.setColor(size);
    }




    public void addSystemAdapterData() {
        iView.addSystemdata(CleanManager.getInstance(context).getCacheSize(), CleanManager.getInstance(context).getSystemCaches());
    }

    public void addApkAdapterData() {
        iView.addApkdata(CleanManager.getInstance(context).getApkSize(), CleanManager.getInstance(context).getApkFiles());
    }
    public void addCleandata(boolean isAdd, long size) {
        if (isAdd) {
            cleanSize += size;
        } else {
            cleanSize -= size;
        }
        iView.setCleanDAta(false, cleanSize);
    }
    public void addUnloadAdapterData() {
        iView.addUnloaddata(CleanManager.getInstance(context).getUnloadSize(), CleanManager.getInstance(context).getUninstallResiduals());
    }

    public void addLogAdapterData() {
        iView.addLogdata(CleanManager.getInstance(context).getLogSize(), CleanManager.getInstance(context).getLogFiles());
    }

    public void addUserAdapterData() {
        iView.addUserdata(CleanManager.getInstance(context).getDataSize(), CleanManager.getInstance(context).getAppCaches());
    }


    public void addRamAdapterData() {
        iView.addRamdata(CleanManager.getInstance(context).getRamSize(), CleanManager.getInstance(context).getAppRamList());
    }

    public void addAdapterData() {

        addSystemAdapterData();
        addApkAdapterData();
        addUnloadAdapterData();
        addLogAdapterData();
        addUserAdapterData();
        addRamAdapterData();
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
            clearList.addAll(CleanManager.getInstance(context).getSystemCaches());
        }

//                adapter1.clear();
        CleanManager.getInstance(context).clearSystemCache();
        for (JunkInfo fileListInfo : apkFiles) {
            if (fileListInfo.isChecked) {
                CleanManager.getInstance(context).removeApkFiles(fileListInfo);
                if (isZhankai) {
                    clearList.add(fileListInfo);
                }
            }
        }

        for (JunkInfo clearListInfo : filesOfUnintalledApk) {
            if (clearListInfo.isChecked) {
                CleanManager.getInstance(context).removeFilesOfUnintalledApk(clearListInfo);
                if (isZhankai) {
                    clearList.add(clearListInfo);
                }
            }
        }

        for (JunkInfo fileListInfo : appJunk) {
            if (fileListInfo.isChecked) {
                CleanManager.getInstance(context).removeAppLog(fileListInfo);
                if (isZhankai) {
                    clearList.add(fileListInfo);
                }
            }
        }

        for (JunkInfo fileListInfo : appCache) {
            if (fileListInfo.isChecked) {
                CleanManager.getInstance(context).removeAppCache(fileListInfo);
                if (isZhankai) {
                    clearList.add(fileListInfo);
                }
            }
        }

        for (JunkInfo speedUpInfo : appRam) {
            if (speedUpInfo.isChecked) {
                CleanManager.getInstance(context).removeRam(speedUpInfo);
                if (isZhankai) {
                    clearList.add(speedUpInfo);
                }

            }
        }
        changeColor(allSize - cleanSize);
        iView.cleanAnimation(isZhankai, clearList, cleanSize);
    }

}
