package com.mutter.clean.junk.myview.conpentview;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.frigate.utils.AutoUtils;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.entity.TuiguangInfo;
import com.mutter.clean.junk.myActivity.MainActivity;
import com.mutter.clean.junk.myActivity.NotifiActivity;
import com.mutter.clean.junk.myActivity.NotifiAnimationActivity;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.UtilGp;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.mutter.ui.demo.entry.CrossItem;
import com.mutter.ui.demo.util.JsonParser;

import java.util.ArrayList;

/**
 * Created by ${} on 2018/1/24.
 */

public class GrildRecycleView extends RecyclerView {
    Context mContext;
    HuiAdapter adapter;

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
    private int list_item;

    public GrildRecycleView(Context context) {
        this(context, null);
    }

    public GrildRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GrildRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        setBackageShape(attrs);
    }

    public void setData(ArrayList<TuiguangInfo> list) {
        if (adapter == null) {
            setLayoutManager(new GridLayoutManager(mContext, 3));
            setAdapter(adapter = new HuiAdapter(list));
        } else {
            adapter.setData(list);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ArrayList<TuiguangInfo> tuiguangList = new ArrayList<>();
        ArrayList<CrossItem> crossItems_soft = JsonParser.getCrossData(mContext, AndroidSdk.getExtraData(), "main_soft");
        if (crossItems_soft != null) {
            for (int i = 0; i < crossItems_soft.size(); i++) {
                CrossItem item = crossItems_soft.get(i);
                TuiguangInfo info = new TuiguangInfo();
                info.action = item.action;
                info.packageName = item.getPkgName();
                info.title = item.title;
                info.url = item.getTagIconUrl();
                tuiguangList.add(info);
                AdUtil.track("交叉推广_广告位", "广告位_主界面", "展示" + info.packageName, 1);
            }
        }
        ArrayList<CrossItem> crossItems = JsonParser.getCrossData(mContext, AndroidSdk.getExtraData(), "main_hard");
        if (crossItems != null) {
            for (int i = 0; i < crossItems.size(); i++) {
                CrossItem item = crossItems.get(i);
                TuiguangInfo info = new TuiguangInfo();
                info.action = item.action;
                info.packageName = item.getPkgName();
                info.title = item.title;
                info.url = item.getTagIconUrl();
                tuiguangList.add(info);
                AdUtil.track("交叉推广_广告位", "广告位_主界面", "展示" + info.packageName, 1);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(mContext, Constant.NOTIFI_KAIGUAN, 1) == 1) {
            TuiguangInfo info = new TuiguangInfo();
            info.title = mContext.getString(R.string.side_notifi);
            info.drable_id = R.mipmap.tuiguang_notifi;
            tuiguangList.add(info);
        }
        TuiguangInfo info = new TuiguangInfo();
        info.title = mContext.getString(R.string.side_rotate);
        info.drable_id = R.mipmap.tuiguang_rotate;
        tuiguangList.add(info);
        setData(tuiguangList);
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
        TypedArray ta_2 = this.mContext.obtainStyledAttributes(attrs, R.styleable.AutoRecycleView);
        list_item = ta_2.getResourceId(R.styleable.AutoRecycleView_listItem, -1);
        ta_2.recycle();

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


    class HuiAdapter extends RecyclerView.Adapter<HuiAdapter.HomeViewHolder> {
        ArrayList<TuiguangInfo> list;

        public HuiAdapter(ArrayList<TuiguangInfo> list) {
            this.list = list;
        }

        public void setData(ArrayList<TuiguangInfo> list) {
            this.list = list;
        }

        public HuiAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HuiAdapter.HomeViewHolder holder = new HuiAdapter.HomeViewHolder(LayoutInflater.from(
                    mContext).inflate(list_item == -1 ? R.layout.layout_tuiguang_item : list_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final HuiAdapter.HomeViewHolder holder, final int position) {
            final TuiguangInfo info = list.get(position);
            holder.recyc_name.setText(info.title);
            if (info.drable_id == -1) {
                holder.recyc_ad.setVisibility(View.VISIBLE);
                Util.loadImg(mContext, info.url, R.mipmap.icon, holder.recyc_icon);
                holder.recycle_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        com.mutter.ui.demo.util.Utils.reactionForAction(mContext, AndroidSdk.getExtraData(), info.packageName, info.action);
                        AdUtil.track("交叉推广_广告位", "广告位_主界面", "点击" + info.packageName, 1);
                    }
                });
            } else if (info.drable_id == R.mipmap.tuiguang_notifi) {
                holder.recyc_ad.setVisibility(View.INVISIBLE);
                holder.recyc_icon.setImageResource(info.drable_id);
                holder.recycle_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Util.isNotificationListenEnabled(mContext) || !PreData.getDB(mContext, Constant.KEY_NOTIFI, true)) {
                            Intent intent6 = new Intent(mContext, NotifiAnimationActivity.class);
                            startActivityForResult(intent6, 1);
                        } else {
                            Intent intent6 = new Intent(mContext, NotifiActivity.class);
                            startActivityForResult(intent6, 1);
                        }
                    }
                });
            } else {
                holder.recyc_ad.setVisibility(View.INVISIBLE);
                holder.recyc_icon.setImageResource(info.drable_id);
                holder.recycle_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdUtil.track("主页面", "点击方格好评", "", 1);
                        UtilGp.rate(mContext);
                        PreData.putDB(mContext, Constant.IS_ROTATE, true);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }

        class HomeViewHolder extends RecyclerView.ViewHolder {
            ImageView recyc_icon;
            TextView recyc_name;
            TextView recyc_ad;
            LinearLayout recycle_item;

            public HomeViewHolder(View view) {
                super(view);
                AutoUtils.autoSize(itemView);
                recycle_item = (LinearLayout) view.findViewById(R.id.recycle_item);
                recyc_icon = (ImageView) view.findViewById(R.id.recyc_icon);
                recyc_name = (TextView) view.findViewById(R.id.recyc_name);
                recyc_ad = (TextView) view.findViewById(R.id.recyc_ad);
            }
        }
    }
}
