package com.eos.module.charge.saver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.eos.module.charge.saver.entry.BatteryEntry;
import com.eos.module.charge.saver.view.BatteryView;
import com.eos.module.charge.saver.view.DuckView;

public class ChargeActivity extends Activity {


    private BatteryView batteryView;
    private DuckView duckView;
    private BatteryEntry entry;
    private boolean isBar;

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

    private void doBar (){
        batteryView = (BatteryView) LayoutInflater.from(this).inflate(R.layout.charge_saver, null);
        setContentView(batteryView);
        batteryView.setUnlockListener(new BatteryView.UnlockListener() {
            @Override
            public void onUnlock() {
                ChargeActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, R.anim.charge_exit);
            }
        });
    }

    private void doDuck(){
        duckView = (DuckView) LayoutInflater.from(this).inflate(R.layout.charge_duck_view, null);
        setContentView(duckView);
        duckView.setUnlockListener(new DuckView.UnlockListener() {
            @Override
            public void onUnlock() {
                ChargeActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, R.anim.charge_exit);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        String type;
        try{
            type = getIntent().getExtras().getString("type");
        } catch (Exception e) {
            type = "bar";
        }
        if (TextUtils.equals(type, "bar")) {
            isBar = true;
            doBar();
        } else if (TextUtils.equals(type, "duck")) {
            isBar = false;
            doDuck();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        batteryView = null;
        duckView = null;
        super.onDestroy();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(Intent.ACTION_BATTERY_CHANGED, intent.getAction())) {
                batteryChange(intent);
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
        if (isBar) {
            if (batteryView != null) {
                batteryView.bind(entry);
            }
        } else {
            if (duckView != null) {
                duckView.bind(entry);
            }
        }
    }

}
