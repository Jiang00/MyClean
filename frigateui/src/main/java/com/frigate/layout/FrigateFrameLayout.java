/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frigate.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.frigate.R;
import com.frigate.event.AutoEventListener;
import com.frigate.event.FrigateEventListener;
import com.frigate.utils.AutoLayoutHelper;
import com.frigate.utils.AutoUtils;

public class FrigateFrameLayout extends FrameLayout {
    private AutoLayoutHelper mHelper;
    Context mContext;

    int tl_roundRadius; //  圆角半径
    int tr_roundRadius; //  圆角半径
    int bl_roundRadius; //  圆角半径
    int br_roundRadius; //  圆角半径
    int gradient_angle; //  渐变角度
    int gradient_startColor; //  开始颜色
    int gradient_centerColor; // 中间颜色
    int gradient_endColor; //  结束颜色
    int roundRadius; //  圆角半径
    int strokeColor;//边框颜色
    int strokeWidth; //  边框宽度
    int soildColor;//内部填充颜色
    GradientDrawable.Orientation orientation;

    private AutoLayoutEventListener autoLayoutEventListener;

    public AutoEventListener getEventListener() {
        return autoLayoutEventListener;
    }

    public void setEventListener(FrigateEventListener autoLayoutEventListener) {
        this.autoLayoutEventListener = autoLayoutEventListener;
    }

    public interface AutoLayoutEventListener extends AutoEventListener {

    }

    public FrigateFrameLayout(Context context) {
        this(context, null);
    }

    public FrigateFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrigateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setBackageShape(attrs);
        mHelper = new AutoLayoutHelper(this, attrs);
    }

    private void setBackageShape(AttributeSet attrs) {
        if (getBackground() != null) {
            return;
        }
        TypedArray ta = this.mContext.obtainStyledAttributes(attrs, R.styleable.AutoView);
        strokeWidth = AutoUtils.getPercentWidthSize(ta.getInt(R.styleable.AutoView_strokeWidth, 0));
        roundRadius = AutoUtils.getPercentWidthSize(ta.getDimensionPixelOffset(R.styleable.AutoView_roundRadius, 0));
        if (roundRadius == 0) {
            tl_roundRadius = AutoUtils.getPercentWidthSize(ta.getDimensionPixelOffset(R.styleable.AutoView_tl_roundRadius, 0));
            tr_roundRadius = AutoUtils.getPercentWidthSize(ta.getDimensionPixelOffset(R.styleable.AutoView_tr_roundRadius, 0));
            bl_roundRadius = AutoUtils.getPercentWidthSize(ta.getDimensionPixelOffset(R.styleable.AutoView_bl_roundRadius, 0));
            br_roundRadius = AutoUtils.getPercentWidthSize(ta.getDimensionPixelOffset(R.styleable.AutoView_br_roundRadius, 0));
        }
        gradient_angle = ta.getInt(R.styleable.AutoView_gradient_angle, 0);
        strokeColor = ta.getColor(R.styleable.AutoView_strokeColor, Color.TRANSPARENT);
        soildColor = ta.getColor(R.styleable.AutoView_soildColor, Color.TRANSPARENT);
        gradient_startColor = ta.getColor(R.styleable.AutoView_gradient_startColor, Color.TRANSPARENT);
        gradient_centerColor = ta.getColor(R.styleable.AutoView_gradient_centerColor, Color.TRANSPARENT);
        gradient_endColor = ta.getColor(R.styleable.AutoView_gradient_endColor, Color.TRANSPARENT);
        ta.recycle();

        setOrientation();
        int[] colors;
        if (gradient_centerColor == 0) {
            colors = new int[]{gradient_startColor, gradient_endColor};
        } else {
            colors = new int[]{gradient_startColor, gradient_centerColor, gradient_endColor};
        }
        GradientDrawable gd = new GradientDrawable(orientation, colors);//创建drawable
        if (strokeColor != 0 && strokeWidth != 0) {
            gd.setStroke(strokeWidth, strokeColor);
        }
        if (soildColor != 0) {
            gd.setColor(soildColor);
        }
        if (roundRadius != 0) {
            gd.setCornerRadius(roundRadius);
        } else {
            gd.setCornerRadii(new float[]{tl_roundRadius, tl_roundRadius, tr_roundRadius, tr_roundRadius
                    , bl_roundRadius, bl_roundRadius, br_roundRadius, br_roundRadius});
        }
        setBackgroundDrawable(gd);
    }

    private void setOrientation() {
        switch (gradient_angle) {
            case 0:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case 1:
                orientation = GradientDrawable.Orientation.TR_BL;
                break;
            case 2:
                orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case 3:
                orientation = GradientDrawable.Orientation.BR_TL;
                break;
            case 4:
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case 5:
                orientation = GradientDrawable.Orientation.BL_TR;
                break;
            case 6:
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case 7:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
            default:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHelper.onFinishInflate(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FrigateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInEditMode()) {
            mHelper.adjustChildren();
        }
        sendMessage();
    }

    protected void sendMessage() {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams
            implements AutoLayoutHelper.AutoLayoutParams {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(FrameLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
            gravity = source.gravity;
        }

        public LayoutParams(LayoutParams source) {
            this((FrameLayout.LayoutParams) source);
            mAutoLayoutInfo = source.mAutoLayoutInfo;
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo() {
            return mAutoLayoutInfo;
        }


    }
}
