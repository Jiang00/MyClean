package com.mutter.clean.junk.myAdapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutter.clean.notifi.NotificationInfo;
import com.mutter.clean.util.Util;
import com.mutter.clean.junk.R;

/**
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
            holder.rl_sys = (RelativeLayout) convertView
                    .findViewById(R.id.sys);
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
            holder.frameLayout.removeAllViews();
            holder.frameLayout.setVisibility(View.GONE);
            holder.rl_sys.setVisibility(View.VISIBLE);
        } else if (info.remoteViews != null) {
            View view = info.remoteViews.apply(context, holder.frameLayout);
            holder.frameLayout.addView(view);
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.rl_sys.setVisibility(View.GONE);
        }
        Log.e("adapter", info.pkg + "=" + info.title);
        return convertView;

    }

    public class ViewHolder {
        FrameLayout frameLayout;
        RelativeLayout rl_sys;
        ImageView icon;
        TextView notifi_title;
        TextView notifi_text;
        TextView time;
    }
}
