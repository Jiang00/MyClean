package com.froumobic.module.charge.saver.Util;

import android.view.View;
import android.view.ViewGroup;

import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.froumobic.module.charge.saver.R;

/**
 * Created by on 2016/11/9.
 */
public class ADRequest {

    public static void showFullAD() {
        AndroidSdk.showFullAd(AndroidSdk.FULL_TAG_PAUSE);
    }

    public View showCustomNativeAD(String adTag, int layoutID, final ICustomNativeADClicked adClick) {
        View adView = null;
        if (AndroidSdk.hasNativeAd(adTag, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
            adView = AndroidSdk.peekNativeAdViewWithLayout(adTag,
                    AndroidSdk.NATIVE_AD_TYPE_ALL, R.layout.native_ad_2_mopub,
                    null);
        }
        return adView;
    }

    public interface ICustomNativeADClicked {
        void onNativeADClicked(ClientNativeAd clientNativeAd);
    }

}
