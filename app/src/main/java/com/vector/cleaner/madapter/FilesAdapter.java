package com.vector.cleaner.madapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vector.cleaner.entity.FileInfo;
import com.vector.mcleaner.file.FileUtil;
import com.vector.mcleaner.file.FileUtils;
import com.vector.mcleaner.mutil.LoadManager;
import com.vector.cleaner.R;

/**
 */

public class FilesAdapter extends MybaseAdapter<FileInfo> {
    String name;

    public FilesAdapter(Context context) {
        super(context);
    }

    public FilesAdapter(Context context, String name) {
        super(context);
        this.name = name;
    }

    class ViewHolder {
        TextView file_name;
        RelativeLayout file;
        TextView file_size;
        ImageView file_icon;
        ImageView file_check;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileInfo info = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_file_second_item, parent, false);
            viewHolder.file = (RelativeLayout) convertView.findViewById(R.id.file);
            viewHolder.file_icon = (ImageView) convertView.findViewById(R.id.file_icon);
            viewHolder.file_name = (TextView) convertView.findViewById(R.id.file_name);
            viewHolder.file_size = (TextView) convertView.findViewById(R.id.file_size);
            viewHolder.file_check = (ImageView) convertView.findViewById(R.id.file_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.equals("apk", name)) {
            Drawable drawable = LoadManager.getInstance(context).getApkIconforPath(info.path);
            if (drawable == null) {
                drawable = ContextCompat.getDrawable(context, R.mipmap.file_apk_button);
            }
            viewHolder.file_icon.setImageDrawable(drawable);
        } else if (TextUtils.equals("zip", name)) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_zip_button);
        } else if (TextUtils.equals("music", name)) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_music_button);
        } else if (TextUtils.equals("video", name)) {
            viewHolder.file_icon.setImageResource(R.mipmap.file_video_button);
        } else {
            viewHolder.file_icon.setImageDrawable(info.icon);
        }
        viewHolder.file_name.setText(info.name);
        viewHolder.file_size.setText(FileUtil.convertStorage(info.size));
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
                FileUtils.openFile(context, info.path);
            }
        });
        return convertView;
    }


}
