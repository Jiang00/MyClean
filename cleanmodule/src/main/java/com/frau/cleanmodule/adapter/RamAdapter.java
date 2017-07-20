package com.frau.cleanmodule.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.frau.cleanmodule.R;
import com.frau.cleanmodule.db.CleanDBHelper;
import com.frau.cleanmodule.entity.JunkInfo;
import com.frau.cleanmodule.util.LoadManager;
import com.frau.cleanmodule.util.Util;
import com.frau.cleanmodule.view.RamPresenter;

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
        white_list = CleanDBHelper.getInstance(context).getWhiteList(CleanDBHelper.TableType.Ram);
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
    public void upList(List<JunkInfo> list) {
        super.upList(list);
        white_list = CleanDBHelper.getInstance(context).getWhiteList(CleanDBHelper.TableType.Ram);
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
            holder.size_danwei = (TextView) convertView
                    .findViewById(R.id.size_danwei);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (info.label == null) {
            info.label = LoadManager.getInstance(context).getAppLabel(info.pkg);
        }
        holder.name.setText(info.label);
        holder.icon.setImageDrawable(LoadManager.getInstance(context).getAppIcon(info.pkg));
        if (white_list.contains(info.pkg)) {
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
                    holder.checkBox.setImageResource(R.mipmap.ram_normal);
                    ramPresenter.addCleandata(false, info.size);
                }
            }
        });
        holder.size.setText(Util.convertStorage(info.size, false));
        holder.size_danwei.setText(Util.convertStorageDanwei(info.size));

        return convertView;
    }


    public class ViewHolder {
        TextView name;
        ImageView icon;
        ImageView checkBox;
        TextView size;
        TextView size_danwei;
    }

    public interface AllListener {
        void onChecked(boolean check);
    }
}
