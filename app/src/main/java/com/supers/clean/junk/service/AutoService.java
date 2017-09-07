package com.supers.clean.junk.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.android.clean.db.CleanDBHelper;
import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.ShortCutActivity;
import com.supers.clean.junk.presenter.FloatStateManager;
import com.supers.clean.junk.presenter.GetTopApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/7.
 */

public class AutoService extends Service {
    private List<String> hmoes;
    private GetTopApp topApp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler myHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (myHandler == null) {
            myHandler = new Handler();
        }
        topApp = new GetTopApp(this);
        hmoes = getLaunchers();

        return START_REDELIVER_INTENT;
    }

    int count = 0;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count++;
            String pkg = topApp.execute();
            Log.e("autoservice", pkg + "==");
            if (hmoes.contains(pkg)) {
                count = 0;
                startActivity(new Intent(AutoService.this, ShortCutActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("from", "auto").putExtra("time", time_diff));
            } else if (count <= 20) {
                myHandler.postDelayed(runnable, 2000);
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("autoservice", "onCreate");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, intentFilter);
    }

    private long time_diff;
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("autoservice", "onReceive1");
            if (!PreData.getDB(AutoService.this, Constant.AUTO_KAIGUAN, false) && PreData.hasDB(AutoService.this, Constant.AUTO_KAIGUAN)) {
                return;
            }
            Log.e("autoservice", "onReceive2");
            String action = intent.getAction();
            if (TextUtils.equals(Intent.ACTION_SCREEN_ON, action)) {
                long this_time = System.currentTimeMillis();
                long time = PreData.getDB(AutoService.this, Constant.AUTO_TIME, System.currentTimeMillis());
                time_diff = this_time - time;
                int default_time = 10;
                try {
                    JSONObject jsonObject = new JSONObject(AndroidSdk.getExtraData());
                    default_time = jsonObject.optInt("auto_time");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (time_diff > default_time * 60 * 1000) {
                    myHandler.post(runnable);
                } else {
                    PreData.putDB(AutoService.this, Constant.AUTO_TIME, this_time);
                }
            } else if (TextUtils.equals(Intent.ACTION_SCREEN_OFF, action)) {
                PreData.putDB(AutoService.this, Constant.AUTO_TIME, System.currentTimeMillis());
            }
        }
    };

    public List<String> getLaunchers() {
        List<String> packageNames = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (activityInfo != null) {
                packageNames.add(resolveInfo.activityInfo.processName);
                packageNames.add(resolveInfo.activityInfo.packageName);
                packageNames.add("com.miui.core");
            }
        }
        return packageNames;
    }

    @Override
    public void onDestroy() {
        myHandler.removeCallbacks(runnable);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
