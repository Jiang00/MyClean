package com.upupup.ui.demo.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upupup.ui.demo.R;
import com.upupup.ui.demo.util.Utils;
import com.upupup.ui.demo.util.WrapNullPointException;

public class PermanentPraiseView extends LinearLayout {

    public PermanentPraiseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        setData();
    }

    public PermanentPraiseView(Context context, Builder builder) throws WrapNullPointException {
        super(context);
        if (context == null) {
            throw new WrapNullPointException("context is null");
        }
        this.mContext = context;
        if (builder == null) {
            return;
        }
        init(context, null);
        this.callBack = builder.callBack;
        setTrack(builder.trackTag);
        setHeadPicture(builder.headBitmapId);
        setCancelPicture(builder.cancelBitmapId);
        setTitleText(builder.msgTxtId);
        setTitleTextColor(builder.msgTxtColorId);
        setPositiveText(builder.positiveTxtId);
        setPositiveBtnTextColor(builder.positiveTxtColorId);
        setPositiveBtnBackground(builder.positiveBackDrawableId);
        setNegativeText(builder.negativeTxtId);
        setNegativeBtnTextColor(builder.negativeTxtColorId);
        setNegativeBtnBackground(builder.negativeBackDrawableId);
    }

    private Builder.OnActionCallBack callBack = null;
    private Context mContext;
    private View view;
    private ImageView head;
    private ImageView cancel;
    private TextView title;
    private TextView positive;
    private TextView negative;

    private boolean type = true;
    private int headId;
    private int cancelId;
    private int titleText;
    private int titleTextColor;
    private int positiveText;
    private int positiveTextColor;
    private int negativeTextColor;
    private int negativeText;
    private int positiveBtnBack;
    private int negativeBtnBack;
    private String trackPosition;


    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PermanentPraiseView);
            type = a.getBoolean(R.styleable.PermanentPraiseView_type, true);
            headId = a.getResourceId(R.styleable.PermanentPraiseView_headPicture, -1);
            cancelId = a.getResourceId(R.styleable.PermanentPraiseView_cancelPicture, -1);
            titleText = a.getResourceId(R.styleable.PermanentPraiseView_titleText, -1);
            titleTextColor = a.getColor(R.styleable.PermanentPraiseView_titleTextColor, Color.WHITE);
            positiveText = a.getResourceId(R.styleable.PermanentPraiseView_positiveText, -1);
            negativeText = a.getResourceId(R.styleable.PermanentPraiseView_negativeText, -1);
            positiveTextColor = a.getColor(R.styleable.PermanentPraiseView_positiveTextColor, Color.WHITE);
            negativeTextColor = a.getColor(R.styleable.PermanentPraiseView_negativeTextColor, Color.WHITE);
            positiveBtnBack = a.getResourceId(R.styleable.PermanentPraiseView_positiveBtnBack, -1);
            negativeBtnBack = a.getResourceId(R.styleable.PermanentPraiseView_negativeBtnBack, -1);
            trackPosition = a.getString(R.styleable.PermanentPraiseView_position);
            a.recycle();

        }
        if (type) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_permanent_type_height, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.layout_permanent_type_lower, null);
        }
        addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        head = (ImageView) view.findViewById(R.id.head);
        cancel = (ImageView) view.findViewById(R.id.cancel);
        title = (TextView) view.findViewById(R.id.title);
        positive = (TextView) view.findViewById(R.id.positive);
        negative = (TextView) view.findViewById(R.id.negative);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.track("常驻好评", trackPosition, "关闭", 1);
                if (callBack != null) {
                    callBack.onCancel();
                }
            }
        });

        positive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.track("常驻好评", trackPosition, "Positive", 1);
                try {
                    Utils.setHavePraise(mContext, true);
                } catch (WrapNullPointException e) {
                    e.printStackTrace();
                }
                Utils.openPlayStore(mContext, mContext.getPackageName(), null);
                if (callBack != null) {
                    callBack.onPositive();
                }
            }
        });

        negative.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.track("常驻好评", trackPosition, "Negative", 1);
                try {
                    Utils.setHavePraise(mContext, true);
                } catch (WrapNullPointException e) {
                    e.printStackTrace();
                }
//                Utils.openPlayStore(mContext, mContext.getPackageName());
                if (callBack != null) {
                    callBack.onNegative();
                }
            }
        });

    }

    private void setData() {
        setTrack(trackPosition);
        setHeadPicture(headId);
        setCancelPicture(cancelId);
        setTitleText(titleText);
        setTitleTextColor(titleTextColor);
        setPositiveText(positiveText);
        setPositiveBtnTextColor(positiveTextColor);
        setPositiveBtnBackground(positiveBtnBack);
        setNegativeText(negativeText);
        setNegativeBtnTextColor(negativeTextColor);
        setNegativeBtnBackground(negativeBtnBack);
    }

    public void setTrack(String track) {
        if (TextUtils.isEmpty(track)) {
            track = "常驻好评-位置";
        }
        Utils.track("常驻好评", track, "展示", 1);
    }

    public void setHeadPicture(int resId) {
        if (resId > 0 && head != null) {
            head.setImageResource(resId);
        }
    }

    public void setCancelPicture(int resId) {
        if (resId > 0 && cancel != null) {
            cancel.setImageResource(resId);
        }
    }

    public void setTitleText(int resId) {
        if (resId > 0 && title != null) {
            title.setText(resId);
        }
    }

    public void setTitleText(String titleText) {
        if (titleText != null && title != null) {
            title.setText(titleText);
        }
    }

    public void setTitleTextColor(int colorId) {
        try {
            title.setTextColor(colorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPositiveText(int resId) {
        if (resId > 0 && positive != null) {
            positive.setText(resId);
        }
    }

    public void setPositiveText(String positiveText) {
        if (positiveText != null && positive != null) {
            positive.setText(positiveText);
        }
    }

    public void setPositiveBtnTextColor(int colorId) {
        try {
            positive.setTextColor(colorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPositiveBtnBackground(int resId) {
        try {
            positive.setBackgroundResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNegativeText(int resId) {
        if (resId > 0 && negative != null) {
            negative.setText(resId);
        }
    }

    public void setNegativeText(String negativeText) {
        if (negativeText != null && negative != null) {
            negative.setText(negativeText);
        }
    }

    public void setNegativeBtnTextColor(int colorId) {
        try {
            negative.setTextColor(colorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNegativeBtnBackground(int resId) {
        try {
            negative.setBackgroundResource(resId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnPermanentClickCallBack(Builder.OnActionCallBack callBack) {
        this.callBack = callBack;
    }

}
