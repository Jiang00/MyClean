package com.eos.manager;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by superjoy on 2014/8/29.
 */
public class ViewHolder {

    ImageView icon;

    TextView appName;

    View encrypted;



    long id = 0;



    public ViewHolder() {
    }

    public ViewHolder(View root) {
        root.setTag(this);
    }
}
