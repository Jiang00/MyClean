package com.eifmobi.charge.saver;

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
import com.eifmobi.charge.saver.Aview.BatteryView;
import com.eifmobi.charge.saver.entry.BatteryEntry;

public class BatteryActivity extends Activity {


    private BatteryView batteryView;
    private BatteryEntry entry;
    private long time;

    @Override
    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
        this.finish();
        overridePendingTransition(com.eifmobi.charge.saver.R.anim.charge_exit, com.eifmobi.charge.saver.R.anim.charge_exit);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        this.finish();
        overridePendingTransition(com.eifmobi.charge.saver.R.anim.charge_exit, com.eifmobi.charge.saver.R.anim.charge_exit);
    }


    protected void hideBottomUIMenu() {
        try {
            if (Build.VERSION.SDK_INT < 19) {
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

    private void doBar() {
        try {
            batteryView = (BatteryView) LayoutInflater.from(this).inflate(com.eifmobi.charge.saver.R.layout.charge_saver, null);
            setContentView(batteryView);
            batteryView.setUnlockListener(new BatteryView.UnlockListener() {
                @Override
                public void onUnlock() {
                    overridePendingTransition(android.R.anim.fade_in, com.eifmobi.charge.saver.R.anim.charge_exit);
                    finish();
                }
            });
            Log.e("battery", "bar===");
        } catch (Exception e) {
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("battery", "onCreate");
        time = System.currentTimeMillis();
        AndroidSdk.track("充电屏保", "展示", "", 1);
        hideBottomUIMenu();
        doBar();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
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
        Log.e("battery", "onDestroy");
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
            } else if (TextUtils.equals(Intent.ACTION_SCREEN_ON, intent.getAction())) {
                Log.d("MyTest", "ON  batteryView = " + batteryView);
                if (batteryView != null) {
                    batteryView.reStartBubble();
                }
            } else if (TextUtils.equals(Intent.ACTION_SCREEN_OFF, intent.getAction())) {
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
