package com.supers.clean.junk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.clean.filemanager.FileUtils;
import com.android.clean.util.LoadManager;
import com.supers.clean.junk.R;
import com.android.clean.util.Util;
import com.android.clean.entity.JunkInfo;
import com.supers.clean.junk.activity.JunkActivity;
import com.supers.clean.junk.presenter.JunkPresenter;

public class JunkAdapter extends MybaseAdapter<JunkInfo> {
    AllListener listener;
    JunkPresenter junkPresenter;

    public JunkAdapter(Context context) {
        super(context);
    }

    public JunkAdapter(Context context, JunkPresenter junkPresenter) {
        super(context);
        this.junkPresenter = junkPresenter;
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
//
            holder.icon.setImageDrawable(LoadManager.getInstance(context).getApkIconforPath(info.path));
        } else if (info.type == JunkInfo.TableType.APP) {
            if (info.label == null) {
                info.label = LoadManager.getInstance(context).getAppLabel(info.pkg);
            }
            holder.name.setText(info.label);
            holder.icon.setImageDrawable(LoadManager.getInstance(context).getAppIcon(info.pkg));
        } else {
            holder.name.setText(info.label);
            Drawable icon = LoadManager.getInstance(context).getAppIcon(info.pkg);
            holder.icon.setImageResource(R.mipmap.file_txt_icon);
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
                    junkPresenter.addCleandata(true, info.size);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.ram_normal);
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

    class DrawableTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
