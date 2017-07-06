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


public class VirtuaRingView extends View {
    private Context context;
    private int width;
    private int height;
    private Paint mPaintCircle;
    public static final int NEED_INVALIDATE = 0X23;
    private int num;
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

    public VirtuaRingView(Context context) {
        super(context);
    }

    public VirtuaRingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        mPaintCircle = new Paint();
        mPaintCircle.setColor(ContextCompat.getColor(context, R.color.A6));
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

        int circleRadius = dip2px(context, 105);
        mPaintCircle.setColor(ContextCompat.getColor(context, R.color.B3));
        //依次旋转画布，画出每个刻度
        for (int i = 1; i <= 60; i++) {
            canvas.save();//保存当前画布
            canvas.rotate(360 / 60 * i, width / 2, dip2px(context, 138));
            //左起：起始位置x坐标，起始位置y坐标，终止位置x坐标，终止位置y坐标，画笔(一个Paint对象)
            canvas.drawLine(width / 2, dip2px(context, 138) - circleRadius, width / 2, dip2px(context, 138) - circleRadius + dip2px(context, 6), mPaintCircle);
            canvas.restore();//
        }
        // 设置颜色
        mPaintCircle.setColor(ContextCompat.getColor(context, R.color.A10));
        for (int i = 1; i <= num; i++) {
            canvas.save();//保存当前画布
            canvas.rotate(360 / 60 * i, width / 2, dip2px(context, 138));
            //左起：起始位置x坐标，起始位置y坐标，终止位置x坐标，终止位置y坐标，画笔(一个Paint对象)
            canvas.drawLine(width / 2, dip2px(context, 138) - circleRadius, width / 2, dip2px(context, 138) - circleRadius + dip2px(context, 6), mPaintCircle);
            canvas.restore();//
        }

    }

    public void setNum(int num) {
        this.num = num;
        invalidate();//刷新
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
