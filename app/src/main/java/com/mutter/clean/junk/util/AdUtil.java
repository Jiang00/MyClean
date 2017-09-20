package com.mutter.clean.junk.util;

import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.client.AndroidSdk;
import com.mutter.clean.junk.BuildConfig;

/**
 */

public class AdUtil {
    public static final String FULL_DEFAULT = "mutter_full";

    public static void track(String category, String action, String label, int value) {
        if (BuildConfig.TRACK) {
            AndroidSdk.track(category, action, label, value);
        }
    }

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd("mutter_native")) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout("mutter_native", layout, null);
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
