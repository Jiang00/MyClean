package com.myboost.junk.privacycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.myboost.junk.R;

public class MainYuanHuView extends View {
    Paint paint;
    RectF rectF, rectF1;
    Context context;
    float dushu;
    Canvas canvas;
    int startDuShu;
    boolean isStop;

    public MainYuanHuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d8));
        paint.setColor(ContextCompat.getColor(context, R.color.A5));

        rectF = new RectF(0, 0, getResources().getDimensionPixelSize(R.dimen.d216), getResources().getDimensionPixelSize(R.dimen.d216));
        rectF.left = getResources().getDimensionPixelSize(R.dimen.d4);
        rectF.top = getResources().getDimensionPixelSize(R.dimen.d4);

        rectF1 = new RectF(0, 0,
                getResources().getDimensionPixelSize(R.dimen.d192), getResources().getDimensionPixelSize(R.dimen.d192));
        rectF1.left = getResources().getDimensionPixelSize(R.dimen.d28);
        rectF1.top = getResources().getDimensionPixelSize(R.dimen.d28);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawArc(rectF, 150, dushu, false, paint);
        if (dushu <= 39) {
            canvas.drawArc(rectF1, 150, dushu, false, paint);
        } else if (dushu <= 79) {
            canvas.drawArc(rectF1, 150, 39, false, paint);
            canvas.drawArc(rectF1, 190, dushu - 40, false, paint);
        } else if (dushu <= 119.5) {
            canvas.drawArc(rectF1, 150, 39, false, paint);
            canvas.drawArc(rectF1, 190, 39, false, paint);
            canvas.drawArc(rectF1, 230.5f, dushu - 80, false, paint);
        } else if (dushu <= 160) {
            canvas.drawArc(rectF1, 150, 39, false, paint);
            canvas.drawArc(rectF1, 190, 39, false, paint);
            canvas.drawArc(rectF1, 230.5f, 39, false, paint);
            canvas.drawArc(rectF1, 271, dushu - 120, false, paint);
        } else if (dushu <= 199.5) {
            canvas.drawArc(rectF1, 150, 39, false, paint);
            canvas.drawArc(rectF1, 190, 39, false, paint);
            canvas.drawArc(rectF1, 230.5f, 39f, false, paint);
            canvas.drawArc(rectF1, 271, 39, false, paint);
            canvas.drawArc(rectF1, 311.5f, dushu - 160, false, paint);
        } else {
            canvas.drawArc(rectF1, 150, 39, false, paint);
            canvas.drawArc(rectF1, 190, 39, false, paint);
            canvas.drawArc(rectF1, 230.5f, 39f, false, paint);
            canvas.drawArc(rectF1, 271, 39, false, paint);
            canvas.drawArc(rectF1, 311.5f, 38, false, paint);
            canvas.drawArc(rectF1, 351.5f, dushu - 201.5f, false, paint);
        }

        paint.setColor(0x8affffff);
        canvas.drawArc(rectF, 150, 240, false, paint);

        canvas.drawArc(rectF1, 150, 39, false, paint);
        canvas.drawArc(rectF1, 190, 39, false, paint);
        canvas.drawArc(rectF1, 230.5f, 39, false, paint);
        canvas.drawArc(rectF1, 271, 39, false, paint);
        canvas.drawArc(rectF1, 311.5f, 38, false, paint);
        canvas.drawArc(rectF1, 351.5f, 38.5f, false, paint);
    }

    public void setDuShu(float dushu) {
        this.dushu = dushu;
        postInvalidate();
    }

    public void start(final float dushu) {
        this.dushu = 0;
        isStop = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (float i = 0, j = 240; i <= 240 && j >= (int) dushu; ) {
                    if (isStop) {
                        if (scanEndListener != null) {
                            scanEndListener.scanDushu(-1);
                        }
                        return;
                    }
                    if (i == 240) {
                        if (scanEndListener != null) {
                            scanEndListener.scanDushu(j);
                        }
                        setDuShu(j);
                        j--;
                    } else {
                        setDuShu(i);
                        if (scanEndListener != null) {
                            scanEndListener.scanDushu(i);
                        }
                        i++;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (scanEndListener != null) {
                    scanEndListener.scanDushu(-2);
                }
            }
        }).start();
    }

    public void stop() {
        canvas = null;
        isStop = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        canvas = null;
        isStop = true;
    }

    private MainYuanHuView.DrawYuanHuListener scanEndListener;

    public void setScanCallBack(MainYuanHuView.DrawYuanHuListener listener) {
        this.scanEndListener = listener;
    }

    public interface DrawYuanHuListener {
        void scanDushu(float dushu);
    }
}
