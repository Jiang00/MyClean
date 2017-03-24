package com.eos.module.charge.saver.service;

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

import com.android.client.AndroidSdk;
import com.eos.eshop.ShopMaster;
import com.eos.module.charge.saver.R;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.module.charge.saver.Util.WidgetContainer;
import com.eos.module.charge.saver.entry.BatteryEntry;
import com.eos.module.charge.saver.view.BatteryView;
import com.eos.module.charge.saver.view.DuckView;

/**
 * Created by on 2016/10/20.
 */
public class BatteryService extends Service {
    private static final String TAG = "BatteryService";
    private boolean b = false;


    private WidgetContainer container;
    private BatteryView batteryView = null;
    private DuckView duckView = null;
    public BatteryEntry entry;

    private static final int MSG_SCREEN_ON_DELAYED = 100;
    private static final int MSG_BATTERY_CHANGE_DELAYED = 5000;

    //    private BatteryDBHelper batteryDb = new BatteryDBHelper(this);
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
//            showNotification(entry);
//            new Thread() {
//                @Override
//                public void run() {
//                    saveDataToDataBase(entry);
//                }
//            }.start();
        }
    };

//    private void dealWidthTable(BatteryDBHelper dbHelper, SQLiteDatabase database, String tableName, String pre){
//        boolean isTableExists = false;
//        ArrayList<String> tables = dbHelper.getAllTable(database);
//        for (String table : tables) {
//            if (table.startsWith(pre)) {
//                if (TextUtils.equals(table, tableName)) {
//                    isTableExists = true;
//                } else {
//                    dbHelper.deleteTable(database, table);
//                }
//            }
//        }
//
//        if (!isTableExists) {
//            if (TextUtils.equals(pre, BatteryDBHelper.TABLE_NAME_PREFIX_BATTERY_LEVEL)) {
//                dbHelper.createTableBatteryLevel(database, tableName);
//            } else if (TextUtils.equals(pre, BatteryDBHelper.TABLE_NAME_PREFIX_FULL_CHARGE)) {
//                dbHelper.createFullChargeTable(database, tableName);
//            } else if (TextUtils.equals(pre, BatteryDBHelper.TABLE_NAME_PREFIX_CHARGE_HISTROY)) {
//                dbHelper.createChargeHistoryTable(database, tableName);
//            }
//        }
//    }

//    private void saveDataToDataBase(BatteryEntry entry) {
//        String tableName = batteryDb.getBatteryLevelTableNameByTimeStamp(System.currentTimeMillis());
//        SQLiteDatabase sqdb = batteryDb.getWritableDatabase();
//        dealWidthTable(batteryDb, sqdb, tableName, BatteryDBHelper.TABLE_NAME_PREFIX_BATTERY_LEVEL);
//        boolean state = batteryDb.insertDataToTableBatteryLevel(sqdb, tableName, new BatterySaveInfo(batteryDb.getCurrentHour() + "", entry.getLevel() + ""));
//        if (state) {
//            try {
//                sendBroadcast(new Intent(Constants.ACTION_BATTERY_RECORD_HOUR_BATTERY));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (entry.getLevel() == 100) {
//            int day = batteryDb.getCurrentDate();
//            int month = batteryDb.getCurrentMonth();
//            int year = batteryDb.getCurrentYear();
//            String curTableName = BatteryDBHelper.TABLE_NAME_PREFIX_FULL_CHARGE + year + month;
//            SQLiteDatabase database = batteryDb.getWritableDatabase();
//            dealWidthTable(batteryDb, database, curTableName, BatteryDBHelper.TABLE_NAME_PREFIX_FULL_CHARGE);
//            boolean insertState = batteryDb.insertDataToTableFullCharge(sqdb, curTableName, year + ":" + month + ":" + day);
//            if (insertState) {
//                try {
//                    sendBroadcast(new Intent(Constants.ACTION_BATTERY_RECORD_FULL));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

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

//            if (TextUtils.equals("android.intent.action.ACTION_POWER_DISCONNECTED", action)) {
//                if (startLevel != -1 && startTime != -1 && startDate != null && entry != null) {
//                    final long endTime = System.currentTimeMillis();
//                    recordBatteryChargingHistory(context, entry.getLevel(), endTime);
//                }
//            }
        }
    };

//    private void recordBatteryChargingHistory(Context context, int endLevel, long endTime){
//        String curDate = new SimpleDateFormat("MM/dd").format(System.currentTimeMillis());
//        String resultDate;
//        if (TextUtils.equals(curDate, startDate)) {
//            resultDate = curDate;
//        } else {
//            resultDate = startDate + " ~ " + curDate;
//        }
//        SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
//        String startHM = hm.format(startTime);
//        String endHM = hm.format(endTime);
//        String interval = Utils.getDistanceTime(startTime, endTime);
//        if (batteryDb == null) {
//            batteryDb = new BatteryDBHelper(context);
//        }
//        SQLiteDatabase database = batteryDb.getWritableDatabase();
//        int month = batteryDb.getCurrentMonth();
//        int year = batteryDb.getCurrentYear();
//        String curTableName = BatteryDBHelper.TABLE_NAME_PREFIX_CHARGE_HISTROY + year + month;
//        dealWidthTable(batteryDb, database, curTableName, BatteryDBHelper.TABLE_NAME_PREFIX_CHARGE_HISTROY);
//        batteryDb.insetDateToTableChargeHistory(database, curTableName, new ChargeHistoryInfo(resultDate, startHM + " ~ " + endHM, startLevel, endLevel, interval));
//        startLevel = -1;
//        startTime = -1;
//        startDate = null;
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("show") && intent.getBooleanExtra("show", false)) {
            b = true;
            Log.e("battery", "1");
            showChargeView();
        }
        ShopMaster.onCreate(getApplicationContext());
        return START_STICKY_COMPATIBILITY;
    }

//    private static int startLevel = -1;
//    private static long startTime = -1;
//    private static String startDate = null;

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
        AndroidSdk.onCreate(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, intentFilter);
        AndroidSdk.track("AndroidSDK", "onCreate", BatteryService.class.getSimpleName(), 1);
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
        if (entry == null) {
            return;
        }
        AndroidSdk.track("AndroidSDK", "aaaa", BatteryService.class.getSimpleName(), 1);
        boolean isChargeScreenSaver = (boolean) Utils.readData(this, Constants.CHARGE_SAVER_SWITCH, true);
        if (!isChargeScreenSaver) {
            return;
        }
        AndroidSdk.track("AndroidSDK", "bbbb", BatteryService.class.getSimpleName(), 1);
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
        AndroidSdk.track("AndroidSDK", "ccc", BatteryService.class.getSimpleName(), 1);
        try {
            if (container == null) {
                container = new WidgetContainer.Builder()
                        .setHeight(WidgetContainer.MATCH_PARENT)
                        .setWidth(WidgetContainer.MATCH_PARENT)
                        .setOrientation(WidgetContainer.PORTRAIT)
                        .build(this);
            }
            AndroidSdk.track("AndroidSDK", "111", BatteryService.class.getSimpleName(), 1);
            if (Utils.readData(this, Constants.KEY_SAVER_TYPE, Constants.TYPE_HOR_BAR).equals(Constants.TYPE_HOR_BAR)) {
                if (batteryView == null) {
                    batteryView = (BatteryView) LayoutInflater.from(this).inflate(R.layout.charge_saver, null);
                    batteryView.bind(entry);
                    batteryView.setUnlockListener(horUnlock);
                    container.removeAllViews();
                }
                container.addView(batteryView,
                        container.makeLayoutParams(
                                WidgetContainer.MATCH_PARENT, WidgetContainer.MATCH_PARENT, Gravity.CENTER));
                container.addToWindow();
                Log.e("battery", "2");
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
            AndroidSdk.track("充电屏保", "", "", 1);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

//    private void showNotification(BatteryEntry entry) {
//        if (entry == null) {
//            return;
//        }
//        try {
//            if (Build.VERSION.SDK_INT >= 16) {
//                if ((Boolean) Utils.readData(this, Constants.CHARGE_ON_NOTIFICATION_SWITCH, true)) {
//                    ChargeOnNotification chargeOnNotification = new ChargeOnNotification(BatteryService.this, false, R.mipmap.ivy_battery_inner_icon);
//                    chargeOnNotification.updateNotification(Constants.CHARGE_ON_NOTIFICATION_ID, R.mipmap.ivy_battery_icon, entry);
//                }
//                if ((Boolean) Utils.readData(this, Constants.CHARGE_STATE_NOTIFICATION_SWITCH, true) && (entry.getLevel() == 100 || entry.getLevel() == 70 || entry.getLevel() == 20)) {
//                    ChargeFinishNotification chargeFinishNotification = new ChargeFinishNotification(BatteryService.this, true, R.mipmap.ivy_battery_inner_icon);
//                    chargeFinishNotification.updateNotification(Constants.CHARGE_FINISH_NOTIFICATION_ID, entry);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
