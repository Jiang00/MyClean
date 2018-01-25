package com.mutter.clean.junk.myview.conpentview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frigate.layout.FrigateLinearLayout;
import com.frigate.utils.AutoUtils;
import com.mutter.clean.junk.R;

/**
 * Created by ${} on 2018/1/23.
 */

public class AutoImageTextView extends FrigateLinearLayout {
    private final View view;
    Context mContext;
    float image_size;
    float text_size;
    float text_margin_top;
    int text_color;
    Drawable image;
    String text;
    ImageView imageView;
    TextView textView;

    public AutoImageTextView(Context context) {
        this(context, null);
    }

    public AutoImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        view = LayoutInflater.from(context).inflate(R.layout.auto_image_text_view, this, true);
        sendAttr(attrs);
        initView();
    }

    @Override
    protected void sendMessage() {
        super.sendMessage();
        sendMess();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private void sendMess() {
        if (image != null) {
            imageView.setImageDrawable(image);
        }

        if (text != null) {
            textView.setText(text);
        }
        if (image_size != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = AutoUtils.getPercentWidthSize((int) image_size);
            layoutParams.height = AutoUtils.getPercentWidthSize((int) image_size);//
            imageView.setLayoutParams(layoutParams);
        }
        if (text_size != 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize((int) text_size));
        }
        if (text_margin_top != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.topMargin = AutoUtils.getPercentHeightSize((int) text_margin_top);
            textView.setLayoutParams(layoutParams);
        }
        if (text_color != -1) {
            textView.setTextColor(text_color);
        }

    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
    }

    private void sendAttr(AttributeSet attrs) {
        TypedArray ta = this.mContext.obtainStyledAttributes(attrs, R.styleable.AutoImageTextView);
        image_size = ta.getDimensionPixelSize(R.styleable.AutoImageTextView_image_size, 0);
        text_size = ta.getDimensionPixelSize(R.styleable.AutoImageTextView_text_size, 0);
        text_margin_top = ta.getDimensionPixelSize(R.styleable.AutoImageTextView_text_margin_top, 0);
        text_color = ta.getColor(R.styleable.AutoImageTextView_text_color, -1);
        image = ta.getDrawable(R.styleable.AutoImageTextView_image);
        text = ta.getString(R.styleable.AutoImageTextView_text);
        ta.recycle();
    }
}
