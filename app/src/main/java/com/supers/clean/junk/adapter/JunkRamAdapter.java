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
import com.supers.clean.junk.presenter.JunkRamPresenter;

public class JunkRamAdapter extends MybaseAdapter<JunkInfo> {
    AllListener listener;
    JunkRamPresenter junkPresenter;

    public JunkRamAdapter(Context context) {
        super(context);
    }

    public JunkRamAdapter(Context context, JunkRamPresenter junkPresenter) {
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
        if (info.type == JunkInfo.TableType.APKFILE) {
            holder.name.setText(info.label);
            Drawable icon = LoadManager.getInstance(context).getApkIconforPath(info.path);
            holder.icon.setImageDrawable(icon);
        } else if (info.type == JunkInfo.TableType.APP) {
            holder.name.setText(LoadManager.getInstance(context).getAppLabel(info.pkg));
            Drawable icon = LoadManager.getInstance(context).getAppIcon(info.pkg);
            holder.icon.setImageDrawable(icon);
        } else {
            holder.name.setText(info.label);
            holder.icon.setImageResource(R.mipmap.log_file);
        }
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
        holder.size.setText(Util.convertStorage(info.size, true));

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
