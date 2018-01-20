package com.eifmobi.clean.junk.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.eifmobi.clean.junk.R;

/**
 */

public class RotateTime extends Button {
    private Paint circlePoint;
    private Context context;
    private int width, height;

    public RotateTime(Context context) {
        this(context, null);
    }

    public RotateTime(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateTime(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    public void setLayerType(int layerType, @Nullable Paint paint) {
        super.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
    }

    private void init() {
        circlePoint = new Paint();
        circlePoint.setColor(ContextCompat.getColor(context, R.color.ad_1));
        circlePoint.setAntiAlias(true);
        circlePoint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF();
        rect.left = 0 + getResources().getDimensionPixelSize(R.dimen.d8);
        rect.right = width - getResources().getDimensionPixelSize(R.dimen.d8);
        rect.top = 0 + getResources().getDimensionPixelSize(R.dimen.d12);
        rect.bottom = height - getResources().getDimensionPixelSize(R.dimen.d12);
        canvas.save();
        canvas.rotate(-2, width / 2, height / 2);
        canvas.drawRect(rect, circlePoint);
        canvas.restore();
    }


}
