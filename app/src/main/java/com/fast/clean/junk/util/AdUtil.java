package com.fast.clean.junk.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.fast.clean.junk.BuildConfig;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.purchase.InAppPurchase;
import com.google.android.gms.ads.purchase.InAppPurchaseListener;

/**
 * Created by on 2017/5/24.
 */

public class AdUtil {

    public static void track(String category, String action, String label, int value) {
        if (BuildConfig.TRACK) {
//            AndroidSdk.track(category, action, label, value);
        }
    }

    static InterstitialAd mInterstitialAd;

    public static void loadFull(final Context context) {
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3150670592875039/8705751307");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Log.e("adadad", "onAdClosed");
                loadFull(context);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.e("adadad", "onAdLoaded");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.e("adadad", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                mInterstitialAd.zzd(false);
                Log.e("adadad", "onAdLeftApplication");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("adadad", "onAdFailedToLoad");
            }
        });
        requestNewInterstitial();
    }

    public static void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public static void showFull(Context context) {
        if (mInterstitialAd != null) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                requestNewInterstitial();
            }
        } else {
            loadFull(context);
        }
        // Play for a while, then display the New Game Button
    }

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
//        if (!AndroidSdk.hasNativeAd(tag, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
//            Log.e("rqy", "getAdView null,because not configuration tag =" + tag);
//            return null;
//        }
        View nativeView = null;
//                AndroidSdk.peekNativeAdViewWithLayout(tag, AndroidSdk.NATIVE_AD_TYPE_ALL, layout, null);
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
