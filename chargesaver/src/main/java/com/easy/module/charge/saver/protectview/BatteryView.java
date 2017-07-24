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
    int heigiht, heigiht1;
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
        this.heigiht = heigiht;
        postInvalidate();
    }

    public void start(final int dushu) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isStop) {
                    return;
                }
                switch (dushu) {
                    case 2:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d6);
                        break;
                    case 10:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d13);
                        break;
                    case 20:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d26);
                        break;
                    case 30:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d39);
                        break;
                    case 40:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d52);
                        break;
                    case 50:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d65);
                        break;
                    case 60:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d78);
                        break;
                    case 70:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d90);
                        break;
                    case 80:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d100);
                        break;
                    case 90:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d116);
                        break;
                    case 95:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d123);
                        break;
                    default:
                        heigiht1 = getResources().getDimensionPixelSize(R.dimen.d130);
                        break;
                }
                setDuShu(heigiht1);
            }
        }).start();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStop = true;
    }
}
