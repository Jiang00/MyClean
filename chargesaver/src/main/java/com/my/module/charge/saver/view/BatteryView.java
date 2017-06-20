package com.my.module.charge.saver.view;

import android.animation.ValueAnimator;
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
import com.my.module.charge.saver.ADActivity;
import com.my.module.charge.saver.R;
import com.my.module.charge.saver.Util.ADRequest;
import com.my.module.charge.saver.Util.Constants;
import com.my.module.charge.saver.Util.Utils;
import com.my.module.charge.saver.entry.BatteryEntry;
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
    private CheckBox saverSwitch;
    private TextView time;
    private TextView date;
    private TextView week;
    private TextView batteryLeft;
    private TextView currentLevel;
    private BubbleLayout bubbleLayout;

    private LottieAnimationView shell;
    private LottieAnimationView water;
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
        final int curLevel = entry.getLevel();
        currentLevel.setText(curLevel + "%");
        final int le = curLevel % 100;

        if (water != null && !water.isAnimating()) {
            initWater();
            water.playAnimation();
            water.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    progress = (int) (animation.getAnimatedFraction() * 100);
                    if (le == 0) {
                        if (progress == 99) {
                            progress = 100;
                            water.pauseAnimation();
                        }
                    } else if (progress == le) {
                        water.pauseAnimation();
                    }
                }
            });
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


    private void initShell() {
        try {
            shell.setImageAssetsFolder(mContext, "theme://images/shell");
            shell.setAnimation(mContext, "theme://shell.json");
            shell.loop(true);
            shell.playAnimation();
        } catch (Exception e) {
            shell.setImageAssetsFolder(null, "images/shell");
            shell.setAnimation(null, "shell.json");
        }
        shell.loop(true);
        shell.playAnimation();
    }

    private void initWater() {
        try {
            water.setImageAssetsFolder(mContext, "theme://images/water");
            water.setAnimation(mContext, "theme://water.json");
        } catch (Exception e) {
            if (!water.isAnimating()) {
                water.setImageAssetsFolder(null, "images/water");
                water.setAnimation(null, "water.json");
            }
        }
        water.loop(true);
        water.setSpeed(5.0f);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isBindView) {
            initViews();
            isBindView = true;

//            shell.setAnimation("shell.json");
//            shell.loop(true);
//            LottieComposition.Factory.fromAssetFileName(getContext(), "shell.json",
//                    new OnCompositionLoadedListener() {
//                        @Override
//                        public void onCompositionLoaded(LottieComposition composition) {
//                            shell.setComposition(composition);
//                        }
//                    });
//            shell.playAnimation();
            initShell();

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

            String titleTxt = (String) Utils.readData(mContext, Constants.CHARGE_SAVER_TITLE, "CLEANER");
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
                    if (mContext != null) {
                        mContext.sendBroadcast(new Intent(Constants.ACTION_CHARGE_SAVER_SWITCH_STATE));
                    }
                }
            });
        }
    }

    private void initViews() {
        shutter = (ImageView) findViewById(R.id.battery_shutter);
        bubbleLayout = (BubbleLayout) findViewById(R.id.battery_bubble_layout);
        currentLevel = (TextView) findViewById(R.id.battery_level);
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
        shell = (LottieAnimationView) findViewById(R.id.battery_shell);
        water = (LottieAnimationView) findViewById(R.id.battery_electricity);
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
                mContext.startActivity(new Intent(mContext, ADActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isRegisterTimeUpdate) {
            unregisterTimeUpdateReceiver();
        }
        if (water != null && water.isAnimating()) {
            water.cancelAnimation();
        }
        if (shell != null && shell.isAnimating()) {
            shell.cancelAnimation();
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
