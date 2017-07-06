package com.vater.clean.junk.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.vater.clean.junk.R;

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
        mPaintCircle.setStrokeWidth(dip2px(context, 3));//设置线宽

//        handler.sendEmptyMessage(NEED_INVALIDATE);//向handler发送一个消息，让它开启重绘
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
        mPaintCircle.setColor(ContextCompat.getColor(context, R.color.B3_5));
        mPaintCircle.setStrokeWidth(dip2px(context, 1));//设置线宽
        //画出大圆
        canvas.drawCircle(width / 2, dip2px(context, 138), dip2px(context, 130), mPaintCircle);
        mPaintCircle.setColor(ContextCompat.getColor(context, R.color.B3_12));
        mPaintCircle.setStrokeWidth(dip2px(context, 2));//设置线宽
        //画出中心圆
        canvas.drawCircle(width / 2, dip2px(context, 138), dip2px(context, 118), mPaintCircle);

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
