package com.supers.clean.junk.customeview;

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

    public int width;
    public int height;
    private Paint textPaint;
    private Paint firstPaint;
    private String text = "50%";
    private int type = 1;
    private int pratent;
    private Bitmap bitmap_normal;
    private Bitmap bitmap_left;
    private Bitmap bitmap_right;


    public FloatStateView(Context context) {
        this(context, null);
    }

    public FloatStateView(Context context, int width, int height) {
        this(context, null);
        this.width = width;
        this.height = height;
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
        width = getResources().getDimensionPixelOffset(R.dimen.d61);
        height = getResources().getDimensionPixelOffset(R.dimen.d100);
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
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.float_huo);
        bitmap_normal = Bitmap.createScaledBitmap(src, getResources().getDimensionPixelOffset(R.dimen.d61),
                getResources().getDimensionPixelOffset(R.dimen.d100), true);
        bitmap_left = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.float_beijing_left), getResources().getDimensionPixelOffset(R.dimen.d30),
                getResources().getDimensionPixelOffset(R.dimen.d30), true);
        bitmap_right = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.float_beijing_right), getResources().getDimensionPixelOffset(R.dimen.d30),
                getResources().getDimensionPixelOffset(R.dimen.d30), true);
    }

    /**
     * 绘制控件的内容
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (type == STATE_NORMAL) {
            canvas.drawBitmap(bitmap_normal, 0, 0, null);
        } else {
            if (type == STATE_LEFT) {
                canvas.drawBitmap(bitmap_left, 0, 0, null);
            } else {
                canvas.drawBitmap(bitmap_right, 0, 0, null);
            }

            if (pratent > 40 && pratent <= 80) {
                firstPaint.setColor(getResources().getColor(R.color.A4));
            } else if (pratent > 80) {
                firstPaint.setColor(getResources().getColor(R.color.A2));
            } else {
                firstPaint.setColor(getResources().getColor(R.color.A1));
            }
            float textWidth = textPaint.measureText(text);
            float x = bitmap_left.getWidth() / 2 - textWidth / 2;
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent) / 2;
            float y = dy + bitmap_left.getHeight() / 2;
            canvas.drawText(text, x, y, textPaint);
        }
    }

    public void setDragState(int type) {
        this.type = type;
//        if (type == STATE_NORMAL) {
//            width = getResources().getDimensionPixelOffset(R.dimen.d61);
//            height = getResources().getDimensionPixelOffset(R.dimen.d100);
//        } else {
//            width = getResources().getDimensionPixelOffset(R.dimen.d30);
//            height = getResources().getDimensionPixelOffset(R.dimen.d30);
//        }
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
