package com.supers.clean.junk.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;

import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.GetTopApp;
import com.supers.clean.junk.presenter.FloatStateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/7.
 */

public class FloatService extends Service {
    private PackageManager pm;
    private ActivityManager am;
    private int count = 0;
    private List<String> hmoes;
    private GetTopApp topApp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler myHandler;
    FloatStateManager manager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (myHandler == null) {
            myHandler = new Handler();
        }
        manager = FloatStateManager.getInstance(FloatService.this.getApplicationContext());
        if (pm == null)
            pm = getPackageManager();
        if (am == null)
            am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        topApp = new GetTopApp(this);
        hmoes = getLaunchers();
        manager.showFloatCircleView();
        myHandler.removeCallbacks(runnable);
        myHandler.postDelayed(runnable, 1000);
        return START_REDELIVER_INTENT;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updata();
            myHandler.postDelayed(runnable, 2000);
        }
    };

    private void updata() {
        if (count == 0) {
            count = 30;
            int memory = CommonUtil.getMemory(this);
            manager.upDate(memory);
        }
        count--;
        new Thread(new Runnable() {
            @Override
            public void run() {
//                final Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                String pkg = TopActivityPkg.getTopPackageName(FloatService.this);
                String pkg = topApp.execute();
                if (hmoes.contains(pkg)) {
                    manager.upDate(CommonUtil.getMemory(FloatService.this));
                    manager.addWindowsView();
                } else {
                    manager.removeWindowsView();
                }
            }
        }).start();

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public List<String> getLaunchers() {
        List<String> packageNames = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null) {
                packageNames.add(resolveInfo.activityInfo.processName);
                packageNames.add(resolveInfo.activityInfo.packageName);
                packageNames.add("com.miui.core");
            }
        }
        return packageNames;
    }

    @Override
    public void onDestroy() {
        myHandler.removeCallbacks(runnable);
        super.onDestroy();
    }
}
