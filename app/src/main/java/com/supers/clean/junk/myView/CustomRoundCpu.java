package com.supers.clean.junk.myView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.CommonUtil;

/**
 */

public class CustomRoundCpu extends View {

    private Context context;
    private Paint circlePoint;
    private Paint backgPoint;
    float lineWidth = CommonUtil.dp2px(4);
    int size;
    private int progress;
    private boolean isRotate;

    public CustomRoundCpu(Context context) {
        this(context, null);
    }

    public CustomRoundCpu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundCpu(Context context, AttributeSet attrs, int defStyleAttr) {
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
        backgPoint = new Paint();
        backgPoint.setAntiAlias(true);
        backgPoint.setStrokeWidth(lineWidth);
        backgPoint.setColor(context.getResources().getColor(R.color.app_color_first));
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
        circlePoint.setColor(context.getResources().getColor(R.color.main_circle_backg));
        RectF rect = new RectF();
        rect.left = 0 + lineWidth / 2;
        rect.right = size - lineWidth / 2;
        rect.top = 0 + lineWidth / 2;
        rect.bottom = size - lineWidth / 2;
        canvas.drawArc(rect, 0, 360, false, backgPoint);
        canvas.drawArc(rect, 0, 360, false, circlePoint);
        canvas.save();
        if (progress >= 0 && progress < 40) {
            circlePoint.setColor(context.getResources().getColor(R.color.main_circle_first));
        } else if (progress >= 40 && progress < 80) {
            circlePoint.setColor(context.getResources().getColor(R.color.main_circle_second));
        } else {
            circlePoint.setColor(context.getResources().getColor(R.color.main_circle_tired));
        }
        if (isRotate) {
            canvas.rotate(65, size / 2, size / 2);
            canvas.drawArc(rect, 0, progress * 3, false, circlePoint);
        } else {
            canvas.rotate(90, size / 2, size / 2);
            canvas.drawArc(rect, 0, progress * 360 / 100, false, circlePoint);
        }
        canvas.restore();
    }

    public void startProgress(boolean isRotate, final int progress) {
        this.isRotate = isRotate;
        final int st = this.progress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = st; i < progress; i++) {
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

}
