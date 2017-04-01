package com.supers.clean.junk.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * Created by renqingyou on 2017/3/30.
 */

public abstract class AutoUpdateWidgetProvider extends AppWidgetProvider {
    public abstract Intent launcherService(Context context);

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent bootService = launcherService(context);
        if (bootService == null) {
            throw new Error("boot service intent can not be null");
        }
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
                bootService, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
        bootService.putExtra(AutoUpdateService.STOP_UPDATE_WIDGET, true);
        context.startService(bootService);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent bootService = launcherService(context);
        if (bootService == null) {
            throw new Error("boot service name can not be null");
        }
        context.startService(bootService);
    }
}
