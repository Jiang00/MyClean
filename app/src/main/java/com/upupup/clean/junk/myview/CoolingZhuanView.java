package com.upupup.clean.junk.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.upupup.clean.junk.R;

/**
 */

public class CoolingZhuanView extends View {
    private Paint backgPoint;
    private int progress;
    private Context context;
    float lineWidth = getResources().getDimension(R.dimen.d5);
    int size;
    private CustomRoundListener customRoundListener;

    public CoolingZhuanView(Context context) {
        this(context, null);
    }

    public CoolingZhuanView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoolingZhuanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    public void setLayerType(int layerType, @Nullable Paint paint) {
        super.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
    }

    private void init() {
        backgPoint = new Paint();
        backgPoint.setAntiAlias(true);
        backgPoint.setStrokeWidth(lineWidth);
        backgPoint.setStrokeCap(Paint.Cap.ROUND);
        backgPoint.setStyle(Paint.Style.STROKE);
        backgPoint.setColor(context.getResources().getColor(R.color.white_20));
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
        backgPoint.setColor(context.getResources().getColor(R.color.white_20));
        canvas.drawArc(rect, 0, 360, false, backgPoint);
        backgPoint.setColor(context.getResources().getColor(R.color.white_100));
        canvas.drawArc(rect, -90, 360f * progress / 100, false, backgPoint);
        if (customRoundListener != null) {
            customRoundListener.progressUpdate(progress);
        }
    }

    public void startProgress(final int progress) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i <= progress; i++) {
                    if (onDestory) {
                        return;
                    }
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (onDestory) {
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

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;

    }

    boolean onDestory;

    public void onDestory() {
        onDestory = true;
    }

    public interface CustomRoundListener {
        void progressUpdate(int progress);

        void progressSuccess();
    }
}
