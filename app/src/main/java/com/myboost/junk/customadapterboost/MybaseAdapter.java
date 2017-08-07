package com.myboost.junk.customadapterboost;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MybaseAdapter<T> extends BaseAdapter {
    protected LayoutInflater inflater;
    List<T> list;
    protected Context context;
    protected LruCache lruCache;

    public MybaseAdapter(Context context) {
        super();
        this.context = context;
        list = new ArrayList<T>();
        inflater = LayoutInflater.from(context);
//		inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public T getData(int location) {
        return list.get(location);
    }


    public void removeData(T t) {
        this.list.remove(t);
    }
    public void addData(T t) {
        list.add(t);
    }

    public void addData(int location, T t) {
        list.add(location, t);
    }

    public void addDataList(List<T> list) {
        this.list.addAll(list);
    }

    public List<T> getData() {
        return list;
    }

    public void removeDataLocation(int location) {
        this.list.remove(location);
    }

    public void removaDataList(List<T> list) {
        this.list.removeAll(list);
    }

    public void upList(List<T> list) {
        this.list = list;
    }

    public void clear() {
        list.clear();
    }

    public void undateData() {
        notifyDataSetChanged();
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    public void setList(List<T> list) {
        // TODO Auto-generated method stub
        this.list = list;
    }

    /**
     * @param key    传入图片的key值，一般用图片url代替
     * @param bitmap 要缓存的图片对象
     */
    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            if (bitmap == null) {
                return;
            } else {

                lruCache.put(key, bitmap);
            }
        }
    }

    public void addLabelToCache(String key, String label) {
        if (getBitmapFromCache(key) == null) {
            if (label == null) {
                return;
            } else {

                lruCache.put(key, label);
            }
        }
    }

    /**
     * @param key 要取出的bitmap的key值
     * @return 返回取出的bitmap
     */
    public Bitmap getBitmapFromCache(String key) {
        if (lruCache != null)
            return (Bitmap) lruCache.get(key);
        else
            return null;
    }

    public String getLabelFromCache(String key) {
        if (lruCache != null)
            return (String) lruCache.get(key);
        else
            return null;
    }
}
