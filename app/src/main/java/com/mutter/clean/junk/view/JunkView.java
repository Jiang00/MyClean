package com.mutter.clean.junk.view;

import android.widget.TextView;


import com.mutter.clean.entity.JunkInfo;

import java.util.List;

/**
 */

public interface JunkView extends IView {

    void initData(long allSize);

    void cleanAnimation(long cleanSize);

}
