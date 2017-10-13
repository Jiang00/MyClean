package com.my.bruder.charge.saver.view;

import android.animation.ObjectAnimator;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.my.bruder.charge.saver.ADingActivity;
import com.my.bruder.charge.saver.R;
import com.my.bruder.charge.saver.Util.ADRequest;
import com.my.bruder.charge.saver.Util.Constants;
import com.my.bruder.charge.saver.Util.Utils;
import com.my.bruder.charge.saver.entry.BatteryEntry;
import com.sample.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BatteringView extends FrameLayout {

    private Context mContext;
    private GestureDetector detector;
    private float distance;
    private boolean isBindView = false;
    private boolean isRegisterTimeUpdate = false;
    private View adView;
    private BatteringView.UnlockListener listener;
//    private BatteryChargeView chargeView = null;

    private BatteringView batteryView;
    private LinearLayout adLayout;
    //    private ImageView icon;
//    private TextView title;
//    private LinearLayout more;
//    private LinearLayout switchLayout;
//    private CheckBox saverSwitch;
    private TextView time;
    private TextView date;
    private TextView week;
    private TextView batteryLeft;
    private LinearLayout slide;
    private ImageView battery_up;
    private TextView currentLevel;
    private DynamicWave battery_ripple_layout;
    private BubblesLayout bubbleLayout;

    //    private LottieAnimationView shell;
    private LottieAnimationView water;
    //    private LottieAnimationView lighting;
    private int halfWidth;
//    private ImageView shutter;

    public interface UnlockListener {
        void onUnlock();
    }

    public BatteringView(Context context) {
        super(context, null);
    }

    public BatteringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public BatteringView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                float startY = event.getY();
                detector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if ((event.getX() - startX) > 20 || (startX - event.getX()) > 20) {
                            if ((event.getX() - startX) > halfWidth / 2) {
                                if (listener != null) {
//                                    listener.onUnlock();
                                }
                            }
                            return true;
                        } else {
                            break;
                        }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if ((event.getY() - startY) > 20 || (startY - event.getY()) > 20) {
                            if ((event.getX() - startX) > halfWidth / 2) {
                                if (listener != null) {
//                                    listener.onUnlock();
                                }
                            }
                            return true;
                        } else {
                            break;
                        }
                }
                return false;
            }
        });
    }

    //设置时间
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
        // 启动水波纹动画
        battery_ripple_layout.start();
        // 设置水波纹位置
        battery_ripple_layout.upDate(curLevel);
        if (water != null && !water.isAnimating()) {
//            initWater();
            water.setImageAssetsFolder("images/battery/");
            water.setAnimation("battery_electricity.json");
            water.loop(false);
            water.setSpeed(0.7f);
            water.playAnimation();
            water.playAnimation();
        }
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(battery_up, "alpha", 1f, 0f, 1f, 0f);
        fadeInOut.setDuration(4000);
        fadeInOut.start();

       /* if (lighting != null && entry.isCharging()) {
            if (entry.getLevel() == 100) {
                if (lighting.isAnimating()) {
                    lighting.cancelAnimation();
                }
                slide.setVisibility(VISIBLE);
                lighting.setVisibility(GONE);
            } else {
                if (lighting.getVisibility() == View.GONE) {
                    lighting.setVisibility(VISIBLE);
                    slide.setVisibility(GONE);
                    lighting.resumeAnimation();
                } else {
                    if (!lighting.isAnimating()) {
                        initLighting();
                        lighting.playAnimation();
                    }
                }
            }
        } else if (lighting != null) {
            if (lighting.isAnimating()) {
                lighting.pauseAnimation();
            }
            slide.setVisibility(VISIBLE);
            lighting.setVisibility(GONE);
        }*/

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


    /*private void initShell() {
        try {
            shell.setImageAssetsFolder(mContext, "theme://images/shell");
            shell.setAnimation(mContext, "theme://data.json");
            shell.loop(true);
            shell.playAnimation();
        } catch (Exception e) {
            shell.setImageAssetsFolder(null, "images/shell");
            shell.setAnimation(null, "data.json");
        }
        shell.loop(true);
        shell.playAnimation();
    }*/

    private void initWater() {
        try {
            water.setAnimation(mContext, "theme://battery_electricity.json");
        } catch (Exception e) {
            if (!water.isAnimating()) {
                water.setAnimation(null, "battery_electricity.json");
            }
        }
        water.loop(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isBindView) {
            initViews();
            isBindView = true;

            updateTime();

            showNativeAD();

            halfWidth = (int) (((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() / 4f);
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                  /*  if (switchLayout != null && switchLayout.getVisibility() == View.VISIBLE) {
                        switchLayout.setVisibility(GONE);
                    }*/
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
//                        listener.onUnlock();
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    if (e2.getY() - e1.getY() == distance) {
                        return true;
                    }
                    distance = e1.getY() - e2.getY();
                    if (batteryView != null) {
//                        batteryView.setTranslationX(distance);
                        batteryView.setAlpha(1 - distance / halfWidth + .2f);
                    }
                    return true;
                }
            });

            String titleTxt = (String) Utils.readData(mContext, Constants.CHARGE_SAVER_TITLE, "BRUDERBATTERY");
//            title.setText(titleTxt);
            int iconId = (int) Utils.readData(mContext, Constants.CHARGE_SAVER_ICON, R.mipmap.battery_inner_icon);
            if (iconId > -1) {
//                icon.setImageResource(iconId);
            }

//            more.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (switchLayout != null) {
//                        switchLayout.setVisibility(VISIBLE);
//                    }
//                }
//            });

            if ((Boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, false)) {
               /* if (saverSwitch != null) {
                    saverSwitch.setChecked(true);
                }*/
            } else {
              /*  if (saverSwitch != null) {
                    saverSwitch.setChecked(false);
                }*/
            }
           /* saverSwitch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((Boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, false)) {
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
            });*/
        }
    }

    private void initViews() {
//        shutter = (ImageView) findViewById(R.id.battery_shutter);
        bubbleLayout = (BubblesLayout) findViewById(R.id.battery_bubble_layout);
        currentLevel = (TextView) findViewById(R.id.battery_level);
        battery_ripple_layout = (DynamicWave) findViewById(R.id.battery_ripple_layout);
        slide = (LinearLayout) findViewById(R.id.battery_slide);
        battery_up = (ImageView) findViewById(R.id.battery_up);
        batteryView = (BatteringView) findViewById(R.id.battery_charge_save);
//        switchLayout = (LinearLayout) findViewById(R.id.battery_switch);
//        saverSwitch = (CheckBox) findViewById(R.id.battery_switch_check);
        adLayout = (LinearLayout) findViewById(R.id.battery_ad_layout);
//        icon = (ImageView) findViewById(R.id.battery_icon);
//        title = (TextView) findViewById(R.id.battery_title);
//        more = (LinearLayout) findViewById(R.id.battery_more);
        time = (TextView) findViewById(R.id.battery_now_time);
        date = (TextView) findViewById(R.id.battery_now_date);
        week = (TextView) findViewById(R.id.battery_now_week);
        batteryLeft = (TextView) findViewById(R.id.battery_now_battery_left);
//        shell = (LottieAnimationView) findViewById(R.id.battery_shell);
        water = (LottieAnimationView) findViewById(R.id.battery_electricity);
//        lighting = (LottieAnimationView) findViewById(R.id.battery_lighting);
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
                mContext.startActivity(new Intent(mContext, ADingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        /*if (lighting != null && lighting.isAnimating()) {
            lighting.cancelAnimation();
        }*/
       /* if (shell != null && shell.isAnimating()) {
            shell.cancelAnimation();
        }*/
        if (bubbleLayout != null) {
            bubbleLayout.destroy();
        }
        if (isBindView) {
            isBindView = false;
        }
        if (battery_ripple_layout != null) {
            battery_ripple_layout.stop();
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