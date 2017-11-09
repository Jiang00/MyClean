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
public class FloatHuoView extends View {


    public int width;
    public int height;
    private Paint firstPaint;
    private Bitmap bitmap_normal;


    public FloatHuoView(Context context) {
        this(context, null);
    }

    public FloatHuoView(Context context, int width, int height) {
        this(context, null);
        this.width = width;
        this.height = height;
    }

    public FloatHuoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatHuoView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.float_huo);
        bitmap_normal = Bitmap.createScaledBitmap(src, getResources().getDimensionPixelOffset(R.dimen.d61),
                getResources().getDimensionPixelOffset(R.dimen.d100), true);
    }

    /**
     * 绘制控件的内容
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmap_normal, 0, 0, null);
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
