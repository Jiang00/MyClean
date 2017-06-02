package com.supers.clean.junk.Zwidget;

import android.content.Context;
import android.content.Intent;

/**
 * Created by renqingyou on 2017/3/30.
 */

public class TempService extends AutoUpdateService {

    /**
     * alarmManager定时启动Service
     */
    @Override
    public Intent createAlarmIntent(Context context) {
        Intent intent = new Intent(context, TempService.class);
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
        intent.putExtra(AutoUpdateService.UPDATE_WIDGET_ACTION, TempProvider.TEMP_PROVIDER_ACTION);
        return intent;
    }
}
