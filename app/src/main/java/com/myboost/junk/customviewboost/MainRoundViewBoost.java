package com.myboost.junk.privacycustomview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.myboost.junk.R;

/**
 */

public class PrivacyMainRoundView extends View {

    private Context context;
    private Paint circlePoint;
    float lineWidth = getResources().getDimension(R.dimen.d3);
    float padding = getResources().getDimension(R.dimen.d10);
    int size;
    private int progress;
    private boolean isRotate;
    private CustomRoundListener customRoundListener;
    private Bitmap bitmap;
    private Matrix mMatrix;

    public PrivacyMainRoundView(Context context) {
        this(context, null);
    }

    public PrivacyMainRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public PrivacyMainRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
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
        circlePoint.setStrokeCap(Paint.Cap.ROUND);
        circlePoint.setStyle(Paint.Style.STROKE);
        mMatrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dian);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePoint.setAntiAlias(true);
        circlePoint.setColor(context.getResources().getColor(R.color.B3));
        RectF rect = new RectF();
        rect.left = 0 + lineWidth / 2;
        rect.right = size - lineWidth / 2;
        rect.top = 0 + lineWidth / 2;
        rect.bottom = size - lineWidth / 2;
        canvas.save();
        canvas.rotate(-90, size / 2, size / 2);
        canvas.drawArc(rect, 0, progress * 360 / 100, false, circlePoint);
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
