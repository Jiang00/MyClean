package com.easy.module.charge.saver.protectview;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.easy.module.charge.saver.EasySetADActivity;
import com.easy.module.charge.saver.R;
import com.easy.module.charge.saver.easyutils.BatteryConstants;
import com.easy.module.charge.saver.easyutils.EasyADRequest;
import com.easy.module.charge.saver.easyutils.EasyUtils;
import com.easy.module.charge.saver.entry.EasyBatteryEntry;
import com.sample.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ProtectBatteryView.UnlockListener listener;

    private ProtectBatteryView batteryView;
    private LinearLayout adLayout;
    private ImageView icon;
    private TextView title;
    private LinearLayout more;
    private LinearLayout switchLayout;
    private CheckBox saverSwitch;
    private TextView time;
    private TextView date;
    private TextView battery_now_year;
    private TextView day;
    private TextView week;
    private TextView batteryLeft;
    private TextView currentLevel;
    private BubbleLayout bubbleLayout;
    private LottieAnimationView battery_ke;
    LottieAnimationView battay_info_lot;
    BatteryView batteryview;

    private int halfWidth;
//    private ImageView shutter;

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
        adView = new EasyADRequest().showCustomNativeAD(BatteryConstants.TAG_CHARGING, R.layout.native_ad, null);
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

                str = new SimpleDateFormat("yy", Locale.getDefault()).format(d);
                battery_now_year.setText(str);
                str = new SimpleDateFormat("MM", Locale.getDefault()).format(d);
                date.setText(str);
                str = new SimpleDateFormat("dd", Locale.getDefault()).format(d);
                day.setText(str);
                str = new SimpleDateFormat("EEEE").format(d);
                week.setText(str);
            } catch (Exception e) {
            }
        }
    }

    private int progress = 0;

    public void bind(EasyBatteryEntry entry) {
        if (entry == null) {
            return;
        }
        final int curLevel = entry.getLevel();
        final int le = curLevel % 100;
//        电量颜色
        if (curLevel < 20) {
//            currentLevel.setTextColor(ContextCompat.getColor(mContext, R.color.charg_3));
        } else if (curLevel < 80) {
//            currentLevel.setTextColor(ContextCompat.getColor(mContext, R.color.charg_2));
        } else {
//            currentLevel.setTextColor(ContextCompat.getColor(mContext, R.color.charg_1));
        }
        currentLevel.setText(curLevel + "%");
        if (le <= 5) {
            batteryview.start(2);
        } else if (le > 5 && le <= 10) {
            batteryview.start(10);
        } else if (le > 10 && le <= 20) {
            batteryview.start(20);
        } else if (le > 20 && le <= 30) {
            batteryview.start(30);
        } else if (le > 30 && le <= 40) {
            batteryview.start(40);
        } else if (le > 40 && le <= 50) {
            batteryview.start(50);
        } else if (le > 50 && le <= 60) {
            batteryview.start(60);
        } else if (le > 60 && le <= 70) {
            batteryview.start(70);
        } else if (le > 70 && le <= 80) {
            batteryview.start(80);
        } else if (le > 80 && le <= 90) {
            batteryview.start(90);
        } else if (le > 90 && le <= 95) {
            batteryview.start(95);
        } else {
            batteryview.start(100);
        }
        if (battay_info_lot != null && !battay_info_lot.isAnimating()) {
            try {
                battay_info_lot.setImageAssetsFolder(mContext, "theme://images");
                battay_info_lot.setAnimation(mContext, "theme://particle.json");
            } catch (Exception e) {
                if (!battay_info_lot.isAnimating()) {
                    battay_info_lot.setImageAssetsFolder(null, "images");
                    battay_info_lot.setAnimation(null, "particle.json");
                }
            }
            battay_info_lot.loop(true);
            battay_info_lot.setSpeed(1.0f);
            battay_info_lot.playAnimation();
        }

        if (battery_ke != null && !battery_ke.isAnimating()) {
            try {
                battery_ke.setAnimation(mContext, "theme://battery_ke.json");
            } catch (Exception e) {
                if (!battery_ke.isAnimating()) {
                    battery_ke.setAnimation(null, "battery_ke.json");
                }
            }
            battery_ke.loop(true);
            battery_ke.setSpeed(1.0f);//调节速度的
            battery_ke.playAnimation();
        }

        int leftChargeTime = entry.getLeftTime();
        if (batteryLeft != null) {
            String str;
            if (entry.isCharging()) {
                str = getResources().getString(R.string.charging_on_left);
            } else {
                str = getResources().getString(R.string.charging_use_left);
            }
            String result = String.format(str, entry.extractHours(leftChargeTime), entry.extractMinutes(leftChargeTime));
            batteryLeft.setText(result);
        }
    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setCharing(boolean isVisible) {
        if (isVisible) {
        } else {
        }
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
                        more.setVisibility(View.VISIBLE);
                        switchLayout.setVisibility(GONE);
                    }
                    detector.onTouchEvent(event);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (distance > halfWidth) {
                            if (listener != null) {
//                                batteryView.setAlpha(1.0f);
                                listener.onUnlock();
                            }
                        } else {
                            if (batteryView != null) {
//                                batteryView.setAlpha(1.0f);
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
//                        batteryView.setAlpha(1.0f);
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
//                        batteryView.setAlpha(1 - distance / halfWidth + .2f);
                    }
                    return true;
                }
            });

            String titleTxt = (String) EasyUtils.readData(mContext, BatteryConstants.CHARGE_SAVER_TITLE, "Cleaner");
            title.setText(titleTxt);
            int iconId = (int) EasyUtils.readData(mContext, BatteryConstants.CHARGE_SAVER_ICON, R.mipmap.battery_inner_icon);
            if (iconId > -1) {
                icon.setImageResource(iconId);
            }

            more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchLayout != null) {
                        more.setVisibility(View.GONE);
                        switchLayout.setVisibility(VISIBLE);
                    }
                }
            });

            if ((Boolean) EasyUtils.readData(mContext, BatteryConstants.CHARGE_SAVER_SWITCH, true)) {
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
                    if ((Boolean) EasyUtils.readData(mContext, BatteryConstants.CHARGE_SAVER_SWITCH, true)) {
                        if (saverSwitch != null) {
                            saverSwitch.setChecked(false);
                            EasyUtils.writeData(mContext, BatteryConstants.CHARGE_SAVER_SWITCH, false);
                        }
                    } else {
                        if (saverSwitch != null) {
                            saverSwitch.setChecked(true);
                            EasyUtils.writeData(mContext, BatteryConstants.CHARGE_SAVER_SWITCH, true);
                        }
                    }
                }
            });
        }
    }

    private void initViews() {
//        shutter = (ImageView) findViewById(R.id.battery_shutter);
        bubbleLayout = (BubbleLayout) findViewById(R.id.battery_bubble_layout);
        currentLevel = (TextView) findViewById(R.id.battery_level);
        batteryView = (ProtectBatteryView) findViewById(R.id.battery_charge_save);
        switchLayout = (LinearLayout) findViewById(R.id.battery_switch);
        saverSwitch = (CheckBox) findViewById(R.id.battery_switch_check);
        adLayout = (LinearLayout) findViewById(R.id.battery_ad_layout);
        icon = (ImageView) findViewById(R.id.battery_icon);
        title = (TextView) findViewById(R.id.battery_title);
        more = (LinearLayout) findViewById(R.id.battery_more);
        time = (TextView) findViewById(R.id.battery_now_time);
        date = (TextView) findViewById(R.id.battery_now_date);
        battery_now_year = (TextView) findViewById(R.id.battery_now_year);
        day = (TextView) findViewById(R.id.battery_now_day);
        week = (TextView) findViewById(R.id.battery_now_week);
        batteryLeft = (TextView) findViewById(R.id.battery_now_battery_left);
        battay_info_lot = (LottieAnimationView) findViewById(R.id.battay_info_lot);
        batteryview = (BatteryView) findViewById(R.id.batteryview);

        battery_ke = (LottieAnimationView) findViewById(R.id.battery_ke);
    }

    public void pauseBubble() {
        if (bubbleLayout != null) {
            bubbleLayout.pause();
        }
    }

    public void reStartBubble() {
        if (bubbleLayout != null) {
            bubbleLayout.reStart();
        }
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
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            JSONObject object = new JSONObject(AndroidSdk.getExtraData());
            int state = analysisJson(object);
            if (state == 1) {
                mContext.startActivity(new Intent(mContext, EasySetADActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isRegisterTimeUpdate) {
            unregisterTimeUpdateReceiver();
        }

        if (battery_ke != null && battery_ke.isAnimating()) {
            battery_ke.cancelAnimation();
        }

        if (battay_info_lot != null && battay_info_lot.isAnimating()) {
            battay_info_lot.cancelAnimation();
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
