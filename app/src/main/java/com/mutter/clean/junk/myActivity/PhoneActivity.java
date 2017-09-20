package com.mutter.clean.junk.myActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mutter.clean.util.MemoryManager;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.android.client.AndroidSdk;
import com.sample.lottie.LottieAnimationView;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myview.MessageRoundView;
import com.mutter.clean.junk.myview.MessageRoundViewCpu;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.CameraUtils;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.CpuTempReader;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by on 2017/3/2.
 */

public class PhoneActivity extends BaseActivity {
    TextView message_brand, message_model, message_version, message_cpu, message_resolution,
            message_q_camera, message_h_camera, message_ram, message_sd;
    LinearLayout ll_ad;
    LottieAnimationView lot_message;
    MessageRoundView message_r_sd, message_r_ram;
    FrameLayout title_left;
    TextView message_text_sd, message_text_ram, message_text_cpu;
    TextView message_button_sd, message_button_ram, message_button_cpu;
    TextView title_name;
    MessageRoundViewCpu message_r_cpu;

    private TelephonyManager telManager;
    private String TAG_MESSAGE = "mutter_message";
    private Handler myHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        message_model = (TextView) findViewById(R.id.message_model);
        message_version = (TextView) findViewById(R.id.message_version);
        message_resolution = (TextView) findViewById(R.id.message_resolution);
        message_q_camera = (TextView) findViewById(R.id.message_q_camera);
        message_h_camera = (TextView) findViewById(R.id.message_h_camera);
        message_ram = (TextView) findViewById(R.id.message_ram);
        message_sd = (TextView) findViewById(R.id.message_sd);
        message_brand = (TextView) findViewById(R.id.message_brand);
        message_cpu = (TextView) findViewById(R.id.message_cpu);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
        message_r_sd = (MessageRoundView) findViewById(R.id.message_r_sd);
        message_r_ram = (MessageRoundView) findViewById(R.id.message_r_ram);
        message_r_cpu = (MessageRoundViewCpu) findViewById(R.id.message_r_cpu);
        message_text_sd = (TextView) findViewById(R.id.message_text_sd);
        message_text_ram = (TextView) findViewById(R.id.message_text_ram);
        message_text_cpu = (TextView) findViewById(R.id.message_text_cpu);
        message_button_sd = (TextView) findViewById(R.id.message_button_sd);
        message_button_ram = (TextView) findViewById(R.id.message_button_ram);
        message_button_cpu = (TextView) findViewById(R.id.message_button_cpu);
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
        message_brand.setText(Build.BRAND);
        message_version.setText(Build.VERSION.RELEASE);

//        long time = System.currentTimeMillis() - SystemClock.elapsedRealtime();
//        message_system_start_time2.setText(Util.millTransFate2(SystemClock.elapsedRealtime()));
        message_cpu.setText(getPhoneCpuNumber() + getString(R.string.message_h));

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
        initN();

        message_button_sd.setOnClickListener(clickListener);
        message_button_ram.setOnClickListener(clickListener);
        message_button_cpu.setOnClickListener(clickListener);
    }

    private void initN() {
        //SD卡储存
        long sd_all = MemoryManager.getPhoneAllSize();
        long sd_kongxian = MemoryManager.getPhoneAllFreeSize();
        long sd_shiyong = sd_all - sd_kongxian;
        int sd_me = (int) (sd_shiyong * 100 / sd_all);
        String sd_size = Util.convertStorage(sd_shiyong, true) + "/" + Util.convertStorage(sd_all, true);
        message_sd.setText(Util.convertStorage(sd_all, true));
        message_r_sd.setProgress(sd_me);
        message_text_sd.setText(sd_size);
        //ram使用
        long ram_kongxian = MemoryManager.getPhoneFreeRamMemory(this);
        long ram_all = MemoryManager.getPhoneTotalRamMemory();
        long ram_shiyong = ram_all - ram_kongxian;
        int memo = (int) (ram_shiyong * 100 / ram_all);
        String ram_size = Util.convertStorage(ram_shiyong, true) + "/" + Util.convertStorage(ram_all, true);
        message_ram.setText(Util.convertStorage(ram_all, true));
        message_r_ram.setProgress(memo);
        message_text_ram.setText(ram_size);

        //cpu温度
        CpuTempReader.getCPUTemp(new CpuTempReader.TemperatureResultCallback() {
            @Override
            public void callbackResult(CpuTempReader.ResultCpuTemperature result) {
                int cpuTemp = 40;
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();
                }
                final int finalCpuTemp = cpuTemp;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message_r_cpu.setProgress(finalCpuTemp);
                        if (finalCpuTemp > 60) {
                            message_text_cpu.setText(R.string.message_hot);
                        }
                    }
                });
            }
        });
    }

    private void loadAd() {
        if (PreData.getDB(this, Constant.FULL_MESSAGE, 0) == 1) {
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AndroidSdk.showFullAd(AdUtil.FULL_DEFAULT);
                }
            }, 1000);
        } else {
            addAd();
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.message_button_sd:
                    jumpTo(CleanActivity.class);
                    break;
                case R.id.message_button_ram:
                    jumpTo(RamAvtivity.class);
                    break;
                case R.id.message_button_cpu:
                    jumpTo(JiangwenActivity.class);
                    break;
            }
        }
    };


    private void addAd() {
        View nativeView = AdUtil.getNativeAdView(TAG_MESSAGE, R.layout.native_ad_3);
        if (ll_ad != null && nativeView != null) {
            ll_ad.addView(nativeView);
            ll_ad.setVisibility(View.VISIBLE);
        } else {
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


    //    cpu数量
    public int getPhoneCpuNumber() {
        class CpuFilter implements FileFilter {
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
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
    protected void onRestart() {
        super.onRestart();
        initN();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lot_message != null) {
            lot_message.pauseAnimation();
        }
    }
}
