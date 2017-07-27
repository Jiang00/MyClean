package com.privacy.module.charge.saver.protectview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.privacy.module.charge.saver.R;

/**
 */

public class PrivacyBatteryView extends View {
    Paint paint;
    Context context;
    boolean isStop = false;
    int battery;
    Canvas canvas;

    public PrivacyBatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.A1));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    int w = (int) getResources().getDimension(R.dimen.d112);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        // 画矩形(RectF)
        RectF rectf = new RectF(0, 0, w * battery / 100, getResources().getDimension(R.dimen.d54));
        canvas.drawRect(rectf, paint);
    }

    public void setDuShu(int battery) {
        this.battery = battery;
        postInvalidate();
    }

    public void start(final float battery) {
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        this.battery = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= battery; i++) {
                    if (isStop) {
                        return;
                    }
                    setDuShu(i);
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == battery) {
                        i = 0;
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
        this.battery = 0;
        isStop = true;
    }
}
