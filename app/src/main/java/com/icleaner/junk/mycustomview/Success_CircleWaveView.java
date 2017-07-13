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

public class Success_CircleWaveView extends View {
    private float radius;//半径
    private float strokeWidth;
    private Paint paint;
    private int width;
    private int height;
    private Context context;
    private boolean flag;
    Handler mHamdle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            strokeWidth += getResources().getDimensionPixelSize(R.dimen.d4);
            radius -= getResources().getDimensionPixelSize(R.dimen.d2);
            paint.setStrokeWidth(strokeWidth);
            invalidate();
        }
    };

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
        if (radius < getResources().getDimensionPixelSize(R.dimen.d23) || strokeWidth > getResources().getDimensionPixelSize(R.dimen.d44)) {
            paint.setStyle(Paint.Style.FILL);
            radius = getResources().getDimensionPixelSize(R.dimen.d44);
            strokeWidth = getResources().getDimensionPixelSize(R.dimen.d23);
            paint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(getResources().getDimensionPixelSize(R.dimen.d45), getResources().getDimensionPixelSize(R.dimen.d45), radius, paint);//根据进度计算扩散半径
        } else {
            canvas.drawCircle(getResources().getDimensionPixelSize(R.dimen.d45), getResources().getDimensionPixelSize(R.dimen.d45), radius, paint);//根据进度计算扩散半径
            if (radius > 9 && radius != getResources().getDimensionPixelSize(R.dimen.d4)) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHamdle.handleMessage(null);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public void startCircleWaveCiew(boolean flag) {
        this.flag = flag;
        invalidate();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
