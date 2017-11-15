package com.supers.clean.junk.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.BuildConfig;
import com.supers.clean.junk.activity.MainActivity;

/**
 * Created by Ivy on 2017/5/24.
 */

public class AdUtil {
    public static String DEFAULT = "result_full";

    public static void track(String category, String action, String label, int value) {
        if (BuildConfig.TRACK) {
            AndroidSdk.track(category, action, label, value);
        }
    }

    public static View getNativeAdView(Context context, String tag, @LayoutRes int layout) {
        if (PreData.getDB(context, Constant.BILL_YOUXIAO, true)) {
            return null;
        }
        if (!AndroidSdk.hasNativeAd("eos_native")) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout("eos_native", layout, null);
        if (nativeView == null) {
            return null;
        }

        if (nativeView != null) {
            ViewGroup viewParent = (ViewGroup) nativeView.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
            }
        }
        return nativeView;
    }

    public static View getNativeAdViewV(Context context, String tag, @LayoutRes int layout) {
        if (PreData.getDB(context, Constant.BILL_YOUXIAO, true)) {
            return null;
        }
        if (!AndroidSdk.hasNativeAd(tag)) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout(tag, layout, null);
        if (nativeView == null) {
            return null;
        }

        if (nativeView != null) {
            ViewGroup viewParent = (ViewGroup) nativeView.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
            }
        }
        return nativeView;
    }
}
