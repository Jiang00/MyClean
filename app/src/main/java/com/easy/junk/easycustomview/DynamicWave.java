
package com.easy.junk.easycustomview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


/**
 * 一：标准正余弦水波纹
 * 这种水波纹可以用具体函数模拟出具体的轨迹，所以思路基本如下：
 * 1.确定水波函数方程
 * 2.根据函数方程得出每一个波纹上点的坐标；
 * 3.将水波进行平移，即将水波上的点不断的移动；
 * 4.不断的重新绘制，生成动态水波纹；
 * 有了上面的思路，我们一步一步进行实现：
 * 正余弦函数方程为：
 * y = Asin(wx+b)+h ，这个公式里：w影响周期，A影响振幅，h影响y位置，b为初相；
 * 根据上面的方程选取自己觉得中意的波纹效果，确定对应参数的取值；
 * 然后根据确定好的方程得出所有的方程上y的数值，并将所有y值保存在数组里：
 */
public class DynamicWave extends View {
    public int width;
    public int height;
    // 波纹颜色,透明度+颜色
    private static final int WAVE_PAINT_COLOR = 0xffd5e8d0;
    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 20;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 7;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 5;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;
    private int pratent;
    boolean isStart = false;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        pratent = 15;
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = dpToPx(TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = dpToPx(TRANSLATE_X_SPEED_TWO);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Style.FILL);
        // 设置画笔颜色
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        int d = (width >= height) ? height : width;
        setMeasuredDimension(width, height);
//        initPaints();
    }

    //真正的绘制部分，绘制的代码都写到这里面;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        resetPositonY();
        for (int i = 0; i < mTotalWidth; i++) {
            // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] - pratent * height / 100, i,
                    mTotalHeight,
                    mWavePaint);

            // 绘制第二条水波纹
            canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] - pratent * height / 100, i,
                    mTotalHeight,
                    mWavePaint);
        }

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postInvalidate();
    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    //在onMeasure后面回调，可以拿到view的宽高等数据，在横竖屏切换时也会回调;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

    public void upDate(int pratent) {
        this.pratent = pratent;
        // 刷新postInvalidate（），invalidate（），区别是前者可以在子线程中更新UI
        //刷新，到onDraw方法
        postInvalidate();
    }

    RunThread runThread;

    public void start() {
        if (isStart) {
            return;
        }
        runThread = new RunThread();
        runThread.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStart = true;
    }

    public void stop() {
        isStart = false;
    }

    public class RunThread extends Thread {
        @Override
        public void run() {
            while (!isStart) {
                try {
                    Thread.sleep(100);
                    postInvalidate();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
