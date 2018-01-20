package com.eifmobi.clean.junk.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.eifmobi.clean.junk.R;

/**
 */

public class LoadingTime extends View {
    private Paint circlePoint;
    private int progress;
    private boolean isRotate;
    private Context context;
    Bitmap bitmap;
    int text = 6;
    float lineWidth = getResources().getDimension(R.dimen.d3);
    int size;
    private CustomRoundListener customRoundListener;
    private boolean stop;

    public LoadingTime(Context context) {
        this(context, null);
    }

    public LoadingTime(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingTime(Context context, AttributeSet attrs, int defStyleAttr) {
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
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint.setAntiAlias(true);
        circlePoint.setStyle(Paint.Style.STROKE);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ad_delete);
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
        rect.left = 0 + lineWidth + 1;
        rect.right = size - lineWidth - 1;
        rect.top = 0 + lineWidth + 1;
        rect.bottom = size - lineWidth - 1;
        canvas.save();
        canvas.rotate(-90, size / 2, size / 2);
//        canvas.drawBitmap(bitmap, 0, 0, circlePoint);
        canvas.drawBitmap(bitmap, null, rect, circlePoint);
        canvas.drawArc(rect, 0, 360 * (progress) / 100, false, circlePoint);
        canvas.restore();
    }

    public void startProgress() {
        stop = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    if (stop) {
                        setProgress(100);
                        if (customRoundListener != null) {
                            customRoundListener.progressUpdate();
                        }
                        return;
                    }
                    try {
                        Thread.sleep(60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (stop) {
                        setProgress(100);
                        if (customRoundListener != null) {
                            customRoundListener.progressUpdate();
                        }
                        return;
                    }
                    setProgress(i);
                }
                if (customRoundListener != null) {
                    customRoundListener.progressUpdate();
                }
            }
        }).start();

    }

    public void cancle() {
        stop = true;
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
