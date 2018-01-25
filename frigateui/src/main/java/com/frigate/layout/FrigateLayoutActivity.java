package com.frigate.layout;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by zhy on 15/11/19.
 */
public class FrigateLayoutActivity extends AppCompatActivity {
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;

        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new FrigateFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new FrigateLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new FrigateRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }


}
