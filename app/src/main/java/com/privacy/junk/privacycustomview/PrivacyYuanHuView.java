package com.privacy.junk.privacycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.privacy.junk.R;

/**
 */

public class PrivacyYuanHuView extends View {
    Paint paint;
    Paint paintCrile;
    RectF rectF;
    Context context;
    float dushu;
    Canvas canvas;
    int startDuShu;

    public PrivacyYuanHuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d4));
        paintCrile = new Paint();
        paintCrile.setStyle(Paint.Style.STROKE);
        paintCrile.setAntiAlias(true);
        paintCrile.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d4));
        paintCrile.setColor(0x1a000000);

        rectF = new RectF(0, 0, getResources().getDimensionPixelSize(R.dimen.d52), getResources().getDimensionPixelSize(R.dimen.d52));
//        rectF.offset(getResources().getDimensionPixelSize(R.dimen.d1), getResources().getDimensionPixelSize(R.dimen.d1));//左上位置
        rectF.left = getResources().getDimensionPixelSize(R.dimen.d2);
        rectF.top = getResources().getDimensionPixelSize(R.dimen.d2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawCircle(getResources().getDimensionPixelSize(R.dimen.d27), getResources().getDimensionPixelSize(R.dimen.d27),
                getResources().getDimensionPixelSize(R.dimen.d25), paintCrile);
        canvas.drawArc(rectF, startDuShu, dushu, false, paint);
    }

    public void setDuShu(float dushu, int idColor, int startDuShu) {
        this.dushu = dushu;
        paint.setColor(idColor);
        this.startDuShu = startDuShu;
        postInvalidate();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        canvas = null;
    }
}
