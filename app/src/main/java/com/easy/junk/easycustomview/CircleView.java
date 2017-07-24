package com.easy.junk.easycustomview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.easy.junk.R;

public class CircleView extends View {
    private Context context;
    private int width;
    private int height;
    private Paint mPaintCircle;
    public static final int NEED_INVALIDATE = 0X23;

    //每隔一秒，在handler中调用一次重新绘制方法
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case NEED_INVALIDATE:
                    invalidate();//告诉UI主线程重新绘制
                    handler.sendEmptyMessageDelayed(NEED_INVALIDATE, 1000);
                    break;
                default:
                    break;
            }
        }
    };

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);//设置是否抗锯齿
        mPaintCircle.setStyle(Paint.Style.STROKE);//设置绘制风格
        mPaintCircle.setStrokeWidth(dip2px(context, getResources().getDimension(R.dimen.d2)));//设置线宽
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaintCircle.setColor(ContextCompat.getColor(context, R.color.B3));

        canvas.drawCircle(width / 2, getResources().getDimensionPixelSize(R.dimen.d138), getResources().getDimensionPixelSize(R.dimen.d130), mPaintCircle);

    }

    boolean isStop;

    public void start(final float dushu) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0, j = 269; i < 270 && j > dushu; ) {
                    if (isStop) {
                        return;
                    }
                    if (i == 269) {
                        j--;
                    } else {
                        i++;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStop = true;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
