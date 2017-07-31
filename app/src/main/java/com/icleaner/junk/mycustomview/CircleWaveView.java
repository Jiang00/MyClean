package com.icleaner.junk.mycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.icleaner.junk.R;

/**
 * Created by on 2017/7/5.
 */

public class CircleWaveView extends View {
    private float radius;//半径
    private float strokeWidth;
    private Paint paint;
    private int width;
    private int height;
    private Context context;
    Handler mHamdle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            if (strokeWidth > getResources().getDimensionPixelSize(R.dimen.d52)) {
                strokeWidth = dip2px(context, 1);
                radius = getResources().getDimensionPixelSize(R.dimen.d78);
                paint.setStrokeWidth(strokeWidth);
            } else {
                strokeWidth += getResources().getDimensionPixelSize(R.dimen.d4);
                radius += getResources().getDimensionPixelSize(R.dimen.d2);
                paint.setStrokeWidth(strokeWidth);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            invalidate();
        }
    };

    public CircleWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        radius = getResources().getDimensionPixelSize(R.dimen.d78);
        strokeWidth = dip2px(context, 1);
        paint.setAntiAlias(true);//设置是否抗锯齿
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(ContextCompat.getColor(context, R.color.B3_5));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width / 2, getResources().getDimensionPixelSize(R.dimen.d138), radius, paint);//根据进度计算扩散半径
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void setRadius(float radius, float strokeWidth) {
        this.radius = radius;
        paint.setStrokeWidth(strokeWidth);
        postInvalidate();
    }

    public void startCircleWaveCiew() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (strokeWidth > getResources().getDimensionPixelSize(R.dimen.d52)) {
                    strokeWidth = dip2px(context, 1);
                    radius = getResources().getDimensionPixelSize(R.dimen.d78);
                } else {
                    strokeWidth += getResources().getDimensionPixelSize(R.dimen.d4);
                    radius += getResources().getDimensionPixelSize(R.dimen.d2);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
