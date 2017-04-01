package com.supers.clean.junk.widget;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.MainActivity;
import com.supers.clean.junk.activity.SuccessActivity;
import com.supers.clean.junk.modle.CommonUtil;

import java.util.List;

/**
 * Created by Ivy on 2017/3/28.
 */

public class WidgetProvider extends AutoUpdateWidgetProvider {
    public static final String WIDGET_PROVIDER_ACTION = "app.eosmobi.action.widget.ram";
    public static final String WIDGET_PROVIDER_UPDATE = "app.eosmobi.action.widget.update";

    boolean isxiao = true;

    private int memory;
    private RemoteViews rv;
    private AppWidgetManager manager;
    private ComponentName cn;
    private boolean isRuning;
    private ColorStateList green, yellow, red;

    private Context mContext;

    Handler handler = new Handler();


    class MyRunnable implements Runnable {
        private int internalMemory;
        private int externalMemory;

        public MyRunnable(int memory) {
            this.internalMemory = memory;
            this.externalMemory = memory;
        }

        @Override
        public void run() {
            if (isxiao) {
                isRuning = true;
                internalMemory -= 3;
                if (internalMemory < 2) {
                    internalMemory = 0;
                    isxiao = false;
                }
            } else {
                internalMemory += 3;
                if (internalMemory >= externalMemory) {
                    internalMemory = externalMemory;
                    isxiao = true;
                    isRuning = false;
                }
            }
            rv.setProgressBar(R.id.widget_progress, 100, internalMemory, false);
            if (internalMemory <= 40) {
                rv.setProgressTintList(R.id.widget_progress, green);
            } else if (internalMemory <= 80) {
                rv.setProgressTintList(R.id.widget_progress, yellow);
            } else {
                rv.setProgressTintList(R.id.widget_progress, red);
            }
            rv.setTextViewText(R.id.widget_text, internalMemory + "% ");
            manager.updateAppWidget(cn, rv);

            if (isRuning) {
                handler.postDelayed(this, 20);
            } else {
                Intent intent = new Intent(mContext, SuccessActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("size", 0);
                mContext.startActivity(intent);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //widget被从屏幕移除
    }

    @Override
    public Intent launcherService(Context context) {
        return new Intent(context, WidgetService.class);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        //最后一个widget被从屏幕移除
        context.stopService(new Intent(context, WidgetService.class));
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        //widget添加到屏幕上执行
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        init(context);

        String action = intent.getAction();
        if (TextUtils.equals(action, WIDGET_PROVIDER_ACTION)) {
            updateRemoteView();
        } else if (TextUtils.equals(action, WIDGET_PROVIDER_UPDATE)) {
            update();
        }
    }

    private void updateRemoteView() {
        if (memory <= 40) {
            rv.setProgressTintList(R.id.widget_progress, green);
        } else if (memory <= 80) {
            rv.setProgressTintList(R.id.widget_progress, yellow);
        } else {
            rv.setProgressTintList(R.id.widget_progress, red);
        }
        rv.setProgressBar(R.id.widget_progress, 100, memory, false);
        rv.setTextViewText(R.id.widget_text, memory + "% ");
        manager.updateAppWidget(cn, rv);
    }

    private void init(Context context) {
        if (mContext == null) {
            mContext = context;
            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_ram);
            manager = AppWidgetManager.getInstance(context.getApplicationContext());
            cn = new ComponentName(context, WidgetProvider.class);
            green = context.getResources().getColorStateList(R.color.widget_green);
            yellow = context.getResources().getColorStateList(R.color.widget_yellow);
            red = context.getResources().getColorStateList(R.color.widget_red);
            memory = CommonUtil.getMemory(context);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //刷新的时候执行widget
        //remoteView  AppWidgetManager
 /*
         * 构造pendingIntent发广播，onReceive()根据收到的广播，更新
         */
        init(context);

        Intent cleanIntent = new Intent("app.eosmobi.action.widget.update");
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, cleanIntent, 0);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", "notifi");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        rv.setOnClickPendingIntent(R.id.widget_clean, pendingIntent1);
        rv.setOnClickPendingIntent(R.id.widget_icon, pendingIntent);

        Log.e("rqy", "onUpdate--memory=" + memory);
        updateRemoteView();
    }

    private void update() {
        handler.post(new MyRunnable(memory));
        new Thread(new Runnable() {
            @Override
            public void run() {
                killAll(mContext);
            }
        }).start();
    }

    public void killAll(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            if (packageInfo.packageName.equals(context.getPackageName())) {
                continue;
            }
            am.killBackgroundProcesses(packageInfo.packageName);
        }
    }

}
