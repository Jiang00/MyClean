package com.eifmobi.clean.junk.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.android.client.AndroidSdk;
import com.eifmobi.clean.junk.BuildConfig;
import com.eifmobi.clean.junk.R;

/**
 */

public class AdUtil {
    public static final String FULL_DEFAULT = "clean_full";//
    public static final String NATIVE_SMALL = "small";//
    public static final String NATIVE_LARGE = "large";//
    public static final String NATIVE_FULL = "full";//

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

    public static void startBannerAnimation(Context context, final View view) {
        view.setVisibility(View.VISIBLE);
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        view.measure(w, h);
//        int height = view.getMeasuredHeight();
//
//        ValueAnimator animator = ValueAnimator.ofInt(context.getResources().getDimensionPixelOffset(R.dimen.d20), height);
//        animator.setDuration(500);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int h = (int) animation.getAnimatedValue();
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
//                layoutParams.height = h;
//                view.setLayoutParams(layoutParams);
//            }
//        });
//        animator.start();
    }

    public static void showBanner() {
        AndroidSdk.showBanner("clean_banner", 4);
    }

    public static void closeBanner() {
        AndroidSdk.closeBanner("clean_banner");
    }
}
