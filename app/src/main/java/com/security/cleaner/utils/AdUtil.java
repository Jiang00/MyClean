package com.security.cleaner.utils;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.android.client.AndroidSdk;
import com.security.cleaner.BuildConfig;

/**
 * Created by on 2017/5/24.
 */

public class AdUtil {
    public static final String DEFAULT = "clean_full";
    public static final String SIZE_FULL = "full";
    public static final String SIZE_LARGE = "large";
    public static final String SIZE_SMALL = "small";

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
