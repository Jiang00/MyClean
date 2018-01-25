package com.mutter.clean.junk.myview;

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

import com.frigate.utils.AutoUtils;
import com.mutter.clean.junk.R;

/**
 */

public class RoundSd extends View {
    private Context context;
    private Paint backgPoint;
    float lineWidth = AutoUtils.getPercentWidthSize(24);
    int size;
    int backageColor;
    private CustomRoundListener customRoundListener;
    private Matrix mMatrix;
    private Paint circlePoint;
    private int progress;
    private boolean isRotate;
    private Bitmap bitmap;
    int startAngle=90;

    public RoundSd(Context context) {
        this(context, null);
    }

    public RoundSd(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundSd(Context context, AttributeSet attrs, int defStyleAttr) {
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
        backageColor=ContextCompat.getColor(context,R.color.B4);
        backgPoint.setColor(backageColor);
        mMatrix = new Matrix();
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_dian);
        bitmap = Bitmap.createScaledBitmap(bitmap, AutoUtils.getPercentWidthSize(30), AutoUtils.getPercentWidthSize(30), true);
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
public void setBitmap(Bitmap bitmap){
    if (this.bitmap!=null&&!this.bitmap.isRecycled()){
        this.bitmap.recycle();
    }
    this.bitmap=bitmap;
}
public void setBitmapSize(int size){
    bitmap = Bitmap.createScaledBitmap(bitmap, AutoUtils.getPercentWidthSize(size), AutoUtils.getPercentWidthSize(size), true);
}
public void setLineWidth(int size){
    lineWidth=AutoUtils.getPercentWidthSize(size);
    circlePoint.setStrokeWidth(AutoUtils.getPercentWidthSize(size));
    backgPoint.setStrokeWidth(AutoUtils.getPercentWidthSize(size));
}
public void setBackagePaintColor(int color){
    backageColor=color;
    backgPoint.setColor(backageColor);
}
public void setStartAngle(int angle){
    startAngle=angle;
}
public void setLineColor(int... colors){
    if (colors.length==1){
        circlePoint.setShader(null);
        circlePoint.setColor( colors[0]);
    }else {
        LinearGradient gradient = new LinearGradient(0, 0, size, size, colors[0]
                , colors[1], Shader.TileMode.CLAMP);
        circlePoint.setShader(gradient);
    }

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
        canvas.rotate(startAngle, size / 2, size / 2);
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
