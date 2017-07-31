package com.icleaner.junk.mycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.icleaner.junk.R;

/**
 * Created by on 2017/7/5.
 */

public class Success_CircleWaveView extends View {
    private float radius;//半径
    private float strokeWidth;
    private Paint paint;
    private int width;
    private int height;
    private Context context;
    boolean isStop;

    public Success_CircleWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        radius = getResources().getDimensionPixelSize(R.dimen.d44);
        strokeWidth = getResources().getDimensionPixelSize(R.dimen.d1);
        paint.setAntiAlias(true);//设置是否抗锯齿
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(ContextCompat.getColor(context, R.color.B3));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // d3  d30
        canvas.drawCircle(getResources().getDimensionPixelSize(R.dimen.d50), getResources().getDimensionPixelSize(R.dimen.d50), radius, paint);//根据进度计算扩散半径
    }

    private void setRadius(float radius, float strokeWidth) {
        this.radius = radius;
        paint.setStrokeWidth(strokeWidth);
        postInvalidate();
    }

    public void startCircleWaveCiew() {
        isStop = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = getResources().getDimensionPixelSize(R.dimen.d44), j = getResources().getDimensionPixelSize(R.dimen.d1);
                     i > getResources().getDimensionPixelSize(R.dimen.d22); ) {
                    if (isStop) {
                        return;
                    }
                    if (i <= getResources().getDimensionPixelSize(R.dimen.d24)) {
                        i = getResources().getDimensionPixelSize(R.dimen.d22);
                        j = getResources().getDimensionPixelSize(R.dimen.d44);
                        setRadius(i, j);
                    } else {
                        setRadius(i, j);
                    }

                    j += getResources().getDimensionPixelSize(R.dimen.d4);
                    i -= getResources().getDimensionPixelSize(R.dimen.d2);

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStop = true;
    }

    public void stop() {
        isStop = true;
    }
}
