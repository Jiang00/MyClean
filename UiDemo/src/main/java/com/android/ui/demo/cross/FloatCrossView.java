package com.android.ui.demo.cross;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.android.ui.demo.util.Utils;

public class FloatCrossView extends View {
    public int width;
    public int height;
    private Bitmap bitmap_normal;

    public static String TAG = "FloatCrossView";

    public static final int STYLE_56 = 56;

    public static final int STYLE_40 = 40;

    public static final int STYLE_WRAP = -1;

    public FloatCrossView(Context context, Bitmap resBitmap, int style) {
        super(context);

        if (resBitmap == null) {
            Log.e(TAG, "FloatCrossView drawable = null");
            return;
        }

        if (style != STYLE_40 && style != STYLE_56 && style != STYLE_WRAP) {
            throw new RuntimeException("style must be STYLE_40 ,STYLE_56 or STYLE_WRAP");
        }

        if (style == STYLE_WRAP) {
            width = resBitmap.getWidth();
            height = resBitmap.getHeight();
        } else {
            height = width = Utils.dip2px(context,style);
        }

        init(resBitmap);
    }

    public FloatCrossView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatCrossView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmap_normal, 0, 0, null);
    }

    private void init(Bitmap resBitmap) {
//        Bitmap src = BitmapFactory.decodeResource(getResources(), resId);
        bitmap_normal = Bitmap.createScaledBitmap(resBitmap, width, height, true);
        try {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}