package com.vector.cleaner.myview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.vector.cleaner.R;

/**
 */

public class LineRam extends View {
    private Context context;
    private Paint circlePoint;
    float lineWidth = getResources().getDimension(R.dimen.d4);
    private int progress;
    private int width, height;
    private CustomRoundListener customRoundListener;

    public LineRam(Context context) {
        this(context, null);
    }

    public LineRam(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineRam(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);

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
        circlePoint.setColor(context.getResources().getColor(R.color.white_15));
        canvas.drawLine(0, height / 2, width, height / 2, circlePoint);
        if (progress >= 0 && progress < 40) {
            circlePoint.setColor(context.getResources().getColor(R.color.A4));
        } else if (progress >= 40 && progress < 80) {
            circlePoint.setColor(context.getResources().getColor(R.color.A3));
        } else {
            circlePoint.setColor(context.getResources().getColor(R.color.A2));
        }
        canvas.drawLine(0, height / 2, width * progress / 100, height / 2, circlePoint);
    }

    public void startProgress(final int progress) {
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

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;
    }

    public interface CustomRoundListener {
        void progressUpdate(int progress);
    }

}
