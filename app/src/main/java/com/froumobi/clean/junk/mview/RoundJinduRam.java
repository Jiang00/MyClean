package com.froumobi.clean.junk.mview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.froumobi.clean.junk.R;

/**
 */

public class RoundJinduRam extends View {

    private Context context;
    private Paint circlePoint;
    float lineWidth = getResources().getDimension(R.dimen.d3);
    int size;
    private int progress;
    private CustomRoundListener customRoundListener;
    private Matrix mMatrix;

    public RoundJinduRam(Context context) {
        this(context, null);
    }

    public RoundJinduRam(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundJinduRam(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        circlePoint = new Paint();
        circlePoint.setAntiAlias(true);
        circlePoint.setStrokeCap(Paint.Cap.ROUND);
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint.setStyle(Paint.Style.STROKE);
        mMatrix = new Matrix();
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePoint.setColor(context.getResources().getColor(R.color.r_35));
        RectF rect = new RectF();
        rect.left = 0 + lineWidth / 2;
        rect.right = size - lineWidth / 2;
        rect.top = 0 + lineWidth / 2;
        rect.bottom = size - lineWidth / 2;
        canvas.drawArc(rect, 0, 360, false, circlePoint);
        canvas.save();

        circlePoint.setColor(context.getResources().getColor(R.color.r_100));
        canvas.rotate(-90, size / 2, size / 2);
        canvas.drawArc(rect, 0, progress * -360 / 100, false, circlePoint);
        canvas.restore();
    }

    public void startProgress(final int progress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 100; i >= progress; i--) {
                    setProgress(i);
                    if (customRoundListener != null) {
                        customRoundListener.progressUpdate(i);
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void reStartProgress(final int progress) {
        final int st = this.progress;
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = st; i <= progress; i++) {
                    setProgress(i);
                    if (customRoundListener != null) {
                        customRoundListener.progressUpdate(i);
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        void progressUpdate(int progress);
    }
}
