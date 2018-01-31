package com.mutter.clean.junk.myview.conpentview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.frigate.layout.FrigateLinearLayout;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.util.Util;
import com.mutter.cleanmodule.activity.LajiActivity;
import com.mutter.cleanmodule.battery.TemperatureDownActivity;
import com.mutter.ui.demo.entry.CrossItem;
import com.mutter.ui.demo.util.JsonParser;
import com.mutter.ui.demo.util.Utils;

import java.util.ArrayList;

/**
 * Created by ${} on 2018/1/23.
 */

public class AutoTuiguangView extends FrigateLinearLayout {
    protected String TUIGUAN_MAIN = "main_hard";
    protected String TUIGUAN_MAIN_SOFT = "main_soft";
    protected String TUIGUAN_SIDE = "slide_hard";
    protected String TUIGUAN_SIDE_SOFT = "slide_soft";
    protected String TUIGUAN_SETTING = "setting_hard";
    protected String TUIGUAN_SETTING_SOFT = "setting_soft";
    protected String TUIGUAN_SUCCESS = "success_hard";
    protected String TUIGUAN_SUCCESS_SOFT = "success_soft";

    Context mContext;

    public AutoTuiguangView(Context context) {
        this(context, null);
    }

    public AutoTuiguangView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoTuiguangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = this.mContext.obtainStyledAttributes(attrs, R.styleable.AutoTuiguangView);
        int type = ta.getInt(R.styleable.AutoTuiguangView_types_of, -1);
        ta.recycle();
        if (type == 0) {
            tuiguang(TUIGUAN_MAIN_SOFT, true, this);
            tuiguang(TUIGUAN_MAIN, false, this);
        } else if (type == 1) {
            tuiguang(TUIGUAN_SIDE_SOFT, true, this);
            tuiguang(TUIGUAN_SIDE, false, this);
        } else if (type == 2) {
            tuiguang(TUIGUAN_SETTING_SOFT, true, this);
            tuiguang(TUIGUAN_SETTING, false, this);
        } else if (type == 3) {
            tuiguang(TUIGUAN_SUCCESS_SOFT, true, this);
            tuiguang(TUIGUAN_SUCCESS, false, this);
        }

    }

    public void tuiguang(final String tag, final boolean isSoftCross, View viewP) {
        ArrayList<CrossItem> crossItems = JsonParser.getCrossData(mContext, AndroidSdk.getExtraData(), tag);
        if (crossItems != null) {
            for (int i = 0; i < crossItems.size(); i++) {
                final CrossItem item = crossItems.get(i);
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_tuiguang_main, null);
                final int j = i + 4;
                String t = "主界面";
                if (tag.equals(TUIGUAN_MAIN) || tag.equals(TUIGUAN_MAIN_SOFT)) {
                    t = "主界面";
                    view = LayoutInflater.from(mContext).inflate(R.layout.layout_tuiguang_main, null);
                    TextView tuiguang_sub_title = (TextView) view.findViewById(R.id.tuiguang_subtitle);
                    tuiguang_sub_title.setText(item.getSubTitle());
                    ImageView image = (ImageView) view.findViewById(R.id.tuiguang_icon);
                    Util.loadImg(mContext, item.getTagIconUrl(), R.mipmap.icon, image);
                    tuiguangZhanshi(isSoftCross, item, t, j);
                } else if (tag.equals(TUIGUAN_SIDE) || tag.equals(TUIGUAN_SIDE_SOFT)) {
                    t = "侧边栏";
                    view = LayoutInflater.from(mContext).inflate(R.layout.layout_tuiguang_side, null);
                    tuiguangZhanshi(isSoftCross, item, t, j);
                    ImageView image = (ImageView) view.findViewById(R.id.tuiguang_icon);
                    Util.loadImg(mContext, item.getTagIconUrl(), R.mipmap.icon, image);
                } else if (tag.equals(TUIGUAN_SETTING) || tag.equals(TUIGUAN_SETTING_SOFT)) {
                    t = "设置";
                    view = LayoutInflater.from(mContext).inflate(R.layout.layout_tuiguang_setting, null);
                    tuiguangZhanshi(isSoftCross, item, t, j);
                } else if (tag.equals(TUIGUAN_SUCCESS) || tag.equals(TUIGUAN_SUCCESS_SOFT)) {
                    t = "清理";
                    view = LayoutInflater.from(mContext).inflate(R.layout.layout_tuiguang_main, null);
                    TextView tuiguang_sub_title = (TextView) view.findViewById(R.id.tuiguang_subtitle);
                    tuiguang_sub_title.setText(item.getSubTitle());
                    ImageView image = (ImageView) view.findViewById(R.id.tuiguang_icon);
                    Util.loadImg(mContext, item.getTagIconUrl(), R.mipmap.icon, image);
                    tuiguangZhanshi(isSoftCross, item, t, j);
                }

                TextView tuiguang_title = (TextView) view.findViewById(R.id.tuiguang_title);
                tuiguang_title.setText(item.getTitle());

                final String finalT = t;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isSoftCross) {
                            if (Utils.checkoutISAppHasInstalled(mContext, item.getPkgName())) {
                                try {
                                    Intent intent = new Intent(item.getAction());
                                    mContext.startActivity(intent);
                                } catch (Exception e) {
                                    Log.e("tuiguang", "not find action=" + item.getAction());
                                    Utils.openPlayStore(mContext, item.getPkgName(), AndroidSdk.getExtraData());
                                }
                            } else {
                                if (TextUtils.equals(item.action, Constant.RAM_CLEAN_ACTION)) {
                                    //内存加速
                                    Intent intent = new Intent(mContext, com.mutter.cleanmodule.activity.RamAvtivity.class);
                                    mContext.startActivity(intent);
                                    AdUtil.track("交叉推广_广告位", "广告位_" + finalT, "点击" + item.getPkgName(), 1);
                                } else if (TextUtils.equals(item.action, Constant.JUNK_CLEAN_ACTION)) {
                                    //垃圾清理
                                    Intent intent = new Intent(mContext, LajiActivity.class);
                                    mContext.startActivity(intent);
                                    AdUtil.track("交叉推广_广告位", "广告位_" + finalT, "点击" + item.getPkgName(), 1);
                                } else if (TextUtils.equals(item.action, Constant.BATTERY_COOL_ACTION)) {
                                    //电池降温
                                    Intent intent = new Intent(mContext, TemperatureDownActivity.class);
                                    mContext.startActivity(intent);
                                    AdUtil.track("交叉推广_广告位", "广告位_" + finalT, "点击" + item.getPkgName(), 1);
                                } else {
                                    Utils.openPlayStore(mContext, item.getPkgName(), AndroidSdk.getExtraData());
                                }
                            }
                        } else {
                            Utils.reactionForAction(mContext, AndroidSdk.getExtraData(), item.getPkgName(), item.getAction());
                            AdUtil.track("交叉推广_广告位", "广告位_" + finalT, "点击" + item.getPkgName(), 1);
                        }
                    }
                });
                ((LinearLayout) viewP).addView(view);
            }

        }

    }

    public void tuiguangZhanshi(boolean isSoftCross, CrossItem item, String t, int j) {
        if (isSoftCross) {
            if (TextUtils.equals(item.action, Constant.RAM_CLEAN_ACTION)) {
                AdUtil.track("交叉推广_广告位", "广告位_" + t, "展示" + item.pkgName, 1);
            } else if (TextUtils.equals(item.action, Constant.JUNK_CLEAN_ACTION)) {
                //垃圾清理
                AdUtil.track("交叉推广_广告位", "广告位_" + t, "展示" + item.pkgName, 1);
            } else if (TextUtils.equals(item.action, Constant.BATTERY_COOL_ACTION)) {
                //电池降温
                AdUtil.track("交叉推广_广告位", "广告位_" + t, "展示" + item.pkgName, 1);
            }
        } else {
            AdUtil.track("交叉推广_广告位", "广告位_" + t, "展示" + item.pkgName, 1);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

}
