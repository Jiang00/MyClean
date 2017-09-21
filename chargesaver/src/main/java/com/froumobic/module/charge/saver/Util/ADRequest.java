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
        AndroidSdk.showFullAd("clean_full");
    }

    public View showCustomNativeAD(String adTag, int layoutID, final ICustomNativeADClicked adClick) {
        View adView = null;
        if (AndroidSdk.hasNativeAd(adTag)) {
            adView = AndroidSdk.peekNativeAdViewWithLayout(adTag,
                    R.layout.native_ad_2_mopub,
                    null);
        }
        return adView;
    }

    public interface ICustomNativeADClicked {
        void onNativeADClicked(ClientNativeAd clientNativeAd);
    }

}
