package com.myboost.junk.presenterprivacy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.myboost.junk.toolsprivacy.PrivacyUtilGp;

/**
 */

public class PresenterBasePrivacy<T> implements PrivacyIPresenter {
    T iView;
    private int cpuTemp = 40;
    Context context;

    public PresenterBasePrivacy(T iView, Context context) {
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
        PrivacyUtilGp.rate(context);
    }

    public void setUnit(long size, TextView textView) {

    }

}
