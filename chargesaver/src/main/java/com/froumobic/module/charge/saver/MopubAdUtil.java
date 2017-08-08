package com.froumobic.module.charge.saver;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNativeAdLoadedListener;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

import java.util.EnumSet;

/**
 * Created by Ivy on 2017/8/3.
 */

public class MopubAdUtil {
    public static String BATTERY_NATIVE = "824580ce77c64b94a785eda64aa5dbb1";
    private static MoPubAdAdapter mAdAdapter;

    public static void creat2(Activity activity, ListView listView) {
        final ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_2_mopub).mainImageId(R.id.ad_image).iconImageId(R.id.ad_icon).titleId(R.id.ad_title)
                .textId(R.id.ad_subtitle).callToActionId(R.id.ad_action).privacyInformationIconImageId(R.id.ad_choices).build();
        MoPubNativeAdPositioning.MoPubServerPositioning adPositioning =
                MoPubNativeAdPositioning.serverPositioning();
        MoPubStaticNativeAdRenderer adRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
        ArrayAdapter<String> adapterad = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1);
        // Set up the MoPubAdAdapter
        adapterad.add("");
        mAdAdapter = new MoPubAdAdapter(activity, adapterad, adPositioning);
        mAdAdapter.registerAdRenderer(adRenderer);
        listView.setAdapter(mAdAdapter);
    }

    public static void loadad(String adId) {
        EnumSet desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                // Don't pull the ICON_IMAGE
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);

        RequestParameters myRequestParameters = new RequestParameters.Builder()
                .desiredAssets(desiredAssets)
                .build();
        // Request ads when the user returns to this activity.
        if (mAdAdapter != null) {
            mAdAdapter.loadAds(adId, myRequestParameters);//11a17b188668469fb0412708c3d16813
            mAdAdapter.setAdLoadedListener(new MoPubNativeAdLoadedListener() {
                @Override
                public void onAdLoaded(int position) {

                }

                @Override
                public void onAdRemoved(int position) {
                }
            });
        }
    }

    public static void distory() {
        if (mAdAdapter != null) {
            mAdAdapter.destroy();
        }
    }
}
