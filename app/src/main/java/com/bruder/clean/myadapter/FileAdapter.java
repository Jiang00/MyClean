package com.bruder.clean.myadapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleaner.filemanager.UtilsFile;
import com.cleaner.filemanager.Util;
import com.cleaner.util.LoadManager;
import com.bruder.clean.junk.R;
import com.bruder.clean.entity.JunkInfo;

/**
 */

public class FileAdapter extends MybaseAdapter<JunkInfo> {
    String name;

    public FileAdapter(Context context) {
        super(context);
    }

    public FileAdapter(Context context, String name) {
        super(context);
        this.name = name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_file_second_item, parent, false);
            viewHolder.file = (RelativeLayout) convertView.findViewById(R.id.file);
            viewHolder.file_icon = (ImageView) convertView.findViewById(R.id.file_icon);
            viewHolder.file_name = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.file_time = (TextView) convertView.findViewById(R.id.file_time);
            viewHolder.file_size = (TextView) convertView.findViewById(R.id.file_size);
            viewHolder.file_check = (ImageView) convertView.findViewById(R.id.file_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.equals("apk", name)) {
            Drawable drawable = LoadManager.getInstance(context).getApkIconforPath(info.path);
            if (drawable == null) {
                drawable = ContextCompat.getDrawable(context, R.mipmap.file_apk_icon);
            }
            viewHolder.file_icon.setImageDrawable(drawable);
        } else if (TextUtils.equals("zip", name)) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_zip_icon);
        } else if (TextUtils.equals("music", name)) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_music_icon);
        } else if (TextUtils.equals("video", name)) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_video_icon);
        } else if (TextUtils.equals("other", name)) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_other_icon);
        } else if (TextUtils.equals("picture", name)) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_picture_icon);
        } else {
            viewHolder.file_icon.setImageDrawable(info.icon);
        }
        viewHolder.file_name.setText(info.name);
        viewHolder.file_size.setText(Util.convertStorage(info.size));
        viewHolder.file_time.setText(com.cleaner.util.Util.getStrTime(info.date));
        if (info.isChecked) {
            viewHolder.file_check.setImageResource(R.mipmap.ram_passed);
        } else {
            viewHolder.file_check.setImageResource(R.mipmap.ram_normal);
        }
        viewHolder.file_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.isChecked) {
                    viewHolder.file_check.setImageResource(R.mipmap.ram_normal);
                    info.isChecked = false;
                } else {
                    info.isChecked = true;
                    viewHolder.file_check.setImageResource(R.mipmap.ram_passed);
                }
            }
        });
        viewHolder.file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsFile.openFile(context, info.path);
            }
        });
        return convertView;
    }

    class ViewHolder {
        RelativeLayout file;
        ImageView file_icon;
        TextView file_name,file_time;
        TextView file_size;
        ImageView file_check;
    }
}
