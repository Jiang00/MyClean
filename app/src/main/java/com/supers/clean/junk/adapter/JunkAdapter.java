package com.supers.clean.junk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.android.clean.util.Util;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.presenter.JunkPresenter;

public class JunkAdapter extends MybaseAdapter<JunkInfo> {
    AllListener listener;
    JunkPresenter junkPresenter;

    public JunkAdapter(Context context) {
        super(context);
    }

    public JunkAdapter(Context context, JunkPresenter junkPresenter) {
        super(context);
        this.junkPresenter = junkPresenter;
    }

    public void setOnlistener(AllListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater
                    .inflate(R.layout.layout_junk_ram_item, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.junk_item_lable);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.junk_item_icon);
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.check_iv);
            holder.size = (TextView) convertView
                    .findViewById(R.id.size_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(info.name);
        Drawable icon = info.icon;
        holder.icon.setImageDrawable(icon);
        if (info.isChecked) {
            holder.checkBox.setImageResource(R.mipmap.junk_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.junk_normal);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isChecked = !info.isChecked;
                if (info.isChecked) {
                    holder.checkBox.setImageResource(R.mipmap.junk_passed);
                    junkPresenter.addCleandata(true, info.size);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.junk_normal);
                    junkPresenter.addCleandata(false, info.size);
                }
            }
        });
        holder.size.setText(Util.convertStorage(info.size,true) );

        return convertView;
    }

    public class ViewHolder {
        TextView name;
        ImageView icon;
        ImageView checkBox;
        TextView size;
    }

    public interface AllListener {
        void onChecked(boolean check);
    }
}
