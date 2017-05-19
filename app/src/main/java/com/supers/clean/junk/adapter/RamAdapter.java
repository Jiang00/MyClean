package com.supers.clean.junk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.clean.util.Util;
import com.supers.clean.junk.R;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.presenter.RamPresenter;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;

import java.util.List;

public class RamAdapter extends MybaseAdapter<JunkInfo> {
    AllListener listener;
    RamPresenter ramPresenter;
    private List<String> white_list;
    private AlertDialog dialog;

    public RamAdapter(Context context) {
        super(context);
    }

    public RamAdapter(Context context, RamPresenter ramPresenter) {
        super(context);
        this.ramPresenter = ramPresenter;
        white_list = PreData.getNameList(context, Constant.WHILT_LIST);

    }

    public void setOnlistener(AllListener listener) {
        this.listener = listener;
    }

    @Override
    public void upList(List<JunkInfo> list) {
        super.upList(list);
        white_list = PreData.getNameList(context, Constant.WHILT_LIST);
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
        if (white_list.contains(info.packageName)) {
            info.isChecked = false;
        }
        if (info.isChecked) {
            holder.checkBox.setImageResource(R.mipmap.ram_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.ram_normal);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            boolean isAdd;

            @Override
            public void onClick(View v) {
                info.isChecked = !info.isChecked;
                if (info.isChecked) {
                    isAdd = true;
                    holder.checkBox.setImageResource(R.mipmap.ram_passed);
                    ramPresenter.addCleandata(true, info.size);
                } else {
                    if (!isAdd) {
                        showDialog(info.icon, info.name, info.packageName);
                    }
                    holder.checkBox.setImageResource(R.mipmap.ram_normal);
                    ramPresenter.addCleandata(false, info.size);
                }
            }
        });
        holder.size.setText(Util.convertStorage(info.size, true));

        return convertView;
    }

    private void showDialog(Drawable drawable, String label, final String pkg) {
        View view = View.inflate(context, R.layout.dialog_file, null);
        ImageView dialog_icon = (ImageView) view.findViewById(R.id.dialog_icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);
        dialog_icon.setImageDrawable(drawable);
        title.setText(label);
        message.setText(context.getString(R.string.add_white, label));
        dialog = new AlertDialog.Builder(context).create();
        dialog.setView(view);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ramPresenter.addWhiteList(pkg);
                PreData.addName(context, pkg, Constant.WHILT_LIST);
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
