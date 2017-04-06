package com.supers.clean.junk.task;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.supers.clean.junk.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by renqingyou on 2017/2/22.
 */

public abstract class SimpleTask extends Thread {

    protected Context mContext;
    protected SimpleTaskListener mSimpleTaskListener;
    protected boolean isCancelTask;
    protected PackageManager pm;
    protected ActivityManager am;
    static byte[] lock = new byte[0];

    @Override
    public void run() {
        super.run();
        if (mSimpleTaskListener != null) {
            mSimpleTaskListener.startLoad();
        }
        long startTime = System.currentTimeMillis();
        loadData();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;
        Log.e("rqy", "线程" + Thread.currentThread().getName() + "--time=" + time);
    }

    abstract void loadData();

    public SimpleTask(Context context, SimpleTaskListener simpleTaskListener, String threadName) {
        super(threadName);
        mContext = context.getApplicationContext();
        mSimpleTaskListener = simpleTaskListener;
        pm = context.getPackageManager();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public interface SimpleTaskListener {
        void startLoad();

        void loading(JunkInfo fileInfo, long size);

        void loadingW(JunkInfo fileInfo);

        void cancelLoading();

        void finishLoading(long dataSize, ArrayList<JunkInfo> dataList);
    }

    public List<PackageInfo> getInstallPackage(PackageManager pm) {
        if (pm == null) {
            return null;
        }
        List<PackageInfo> installPackage;
        synchronized (lock) {
            installPackage = pm.getInstalledPackages(0);
        }
        return installPackage;
    }

    public List<ApplicationInfo> getInstalledApplications(PackageManager pm) {
        if (pm == null) {
            return null;
        }
        List<ApplicationInfo> installPackage;
        synchronized (lock) {
            installPackage = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        }
        return installPackage;
    }


    public void cancelTask() {
        isCancelTask = true;
    }

}
