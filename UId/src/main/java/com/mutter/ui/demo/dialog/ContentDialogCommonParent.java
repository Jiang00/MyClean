package com.mutter.ui.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mutter.ui.demo.R;
import com.mutter.ui.demo.util.Utils;

public abstract class ContentDialogCommonParent implements IContentDialog {

    protected Builder.DialogType type;
    protected Context mContext;
    protected Builder mBuilder;
    protected Dialog dialog;

    private int width;

    public abstract boolean checkoutIsShouldShowPraiseDialog();
    public abstract void onCancel();
    public abstract void onPositive();
    public abstract void onNegative();
    public abstract void onDialogDismiss();

    @Override
    public void createDialog(Context context, Builder builder) {
        dialog = new Dialog(context);
        dialog.show();
        View view = null;
        if (mBuilder.type == Builder.DialogType.TYPE_PRAISE_NARROW || mBuilder.type == Builder.DialogType.TYPE_VERSION_NARROW){
            view = LayoutInflater.from(context).inflate(R.layout.layout_praise_type_narrow, null);
            width = Utils.getScreenWidth(context) * 4 / 5;
        } else if (mBuilder.type == Builder.DialogType.TYPE_VERSION_WIDE || mBuilder.type == Builder.DialogType.TYPE_PRAISE_WIDE){
            view = LayoutInflater.from(context).inflate(R.layout.layout_praise_type_wide, null);
            width = Utils.getScreenWidth(context) * 9 / 10;
        }

        if (view == null) {
            return;
        }
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width;
        window.setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView cancel = (ImageView) view.findViewById(R.id.cancel);
        ImageView head = (ImageView) view.findViewById(R.id.head);
        TextView express = (TextView) view.findViewById(R.id.express);
        TextView negative = (TextView) view.findViewById(R.id.negative);
        final TextView positive = (TextView) view.findViewById(R.id.positive);
        setCancelBtn(cancel, mBuilder);
        setTitleImage(head, mBuilder);
        setTitleTxt(express, mBuilder);
        setPositiveBtn(positive, mBuilder);
        setNegativeBtn(negative, mBuilder);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
                cancelDialog();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPositive();
                cancelDialog();
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegative();
                cancelDialog();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onDialogDismiss();
            }
        });
    }

    protected void cancelDialog(){
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void setCancelBtn(ImageView imageView, Builder builder) {
        if (builder == null || imageView == null) {
            return;
        }
        if (builder.cancelBitmapId > -1) {
            imageView.setImageResource(builder.cancelBitmapId);
        }
        if (builder.cancelBitmap != null) {
            imageView.setImageBitmap(builder.cancelBitmap);
        }
    }

    @Override
    public void setTitleImage(ImageView imageView, Builder builder) {
        if (builder == null || imageView == null) {
            return;
        }
        if (builder.headBitmapId > -1) {
            imageView.setImageResource(builder.headBitmapId);
        }
        if (builder.headBitmap != null) {
            imageView.setImageBitmap(builder.headBitmap);
        }
    }

    @Override
    public void setTitleTxt(TextView textView, Builder builder) {
        if (builder == null || textView == null) {
            return;
        }
        if (builder.msgTxtId > -1) {
            textView.setText(builder.msgTxtId);
        }
        if (builder.msgTxt != null) {
            textView.setText(builder.msgTxt);
        }
        if (builder.msgTxtColorId > -1) {
            textView.setTextColor(builder.msgTxtColorId);
        }
        if (!TextUtils.isEmpty(builder.msgTxtColor)) {
            textView.setTextColor(Color.parseColor(builder.msgTxtColor));
        }
    }

    @Override
    public void setPositiveBtn(TextView textView, Builder builder) {
        if (builder == null || textView == null) {
            return;
        }
        if (builder.positiveTxtId > -1) {
            textView.setText(builder.positiveTxtId);
        }
        if (builder.positiveTxt != null) {
            textView.setText(builder.positiveTxt);
        }
        try{
            textView.setTextColor(builder.positiveTxtColorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(builder.positiveTxtColor)) {
            textView.setTextColor(Color.parseColor(builder.positiveTxtColor));
        }
        if (builder.positiveBackDrawableId > -1) {
            textView.setBackgroundResource(builder.positiveBackDrawableId);
        }
        if (builder.positiveBackDrawable != null) {
            textView.setBackgroundDrawable(builder.positiveBackDrawable);
        }
    }

    @Override
    public void setNegativeBtn(TextView textView, Builder builder) {
        if (builder == null || textView == null) {
            return;
        }
        if (builder.negativeTxtId > -1) {
            textView.setText(builder.negativeTxtId);
        }
        if (builder.negativeTxt != null) {
            textView.setText(builder.negativeTxt);
        }
        try{
            textView.setTextColor(builder.negativeTxtColorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(builder.negativeTxtColor)) {
            textView.setTextColor(Color.parseColor(builder.negativeTxtColor));
        }
        if (builder.negativeBackDrawableId > -1) {
            textView.setBackgroundResource(builder.negativeBackDrawableId);
        }
        if (builder.negativeBackDrawable != null) {
            textView.setBackgroundDrawable(builder.negativeBackDrawable);
        }
    }

}
