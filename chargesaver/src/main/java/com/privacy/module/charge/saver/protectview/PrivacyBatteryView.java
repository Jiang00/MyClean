package com.privacy.module.charge.saver.protectview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.privacy.module.charge.saver.R;

public class PrivacyBatteryView extends View {
    Paint paint;
    Context context;
    boolean isStop = false;
    int battery;
    Canvas canvas;

    public PrivacyBatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.A1));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    int w = (int) getResources().getDimension(R.dimen.d112);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        // 画矩形(RectF)
        RectF rectf = new RectF(0, 0, w * battery / 100, getResources().getDimension(R.dimen.d54));
        canvas.drawRect(rectf, paint);
    }

    public void setDuShu(int battery) {
        this.battery = battery;
        postInvalidate();
    }

    public void start(final float battery) {
        setDuShu((int) battery);
//        this.battery = 0;

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.battery = 0;
    }

    public void stop() {
        isStop = true;
    }
}
