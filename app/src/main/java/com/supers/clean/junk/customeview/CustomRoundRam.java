package com.supers.clean.junk.customeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.supers.clean.junk.R;

/**
 */

public class CustomRoundRam extends View {
    private Context context;
    private Paint circlePoint;
    private Paint backgPoint;
    private Paint bluePoint;
    float lineWidth = getResources().getDimension(R.dimen.d5);
    int size;
    private int progress;
    private boolean isRotate;
    private CustomRoundListener customRoundListener;
    private Matrix mMatrix;
    private Bitmap bitmap;

    public CustomRoundRam(Context context) {
        this(context, null);
    }

    public CustomRoundRam(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRoundRam(Context context, AttributeSet attrs, int defStyleAttr) {
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
        circlePoint.setColor(ContextCompat.getColor(context, R.color.A8));
        circlePoint.setStrokeCap(Paint.Cap.ROUND);
        circlePoint.setFilterBitmap(true);
        circlePoint.setAntiAlias(true);
        circlePoint.setStrokeWidth(lineWidth);
        circlePoint.setStyle(Paint.Style.STROKE);
        backgPoint = new Paint();
        backgPoint.setAntiAlias(true);
        backgPoint.setStrokeWidth(lineWidth);
        backgPoint.setStrokeCap(Paint.Cap.ROUND);
        backgPoint.setStyle(Paint.Style.STROKE);
        backgPoint.setColor(context.getResources().getColor(R.color.B4));
        bluePoint = new Paint();
        bluePoint.setAntiAlias(true);
        bluePoint.setStrokeWidth(lineWidth);
        bluePoint.setColor(context.getResources().getColor(R.color.B6));
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
        LinearGradient gradient = new LinearGradient(0, 0, size, size, ContextCompat.getColor(context, R.color.C8)
                , ContextCompat.getColor(context, R.color.C9), Shader.TileMode.CLAMP);
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
        canvas.rotate(90, size / 2, size / 2);
        canvas.drawArc(rect, 0, progress * 360 / 100, false, circlePoint);
        Path path = new Path();
        path.addArc(rect, 0, progress * 360 / 100);
        float[] pos = new float[2]; // 当前点的实际位置
        float[] tan = new float[2]; // 当前点的tangent值,
        PathMeasure measure = new PathMeasure(path, false);
        measure.getPosTan(measure.getLength() * 1, pos, tan);
        mMatrix.reset();
        mMatrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, mMatrix, circlePoint);
        canvas.restore();
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
