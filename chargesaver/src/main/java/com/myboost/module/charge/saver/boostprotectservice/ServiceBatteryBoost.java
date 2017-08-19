package com.myboost.module.charge.saver.boostprotectservice;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;

import com.myboost.module.charge.saver.BoostBatteryProtectActivity;
import com.myboost.module.charge.saver.R;
import com.myboost.module.charge.saver.entry.BoostBatteryEntry;
import com.myboost.module.charge.saver.boostutils.BoostBatteryConstants;
import com.myboost.module.charge.saver.boostutils.BoostWidgetContainer;
import com.myboost.module.charge.saver.boostutils.BatteryUtils;
import com.myboost.module.charge.saver.protectview.ProtectBatteryView;


/**
 * Created by on 2016/10/20.
 */
public class ServiceBatteryBoost extends Service {
    private boolean b = false;


    private BoostWidgetContainer container;
    private ProtectBatteryView batteryView = null;
    public BoostBatteryEntry entry;

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
                batteryChange(intent);
                mHandler.removeCallbacks(batteryChangeRunnable);
                mHandler.postDelayed(batteryChangeRunnable, MSG_BATTERY_CHANGE_DELAYED);
            } else if (Intent.ACTION_POWER_CONNECTED.equals(action) || Intent.ACTION_SCREEN_ON.equals(action)) {
                showChargeView();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("show") && intent.getBooleanExtra("show", false)) {
            b = true;
            showChargeView();
        }
        return START_STICKY_COMPATIBILITY;
    }

    public void batteryChange(Intent intent) {
        if (entry == null) {
            entry = new BoostBatteryEntry(this, intent);
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
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        try {
            Intent localIntent = new Intent();
            localIntent.setClass(this, ServiceBatteryBoost.class);
            this.startService(localIntent);
        } catch (Exception e) {
        }
    }

    private void showChargeView() {
        boolean isChargeScreenSaver = (boolean) BatteryUtils.readData(this, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true);
        if (!isChargeScreenSaver) {
            return;
        }
        if ((Boolean) BatteryUtils.readData(this, BoostBatteryConstants.IS_ACTIVITY, true)) {
            if (entry == null || !entry.isCharging()) {
                return;
            }
            Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (BatteryUtils.readData(this, BoostBatteryConstants.KEY_SAVER_TYPE, BoostBatteryConstants.TYPE_HOR_BAR).equals(BoostBatteryConstants.TYPE_HOR_BAR)) {
                intent.putExtra("type", "bar");
            } else {
                intent.putExtra("type", "duck");
            }
            intent.setClass(this, BoostBatteryProtectActivity.class);
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
            return;
        }
        try {
            if (container == null) {
                container = new BoostWidgetContainer.Builder()
                        .setHeight(BoostWidgetContainer.MATCH_PARENT)
                        .setWidth(BoostWidgetContainer.MATCH_PARENT)
                        .setOrientation(BoostWidgetContainer.PORTRAIT)
                        .build(this);
            }
            if (BatteryUtils.readData(this, BoostBatteryConstants.KEY_SAVER_TYPE, BoostBatteryConstants.TYPE_HOR_BAR).equals(BoostBatteryConstants.TYPE_HOR_BAR)) {
                if (batteryView == null) {
                    batteryView = (ProtectBatteryView) LayoutInflater.from(this).inflate(R.layout.charge_saver, null);
                    batteryView.bind(entry);
                    batteryView.setUnlockListener(horUnlock);
                }
                container.removeAllViews();
                container.addView(batteryView,
                        container.makeLayoutParams(
                                BoostWidgetContainer.MATCH_PARENT, BoostWidgetContainer.MATCH_PARENT, Gravity.CENTER));
                container.addToWindow();
            }
        } catch (Exception e) {
        }
    }

    ProtectBatteryView.UnlockListener horUnlock = new ProtectBatteryView.UnlockListener() {
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


}
