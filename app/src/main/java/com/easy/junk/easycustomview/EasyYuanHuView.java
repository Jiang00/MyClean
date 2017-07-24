package com.easy.junk.easycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.easy.junk.R;

/**
 * Created by Ivy on 2017/7/17.
 */

public class EasyYuanHuView extends View {
    Paint paint;
    RectF rectF;
    Context context;
    float dushu;
    float dushuMax;

    public EasyYuanHuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        dushuMax = 269;
        dushu = 0;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.A7));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d20));

        rectF = new RectF(getResources().getDimensionPixelSize(R.dimen.d17), getResources().getDimensionPixelSize(R.dimen.d17),
                getResources().getDimensionPixelSize(R.dimen.d217), getResources().getDimensionPixelSize(R.dimen.d217));
//        rectF.offset(getResources().getDimensionPixelSize(R.dimen.d4), getResources().getDimensionPixelSize(R.dimen.d4));//左上位置
        rectF.left = getResources().getDimensionPixelSize(R.dimen.d19);
        rectF.top = getResources().getDimensionPixelSize(R.dimen.d19);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rectF, 136, dushu, false, paint);
//        if (dushu < dushuMax) {
//            handler.handleMessage(null);
//        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dushu += 1;
            if (dushu > dushuMax) {
                dushu = dushuMax;
            }
            if (scanEndListener != null) {
                scanEndListener.scanDushu(dushu);
            }
            invalidate();
        }
    };

    public void startYuanHuView(float dushu) {
        if (dushu != 0) {
            dushuMax = dushu - 1;
            this.dushu = 0;
            invalidate();
        }
    }

    public void setDuShu(float dushu) {
        this.dushu = dushu;
        postInvalidate();
    }

    boolean isStop;

    public void start(final float dushu) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0, j = 270; i < 270 && j > dushu - 2; ) {
                    if (isStop) {
                        if (scanEndListener != null) {
                            scanEndListener.scanDushu(-1);
                        }
                        return;
                    }
                    if (i == 269) {
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
            }
        }).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStop = true;
    }

    private EasyYuanHuView.DrawYuanHuListener scanEndListener;

    public void setScanCallBack(EasyYuanHuView.DrawYuanHuListener listener) {
        this.scanEndListener = listener;
    }

    public interface DrawYuanHuListener {
        void scanDushu(float dushu);
    }
}
