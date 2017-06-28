package com.bruder.clean.customeview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import com.bruder.clean.junk.R;

/**
 */

public class MyMainScrollView extends NestedScrollView implements Pullable {

    private Scroller mScroller;
    Context context;

    public MyMainScrollView(Context context) {
        super(context);
        this.context = context;
        mScroller = new Scroller(context);
    }

    public MyMainScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mScroller = new Scroller(context);
    }

    public MyMainScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    public interface ScrollViewListener {

        void onScrollChanged(MyMainScrollView scrollView, int x, int y, int oldx, int oldy);

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

    boolean adSuccess;

    public void setAdSuccess(boolean isSuccess) {
        this.adSuccess = isSuccess;
    }

    public boolean canPullUp() {
        if (adSuccess) {
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
