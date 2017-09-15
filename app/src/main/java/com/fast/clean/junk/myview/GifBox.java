package com.fast.clean.junk.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fast.clean.junk.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ivy on 2017/9/14.
 */

public class GifBox extends View {

    public GifBox(Context context) {
        super(context);
    }

    public GifBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GifBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void init(){

        initBox();
        initHeart();

        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }

    }

    private void initBox(){
        if (box == null || box.isRecycled()) {
            box = BitmapFactory.decodeResource(getResources(), R.mipmap.gif_box);
        }
    }

    private void initHeart(){
        if (heart == null || heart.isRecycled()) {
            heart = BitmapFactory.decodeResource(getResources(), R.mipmap.heart);
        }
    }

    private int width;
    private int height;

    private Paint mPaint = null;

    private Bitmap box = null;
    private Bitmap heart = null;

    private boolean animation = false;
    private int REFRESH = 0;
    private int FRAME = 10;

    private int leftSide = 0;
    private int rightSide = 0;
    private int topSide = 0;

    private int disX = 0;
    private int disY = 0;


    private boolean isHeartCreating = false;
    private ArrayList<Heart> hearts = new ArrayList<>();

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (!animation) {
            return;
        }
        init();

        disX = leftSide = width / 2 - box.getWidth() / 10;
        disY = (height - box.getHeight()) / 2;

        rightSide = width / 2 + box.getWidth() * 2 / 5;
        topSide = height / 2 + box.getHeight() / 2;

        startCreateHeart();

        ArrayList<Heart> list = new ArrayList<Heart>(hearts);
        for (Heart item : list) {

            mPaint.setAlpha(item.alpha);
            Matrix matrix = new Matrix();
            matrix.preTranslate(item.startX, item.startY);
            matrix.preScale(item.scale, item.scale);
            canvas.drawBitmap(heart, matrix, mPaint);

//            mPaint.setAlpha(item.alpha);
//            canvas.drawBitmap(heart, item.startX, item.startY, mPaint);

            item.startX += item.offsetX;
            item.startY -= item.offsetY;
            item.alpha -= item.offsetAlpha;
            item.scale -= 0.05f;
            if (item.alpha <= 0) {
                hearts.remove(item);
            } else if (item.startX > rightSide) {
                hearts.remove(item);
            } else if (item.startX < leftSide) {
                hearts.remove(item);
            } else if (item.scale <= 0.5) {
                hearts.remove(item);
            } else if (item.startY > topSide) {
                hearts.remove(item);
            }
        }

        canvas.drawBitmap(box, (width - box.getWidth()) / 2, (height - box.getHeight()) / 2, null);

        mHandler.sendEmptyMessageDelayed(REFRESH, 1000 / FRAME);
    }

    private void startCreateHeart(){
        if (isHeartCreating) {
            return;
        }
        isHeartCreating = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                while (animation) {
                    if (!animation) {
                        return;
                    }
                    Heart heart = new Heart();
                    heart.startX += disX;
                    heart.startY += disY;
                    hearts.add(heart);
                    try {
                        Thread.sleep(random.nextInt(3) * 300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REFRESH) {
                postInvalidate();
            }
        }
    };

    public void playAni(){
        if (animation) {
            return;
        }
        animation = true;
        isHeartCreating = false;
        postInvalidate();
    }

    public void stopAni(){
        if (!animation) {
            return;
        }
        animation = false;
        isHeartCreating = false;
        if (mHandler != null) {
            mHandler.removeMessages(REFRESH);
        }
//        destroy();
    }

    public void destroy(){
        if (box != null && !box.isRecycled()){
            box.recycle();
        }
        if (heart != null && !heart.isRecycled()){
            heart.recycle();
        }
    }

    class Heart {
        public int alpha;
        public float scale;
        public int startX;
        public int offsetY;
        public int offsetX;
        public int offsetAlpha;
        public int startY = 0;
        private Random random;
        public Heart(){
            random = new Random();
            getAlpha();
            getScale();
            getStartX();
            getOffsetX();
            getOffsetY();
            getOffsetAlpha();
        }

        private void getAlpha(){
            alpha = random.nextInt(255);
            if (alpha <= 0) {
                alpha = 125;
            }
        }
        private void getScale(){
            scale = 1 + random.nextFloat();
        }
        private void getStartX(){
            startX = random.nextInt(60);
        }
        private void getOffsetX(){
            offsetX = random.nextInt(6);
            if (offsetX <= 0) {
                offsetX = 6;
            }
            if (random.nextBoolean()) {
                offsetX *= -1;
            }
        }
        private void getOffsetY(){
            offsetY = random.nextInt(6);
            if (offsetY <= 0) {
                offsetY = 6;
            }
        }
        private void getOffsetAlpha(){
            offsetAlpha = random.nextInt(20);
            if (offsetAlpha <= 0) {
                offsetAlpha = 10;
            }
        }

    }


}
