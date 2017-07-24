package com.easy.junk.easycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.easy.junk.R;

/**
 */

public class YuanHuView extends View {
    Paint paint;
    Paint paintCrile;
    RectF rectF;
    Context context;
    float dushu;
    Canvas canvas;

    public YuanHuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d2));
        paintCrile = new Paint();
        paintCrile.setStyle(Paint.Style.FILL);
        paintCrile.setAntiAlias(true);

        rectF = new RectF(0, 0, getResources().getDimensionPixelSize(R.dimen.d48), getResources().getDimensionPixelSize(R.dimen.d48));
        rectF.offset(getResources().getDimensionPixelSize(R.dimen.d1), getResources().getDimensionPixelSize(R.dimen.d1));//左上位置
//        rectF.left = getResources().getDimensionPixelSize(R.dimen.d19);
//        rectF.top = getResources().getDimensionPixelSize(R.dimen.d19);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
//        canvas.restore();
        if (dushu < 108) {
            paintCrile.setColor(ContextCompat.getColor(context, R.color.A1_10));
            paint.setColor(ContextCompat.getColor(context, R.color.A1));
        } else if (dushu >= 108 && dushu < 228) {
            paintCrile.setColor(ContextCompat.getColor(context, R.color.A2_10));
            paint.setColor(ContextCompat.getColor(context, R.color.A2));
        } else {
            paintCrile.setColor(ContextCompat.getColor(context, R.color.A3_10));
            paint.setColor(ContextCompat.getColor(context, R.color.A3));
        }
        canvas.drawArc(rectF, -90, dushu, false, paint);
        canvas.drawCircle(getResources().getDimensionPixelSize(R.dimen.d25), getResources().getDimensionPixelSize(R.dimen.d25),
                getResources().getDimensionPixelSize(R.dimen.d25), paintCrile);
    }

    public void setDuShu(float dushu) {
        this.dushu = dushu;
        postInvalidate();
    }

    boolean isStop;

    public void start(final float dushu1) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i <= dushu1; i++) {
                    if (isStop) {
                        return;
                    }
                    setDuShu(i);
                    try {
                        Thread.sleep(20);
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
        canvas = null;
        isStop = true;
    }

    private YuanHuView.DrawYuanHuListener scanEndListener;

    public void setScanCallBack(YuanHuView.DrawYuanHuListener listener) {
        this.scanEndListener = listener;
    }

    public interface DrawYuanHuListener {
        void scanDushu(float dushu);
    }
}
