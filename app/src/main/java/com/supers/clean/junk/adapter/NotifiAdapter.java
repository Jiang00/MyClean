package com.supers.clean.junk.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.clean.notification.NotificationInfo;
import com.android.clean.util.Util;
import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/4/17.
 */

public class NotifiAdapter extends MybaseAdapter<NotificationInfo> {
    public NotifiAdapter(Context context) {
        super(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NotificationInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_notifi_item, null);
            holder = new ViewHolder();
            holder.frameLayout = (FrameLayout) convertView
                    .findViewById(R.id.fl);
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
        if (!TextUtils.isEmpty(info.title) && !TextUtils.isEmpty(info.subTitle)) {
            holder.notifi_title.setText(info.title);
            holder.notifi_text.setText(info.subTitle);
            holder.time.setText(Util.getStrTime2(info.time));
            holder.frameLayout.setVisibility(View.GONE);
        } else if (info.remoteViews != null) {
            try {
                View view = info.remoteViews.apply(context, holder.frameLayout);
                holder.frameLayout.removeAllViews();
                holder.frameLayout.addView(view);
                holder.frameLayout.setVisibility(View.VISIBLE);
            } catch (Exception e) {
            }
        }
        Log.e("adapter", info.pkg + "=" + info.title);
        return convertView;

    }

    public class ViewHolder {
        FrameLayout frameLayout;
        ImageView icon;
        TextView notifi_title;
        TextView notifi_text;
        TextView time;
    }
}
