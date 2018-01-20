package com.eifmobi.clean.junk.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.eifmobi.clean.junk.R;

/**
 */

public class RoundRam extends View {
    private Context context;
    private Paint backgPoint;//背景圆
    private Paint circlePoint;//进度
    private Paint keduPaint;//圆刻度
    float lineWidth = getResources().getDimension(R.dimen.d7);
    float keduWidth = getResources().getDimension(R.dimen.d10);
    float keduHight = getResources().getDimension(R.dimen.d3);
    float margin = getResources().getDimension(R.dimen.d12);
    int size;
    private CustomRoundListener customRoundListener;
    private Matrix mMatrix;
    private int progress;
    private boolean isRotate;
    private Bitmap bitmap;

    public RoundRam(Context context) {
        this(context, null);
    }

    public RoundRam(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRam(Context context, AttributeSet attrs, int defStyleAttr) {
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
        circlePoint.setStrokeCap(Paint.Cap.ROUND);
        circlePoint.setFilterBitmap(true);
        circlePoint.setAntiAlias(true);
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint.setStyle(Paint.Style.STROKE);

        keduPaint = new Paint();
        keduPaint.setStrokeCap(Paint.Cap.ROUND);
        keduPaint.setFilterBitmap(true);
        keduPaint.setAntiAlias(true);
        keduPaint.setStrokeWidth(keduHight);

        backgPoint = new Paint();
        backgPoint.setAntiAlias(true);
        backgPoint.setStrokeWidth(lineWidth);
        backgPoint.setStrokeCap(Paint.Cap.ROUND);
        backgPoint.setStyle(Paint.Style.STROKE);
        backgPoint.setColor(context.getResources().getColor(R.color.A2));
        mMatrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_dian_s);
        bitmap = Bitmap.createScaledBitmap(bitmap, getResources().getDimensionPixelOffset(R.dimen.d5),
                getResources().getDimensionPixelOffset(R.dimen.d5), true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        size = d;
        setMeasuredDimension(d, d);
        LinearGradient gradient = new LinearGradient(0, 0, size, size, ContextCompat.getColor(context, R.color.A6)
                , ContextCompat.getColor(context, R.color.A5), Shader.TileMode.CLAMP);
        circlePoint.setShader(gradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF();
        rect.left = 0 + lineWidth / 2 + 1;
        rect.right = size - lineWidth / 2 - 1;
        rect.top = 0 + lineWidth / 2 + 1;
        rect.bottom = size - lineWidth / 2 - 1;
        canvas.drawArc(rect, 0, 360, false, backgPoint);

        canvas.save();
        canvas.rotate(-90, size / 2, size / 2);
        canvas.drawArc(rect, 0, progress * 360 / 100, false, circlePoint);
        if (progress != 100 || progress != 0) {
            Path path = new Path();
            path.addArc(rect, 0, progress * 360 / 100);
            float[] pos = new float[2]; // 当前点的实际位置
            float[] tan = new float[2]; // 当前点的tangent值,
            PathMeasure measure = new PathMeasure(path, false);
            measure.getPosTan(measure.getLength() * 1, pos, tan);
            mMatrix.reset();
            mMatrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);
            canvas.drawBitmap(bitmap, mMatrix, circlePoint);
        }
        canvas.restore();


        int src = canvas.saveLayer(0, 0, size, size, keduPaint, Canvas.ALL_SAVE_FLAG);
        canvas.save();
        canvas.translate(0, size / 2);
        canvas.rotate(90, size / 2, 0);
        float degrees = (float) (360.0 / 36);
        for (int i = 0; i <= 36; i++) {
            canvas.drawLine(margin, keduHight / 2, keduWidth + margin, keduHight / 2, keduPaint);
            canvas.rotate(degrees, size / 2, 0);
        }
        canvas.restore();

        canvas.save();
        canvas.rotate(-90, size / 2, size / 2);
        keduPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        SweepGradient mColorShader = new SweepGradient(size / 2, size / 2, new int[]{ContextCompat.getColor(context, R.color.A6), ContextCompat.getColor(context, R.color.A5)
                , ContextCompat.getColor(context, R.color.A4)}, null);
//        LinearGradient gradient = new LinearGradient(0, 0, size, size, ContextCompat.getColor(context, R.color.A6)
//                , ContextCompat.getColor(context, R.color.A4), Shader.TileMode.CLAMP);
        keduPaint.setShader(mColorShader);
        canvas.drawCircle(size / 2, size / 2, size / 2, keduPaint);
        keduPaint.setShader(null);
        keduPaint.setXfermode(null);
        canvas.restore();
        canvas.restoreToCount(src);
    }

    public void startProgress(boolean isRotate, final int progress) {
        this.isRotate = isRotate;
        final int st = this.progress;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= progress; i++) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setProgress(i);
                    if (customRoundListener != null) {
                        customRoundListener.progressUpdate(i);
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
