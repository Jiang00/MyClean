package com.myboost.module.charge.saver.protectview;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.myboost.module.charge.saver.SetADActivityBoost;
import com.myboost.module.charge.saver.R;
import com.myboost.module.charge.saver.boostutils.GetCpuTempReaderBoost;
import com.myboost.module.charge.saver.entry.BoostBatteryEntry;
import com.myboost.module.charge.saver.boostutils.BoostBatteryConstants;
import com.myboost.module.charge.saver.boostutils.BoostADRequest;
import com.myboost.module.charge.saver.boostutils.BatteryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProtectBatteryView extends FrameLayout {

    private Context mContext;
    private GestureDetector detector;
    private float distance;
    private boolean isBindView = false;
    private boolean isRegisterTimeUpdate = false;
    private View adView;
    private ProtectBatteryView.UnlockListener unlockListener;
    private ProtectBatteryView batteryView;
    private LinearLayout adLayout;
    private TextView title;
    private LinearLayout more;
    private LinearLayout switchLayout;
    private CheckBox saverSwitch;
    private TextView time;
    private TextView date;
    private TextView battery_now_year;
    private TextView day;
    private TextView week;
    private TextView batteryLeft, batteryLeft1;
    LinearLayout battery_charge_time;
    AWaterView battery_le;
    MWaterView battery_ram;
    TextView battery_temp;
    private BubbleLayoutBoost bubbleLayout;

    private int halfWidth;
    private int cpuTemp;

    public interface UnlockListener {
        void onUnlock();
    }

    public ProtectBatteryView(Context context) {
        super(context, null);
    }

    public ProtectBatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ProtectBatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUnlockListener(UnlockListener unlockListener) {
        this.unlockListener = unlockListener;
    }

    private BroadcastReceiver timerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTime();
        }
    };

    private IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);

    private void showNativeAD() {
        adView = new BoostADRequest().showCustomNativeAD(BoostBatteryConstants.TAG_CHARGING, R.layout.native_ad, null);
        if (adLayout != null && adView != null) {
            if (adLayout.getVisibility() == View.GONE) {
                adLayout.setVisibility(VISIBLE);
            }
            if (adView.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) adView.getParent();
                viewGroup.removeAllViews();
            }
            adLayout.removeAllViews();
            adLayout.addView(adView);
        }
        adLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startX = event.getX();
                detector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if ((event.getX() - startX) > 20 || (startX - event.getX()) > 20) {
                            if ((event.getX() - startX) > halfWidth / 2) {
                                if (unlockListener != null) {
//                                    unlockListener.onUnlock();
                                }
                            }
                            return true;
                        } else {
                            break;
                        }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }

    public void updateTime() {
        if (time != null && date != null && week != null) {
            try {
                long t = System.currentTimeMillis();
                Date d = new Date(t);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String str = sdf.format(d);
                time.setText(str);
                str = new SimpleDateFormat("yy", Locale.getDefault()).format(d);
                battery_now_year.setText(str + "/");
                str = new SimpleDateFormat("MM", Locale.getDefault()).format(d);
                date.setText(str + "/");
                str = new SimpleDateFormat("dd", Locale.getDefault()).format(d);
                day.setText(str);
                str = new SimpleDateFormat("EEEE").format(d);
                week.setText(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int progress = 0;
    boolean flag = false;
    int i = 0;

    public static int getMemory(Context context) {
        long ram_kongxian = getPhoneFreeRamMemory(context);
        long ram_all = getPhoneTotalRamMemory();
        int memory = (int) ((ram_all - ram_kongxian) * 100 / ram_all);
        return memory;
    }

    /**
     * 设备空闲运行内存 单位B
     */
    public static long getPhoneFreeRamMemory(Context context) {
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        am.getMemoryInfo(info);
        return info.availMem;
    }

    /**
     * 设备完整运行内存 单位B
     */
    public static long getPhoneTotalRamMemory() {
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split("\\s+");
            return Long.valueOf(array[1]) * 1024; // 原为kb, 转为b
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void bind(BoostBatteryEntry entry) {
        if (entry == null) {
            return;
        }
        final int curLevel = entry.getLevel();
        battery_le.upDate(curLevel);

        int leftChargeTime = entry.getLeftTime();
        if (batteryLeft != null && batteryLeft1 != null) {
            if (curLevel == 100) {
                if (battery_charge_time != null) {
                    battery_charge_time.setVisibility(GONE);
                }
            } else {
                batteryLeft.setText(entry.extractHours(leftChargeTime) + "");
                batteryLeft1.setText(entry.extractMinutes(leftChargeTime) + "");
            }
        }

    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setCharing(boolean isVisible) {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isBindView) {
            initViews();
            isBindView = true;
            updateTime();

            showNativeAD();

            halfWidth = (int) (((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() / 3f);
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (switchLayout != null && switchLayout.getVisibility() == View.VISIBLE) {
                        more.setVisibility(View.VISIBLE);
                        switchLayout.setVisibility(GONE);
                    }
                    detector.onTouchEvent(event);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (distance > halfWidth) {
                            if (unlockListener != null) {
                                batteryView.setAlpha(1.0f);
                                unlockListener.onUnlock();
                            }
                        } else {
                            if (batteryView != null) {
                                batteryView.setAlpha(1.0f);
                            }
                        }
                    }
                    return true;
                }
            });
            detector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (unlockListener != null) {
                        batteryView.setAlpha(1.0f);
//                        unlockListener.onUnlock();
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    //e1.getX()是按下的位置
                    if (e2.getX() - e1.getX() == 100) {
                        return true;
                    }

                    distance = e2.getX() - e1.getX();
                    if (batteryView != null) {
//                        batteryView.setTranslationX(distance);
                        batteryView.setAlpha(1 - distance / halfWidth + .2f);
                    }
                    return true;
                }
            });

            String titleTxt = (String) BatteryUtils.readData(mContext, BoostBatteryConstants.CHARGE_SAVER_TITLE, "Cleaner");
            title.setText(titleTxt);

            more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchLayout != null) {
                        more.setVisibility(View.GONE);
                        switchLayout.setVisibility(VISIBLE);
                    }
                }
            });

            if ((Boolean) BatteryUtils.readData(mContext, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true)) {
                if (saverSwitch != null) {
                    saverSwitch.setChecked(true);
                }
            } else {
                if (saverSwitch != null) {
                    saverSwitch.setChecked(false);
                }
            }
            saverSwitch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((Boolean) BatteryUtils.readData(mContext, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true)) {
                        if (saverSwitch != null) {
                            saverSwitch.setChecked(false);
                            BatteryUtils.writeData(mContext, BoostBatteryConstants.CHARGE_SAVER_SWITCH, false);
                        }
                    } else {
                        if (saverSwitch != null) {
                            saverSwitch.setChecked(true);
                            BatteryUtils.writeData(mContext, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true);
                        }
                    }
                }
            });
        }
    }

    private void initViews() {
        bubbleLayout = (BubbleLayoutBoost) findViewById(R.id.battery_bubble_layout);
        batteryView = (ProtectBatteryView) findViewById(R.id.battery_charge_save);
        switchLayout = (LinearLayout) findViewById(R.id.battery_switch);
        saverSwitch = (CheckBox) findViewById(R.id.battery_switch_check);
        adLayout = (LinearLayout) findViewById(R.id.battery_ad_layout);
        title = (TextView) findViewById(R.id.battery_title);
        more = (LinearLayout) findViewById(R.id.battery_more);
        time = (TextView) findViewById(R.id.battery_now_time);
        date = (TextView) findViewById(R.id.battery_now_date);
        battery_charge_time = (LinearLayout) findViewById(R.id.battery_charge_time);
        battery_now_year = (TextView) findViewById(R.id.battery_now_year);
        day = (TextView) findViewById(R.id.battery_now_day);
        week = (TextView) findViewById(R.id.battery_now_week);
        batteryLeft = (TextView) findViewById(R.id.battery_now_battery_left);
        batteryLeft1 = (TextView) findViewById(R.id.battery_now_battery_left2);
        battery_le = (AWaterView) findViewById(R.id.battery_le);
        battery_ram = (MWaterView) findViewById(R.id.battery_ram);
        battery_temp = (TextView) findViewById(R.id.battery_temp);
    }

    public void pauseBubble() {
        if (bubbleLayout != null) {
            bubbleLayout.pause();
        }
        if (battery_le != null) {
            battery_le.stop();
        }
        if (battery_ram != null) {
            battery_ram.stop();
        }
    }

    public void reStartBubble() {
        if (bubbleLayout != null) {
            bubbleLayout.reStart();
        }
        if (battery_le != null) {
            battery_le.start();
        }
        if (battery_ram != null) {
            battery_ram.start();
        }
        GetCpuTempReaderBoost.getCPUTemp(new GetCpuTempReaderBoost.TemperatureResultCallback() {
            @Override
            public void callbackResult(GetCpuTempReaderBoost.ResultCpuTemperature result) {
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();
                } else {
                    cpuTemp = 40;
                }
                if (battery_temp != null) {
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            battery_temp.setText(cpuTemp + "℃");
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isRegisterTimeUpdate) {
            registerTimeUpdateReceiver();
        }
        if (!isBindView) {
            isBindView = true;
        }
        if (battery_le != null) {
            battery_le.start();
        }
        if (battery_ram != null) {
            battery_ram.upDate(getMemory(mContext));
            battery_ram.start();
        }
        GetCpuTempReaderBoost.getCPUTemp(new GetCpuTempReaderBoost.TemperatureResultCallback() {
            @Override
            public void callbackResult(GetCpuTempReaderBoost.ResultCpuTemperature result) {
                if (result != null) {
                    cpuTemp = (int) result.getTemperature();
                } else {
                    cpuTemp = 40;
                }
                if (battery_temp != null) {

                    battery_temp.post(new Runnable() {
                        @Override
                        public void run() {
                            battery_temp.setText(cpuTemp + "℃");
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (battery_le != null) {
            battery_le.start();
        }
        if (battery_ram != null) {
            battery_ram.start();
        }
        try {
            JSONObject object = new JSONObject(AndroidSdk.getExtraData());
            int state = analysisJson(object);
            if (state == 1) {
                mContext.startActivity(new Intent(mContext, SetADActivityBoost.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isRegisterTimeUpdate) {
            unregisterTimeUpdateReceiver();
        }
        if (bubbleLayout != null) {
            bubbleLayout.destroy();
        }
        if (isBindView) {
            isBindView = false;
        }
    }

    private int analysisJson(JSONObject object) throws JSONException {
        int state = 0;
        if (object != null) {
            state = object.optInt("charging", 0);
        }
        return state;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            unregisterTimeUpdateReceiver();
        } else {
            updateTime();
            registerTimeUpdateReceiver();
        }
    }

    public void registerTimeUpdateReceiver() {
        mContext.registerReceiver(timerUpdateReceiver, mIntentFilter);
        isRegisterTimeUpdate = true;
    }

    public void unregisterTimeUpdateReceiver() {
        mContext.unregisterReceiver(timerUpdateReceiver);
        isRegisterTimeUpdate = false;
    }
}
