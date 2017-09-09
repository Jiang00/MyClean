package com.bruder.clean.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CanvasView extends FrameLayout {

    Path mPath = new Path();
    Path sTraversalPath = new Path();

    Paint mPathPaint = new Paint();

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPathPaint.setColor(0xFFFFFFFF);
        mPathPaint.setStyle(Paint.Style.STROKE);
        mPathPaint.setAlpha(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            Matrix scale = new Matrix();
            float scaleWidth = (right - left) / 7.0f; //(1080 - 0 )/7.0f; = 154.028
            float scaleHeight = (bottom - top) / 7.0f;//174.042
            scale.setScale(scaleWidth, scaleHeight);
            sTraversalPath.transform(scale, mPath);
        }
    }

    public Path getPath() {
        return mPath;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPathPaint);
        super.draw(canvas);
    }

    public void setsTraversalPath(Path sTraversalPath) {
        this.sTraversalPath = sTraversalPath;
    }
}
