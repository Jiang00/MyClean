package com.fast.clean.junk.util;

import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.client.AndroidSdk;
import com.fast.clean.junk.BuildConfig;

/**
 * Created by on 2017/5/24.
 */

public class AdUtil {

    public static void track(String category, String action, String label, int value) {
        if (BuildConfig.TRACK) {
            AndroidSdk.track(category, action, label, value);
        }
    }

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd(tag, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
            Log.e("rqy", "getAdView null,because not configuration tag =" + tag);
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout(tag, AndroidSdk.NATIVE_AD_TYPE_ALL, layout, null);
        if (nativeView == null) {
            Log.e("rqy", "getAdView null,because peek native ad = null");
            return null;
        }

        if (nativeView != null) {
//            ViewParent viewParent = nativeView.getParent();
//            if (viewParent != null && viewParent instanceof ViewGroup) {
//                ((ViewGroup) viewParent).removeAllViews();
//            }
            ViewGroup viewParent = (ViewGroup) nativeView.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
            }
        }
        return nativeView;
    }
}
