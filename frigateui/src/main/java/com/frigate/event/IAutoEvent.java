package com.frigate.event;

import android.view.View;
import android.view.ViewGroup;

import com.frigate.parser.FrigateData;

/**
 * Created by renqingyou on 2017/2/16.
 */

public interface IAutoEvent {
    void onClickEvent(View v, ViewGroup viewGroup, FrigateData frigateData);

    boolean onLongClickEvent(View v, ViewGroup viewGroup, FrigateData frigateData);
}
