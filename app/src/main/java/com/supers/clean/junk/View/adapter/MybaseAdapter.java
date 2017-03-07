package com.supers.clean.junk.View.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MybaseAdapter<T> extends BaseAdapter {
    List<T> list;
    protected Context context;
    protected LayoutInflater inflater;

    public MybaseAdapter(Context context) {
        super();
        this.context = context;
        list = new ArrayList<T>();
        inflater = LayoutInflater.from(context);
//		inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<T> getData() {
        return list;
    }

    public T getData(int location) {
        return list.get(location);
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

    public void removeData(T t) {
        this.list.remove(t);
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
}
