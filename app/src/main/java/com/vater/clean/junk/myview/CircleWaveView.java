package com.vater.clean.junk.myview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.vater.clean.junk.R;

/**
 * Created by Ivy on 2017/7/5.
 */

public class CircleWaveView extends View {
    private float maxWaveRadius;//扩散半径
    private long waveTime = 2500;//一个涟漪扩散的时间
    private int waveRate = 1;//涟漪的个数
    private Paint paint;
    private int width;
    private int height;
    private Context context;
    private float[] waveList;//涟漪队列

    public CircleWaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    //构造方法略
    private void init() {
        paint = new Paint();
        maxWaveRadius = dip2px(context, 131);
        waveList = new float[waveRate];
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(waveTime);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                for (int i = 0; i < waveList.length; i++) {
                    float v = value - i * 1.0f / waveRate;
                    if (v < 0 && waveList[i] > 0) {
                        v += 1;
                    }
                    waveList[i] = v;
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Float waveRadius : waveList) {
            paint.setColor(ContextCompat.getColor(context, R.color.B3));
//            paint.setColor(Color.argb((int) (255 * (1 - waveRadius)), 60, 114, 236));//根据进度产生一个argb颜色
            canvas.drawCircle(width / 2, dip2px(context, 138), waveRadius * maxWaveRadius, paint);//根据进度计算扩散半径
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
