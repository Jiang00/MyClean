package com.supers.clean.junk.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.android.internal.os.PowerProfile;
import com.eos.module.charge.saver.service.BatteryService;
import com.eos.ui.demo.cross.CrossManager;
import com.eos.ui.demo.dialog.DialogManager;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.CameraUtils;
import com.android.clean.util.MemoryManager;
import com.supers.clean.junk.util.PhoneManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by on 2017/3/2.
 */

public class MessageActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    TextView message_model, message_android_version, message_system_start_time, message_system_start_time2, message_isRoot, message_resolution,
            message_q_camera, message_h_camera, message_imei, message_ram, message_sd;
    TextView message_cpu_1, message_cpu_2, message_cpu_3;
    TextView message_battery_1, message_battery_2, message_battery_3, message_battery_4, message_battery_5;
    TextView message_acc, message_temp, message_gyr, message_light, message_field,
            message_pressure, message_proxi, message_humi, message_orienation, message_gravity, message_line_acc, message_rotate;
    TextView message_button_cpu, message_button_cooling, message_button_ram;
    LinearLayout ll_ad;
    FrameLayout fl_lot_message;
    LottieAnimationView lot_message;

    private TelephonyManager telManager;
    private String TAG_MESSAGE = "eos_message";
    private Handler myHandler;
    private int batteryCapacity;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        message_model = (TextView) findViewById(R.id.message_model);
        message_android_version = (TextView) findViewById(R.id.message_android_version);
        message_system_start_time = (TextView) findViewById(R.id.message_system_start_time);
        message_system_start_time2 = (TextView) findViewById(R.id.message_system_start_time2);
        message_isRoot = (TextView) findViewById(R.id.message_isRoot);
        message_resolution = (TextView) findViewById(R.id.message_resolution);
        message_q_camera = (TextView) findViewById(R.id.message_q_camera);
        message_h_camera = (TextView) findViewById(R.id.message_h_camera);
        message_imei = (TextView) findViewById(R.id.message_imei);
        message_ram = (TextView) findViewById(R.id.message_ram);
        message_sd = (TextView) findViewById(R.id.message_sd);
        message_cpu_1 = (TextView) findViewById(R.id.message_cpu_1);
        message_cpu_2 = (TextView) findViewById(R.id.message_cpu_2);
        message_cpu_3 = (TextView) findViewById(R.id.message_cpu_3);
        message_battery_1 = (TextView) findViewById(R.id.message_battery_1);
        message_battery_2 = (TextView) findViewById(R.id.message_battery_2);
        message_battery_3 = (TextView) findViewById(R.id.message_battery_3);
        message_battery_4 = (TextView) findViewById(R.id.message_battery_4);
        message_battery_5 = (TextView) findViewById(R.id.message_battery_5);
        message_button_cpu = (TextView) findViewById(R.id.message_button_cpu);
        message_button_cooling = (TextView) findViewById(R.id.message_button_cooling);
        message_button_ram = (TextView) findViewById(R.id.message_button_ram);
        message_acc = (TextView) findViewById(R.id.message_acc);
        message_temp = (TextView) findViewById(R.id.message_temp);
        message_gyr = (TextView) findViewById(R.id.message_gyr);
        message_light = (TextView) findViewById(R.id.message_light);
        message_field = (TextView) findViewById(R.id.message_field);
        message_pressure = (TextView) findViewById(R.id.message_pressure);
        message_proxi = (TextView) findViewById(R.id.message_proxi);
        message_humi = (TextView) findViewById(R.id.message_humi);
        message_orienation = (TextView) findViewById(R.id.message_orienation);
        message_gravity = (TextView) findViewById(R.id.message_gravity);
        message_line_acc = (TextView) findViewById(R.id.message_line_acc);
        message_rotate = (TextView) findViewById(R.id.message_rotate);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        fl_lot_message = (FrameLayout) findViewById(R.id.fl_lot_message);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message);
        myHandler = new Handler();
        loadAd();
        title_name.setText(R.string.main_msg_title);
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        message_model.setText(Build.MODEL);
        message_android_version.setText("Android " + Build.VERSION.RELEASE);

        long time = System.currentTimeMillis() - SystemClock.elapsedRealtime();

        message_system_start_time.setText(Util.getStrTime(time));

        message_system_start_time2.setText(Util.millTransFate2(SystemClock.elapsedRealtime()));

        message_isRoot.setText(PhoneManager.isRoot() == true ? R.string.message_root : R.string.message_not_root);

        message_resolution.setText(getResolution());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String q = CameraUtils.getCameraPixels(CameraUtils.HasFrontCamera());
                final String h = CameraUtils.getCameraPixels(CameraUtils.HasBackCamera());

                if (isW()) {
                    try {
                        final double q_w = Double.valueOf(q) / 100;
                        final double h_w = Double.valueOf(h) / 100;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                message_q_camera.setText(q_w + getString(R.string.message_pix));
                                message_h_camera.setText(h_w + getString(R.string.message_pix));
                            }
                        });
                    } catch (Exception e) {
                        Log.e("aa", "像素为空");
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            message_q_camera.setText(q + getString(R.string.message_pix));
                            message_h_camera.setText(h + getString(R.string.message_pix));
                        }
                    });
                }

            }
        }).start();

        message_imei.setText(getPhoneIMEI());

        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        message_ram.setText(Util.convertStorage(ram_all, true));
        long sd_all = MemoryManager.getPhoneAllSize();
        message_sd.setText(Util.convertStorage(sd_all, true));


        setData();
        initBattery();
        initSensor();
        initListener();
    }

    private void initListener() {
        message_button_cpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, CoolingActivity.class));
            }
        });
        message_button_cooling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, CoolingActivity.class));
            }
        });
        message_button_ram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, JunkAndRamActivity.class));
            }
        });
    }

    private void initSensor() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);  //获取系统的传感器服务并创建实例

        List<Sensor> list = sm.getSensorList(Sensor.TYPE_ALL);  //获取传感器的集合
        ArrayList<Integer> tapes = new ArrayList<>();
        for (Sensor sensor : list) {
            tapes.add(sensor.getType());
        }
        if (tapes.contains(Sensor.TYPE_ACCELEROMETER)) {
            message_acc.setText(R.string.message_25);
        } else {
            message_acc.setText(R.string.message_26);
            message_acc.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_AMBIENT_TEMPERATURE)) {
            message_temp.setText(R.string.message_25);
        } else {
            message_temp.setText(R.string.message_26);
            message_temp.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_GYROSCOPE)) {
            message_gyr.setText(R.string.message_25);
        } else {
            message_gyr.setText(R.string.message_26);
            message_gyr.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_LIGHT)) {
            message_light.setText(R.string.message_25);
        } else {
            message_light.setText(R.string.message_26);
            message_light.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_MAGNETIC_FIELD)) {
            message_field.setText(R.string.message_25);
        } else {
            message_field.setText(R.string.message_26);
            message_field.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_PRESSURE)) {
            message_pressure.setText(R.string.message_25);
        } else {
            message_pressure.setText(R.string.message_26);
            message_pressure.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_PROXIMITY)) {
            message_proxi.setText(R.string.message_25);
        } else {
            message_proxi.setText(R.string.message_26);
            message_proxi.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_RELATIVE_HUMIDITY)) {
            message_humi.setText(R.string.message_25);
        } else {
            message_humi.setText(R.string.message_26);
            message_humi.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_ORIENTATION)) {
            message_orienation.setText(R.string.message_25);
        } else {
            message_orienation.setText(R.string.message_26);
            message_orienation.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_GRAVITY)) {
            message_gravity.setText(R.string.message_25);
        } else {
            message_gravity.setText(R.string.message_26);
            message_gravity.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_LINEAR_ACCELERATION)) {
            message_line_acc.setText(R.string.message_25);
        } else {
            message_line_acc.setText(R.string.message_26);
            message_line_acc.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
        if (tapes.contains(Sensor.TYPE_ROTATION_VECTOR)) {
            message_rotate.setText(R.string.message_25);
        } else {
            message_rotate.setText(R.string.message_26);
            message_rotate.setTextColor(ContextCompat.getColor(this, R.color.A2));
        }
    }

    private void initBattery() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiver, intentFilter);
        PowerProfile power = new PowerProfile(MessageActivity.this);
        batteryCapacity = (int) power.getBatteryCapacity();
        message_battery_2.setText(String.valueOf(batteryCapacity) + "mAh");
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            String tec = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            message_battery_1.setText(batteryCapacity * level / 100 + "mAh");
            DecimalFormat decimalFormat = new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(voltage / 1000f);//format 返回的是字符串
            message_battery_3.setText(p + "V");
            message_battery_4.setText(temp / 10 + "℃");
            message_battery_5.setText(tec);
        }
    };

    private void setData() {
        PhoneManager phoneManager = PhoneManager.getPhoneManage(this);
        message_cpu_1.setText(phoneManager.getCpuName());
        message_cpu_2.setText(phoneManager.getPhoneCpuNumber() + "");
        message_cpu_3.setText(Integer.parseInt(phoneManager.getPhoneCpuMinFreq()) / 1000 + "MHz" +
                "-" + Integer.parseInt(phoneManager.getPhoneCpuMaxFreq()) / 1000 + "MHz");

    }

    @Override
    public void tuiGuang() {
        super.tuiGuang();
        DialogManager.getCrossView(getApplicationContext(), extraData, "list1", "message", true, new CrossManager.onCrossViewClickListener() {
            @Override
            public void onClick(View view) {

            }

            @Override
            public void onLoadView(View view) {
                if (view != null) {
                    ((ImageView) view.findViewById(R.id.cross_default_image)).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    lot_message = ((LottieAnimationView) view.findViewById(R.id.cross_default_lottie));
                    lot_message.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    fl_lot_message.setVisibility(View.VISIBLE);
                    if (onPause) {
                        lot_message.pauseAnimation();
                    }
                    fl_lot_message.addView(view, 0);
                } else {
                    fl_lot_message.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadAd() {
        if (PreData.getDB(this, Constant.FULL_MESSAGE, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AdUtil.DEFAULT);
                }
            }, 1000);
            tuiGuang();
        } else {
            addAd();
        }
    }

    private void addAd() {
        View nativeView = AdUtil.getNativeAdView(TAG_MESSAGE, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            if (nativeView.getHeight() == Util.dp2px(250)) {
                layout_ad.height = Util.dp2px(250);
            }
            ll_ad.setLayoutParams(layout_ad);
            ll_ad.addView(nativeView);
        } else {
            tuiGuang();
        }
    }

    private boolean isW() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("en") || language.endsWith("es"))
            return true;
        else
            return false;
    }

    //获取手机分辨率
    public String getResolution() {
        String resolution = "";
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        resolution = metrics.widthPixels + "*" + (metrics.heightPixels);
        return resolution;
    }

    /**
     * 设备串号 permission.READ_PHONE_STATE
     */
    public String getPhoneIMEI() {
        // 检查是否有权限
        if (PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, getPackageName())) {
            String s = telManager.getDeviceId();
            return s;//getDeviceId
        } else {
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidSdk.onResumeWithoutTransition(this);
        if (lot_message != null) {
            lot_message.playAnimation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lot_message != null) {
            lot_message.pauseAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
