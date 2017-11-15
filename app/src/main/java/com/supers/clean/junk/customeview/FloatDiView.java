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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.android.clean.util.Util;
import com.supers.clean.junk.R;

/**
 * Created by chengyuan on 16/8/12.
 */
public class FloatDiView extends View {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_LEFT = 1;
    public static final int STATE_RIGHT = 2;

    public int width;
    public int height;
    public int bitmap_width;
    public int bitmap_height;
    private Paint textPaint;
    private Paint firstPaint;
    private String text;
    private int type = 1;
    private int pratent;
    private Bitmap bitmap_normal;
    private Bitmap bitmap_left;
    private Bitmap bitmap_right;
    Context context;


    public FloatDiView(Context context) {
        this(context, null);
    }

    public FloatDiView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatDiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
        text = context.getString(R.string.float_huo);
    }

    /**
     * 确定该控件或子控件的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(metrics);
        Log.e("metrics", metrics.widthPixels + "==" + metrics.heightPixels);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        setMeasuredDimension(width, height);
        initPaints();

    }


    /**
     * 初始化画笔
     */
    private void initPaints() {
        firstPaint = new Paint();
        firstPaint.setColor(getResources().getColor(R.color.black_50));
        firstPaint.setAntiAlias(true);
//        firstPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // 文字
        textPaint = new Paint();
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.s13));
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        bitmap_width = getResources().getDimensionPixelOffset(R.dimen.d280);
        bitmap_height = getResources().getDimensionPixelOffset(R.dimen.d59);
//        textPaint.setFakeBoldText(true);
    }

    /**
     * 绘制控件的内容
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Rect rect = new Rect();
        rect.right = width;
        rect.left = 0;
        rect.bottom = height;
        rect.top = 0;
        canvas.drawRect(rect, firstPaint);
        firstPaint.setColor(getResources().getColor(R.color.white_40));
        canvas.drawCircle(width / 2, getResources().getDimensionPixelOffset(R.dimen.d141) + height, getResources().getDimensionPixelOffset(R.dimen.d200), firstPaint);
        float textWidth = textPaint.measureText(text);
        float x = width / 2 - textWidth / 2;
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = height - getResources().getDimensionPixelOffset(R.dimen.d33);
        canvas.drawText(text, x, y, textPaint);
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
