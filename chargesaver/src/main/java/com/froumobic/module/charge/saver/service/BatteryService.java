package com.froumobic.module.charge.saver.service;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import com.froumobic.module.charge.saver.ChargeActivity;
import com.froumobic.module.charge.saver.R;
import com.froumobic.module.charge.saver.Util.Constants;
import com.froumobic.module.charge.saver.Util.Utils;
import com.froumobic.module.charge.saver.Util.WidgetContainer;
import com.froumobic.module.charge.saver.entry.BatteryEntry;
import com.froumobic.module.charge.saver.view.BatteryView;

/**
 * Created by on 2016/10/20.
 */
public class BatteryService extends Service {
    private boolean b = false;


    private WidgetContainer container;
    private BatteryView batteryView = null;
    //    private DuckView duckView = null;
    public BatteryEntry entry;

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
//            if (duckView != null) {
//                duckView.bind(entry);
//            }
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
            } else if (Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_POWER_CONNECTED.equals(action)) {
//                mHandler.removeCallbacks(runnable);
//                mHandler.postDelayed(runnable, MSG_SCREEN_ON_DELAYED);
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
    }

    @Override
    public void onDestroy() {
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
            Intent intent = new Intent(this, ChargeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
//            if (duckView != null) {
//                duckView.bind(entry);
//            }
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

//    DuckView.UnlockListener duckUnlock = new DuckView.UnlockListener() {
//        @Override
//        public void onUnlock() {
//            if (container != null) {
//                container.removeFromWindow();
//                container.removeAllViews();
//                container = null;
//                duckView = null;
//            }
//        }
//    };


}
