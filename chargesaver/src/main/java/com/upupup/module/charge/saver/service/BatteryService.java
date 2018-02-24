package com.upupup.module.charge.saver.service;

import android.annotation.TargetApi;
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
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import com.android.client.AndroidSdk;
import com.upupup.clean.util.CleanConstant;
import com.upupup.clean.util.PreData;
import com.upupup.module.charge.saver.Aview.BatteryView;
import com.upupup.module.charge.saver.BatteryActivity;
import com.upupup.module.charge.saver.DetectActivity;
import com.upupup.module.charge.saver.R;
import com.upupup.module.charge.saver.Util.Constants;
import com.upupup.module.charge.saver.Util.GetTopPackage;
import com.upupup.module.charge.saver.Util.Utils;
import com.upupup.module.charge.saver.Util.WidgetContainer;
import com.upupup.module.charge.saver.entry.BatteryEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2016/10/20.
 */
public class BatteryService extends Service {
    private boolean b = false;


    private WidgetContainer container;
    private BatteryView batteryView = null;
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
            if (batteryView != null) {
                batteryView.bind(entry);
            }
        }
    };
    private GetTopPackage topPackage;
    private List<String> lunchPackage;
    private JSONObject jsonObject;

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
                long time = System.currentTimeMillis();
                PreData.putDB(BatteryService.this, CleanConstant.CONNECTED_TIME, time);
            } else if (Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_POWER_CONNECTED.equals(action)) {
//                mHandler.removeCallbacks(runnable);
//                mHandler.postDelayed(runnable, MSG_SCREEN_ON_DELAYED);
                showChargeView();
            }
            Log.e("chfq", "==mReceiver==");
            if (!PreData.getDB(BatteryService.this, CleanConstant.DETECT_KAIGUAN, true)) {
                return;
            }
            Log.e("chfq", "==mReceiver=2=");
            if (TextUtils.equals(Intent.ACTION_POWER_CONNECTED, action)) {
                PreData.putDB(BatteryService.this, CleanConstant.CONNECTED_CHA, true);

            } else if (TextUtils.equals(Intent.ACTION_POWER_DISCONNECTED, action)) {
                Log.e("battery", "ACTION_POWER_DISCONNECTED==1");
                if (entry == null) {
                    return;
                }
                //发送通知
                long time_now = System.currentTimeMillis();
                long connected_time = (long) PreData.getDB(BatteryService.this, CleanConstant.CONNECTED_TIME, time_now);
                long chongdian_time = time_now - connected_time;
                Log.e("battery", "ACTION_POWER_DISCONNECTED==" + chongdian_time);
                try {
                    jsonObject = new JSONObject(AndroidSdk.getExtraData());
                    Log.e("chfq", "ACTION_POWER_DISCONNECTED==" + jsonObject.getInt("auto_time"));
                    Log.e("chfq", "ACTION_POWER_DISCONNECTED==");
                    if (chongdian_time <= jsonObject.getInt("auto_time") * 1000 * 1000) {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("chfq", "ACTION_POWER_DISCONNECTED==");
                long leftUseTime = entry.getLeftUseTime() * 1000;
                int level = entry.getLevel();
                int connected_level = PreData.getDB(BatteryService.this, CleanConstant.CONNECTED_LEVEL, 0);
                Log.e("battery", "connected_level==" + connected_level);
                PreData.putDB(BatteryService.this, CleanConstant.CONNECTED_TIME_LUN, chongdian_time);
                PreData.putDB(BatteryService.this, CleanConstant.CONNECTED_LEVEL_LUN, level - connected_level);
                PreData.putDB(BatteryService.this, CleanConstant.CONNECTED_LEFT_TIME_LUN, leftUseTime);
                if (level == 100) {
                    if ((time_now - PreData.getDB(BatteryService.this, CleanConstant.CONNECTED_GUO, time_now)) > 60 * 60 * 1000) {
                        PreData.putDB(BatteryService.this, CleanConstant.CONNECTED_ZZ, 2);
                    } else {
                        PreData.putDB(BatteryService.this, CleanConstant.CONNECTED_ZZ, 1);
                    }
                } else {
                    PreData.putDB(BatteryService.this, CleanConstant.CONNECTED_ZZ, 0);
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
        topPackage = new GetTopPackage(this);
        lunchPackage = new ArrayList<>();
        lunchPackage = getLaunchers();
    }

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
        unregisterReceiver(mReceiver);
        try {
            Intent localIntent = new Intent();
            localIntent.setClass(this, BatteryService.class);
            this.startService(localIntent);
        } catch (Exception e) {
        }
    }

    private void showChargeView() {
        Log.e("battery", "showChargeView");
        boolean isChargeScreenSaver = (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, true);
        if (!isChargeScreenSaver) {
            return;
        }
        if (true) {
            if (entry == null || !entry.isCharging()) {
                return;
            }
            Intent intent = new Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClass(this, BatteryActivity.class);
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


}
