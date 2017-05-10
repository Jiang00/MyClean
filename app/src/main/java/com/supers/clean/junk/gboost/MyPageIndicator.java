package com.supers.clean.junk.gboost;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.supers.clean.junk.R;

/**
 * Created by zhuguohui on 2016/8/21 0021.
 */
public class MyPageIndicator extends LinearLayout implements PageGridView.PageIndicator {
    public MyPageIndicator(Context context) {
        this(context, null);
    }

    public MyPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void InitIndicatorItems(int itemsNumber) {
        removeAllViews();
        for (int i = 0; i < itemsNumber; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.shape_online);
            imageView.setPadding(10, 0, 10, 0);
            addView(imageView);
        }
    }

    @Override
    public void onPageSelected(int pageIndex) {
        ImageView imageView = (ImageView) getChildAt(pageIndex);
        if(imageView!=null) {
            imageView.setImageResource(R.drawable.shape_invisible);
        }
    }

    @Override
    public void onPageUnSelected(int pageIndex) {
        ImageView imageView = (ImageView) getChildAt(pageIndex);
        if(imageView!=null) {
            imageView.setImageResource(R.drawable.shape_online);
        }
    }
}
