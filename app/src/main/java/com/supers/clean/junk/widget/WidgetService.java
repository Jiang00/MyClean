package com.supers.clean.junk.widget;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Ivy on 2017/3/28.
 */

public class WidgetService extends AutoUpdateService {


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
        intent.putExtra(AutoUpdateService.UPDATE_WIDGET_ACTION, WidgetProvider.WIDGET_PROVIDER_ACTION);
        return intent;
    }

}
