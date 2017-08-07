package com.myboost.module.charge.saver.boostreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.myboost.module.charge.saver.boostprotectservice.ServiceBatteryBoost;


/**
 * Created by on 2016/12/14.
 */

public class BoostBroadcastReceiverStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceBatteryBoost.class));
    }
}
