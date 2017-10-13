package com.my.bruder.charge.saver.service;

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

import com.my.bruder.charge.saver.ChargingActivity;
import com.my.bruder.charge.saver.R;
import com.my.bruder.charge.saver.Util.Constants;
import com.my.bruder.charge.saver.Util.Utils;
import com.my.bruder.charge.saver.Util.WidgetContainer;
import com.my.bruder.charge.saver.entry.BatteryEntry;
import com.my.bruder.charge.saver.view.BatteringView;

/**
 * 电池服务
 * Created by on 2016/10/20.
 */
public class BatteringService extends Service {
    private static final int MSG_SCREEN_ON_DELAYED = 100;
    private static final int MSG_BATTERY_CHANGE_DELAYED = 5000;
    private WidgetContainer container;
    private BatteringView batteryView = null;
    private boolean b = false;
    private Handler mHandler = new Handler();
    public BatteryEntry entry;

    private Runnable batteryChangeRunnable = new Runnable() {
        @Override
        public void run() {
            if (batteryView != null) {
                batteryView.bind(entry);
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            boolean isScreenOn = isScreenOn();//获取电源状态
            if (isScreenOn) {
                showChargeView();
            } else {
                if (container != null) {
                    container.removeFromWindow();
                }
            }
        }
    };

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(Intent.ACTION_BATTERY_CHANGED, action)) {
                batteryChange(intent);
                mHandler.removeCallbacks(batteryChangeRunnable);
                mHandler.postDelayed(batteryChangeRunnable, MSG_BATTERY_CHANGE_DELAYED);
            } else if (Intent.ACTION_POWER_CONNECTED.equals(action) || Intent.ACTION_SCREEN_ON.equals(action)) {  //修改部分ACTION_POWER_CONNECTED
                showChargeView();
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private boolean isScreenOn() {
        // PowerManager这个类主要是用来控制电源状态的
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) { // KITKAT_WATCH对应 API 20
            return powerManager.isInteractive();
        } else {
            return powerManager.isScreenOn();
        }
    }

    public void batteryChange(Intent intent) {
        if (entry == null) {
            entry = new BatteryEntry(this, intent);
        } else {
            entry.update(intent);
            entry.evaluate();
        }
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
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("show") && intent.getBooleanExtra("show", false)) {
            b = true;
            showChargeView();
        }
        return START_STICKY_COMPATIBILITY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
            intent.setClass(this, ChargingActivity.class);
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
                container = new WidgetContainer.Builder()
                        .setHeight(WidgetContainer.MATCH_PARENT)
                        .setWidth(WidgetContainer.MATCH_PARENT)
                        .setOrientation(WidgetContainer.PORTRAIT)
                        .build(this);
            }
            if (Utils.readData(this, Constants.KEY_SAVER_TYPE, Constants.TYPE_HOR_BAR).equals(Constants.TYPE_HOR_BAR)) {
                if (batteryView == null) {
                    batteryView = (BatteringView) LayoutInflater.from(this).inflate(R.layout.charge_saver, null);
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

    BatteringView.UnlockListener horUnlock = new BatteringView.UnlockListener() {
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

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        try {
            Intent localIntent = new Intent();
            localIntent.setClass(this, BatteringService.class);
            this.startService(localIntent);
        } catch (Exception e) {
        }
    }
}