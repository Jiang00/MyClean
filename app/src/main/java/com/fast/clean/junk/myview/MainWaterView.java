package com.fast.clean.junk.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fast.clean.junk.R;


/**
 * Created by Ivy on 2016/12/2.
 */

public class MainWaterView extends View {
    public int width;
    public int height;
    private Paint firstPaint;
    private Paint beijingPaint;
    private String text = "50%";
    private int pratent = 0;
    private Bitmap bitmap;
    private RectF rectF;

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
        setMeasuredDimension(d, d);
        initPaints();

    }

    private void initPaints() {
        firstPaint = new Paint();
        firstPaint.setAntiAlias(true);
//        firstPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        beijingPaint = new Paint();
        beijingPaint.setAntiAlias(true);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_beijing);
        rectF = new RectF();
        rectF.left = 0;
        rectF.bottom = height;
        rectF.right = width;
        rectF.top = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//裁剪成圆形区域
        Path path = new Path();
        canvas.save();
        path.reset();
        canvas.clipPath(path);
        path.addCircle(width / 2, height / 2, width / 2, Path.Direction.CCW);
        canvas.clipPath(path, Region.Op.REPLACE);
        firstPaint.setColor(getResources().getColor(R.color.A7));
        canvas.drawPath(firstPath(), firstPaint);
        firstPaint.setColor(getResources().getColor(R.color.A6));
        canvas.drawPath(firstPath2(), firstPaint);
        canvas.drawBitmap(bitmap, null, rectF, beijingPaint);
        sin_offset += sin_offset_increment_value;
        sin_offset2 += sin_offset_increment_value2;
        canvas.restore();
    }

    private float sin_offset_increment_value = 0.3f;//初项递增值，表示波浪的快慢
    private float sin_offset_increment_value2 = 0.36f;//初项递增值，表示波浪的快慢
    private Path firstPath = new Path();
    private int sin_amplitude = 35;//振幅 ，10到100之间
    private float sin_cycle = 0.01f;//周期 ， 0.01f左右
    float sin_offset = 0.0f;//初项，偏移量
    float sin_offset2 = 5.0f;//初项，偏移量

    private Path firstPath() {
        firstPath.reset();
        firstPath.moveTo(0, height);// 移动到左下角的点
        for (float x = 0; x <= width; x++) {
            if (pratent != 100) {
                float y = (float) (sin_amplitude * Math.sin(sin_cycle * x + sin_offset)) + height * (100 - pratent) / 100;
                firstPath.lineTo(x, y);
            } else {
                firstPath.lineTo(x, 0);
            }
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
            if (pratent != 100) {
                float y = (float) (sin_amplitude * Math.sin(sin_cycle * x + sin_offset2)) + height * (100 - pratent) / 100;
                firstPath.lineTo(x, y);
            } else {
                firstPath.lineTo(x, 0);
            }
        }
        firstPath.lineTo(width, height);
        firstPath.lineTo(0, height);
        firstPath.close();
        return firstPath;
    }

    public void upDate(int pratent) {
        this.pratent = pratent;
//        postInvalidate();
    }

    UpThread upThread;
    boolean isup = false;

    public void setPratent(int pratent) {
        if (!isup) {
            isup = true;
            upThread = new UpThread(pratent);
            upThread.start();
        }

    }

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
            for (int i = 0; i < 100; i++) {
                upDate(i);
                if (listener != null) {
                    listener.update(i);
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            for (int i = 100; i >= p; i--) {
                upDate(i);
                if (listener != null) {
                    listener.update(i);
                }
                try {
                    Thread.sleep(10);
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

        void update(int jindu);
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
