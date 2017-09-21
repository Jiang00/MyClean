package com.fraumobi.call.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fraumobi.call.entries.RejectInfo;
import com.fraumobi.call.record.R;

/**
 * Created by ${} on 2017/8/23.
 */

public class CallAdapter extends MybaseAdapter<RejectInfo> {

    public CallAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RejectInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater
                    .inflate(R.layout.layout_call_item, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.call_name);
            holder.time = (TextView) convertView
                    .findViewById(R.id.call_time);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.call_icon);
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.call_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(info.name);
        holder.time.setText(info.date);
        holder.icon.setImageResource(R.mipmap.call_icon);
        if (info.isChecked) {
            holder.checkBox.setImageResource(R.mipmap.checked);
        } else {
            holder.checkBox.setImageResource(R.mipmap.no_checked);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView icon;
        TextView time;
        ImageView checkBox;
        TextView name;
    }
}