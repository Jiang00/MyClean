package com.easy.junk.easypresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.easy.junk.easytools.EasyUtilGp;

/**
 */

public class PresenterBase<T> implements EasyIPresenter {
    T iView;
    private int cpuTemp = 40;
    Context context;

    public PresenterBase(T iView, Context context) {
        this.iView = iView;
        this.context = context;
    }


    public void jumpToActivity(Class<?> classs, int requestCode) {
        Intent intent = new Intent(context, classs);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
    @Override
    public void init() {

    }

    public void jumpToActivity(Class<?> classs, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, classs);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public void goToGooglePlay() {
        EasyUtilGp.rate(context);
    }

    public void setUnit(long size, TextView textView) {

    }

}
