package com.bruder.clean.myview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bruder.clean.junk.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BubbleLayout extends View {

    private List<Bubble> bubbles = new ArrayList<Bubble>();
    private Random random = new Random();//生成随机数
    private int width, height;
    private boolean starting = false;
    private boolean thread = true;
    private Bitmap bitmap = null;
    private Bitmap bitmap1 = null;
    private Bitmap dstBitmap;

    public BubbleLayout(Context context) {
        super(context);
        init();
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.main_xing3);
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.main_xing33);
    }

    public void destroy() {
        starting = false;
        thread = false;
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (bitmap1 != null) {
            bitmap1.recycle();
        }
        if (dstBitmap != null) {
            dstBitmap.recycle();
        }
    }

    public void setParticleBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void pause() {
        thread = false;
        if (bubbles != null) {
            bubbles.clear();
        }
    }

    //启动动画
    public void reStart() {
        thread = true;
        starting = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1080===1845
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
                        float speedY = random.nextFloat() * 15 + 7;
                        while (speedY < 5) {
                            Log.e("bubble", "===speedY<5===");
                            speedY = random.nextFloat() * 15 + 7;
                        }
                        float scale = random.nextFloat();
                        while (scale <= 0.5) {
                            scale = random.nextFloat();
                        }
                        bubble.setScale(1);
                        bubble.setRadius(radius);
                        bubble.setSpeedY(speedY);
                        bubble.setX(0);//x坐标
                        float a = (float) Math.random();
                        while (a <= 0.7) {
                            a = (float) Math.random();
                        }
                        bubble.setY(height);//y坐标

                        float speedX = random.nextFloat() * 5 + 5f;
                        bubble.setSpeedX(speedX);
                        if (((int) (Math.random() * 2)) == 0) {
                            if (bitmap != null && !bitmap.isRecycled()) {
                                try {
                                    bubble.setBitmap(bitmap);
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                } catch (OutOfMemoryError error) {
                                    bubble.setBitmap(null);
                                }
                            }
                        } else {
                            //bitmap1.isRecycled()判断是否回收
                            if (bitmap1 != null && !bitmap1.isRecycled()) {
                                try {
                                    bubble.setBitmap(bitmap1);
                                } catch (RuntimeException e) {
                                    e.printStackTrace();
                                } catch (OutOfMemoryError error) {
                                    bubble.setBitmap(null);
                                }
                            }
                        }

                        bubbles.add(bubble);
                        try {
                            Thread.sleep(random.nextInt(3) * 150 + 100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
        Paint paint = new Paint();

        paint.reset();// 重置Paint。
        paint.setColor(0X669999);//灰白色
        paint.setAlpha(255);//设置不透明度：透明为0，完全不透明为255
        List<Bubble> list = new ArrayList<Bubble>(bubbles);
        //依次绘制气泡
        for (Bubble bubble : list) {
            //碰到上边界从数组中移除
            if (bubble.getY() - bubble.getSpeedY() <= 0) {
                bubbles.remove(bubble);
            } else if (bubble.getX() + bubble.getRadius() >= width) { //碰到右边界从数组中移除
                bubbles.remove(bubble);
            } else {
                int i = bubbles.indexOf(bubble);
                if (bubble.getX() + bubble.getSpeedX() <= bubble.getRadius()) {
                    bubble.setX(bubble.getRadius());
                } else if (bubble.getX() + bubble.getSpeedX() >= width - bubble.getRadius()) {
                    bubble.setX(width - bubble.getRadius());
                } else {
                    bubble.setX(bubble.getX() + bubble.getSpeedX());
                }
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