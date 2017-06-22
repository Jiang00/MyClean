package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.supers.clean.junk.R;

/**
 */

public class MessageRoundViewCpu extends View {
    private Context context;
    private Paint circlePoint;
    private Paint backgPoint;
    private Paint textPaint;
    private String text = "50℃";
    float lineWidth = getResources().getDimensionPixelOffset(R.dimen.d3);
    int size;
    private int progress;
    private boolean isRotate;
    private CustomRoundListener customRoundListener;

    public MessageRoundViewCpu(Context context) {
        this(context, null);
    }

    public MessageRoundViewCpu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageRoundViewCpu(Context context, AttributeSet attrs, int defStyleAttr) {
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
        circlePoint.setStrokeCap(Paint.Cap.ROUND);
        circlePoint.setFilterBitmap(true);
        circlePoint.setAntiAlias(true);
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint.setStyle(Paint.Style.STROKE);
        backgPoint = new Paint();
        backgPoint.setAntiAlias(true);
        backgPoint.setStrokeWidth(lineWidth);
        backgPoint.setStrokeCap(Paint.Cap.ROUND);
        backgPoint.setStyle(Paint.Style.STROKE);
        backgPoint.setColor(context.getResources().getColor(R.color.C7));
        textPaint = new Paint();
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.s14));
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
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
        RectF rect = new RectF();
        rect.left = 0 + lineWidth / 2;
        rect.right = size - lineWidth / 2;
        rect.top = 0 + lineWidth / 2;
        rect.bottom = size - lineWidth / 2;

        canvas.drawArc(rect, 0, 360, false, backgPoint);
        canvas.save();
        canvas.rotate(90, size / 2, size / 2);
        if (progress > 60) {
            circlePoint.setColor(context.getResources().getColor(R.color.A2));
            textPaint.setColor(context.getResources().getColor(R.color.A2));
        } else {
            circlePoint.setColor(context.getResources().getColor(R.color.A1));
            textPaint.setColor(context.getResources().getColor(R.color.A1));
        }
        canvas.drawArc(rect, 0, progress * 360 / 100, false, circlePoint);
        canvas.restore();
        float textWidth = textPaint.measureText(text);
        float x = size / 2 - textWidth / 2;
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = dy + size / 2;
        canvas.drawText(text, x, y, textPaint);


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

    public void setProgress(int progress) {
        this.progress = progress;
        this.text = progress + "℃";
        postInvalidate();
    }

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;

    }

    public interface CustomRoundListener {
        void progressUpdate(int progress);
    }
}
