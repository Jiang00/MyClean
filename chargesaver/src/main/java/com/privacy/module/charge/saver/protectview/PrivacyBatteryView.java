package com.privacy.module.charge.saver.protectview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.privacy.module.charge.saver.R;

public class PrivacyBatteryView extends View {
    Paint paint;
    Paint paint_di, paint_kuang;
    Context context;
    boolean isStop = false;
    int battery;
    int radio;
    int line_whith;
    int width, height;

    public PrivacyBatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.A1));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint_di = new Paint();
        paint_di.setColor(ContextCompat.getColor(context, R.color.white_20));
        paint_di.setAntiAlias(true);
        paint_di.setStyle(Paint.Style.FILL);
        paint_kuang = new Paint();
        paint_kuang.setColor(ContextCompat.getColor(context, R.color.white_100));
        paint_kuang.setAntiAlias(true);
        radio = getResources().getDimensionPixelOffset(R.dimen.d5);
        line_whith = getResources().getDimensionPixelOffset(R.dimen.d2);
        paint_kuang.setStrokeWidth(line_whith);
        paint_kuang.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画矩形(RectF)
        RectF rectf = new RectF(0 + line_whith / 2, 0 + line_whith / 2, width * battery / 100 - line_whith / 2, height - line_whith / 2);
        RectF rectf_di = new RectF(0 + line_whith / 2, 0 + line_whith / 2, width - line_whith / 2, height - line_whith / 2);
        canvas.drawRoundRect(rectf_di, radio, radio, paint_di);
        canvas.drawRoundRect(rectf, radio, radio, paint);
        canvas.drawRoundRect(rectf_di, radio, radio, paint_kuang);
    }

    public void setDuShu(int battery) {
        this.battery = battery;
        postInvalidate();
    }

    public void start(final float battery) {
        setDuShu((int) battery);
//        this.battery = 0;

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.battery = 0;
    }

    public void stop() {
        isStop = true;
    }
}
