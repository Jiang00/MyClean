package com.fast.clean.junk.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.fast.clean.junk.R;

/**
 */

public class CpuRoundView extends View {

    private Context context;
    private Paint circlePoint;
    float lineWidth = getResources().getDimension(R.dimen.d4);
    int size;
    private int progress;
    private boolean isRotate;
    private CustomRoundListener customRoundListener;

    public CpuRoundView(Context context) {
        this(context, null);
    }

    public CpuRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CpuRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;

    }

    public interface CustomRoundListener {
        void progressUpdate(int progress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        size = d;
        setMeasuredDimension(d, d);

    }

    private void init() {
        circlePoint = new Paint();
        circlePoint.setAntiAlias(true);
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePoint.setColor(context.getResources().getColor(R.color.A5));
        RectF rect = new RectF();
        rect.left = 0 + lineWidth / 2 + 1;
        rect.right = size - lineWidth / 2 - 1;
        rect.top = 0 + lineWidth / 2 + 1;
        rect.bottom = size - lineWidth / 2 - 1;
        canvas.drawArc(rect, 0, 360, false, circlePoint);
        canvas.save();
        if (progress >= 0 && progress < 40) {
            circlePoint.setColor(context.getResources().getColor(R.color.A4));
        } else if (progress >= 40 && progress < 80) {
            circlePoint.setColor(context.getResources().getColor(R.color.A3));
        } else {
            circlePoint.setColor(context.getResources().getColor(R.color.A2));
        }
        if (isRotate) {
            canvas.rotate(61, size / 2, size / 2);
            canvas.drawArc(rect, 0, progress * 310 / 100, false, circlePoint);
        } else {
            canvas.rotate(90, size / 2, size / 2);
            canvas.drawArc(rect, 0, progress * 360 / 100, false, circlePoint);
        }
        canvas.restore();
    }

    public void startProgress(boolean isRotate, final int progress) {
        this.isRotate = isRotate;
        final int st = this.progress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= progress; i++) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setProgress(i);
                    if (customRoundListener != null) {
                        customRoundListener.progressUpdate(i);
                    }
                }
            }
        }).start();

    }


}
