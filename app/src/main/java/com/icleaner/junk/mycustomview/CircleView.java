package com.icleaner.junk.mycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.icleaner.junk.R;

public class CircleView extends View {
    private Context context;
    private int width;
    private int height;
    private Paint mPaintCircle;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);//设置是否抗锯齿
        mPaintCircle.setStyle(Paint.Style.STROKE);//设置绘制风格
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintCircle.setColor(ContextCompat.getColor(context, R.color.B3_5));
        mPaintCircle.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d1));//设置线宽
        //画出大圆
        canvas.drawCircle(width / 2, getResources().getDimensionPixelSize(R.dimen.d138), getResources().getDimensionPixelSize(R.dimen.d130), mPaintCircle);
        mPaintCircle.setColor(ContextCompat.getColor(context, R.color.B3_12));
        mPaintCircle.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d2));//设置线宽
        //画出中心圆
        canvas.drawCircle(width / 2, getResources().getDimensionPixelSize(R.dimen.d138), getResources().getDimensionPixelSize(R.dimen.d118), mPaintCircle);

    }
}
