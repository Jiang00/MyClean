package com.supers.clean.junk.widget;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.SuccessActivity;
import com.supers.clean.junk.modle.CommonUtil;

import java.util.List;
import java.util.Timer;

/**
 * Created by Ivy on 2017/3/28.
 */

public class WidgetService extends AutoUpdateService {
    private Handler handler;
    private KillBackg killBackg;
    private int memory, memory_kill;
    private RemoteViews rv;
    private AppWidgetManager manager;
    private ComponentName cn;
    private boolean isRuning;
    private ColorStateList green, yellow, red;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public Intent createAlarmIntent(Context context) {
        Intent intent = new Intent(context, WidgetService.class);
        /**
         * Service通过alarm manager定时启动
         */
        intent.putExtra(AutoUpdateService.SERVICE_UPDATE, 30 * 60 * 1000);
        /**
         * widget更新时间
         */
        intent.putExtra(AutoUpdateService.WIDGET_UPDATE, 5 * 1000);
        /**
         * 更新widget 广播action
         */
        intent.putExtra(AutoUpdateService.UPDATE_WIDGET_ACTION, WidgetProvider.TEMP_PROVIDER_ACTION);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        rv = new RemoteViews(getPackageName(), R.layout.layout_widget_ram);
        manager = AppWidgetManager.getInstance(getApplicationContext());
        cn = new ComponentName(getApplicationContext(), WidgetProvider.class);
        green = WidgetService.this.getResources().getColorStateList(R.color.widget_green);
        yellow = WidgetService.this.getResources().getColorStateList(R.color.widget_yellow);
        red = WidgetService.this.getResources().getColorStateList(R.color.widget_red);
        killBackg = new KillBackg() {
            @Override
            public void killS(int memory, long M) {
                WidgetService.this.memory_kill = memory;
            }
        };

    }

    boolean isxiao = true;
    Runnable runnableT = new Runnable() {
        @Override
        public void run() {
            if (isxiao) {
                isRuning = true;
                memory -= 3;
                if (memory < 2) {
                    memory = 0;
                    isxiao = false;
                }
            } else {
                memory += 3;
                if (memory > memory_kill) {
                    memory = memory_kill;
                    isxiao = true;
                    isRuning = false;
                }
            }

            rv.setProgressBar(R.id.widget_progress, 100, memory, false);
            if (memory <= 40) {
                rv.setProgressTintList(R.id.widget_progress, green);
            } else if (memory <= 80) {
                rv.setProgressTintList(R.id.widget_progress, yellow);
            } else {
                rv.setProgressTintList(R.id.widget_progress, red);
            }
            rv.setTextViewText(R.id.widget_text, memory + "% ");
            manager.updateAppWidget(cn, rv);
            if (isRuning) {
                handler.postDelayed(runnableT, 20);
            } else {
                startActivity(new Intent(WidgetService.this, SuccessActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("size", 0));
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRuning) {
            updata();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void updata() {
        handler.post(runnableT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                killAll(WidgetService.this);
            }
        }).start();

    }

    public void killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        final long beforeMem = getAvailMemory(am);
        for (PackageInfo packageInfo : installedPackages) {
            if (packageInfo.packageName.equals(context.getPackageName())) {
                continue;
            }
            am.killBackgroundProcesses(packageInfo.packageName);
        }
        final long afterMem = getAvailMemory(am);
        final long M = (afterMem - beforeMem);
        if (killBackg != null) {
            killBackg.killS(CommonUtil.getMemory(context), M);
        }
    }

    private long getAvailMemory(ActivityManager am) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return (mi.availMem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    interface KillBackg {
        void killS(int memory, long M);
    }
}
