package com.eos.module.charge.saver.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.eos.module.charge.saver.R;

/**
 * Created by on 2016/11/9.
 */
public class ADRequest {

    public View showCustomNativeAD(Context context, String adTag, int layoutID, final ICustomNativeADClicked adClick) {
        if (PreData.getDB(context, Constant.BILL_YOUXIAO, true)) {
            return null;
        }
        View adView = null;
        if (AndroidSdk.hasNativeAd(adTag)) {
            adView = AndroidSdk.peekNativeAdViewWithLayout(adTag,
                    R.layout.native_ad,
                    new ClientNativeAd.NativeAdClickListener() {
                        @Override
                        public void onNativeAdClicked(ClientNativeAd clientNativeAd) {
                            if (adClick != null) {
                                adClick.onNativeADClicked(clientNativeAd);
                            }
                        }
                    });
            if (adView != null) {
                View ad_image = adView.findViewWithTag("ad_image");
                ad_image.setClickable(false);
                ad_image.setOnClickListener(null);
                if (ad_image instanceof ViewGroup) {
                    final int childCount = ((ViewGroup) ad_image).getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        ((ViewGroup) ad_image).getChildAt(i).setClickable(false);
                    }
                }
                View ad_title = adView.findViewWithTag("ad_title");
                ad_title.setClickable(false);
                ad_title.setOnClickListener(null);
                if (ad_title instanceof ViewGroup) {
                    final int childCount = ((ViewGroup) ad_title).getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        ((ViewGroup) ad_title).getChildAt(i).setClickable(false);
                    }
                }
                View ad_subtitle = adView.findViewWithTag("ad_desc");
                ad_subtitle.setClickable(false);
                ad_subtitle.setOnClickListener(null);
                if (ad_subtitle instanceof ViewGroup) {
                    final int childCount = ((ViewGroup) ad_subtitle).getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        ((ViewGroup) ad_subtitle).getChildAt(i).setClickable(false);
                    }
                }
            }
        }
        return adView;
    }

    public interface ICustomNativeADClicked {
        void onNativeADClicked(ClientNativeAd clientNativeAd);
    }

}
