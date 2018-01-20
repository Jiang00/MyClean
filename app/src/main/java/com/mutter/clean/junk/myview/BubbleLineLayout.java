package com.mutter.clean.junk.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mutter.clean.junk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubbleLineLayout extends View {

    private List<Bubble> bubbles = new ArrayList<Bubble>();
    private Random random = new Random();//生成随机数
    private int width, height;
    private boolean starting = false;
    private boolean thread = true;
    private Bitmap bitmap1 = null;
    private Bitmap dstBitmap;

    public BubbleLineLayout(Context context) {
        super(context);
        init();
    }

    public BubbleLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleLineLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.bubble_line);
    }

    public void destroy() {
        starting = false;
        thread = false;
        if (bitmap1 != null) {
            bitmap1.recycle();
        }
        if (dstBitmap != null) {
            dstBitmap.recycle();
        }
    }

    public void setParticleBitmap(Bitmap bitmap) {
        this.bitmap1 = bitmap;
    }

    public void pause() {
        thread = false;
        if (bubbles != null) {
            bubbles.clear();
        }
    }

    public void reStart() {
        thread = true;
        starting = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        if (!starting) {
            starting = true;
            new Thread() {
                public void run() {
                    while (starting && thread) {
                        Log.d("MyTest", "bubble Thread");
                        Bubble bubble = new Bubble();
                        int radius = random.nextInt(30);
                        while (radius == 0) {
                            radius = random.nextInt(30);
                        }
                        float speedY = -random.nextFloat() * 5-10;
                        float scale = random.nextFloat();
                        while (scale <= 0.1) {
                            scale = random.nextFloat() * 5;
                        }
                        bubble.setScale(1);
                        bubble.setRadius(radius);
                        bubble.setSpeedY(speedY);
                        bubble.setX(random.nextInt(width));
                        bubble.setY(0);

                        float speedX = random.nextFloat() - 0.5f;
                        while (speedX == 0) {
                            speedX = random.nextFloat() - 0.5f;
                        }
                        bubble.setSpeedX(0);
                        if (bitmap1 != null && !bitmap1.isRecycled()) {
                            try {
                                int width = (int) (bitmap1.getWidth() * bubble.getScale());
                                int height = (int) (bitmap1.getHeight() * bubble.getScale());
                                if (width <= 0 || height <= 0) {
                                    throw new RuntimeException();
                                }
                                dstBitmap = Bitmap.createScaledBitmap(bitmap1, width, height, true);
                                bubble.setBitmap(dstBitmap);
                            } catch (RuntimeException e) {
                                e.printStackTrace();
                            } catch (OutOfMemoryError error) {
                                bubble.setBitmap(null);
                            }
                        }
                        bubbles.add(bubble);
                        try {
                            Thread.sleep(random.nextInt(3) * 100 + 50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        Paint paint = new Paint();

        paint.reset();
        paint.setColor(0X669999);//灰白色
        paint.setAlpha(200);//设置不透明度：透明为0，完全不透明为255
        List<Bubble> list = new ArrayList<Bubble>(bubbles);
        //依次绘制气泡
        for (Bubble bubble : list) {
            //碰到上边界从数组中移除
            if (bubble.getY() + bubble.getSpeedY() >= height) {
                bubbles.remove(bubble);
            } else {
                int i = bubbles.indexOf(bubble);
                bubble.setY(bubble.getY() - bubble.getSpeedY());

                bubbles.set(i, bubble);
//				canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), paint);
                Bitmap dst = bubble.getBitmap();
                if (dst != null) {
                    canvas.drawBitmap(dst, bubble.getX(), bubble.getY(), paint);
                } else {
                    bubbles.remove(bubble);
                }
            }
        }
        //刷新屏幕
        invalidate();
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