package com.supers.clean.junk.widget;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
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

            if (progressBar == null) {
                progressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
                progressBar.setIndeterminate(false);
                progressBar.setMax(100);
            }
            progressBar.setProgress(internalMemory);

            if (bitmap != null) {
                bitmap.recycle();
            }

            int progressColor;
            if (memory <= 40) {
                progressColor = R.drawable.green_progress;
            } else if (memory <= 80) {
                progressColor = R.drawable.huang_progress;
            } else {
                progressColor = R.drawable.hong_progress;
            }

            Drawable drawable = ContextCompat.getDrawable(mContext, progressColor);

            progressBar.setProgressDrawable(drawable);

            Bitmap bitmap = getViewBitmap(progressBar);

            rv.setImageViewBitmap(R.id.widget_progress, bitmap);

            rv.setTextViewText(R.id.widget_text, internalMemory + "% ");
            manager.updateAppWidget(cn, rv);

            if (isRuning) {
                handler.postDelayed(this, 20);
            } else {
                Intent intent = new Intent(mContext, SuccessActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("size", 0);
                mContext.startActivity(intent);
                Intent launcherService = launcherService(mContext);
                launcherService.putExtra(AutoUpdateService.START_UPDATE_WIDGET, true);
                mContext.startService(launcherService);
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

    ProgressBar progressBar;
    Bitmap bitmap;

    private void updateRemoteView() {
        if (progressBar == null) {
            progressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);
        }

        if (bitmap != null) {
            bitmap.recycle();
        }

        progressBar.setProgress(memory);
        int progressColor;
        if (memory <= 40) {
            progressColor = R.drawable.green_progress;
        } else if (memory <= 80) {
            progressColor = R.drawable.huang_progress;
        } else {
            progressColor = R.drawable.hong_progress;
        }

        Drawable drawable = ContextCompat.getDrawable(mContext, progressColor);
        progressBar.setProgressDrawable(drawable);

        bitmap = getViewBitmap(progressBar);
        //rv.setProgressBar(R.id.widget_progress, 100, memory, false);
        //rv.setImageViewBitmap(R.id.widget_progress, bitmap);
        rv.setImageViewBitmap(R.id.widget_progress, bitmap);
        rv.setTextViewText(R.id.widget_text, memory + "% ");
        manager.updateAppWidget(cn, rv);
    }

    public final Bitmap getViewBitmap(View view) {
        if (null == view) {
            throw new IllegalArgumentException("parameter can't be null.");
        }
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, CommonUtil.dp2px(241), CommonUtil.dp2px(29));
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    private void init(Context context) {
        if (mContext == null) {
            mContext = context;
            rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_ram);
            manager = AppWidgetManager.getInstance(context.getApplicationContext());
            cn = new ComponentName(context, WidgetProvider.class);
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

        Intent cleanIntent = new Intent(WIDGET_PROVIDER_UPDATE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, cleanIntent, 0);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", "notifi");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        rv.setOnClickPendingIntent(R.id.widget_clean, pendingIntent1);
        rv.setOnClickPendingIntent(R.id.widget_icon, pendingIntent);

        updateRemoteView();
    }

    private void update() {
        Intent launcherService = launcherService(mContext);
        launcherService.putExtra(AutoUpdateService.STOP_UPDATE_WIDGET, true);
        mContext.startService(launcherService);

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
