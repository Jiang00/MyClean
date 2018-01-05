package com.upupup.clean.junk.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import com.upupup.clean.junk.R;

import java.util.Random;

public class MainFullLayout extends View {

    private Random random = new Random();//生成随机数
    private int width, height;
    private boolean starting = false;
    private boolean thread = true;
    private Bitmap bitmap = null;
    private Bitmap dstBitmap;
    private Bubble bubble;

    public MainFullLayout(Context context) {
        super(context);
        init();
    }

    public MainFullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MainFullLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cooling_0);
    }

    public void destroy() {
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (dstBitmap != null) {
            dstBitmap.recycle();
        }
    }


    public void pause() {
        bubble = null;
    }

    public void startAnimation() {
        bubble = new Bubble();
        int radius = random.nextInt(30);
        while (radius == 0) {
            radius = random.nextInt(30);
        }
        float speedY = random.nextFloat() * 3 + 1;
        float scale = random.nextFloat();
        while (scale <= 0.1 || scale > 0.5) {
            scale = random.nextFloat();
        }
        bubble.setScale(1);
        bubble.setRadius(radius);
        bubble.setSpeedY(1.3f);
        bubble.setX(0);
        bubble.setY(0);

        float speedX = random.nextFloat() * 5 + 10;
        bubble.setSpeedX(2);

        if (bitmap != null && !bitmap.isRecycled()) {
            try {
                int width = (int) (bitmap.getWidth() * bubble.getScale());
                int height = (int) (bitmap.getHeight() * bubble.getScale());
                if (width <= 0 || height <= 0) {
                    throw new RuntimeException();
                }
                dstBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                bubble.setBitmap(dstBitmap);
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError error) {
                bubble.setBitmap(null);
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();

        Paint paint = new Paint();
        paint.reset();
        paint.setColor(0X669999);//灰白色
        paint.setAlpha(255);//设置不透明度：透明为0，完全不透明为255
        //依次绘制气泡
        //碰到上边界从数组中移除
        if (bubble == null) {
            return;
        }
        if (bubble.getY() + bubble.getSpeedY() + bitmap.getHeight() >= height) {
            bubble = null;
        } else if (bubble.getX() + bubble.getSpeedX() <= 0) {//碰到左边界从数组中移除
            bubble.setSpeedX(-bubble.getSpeedX());
            Bitmap dst = bubble.getBitmap();
            if (dst != null) {
                canvas.drawBitmap(dst, bubble.getX(), bubble.getY(), paint);
            }
        } else if (bubble.getX() + bitmap.getWidth() + bubble.getSpeedX() >= width) { //碰到右边界从数组中移除
            bubble.setSpeedX(-bubble.getSpeedX());
            Bitmap dst = bubble.getBitmap();
            if (dst != null) {
                canvas.drawBitmap(dst, bubble.getX(), bubble.getY(), paint);
            }
        } else {
            bubble.setX(bubble.getX() + bubble.getSpeedX());
            bubble.setY(bubble.getY() + bubble.getSpeedY());
//				canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), paint);
            Bitmap dst = bubble.getBitmap();
            if (dst != null) {
                canvas.drawBitmap(dst, bubble.getX(), bubble.getY(), paint);
            }
        }
        //刷新屏幕
        if (bubble != null) {
            invalidate();
        }
    }

    //内部VO，不需要太多注释吧？
    private class Bubble {
        //气泡半径
        private float radius;
        //上升速度
        private float speedY;
        //平移速度
        private float speedX;
        //气泡x坐标
        private float x;
        // 气泡y坐标
        private float y;

        private float scale;

        private Bitmap bitmap = null;

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public float getScale() {
            return scale;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getSpeedY() {
            return speedY;
        }

        public void setSpeedY(float speedY) {
            this.speedY = speedY;
        }

        public float getSpeedX() {
            return speedX;
        }

        public void setSpeedX(float speedX) {
            this.speedX = speedX;
        }

    }
}