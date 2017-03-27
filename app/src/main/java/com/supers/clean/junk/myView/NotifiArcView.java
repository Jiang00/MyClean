package com.supers.clean.junk.myView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;


public class NotifiArcView extends ProgressBar {

    private Paint percentPaint;
    private Paint cirPaint;

    private String text = "50%";
    private Paint textPaint;
    private int textSize = CommonUtil.dp2px(8);
    Context context;
    private int percent = 0;

    public NotifiArcView(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public NotifiArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public NotifiArcView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // TODO Auto-generated method stub
        cirPaint = new Paint();
        cirPaint.setAntiAlias(true);

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
        canvas.save();
        canvas.translate(0, pointY);
        canvas.rotate(135, pointX, 0);
        cirPaint.setStrokeWidth(dp2px(1));
        cirPaint.setStyle(Paint.Style.STROKE);
        cirPaint.setColor(context.getResources().getColor(R.color.white_40));
        RectF oval = new RectF(0 + dp2px(1), -pointX + dp2px(1), pointX
                * 2 - dp2px(1), pointX - dp2px(1));
        canvas.drawArc(oval, 0, 270, false, cirPaint);

        cirPaint.setStrokeWidth(dp2px(1));
        cirPaint.setColor(context.getResources().getColor(R.color.white_100));
        canvas.drawArc(oval, 0, 270 * percent / 100, false, cirPaint);
        canvas.restore();
        float textWidth = textPaint.measureText(text);
        float x = width / 2 - textWidth / 2;
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = dy + height / 2;
        canvas.drawText(text, x, y, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        setMeasuredDimension(d, d);
    }

    public void start(final int percent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 100; i >= percent; i--) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setPercent(i);
                }
            }
        }).start();
    }

    public void startUp(final int percent) {
        final int st = this.percent;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = st; i <= percent; i++) {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setPercent(i);

                    if (listener != null) {
                        listener.setPerc(i);
                    }
                }
                if (listener != null) {
                    listener.setSucc();
                }
            }
        });
        thread.start();
    }

    @Override
    public void setProgress(int progress) {
        setPercent(progress);
    }

    public void setPercent(int percent) {
        // TODO Auto-generated method stub
        this.percent = percent;
        this.text = percent + "%";
        postInvalidate();
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