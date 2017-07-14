package com.icleaner.junk.mycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.icleaner.junk.R;

/**
 * Created by Ivy on 2017/7/13.
 */

public class LineView extends View {
    private Paint paint;
    private Context context;
    private int width;
    private int height;

    public LineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        // 获取手机屏幕宽高
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
        Log.e("init", "===========" + width + "===" + height);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ContextCompat.getColor(context, R.color.A7));
        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.d3));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, getResources().getDimensionPixelSize(R.dimen.d123), width, getResources().getDimensionPixelSize(R.dimen.d123), paint);
    }
}
