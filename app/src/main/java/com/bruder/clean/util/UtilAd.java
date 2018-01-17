package com.bruder.clean.util;

import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.client.AndroidSdk;
import com.bruder.clean.junk.BuildConfig;

public class UtilAd {
    public static final String DEFAULT_FULL = "clean_full";
    public static final String NATIVE_FULL = "full";
    public static final String NATIVE_LARGE = "large";
    public static final String NATIVE_SMALL = "small";

    public static void track(String category, String action, String label, int value) {
        if (BuildConfig.TRACK) {
            AndroidSdk.track(category, action, label, value);
        }
    }

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd("clean_native")) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout("clean_native", layout, null);
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

    public static void showBanner() {
        AndroidSdk.showBanner("clean_banner", 4);
    }

    public static void closeBanner() {
        AndroidSdk.closeBanner("clean_banner");
    }

}
