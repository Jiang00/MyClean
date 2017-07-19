package com.android.ui.demo.cross;

import android.text.TextUtils;

public class Builder{
    public enum Type{
        TYPE_72, TYPE_56, TYPE_48, TYPE_HORIZONTAL_76, TYPE_HORIZONTAL_99, TYPE_SQUARE_272, TYPE_SQUARE_193, TYPE_DIALOG
    }
    protected String extraData = null;
    protected boolean isShowActionBtn= true;
    protected int actionBtnBackground = -1;
    protected int actionBtnTextColor = -1;
    protected int actionText = -1;
    protected Type type;
    protected int adTagImageId = -1;
    protected int mainIconId = -1;
    protected int titleTextId = -1;
    protected int titleColor = -1;
    protected int subTitleText = -1;
    protected int subTitleColor = -1;
    protected int headImage = -1;
    protected int rootViewBackground = -1;
    protected String requestTag;
    protected String trackPosition = null;
    protected OnCrossActionCallBack callBack = null;
    public interface OnCrossActionCallBack{
        //        void onAction();
        void onViewClick();
    }
    private Builder(){}

    public Builder setTrackTag(String position){
        if (TextUtils.isEmpty(position)) {
            position = "交叉推广";
        }
        this.trackPosition = position;
        return this;
    }

    public Builder( String requestTag){
        this.requestTag = requestTag;
    }
    /**
     * 设置整体的背景
     * @param id  color资源id
     */
    public Builder setRootViewBackgroundColor(int id){
        this.rootViewBackground = id;
        return this;
    }
    /**
     * 传入交叉推广数据 若为空 则显示默认数据（默认数据为空）
     * 可根据builder内的方法手动设置在获取不到数据时的展示内容
     * @param data 服务器数据
     */
    public Builder setServiceData(String data){
        this.extraData = data;
        return this;
    }
    /**
     * 设置是否显示ActionBtn
     * @param isShouldShow false 不显示 true 显示
     */
    public Builder setIsShouldShowDownLoadBtn(boolean isShouldShow){
        this.isShowActionBtn = isShouldShow;
        return this;
    }
    /**
     *  Actionbtn的背景
     *  @param id 需要设置的资源id
     */
    public Builder setActionBtnBackground(int id){
        this.actionBtnBackground = id;
        return this;
    }
    /**
     *  Actionbtn的字体颜色
     *  @param color 需要设置的资源id
     */
    public Builder setActionTextColor(int color){
        this.actionBtnTextColor = color;
        return this;
    }
    /**
     *  Actionbtn显示的文字
     *  @param id 需要设置的资源id
     */
    public Builder setDefaultActionText(int id){
        this.actionText = id;
        return this;
    }
    /**
     * 需要显示的CrossView的类型
     * @param type 类型参数 参照enmu Type
     */
    public Builder setType(Type type){
        this.type = type;
        return this;
    }
    /**
     * 设置交叉推广的AD角标
     * @param id 角标资源的id
     */
    public Builder setAdTagImageId(int id){
        this.adTagImageId = id;
        return this;
    }
    /**
     * 设置推广应用的icon
     * @param id  icon资源的id
     */
    public Builder setDefaultIconId(int id){
        this.mainIconId = id;
        return this;
    }
    /**
     * 设置推广应用的主标题
     * @param resId  标题资源的id
     */
    public Builder setDefaultTitle(int resId){
        this.titleTextId = resId;
        return this;
    }
    /**
     * 设置推广应用的主标题的字体颜色
     * @param id  主标题颜色资源的id
     */
    public Builder setTitleTextColor(int id){
        this.titleColor = id;
        return this;
    }
    /**
     * 设置推广应用的副标题
     * @param resId  副标题资源的id
     */
    public Builder setDefaultSubTitle(int resId){
        this.subTitleText = resId;
        return this;
    }
    /**
     * 设置推广应用的副标题的字体颜色
     * @param id  副标题颜色资源的id
     */
    public Builder setSubTitleTextColor(int id){
        this.subTitleColor = id;
        return this;
    }
    /**
     * 应用的推广图片
     * @param id 图片资源的id
     */
    public Builder setDefaultHeadImage(int id){
        this.headImage = id;
        return this;
    }
    /**
     * CrossView的点击事件的回调
     * @param crossActionCallBack
     */
    public Builder setOnCrossActionCallBack(OnCrossActionCallBack crossActionCallBack){
        this.callBack = crossActionCallBack;
        return this;
    }
}