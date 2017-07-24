package com.frau.cleanmodule.view;

/**
 * Created by Ivy on 2017/7/12.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.frau.cleanmodule.R;


public class CustomerTextView extends AppCompatTextView {
    String typeface = null;
    Context mContext;

    public CustomerTextView(Context context) {
        super(context, (AttributeSet) null);
    }

    public CustomerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.obtainAttributes(attrs);
    }

    private void obtainAttributes(AttributeSet attrs) {
        TypedArray ta = this.mContext.obtainStyledAttributes(attrs, R.styleable.CustomerTypefaceTextView);
        this.typeface = ta.getString(R.styleable.CustomerTypefaceTextView_typeface);
        ta.recycle();
        if (!TextUtils.isEmpty(this.typeface)) {
            Typeface tf = Typeface.createFromAsset(this.mContext.getAssets(), this.typeface);
            this.setTypeface(tf);
        }

    }

    public void setTypeface(String typeface) {
        if (!TextUtils.isEmpty(typeface)) {
            this.typeface = typeface;
            Typeface tf = Typeface.createFromAsset(this.mContext.getAssets(), typeface);
            this.setTypeface(tf);
        }
    }
}