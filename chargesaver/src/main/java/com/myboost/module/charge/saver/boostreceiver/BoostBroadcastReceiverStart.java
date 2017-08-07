package com.myboost.module.charge.saver.receiverprivacy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.myboost.module.charge.saver.privacyprotectservice.PrivacyServiceBattery;


/**
 * Created by on 2016/12/14.
 */

public class BroadcastReceiverStartPrivacy extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, PrivacyServiceBattery.class));
    }
}
