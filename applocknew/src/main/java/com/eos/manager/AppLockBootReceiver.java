package com.eos.manager;

import android.content.*;

import com.eos.manager.meta.MApps;
import com.eos.manager.meta.SecurityMyPref;
import com.eos.manager.lib.Utils;

/**
 * Created by superjoy on 2014/8/25.
 */
public class AppLockBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String act = intent.getAction();
        Utils.LOGER("on receive " + intent.toString());
        switch (act) {
            case Intent.ACTION_BOOT_COMPLETED:
                context.startService(new Intent(context, AppLockEosService.class));
                break;
            case Intent.ACTION_PACKAGE_REMOVED:{
                if (intent.getDataString() == null || intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) return;
                final String pkg = intent.getDataString().substring("package:".length());
                if (pkg.startsWith(context.getPackageName())) {
                    //do nothing for our skins and ourselves
                    return;
                }
                MApps.removed(pkg);
            }
                break;
            case Intent.ACTION_PACKAGE_ADDED:{
                if (intent.getDataString() == null || intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) return;
                final String pkg = intent.getDataString().substring("package:".length());
                if (pkg.startsWith(context.getPackageName())) {
                    //do nothing for our skins and ourselves
                    return;
                }
                MApps.add(context, pkg);
                SharedPreferences sp = App.getSharedPreferences();
                if (sp.getBoolean(SecurityMyPref.LOCK_NEW, SecurityMyPref.LOCK_DEFAULT)){
                    context.startService(new Intent(context, AppLockEosService.class).putExtra(AppLockEosService.WORK_EXTRA_KEY, AppLockEosService.WORK_LOCK_NEW).putExtra("pkg", pkg));
                }
            }
                break;
            case Intent.ACTION_USER_PRESENT:
                context.startService(new Intent(context, AppLockEosService.class).putExtra("on", true));
                break;
            case Intent.ACTION_MEDIA_MOUNTED:
                context.startService(new Intent(context, AppLockEosService.class));
                break;
        }
    }
}
