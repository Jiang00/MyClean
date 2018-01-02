package com.security.cleaner.madapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.security.cleaner.presenter.ManagerPresenter;
import com.security.mcleaner.mutil.LoadManager;
import com.security.cleaner.R;
import com.security.mcleaner.mutil.Util;
import com.security.mcleaner.entity.JunkInfo;

public class AppAdapter extends MybaseAdapter<JunkInfo> {
    AllListener listener;
    ManagerPresenter managerPresenter;

    public AppAdapter(Context context) {
        super(context);
    }

    public AppAdapter(Context context, ManagerPresenter managerPresenter) {
        super(context);
        this.managerPresenter = managerPresenter;
        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 4) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 返回用户定义的item的大小，默认返回1代表item的数量.重写此方法来衡量每张图片的大小。
                return bitmap.getByteCount() / 1024;
            }
        };
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
        holder.time.setText(Util.getStrTime3(info.date));
        holder.size.setText(Util.convertStorage(info.allSize, true));

        Drawable icon = LoadManager.getInstance(context).getAppIcon(info.pkg);
        holder.icon.setImageDrawable(icon);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managerPresenter.bleachFile(info);
            }
        });


        return convertView;
    }

    public class ViewHolder {
        ImageView icon;
        TextView time;
        ImageView checkBox;
        TextView name;
        TextView size;
    }

    public interface AllListener {
        void onChecked(boolean check);
    }
}
