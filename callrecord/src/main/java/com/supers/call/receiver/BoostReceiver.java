package com.supers.call.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.supers.call.service.PhoneService;


/**
 * Created by Ivy on 2017/4/27.
 */

public class BoostReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, PhoneService.class));
    }
}
