package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/4/27.
 */

public class LineProgressView extends View {

    Paint paint;
    float whight, hight;
    int progress;
    Context context;

    public LineProgressView(Context context) {
        this(context, null);

    }

    public LineProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        whight = MeasureSpec.getSize(widthMeasureSpec);
        hight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) whight, (int) hight);
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(hight);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(context, R.color.app_color_first));
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawLine(0, hight / 2, whight, hight / 2, paint_white);
        canvas.drawLine(0, hight / 2, whight * progress / 100, hight / 2, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
