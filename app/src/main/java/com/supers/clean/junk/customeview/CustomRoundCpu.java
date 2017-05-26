package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.supers.clean.junk.R;

/**
 */

public class CustomRoundCpu extends View {

    private Context context;
    private Paint circlePoint;
    float lineWidth = getResources().getDimension(R.dimen.d5);
    float padding = getResources().getDimension(R.dimen.d14);
    int size;
    private int progress;
    private CustomRoundListener customRoundListener;
    private Bitmap bitmap;
    private Matrix mMatrix;

    public CustomRoundCpu(Context context) {
        this(context, null);
    }

    public CustomRoundCpu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundCpu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        circlePoint = new Paint();
        circlePoint.setAntiAlias(true);
        circlePoint.setStrokeCap(Paint.Cap.ROUND);
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint.setStyle(Paint.Style.STROKE);
        mMatrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_dian);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        size = d;
        setMeasuredDimension(d, d);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePoint.setColor(context.getResources().getColor(R.color.white_25));
        RectF rect = new RectF();
        rect.left = 0 + lineWidth / 2 + padding;
        rect.right = size - lineWidth / 2 - padding;
        rect.top = 0 + lineWidth / 2 + padding;
        rect.bottom = size - lineWidth / 2 - padding;
        canvas.drawArc(rect, 0, 360, false, circlePoint);
        canvas.save();

//        if (progress >= 0 && progress < 40) {
        circlePoint.setColor(context.getResources().getColor(R.color.A8));
//        } else if (progress >= 40 && progress < 80) {
//            circlePoint.setColor(context.getResources().getColor(R.color.A4));
//        } else {
//            circlePoint.setColor(context.getResources().getColor(R.color.A2));
//        }
        canvas.rotate(-90, size / 2, size / 2);
        canvas.drawArc(rect, 0, progress * -360 / 100, false, circlePoint);
        Path path = new Path();
        path.addArc(rect, 0, progress * -360 / 100);
        float[] pos = new float[2]; // 当前点的实际位置
        float[] tan = new float[2]; // 当前点的tangent值,
        PathMeasure measure = new PathMeasure(path, false);
        measure.getPosTan(measure.getLength() * 1, pos, tan);
        mMatrix.reset();
        mMatrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, mMatrix, circlePoint);
//        canvas.drawCircle(pos[0], pos[1], padding, circlePoint);
        Log.e("zuobiao", pos[0] + "==" + pos[1]);
        canvas.restore();
    }

    public void startProgress(final int progress) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 100; i >= progress; i--) {
                    setProgress(i);
                    if (customRoundListener != null) {
                        customRoundListener.progressUpdate(i);
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void reStartProgress(final int progress) {
        final int st = this.progress;
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = st; i <= progress; i++) {
                    setProgress(i);
                    if (customRoundListener != null) {
                        customRoundListener.progressUpdate(i);
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setCustomRoundListener(CustomRoundListener customRoundListener) {
        this.customRoundListener = customRoundListener;

    }

    public interface CustomRoundListener {
        void progressUpdate(int progress);
    }
}
