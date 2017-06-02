package com.supers.clean.junk.mview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class PagerAdapter<String> {

    protected List<String> mList;

    protected OnDataChangeListener mDataChangeListener;

    protected LayoutInflater mInflater;

    public PagerAdapter(Context context, ArrayList<String> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    public View getView(int position) {
        String item = mList.get(position);
        View view = mInflater.inflate(getItemLayoutId(position, item), null);
        onBindView(view, item);
        return view;
    }

    public abstract int getItemLayoutId(int position, String item);

    public abstract void onBindView(View view, String item);

    public int getCount() {
        return mList.size();
    }

    public Context getContext() {
        return mInflater.getContext();
    }

    public void exchange(int oldPosition, int newPosition) {
        String item = mList.get(oldPosition);
        mList.remove(oldPosition);
        mList.add(newPosition, item);
    }

    public void setOnDataChangeListener(OnDataChangeListener dataChangeListener) {
        this.mDataChangeListener = dataChangeListener;
    }

    public void delete(int position) {
        if (position < getCount()) {
            mList.remove(position);
            dispatchDataChange();
        }
    }

    private void dispatchDataChange() {
        mDataChangeListener.onDataChange();
    }

    public void add(String item) {
        if (mList.add(item)) {
            dispatchDataChange();
        }
    }

    public String getItem(int position) {
        return mList.get(position);
    }

    public int indexOf(Object item) {
        return mList.indexOf(item);
    }

    public interface OnDataChangeListener {
        void onDataChange();
    }
}
