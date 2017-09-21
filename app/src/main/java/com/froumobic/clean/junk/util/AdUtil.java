package com.froumobic.clean.junk.util;

import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.client.AndroidSdk;
import com.froumobic.clean.junk.BuildConfig;

/**
 * Created by froumobi on 2017/5/24.
 */

public class AdUtil {

    public static final String DEFAULT_FULL = "clean_full";

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
            FrameLayout ad_image = (FrameLayout) nativeView.findViewWithTag("ad_image");
            if (ad_image != null) {
                View child = ad_image.getChildAt(0);
                if (child != null && child instanceof ImageView)
                    ((ImageView) child).setScaleType(ImageView.ScaleType.FIT_CENTER);
            }

        }
        return nativeView;
    }
}
