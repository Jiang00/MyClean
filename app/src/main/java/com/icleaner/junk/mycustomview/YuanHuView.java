package com.icleaner.junk.mycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.icleaner.junk.R;

/**
 * Created by Ivy on 2017/7/17.
 */

public class YuanHuView extends View {
    Paint paint;
    RectF rectF;
    Context context;
    float dushu;
    float dushuMax = 269;

    public YuanHuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.A7));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d20));

        rectF = new RectF(getResources().getDimensionPixelSize(R.dimen.d17), getResources().getDimensionPixelSize(R.dimen.d17),
                getResources().getDimensionPixelSize(R.dimen.d217), getResources().getDimensionPixelSize(R.dimen.d217));
//        rectF.offset(getResources().getDimensionPixelSize(R.dimen.d4), getResources().getDimensionPixelSize(R.dimen.d4));//左上位置
        rectF.left = getResources().getDimensionPixelSize(R.dimen.d19);
        rectF.top = getResources().getDimensionPixelSize(R.dimen.d19);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rectF, 136, dushu, false, paint);
        if (dushu < dushuMax) {
            handler.handleMessage(null);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dushu += 1;
            if (dushu > dushuMax) {
                dushu = dushuMax;
                Log.e("file","====2===");
            }
            invalidate();
        }
    };

    public void startYuanHuView(float dushu) {
        Log.e("file","====1===");
        dushuMax = dushu - 1;
        this.dushu = 0;
        invalidate();
    }
}
