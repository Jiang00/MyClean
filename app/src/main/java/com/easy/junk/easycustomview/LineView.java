package com.easy.junk.easycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.easy.junk.R;

/**
 * Created by Ivy on 2017/7/13.
 */

public class LineView extends View {
    private Paint paint;
    private Context context;
    private int width;
    private int widthMax;
    private int height;

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        // 获取手机屏幕宽高
//        width = context.getResources().getDisplayMetrics().widthPixels;
//        height = context.getResources().getDisplayMetrics().heightPixels;
//        Log.e("init", "===========" + width + "===" + height);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(context, R.color.A12));
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d33));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 0, width, 0, paint);
        Log.e("init", "===========" + width);
        if (width < widthMax) {
            handler.handleMessage(null);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            width += 1;
            if (width > widthMax) {
                width = widthMax;
            }
            invalidate();
        }
    };

    public void startDrawLine(double d) {
        widthMax = (int) d;
        invalidate();
    }
}
