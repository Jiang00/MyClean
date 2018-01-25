package com.frigate.event;

import android.view.View;
import android.view.ViewGroup;

import com.frigate.parser.FrigateData;

/**
 * Created by renqingyou on 2017/2/17.
 */

public class FrigateEventListenImp implements FrigateEventListener {
    @Override
    public void onClickEvent(View v, ViewGroup viewGroup, FrigateData frigateData) {

    }

    @Override
    public boolean onLongClickEvent(View v, ViewGroup viewGroup, FrigateData frigateData) {
        return false;
    }
}
