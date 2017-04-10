package com.supers.clean.junk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.supers.clean.junk.activity.UnloadActivity;


/**
 * Created by on 2017/2/6.
 */

public class UnloadBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            context.startActivity(new Intent(context, UnloadActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("packageName", packageName));
//                }
//            }
        }
    }
}
