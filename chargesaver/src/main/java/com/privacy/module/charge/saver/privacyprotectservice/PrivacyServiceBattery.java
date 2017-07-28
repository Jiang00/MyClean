package com.privacy.module.charge.saver.privacyprotectservice;

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

import com.privacy.module.charge.saver.BatteryProtectActivityPrivacy;
import com.privacy.module.charge.saver.R;
import com.privacy.module.charge.saver.entry.PrivacyBatteryEntry;
import com.privacy.module.charge.saver.privacyutils.BatteryConstantsPrivacy;
import com.privacy.module.charge.saver.privacyutils.PrivacyWidgetContainer;
import com.privacy.module.charge.saver.privacyutils.UtilsPrivacy;
import com.privacy.module.charge.saver.protectview.ProtectBatteryView;


/**
 * Created by on 2016/10/20.
 */
public class PrivacyServiceBattery extends Service {
    private boolean b = false;


    private PrivacyWidgetContainer container;
    private ProtectBatteryView batteryView = null;
    public PrivacyBatteryEntry entry;

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
            } else if (Intent.ACTION_SCREEN_OFF.equals(action) || Intent.ACTION_SCREEN_ON.equals(action)) {
                mHandler.removeCallbacks(runnable);
                mHandler.postDelayed(runnable, MSG_SCREEN_ON_DELAYED);
            } else {
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
            entry = new PrivacyBatteryEntry(this, intent);
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
            localIntent.setClass(this, PrivacyServiceBattery.class);
            this.startService(localIntent);
        } catch (Exception e) {
        }
    }

    private void showChargeView() {
        boolean isChargeScreenSaver = (boolean) UtilsPrivacy.readData(this, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true);
        if (!isChargeScreenSaver) {
            return;
        }
        if ((Boolean) UtilsPrivacy.readData(this, BatteryConstantsPrivacy.IS_ACTIVITY, true)) {
            if (entry == null || !entry.isCharging()) {
                return;
            }
            Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (UtilsPrivacy.readData(this, BatteryConstantsPrivacy.KEY_SAVER_TYPE, BatteryConstantsPrivacy.TYPE_HOR_BAR).equals(BatteryConstantsPrivacy.TYPE_HOR_BAR)) {
                intent.putExtra("type", "bar");
            } else {
                intent.putExtra("type", "duck");
            }
            intent.setClass(this, BatteryProtectActivityPrivacy.class);
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
                container = new PrivacyWidgetContainer.Builder()
                        .setHeight(PrivacyWidgetContainer.MATCH_PARENT)
                        .setWidth(PrivacyWidgetContainer.MATCH_PARENT)
                        .setOrientation(PrivacyWidgetContainer.PORTRAIT)
                        .build(this);
            }
            if (UtilsPrivacy.readData(this, BatteryConstantsPrivacy.KEY_SAVER_TYPE, BatteryConstantsPrivacy.TYPE_HOR_BAR).equals(BatteryConstantsPrivacy.TYPE_HOR_BAR)) {
                if (batteryView == null) {
                    batteryView = (ProtectBatteryView) LayoutInflater.from(this).inflate(R.layout.charge_saver, null);
                    batteryView.bind(entry);
                    batteryView.setUnlockListener(horUnlock);
                }
                container.removeAllViews();
                container.addView(batteryView,
                        container.makeLayoutParams(
                                PrivacyWidgetContainer.MATCH_PARENT, PrivacyWidgetContainer.MATCH_PARENT, Gravity.CENTER));
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
