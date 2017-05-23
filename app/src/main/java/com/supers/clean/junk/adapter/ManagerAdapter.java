package com.supers.clean.junk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.clean.util.LoadManager;
import com.supers.clean.junk.R;
import com.android.clean.util.Util;
import com.android.clean.entity.JunkInfo;
import com.supers.clean.junk.presenter.ManagerPresenter;

public class ManagerAdapter extends MybaseAdapter<JunkInfo> {
    AllListener listener;
    ManagerPresenter managerPresenter;

    public ManagerAdapter(Context context) {
        super(context);
    }

    public ManagerAdapter(Context context, ManagerPresenter managerPresenter) {
        super(context);
        this.managerPresenter = managerPresenter;
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
                    .inflate(R.layout.layout_manager_item, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.junk_item_lable);
            holder.time = (TextView) convertView
                    .findViewById(R.id.item_time);
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
        holder.name.setText(info.label);
        holder.time.setText(Util.getStrTime(info.date));
        holder.size.setText(Util.convertStorage(info.allSize, true));

        Drawable icon = LoadManager.getInstance(context).getAppIcon(info.pkg);
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
                    managerPresenter.addCleandata(true, info.allSize);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.ram_normal);
                    managerPresenter.addCleandata(false, info.allSize);
                }
            }
        });


        return convertView;
    }

    public class ViewHolder {
        TextView name;
        ImageView icon;
        ImageView checkBox;
        TextView size;
        TextView time;
    }

    public interface AllListener {
        void onChecked(boolean check);
    }
}
