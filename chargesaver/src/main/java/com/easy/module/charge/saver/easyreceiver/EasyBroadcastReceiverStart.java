package com.easy.module.charge.saver.easyreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.easy.module.charge.saver.easyprotectservice.EasyServiceBattery;


/**
 * Created by on 2016/12/14.
 */

public class EasyBroadcastReceiverStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, EasyServiceBattery.class));
    }
}
