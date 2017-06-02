package com.froumobic.module.charge.saver.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.froumobic.module.charge.saver.service.BatteryService;


/**
 * Created by on 2016/12/14.
 */

public class StartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, BatteryService.class));
    }
}
