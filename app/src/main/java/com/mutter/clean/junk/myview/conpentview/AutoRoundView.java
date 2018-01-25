package com.mutter.clean.junk.myview.conpentview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frigate.layout.FrigateFrameLayout;
import com.frigate.utils.AutoUtils;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myview.RoundSd;

/**
 * Created by ${} on 2018/1/23.
 */

public class AutoRoundView extends FrigateFrameLayout {

    Context mContext;
    RoundSd main_custom_sd;
    TextView name, memory, memory_danwei, usage;
    RelativeLayout memory_fl;
    Handler handler;
    private int backageColor;
    private BitmapDrawable bitmap;
    private float bitmapSize;
    private int endColor;
    private int startColor;
    private float lineWidth;
    private int startAngle;
    private int memorya;
    private float memory_text_size;
    private float memory_marge_top;
    private String memory_danwei_name;
    private float memory_danwei_size;
    private float name_marge_top;
    private String namea;
    private float name_text_size;
    private String usagea;
    private float usage_marge_top;
    private float usage_text_size;

    public AutoRoundView(Context context) {
        this(context, null);
    }

    public AutoRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        handler = new Handler();
        LayoutInflater.from(context).inflate(R.layout.auto_round, this, true);

        intView();
        sendAttr(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void sendMessage() {
        super.sendMessage();
        sendMess();
    }

    private void sendMess() {
        if (backageColor != -1) {
            main_custom_sd.setBackagePaintColor(backageColor);
        }
        if (bitmap != null) {
            main_custom_sd.setBitmap(bitmap.getBitmap());
        }
        if (bitmapSize != 0) {
            main_custom_sd.setBitmapSize((int) bitmapSize);
        }
        if (endColor != -1) {
            main_custom_sd.setLineColor(new int[]{startColor, endColor});
        } else if (startColor != -1) {
            main_custom_sd.setLineColor(new int[]{startColor});
        }
        if (lineWidth != 0) {
            main_custom_sd.setLineWidth((int) lineWidth);
        }
        main_custom_sd.setStartAngle((int) startAngle);

/**/
        if (memorya != 0) {
            main_custom_sd.startProgress(true, memorya);
        }
        if (memory_text_size != 0) {
            memory.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize((int) memory_text_size));
        }
        if (memory_danwei_size != 0) {
            memory_danwei.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize((int) memory_danwei_size));
        }
        if (memory_marge_top != 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) memory_fl.getLayoutParams();
            layoutParams.topMargin = AutoUtils.getPercentHeightSize((int) memory_marge_top);
            memory_fl.setLayoutParams(layoutParams);
        }

/**/
        if (memory_danwei_name != null) {
            memory_danwei.setText(memory_danwei_name);
        }

        /**/
        if (namea != null) {
            name.setText(namea);
        }
        if (name_text_size != 0) {
            name.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize((int) name_text_size));
        }
        if (name_marge_top != 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) name.getLayoutParams();
            layoutParams.topMargin = AutoUtils.getPercentHeightSize((int) name_marge_top);
            name.setLayoutParams(layoutParams);
        }

        /**/
        if (usagea != null) {
            usage.setText(usagea);
        }
        if (usage_text_size != 0) {
            usage.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize((int) usage_text_size));
        }
        if (usage_marge_top != 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) usage.getLayoutParams();
            layoutParams.topMargin = AutoUtils.getPercentHeightSize((int) usage_marge_top);
            usage.setLayoutParams(layoutParams);
        }


    }

    private void sendAttr(AttributeSet attrs) {
        TypedArray ta = this.mContext.obtainStyledAttributes(attrs, R.styleable.AutoRoundView);
//        yuanqiu
        backageColor = ta.getColor(R.styleable.AutoRoundView_backColor, -1);
        bitmap = (BitmapDrawable) ta.getDrawable(R.styleable.AutoRoundView_bitmap);
        bitmapSize = ta.getDimension(R.styleable.AutoRoundView_bitmapSize, 0);
        endColor = ta.getColor(R.styleable.AutoRoundView_endColor, -1);
        startColor = ta.getColor(R.styleable.AutoRoundView_startColor, -1);
        lineWidth = ta.getDimension(R.styleable.AutoRoundView_lineWidth, 0);
        startAngle = ta.getInt(R.styleable.AutoRoundView_startAngle, 90);


        /**/
        memorya = ta.getInt(R.styleable.AutoRoundView_memory, 0);
        memory_text_size = ta.getDimension(R.styleable.AutoRoundView_memory_text_size, 0);
        memory_marge_top = ta.getDimension(R.styleable.AutoRoundView_memory_marge_top, 0);


        memory_danwei_name = ta.getString(R.styleable.AutoRoundView_memory_danwei);
        memory_danwei_size = ta.getDimension(R.styleable.AutoRoundView_memory_danwei_size, 0);


        namea = ta.getString(R.styleable.AutoRoundView_name);
        name_marge_top = ta.getDimension(R.styleable.AutoRoundView_name_marge_top, 0);
        name_text_size = ta.getDimension(R.styleable.AutoRoundView_name_text_size, 0);

        usagea = ta.getString(R.styleable.AutoRoundView_usage);
        usage_marge_top = ta.getDimension(R.styleable.AutoRoundView_usage_marge_top, 0);
        usage_text_size = ta.getDimension(R.styleable.AutoRoundView_usage_text_size, 0);
        ta.recycle();

    }

    private void intView() {
        main_custom_sd = (RoundSd) findViewById(R.id.main_custom_sd);
        name = (TextView) findViewById(R.id.name);
        memory_fl = (RelativeLayout) findViewById(R.id.memory_fl);
        memory = (TextView) findViewById(R.id.memory);
        memory_danwei = (TextView) findViewById(R.id.memory_danwei);
        usage = (TextView) findViewById(R.id.usage);
        main_custom_sd.setCustomRoundListener(new RoundSd.CustomRoundListener() {
            @Override
            public void progressUpdate(final int progress) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        memory.setText(progress + "");
                    }
                });
            }
        });
    }

    public void setMemory(int memo) {
        main_custom_sd.startProgress(true, memo);
    }

    public void setName(String namea) {
        name.setText(namea);
    }

    public void setUsage(String usagea) {
        usage.setText(usagea);
    }
}
