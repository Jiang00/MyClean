package com.security.module.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.security.module.service.BatteryService;


/**
 * Created by on 2016/12/14.
 */

public class QidongReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, BatteryService.class));
    }
}
