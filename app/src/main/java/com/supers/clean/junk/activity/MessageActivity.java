package com.supers.clean.junk.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
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
import com.eos.ui.demo.cross.CrossManager;
import com.eos.ui.demo.dialog.DialogManager;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.CameraUtils;
import com.android.clean.util.MemoryManager;
import com.supers.clean.junk.util.PhoneManager;

import java.util.Locale;

/**
 * Created by on 2017/3/2.
 */

public class MessageActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    TextView message_model, message_android_version, message_system_start_time, message_system_start_time2, message_isRoot, message_resolution,
            message_q_camera, message_h_camera, message_imei, message_ram, message_sd;
    LinearLayout ll_ad;
    FrameLayout fl_lot_message;
    LottieAnimationView lot_message;

    private TelephonyManager telManager;
    private String TAG_MESSAGE = "eos_message";
    private Handler myHandler;

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
        message_ram.setText(Util.convertStorage(ram_all,true));
        long sd_all = MemoryManager.getPhoneAllSize();
        message_sd.setText(Util.convertStorage(sd_all,true));

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
                    AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
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
}
