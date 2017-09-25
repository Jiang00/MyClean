package com.vector.cleaner.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.vector.cleaner.R;

/**
 * Created by Ivy on 2017/9/14.
 */

public class Gou extends View {

    private boolean isGou;

    public Gou(Context context) {
        super(context);
        init();
        this.mContext = context;
    }

    public Gou(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        this.mContext = context;
    }

    public Gou(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
//        width = height = DisplayUtil.dip2px(getContext(), 180);
        lint_yua = width * 3 / 16;
        lint_h = width * 2 / 16;
        setMeasuredDimension(width, height);
    }

    private void init() {
        if (ballPaint == null) {
            ballPaint = new Paint();
            ballPaint.setAntiAlias(true);
            ballPaint.setStyle(Paint.Style.FILL);
        }
        if (ballPaint_1 == null) {
            ballPaint_1 = new Paint();
            ballPaint_1.setAntiAlias(true);
            ballPaint_1.setStrokeWidth(getResources().getDimensionPixelOffset(R.dimen.d6));
            ballPaint_1.setStrokeCap(Paint.Cap.ROUND);
            ballPaint_1.setStyle(Paint.Style.STROKE);
        }
    }

    private Context mContext;

    private int width;
    private int height;

    private Paint ballPaint = null;
    private Paint ballPaint_1 = null;

    private float pretant;
    private float pretant_e;
    private float line;
    private float line_e;

    private int lint_yua;
    private int lint_h;

    //线1的x轴
    private int line1_x = 0;
    //线1的y轴
    private int line1_y = 0;
    //线2的x轴
    private int line2_x = 0;
    //线2的y轴
    private int line2_y = 0;

    private float line2_success = 0;

    private int move_distance = 6;//dp


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        ballPaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawCircle(width / 2, height / 2, width / 3, ballPaint);
        ballPaint_1.setColor(Color.parseColor("#9696ce"));
        RectF rectF = new RectF();
        rectF.left = width / 4 + 20;
        rectF.top = width / 4 + 20;
        rectF.bottom = width * 3 / 4 - 20;
        rectF.right = width * 3 / 4 - 20;
        canvas.save();
        canvas.rotate(-90, width / 2, width / 2);
        canvas.drawArc(rectF, pretant_e * -3.6f, pretant * -3.6f, false, ballPaint_1);
        canvas.restore();
        if (pretant_e < 100) {
            return;
        }

        int radius = width / 2;
        //获取圆心的x坐标
        int center = width / 2;
        int center1 = center - width / 5;
        if (line1_x < radius / 4) {
            line1_x += move_distance;
            line1_y += move_distance;
            line2_x = line1_x;
            line2_y = line1_y;
            //画第一根线
            canvas.drawLine(center1, center, center1 + line1_x, center + line1_y, ballPaint_1);
        } else if (line1_x >= radius / 4 && line2_x <= radius * 3 / 4) {
            line2_x += move_distance;
            line2_y -= move_distance;
            //画第一根线
            canvas.drawLine(center1, center, center1 + line1_x, center + line1_y, ballPaint_1);
            //画第二根线
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                canvas.drawLine(center1 + line1_x, center + line1_y, center1 + line2_x, center + line2_y, ballPaint_1);
            } else {
                canvas.drawLine(center1 + line1_x - 14 / 2, center + line1_y, center1 + line2_x, center + line2_y, ballPaint_1);
            }
        } else {
            isGou = true;
            //画第一根线
            canvas.drawLine(center1, center, center1 + line1_x, center + line1_y, ballPaint_1);
            //画第二根线
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                canvas.drawLine(center1 + line1_x, center + line1_y, center1 + line2_x, center + line2_y, ballPaint_1);
            } else {
                canvas.drawLine(center1 + line1_x - 14 / 2, center + line1_y, center1 + line2_x, center + line2_y, ballPaint_1);
            }
            ballPaint_1.setColor(Color.parseColor("#ffffff"));
            canvas.save();
            canvas.rotate(-120, width * 3 / 8, lint_yua);
            canvas.drawLine(width * 3 / 8 + lint_h * (line_e) / 50, lint_yua, width * 3 / 8 + lint_h * (line) / 50, lint_yua, ballPaint_1);
            canvas.restore();
            canvas.save();
            canvas.rotate(-90, width / 2, lint_yua);
            canvas.drawLine(width / 2 + lint_h * (line_e) / 50, lint_yua, width / 2 + lint_h * (line) / 50, lint_yua, ballPaint_1);
            canvas.restore();
            canvas.save();
            canvas.rotate(-60, width * 5 / 8, lint_yua);
            canvas.drawLine(width * 5 / 8 + lint_h * (line_e) / 50, lint_yua, width * 5 / 8 + lint_h * (line) / 50, lint_yua, ballPaint_1);
            canvas.restore();
            ballPaint_1.setColor(Color.parseColor("#9696ce"));
        }
    }

    public void startAni() {
        run = true;
        isGou = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 140; i++) {
                    setPa(i);
                    if (!run) {
                        return;
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!run) {
                        return;
                    }
                }
                while (!isGou) {
                    if (!run) {
                        return;
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!run) {
                        return;
                    }
                    postInvalidate();
                }
                for (int i = 0; i <= 100; i++) {
                    setPaL(i);
                    if (!run) {
                        return;
                    }
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!run) {
                        return;
                    }
                }
                if (!run) {
                    return;
                }
                if (listener != null) {
                    listener.animaEnd();
                }
            }
        }).start();

    }

    boolean run;

    public void stopAnima() {
        run = false;
    }

    public void setPa(int pretant) {
        if (pretant < 40) {
            this.pretant = pretant;
        } else if (pretant < 100) {
            this.pretant_e = pretant - 40;
        } else {
            this.pretant_e = pretant - 40;
            this.pretant = 140 - pretant;
        }
        postInvalidate();
    }

    public void setPaL(int pretant) {


        if (pretant <= 50) {
            this.line = pretant;
        } else if (pretant <= 100) {
            this.line_e = pretant - 50;
        }
        postInvalidate();
    }


    GouListener listener;

    public void setGouListener(GouListener listener) {
        this.listener = listener;
    }

    public interface GouListener {
        void animaEnd();
    }

}
