package com.vector.cleaner.myview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import com.vector.cleaner.R;

/**
 */

public class MyScrollView extends NestedScrollView implements Pullable {

    private Scroller mScroller;
    Context context;


    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mScroller = new Scroller(context);
    }

    public MyScrollView(Context context) {
        super(context);
        this.context = context;
        mScroller = new Scroller(context);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mScroller = new Scroller(context);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    private ScrollViewListener scrollViewListener = null;

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }


    public interface ScrollViewListener {

        void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy);

    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public boolean canPullDown() {
        if (true) {
            return false;
        }
        if (getScrollY() == 0)
            return true;
        else {
            return false;
        }
    }

    boolean XadSuccess;
    boolean SadSuccess;

    public void setXadSuccess(boolean isSuccess) {
        this.XadSuccess = isSuccess;
    }

    public void setSadSuccess(boolean isSuccess) {
        this.SadSuccess = isSuccess;
    }

    public boolean canPullUp() {
        if (XadSuccess) {
            return false;
        }
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return true;
        else
            return false;
    }

    //调用此方法滚动到目标位置  duration滚动时间
    public void smoothScrollToSlow(int duration) {
        int dx = 0 - getScrollX();//mScroller.getFinalX();  普通view使用这种方法
        int dy = getBottom() - getScrollY() + context.getResources().getDimensionPixelSize(R.dimen.d1500);  //mScroller.getFinalY();
        smoothScrollBySlow(dx, dy, duration);
    }

    //调用此方法滚动到顶部  duration滚动时间
    public void smoothScrollToSlowS(int duration) {
        int dx = 0 - getScrollX();//mScroller.getFinalX();  普通view使用这种方法
        int dy = getTop() + getScrollY() + context.getResources().getDimensionPixelSize(R.dimen.d250);  //mScroller.getFinalY();
        mScroller.startScroll(getScrollX(), dy, 0, -dy, duration);//scrollView使用的方法（因为可以触摸拖动）
        invalidate();
    }

    public void smoothScrollToSlowS() {
        isTouch = false;
        int dy = getTop() + getScrollY() + context.getResources().getDimensionPixelSize(R.dimen.d250);  //mScroller.getFinalY();
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, 500);//scrollView使用的方法（因为可以触摸拖动）
        invalidate();
    }


    //调用此方法设置滚动的相对偏移
    public void smoothScrollBySlow(int dx, int dy, int duration) {

        //设置mScroller的滚动偏移量
        mScroller.startScroll(getScrollX(), getScrollY(), 0, dy, duration);//scrollView使用的方法（因为可以触摸拖动）
//        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, duration);  //普通view使用的方法
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    public boolean isTouch;

    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset() && !isTouch) {
            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            smoothScrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        isTouch = true;
        return super.onTouchEvent(ev);
    }
}
