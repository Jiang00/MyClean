package com.eos.module.charge.saver.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.RemoteViews;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.eos.eshop.ShopMaster;
import com.eos.module.charge.saver.ChargeActivity;
import com.eos.module.charge.saver.DetectActivity;
import com.eos.module.charge.saver.R;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.GetTopPackage;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.module.charge.saver.Util.WidgetContainer;
import com.eos.module.charge.saver.entry.BatteryEntry;
import com.eos.module.charge.saver.view.BatteryView;
import com.eos.module.charge.saver.view.DuckView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2016/10/20.
 */
public class BatteryService extends Service {
    private boolean b = false;


    private WidgetContainer container;
    private BatteryView batteryView = null;
    private DuckView duckView = null;
    public BatteryEntry entry;
    private GetTopPackage topPackage;
    private List<String> lunchPackage;
    NotificationManager notificationManager;

    private static final int MSG_SCREEN_ON_DELAYED = 100;
    private static final int MSG_BATTERY_CHANGE_DELAYED = 5000;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            boolean isScreenOn = isScreenOn();
            if (isScreenOn) {
                showChargeView();
            } else {
                if (container != null) {
                    container.removeFromWindow();
                }
            }
        }
    };

    private Runnable batteryChangeRunnable = new Runnable() {
        @Override
        public void run() {
            if (duckView != null) {
                duckView.bind(entry);
            }
            if (batteryView != null) {
                batteryView.bind(entry);
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();
        } else {
            return powerManager.isScreenOn();
        }
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(Intent.ACTION_BATTERY_CHANGED, action)) {
                int oldLevel = -1;
                if (entry != null) {
                    oldLevel = entry.getLevel();
                }
                batteryChange(intent);
                int level = entry.getLevel();
                if (PreData.getDB(BatteryService.this, Constant.CONNECTED_CHA, false)) {
                    PreData.putDB(BatteryService.this, Constant.CONNECTED_CHA, false);
                    long time = System.currentTimeMillis();
                    if (level == -1) {
                        level = 100;
                    }
                    PreData.putDB(BatteryService.this, Constant.CONNECTED_TIME, time);
                    PreData.putDB(BatteryService.this, Constant.CONNECTED_LEVEL, level);
                }
                if (oldLevel != level && level == 100 && PreData.getDB(BatteryService.this, Constant.DETECT_KAIGUAN, true)) {
                    int connected = (int) PreData.getDB(BatteryService.this, Constant.CONNECTED_LEVEL, 0);
                    PreData.putDB(BatteryService.this, Constant.CONNECTED_GUO, System.currentTimeMillis());
                    if (connected < 90) {
                        //发送通知
                        long leftUseTime = entry.getLeftUseTime() * 1000;
                        long time = PreData.getDB(BatteryService.this, Constant.CONNECTED_TIME, System.currentTimeMillis() - 60 * 60 * 1000);
                        PreData.putDB(BatteryService.this, Constant.CONNECTED_TIME_MAIN, System.currentTimeMillis() - time);
                        PreData.putDB(BatteryService.this, Constant.CONNECTED_LEVEL_MAIN, 100 - connected);
                        PreData.putDB(BatteryService.this, Constant.CONNECTED_LEFT_TIME_MAIN, leftUseTime);
                        tonghzi_();
                    }
                }
                mHandler.removeCallbacks(batteryChangeRunnable);
                mHandler.postDelayed(batteryChangeRunnable, MSG_BATTERY_CHANGE_DELAYED);
            } else if (Intent.ACTION_SCREEN_OFF.equals(action) || Intent.ACTION_POWER_CONNECTED.equals(action)) {
                showChargeView();
            }
            if (!PreData.getDB(BatteryService.this, Constant.DETECT_KAIGUAN, true)) {
                return;
            }
            if (TextUtils.equals(Intent.ACTION_POWER_CONNECTED, action)) {
                PreData.putDB(BatteryService.this, Constant.CONNECTED_CHA, true);

            } else if (TextUtils.equals(Intent.ACTION_POWER_DISCONNECTED, action)) {
                Log.e("battery", "ACTION_POWER_DISCONNECTED==1");
                if (entry == null) {
                    return;
                }
                //发送通知
                long time_now = System.currentTimeMillis();
                long connected_time = (long) PreData.getDB(BatteryService.this, Constant.CONNECTED_TIME, time_now);
                long chongdian_time = time_now - connected_time;
                Log.e("battery", "ACTION_POWER_DISCONNECTED==" + chongdian_time);
                if (chongdian_time <= 60 * 5 * 1000) {
                    return;
                }
                long leftUseTime = entry.getLeftUseTime() * 1000;
                int level = entry.getLevel();
                int connected_level = PreData.getDB(BatteryService.this, Constant.CONNECTED_LEVEL, 0);
                Log.e("battery", "connected_level==" + connected_level);
                PreData.putDB(BatteryService.this, Constant.CONNECTED_TIME_LUN, chongdian_time);
                PreData.putDB(BatteryService.this, Constant.CONNECTED_LEVEL_LUN, level - connected_level);
                PreData.putDB(BatteryService.this, Constant.CONNECTED_LEFT_TIME_LUN, leftUseTime);
                if (level == 100) {
                    if ((time_now - PreData.getDB(BatteryService.this, Constant.CONNECTED_GUO, time_now)) > 60 * 60 * 1000) {
                        PreData.putDB(BatteryService.this, Constant.CONNECTED_ZZ, 0);
                    } else {
                        PreData.putDB(BatteryService.this, Constant.CONNECTED_ZZ, 1);
                    }
                } else {
                    PreData.putDB(BatteryService.this, Constant.CONNECTED_ZZ, 2);
                }
                mHandler.post(runnableDialog);
            }

        }
    };
    Runnable runnableDialog = new Runnable() {
        @Override
        public void run() {
            String packageName = topPackage.execute();
            Log.e("runnableDialog", "===1" + packageName);
            if (lunchPackage.contains(packageName) || TextUtils.equals(getPackageName(), packageName)) {
                Log.e("runnableDialog", "===");
                startActivity(new Intent(BatteryService.this, DetectActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                mHandler.postDelayed(this, 1000);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void tonghzi_() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        RemoteViews remoteView = new RemoteViews(getPackageName(),
                R.layout.layout_tongzhi_detect);
        int requestCode = (int) SystemClock.uptimeMillis();
        Intent notifyIntentGBoost = new Intent("com.eos.clean.main");
        notifyIntentGBoost.putExtra("from", "detect");
        notifyIntentGBoost.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendIntent = PendingIntent.getActivity(this, requestCode,
                notifyIntentGBoost, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContent(remoteView);
        mBuilder.setContentIntent(pendIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(false);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setSmallIcon(R.mipmap.notification_title);
        Notification notification_gboost = mBuilder.build();
        notification_gboost.defaults = Notification.DEFAULT_SOUND;
        notification_gboost.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(104, notification_gboost);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("show") && intent.getBooleanExtra("show", false)) {
            b = true;
            showChargeView();
        }
        ShopMaster.onCreate(getApplicationContext());
        return START_STICKY_COMPATIBILITY;
    }

    public void batteryChange(Intent intent) {
        if (entry == null) {
            entry = new BatteryEntry(this, intent);
        } else {
            entry.update(intent);
            entry.evaluate();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, intentFilter);
        topPackage = new GetTopPackage(this);
        lunchPackage = new ArrayList<>();
        lunchPackage = getLaunchers();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(104);
        unregisterReceiver(mReceiver);
        try {
            Intent localIntent = new Intent();
            localIntent.setClass(this, BatteryService.class);
            this.startService(localIntent);
        } catch (Exception e) {
        }
    }

    private void showChargeView() {
        boolean isChargeScreenSaver = (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, false);
        if (!isChargeScreenSaver) {
            return;
        }
        if ((Boolean) Utils.readData(this, Constants.IS_ACTIVITY, true)) {
            if (entry == null || !entry.isCharging()) {
                return;
            }
            Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Utils.readData(this, Constants.KEY_SAVER_TYPE, Constants.TYPE_HOR_BAR).equals(Constants.TYPE_HOR_BAR)) {
                intent.putExtra("type", "bar");
            } else {
                intent.putExtra("type", "duck");
            }
            intent.setClass(this, ChargeActivity.class);
            startActivity(intent);
            return;
        }

        if (entry == null) {
            return;
        }
        boolean isCharging = entry.isCharging();
        if (!isCharging && !b) {
            if (batteryView != null) {
                batteryView.bind(entry);
            }
            if (duckView != null) {
                duckView.bind(entry);
            }
            return;
        }
        try {
            if (container == null) {
                container = new WidgetContainer.Builder()
                        .setHeight(WidgetContainer.MATCH_PARENT)
                        .setWidth(WidgetContainer.MATCH_PARENT)
                        .setOrientation(WidgetContainer.PORTRAIT)
                        .build(this);
            }
            if (Utils.readData(this, Constants.KEY_SAVER_TYPE, Constants.TYPE_HOR_BAR).equals(Constants.TYPE_HOR_BAR)) {
                if (batteryView == null) {
                    batteryView = (BatteryView) LayoutInflater.from(this).inflate(R.layout.charge_saver, null);
                    batteryView.bind(entry);
                    batteryView.setUnlockListener(horUnlock);
                }
                container.removeAllViews();
                container.addView(batteryView,
                        container.makeLayoutParams(
                                WidgetContainer.MATCH_PARENT, WidgetContainer.MATCH_PARENT, Gravity.CENTER));
                container.addToWindow();
            } else if (Utils.readData(this, Constants.KEY_SAVER_TYPE, Constants.TYPE_HOR_BAR).equals(Constants.TYPE_DUCK)) {
                if (duckView == null) {
                    duckView = (DuckView) LayoutInflater.from(this).inflate(R.layout.charge_duck_view, null);
                    duckView.bind(entry);
                    duckView.setUnlockListener(duckUnlock);
                }
                container.removeAllViews();
                container.addView(duckView,
                        container.makeLayoutParams(
                                WidgetContainer.MATCH_PARENT, WidgetContainer.MATCH_PARENT, Gravity.CENTER));
                container.addToWindow();
            }
        } catch (Exception e) {
        }
    }

    BatteryView.UnlockListener horUnlock = new BatteryView.UnlockListener() {
        @Override
        public void onUnlock() {
            if (container != null) {
                container.removeFromWindow();
                container.removeAllViews();
                container = null;
                batteryView = null;
            }
        }
    };

    DuckView.UnlockListener duckUnlock = new DuckView.UnlockListener() {
        @Override
        public void onUnlock() {
            if (container != null) {
                container.removeFromWindow();
                container.removeAllViews();
                container = null;
                duckView = null;
            }
        }
    };


}
