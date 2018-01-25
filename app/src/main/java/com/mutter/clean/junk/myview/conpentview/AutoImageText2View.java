package com.mutter.clean.junk.myview.conpentview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frigate.layout.FrigateLinearLayout;
import com.frigate.utils.AutoUtils;
import com.mutter.clean.junk.R;

/**
 * Created by ${} on 2018/1/23.
 */

public class AutoImageText2View extends FrigateLinearLayout {
    Context mContext;
    float image_size;
    float title_size;
    float msg_size;
    float msg_margin_top;
    float image_margin_left;
    float text_margin_left;
    int title_color;
    int msg_color;
    Drawable image;
    String title;
    String msg;

    ImageView imageView;
    TextView textView;
    LinearLayout textView_left;
    TextView textView2;

    public AutoImageText2View(Context context) {
        this(context, null);
    }

    public AutoImageText2View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoImageText2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.auto_image_text2_view, this, true);
        sendAttr(attrs);
        initView();
        sendMess();
    }


    private void sendMess() {
        if (image != null) {
            imageView.setImageDrawable(image);
        }
        if (title != null) {
            textView.setText(title);
        }
        if (msg != null) {
            textView2.setText(msg);
        }
        if (image_size != 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = AutoUtils.getPercentWidthSize((int) image_size);
            layoutParams.height = AutoUtils.getPercentWidthSize((int) image_size);
            imageView.setLayoutParams(layoutParams);
        }

        if (title_size != 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize((int) title_size));
        }
        if (msg_size != 0) {
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentWidthSize((int) msg_size));
        }
        if (image_margin_left != 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.leftMargin = AutoUtils.getPercentWidthSize((int) image_margin_left);
            imageView.setLayoutParams(layoutParams);
        }
        if (text_margin_left != 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) textView_left.getLayoutParams();
            layoutParams.leftMargin = AutoUtils.getPercentHeightSize((int) text_margin_left);
            textView_left.setLayoutParams(layoutParams);
        }
        if (msg_margin_top != 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView2.getLayoutParams();
            layoutParams.topMargin = AutoUtils.getPercentHeightSize((int) msg_margin_top);
            textView2.setLayoutParams(layoutParams);
        }
        if (title_color != -1) {
            textView.setTextColor(title_color);
        }
        if (msg_color != -1) {
            textView2.setTextColor(msg_color);
        }

    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        textView_left = (LinearLayout) findViewById(R.id.textView_left);
        textView2 = (TextView) findViewById(R.id.textView2);
    }

    private void sendAttr(AttributeSet attrs) {
        TypedArray ta = this.mContext.obtainStyledAttributes(attrs, R.styleable.AutoImageText2View);
        image_size = ta.getDimensionPixelSize(R.styleable.AutoImageText2View_image_size, 0);
        title_size = ta.getDimensionPixelSize(R.styleable.AutoImageText2View_title_size, 0);
        msg_size = ta.getDimensionPixelSize(R.styleable.AutoImageText2View_msg_size, 0);
        msg_margin_top = ta.getDimensionPixelSize(R.styleable.AutoImageText2View_msg_margin_top, 0);
        image_margin_left = ta.getDimensionPixelSize(R.styleable.AutoImageText2View_image_margin_left, 0);
        text_margin_left = ta.getDimensionPixelSize(R.styleable.AutoImageText2View_text_margin_left, 0);
        image = ta.getDrawable(R.styleable.AutoImageText2View_image);
        title = ta.getString(R.styleable.AutoImageText2View_title_text);
        msg = ta.getString(R.styleable.AutoImageText2View_msg);
        title_color = ta.getColor(R.styleable.AutoImageText2View_title_color, -1);
        msg_color = ta.getColor(R.styleable.AutoImageText2View_msg_color, -1);
        ta.recycle();
    }
}
