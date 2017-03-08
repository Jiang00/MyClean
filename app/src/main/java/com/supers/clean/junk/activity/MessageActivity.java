package com.supers.clean.junk.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CameraUtils;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.MemoryManager;
import com.supers.clean.junk.modle.PhoneManager;

import java.util.Locale;

/**
 * Created by Ivy on 2017/3/2.
 */

public class MessageActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    TextView message_model, message_android_version, message_system_start_time, message_system_start_time2, message_isRoot, message_resolution,
            message_q_camera, message_h_camera, message_imei, message_ram, message_sd;
    LinearLayout ll_ad;
    private View nativeView;
    private TelephonyManager telManager;
    private String TAG_MESSAGE = "junk_message";

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
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message);
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

        message_system_start_time.setText(CommonUtil.getStrTime(SystemClock.elapsedRealtime()));

        message_system_start_time2.setText(CommonUtil.millTransFate2(time));

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
        message_ram.setText(CommonUtil.getFileSize1(ram_all));
        long sd_all = MemoryManager.getPhoneAllSize();
        message_sd.setText(CommonUtil.getFileSize1(sd_all));
    }

    private void addAd() {
        if (AndroidSdk.hasNativeAd(TAG_MESSAGE, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
            if (nativeView != null)
                AndroidSdk.destroyNativeAdView(TAG_MESSAGE, nativeView);
            nativeView = AndroidSdk.peekNativeAdViewWithLayout(TAG_MESSAGE, AndroidSdk.NATIVE_AD_TYPE_ALL, R.layout.native_ad, null);

            if (nativeView != null && ll_ad != null) {
                ll_ad.addView(nativeView);
            } else {
                ll_ad.setVisibility(View.GONE);
            }
        } else {
            ll_ad.setVisibility(View.GONE);
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
}
