package com.supers.clean.junk.modle.task;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.ArrayList;


/**
 * Created by renqingyou on 2017/2/22.
 */

public abstract class SimpleTask extends Thread {

    protected Context mContext;
    protected SimpleTaskListener mSimpleTaskListener;
    protected boolean isCancelTask;
    protected PackageManager pm;
    protected ActivityManager am;

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
        mContext = context;
        mSimpleTaskListener = simpleTaskListener;
        pm = context.getPackageManager();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public interface SimpleTaskListener {
        void startLoad();

        void loading(JunkInfo fileInfo, long size);

        void cancelLoading();

        void finishLoading(long dataSize, ArrayList<JunkInfo> dataList);
    }

    public void cancelTask() {
        isCancelTask = true;
    }

}
