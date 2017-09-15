package com.fast.module.charge.saver.view;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.fast.module.charge.saver.ADActivity;
import com.fast.module.charge.saver.R;
import com.fast.module.charge.saver.Util.ADRequest;
import com.fast.module.charge.saver.Util.Constants;
import com.fast.module.charge.saver.Util.Utils;
import com.fast.module.charge.saver.entry.BatteryEntry;
import com.sample.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BatteryView extends FrameLayout {

    private Context mContext;
    private GestureDetector detector;
    private float distance;
    private boolean isBindView = false;
    private boolean isRegisterTimeUpdate = false;
    private View adView;
    private BatteryView.UnlockListener listener;
//    private BatteryChargeView chargeView = null;

    private BatteryView batteryView;
    private LinearLayout adLayout;
    private ImageView icon;
    private TextView title;
    private LinearLayout more;
    private LinearLayout switchLayout;
    private TextView saverSwitch;
    private TextView time;
    private TextView date;
    private TextView week;
    private TextView batteryLeft;
    private TextView battery_now_battery_left_1;
    private TextView currentLevel;
    private MainWaterView charing_water;
    private LinearLayout level_all;
    private ImageView level_0, level_1, level_2;
    private BubbleLayout bubbleLayout;

    private int halfWidth;
    private ImageView shutter;

    public interface UnlockListener {
        void onUnlock();
    }

    public BatteryView(Context context) {
        super(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUnlockListener(UnlockListener unlockListener) {
        listener = unlockListener;
    }

    private BroadcastReceiver timerUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTime();
        }
    };

    private IntentFilter mIntentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);

    private void showNativeAD() {
        adView = new ADRequest().showCustomNativeAD(Constants.TAG_CHARGING, R.layout.native_ad, null);
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
                                if (listener != null) {
                                    listener.onUnlock();
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
                str = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(d);
                date.setText(str);
                str = new SimpleDateFormat("EEEE").format(d);
                week.setText(str);
            } catch (Exception e) {
            }
        }
    }

    private int progress = 0;

    public void bind(BatteryEntry entry) {
        if (entry == null) {
            return;
        }
//        final int curLevel = 44;
        final int curLevel = entry.getLevel();
        currentLevel.setText(curLevel + "%");
        charing_water.upDate(curLevel);
        if (curLevel != 0) {
            int s = curLevel / 10;
            int g = curLevel % 10;
            if (curLevel == 100) {
                level_0.setVisibility(VISIBLE);
                level_all.setBackgroundColor(ContextCompat.getColor(mContext, R.color.A3));
            } else {
                level_0.setVisibility(GONE);
                level_all.setBackgroundColor(Color.TRANSPARENT);
            }
            if (g == 0) {
                level_2.setBackgroundResource(R.mipmap.charging_0);
            } else if (g == 1) {
                level_2.setBackgroundResource(R.mipmap.charging_1);
            } else if (g == 2) {
                level_2.setBackgroundResource(R.mipmap.charging_2);
            } else if (g == 3) {
                level_2.setBackgroundResource(R.mipmap.charging_3);
            } else if (g == 4) {
                level_2.setBackgroundResource(R.mipmap.charging_4);
            } else if (g == 5) {
                level_2.setBackgroundResource(R.mipmap.charging_5);
            } else if (g == 6) {
                level_2.setBackgroundResource(R.mipmap.charging_6);
            } else if (g == 7) {
                level_2.setBackgroundResource(R.mipmap.charging_7);
            } else if (g == 8) {
                level_2.setBackgroundResource(R.mipmap.charging_8);
            } else if (g == 9) {
                level_2.setBackgroundResource(R.mipmap.charging_9);
            }
            if (s == 0) {
                level_1.setBackgroundResource(R.mipmap.charging_0);
            } else if (s == 1) {
                level_1.setBackgroundResource(R.mipmap.charging_1);
            } else if (s == 2) {
                level_1.setBackgroundResource(R.mipmap.charging_2);
            } else if (s == 3) {
                level_1.setBackgroundResource(R.mipmap.charging_3);
            } else if (s == 4) {
                level_1.setBackgroundResource(R.mipmap.charging_4);
            } else if (s == 5) {
                level_1.setBackgroundResource(R.mipmap.charging_5);
            } else if (s == 6) {
                level_1.setBackgroundResource(R.mipmap.charging_6);
            } else if (s == 7) {
                level_1.setBackgroundResource(R.mipmap.charging_7);
            } else if (s == 8) {
                level_1.setBackgroundResource(R.mipmap.charging_8);
            } else if (s == 9) {
                level_1.setBackgroundResource(R.mipmap.charging_9);
            }
        } else {
            level_1.setBackgroundResource(R.mipmap.charging_0);
            level_2.setBackgroundResource(R.mipmap.charging_0);
        }

        final int le = curLevel % 100;


        int leftChargeTime = entry.getLeftTime();
        if (batteryLeft != null) {
            String str;
            if (entry.isCharging()) {
                batteryLeft.setText(R.string.left);
                str = getResources().getString(R.string.charging_on_left);
            } else {
                batteryLeft.setText(R.string.use);
                str = getResources().getString(R.string.charging_use_left);
            }
            battery_now_battery_left_1.setText(entry.extractHours(leftChargeTime) + "h" + entry.extractMinutes(leftChargeTime) + "m");
        }
    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isBindView) {
            initViews();
            isBindView = true;

            updateTime();

            showNativeAD();

            halfWidth = (int) (((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() / 1.3f);
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (switchLayout != null && switchLayout.getVisibility() == View.VISIBLE) {
                        switchLayout.setVisibility(GONE);
                    }
                    detector.onTouchEvent(event);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (distance > halfWidth) {
                            if (listener != null) {
                                batteryView.setAlpha(1.0f);
                                listener.onUnlock();
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
                    if (listener != null) {
                        batteryView.setAlpha(1.0f);
                        listener.onUnlock();
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (e2.getX() - e1.getX() == distance) {
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

            String titleTxt = (String) Utils.readData(mContext, Constants.CHARGE_SAVER_TITLE, "Cleaner");
            title.setText(titleTxt);
            int iconId = (int) Utils.readData(mContext, Constants.CHARGE_SAVER_ICON, R.mipmap.battery_inner_icon);
            if (iconId > -1) {
                icon.setImageResource(iconId);
            }

            more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchLayout != null) {
                        switchLayout.setVisibility(VISIBLE);
                    }
                }
            });

//            if ((Boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, true)) {
//                if (saverSwitch != null) {
//                    saverSwitch.setChecked(true);
//                }
//            } else {
//                if (saverSwitch != null) {
//                    saverSwitch.setChecked(false);
//                }
//            }
            saverSwitch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.writeData(mContext, Constants.CHARGE_SAVER_SWITCH, false);
//                    if ((Boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, true)) {
//                        if (saverSwitch != null) {
//                            saverSwitch.setChecked(false);
//
//                        }
//                    } else {
//                        if (saverSwitch != null) {
//                            saverSwitch.setChecked(true);
//                            Utils.writeData(mContext, Constants.CHARGE_SAVER_SWITCH, true);
//                        }
//                    }
                }
            });
        }
    }

    private void initViews() {
        shutter = (ImageView) findViewById(R.id.battery_shutter);
        bubbleLayout = (BubbleLayout) findViewById(R.id.battery_bubble_layout);
        currentLevel = (TextView) findViewById(R.id.battery_level);
        charing_water = (MainWaterView) findViewById(R.id.charing_water);
        level_all = (LinearLayout) findViewById(R.id.level_all);
        level_0 = (ImageView) findViewById(R.id.level_0);
        level_1 = (ImageView) findViewById(R.id.level_1);
        level_2 = (ImageView) findViewById(R.id.level_2);
        batteryView = (BatteryView) findViewById(R.id.battery_charge_save);
        switchLayout = (LinearLayout) findViewById(R.id.battery_switch);
        saverSwitch = (TextView) findViewById(R.id.battery_switch_check);
        adLayout = (LinearLayout) findViewById(R.id.battery_ad_layout);
        icon = (ImageView) findViewById(R.id.battery_icon);
        title = (TextView) findViewById(R.id.battery_title);
        more = (LinearLayout) findViewById(R.id.battery_more);
        time = (TextView) findViewById(R.id.battery_now_time);
        date = (TextView) findViewById(R.id.battery_now_date);
        week = (TextView) findViewById(R.id.battery_now_week);
        batteryLeft = (TextView) findViewById(R.id.battery_now_battery_left);
        battery_now_battery_left_1 = (TextView) findViewById(R.id.battery_now_battery_left_1);
    }

    public void pauseBubble() {
        if (bubbleLayout != null) {
            bubbleLayout.pause();
        }
        if (charing_water != null) {
            charing_water.stop();
        }
    }

    public void reStartBubble() {
        if (bubbleLayout != null) {
            bubbleLayout.reStart();
        }
        if (charing_water != null) {
            charing_water.start();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isRegisterTimeUpdate) {
            registerTimeUpdateReceiver();
        }
        if (charing_water != null) {
            charing_water.start();
        }
        if (!isBindView) {
            isBindView = true;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            JSONObject object = new JSONObject(AndroidSdk.getExtraData());
            int state = analysisJson(object);
            if (state == 1) {
                mContext.startActivity(new Intent(mContext, ADActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        if (charing_water != null) {
            charing_water.stop();
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
