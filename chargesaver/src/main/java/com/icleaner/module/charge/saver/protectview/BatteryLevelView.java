package com.icleaner.module.charge.saver.protectview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.icleaner.module.charge.saver.R;

/**
 * Created by Ivy on 2017/9/14.
 */

public class BatteryLevelView extends View {

    private int level = 0;

    public BatteryLevelView(Context context) {
        super(context);
        init();
    }

    public BatteryLevelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BatteryLevelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
//        width = height = DisplayUtil.dip2px(getContext(), 180);
        setMeasuredDimension(width, height);
    }

    private void init() {
        getRocket();
        if (ballPaint == null) {
            ballPaint = new Paint();
            ballPaint.setAntiAlias(true);
            ballPaint.setStyle(Paint.Style.FILL);
        }
    }

    private void getRocket() {
        if (battery == null || battery.isRecycled()) {
            battery = BitmapFactory.decodeResource(getResources(), R.mipmap.battery);
        }
        if (battery_di == null || battery_di.isRecycled()) {
            battery_di = BitmapFactory.decodeResource(getResources(), R.mipmap.battery_di);
        }
    }


    private Context mContext;

    private int width;
    private int height;

    private Paint ballPaint = null;

    private Bitmap battery = null;
    private Bitmap battery_di = null;

    private boolean animation = true;


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        init();
        ballPaint.setColor(Color.parseColor("#ffffff"));
        int src = canvas.saveLayer(0, 0, width, height, ballPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(battery_di, 0, 0, ballPaint);
        ballPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        if (level <= 20) {
            ballPaint.setColor(Color.parseColor("#fe3b3b"));
        } else {
            ballPaint.setColor(Color.parseColor("#89e864"));
        }
        ballPaint.setStrokeWidth(height);
        canvas.drawLine(0, height / 2, (width) * level / 100, height / 2, ballPaint);
        ballPaint.setXfermode(null);
        canvas.restoreToCount(src);
        canvas.drawBitmap(battery, 0, 0, ballPaint);

    }

    public void setLevel(int level) {
        this.level = level;
    }


    public void destroy() {
        if (battery != null && battery.isRecycled()) {
            battery.recycle();
        }
        if (battery_di != null && battery_di.isRecycled()) {
            battery_di.recycle();
        }
    }


}
