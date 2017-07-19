package com.easy.junk.easycustomview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.easy.junk.R;


/**
 * Created by  on 2016/12/2.
 */

public class ICleanerWaterView extends View {
    public int width;
    public int height;
    private Paint firstPaint;
    private Paint beijingPaint;
    private String text = "50%";
    private int pratent = 0;
    private Bitmap bitmap;
    private RectF rectF;
    Context context;
//    private Paint textPaint;
//    private int textSize;

    public ICleanerWaterView(Context context) {
        this(context, null);
    }

    public ICleanerWaterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ICleanerWaterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
        beijingPaint.setStyle(Paint.Style.FILL);
        beijingPaint.setColor(ContextCompat.getColor(context, R.color.A1));
        firstPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_beijing);//main_beijing  battery_m
        // 文字
//        textPaint = new Paint();
//        textSize = context.getResources().getDimensionPixelSize(R.dimen.s10);
//        textPaint.setTextSize(textSize);
//        textPaint.setColor(ContextCompat.getColor(mContext, R.color.white_100));
//        textPaint.setAntiAlias(true);
//        textPaint.setFakeBoldText(true);
        rectF = new RectF();
        rectF.left = 0;
        rectF.bottom = height;
        rectF.right = width;
        rectF.top = 0;
    }

    public void setTextSize(int size) {
//        textPaint.setTextSize(size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
        canvas.drawArc(rectF, 0, 360, false, beijingPaint);
        firstPaint.setColor(getResources().getColor(R.color.B5));
        canvas.drawPath(firstPath(), firstPaint);
        firstPaint.setColor(getResources().getColor(R.color.A10));
        canvas.drawPath(firstPath2(), firstPaint);

//        float textWidth = textPaint.measureText(text);
//        float x = width / 2 - textWidth / 2;
//        Paint.FontMetrics metrics = textPaint.getFontMetrics();
//        float dy = -(metrics.descent + metrics.ascent) / 2;
//        float y = dy + height / 2;
//        canvas.drawText(text, x, y, textPaint);
        canvas.drawBitmap(bitmap, null, rectF, beijingPaint);
        sin_offset += sin_offset_increment_value;
        sin_offset2 += sin_offset_increment_value2;
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


    UpThread upThread;
    boolean isup = false;

    public void setPratent(int pratent) {
        if (!isup) {
            isup = true;
            upThread = new UpThread(pratent);
            upThread.start();
        }

    }

    public void upDate(int pratent) {
        this.pratent = pratent;
        this.text = pratent + "%";
//        postInvalidate();
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
