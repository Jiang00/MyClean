package com.supers.clean.junk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.supers.clean.junk.service.ReStarService;

/**
 */

public class BootBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            ReStarService.start(context);
            Intent serviceIntent = new Intent(context, ReStarService.class);
            context.startService(serviceIntent);
        }
    }
}
