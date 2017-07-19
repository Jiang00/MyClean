package com.android.ui.demo.dialog;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class Builder {

    protected int cancelBitmapId = -1;
    protected Bitmap cancelBitmap = null;
    protected int headBitmapId = -1;
    protected Bitmap headBitmap = null;
    protected int msgTxtId = -1;
    protected int msgTxtColorId = -1;
    protected String msgTxtColor = null;
    protected String msgTxt = null;
    protected int negativeTxtId = -1;
    protected String negativeTxt = null;
    protected int negativeTxtColorId = -1;
    protected String negativeTxtColor = null;
    protected int positiveTxtId = -1;
    protected String positiveTxt = null;
    protected String positiveTxtColor = null;
    protected int positiveTxtColorId = -1;
    protected int negativeBackDrawableId = -1;
    protected Drawable negativeBackDrawable = null;
    protected int positiveBackDrawableId = -1;
    protected Drawable positiveBackDrawable = null;
    protected String trackTag = null;
    protected OnActionCallBack callBack = null;

    public enum DialogType{
        TYPE_PRAISE_NARROW, TYPE_PRAISE_WIDE, TYPE_VERSION_NARROW, TYPE_VERSION_WIDE
    }

    public Builder(){

    }

    public Builder setTrackTag(String trackTag){
        if (TextUtils.isEmpty(trackTag)) {
            trackTag = "交叉推广";
        }
        this.trackTag = trackTag;
        return this;
    }

    public Builder setCancelBitmap(int id){
        this.cancelBitmapId = id;
        return this;
    }

    public Builder setCancelBitmap(Bitmap bitmap){
        this.cancelBitmap = bitmap;
        return this;
    }

    public Builder setHeadBitmap(int id){
        this.headBitmapId = id;
        return this;
    }

    public Builder setHeadBitmap(Bitmap bitmap){
        this.headBitmap = bitmap;
        return this;
    }

    public Builder setMsgTxt(int id){
        this.msgTxtId = id;
        return this;
    }

    public Builder setMsgTxtColor(int id){
        this.msgTxtColorId = id;
        return this;
    }

    public Builder setMsgTxtColor(String color){
        this.msgTxtColor = color;
        return this;
    }

    public Builder setMsgTxt(String express){
        this.msgTxt = express;
        return this;
    }

    public Builder setNegativeTxt(int id){
        this.negativeTxtId = id;
        return this;
    }

    public Builder setNegativeTxt(String express){
        this.negativeTxt = express;
        return this;
    }

    public Builder setNegativeTxtColor(int id){
        this.negativeTxtColorId = id;
        return this;
    }

    public Builder setNegativeTxtColor(String color){
        this.negativeTxtColor = color;
        return this;
    }

    public Builder setPositiveTxt(int id){
        this.positiveTxtId = id;
        return this;
    }

    public Builder setPositiveTxt(String express){
        this.positiveTxt = express;
        return this;
    }

    public Builder setPositiveTxtColor(int id){
        this.positiveTxtColorId = id;
        return this;
    }

    public Builder setPositiveTxtColor(String color){
        this.positiveTxtColor = color;
        return this;
    }

    public Builder setNegativeBackDrawable(int id){
        this.negativeBackDrawableId = id;
        return this;
    }

    public Builder setNegativeBackDrawable(Drawable drawable){
        this.negativeBackDrawable = drawable;
        return this;
    }

    public Builder setPositiveBackDrawable(int id){
        this.positiveBackDrawableId = id;
        return this;
    }

    public Builder setPositiveBackDrawable(Drawable drawable){
        this.positiveBackDrawable = drawable;
        return this;
    }

    public Builder setCallBack(OnActionCallBack callBack){
        this.callBack = callBack;
        return this;
    }

    public interface OnActionCallBack{
        void onPositive();
        void onNegative();
        void onCancel();
        void onDismiss();
    }

}
