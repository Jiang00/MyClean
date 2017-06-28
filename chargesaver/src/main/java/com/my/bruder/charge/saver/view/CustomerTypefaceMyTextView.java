package com.my.bruder.charge.saver.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.my.bruder.charge.saver.R;

/**
 * 设置字体
 * Created by on 2016/10/27.
 */

public class CustomerTypefaceMyTextView extends android.support.v7.widget.AppCompatTextView {
    String typeface = null;

    Context mContext;

    public CustomerTypefaceMyTextView(Context context) {
        super(context, null);
    }

    public CustomerTypefaceMyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        obtainAttributes(attrs);
    }

    private void obtainAttributes(AttributeSet attrs) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.CustomerTypefaceMyTextView);
        typeface = ta.getString(R.styleable.CustomerTypefaceMyTextView_typeface);
        ta.recycle();
        if (!TextUtils.isEmpty(typeface)) {
            Typeface tf = Typeface.createFromAsset(mContext.getAssets(), typeface);
            setTypeface(tf);
        }
    }

    public void setTypeface(String typeface) {
        if (TextUtils.isEmpty(typeface)) {
            return;
        }
        this.typeface = typeface;
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), typeface);
        setTypeface(tf);
    }
}
