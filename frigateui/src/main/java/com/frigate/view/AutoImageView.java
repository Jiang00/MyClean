package com.frigate.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.frigate.R;
import com.frigate.utils.AutoUtils;

/**
 * Created by ${} on 2018/1/22.
 */

public class AutoImageView extends AppCompatImageView {
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

    public AutoImageView(Context context) {
        super(context);
    }

    public AutoImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setBackageShape(attrs);

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


}
