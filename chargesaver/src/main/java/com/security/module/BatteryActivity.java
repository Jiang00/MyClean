package com.security.module;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.client.AndroidSdk;
import com.security.module.entry.BatteryEntry;
import com.security.module.mView.BatteryView;

public class BatteryActivity extends Activity {


    private BatteryEntry entry;
    private BatteryView batteryView;
    private long time;


    protected void hideBottomUIMenu() {
        try {
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
                View v = this.getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception e) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
        this.finish();
        overridePendingTransition(R.anim.charge_exit, R.anim.charge_exit);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.charge_exit, R.anim.charge_exit);
    }

    private void doBar() {
        try {
            batteryView = (BatteryView) LayoutInflater.from(this).inflate(R.layout.charge_saver, null);
            setContentView(batteryView);
            batteryView.setUnlockListener(new BatteryView.UnlockListener() {
                @Override
                public void onUnlock() {
                    BatteryActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, R.anim.charge_exit);
                }
            });
        } catch (Exception e) {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        time = System.currentTimeMillis();
        AndroidSdk.track("充电屏保", "展示", "", 1);
        String type;
        try {
            type = getIntent().getExtras().getString("type");
        } catch (Exception e) {
            type = "bar";
        }
        if (TextUtils.equals(type, "bar")) {
            doBar();
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        batteryView = null;
        if (time != 0 && System.currentTimeMillis() - time > 3 * 1000) {
            AndroidSdk.track("充电屏保", "超3秒展示", "", 1);
        }
        super.onDestroy();
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(Intent.ACTION_BATTERY_CHANGED, intent.getAction())) {
                batteryChange(intent);
            } else if (TextUtils.equals(Intent.ACTION_SCREEN_ON, intent.getAction()) || TextUtils.equals(Intent.ACTION_POWER_CONNECTED, intent.getAction())) {
                Log.d("MyTest", "ON  batteryView = " + batteryView);
                if (batteryView != null) {
                    batteryView.reStartBubble();
                }
            } else if (TextUtils.equals(Intent.ACTION_SCREEN_OFF, intent.getAction()) || TextUtils.equals(Intent.ACTION_POWER_DISCONNECTED, intent.getAction())) {
                Log.d("MyTest", "OFF  batteryView = " + batteryView);
                if (batteryView != null) {
                    batteryView.pauseBubble();
                }
            }
        }
    };

    public void batteryChange(Intent intent) {
        if (entry == null) {
            entry = new BatteryEntry(this, intent);
        } else {
            entry.update(intent);
            entry.evaluate();
        }
        if (batteryView != null) {
            batteryView.bind(entry);
        }
    }

}
