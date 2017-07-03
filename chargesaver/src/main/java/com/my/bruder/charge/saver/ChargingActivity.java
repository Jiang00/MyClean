package com.my.bruder.charge.saver;

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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.my.bruder.charge.saver.Util.Constants;
import com.my.bruder.charge.saver.Util.Utils;
import com.my.bruder.charge.saver.entry.BatteryEntry;
import com.my.bruder.charge.saver.view.BatteringView;

public class ChargingActivity extends Activity {

    private BatteryEntry entry;
    private BatteringView batteryView;
    LinearLayout battery_more;
    LinearLayout battery_more1;
    ImageView setting_battery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
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
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.charge_exit, R.anim.charge_exit);
    }

    @Override
    protected void onUserLeaveHint() {
        this.finish();
        overridePendingTransition(R.anim.charge_exit, R.anim.charge_exit);
    }

    private void doBar() {
        try {
            batteryView = (BatteringView) LayoutInflater.from(this).inflate(R.layout.charge_saver, null);
            battery_more = (LinearLayout) batteryView.findViewById(R.id.battery_more);
            battery_more1 = (LinearLayout) batteryView.findViewById(R.id.battery_more1);
            setting_battery = (ImageView) batteryView.findViewById(R.id.setting_battery);
            battery_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    battery_more.setVisibility(View.GONE);
                    battery_more1.setVisibility(View.VISIBLE);
                }
            });
            setting_battery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((boolean) Utils.readData(ChargingActivity.this, Constants.CHARGE_SAVER_SWITCH, false)) {
                        Utils.writeData(ChargingActivity.this, Constants.CHARGE_SAVER_SWITCH, false);
                        setting_battery.setImageResource(R.mipmap.side_check_normal1);
                    } else {
                        Utils.writeData(ChargingActivity.this, Constants.CHARGE_SAVER_SWITCH, true);
                        setting_battery.setImageResource(R.mipmap.side_check_passed3);
                    }
                }
            });
            setContentView(batteryView);
            batteryView.setUnlockListener(new BatteringView.UnlockListener() {
                @Override
                public void onUnlock() {
                    ChargingActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, R.anim.charge_exit);
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected void onDestroy() {
        batteryView = null;
        super.onDestroy();
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    protected void onPause() {
        super.onPause();
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
}
