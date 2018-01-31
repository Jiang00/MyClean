package com.mutter.clean.junk.myview.conpentview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.client.AndroidSdk;
import com.frigate.layout.FrigateLinearLayout;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myActivity.LoadingActivity;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.util.PreData;

import org.json.JSONObject;

/**
 * Created by ${} on 2018/1/30.
 */

public class AutoNativeAdView extends FrigateLinearLayout {
    public AutoNativeAdView(Context context) {
        this(context, null);
    }

    public AutoNativeAdView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoNativeAdView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.AutoNativeAdView);
        int ad_layout = ta.getResourceId(R.styleable.AutoNativeAdView_ad_layout, -1);
        String ad_tag = ta.getString(R.styleable.AutoNativeAdView_ad_tag);
        String ad_switch = ta.getString(R.styleable.AutoNativeAdView_ad_switch);
        ta.recycle();
        if (ad_tag == null) {
            ad_tag = AdUtil.NATIVE_DEFAULT;
        }
        if (ad_switch != null) {
            try {
                JSONObject jsonObject = new JSONObject(AndroidSdk.getExtraData());
                if (jsonObject.has(ad_switch)) {
                    if (jsonObject.getInt(ad_switch) == 0) {
                        return;
                    }
                }
            } catch (Exception e) {
            }

        }
        if (ad_layout == -1) {
            ad_layout = R.layout.native_ad_2;
        }
        View nativeView_side = getNativeAdView(ad_tag, ad_layout);
        if (nativeView_side != null) {
            addView(nativeView_side);
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    public static View getNativeAdView(String tag, @LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd(tag)) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout(tag, layout, null);
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
}
