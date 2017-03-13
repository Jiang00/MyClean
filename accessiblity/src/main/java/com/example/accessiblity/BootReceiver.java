package com.example.accessiblity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by song on 15/9/1.
 */
public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Boot receiver ", intent + "");
        context.startService(new Intent(context, Accessibility.class));
    }
}
