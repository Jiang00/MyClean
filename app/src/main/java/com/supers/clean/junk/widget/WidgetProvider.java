package com.supers.clean.junk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.MainActivity;
import com.supers.clean.junk.modle.CommonUtil;

/**
 * Created by Ivy on 2017/3/28.
 */

public class WidgetProvider extends AppWidgetProvider {
    private RemoteViews rv;
    private ColorStateList green, yellow, red;

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        //widget被从屏幕移除
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
        context.startService(new Intent(context, WidgetService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        green = context.getResources().getColorStateList(R.color.widget_green);
        yellow = context.getResources().getColorStateList(R.color.widget_yellow);
        red = context.getResources().getColorStateList(R.color.widget_red);
        if (TextUtils.equals(intent.getAction(), "app.eosmobi.action.widget.ram")) {
            int memory = intent.getIntExtra("memory", CommonUtil.getMemory(context));
            if (rv == null) {
                rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_ram);
            }
            if (memory <= 40) {
                rv.setProgressTintList(R.id.widget_progress, green);
            } else if (memory <= 80) {
                rv.setProgressTintList(R.id.widget_progress, yellow);
            } else {
                rv.setProgressTintList(R.id.widget_progress, red);
            }
            rv.setProgressBar(R.id.widget_progress, 100, memory, false);
            rv.setTextViewText(R.id.widget_text, memory + "% ");
            AppWidgetManager manager = AppWidgetManager.getInstance(context.getApplicationContext());
            ComponentName cn = new ComponentName(context.getApplicationContext(), WidgetProvider.class);
            manager.updateAppWidget(cn, rv);
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
        green = context.getResources().getColorStateList(R.color.widget_green);
        yellow = context.getResources().getColorStateList(R.color.widget_yellow);
        red = context.getResources().getColorStateList(R.color.widget_red);
        Intent intent1 = new Intent(context, WidgetService.class);
        PendingIntent pendingIntent1 = PendingIntent.getService(context, 0, intent1, 0);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("from", "notifi");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        rv = new RemoteViews(context.getPackageName(), R.layout.layout_widget_ram);
        rv.setOnClickPendingIntent(R.id.widget_clean, pendingIntent1);
        rv.setOnClickPendingIntent(R.id.widget_icon, pendingIntent);
        int memory = CommonUtil.getMemory(context);
        if (memory <= 40) {
            rv.setProgressTintList(R.id.widget_progress, green);
        } else if (memory <= 80) {
            rv.setProgressTintList(R.id.widget_progress, yellow);
        } else {
            rv.setProgressTintList(R.id.widget_progress, red);
        }
        rv.setProgressBar(R.id.widget_progress, 100, memory, false);
        rv.setTextViewText(R.id.widget_text, memory + "% ");
        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

}
