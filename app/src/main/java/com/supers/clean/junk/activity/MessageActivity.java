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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.eos.module.charge.saver.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CameraUtils;
import com.supers.clean.junk.modle.CommonUtil;
import com.supers.clean.junk.modle.MemoryManager;
import com.supers.clean.junk.modle.PhoneManager;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.UtilGp;
import com.supers.clean.junk.modle.entity.Contents;

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
        lot_message = (LottieAnimationView) findViewById(R.id.lot_message);
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

        message_system_start_time.setText(CommonUtil.getStrTime(time));

        message_system_start_time2.setText(CommonUtil.millTransFate2(SystemClock.elapsedRealtime()));

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

    @Override
    public void tuiGuang() {
        super.tuiGuang();
        if (!CommonUtil.isPkgInstalled(tuiguang, getPackageManager())) {
            lot_message.setImageAssetsFolder("images/applocks/");
            lot_message.setAnimation("applocks.json");
            lot_message.loop(true);
            lot_message.playAnimation();

        } else if (!CommonUtil.isPkgInstalled(tuiguang1, getPackageManager())) {
            lot_message.setImageAssetsFolder("images/flashs/");
            lot_message.setAnimation("flashs.json");
            lot_message.loop(true);
            lot_message.playAnimation();

        } else {
            lot_message.setVisibility(View.GONE);
        }
        lot_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CommonUtil.isPkgInstalled(tuiguang, getPackageManager())) {
                    AndroidSdk.track("硬件信息页面", "推广applock点击", "", 1);
                    UtilGp.openPlayStore(MessageActivity.this, tuiguang);
                } else if (!CommonUtil.isPkgInstalled(tuiguang1, getPackageManager())) {
                    AndroidSdk.track("硬件信息页面", "推广手电筒点击", "", 1);
                    UtilGp.openPlayStore(MessageActivity.this, tuiguang1);
                }
            }
        });
    }

    private void loadAd() {
        if (PreData.getDB(this, Contents.FULL_MESSAGE, 0) == 1) {
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
        View nativeView = CommonUtil.getNativeAdView(TAG_MESSAGE, R.layout.native_ad);
        if (ll_ad != null && nativeView != null) {
            ViewGroup.LayoutParams layout_ad = ll_ad.getLayoutParams();
            if (nativeView.getHeight() == CommonUtil.dp2px(250)) {
                layout_ad.height = CommonUtil.dp2px(250);
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
}
