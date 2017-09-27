package com.icleaner.junk.mytools;

import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.android.client.AndroidSdk;
import com.icleaner.junk.BuildConfig;

/**
 * Created by on 2017/5/24.
 */

public class SetAdUtil {
    public static final String DEFAULT_FULL = "clean_full";

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

    public static void track(String category, String action, String label, int value) {
        if (BuildConfig.TRACK) {
            AndroidSdk.track(category, action, label, value);
        }
    }
}
