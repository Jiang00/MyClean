package com.vector.module.mView;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.vector.module.ADActivity;
import com.vector.module.R;
import com.vector.module.Util.ADUtileRequest;
import com.vector.module.Util.Constants;
import com.vector.module.Util.Utils;
import com.vector.module.entry.BatteryEntry;

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
    private CheckBox saverSwitch;
    private TextView time;
    private TextView date;
    private TextView week;
    private TextView batteryLeft, batteryLeft_2;
    private TextView currentLevel;
    private ImageView battery_zhuan;
    private AWaterView battery_water;

    private int halfWidth;
    private ObjectAnimator objectAnimator;

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
        adView = new ADUtileRequest().showCustomNativeAD(Constants.TAG_CHARGING, R.layout.native_ad, null);
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
        final int curLevel = entry.getLevel();
        currentLevel.setText(curLevel + "%");
//        battery_water.upDate(50);

        int leftChargeTime = entry.getLeftTime();
        if (batteryLeft != null) {
            String str;
            if (entry.isCharging()) {
                str = getResources().getString(R.string.charging_2);
            } else {
                str = getResources().getString(R.string.charging_1);
            }
            batteryLeft.setText(str);
            String result = String.format(getResources().getString(R.string.charging_3), entry.extractHours(leftChargeTime), entry.extractMinutes(leftChargeTime));
            batteryLeft_2.setText(result);
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

            if ((Boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, true)) {
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
                    if ((Boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, true)) {
                        if (saverSwitch != null) {
                            saverSwitch.setChecked(false);
                            Utils.writeData(mContext, Constants.CHARGE_SAVER_SWITCH, false);
                        }
                    } else {
                        if (saverSwitch != null) {
                            saverSwitch.setChecked(true);
                            Utils.writeData(mContext, Constants.CHARGE_SAVER_SWITCH, true);
                        }
                    }
                }
            });
        }
    }

    private void initViews() {
        currentLevel = (TextView) findViewById(R.id.battery_level);
        battery_zhuan = (ImageView) findViewById(R.id.battery_zhuan);
        battery_water = (AWaterView) findViewById(R.id.battery_water);
        batteryView = (BatteryView) findViewById(R.id.battery_charge_save);
        switchLayout = (LinearLayout) findViewById(R.id.battery_switch);
        saverSwitch = (CheckBox) findViewById(R.id.battery_switch_check);
        adLayout = (LinearLayout) findViewById(R.id.battery_ad_layout);
        icon = (ImageView) findViewById(R.id.battery_icon);
        title = (TextView) findViewById(R.id.battery_title);
        more = (LinearLayout) findViewById(R.id.battery_more);
        time = (TextView) findViewById(R.id.battery_now_time);
        date = (TextView) findViewById(R.id.battery_now_date);
        week = (TextView) findViewById(R.id.battery_now_week);
        batteryLeft = (TextView) findViewById(R.id.battery_now_battery_left);
        batteryLeft_2 = (TextView) findViewById(R.id.battery_now_battery_left_2);
    }

    public void pauseBubble() {
//        if (battery_water != null) {
//            battery_water.stop();
//        }
        if (objectAnimator != null && objectAnimator.isRunning()) {
            objectAnimator.cancel();
        }
    }

    public void reStartBubble() {
//        if (battery_water != null) {
//            battery_water.start();
//        }
        if (objectAnimator != null && !objectAnimator.isRunning()) {
            objectAnimator.start();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isRegisterTimeUpdate) {
            registerTimeUpdateReceiver();
        }
//        if (battery_water != null) {
//            battery_water.start();
//        }

        if (battery_zhuan != null) {
            objectAnimator = ObjectAnimator.ofFloat(battery_zhuan, View.ROTATION
                    , 0, -360);
            objectAnimator.setDuration(1500);
            objectAnimator.setRepeatCount(-1);
            objectAnimator.setInterpolator(new LinearInterpolator());
            objectAnimator.start();
        }
        if (!isBindView) {
            isBindView = true;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
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
        if (isBindView) {
            isBindView = false;
        }
//        if (battery_water != null) {
//            battery_water.stop();
//        }
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
