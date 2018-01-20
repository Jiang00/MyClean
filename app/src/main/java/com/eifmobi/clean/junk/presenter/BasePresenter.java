package com.eifmobi.clean.junk.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.eifmobi.clean.junk.util.UtilGp;

/**
 */

public class BasePresenter<T> implements IPresenter {
    T iView;
    Context context;

    public BasePresenter(T iView, Context context) {
        this.iView = iView;
        this.context = context;
    }

    @Override
    public void init() {

    }

    public void jumpToActivity(Class<?> classs, int requestCode) {
        Intent intent = new Intent(context, classs);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public void jumpToActivity(Class<?> classs, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, classs);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public void goToGooglePlay() {
        UtilGp.rate(context);
    }

    public void setUnit(long size, TextView textView) {

    }

}