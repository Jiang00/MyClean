package com.supers.clean.junk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.presenter.RamPresenter;

public class RamAdapter extends MybaseAdapter<JunkInfo> {
    AllListener listener;
    RamPresenter ramPresenter;

    public RamAdapter(Context context) {
        super(context);
    }

    public RamAdapter(Context context, RamPresenter ramPresenter) {
        super(context);
        this.ramPresenter = ramPresenter;
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
                    .inflate(R.layout.layout_ram_item, null);
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
            holder.checkBox.setImageResource(R.mipmap.ram_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.ram_normal);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isChecked = !info.isChecked;
                if (info.isChecked) {
                    holder.checkBox.setImageResource(R.mipmap.ram_passed);
                    ramPresenter.addCleandata(true, info.size);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.ram_normal);
                    ramPresenter.addCleandata(false, info.size);
                }
            }
        });
        holder.size.setText(CommonUtil.getFileSize(info.size));

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
