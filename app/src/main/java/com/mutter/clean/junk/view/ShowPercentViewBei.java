package com.mutter.clean.junk.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.mutter.clean.junk.R;


public class ShowPercentViewBei extends View {

    private Paint percentPaint;

    Context context;

    private int percentLineWidth = (int) getResources().getDimension(R.dimen.d2);
    private int lineHeight = (int) getResources().getDimension(R.dimen.d8);
    private int line_size = 200;


    public ShowPercentViewBei(Context context) {
        this(context, null);
    }

    public ShowPercentViewBei(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowPercentViewBei(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        this.context = context;
        init(attrs, defStyle);

    }

    private void init(AttributeSet attrs, int defStyle) {
        percentPaint = new Paint();
        percentPaint.setAntiAlias(true);
        percentPaint.setColor(context.getResources().getColor(R.color.white_25));
        percentPaint.setStrokeWidth(percentLineWidth);
        percentPaint.setStrokeCap(Paint.Cap.ROUND);
        percentPaint.setStyle(Paint.Style.FILL);

    }

    private int dp2px(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int pointX = width / 2;
        int pointY = height / 2;


        float degrees = (360f / line_size);
        canvas.save();
        canvas.translate(0, pointY);
        canvas.rotate(90, pointX, 0);

        for (int i = 0; i <= line_size; i++) {
            canvas.drawLine(0 + percentLineWidth, percentLineWidth / 2, lineHeight + percentLineWidth, percentLineWidth / 2, percentPaint);
            canvas.rotate(degrees, pointX, 0);
        }
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        setMeasuredDimension(d, d);
    }

}