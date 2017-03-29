package com.eos.module.charge.saver.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.android.theme.internal.data.ThemeManager;
import com.eos.module.charge.saver.ADActivity;
import com.eos.module.charge.saver.R;
import com.eos.module.charge.saver.Util.ADRequest;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.eos.module.charge.saver.cpuutils.CpuTempReader;
import com.eos.module.charge.saver.entry.BatteryEntry;
import com.sample.lottie.LottieAnimationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Switch on 2017/2/6.
 */

public class DuckView extends FrameLayout {

    private LinearLayout adLayout;
    private Context mContext;
    private GestureDetector detector;
    float distance;
    private boolean isBindView = false;
    private boolean isRegisterTimeUpdate = false;
    private View adView;
    private DuckView.UnlockListener listener;
    private ImageView icon;
    private TextView title;
    private LinearLayout more;
    private LinearLayout switchLayout;
    private CheckBox switchBox;
    private TextView time;
    private TextView date;
    private TextView week;
    private TextView currentLevel;
    private TextView timeLeft;
    private ViewPager mViewPager;
    private LinearLayout slide;

    private LottieAnimationView shell;
    private LottieAnimationView water;
    private IndicatorView indicator;

    public interface UnlockListener {
        void onUnlock();
    }

    public DuckView(Context context) {
        super(context, null);
    }

    public DuckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public DuckView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    private void showNativeAD(String tag) {
        adView = new ADRequest().showCustomNativeAD(tag, R.layout.native_ad, null);
        if (adLayout != null && adView != null) {
            if (adLayout.getVisibility() == View.GONE) {
                adLayout.setVisibility(View.VISIBLE);
            }
            if (adView.getParent() != null) {
                ViewGroup viewGroup = (ViewGroup) adView.getParent();
                viewGroup.removeAllViews();
            }
            adLayout.addView(adView);
        }
    }

    public void updateTime() {
        if (time != null && date != null && week != null) {
            long t = System.currentTimeMillis();
            Date d = new Date(t);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String str = sdf.format(d);
            time.setText(str);
            str = new SimpleDateFormat("dd/MM", Locale.getDefault()).format(d);
            date.setText(str);
            str = new SimpleDateFormat("EEEE").format(d);
            week.setText(str);
        }
    }

    public void bind(BatteryEntry entry) {
        if (entry == null) {
            return;
        }
        final int level = entry.getLevel();
        currentLevel.setText(level + "%");
        final int le = level % 100;
        if (!water.isAnimating()) {
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
        if (timeLeft != null) {
            String str;
            if (entry.isCharging()) {
                str = getResources().getString(R.string.charging_on_left);
            } else {
                str = getResources().getString(R.string.charging_use_left);
            }
            String result = String.format(str, entry.extractHours(leftChargeTime), entry.extractMinutes(leftChargeTime));
            timeLeft.setText(result);
        }

        if (temp != null) {
            temp.setText(Integer.valueOf(entry.temp) / 10 + "℃");
        }
        CpuTempReader.getCPUTemp(new CpuTempReader.TemperatureResultCallback() {
            @Override
            public void callbackResult(CpuTempReader.ResultCpuTemperature result) {
                if (cpu != null && result != null) {
                    cpu.setText((int) result.getTemperature() + "℃");
                }
            }
        });

        if (mContext != null) {
            long total = Utils.getTotalMemory();
            long used = total - Utils.getAvailMemory(mContext);
            float scale = Float.valueOf(String.valueOf(used)) / Float.valueOf(String.valueOf(total));
            int memoryScale = (int) (scale * 100);
            if (ram != null) {
                ram.setText(memoryScale + "%");
            }
        }
        int leftTime = entry.getLeftUseTime();
        if (game != null) {
            game.setText(entry.extractHours((int) (leftTime * Constants.GAME_PARAMETER)) + " h " +
                    entry.extractMinutes((int) (leftTime * Constants.GAME_PARAMETER)) + " min");
        }
        if (internet != null) {
            internet.setText(entry.extractHours((int) (leftTime * Constants.INTERNET_PARAMETER)) + " h " +
                    entry.extractMinutes((int) (leftTime * Constants.INTERNET_PARAMETER)) + " min");
        }
        if (phone != null) {
            phone.setText(entry.extractHours((int) (leftTime * Constants.TALK_PARAMETER)) + " h " +
                    entry.extractMinutes((int) (leftTime * Constants.TALK_PARAMETER)) + " min");
        }
    }

    private View infos;
    private TextView phone;
    private TextView game;
    private TextView internet;
    private TextView temp;
    private TextView cpu;
    private TextView ram;
    private int progress = 0;
    private DuckView duckView;

    private void initViews() {
        duckView = (DuckView) findViewById(R.id.battery_duck);
        indicator = (IndicatorView) findViewById(R.id.battery_second_indicator);
        icon = (ImageView) findViewById(R.id.battery_second_icon);
        title = (TextView) findViewById(R.id.battery_second_title);
        more = (LinearLayout) findViewById(R.id.battery_second_more);
        switchLayout = (LinearLayout) findViewById(R.id.battery_duck_switch_second_layout);
        switchBox = (CheckBox) findViewById(R.id.battery_duck_second_switch);
        time = (TextView) findViewById(R.id.battery_current_second_time);
        date = (TextView) findViewById(R.id.battery_current_second_date);
        week = (TextView) findViewById(R.id.battery_current_second_week);
        currentLevel = (TextView) findViewById(R.id.battery_current_second_level);
        timeLeft = (TextView) findViewById(R.id.battery_current_battery_second_left);
        mViewPager = (ViewPager) findViewById(R.id.battery_current_second_viewpager);
        slide = (LinearLayout) findViewById(R.id.battery_current_battery_second_slide);
        shell = (LottieAnimationView) findViewById(R.id.battery_second_shell);
        water = (LottieAnimationView) findViewById(R.id.battery_second_electricity);

        infos = LayoutInflater.from(mContext).inflate(R.layout.second_information, null);
        game = (TextView) infos.findViewById(R.id.battery_game_left);
        phone = (TextView) infos.findViewById(R.id.battery_phone_left);
        internet = (TextView) infos.findViewById(R.id.battery_internet_left);
        temp = (TextView) infos.findViewById(R.id.battery_temperature);
        cpu = (TextView) infos.findViewById(R.id.battery_cpu);
        ram = (TextView) infos.findViewById(R.id.battery_ram);

        adLayout = new LinearLayout(mContext);
        adLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        adLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        int id = (int) Utils.readData(mContext, Constants.CHARGE_SAVER_ICON, R.mipmap.battery_inner_icon);
        if (id > 0) {
            icon.setImageResource(id);
        }
        title.setText((String) Utils.readData(mContext, Constants.CHARGE_SAVER_TITLE, "EOSBATTERY"));
        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLayout.setVisibility(VISIBLE);
                if ((Boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, true)) {
                    switchBox.setChecked(true);
                } else {
                    switchBox.setChecked(false);
                }
            }
        });
        switchBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Boolean) Utils.readData(mContext, Constants.CHARGE_SAVER_SWITCH, true)) {
                    Utils.writeData(mContext, Constants.CHARGE_SAVER_SWITCH, false);
                    switchBox.setChecked(false);
                } else {
                    Utils.writeData(mContext, Constants.CHARGE_SAVER_SWITCH, true);
                    switchBox.setChecked(true);
                }
            }
        });

    }

    private void initViewPager() {
        showNativeAD(Constants.TAG_DUCK_CHARGING);
        List<View> list = new ArrayList<>();
        if (infos != null) {
            list.add(infos);
        }
        if (adView != null) {
            indicator.setVisibility(VISIBLE);
            list.add(adLayout);
        }

        mViewPager.setAdapter(new Adapter(list));
        indicator.setViewPager(mViewPager);
        mHandler.sendEmptyMessageDelayed(0, 2000);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (mViewPager != null && adView != null) {
                    mViewPager.setCurrentItem(1);
                }
            }
        }
    };

    private class Adapter extends PagerAdapter {

        private List<View> list;

        public Adapter(List<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }

    private void initShell(){
        try{
            String pkg = ThemeManager.currentTheme().getPackageName();
            Context themeContext = mContext.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
            shell.setImageAssetsFolder(themeContext, "theme://images/shell");
            shell.setAnimation(themeContext, "theme://shell.json");
            shell.loop(true);
            shell.playAnimation();
        } catch (Exception e) {
            shell.setImageAssetsFolder(null, "images/shell");
            shell.setAnimation(null, "shell.json");
        }
        shell.loop(true);
        shell.playAnimation();
    }

    private void initWater(){
        try{
            String pkg = ThemeManager.currentTheme().getPackageName();
            Context themeContext = mContext.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
            water.setImageAssetsFolder(themeContext, "theme://images/water");
            water.setAnimation(themeContext, "theme://water.json");
        } catch (Exception e) {
            if (!water.isAnimating()) {
                water.setImageAssetsFolder(null, "images/water");
                water.setAnimation(null, "water.json");
            }
        }
        water.loop(true);
        water.setSpeed(5.0f);
    }

    private void initBack(){
        try{
            String pkg = ThemeManager.currentTheme().getPackageName();
            Context themeContext = mContext.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY);
            InputStream input = themeContext.getAssets().open("eos_back.png");
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inScaled = false;
            opts.inDensity = 160;
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, opts);
            if (bitmap != null && duckView != null) {
                duckView.setBackgroundDrawable(new BitmapDrawable(bitmap));
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isBindView) {
            initViews();
            isBindView = true;

            initShell();
            initBack();

            initViewPager();

            final int halfWidth = (int) (((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth() / 1.5f);
            setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (switchLayout.getVisibility() == View.VISIBLE) {
                        switchLayout.setVisibility(View.GONE);
                    }
                    detector.onTouchEvent(event);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (distance > halfWidth) {
                            if (listener != null) {
                                listener.onUnlock();
                            }
                        } else {
                            if (slide != null) {
                                slide.setAlpha(1.f);
                                ObjectAnimator animator = ObjectAnimator.ofFloat(slide, "translationX", 0);
                                animator.setDuration(500);
                                animator.start();
                            }
                        }
                    }
                    return true;
                }
            });
            detector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (velocityX > 1000) {
                        if (listener != null) {
                            listener.onUnlock();
                        }
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    distance = e2.getX() - e1.getX();
                    if (slide != null) {
                        slide.setTranslationX(distance);
                        slide.setAlpha(1 - distance / halfWidth + .2f);
                    }
                    return true;
                }
            });

        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isRegisterTimeUpdate) {
            registerTimeUpdateReceiver();
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

        if (isBindView) {
            isBindView = false;
        }
    }


    public void registerTimeUpdateReceiver() {
        mContext.registerReceiver(timerUpdateReceiver, mIntentFilter);
        isRegisterTimeUpdate = true;
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

    public void unregisterTimeUpdateReceiver() {
        mContext.unregisterReceiver(timerUpdateReceiver);
        isRegisterTimeUpdate = false;
    }

}