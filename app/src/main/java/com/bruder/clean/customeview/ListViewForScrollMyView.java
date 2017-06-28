package com.bruder.clean.customeview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 */


public class ListViewForScrollMyView extends ListView {
    public ListViewForScrollMyView(Context context) {
        super(context);
    }

    public ListViewForScrollMyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollMyView(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    /**
     * 重写该方法,达到Listview适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}