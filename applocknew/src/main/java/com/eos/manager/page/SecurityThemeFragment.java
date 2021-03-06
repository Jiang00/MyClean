package com.eos.manager.page;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.*;

//import com.android.client.AndroidSdk;
//import com.android.client.ClientNativeAd;
import com.android.client.AndroidSdk;
import com.android.client.ClientNativeAd;
import com.privacy.lock.R;
import com.eos.manager.App;
import com.eos.manager.meta.SecurityTheBridge;
import com.eos.manager.lib.Utils;

import com.eos.manager.myinterface.ISecurityBridge;

/**
 * Created by huale on 2014/11/20.
 */
public class SecurityThemeFragment extends Fragment {
    public static final String TAG_UNLOCK = "unlock";
    public static final String TAG_TLEF_AD = "Leftmenu";
    public static final String TAG_TOP_AD = "TopLocklist";
    public static final String TAG_MAIN_PAGE_FULL = "slock";
    public static final String TAG_UNLOCK_FULL = "unlock_full";


    public static View adView = null;


    public interface ICheckResult {
        void onSuccess();

        void unLock();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctrl = new OverflowCtrl();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        afterViewCreated(view, ctrl);

    }

    public static void afterViewCreated(View view, OverflowCtrl ctrl) {
        try {
            createAdView((ViewGroup) view);
        } catch (Exception e) {
            Log.e("testback", "exception----" + e);

            e.printStackTrace();
        }
        setupTitle(view);
    }


    public static void setupTitle(View v) {
        ISecurityBridge bridge = SecurityTheBridge.bridge;
        TextView appName = new TextView(v.getContext());
        appName.setTag("realAppName");
        appName.setText(bridge.appName());
        appName.setTextSize(21);
        appName.setTextColor(0xffffffff);
        appName.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.getDimens(v.getContext(), 48));
        lp.leftMargin = Utils.getDimens(v.getContext(), 8);
        ((ViewGroup) v).addView(appName, lp);
        appName.setAlpha(0);
        TextView appname = (TextView) v.findViewWithTag("text_appname");
        ImageView icon = (ImageView) v.findViewWithTag("title");
        ImageView statusicon = (ImageView) v.findViewWithTag("app_icon");
        appname.setText(bridge.appName());
        icon.setBackgroundDrawable(bridge.icon());


        if (adView != null) {
            icon.setVisibility(View.GONE);
            appname.setVisibility(View.GONE);
            statusicon.setBackgroundDrawable(bridge.icon());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ViewGroup group = (ViewGroup) getView();
        if (group != null) {
            group.removeAllViewsInLayout();
            View v = onCreateView(LayoutInflater.from(getActivity()), group, null);
            group.addView(v);
            onViewCreated(v, null);
        }
    }

    protected static Animation out, in;
    protected OverflowCtrl ctrl;

    @Override
    public void onDestroyView() {
        ctrl.hideOverflow = null;
        if (ctrl.overflowStub != null) {
            ctrl.overflowStub.removeAllViews();
        }
        ctrl.overflowStub = null;
        ctrl.ovf = null;
        ctrl = null;
        ViewGroup group = (ViewGroup) getView();
        if (group != null) {
            group.removeAllViews();
        }
        super.onDestroyView();
        try {
            AndroidSdk.destroyNativeAdView(TAG_UNLOCK, adView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected static void createAdView(ViewGroup view) {
        if (AndroidSdk.hasNativeAd(TAG_UNLOCK)) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            Point size = Utils.getScreenSize(view.getContext());
            if (size.y < 854) {
                layoutParams.topMargin = Utils.getDimens(view.getContext(), 32);
            } else {
                layoutParams.topMargin = Utils.getDimens(view.getContext(), 48);
            }

            adView = AndroidSdk.peekNativeAdScrollViewWithLayout(TAG_UNLOCK,
                    AndroidSdk.HIDE_BEHAVIOR_AUTO_HIDE,
                    AndroidSdk.getDefaultNativeLayoutId(false), null, null);
//            adView = AndroidSdk.peekNativeAdScrollViewWithLayout(TAG_UNLOCK, AndroidSdk.NATIVE_AD_TYPE_ALL, AndroidSdk.HIDE_BEHAVIOR_AUTO_HIDE, R.layout.security_native_layout, new ClientNativeAd.NativeAdClickListener() {
//                @Override
//                public void onNativeAdClicked(ClientNativeAd clientNativeAd) {
//
//                }
//            }, new ClientNativeAd.NativeAdScrollListener() {
//                @Override
//                public void onNativeAdScrolled(float v) {
////                    icon.setAlpha(1 - v);
////                    appName.setAlpha(1 - v);
////                    realAppName.setAlpha(v);
//                }
//            });
            if (adView != null) {
                view.addView(adView, layoutParams);
            }
        }

    }


    public static MyFrameLayout inflate(String layoutId, ViewGroup container, Context c) {
//      Context themeContext = ThemeManager.currentTheme().getThemeContext();
        LayoutInflater inflater = LayoutInflater.from(c);
        int layout = c.getResources().getIdentifier(layoutId, "layout", c.getPackageName());
        MyFrameLayout v = (MyFrameLayout) inflater.inflate(layout, container, false);

//        ViewStub forbidden = new ViewStub(ApplicationModule.getModule().provideContext(), R.layout.forbidden);
//        v.addView(forbidden);
//        final ForbiddenView forbiddenView = new ForbiddenView(forbidden);
//        v.setTag(forbiddenView);
//        v.post(new Runnable() {
//            @Override
//            public void run() {
//                ApplicationModule.getModule().provideHandler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        forbiddenView.init();
//                    }
//                });
//            }
//        });
        return v;
    }


}
