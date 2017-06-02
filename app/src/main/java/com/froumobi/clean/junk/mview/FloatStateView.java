package com.supers.clean.junk.mview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.android.clean.util.Util;
import com.supers.clean.junk.R;

/**
 * Created by chengyuan on 16/8/12.
 */
public class FloatStateView extends View {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_LEFT = 1;
    public static final int STATE_RIGHT = 2;

    public int width = Util.dp2px(30);
    public int height = Util.dp2px(30);
    private Paint textPaint;
    private Paint firstPaint;
    private String text = "50%";
    private int type = 1;
    private int pratent;
    private Bitmap bitmap_normal;
    private Bitmap bitmap_left;


    public FloatStateView(Context context) {
        this(context, null);
    }

    public FloatStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    /**
     * 确定该控件或子控件的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
        initPaints();

    }


    /**
     * 初始化画笔
     */
    private void initPaints() {
        firstPaint = new Paint();
        firstPaint.setColor(getResources().getColor(R.color.A1));
        firstPaint.setAntiAlias(true);
        firstPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 文字
        textPaint = new Paint();
        textPaint.setTextSize(Util.dp2px(12));
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        bitmap_normal = Bitmap.createScaledBitmap(src, width, height, true);
        bitmap_left = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.float_beijing), width, height, true);
    }

    /**
     * 绘制控件的内容
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        if (type == STATE_NORMAL) {
            canvas.drawBitmap(bitmap_normal, 0, 0, null);
        } else {
            canvas.drawBitmap(bitmap_left, 0, 0, null);

            if (pratent > 40 && pratent <= 80) {
                firstPaint.setColor(getResources().getColor(R.color.A4));
            } else if (pratent > 80) {
                firstPaint.setColor(getResources().getColor(R.color.A2));
            } else {
                firstPaint.setColor(getResources().getColor(R.color.A1));
            }
            float textWidth = textPaint.measureText(text);
            float x = width / 2 - textWidth / 2;
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent) / 2;
            float y = dy + height / 2;
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public void setDragState(int type) {
        this.type = type;
        invalidate();
    }

    public void upDate(int pratent) {
        this.pratent = pratent;
        this.text = pratent + "";
        postInvalidate();
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
                    postInvalidate();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
