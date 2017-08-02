package com.easy.module.charge.saver.protectview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.easy.module.charge.saver.R;

/**
 * 电池动画
 */

public class BatteryView extends View {
    Paint paint;
    Context context;
    int heigiht;
    int heigihtMax;
    Rect rect;
    Canvas canvas;

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        heigihtMax = getResources().getDimensionPixelSize(R.dimen.d133);
        heigiht = 0;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.list_progress_green));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d20));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        rect = new Rect(0, heigihtMax - heigiht, getResources().getDimensionPixelSize(R.dimen.d66), heigihtMax);
        canvas.drawRect(rect, paint);
    }

    boolean isStop;

    public void setDuShu(int heigiht) {
        this.heigiht = heigihtMax * heigiht / 100;
        postInvalidate();
    }

    public void start(final int dushu) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isStop) {
                    return;
                }
                setDuShu(dushu);
            }
        }).start();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStop = true;
    }
}
