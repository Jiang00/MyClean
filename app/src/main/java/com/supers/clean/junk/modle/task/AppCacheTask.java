package com.supers.clean.junk.modle.task;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;


import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.entity.JunkInfo;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.MemoryManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2016/11/22.
 */

public class AppCacheTask extends SimpleTask {

    private static final String TAG = "AppCacheTask";

    public AppCacheTask(Context context, SimpleTaskListener simpleTaskListener) {
        super(context, simpleTaskListener, TAG);
    }

    @Override
    void loadData() {
        long t1 = System.currentTimeMillis();
        ArrayList<JunkInfo> dataList = new ArrayList<>();

        List<PackageInfo> packages;
        packages = pm.getInstalledPackages(0);
        String sdPath = MemoryManager.getPhoneInSDCardPath();
        long dataSize = 0;
        for (final PackageInfo packageInfo : packages) {
            final String packageName = packageInfo.packageName;
            if (packageName.equals(mContext.getPackageName())) {
                continue;
            }
            String path = sdPath + "/Android/data/" + packageName;
            long size = 0;
            try {
                size = CommonUtil.getFileSize(new File(path));
            } catch (Exception e) {

            }
            if (size > 0) {
                Drawable drawable;
                try {
                    drawable = packageInfo.applicationInfo.loadIcon(pm);
                } catch (Exception e) {
                    drawable = mContext.getResources().getDrawable(R.mipmap.icon);
                }
                final String label = (String) packageInfo.applicationInfo.loadLabel(pm);
                JunkInfo info = new JunkInfo(drawable, path, label, packageName, true, size);
                dataSize += size;
                dataList.add(info);
                if (mSimpleTaskListener != null) {
                    mSimpleTaskListener.loading(info, size);
                }
                drawable.setCallback(null);
            }

            if (isCancelTask) {
                if (mSimpleTaskListener != null) {
                    mSimpleTaskListener.cancelLoading();
                }
                break;
            }
        }

        if (mSimpleTaskListener != null) {
            Log.e("aaa", System.currentTimeMillis() - t1 + "======应用缓存时间");
            mSimpleTaskListener.finishLoading(dataSize, dataList);
        }
    }

}
