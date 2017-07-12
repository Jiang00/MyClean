package com.icleaner.junk.mycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.icleaner.junk.R;

public class CustomRoundCpu extends View {

    private Context context;
    private Paint circlePoint;
    private Paint backgPoint;
    float lineWidth = getResources().getDimension(R.dimen.d4);
    int size;
    private int progress;
    private boolean isRotate;
    private CustomRoundListener customRoundListener;

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
        backgPoint.setColor(context.getResources().getColor(R.color.A8));
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
        circlePoint.setColor(context.getResources().getColor(R.color.B4));
        RectF rect = new RectF();//矩形
        rect.left = 0 + lineWidth / 2 + 1;
        rect.right = size - lineWidth / 2 - 1;
        rect.top = 0 + lineWidth / 2 + 1;
        rect.bottom = size - lineWidth / 2 - 1;
        RectF rectBack = new RectF();
        rectBack.left = 0 + 1;
        rectBack.right = size - 1;
        rectBack.top = 0 + 1;
        rectBack.bottom = size - 1;
        canvas.drawArc(rectBack, 0, 360, false, backgPoint);
        canvas.drawArc(rect, 0, 360, false, circlePoint);
        canvas.save();
        if (progress >= 0 && progress < 40) {
            circlePoint.setColor(context.getResources().getColor(R.color.A3));
        } else if (progress >= 40 && progress < 80) {
            circlePoint.setColor(context.getResources().getColor(R.color.A4));
        } else {
            circlePoint.setColor(context.getResources().getColor(R.color.A2));
        }
        if (isRotate) {
            canvas.rotate(61, size / 2, size / 2);//度数和位置（x,y）
            //绘制一段弧线，它表示由一对坐标、宽度和高度指定的椭圆部分。
            canvas.drawArc(rect, 0, progress * 310 / 100, false, circlePoint);
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
        postInvalidate();
    }

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;

    }

    public interface CustomRoundListener {
        void progressUpdate(int progress);
    }
}
