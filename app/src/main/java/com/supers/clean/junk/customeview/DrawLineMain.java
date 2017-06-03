package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/6/3.
 */

public class DrawLineMain extends View {

    private int width;
    private int height;
    private Context mContext;

    private Paint point;

    public DrawLineMain(Context context) {
        this(context, null);
    }

    public DrawLineMain(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawLineMain(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        point = new Paint();
        point.setAntiAlias(true);
        point.setColor(ContextCompat.getColor(mContext, R.color.A7));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, mContext.getResources().getDimensionPixelOffset(R.dimen.d61));
        path.lineTo(width, mContext.getResources().getDimensionPixelOffset(R.dimen.d18));
        path.lineTo(width, height);
        path.lineTo(0, height);
        canvas.drawPath(path, point);

    }
}
