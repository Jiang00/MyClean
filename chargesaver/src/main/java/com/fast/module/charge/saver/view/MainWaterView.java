package com.fast.module.charge.saver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.fast.module.charge.saver.R;


/**
 * Created by Ivy on 2016/12/2.
 */

public class MainWaterView extends View {
    public int width;
    public int height;
    private Paint firstPaint;
    private Paint beijingPaint;
    private String text = "50%";
    private int pratent = 50;

    public MainWaterView(Context context) {
        this(context, null);
    }

    public MainWaterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainWaterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        setMeasuredDimension(width, height);
        initPaints();

    }

    private void initPaints() {
        firstPaint = new Paint();
        firstPaint.setAntiAlias(true);
//        firstPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        beijingPaint = new Paint();
//        beijingPaint.setColor(Color.WHITE);
//        beijingPaint.setAntiAlias(true);
//        beijingPaint.setFilterBitmap(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawCircle(width / 2, height / 2, width / 2, beijingPaint);
//        if (pratent > 40 && pratent <= 80) {
//            firstPaint.setColor(getResources().getColor(R.color.clear_y_2));
//        } else if (pratent > 80) {
//            firstPaint.setColor(getResources().getColor(R.color.clear_y_3));
//        } else {
//            firstPaint.setColor(getResources().getColor(R.color.clear_y_1));
//        }
        firstPaint.setColor(getResources().getColor(R.color.A3_50));
        canvas.drawPath(firstPath(), firstPaint);
        firstPaint.setColor(getResources().getColor(R.color.A3));
        canvas.drawPath(firstPath2(), firstPaint);
        sin_offset += sin_offset_increment_value;
        sin_offset2 += sin_offset_increment_value2;
    }

    private float sin_offset_increment_value = 0.18f;//初项递增值，表示波浪的快慢
    private float sin_offset_increment_value2 = 0.20f;//初项递增值，表示波浪的快慢
    private Path firstPath = new Path();
    private int sin_amplitude = 10;//振幅 ，10到100之间
    private float sin_cycle = 0.02f;//周期 ， 0.01f左右
    float sin_offset = 0.0f;//初项，偏移量
    float sin_offset2 = 5.0f;//初项，偏移量

    private Path firstPath() {
        firstPath.reset();
        firstPath.moveTo(0, height);// 移动到左下角的点
        for (float x = 0; x <= width; x++) {
            float y = (float) (sin_amplitude * Math.sin(sin_cycle * x + sin_offset)) + height * (100 - pratent) / 100;
            firstPath.lineTo(x, y);
        }
        firstPath.lineTo(width, height);
        firstPath.lineTo(0, height);
        firstPath.close();
        return firstPath;
    }

    private Path firstPath2() {
        firstPath.reset();
        firstPath.moveTo(0, height);// 移动到左下角的点
        for (float x = 0; x <= width; x++) {
            float y = (float) (sin_amplitude * Math.sin(sin_cycle * x + sin_offset2)) + height * (100 - pratent) / 100;
            firstPath.lineTo(x, y);
        }
        firstPath.lineTo(width, height);
        firstPath.lineTo(0, height);
        firstPath.close();
        return firstPath;
    }

    public void upDate(int pratent) {
        this.pratent = pratent;
        postInvalidate();
    }

    UpThread upThread;
    boolean isup = false;


    RunThread runThread;
    boolean isStart = false;

    public void start() {
        if (!isStart) {
            isStart = true;
            runThread = new RunThread();
            runThread.start();
        }
    }

    public void stop() {
        isStart = false;
    }

    public class RunThread extends Thread {
        @Override
        public void run() {
            while (isStart) {
                try {
                    Thread.sleep(100);
                    postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class UpThread extends Thread {
        int p;

        public UpThread(int p) {
            this.p = p;
        }

        @Override
        public void run() {

            for (int i = 0; i <= p; i++) {
                upDate(i);
                try {
                    Thread.sleep(10);
                    postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (listener != null) {
                listener.success();
            }
            isup = false;

        }
    }

    FloatWaterListener listener;

    public void setFloatWaterListener(FloatWaterListener listener) {
        this.listener = listener;
    }

    public interface FloatWaterListener {
        void success();
    }

    private void init() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
