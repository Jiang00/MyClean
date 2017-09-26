package com.myboost.junk.customviewboost;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.myboost.junk.R;

public class Cooling2View extends View {

    Paint paint_r_wai, paint_r_nei, paint_l_wai, paint_l_nei, paint_l_nei_2;
    int width, height;
    Context context;
    Handler handler;
    int praent;

    public Cooling2View(Context context) {
        this(context, null);
    }

    public Cooling2View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cooling2View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);

    }

    private void init() {
        handler = new Handler();
        paint_r_wai = new Paint();
        paint_r_wai.setAntiAlias(true);
        paint_r_wai.setColor(ContextCompat.getColor(context, R.color.white_100));
        paint_r_nei = new Paint();
        paint_r_nei.setAntiAlias(true);
        paint_r_nei.setColor(ContextCompat.getColor(context, R.color.A22));
        paint_l_wai = new Paint();
        paint_l_wai.setStrokeCap(Paint.Cap.ROUND);
        paint_l_wai.setAntiAlias(true);
        paint_l_wai.setColor(ContextCompat.getColor(context, R.color.white_100));
        paint_l_wai.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.d44));
        paint_l_nei = new Paint();
        paint_l_nei.setStrokeCap(Paint.Cap.ROUND);
        paint_l_nei.setAntiAlias(true);
        paint_l_nei.setColor(ContextCompat.getColor(context, R.color.A1));
        paint_l_nei.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.d16));
        paint_l_nei_2 = new Paint();
        paint_l_nei_2.setStrokeCap(Paint.Cap.ROUND);
        paint_l_nei_2.setAntiAlias(true);
        paint_l_nei_2.setColor(ContextCompat.getColor(context, R.color.A22));
        paint_l_nei_2.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.d16));

        cricle_n = getResources().getDimensionPixelOffset(R.dimen.d22);
    }

    int cricle_n;
    boolean isRun;

    public void pause() {
        isRun = false;
    }

    public void reStart() {
        isRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    praent = i;
                    postInvalidate();
                    if (!isRun) {
                        return;
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!isRun) {
                        return;
                    }
                }

            }
        }).start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(width / 2, height - width / 2, width / 2, width / 2, paint_l_wai);
        canvas.drawCircle(width / 2, height - width / 2, width / 2, paint_r_wai);
        canvas.drawLine(width / 2, height - width / 2, width / 2, width / 2, paint_l_nei);
        canvas.drawLine(width / 2, height - width / 2, width / 2, width / 2 + width * praent / 200, paint_l_nei_2);
        canvas.drawCircle(width / 2, height - width / 2, cricle_n, paint_r_nei);
    }


}