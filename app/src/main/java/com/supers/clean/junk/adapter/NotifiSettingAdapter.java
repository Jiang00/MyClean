package com.supers.clean.junk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;

/**
 * Created by Ivy on 2017/4/17.
 */

public class NotifiSettingAdapter extends MybaseAdapter<JunkInfo> {
    public NotifiSettingAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_white_list_item, null);
            holder = new ViewHolder();
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.iv_check);
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);
            holder.iv_icon = (ImageView) convertView
                    .findViewById(R.id.iv_le);
            holder.tv_lable = (TextView) convertView
                    .findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_icon.setImageDrawable(info.icon);
        holder.tv_lable.setText(info.label);
        holder.checkBox.setClickable(false);
        if (!info.isnotifiWhiteList) {
            holder.checkBox.setImageResource(R.mipmap.side_check_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.side_check_normal);
        }
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!info.isnotifiWhiteList) {
                    info.isnotifiWhiteList = true;
                    PreData.addName(context, info.packageName, Constant.NOTIFI_WHILT_LIST);
                    holder.checkBox.setImageResource(R.mipmap.side_check_normal);
                } else {
                    info.isnotifiWhiteList = false;
                    PreData.removeName(context, info.packageName, Constant.NOTIFI_WHILT_LIST);
                    holder.checkBox.setImageResource(R.mipmap.side_check_passed);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        RelativeLayout rl_item;
        ImageView checkBox;
        ImageView iv_icon;
        TextView tv_lable;
    }
}
