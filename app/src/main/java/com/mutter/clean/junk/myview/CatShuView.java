package com.mutter.clean.junk.myview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mutter.clean.junk.R;

/**
 */

public class CatShuView extends View {
    private Context context;
    private Paint circlePoint;
    float icewidth = getResources().getDimension(R.dimen.d5);
    float margin = getResources().getDimension(R.dimen.d2);
    int size;
    private int progress;
    private CustomRoundListener customRoundListener;
    private int width;
    private int height;

    public CatShuView(Context context) {
        this(context, null);
    }

    public CatShuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CatShuView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        circlePoint.setColor(ContextCompat.getColor(context, R.color.white_100));
        circlePoint.setFilterBitmap(true);
        circlePoint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int src = canvas.saveLayer(0, 0, width, height, circlePoint, Canvas.ALL_SAVE_FLAG);
        circlePoint.setColor(ContextCompat.getColor(context, R.color.white_100));
        canvas.drawCircle(width / 2, height / 2, width / 2, circlePoint);
        circlePoint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.save();
        canvas.translate(width / 2, 0);
        canvas.rotate(progress, 0, height / 2);
        circlePoint.setColor(ContextCompat.getColor(context, R.color.cat));
        canvas.drawCircle(-icewidth / 2, icewidth / 2 + margin, icewidth / 2, circlePoint);
        canvas.restore();
        if (Math.abs(progress % 360) > 90 && Math.abs(progress % 360) < 270) {
            progress = 90;
        }
        Rect rect = new Rect();
        rect.left = 0;
        rect.right = width;
        rect.top = 0;
        rect.bottom = (int) (height / 2 - (Math.sin(2 * Math.PI / 360 * (90 - Math.abs(progress % 360))) * width / 2));
        canvas.drawRect(rect, circlePoint);
        circlePoint.setXfermode(null);
        canvas.restoreToCount(src);
    }


    public void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1440);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(4000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();

            }
        });
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
