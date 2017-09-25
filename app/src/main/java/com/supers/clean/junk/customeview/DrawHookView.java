package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.android.clean.util.Util;
import com.supers.clean.junk.R;

/**
 */

public class DrawHookView extends View {
    //绘制圆弧的进度值
    private int progress = 0;
    //线1的x轴
    private int line1_x = 0;
    //线1的y轴
    private int line1_y = 0;
    //线2的x轴
    private int line2_x = 0;
    //线2的y轴
    private int line2_y = 0;
    private int size;
    private boolean stop;

    private int move_distance = Util.dp2px(2);

    DrawHookListener drawHookListener;
    private Paint paint;

    public DrawHookView(Context context) {
        this(context, null);

    }

    public DrawHookView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawHookView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }

    private void init() {
        /**
         * 绘制圆弧
         */
        paint = new Paint();
        //设置画笔颜色
//        paint.setColor(getResources().getColor(R.color.white_40));
        //设置圆弧的宽度
        paint.setStrokeWidth(Util.dp2px(10));
        //设置圆弧为空心
        paint.setStyle(Paint.Style.STROKE);
        //消除锯齿
        paint.setAntiAlias(true);
//        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(getResources().getColor(R.color.white_100));
    }

    //绘制
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
        //获取圆心的x坐标
        int center = size / 2;
        int center1 = center - size / 5;
        //圆弧半径
        int radius = size / 2;

        //定义的圆弧的形状和大小的界限
//        RectF rectF = new RectF(0 + Util.dp2px(5), 0 + Util.dp2px(5), size - Util.dp2px(5), size - Util.dp2px(5));

        //根据进度画圆弧
//        canvas.drawArc(rectF, 235, -360 * progress / 100, false, paint);
        /**
         * 绘制对勾
         */
        //先等圆弧画完，才话对勾
        if (progress == 0) {
            return;
        }
        if (line1_x < radius / 3) {
            line1_x += move_distance;
            line1_y += move_distance;
            line2_x = line1_x;
            line2_y = line1_y;
            //画第一根线
            canvas.drawLine(center1, center, center1 + line1_x, center + line1_y, paint);
        } else if (line1_x >= radius / 3 && line2_x <= radius * 5 / 6) {
            line2_x += move_distance;
            line2_y -= move_distance;
            //画第一根线
            canvas.drawLine(center1, center, center1 + line1_x, center + line1_y, paint);
            //画第二根线
            canvas.drawLine(center1 + line1_x - Util.dp2px(6), center + line1_y, center1 + line2_x, center + line2_y, paint);
        } else {
            if (drawHookListener != null) {
                drawHookListener.duogouSc();
            }
            //画第一根线
            canvas.drawLine(center1, center, center1 + line1_x, center + line1_y, paint);
            //画第二根线
            canvas.drawLine(center1 + line1_x - Util.dp2px(6), center + line1_y, center1 + line2_x, center + line2_y, paint);
            stop = true;
        }
    }

    public void startProgress(final int progress) {
        final int st = this.progress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = st; i <= progress; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!stop) {
                        setProgress(i);
                    } else {
                        return;
                    }
                }
                if (drawHookListener != null) {
                    drawHookListener.duogouSc();
                }
            }
        }).start();

    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setListener(DrawHookListener drawHookListener) {
        this.drawHookListener = drawHookListener;
    }

    public interface DrawHookListener {
        void duogouSc();
    }
}

