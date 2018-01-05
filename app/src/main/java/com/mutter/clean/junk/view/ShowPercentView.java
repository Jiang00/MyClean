package com.mutter.clean.junk.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mutter.clean.junk.R;


public class ShowPercentView extends View {

    private Paint percentPaint;
    private Paint cirPaint;

    private Paint textPaint;
    private int textSize = 30;
    Context context;
    private int percent = 100;
    boolean isRuning;
    boolean isDestory;

    private int percentLineWidth = (int) getResources().getDimension(R.dimen.d2);
    private int lineHeight = (int) getResources().getDimension(R.dimen.d8);
    private int line_size = 200;


    public ShowPercentView(Context context) {
        this(context, null);
    }

    public ShowPercentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowPercentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        硬件加速
//        setLayerType(LAYER_TYPE_SOFTWARE, null);
        this.context = context;
        init(attrs, defStyle);

    }

    private void init(AttributeSet attrs, int defStyle) {
        percentPaint = new Paint();
        percentPaint.setAntiAlias(true);
        cirPaint = new Paint();
        cirPaint.setAntiAlias(true);
        percentPaint.setStrokeWidth(percentLineWidth);
        percentPaint.setStyle(Paint.Style.FILL);
        percentPaint.setStrokeCap(Paint.Cap.ROUND);
        percentPaint.setColor(ContextCompat.getColor(context, R.color.A3));
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
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
        int size = percent * line_size / 100;
        for (int i = 0; i <= size; i++) {
            canvas.drawLine(0 + percentLineWidth, percentLineWidth / 2, lineHeight + percentLineWidth, percentLineWidth / 2, percentPaint);
            canvas.rotate(degrees, pointX, 0);
            if (listener != null) {
                listener.setPerc(percent);
            }
        }
        canvas.restore();
        if (isRuning) {
            postInvalidate();
        }

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


    public void start(final int percent) {
        if (isRuning) {
            return;
        }
        isRuning = true;
        postInvalidate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int s = 50;
                for (int i = 100; i >= percent; i--) {
                    s--;
                    if (s < 20)
                        s = 20;
                    if (isDestory) {
                        return;
                    }
                    try {
                        Thread.sleep(s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isDestory) {
                        return;
                    }
                    setPercent(i);
                }

                if (listener != null) {
                    listener.setSucc();
                }
                isRuning = false;
            }
        }).start();
    }

    public void startUp(final int percent, final boolean b) {
        if (isRuning) {
            return;
        }
        isRuning = true;
        final int st = this.percent;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = st; i <= percent; i++) {
                    if (isDestory) {
                        return;
                    }
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isDestory) {
                        return;
                    }
                    setPercent(i);
                }
                if (b) {
                    if (listener != null) {
                        listener.setSucc();
                    }
                }
                isRuning = false;
            }
        }).start();

    }

    public void stop() {
        isDestory = true;
    }

    public void setPercent(int percent) {
        // TODO Auto-generated method stub
        this.percent = percent;
    }

    PercentViewListener listener;

    public void setListener(PercentViewListener listener) {
        this.listener = listener;
    }

    public interface PercentViewListener {
        void setPerc(int percent);

        void setSucc();
    }

}