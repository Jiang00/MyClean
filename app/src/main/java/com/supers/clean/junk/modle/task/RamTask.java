package com.supers.clean.junk.modle.task;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug.MemoryInfo;


import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.supers.clean.junk.modle.entity.JunkInfo;
import com.supers.clean.junk.modle.PreData;

import java.util.ArrayList;
import java.util.List;


public class RamTask extends SimpleTask {
    private static final String TAG = "RamTask";
    private ArrayList<JunkInfo> dataList;
    private long dataSize;

    public RamTask(Context context, SimpleTaskListener simpleTaskListener) {
        super(context, simpleTaskListener, TAG);
    }

    @Override
    void loadData() {
        dataList = new ArrayList<>();
        dataSize = 0;
        List ignoreApp = PreData.getNameList(mContext);
        List<AndroidAppProcess> listInfo = AndroidProcesses.getRunningAppProcesses();
        for (AndroidAppProcess info : listInfo) {
            int pid = info.pid;
            String packageName = info.name;
            if (packageName.equals(mContext.getPackageName()) || packageName.contains("com.eosmobi")) {
                continue;
            }
            if (!ignoreApp.contains(packageName)) {
                addApplica(false, packageName, pid);
            } else {
                addApplica(true, packageName, pid);
            }
            if (isCancelTask) {
                if (mSimpleTaskListener != null) {
                    mSimpleTaskListener.cancelLoading();
                }
                break;
            }
        }

        if (mSimpleTaskListener != null) {
            mSimpleTaskListener.finishLoading(dataSize, dataList);
        }
    }

    private void addApplica(boolean isWhite, String packageName, int pid) {
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA | PackageManager.GET_SHARED_LIBRARY_FILES);
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
            } else {
                MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
                int totalPrivateDirty = processMemoryInfo[0].getTotalPrivateDirty();
                if (isWhite) {
                    JunkInfo speedUpInfo = new JunkInfo(false, pm.getApplicationIcon(applicationInfo), (String) pm.getApplicationLabel(applicationInfo), totalPrivateDirty * 1024, packageName);
                    if (mSimpleTaskListener != null) {
                        mSimpleTaskListener.loadingW(speedUpInfo);
                    }
                } else {
                    JunkInfo speedUpInfo = new JunkInfo(true, pm.getApplicationIcon(applicationInfo), (String) pm.getApplicationLabel(applicationInfo), totalPrivateDirty * 1024, packageName);
                    dataSize += speedUpInfo.size;
                    dataList.add(speedUpInfo);
                    if (mSimpleTaskListener != null) {
                        mSimpleTaskListener.loading(speedUpInfo, speedUpInfo.size);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
