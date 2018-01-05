package com.upupup.clean.junk.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.upupup.clean.junk.R;

/**
 */

public class FloatRound extends View {
    float lineWidth = getResources().getDimension(R.dimen.d4);
    private Context context;
    private Paint circlePoint;
    int size;
    private int progress;
    private CustomRoundListener customRoundListener;

    public FloatRound(Context context) {
        this(context, null);
    }

    public FloatRound(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatRound(Context context, AttributeSet attrs, int defStyleAttr) {
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
        circlePoint.setColor(ContextCompat.getColor(context, R.color.float_kaiguan));
        circlePoint.setStrokeCap(Paint.Cap.ROUND);
        circlePoint.setFilterBitmap(true);
        circlePoint.setAntiAlias(true);
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint.setStyle(Paint.Style.STROKE);
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


    public void startProgress1() {
        final int st = this.progress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = st; i >= 0; i--) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setProgress(i);
                }
                if (customRoundListener != null) {
                    customRoundListener.progressUpdate();
                }

            }
        }).start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF();
        rect.left = 0 + lineWidth / 2;
        rect.right = size - lineWidth / 2;
        rect.top = 0 + lineWidth / 2;
        rect.bottom = size - lineWidth / 2;
        canvas.save();
        canvas.rotate(90, size / 2, size / 2);
        canvas.drawArc(rect, 0, -progress * -360 / 100, false, circlePoint);
        canvas.restore();
    }

    public void startProgress2(final int progress) {
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
                }

            }
        }).start();

    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;

    }

    public interface CustomRoundListener {
        void progressUpdate();
    }
}
