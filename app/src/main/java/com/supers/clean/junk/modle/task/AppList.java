package com.supers.clean.junk.modle.task;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;


import com.supers.clean.junk.modle.entity.JunkInfo;
import com.supers.clean.junk.modle.PreData;

import java.util.List;

/**
 */

public class AppList {

    private Context context;
    private PackageManager pm;
    private List<String> list;
    private Thread thread;
    private AppListListener listener;

    public AppList(Context context) {
        this.context = context;
        pm = context.getPackageManager();
        list = PreData.getNameList(context);
        doBackg();
    }

    public void doBackg() {
        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long t2 = System.currentTimeMillis();
                    List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
                    for (PackageInfo packageInfo : installedPackages) {
                        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                            continue;
                        }
                        Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
                        String label = (String) packageInfo.applicationInfo.loadLabel(pm);
                        String packageName = packageInfo.packageName;
                        JunkInfo info = new JunkInfo(icon, label, packageName, list.contains(packageName));
                        if (listener != null) {
                            listener.addInfo(info);
                        }
                    }
                    if (listener != null) {
                        Log.e("aaa", System.currentTimeMillis() - t2 + "======应用列表时间");
                        listener.successStop();
                    }
                }
            });
        }
        thread.start();
    }

    public void setAppListListener(AppListListener listener) {
        this.listener = listener;
    }

    public interface AppListListener {
        void addInfo(JunkInfo info);

        void successStop();
    }
}
