package com.supers.clean.junk.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.filemanager.FileUtils;
import com.android.clean.filemanager.MultipleItem;
import com.android.clean.filemanager.Util;
import com.android.clean.util.LoadManager;
import com.supers.clean.junk.R;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.service.FileIconHelper;

import java.util.List;

/**
 * Created by Ivy on 2017/4/21.
 */

public class FilesdAdapter extends MybaseAdapter<MultipleItem> {
    List<MultipleItem> name;

    public FilesdAdapter(Context context) {
        super(context);
    }

    public FilesdAdapter(Context context, List<MultipleItem> name) {
        super(context);
        this.name = name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MultipleItem info = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_file_second_item, parent, false);
            viewHolder.file = (RelativeLayout) convertView.findViewById(R.id.file);
            viewHolder.file_icon = (ImageView) convertView.findViewById(R.id.file_icon);
            viewHolder.file_name = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.file_size = (TextView) convertView.findViewById(R.id.file_size);
            viewHolder.file_check = (ImageView) convertView.findViewById(R.id.file_check);
            viewHolder.file_right = (ImageView) convertView.findViewById(R.id.file_right);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (info.getData().isDir) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_dir_icon);
            viewHolder.file_right.setVisibility(View.VISIBLE);
            viewHolder.file_check.setVisibility(View.GONE);
            viewHolder.file_size.setText(context.getString(R.string.file_num, info.getData().count));
        } else {
            viewHolder.file_right.setVisibility(View.GONE);
            viewHolder.file_check.setVisibility(View.VISIBLE);
            FileIconHelper.setIcon(info.getData().filePath, viewHolder.file_icon);
            if (info.getData().isSelected) {
                viewHolder.file_check.setImageResource(R.mipmap.ram_passed);
            } else {
                viewHolder.file_check.setImageResource(R.mipmap.ram_normal);
            }

            viewHolder.file_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (info.getData().isSelected) {
                        viewHolder.file_check.setImageResource(R.mipmap.ram_normal);
                        info.getData().isSelected = false;
                    } else {
                        info.getData().isSelected = true;
                        viewHolder.file_check.setImageResource(R.mipmap.ram_passed);
                    }
                }
            });
            viewHolder.file_size.setText(Util.convertStorage(info.getData().fileSize));
        }
        viewHolder.file_name.setText(info.getData().fileName);


//        viewHolder.file.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (info.getData().isDir) {
//
//                } else {
//                }
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        RelativeLayout file;
        ImageView file_icon;
        TextView file_name;
        TextView file_size;
        ImageView file_check;
        ImageView file_right;
    }
}
