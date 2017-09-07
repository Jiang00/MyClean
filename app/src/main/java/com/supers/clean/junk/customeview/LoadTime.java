package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.supers.clean.junk.R;


/**
 */

public class LoadTime extends View {
    private Paint circlePoint;
    private Paint circlePoint_di;
    private int progress;
    private boolean isRotate;
    private Context context;
    Bitmap bitmap;
    int text = 6;
    float lineWidth = getResources().getDimension(R.dimen.d2);
    int size;
    private CustomRoundListener customRoundListener;
    private boolean stop;
    private DashPathEffect dashPathEffect;
    Path path;
    Path path1;
    private RectF mTableRectF;
    private RectF rect;

    public LoadTime(Context context) {
        this(context, null);
    }

    public LoadTime(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadTime(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    @Override
    public void setLayerType(int layerType, @Nullable Paint paint) {
        super.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
    }

    private void init() {
        circlePoint = new Paint();
        circlePoint_di = new Paint();
        circlePoint_di.setColor(ContextCompat.getColor(context, R.color.black_20));
        circlePoint.setColor(ContextCompat.getColor(context, R.color.white_100));
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint_di.setStrokeWidth(lineWidth);
        circlePoint.setAntiAlias(true);
        circlePoint_di.setAntiAlias(true);
        circlePoint.setStyle(Paint.Style.STROKE);
        circlePoint_di.setStyle(Paint.Style.STROKE);
        circlePoint.setDither(true);
        circlePoint_di.setDither(true);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ad_delete);
        //计算路径的长度

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        size = d;
        setMeasuredDimension(d, d);
        path = new Path();
        path1 = new Path();
        rect = new RectF();
        rect.left = 0 + lineWidth + 1;
        rect.right = size - lineWidth - 1;
        rect.top = 0 + lineWidth + 1;
        rect.bottom = size - lineWidth - 1;
        path1.addArc(rect, 0, 360);
        PathMeasure pathMeasure = new PathMeasure(path1, false);
        float length = pathMeasure.getLength();
        float step = length / 12;
        dashPathEffect = new DashPathEffect(new float[]{step * 4 / 5, step / 5}, 0);
        circlePoint_di.setPathEffect(dashPathEffect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, null, rect, circlePoint);
        //在油表路径中增加一个从起始弧度
        path.reset();
        path.addArc(rect, 0, 30 * progress);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        float step = length / progress;
        dashPathEffect = new DashPathEffect(new float[]{step * 4 / 5, step / 5}, 0);
        circlePoint.setPathEffect(dashPathEffect);
        //把油表的方框平移到正中间
        canvas.save();
        //旋转画布
        canvas.rotate(-90, size / 2, size / 2);
//        canvas.drawArc(rect, 0, 360 * (progress) / 100, false, circlePoint);
        canvas.drawPath(path1, circlePoint_di);
        canvas.drawPath(path, circlePoint);
        canvas.restore();
    }

    public void startProgress() {
        stop = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 12; i++) {
                    if (stop) {
                        setProgress(12);
                        if (customRoundListener != null) {
                            customRoundListener.progressUpdate();
                        }
                        return;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (stop) {
                        setProgress(12);
                        if (customRoundListener != null) {
                            customRoundListener.progressUpdate();
                        }
                        return;
                    }
                    setProgress(i);
                }
                if (customRoundListener != null) {
                    customRoundListener.progressUpdate();
                }
            }
        }).start();

    }

    public void cancle() {
        stop = true;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;

    }

    public interface CustomRoundListener {
        void progressUpdate();
    }
}
