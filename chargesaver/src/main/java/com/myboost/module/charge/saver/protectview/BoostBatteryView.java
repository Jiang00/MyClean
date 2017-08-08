package com.myboost.module.charge.saver.protectview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.myboost.module.charge.saver.R;

public class BoostBatteryView extends View {
    Paint paint;
    Context context;
    boolean isStop = false;
    int battery;
    int w = (int) getResources().getDimension(R.dimen.d167);

    public BoostBatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xff49e7fe);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画矩形(RectF)
        RectF rectf = new RectF(0, 0, w * battery / 100, getResources().getDimension(R.dimen.d84));
//        canvas.drawRect(rectf, paint);
        canvas.drawRoundRect(rectf, getResources().getDimension(R.dimen.d9), getResources().getDimension(R.dimen.d9), paint);//第二个参数是x半径，第三个参数是y半径
    }

    public void setDuShu(int battery) {
        this.battery = battery;
        postInvalidate();
    }

    public void start(final float battery) {
        setDuShu((int) battery);
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
