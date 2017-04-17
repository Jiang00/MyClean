package com.supers.clean.junk.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.entity.NotifiInfo;
import com.supers.clean.junk.util.CommonUtil;

/**
 * Created by Ivy on 2017/4/17.
 */

public class NotifiAdapter extends MybaseAdapter<NotifiInfo> {
    public NotifiAdapter(Context context) {
        super(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NotifiInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_notifi_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.iv_le);
            holder.notifi_title = (TextView) convertView
                    .findViewById(R.id.notifi_title);
            holder.notifi_text = (TextView) convertView
                    .findViewById(R.id.notifi_text);
            holder.time = (TextView) convertView
                    .findViewById(R.id.notifi_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (info.icon != null) {
            holder.icon.setImageDrawable(info.icon);
        }
        if (!TextUtils.isEmpty(info.title)) {
            holder.notifi_title.setText(info.title);
        }
        if (!TextUtils.isEmpty(info.subTitle)) {
            holder.notifi_text.setText(info.subTitle);
        }
        holder.time.setText(CommonUtil.getStrTime2(info.time));
        return convertView;
    }

    public class ViewHolder {
        ImageView icon;
        TextView notifi_title;
        TextView notifi_text;
        TextView time;
    }
}
