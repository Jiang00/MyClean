package com.eos.module.charge.saver.Util;

import android.view.View;

import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.eos.module.charge.saver.R;

/**
 * Created by Ivy on 2016/11/9.
 */
public class ADRequest {

    public static void showFullAD() {
        AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
    }

    public View showCustomNativeAD(String adTag, int layoutID, final ICustomNativeADClicked adClick) {
        View adView = null;
        if (AndroidSdk.hasNativeAd(adTag, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
            adView = AndroidSdk.peekNativeAdViewWithLayout(adTag,
                    AndroidSdk.NATIVE_AD_TYPE_ALL, R.layout.native_ad,
                    new ClientNativeAd.NativeAdClickListener() {
                        @Override
                        public void onNativeAdClicked(ClientNativeAd clientNativeAd) {
                            if (adClick != null) {
                                adClick.onNativeADClicked(clientNativeAd);
                            }
                        }
                    });
        }
        return adView;
    }

    public interface ICustomNativeADClicked {
        void onNativeADClicked(ClientNativeAd clientNativeAd);
    }

}
