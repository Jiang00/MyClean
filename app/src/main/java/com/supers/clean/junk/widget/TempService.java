package com.supers.clean.junk.widget;

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
        return new Intent(context, TempService.class);
    }

    @Override
    public void updateWidget() {
        //定时发送广播更新widget
        sendBroadcast(new Intent(TempProvider.TEMP_PROVIDER_ACTION));
    }
}
