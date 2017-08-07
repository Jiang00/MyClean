package com.myboost.junk.customviewboost;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by on 2017/4/27.
 */

public class GridLayoutManagerBoost extends GridLayoutManager {
    private boolean isScrollEnabled = false;

    public GridLayoutManagerBoost(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public GridLayoutManagerBoost(Context context, int spanCount) {
        super(context, spanCount);
    }

    public GridLayoutManagerBoost(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        //isScrollEnabled：recyclerview是否支持滑动
        //super.canScrollVertically()：是否为竖直方向滚动
        return isScrollEnabled && super.canScrollVertically();
    }

    /**
     * 是否支持滑动
     *
     * @param flag
     */
    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }


}
