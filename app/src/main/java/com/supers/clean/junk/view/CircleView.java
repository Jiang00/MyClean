package com.supers.clean.junk.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Ivy on 2017/6/13.
 */

public class CircleView extends View {
    Context context;
    // 创建画笔工具等信息
    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();//创建画笔
        paint.setColor(Color.RED);
        paint.setTextSize(100);
        paint.setStyle(Paint.Style.STROKE);//设置为空心
        paint.setStrokeWidth(10);//设置空心边框的宽度，没有单位
        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                invalidate();
            }
        });
    }

    Paint paint;
    int i;

    @Override
    protected void onDraw(Canvas canvas) {//canvas就是画布
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        //绘制圆形，x,y,r
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 42,
                paint);
//        paint.setColor(Color.GREEN);
//        canvas.drawText(i + "", getWidth() / 2 - 25, getHeight() / 2 + 25,
//                paint);
//        i++;
    }
}
