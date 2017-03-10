package com.supers.clean.junk.myView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/2/28.
 */

public class MainScrollView extends ScrollView implements Pullable {

    private ValueAnimator valueAnimator;

    private MarginLayoutParams headerParams;//头部的参数
    private int headerHeight;               //头部的原始高度
    private View headerView;                //头布局

    private int cercleHeight;

    private int touchSlop;

    FrameLayout main_scale_all;

    private boolean isActionDown = false;   //第一次接收的事件是否是Down事件
    private float lastEventX;               //Move事件最后一次发生时的X坐标
    private float lastEventY;               //Move事件最后一次发生时的Y坐标
    private float downX;                    //Down事件的X坐标
    private float downY;                    //Down事件的Y坐标

    ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            cercleHeight = (int) animation.getAnimatedValue();
            Log.e("rqy", "--height=" + cercleHeight);
            headerParams.height = cercleHeight;
            headerView.setLayoutParams(headerParams);
            main_scale_all.setScaleY((float) cercleHeight / headerHeight);
            main_scale_all.setScaleX((float) cercleHeight / headerHeight);
        }
    };

    public MainScrollView(Context context) {
        super(context);
    }

    public MainScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.e("rqy", "touchSlop=" + touchSlop);
    }

    public MainScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        headerView = findViewById(R.id.main_all_cercle);
        main_scale_all = (FrameLayout) findViewById(R.id.main_scale_all);
        headerParams = (MarginLayoutParams) headerView.getLayoutParams();

        cercleHeight = headerHeight = headerParams.height;
        Log.e("rqy", "headerHeight=" + headerHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isHeadVisible() {
        return headerParams.height == 0;
    }

    private boolean isFirstVisibleHead = true;


    float scrollY = 0;

    public boolean isVisibleHead() {
        return isFirstVisibleHead;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("rqy", "scrollViewTouchListener,v=" + "onTouchEvent");
        float currentX = event.getX();
        float currentY = event.getY();
        float dy = 0;


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = lastEventX = currentX;
                downY = lastEventY = currentY;
                isActionDown = true;
                isFirstVisibleHead = cercleHeight != 0;
                scrollY = getScrollY();
                Log.e("rqy", "scrollViewTouchListener,ACTION_DOWN--isFirstVisibleHead=" + isFirstVisibleHead);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isActionDown) {
                    downX = lastEventX = currentX;
                    downY = lastEventY = currentY;
                    isFirstVisibleHead = cercleHeight != 0;
                    isActionDown = true;
                }
                //float shiftY = Math.abs(currentY - downY);
                dy = currentY - lastEventY;
                lastEventY = currentY;

                Log.e("rqy1", "ACTION_MOVE--dy=" + dy + "--getScrollY=" + getScrollY() + "--cercleHeight=" + cercleHeight + "--isFirstVisibleHead=" + isFirstVisibleHead);
                //rqy move
                // main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (dy == 0) {
                    break;
                }
                if (dy > 0) { //下拉
                    if (!isFirstVisibleHead && getScrollY() > 0) {
                        break;
                    }
                    if (cercleHeight < headerHeight) {
                        cercleHeight += dy;
                        if (cercleHeight > headerHeight) {
                            cercleHeight = headerHeight;
                        }
                        main_scale_all.setScaleY((float) cercleHeight / headerHeight);
                        main_scale_all.setScaleX((float) cercleHeight / headerHeight);
                        headerParams.height = cercleHeight;
                        headerView.setLayoutParams(headerParams);
                    }
                } else { //上拉
                    if (!isFirstVisibleHead && getScrollY() > 0) {
                        break;
                    }
                    cercleHeight += dy;
                    if (cercleHeight >= headerHeight) {
                        cercleHeight = headerHeight;
                    } else if (cercleHeight <= 0) {
                        cercleHeight = 0;
                    }
                    main_scale_all.setScaleY((float) cercleHeight / headerHeight);
                    main_scale_all.setScaleX((float) cercleHeight / headerHeight);
                    headerParams.height = cercleHeight;
                    headerView.setLayoutParams(headerParams);
                    Log.e("rqy1", "cercleHeight=" + cercleHeight + "--headerHeight=" + headerHeight);
                    if (cercleHeight != 0) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isActionDown = false;
                Log.e("rqy", "scrollViewTouchListener,ACTION_UP---getScrollY=" + getScrollY());
                if (cercleHeight == 0 || cercleHeight == headerHeight) {
                    break;
                }
                if (headerParams.height < (headerHeight / 2)) {
                    Log.e("rqy1", "cercleHeight=" + cercleHeight + "--headerHeight=" + headerHeight);
                    valueAnimator = ValueAnimator.ofInt(headerParams.height, 0);
                    startValueAnimator();
                } else if (headerParams.height < headerHeight) {
                    Log.e("rqy1", "cercleHeight1=" + cercleHeight + "--headerHeight1=" + headerHeight);
                    valueAnimator = ValueAnimator.ofInt(headerParams.height, headerHeight);
                    startValueAnimator();
                }
                break;
            default:
                break;
        }
        if (dy > 0 && !isFirstVisibleHead && getScrollY() > 0) {
            return super.onTouchEvent(event);
        }
        if (dy < 0 && !isFirstVisibleHead) {
            return super.onTouchEvent(event);
        }
        return (cercleHeight < headerHeight) || super.onTouchEvent(event);
    }

    //启动回弹动画
    private void startValueAnimator() {
        if (valueAnimator != null) {
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(updateListener);
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    //main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // main_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    if (headerParams.height == headerHeight) {
                        //smoothScrollTo(0, 0);
                        fullScroll(ScrollView.FOCUS_UP);
                    }
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationStart(Animator animation) {

                }
            });
            valueAnimator.start();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public boolean canPullDown() {
//        if (getScrollY() == 0)
//            return true;
//        else
        return false;
    }

    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

}
