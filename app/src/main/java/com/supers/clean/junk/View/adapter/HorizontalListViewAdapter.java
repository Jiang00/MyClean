package com.supers.clean.junk.View.adapter;

/**
 * Created by Ivy on 2016/12/1.
 */


import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.modle.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;


public class HorizontalListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    Bitmap iconBitmap;
    private int selectIndex = -1;
    List<JunkInfo> list;
    public ActivityManager am;

    public HorizontalListViewAdapter(Context context) {
        this.mContext = context;
        list = new ArrayList<JunkInfo>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
        am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    public void addData(JunkInfo entity) {
        list.add(entity);
    }

    public void addDataList(List<JunkInfo> ls) {
        list.addAll(ls);
    }

    public void clear() {
        list.clear();
    }

    public List<JunkInfo> getData() {
        return list;
    }

    public void removeData(int location) {
        list.remove(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final JunkInfo info = list.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
            holder.mImage = (ImageView) convertView.findViewById(R.id.img_list_item);
            holder.mTitle = (TextView) convertView.findViewById(R.id.text_list_item);
            holder.delete_app = (ImageView) convertView.findViewById(R.id.delete_app);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitle.setText(info.name);
        iconBitmap = getPropThumnail(info.icon);
        holder.mImage.setImageBitmap(iconBitmap);
        holder.delete_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                am.killBackgroundProcesses(info.packageName);
                notifyDataSetChanged();
                Log.e("aaa", "delete_app======");
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle;
        private ImageView mImage;
        private ImageView delete_app;
    }

    private Bitmap getPropThumnail(Drawable drawable) {
        Bitmap bitmap = null;
        bitmap = ((BitmapDrawable) drawable).getBitmap();
        return bitmap;
    }
}