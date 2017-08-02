package com.privacy.junk.privacycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.privacy.junk.R;

public class MainYuanHuView extends View {
    Paint paint;
    RectF rectF, rectF1;
    Context context;
    float dushu;
    Canvas canvas;
    int startDuShu;

    public MainYuanHuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d8));
        paint.setColor(ContextCompat.getColor(context, R.color.A5));

        rectF = new RectF(0, 0, getResources().getDimensionPixelSize(R.dimen.d216), getResources().getDimensionPixelSize(R.dimen.d216));
        rectF.left = getResources().getDimensionPixelSize(R.dimen.d4);
        rectF.top = getResources().getDimensionPixelSize(R.dimen.d4);

        rectF1 = new RectF(0, 0,
                getResources().getDimensionPixelSize(R.dimen.d192), getResources().getDimensionPixelSize(R.dimen.d192));
        rectF1.left = getResources().getDimensionPixelSize(R.dimen.d28);
        rectF1.top = getResources().getDimensionPixelSize(R.dimen.d28);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        canvas.drawArc(rectF, 150, 100, false, paint);
        canvas.drawArc(rectF1, 150, 190, false, paint);
        paint.setColor(0x8affffff);
        canvas.drawArc(rectF, 150, 240, false, paint);
        canvas.drawArc(rectF1, 150, 240, false, paint);
    }

    public void setDuShu(float dushu, int startDuShu) {
        this.dushu = dushu;
        this.startDuShu = startDuShu;
        postInvalidate();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        canvas = null;
    }
}
