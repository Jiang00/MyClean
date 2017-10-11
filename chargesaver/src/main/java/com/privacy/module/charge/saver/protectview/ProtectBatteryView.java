package com.privacy.module.charge.saver.protectview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
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
import com.privacy.module.charge.saver.PrivacySetADActivity;
import com.privacy.module.charge.saver.R;
import com.privacy.module.charge.saver.entry.PrivacyBatteryEntry;
import com.privacy.module.charge.saver.privacyutils.BatteryConstantsPrivacy;
import com.privacy.module.charge.saver.privacyutils.PrivacyADRequest;
import com.privacy.module.charge.saver.privacyutils.UtilsPrivacy;

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
    private ProtectBatteryView.UnlockListener unlockListener;
//    private BatteryChargeView chargeView = null;

    private ProtectBatteryView batteryView;
    private LinearLayout adLayout;
    private TextView title;
    private LinearLayout more;
    private LinearLayout switchLayout;
    private CheckBox saverSwitch;
    ImageView battery_shandian;
    private TextView time;
    private TextView date;
    //    private TextView battery_now_year;
    private TextView day;
    private TextView week;
    private TextView batteryLeft;
    //    private TextView currentLevel_ing;
    private BubbleLayoutPrivacy bubbleLayout;
    PrivacyBatteryView batteryview;
//    private LottieAnimationView water;
//    LottieAnimationView battay_info_lot;

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
        adView = new PrivacyADRequest().showCustomNativeAD(BatteryConstantsPrivacy.TAG_CHARGING, null);
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
                str = new SimpleDateFormat("MM", Locale.getDefault()).format(d);
                date.setText(str);
                str = new SimpleDateFormat("dd", Locale.getDefault()).format(d);
                day.setText(str + ".");
                str = new SimpleDateFormat("EEEE").format(d);
                week.setText(str + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int progress = 0;
    boolean flag = false;
    int i = 0;
    CountDownTimer cdt;

    public void bind(PrivacyBatteryEntry entry) {
        if (entry == null) {
            return;
        }
        final int curLevel = entry.getLevel();
        final int le = curLevel % 100;
        batteryview.start(curLevel);

        cdt = new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (i == 0) {
                    battery_shandian.setImageResource(R.mipmap.battery_shandian1);
                } else {
                    battery_shandian.setImageResource(R.mipmap.battery_shandian);
                }
                i++;
                if (i == 2) {
                    i = 0;
                }
                start();
            }
        }.start();
//        if (!flag) {
//            flag = true;
//            battery_shandian.setColorFilter(ContextCompat.getColor(mContext, R.color.A2));
//        } else {
//            flag = false;
////            battery_shandian.setColorFilter(ContextCompat.getColor(mContext, R.color.A3));
//        }

//        if (curLevel < 20) {
//            currentLevel.setTextColor(ContextCompat.getColor(mContext, R.color.charg_3));
//        } else if (curLevel < 80) {
//            currentLevel.setTextColor(ContextCompat.getColor(mContext, R.color.charg_2));
//        } else {
//            currentLevel.setTextColor(ContextCompat.getColor(mContext, R.color.charg_1));
//        }
//        currentLevel.setText(curLevel + "%");

//        battay_info_lot.setImageAssetsFolder("images/");
//        battay_info_lot.setAnimation("particle.json");
//        battay_info_lot.loop(false);
//        battay_info_lot.setSpeed(0.7f);
//        battay_info_lot.playAnimation();


        //        if (water != null && !water.isAnimating()) {
//            initWater();
//            water.playAnimation();
//            water.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    progress = (int) (animation.getAnimatedFraction() * 100);
//                    if (le == 0) {
//                        if (progress == 99) {
//                            progress = 100;
//                            water.pauseAnimation();
//                        }
//                    } else if (progress == le) {
//                        water.pauseAnimation();
//                    }
//                }
//            });
//        }
        int leftChargeTime = entry.getLeftTime();
        if (batteryLeft != null) {
            String str;
            if (entry.isCharging()) {
                str = getResources().getString(R.string.charging_on_left);
            } else {
                str = getResources().getString(R.string.charging_use_left);
            }
            String result = String.format(str, entry.extractHours(leftChargeTime), entry.extractMinutes(leftChargeTime));
            if (curLevel == 100) {
                batteryLeft.setText("100%");
            } else {
                batteryLeft.setText(result);
            }
        }

    }

    private void initWater() {
//        try {
//            water.setImageAssetsFolder(mContext, "theme://images/battery");
//            water.setAnimation(mContext, "theme://battery.json");
//        } catch (Exception e) {
//            if (!water.isAnimating()) {
//                water.setImageAssetsFolder(null, "images/battery");
//                water.setAnimation(null, "battery.json");
//            }
//        }
//        water.loop(true);
//        water.setSpeed(5.0f);
    }

    protected int dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setCharing(boolean isVisible) {
//        if (isVisible) {
//            currentLevel_ing.setVisibility(VISIBLE);
//        } else {
//            currentLevel_ing.setVisibility(GONE);
//        }
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

            String titleTxt = (String) UtilsPrivacy.readData(mContext, BatteryConstantsPrivacy.CHARGE_SAVER_TITLE, "Cleaner");
            title.setText(titleTxt);
            int iconId = (int) UtilsPrivacy.readData(mContext, BatteryConstantsPrivacy.CHARGE_SAVER_ICON, R.mipmap.battery_icon1);
            if (iconId > -1) {
//                icon.setImageResource(iconId);
            }

            more.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (switchLayout != null) {
                        more.setVisibility(View.INVISIBLE);
                        switchLayout.setVisibility(VISIBLE);
                    }
                }
            });

            if ((Boolean) UtilsPrivacy.readData(mContext, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true)) {
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
                    if ((Boolean) UtilsPrivacy.readData(mContext, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true)) {
                        if (saverSwitch != null) {
                            saverSwitch.setChecked(false);
                            UtilsPrivacy.writeData(mContext, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, false);
                        }
                    } else {
                        if (saverSwitch != null) {
                            saverSwitch.setChecked(true);
                            UtilsPrivacy.writeData(mContext, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true);
                        }
                    }
                }
            });
        }
    }

    private void initViews() {
//        shutter = (ImageView) findViewById(R.id.battery_shutter);
        bubbleLayout = (BubbleLayoutPrivacy) findViewById(R.id.battery_bubble_layout);
//        currentLevel = (TextView) findViewById(R.id.battery_level);
//        currentLevel_ing = (TextView) findViewById(R.id.currentLevel_ing);
        batteryView = (ProtectBatteryView) findViewById(R.id.battery_charge_save);
        switchLayout = (LinearLayout) findViewById(R.id.battery_switch);
        saverSwitch = (CheckBox) findViewById(R.id.battery_switch_check);
        adLayout = (LinearLayout) findViewById(R.id.battery_ad_layout);
        title = (TextView) findViewById(R.id.battery_title);
        more = (LinearLayout) findViewById(R.id.battery_more);
        time = (TextView) findViewById(R.id.battery_now_time);
        date = (TextView) findViewById(R.id.battery_now_date);
//        battery_now_year = (TextView) findViewById(R.id.battery_now_year);
        day = (TextView) findViewById(R.id.battery_now_day);
        week = (TextView) findViewById(R.id.battery_now_week);
        batteryLeft = (TextView) findViewById(R.id.battery_now_battery_left);
//        water = (LottieAnimationView) findViewById(R.id.battery_electricity);
//        battay_info_lot = (LottieAnimationView) findViewById(R.id.battay_info_lot);
        battery_shandian = (ImageView) findViewById(R.id.battery_shandian);
        batteryview = (PrivacyBatteryView) findViewById(R.id.batteryview);
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
        if (batteryview != null) {
            batteryview.stop();
        }
        if (cdt != null) {
            cdt.cancel();
        }
        try {
            JSONObject object = new JSONObject(AndroidSdk.getExtraData());
            int state = analysisJson(object);
            if (state == 1) {
                mContext.startActivity(new Intent(mContext, PrivacySetADActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
