package com.mutter.clean.junk.presenter;

import android.content.Context;
import android.widget.TextView;

import com.mutter.clean.core.CleanManager;
import com.mutter.clean.junk.view.JunkView;
import com.mutter.clean.entity.JunkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/2.
 */

public class JunkPresenter extends BasePresenter<JunkView> {
    private long allSize;

    public JunkPresenter(JunkView iView, Context context) {
        super(iView, context);
    }

    @Override
    public void init() {
        super.init();
        iView.loadFullAd();
        allSize = CleanManager.getInstance(context).getApkSize() + CleanManager.getInstance(context).getCacheSize() + CleanManager.getInstance(context).getUnloadSize() + CleanManager.getInstance(context).getLogSize()
                + CleanManager.getInstance(context).getDataSize();
        iView.initData(allSize);
        iView.onClick();
    }


    @Override
    public void setUnit(long size, TextView textView) {
        super.setUnit(size, textView);

    }

    public void bleachFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CleanManager.getInstance(context).clearSystemCache();
                CleanManager.getInstance(context).removeAllApkFiles();
                CleanManager.getInstance(context).removeAllFilesOfUnintalledApk();
                CleanManager.getInstance(context).removeAllAppLog();
                CleanManager.getInstance(context).removeAllAppCache();
            }
        }).start();
        iView.cleanAnimation(allSize);
    }

}
