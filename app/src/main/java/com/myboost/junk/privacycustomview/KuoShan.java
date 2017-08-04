package com.myboost.junk.privacycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.myboost.module.charge.saver.R;

/**
 */

public class KuoShan extends View {
    Paint paint;
    int radius, radiusMax, radiusMin;
    boolean flag = true;
    boolean stopFlag = false;
    int strokeWidth;
    Context context;
    int width, height;
    Canvas canvas;
    float alpha1, alpha2;

    public KuoShan(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        radius = getResources().getDimensionPixelSize(R.dimen.d116);
        strokeWidth = getResources().getDimensionPixelSize(R.dimen.d1);
        paint = new Paint();
        paint.setColor(0x33ffffff);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
//        this.canvas.drawCircle(width, height, radiusMin, paint);//最小圆
//        canvas.drawCircle(width, height, getResources().getDimensionPixelSize(R.dimen.d268), paint);//最大圆
        if (flag) {
            paint.setAlpha((int) alpha1);
            this.canvas.drawCircle(width, height, radius, paint);
            if (radius >= (radiusMax - radiusMin) / 2 + radiusMin) {
                alpha2 = alpha1 + 16;
                paint.setAlpha((int) alpha2);
                this.canvas.drawCircle(width, height, radius - (radiusMax - radiusMin) / 2, paint);
            }
        } else {
            if (radius <= (radiusMax - radiusMin) / 2 + radiusMin) {
                alpha2 = alpha1 - 16;
                paint.setAlpha((int) alpha2);
                this.canvas.drawCircle(width, height, radius + (radiusMax - radiusMin) / 2, paint);
                paint.setAlpha((int) alpha1);
                this.canvas.drawCircle(width, height, radius, paint);
            } else {
                alpha2 = alpha1 + 16;
                paint.setAlpha((int) alpha2);
                this.canvas.drawCircle(width, height, radius - (radiusMax - radiusMin) / 2, paint);

                paint.setAlpha((int) alpha1);
                this.canvas.drawCircle(width, height, radius, paint);
            }
        }
    }

    public void setRadius(int radius) {
        this.radius = radius;
        postInvalidate();
    }

    public void start(final int radiusMax, final int radiusMin, int strokeWidth, final int time, final float jianAlphal) {
        this.radiusMax = radiusMax;
        this.radiusMin = radiusMin;
        this.strokeWidth = strokeWidth;
        width = radiusMax;
        height = radiusMax;
        alpha1 = 33;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = radiusMin; i <= radiusMax; i++) {
                    if (stopFlag) {
                        if (canvas != null) {
                            canvas = null;
                        }
                        radius = radiusMin;
                        return;
                    }
                    alpha1 -= jianAlphal;
                    setRadius(i);
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == width) {
                        alpha1 = 33;
                        i = radiusMin;
                        flag = false;
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopFlag = true;
    }
}
