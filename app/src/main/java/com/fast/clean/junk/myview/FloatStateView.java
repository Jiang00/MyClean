package com.fast.clean.junk.myview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.fast.clean.mutil.Util;
import com.fast.clean.junk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengyuan on 16/8/12.
 */
public class FloatStateView extends View {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_LEFT = 1;
    public static final int STATE_RIGHT = 2;

    private Paint textPaint;
    private Paint firstPaint;
    public int width = Util.dp2px(33);
    public int height = Util.dp2px(33);
    private String text = "50%";
    private int type = 1;
    private int pratent;
    private Bitmap bitmap_normal;
    private Bitmap bitmap_left;
    private Path firstPath;
    Context context;
    private RectF rectF;

    private Matrix mShaderMatrix;
    private Paint mViewPaint;
    private AnimatorSet mAnimatorSet;
    private BitmapShader mWaveShader;
    private float mDefaultWaterLevel;
    private double mDefaultAngularFrequency;
    private float mDefaultAmplitude;
    private float mDefaultWaveLength;
    private float mWaterLevelRatio = 0.5f;
    private float mWaveShiftRatio;
    private int mFirstColor;
    private int mSecondColor;


    public FloatStateView(Context context) {
        this(context, null);
    }

    public FloatStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
        initAnimation();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createShader();
    }

    private void initW() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
        mFirstColor = ContextCompat.getColor(context, R.color.fri);
        mSecondColor = ContextCompat.getColor(context, R.color.sec);

    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();
        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                this, "amplitudeRatio", 0.03f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(1500);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        animators.add(amplitudeAnim);
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(waveShiftAnim);
    }

    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / width;
        mDefaultAmplitude = width * 0.05f;
        mDefaultWaterLevel = height * 0.5f;
        mDefaultWaveLength = width;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(1);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = width + 1;
        final int endY = height + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(mFirstColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mSecondColor);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    /**
     * 确定该控件或子控件的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
        initPaints();
        initW();
    }

    /**
     * 初始化画笔
     */
    private void initPaints() {
        firstPaint = new Paint();
        firstPaint.setAntiAlias(true);
        firstPaint.setColor(ContextCompat.getColor(context, R.color.black_5));
        // 文字
        textPaint = new Paint();
        textPaint.setTextSize(Util.dp2px(10));
        textPaint.setColor(ContextCompat.getColor(mContext, R.color.white_100));
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        bitmap_normal = Bitmap.createScaledBitmap(src, width, height, true);
        bitmap_left = BitmapFactory.decodeResource(getResources(), R.mipmap.float_beijing_left);
        rectF = new RectF();
        rectF.top = 0;
        rectF.left = 0;
        rectF.bottom = height;
        rectF.right = width;
        firstPath = new Path();
    }

    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    /**
     * 绘制控件的内容
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (type == STATE_NORMAL) {
            canvas.drawBitmap(bitmap_normal, 0, 0, null);
        } else {
            if (mWaveShader != null) {
                // first call after mShowWave, assign it to our paint
                if (mViewPaint.getShader() == null) {
                    mViewPaint.setShader(mWaveShader);
                }
                // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
                // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
                mShaderMatrix.setScale(
                        1,
                        1,
                        0,
                        mDefaultWaterLevel);
                // translate shader according to mWaveShiftRatio and mWaterLevelRatio
                // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
                mShaderMatrix.postTranslate(
                        mWaveShiftRatio * width,
                        (0.5f - mWaterLevelRatio) * height);

                // assign matrix to invalidate the shader
                mWaveShader.setLocalMatrix(mShaderMatrix);

                float radius = height / 2f;
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, firstPaint);
                canvas.drawCircle(width / 2f, height / 2f, radius, mViewPaint);
            } else {
                mViewPaint.setShader(null);
            }
            float textWidth = textPaint.measureText(text);
            float x = width / 2 - textWidth / 2;
            Paint.FontMetrics metrics = textPaint.getFontMetrics();
            float dy = -(metrics.descent + metrics.ascent) / 2;
            float y = dy + height / 2;
            canvas.drawText(text, x, y, textPaint);
            canvas.drawBitmap(bitmap_left, null, rectF, firstPaint);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        //mBackgroundColor=color;
        invalidate();
    }

    public void setDragState(int type) {
        this.type = type;
        invalidate();
    }

    public void upDate(int pratent) {
        this.text = pratent + "%";
        setWaterLevelRatio((float) pratent / 100);
        if (pratent < 40) {
            mFirstColor = ContextCompat.getColor(context, R.color.A12);
            mSecondColor = ContextCompat.getColor(context, R.color.A4);
        } else if (pratent < 80) {
            mFirstColor = ContextCompat.getColor(context, R.color.sec);
            mSecondColor = ContextCompat.getColor(context, R.color.A3);
        } else {
            mFirstColor = ContextCompat.getColor(context, R.color.sec);
            mSecondColor = ContextCompat.getColor(context, R.color.A2);
        }
    }

    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public void start() {
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    public void cancel() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet.end();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancel();
    }

    private void init() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
