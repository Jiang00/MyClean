package com.mutter.clean.junk.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mutter.clean.junk.R;

/**
 */

public class CoolingView extends View {
    float lineWidth = getResources().getDimension(R.dimen.d40);
    float lineWidth_2 = getResources().getDimension(R.dimen.d36);
    float lineWidth_3 = getResources().getDimension(R.dimen.d27);
    float raudio_2 = getResources().getDimension(R.dimen.d36);
    float raudio_3 = getResources().getDimension(R.dimen.d25);
    float progress_h = getResources().getDimension(R.dimen.d61);
    private Context context;
    private Paint circlePoint;
    private Paint circlePoint_2;
    private Paint circlePoint_3;
    int size;
    private int progress;
    private CustomRoundListener customRoundListener;
    private int width;
    private int height;
    private boolean ondestory;

    public CoolingView(Context context) {
        this(context, null);
    }

    public CoolingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoolingView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        circlePoint.setColor(ContextCompat.getColor(context, R.color.A10));
        circlePoint.setStrokeCap(Paint.Cap.ROUND);
        circlePoint.setFilterBitmap(true);
        circlePoint.setAntiAlias(true);
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint_2 = new Paint();
        circlePoint_2.setColor(ContextCompat.getColor(context, R.color.white_100));
        circlePoint_2.setStrokeCap(Paint.Cap.ROUND);
        circlePoint_2.setFilterBitmap(true);
        circlePoint_2.setAntiAlias(true);
        circlePoint_2.setStrokeWidth(lineWidth_2);
        circlePoint_3 = new Paint();
        circlePoint_3.setColor(ContextCompat.getColor(context, R.color.A9));
        circlePoint_3.setStrokeCap(Paint.Cap.ROUND);
        circlePoint_3.setFilterBitmap(true);
        circlePoint_3.setAntiAlias(true);
        circlePoint_3.setStrokeWidth(lineWidth_3);
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
        canvas.drawCircle(width / 2, height - width / 2, width / 2, circlePoint);
        canvas.drawLine(width / 2, height - width / 2, width / 2, lineWidth / 2, circlePoint);
        canvas.drawCircle(width / 2, height - width / 2, raudio_2, circlePoint_2);
        canvas.drawLine(width / 2, height - width / 2, width / 2, lineWidth / 2, circlePoint_2);

        canvas.drawCircle(width / 2, height - width / 2, raudio_3, circlePoint_3);
        circlePoint_3.setColor(ContextCompat.getColor(context, R.color.A9_30));
        canvas.drawLine(width / 2, height - width / 2, width / 2, lineWidth / 2, circlePoint_3);
        circlePoint_3.setColor(ContextCompat.getColor(context, R.color.A9));
        canvas.drawLine(width / 2, height - width / 2, width / 2, lineWidth / 2 + (Math.abs(100 - progress) * progress_h / 100), circlePoint_3);
    }

    public void startProgress2(final int progress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    if (ondestory) {
                        return;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ondestory) {
                        return;
                    }
                    setProgress(i);
                }
                for (int i = 100; i >= progress; i--) {
                    if (ondestory) {
                        return;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (ondestory) {
                        return;
                    }
                    setProgress(i);
                }
                if (customRoundListener != null) {
                    customRoundListener.progressSuccess();
                }

            }
        }).start();

    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void onDecsory() {
        ondestory = true;
    }

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;

    }

    public interface CustomRoundListener {
        void progressSuccess();
    }
}
