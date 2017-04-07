package com.supers.clean.junk.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.supers.clean.junk.R;

/**
 * Created by Ivy on 2017/4/6.
 */

public class PowerView {
    private LayoutInflater inflater;
    private Context context;
    private View view;

    public PowerView(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        init();
    }

    public void init() {
        view = inflater.inflate(R.layout.layout_power, null);
    }
}
