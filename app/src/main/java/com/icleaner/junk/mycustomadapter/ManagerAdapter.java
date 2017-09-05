package com.icleaner.junk.mycustomadapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icleaner.clean.utils.LoadManager;
import com.icleaner.junk.R;
import com.icleaner.clean.utils.MyUtils;
import com.icleaner.clean.entity.JunkInfo;
import com.icleaner.junk.mypresenter.ManagerPresenter;

public class ManagerAdapter extends MybaseAdapter<JunkInfo> {
    ManagerPresenter managerPresenter;
    AllListener listener;
    String identification;

    public ManagerAdapter(Context context) {
        super(context);
    }

    public ManagerAdapter(Context context, ManagerPresenter managerPresenter, String identification) {
        super(context);
        this.identification = identification;
        this.managerPresenter = managerPresenter;
        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 1024) / 4) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 返回用户定义的item的大小，默认返回1代表item的数量.重写此方法来衡量每张图片的大小。
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public ManagerAdapter(Context context, ManagerPresenter managerPresenter) {
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
        if ("time".equals(identification)) {
            holder.time.setText(MyUtils.getStrTime2(info.date));
        } else {
            holder.time.setText(MyUtils.getStrTime3(info.date));
        }
        holder.size.setText(MyUtils.convertStorage(info.allSize, true));

        Drawable icon = LoadManager.getInstance(context).getAppIcon(info.pkg);
        holder.icon.setImageDrawable(icon);
        if ("size".equals(identification)) {
            holder.time.setVisibility(View.GONE);
            holder.size.setVisibility(View.VISIBLE);
        } else {
            holder.time.setVisibility(View.VISIBLE);
            holder.size.setVisibility(View.GONE);
        }
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