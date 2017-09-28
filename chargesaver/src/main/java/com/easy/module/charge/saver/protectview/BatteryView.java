package com.easy.module.charge.saver.protectview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.easy.module.charge.saver.R;

/**
 * 电池动画
 */

public class BatteryView extends View {
    Paint paint;
    Paint paint_text;
    Context context;
    int width, height;
    int level;
    int round;

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.charg_1));
        paint.setAntiAlias(true);
        paint_text = new Paint();
        paint_text.setColor(ContextCompat.getColor(context, R.color.white_100));
        paint_text.setAntiAlias(true);
        paint_text.setTextSize(getResources().getDimensionPixelOffset(R.dimen.s14));
        paint.setStyle(Paint.Style.FILL);
        round = getResources().getDimensionPixelOffset(R.dimen.d5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.right = width;
        rectF.bottom = height;
        rectF.top = height - height * level / 100;
        canvas.drawRoundRect(rectF, round, round, paint);
        float textWidth = paint_text.measureText(level + "%");
        float x = width / 2 - textWidth / 2;
        Paint.FontMetrics metrics = paint_text.getFontMetrics();
        float dy = -(metrics.descent + metrics.ascent) / 2;
        float y = dy + height / 2;
        canvas.drawText(level + "%", x, y, paint_text);
    }


    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
