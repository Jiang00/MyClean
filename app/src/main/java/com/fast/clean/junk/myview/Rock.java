package com.fast.clean.junk.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fast.clean.junk.R;

/**
 * Created by Ivy on 2017/9/14.
 */

public class Rock extends View {

    public Rock(Context context) {
        super(context);
        init();
    }

    public Rock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Rock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
//        width = height = DisplayUtil.dip2px(getContext(), 180);
        setMeasuredDimension(width, height);
    }

    private void init() {
        getRocket();
        getPit();
        getSmoke();
        if (ballPaint == null) {
            ballPaint = new Paint();
            ballPaint.setAntiAlias(true);
            ballPaint.setStyle(Paint.Style.FILL);
        }
        if (ballPaint_1 == null) {
            ballPaint_1 = new Paint();
            ballPaint_1.setAntiAlias(true);
            ballPaint_1.setStyle(Paint.Style.FILL);
        }
    }

    private void getRocket() {
        if (rocket == null || rocket.isRecycled()) {
            rocket = BitmapFactory.decodeResource(getResources(), R.mipmap.rocket);
        }
    }

    private void getPit() {
        if (pit == null || pit.isRecycled()) {
            pit = BitmapFactory.decodeResource(getResources(), R.mipmap.pit);
        }
    }

    private void getSmoke() {
        if (smoke == null || smoke.isRecycled()) {
            smoke = BitmapFactory.decodeResource(getResources(), R.mipmap.smoke);
        }
    }

    private Context mContext;

    private int width;
    private int height;

    private Paint ballPaint = null;
    private Paint ballPaint_1 = null;

    private Bitmap rocket = null;
    private Bitmap pit = null;
    private Bitmap smoke = null;

    private boolean animation = true;
    private int REFRESH = 0;
    private int FRAME = 30;

    private int START_PIT = 0;
    private int START = 0;
    private int OFFSET = 8;

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!animation) {
            return;
        }

        init();
        ballPaint.setColor(Color.parseColor("#ffffff"));
        int src = canvas.saveLayer(0, 0, width, height, ballPaint, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(width / 2, height / 2, width / 2, ballPaint);
        ballPaint.setColor(Color.parseColor("#b3f5e06e"));
        canvas.drawCircle(width / 2, height / 2, width / 2, ballPaint);
        ballPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));


        START += OFFSET;
        START_PIT = height - pit.getHeight();
        if (START_PIT + START >= 0) {
            START = 0;
        }
        ballPaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawBitmap(pit, -1 * START, START_PIT + START, ballPaint);
        ballPaint.setColor(Color.parseColor("#4cffffff"));
        canvas.drawCircle(width / 3, height / 3, width / 2, ballPaint);

        ballPaint.setXfermode(null);
        canvas.restoreToCount(src);


        canvas.drawBitmap(rocket, (width - rocket.getWidth()) / 2, (height - rocket.getHeight()) / 2, null);

//        Matrix matrix = new Matrix();
        canvas.drawBitmap(smoke, width / 2 - smoke.getWidth() * 1.3f, height / 2, null);

        mHandler.sendEmptyMessageDelayed(REFRESH, 1000 / FRAME);

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH) {
                postInvalidate();
            }
        }
    };

    public void playAni() {
        if (animation) {
            return;
        }
        animation = true;
        postInvalidate();
    }

    public void stopAni() {
        if (!animation) {
            return;
        }
        animation = false;
        if (mHandler != null) {
            mHandler.removeMessages(REFRESH);
        }
    }

    public void destroy() {
        if (rocket != null && rocket.isRecycled()) {
            rocket.recycle();
        }
        if (pit != null && pit.isRecycled()) {
            pit.recycle();
        }
        if (smoke != null && smoke.isRecycled()) {
            smoke.recycle();
        }
    }


}
