package com.eos.manager.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.eos.manager.AppLockSettingsAdvance;
import com.privacy.lock.R;
import com.eos.manager.App;
import com.eos.manager.Tracker;
import com.eos.manager.meta.SecurityTheBridge;
import com.eos.manager.myinterface.ISecurityBridge;

import java.util.List;


/**
 * Created by huale on 2014/11/21.
 */
public class PatternFragmentSecurity extends SecurityThemeFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return pattern = getView(inflater, container, ctrl, new ICheckResult() {
            @Override
            public void onSuccess() {
                getActivity().finish();
                Tracker.sendEvent(Tracker.ACT_UNLOCK,Tracker.CATE_ACTION_UNLOCK_SUSSFUL,Tracker.CATE_ACTION_UNLOCK_SUSSFUL,1);
            }

            @Override
            public void unLock() {

            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (pattern != null) {
            ((SecurityPatternView) pattern.findViewById(R.id.lpv_lock)).resetPattern();
        }
    }

    @Override
    public void onDestroyView() {
        if (pattern != null) {
            ViewGroup group = (ViewGroup) getView();
            if (group != null) {
                group.removeView(pattern);
            }
            pattern = null;
        }
        super.onDestroyView();
    }

    View pattern;

    public static View getView(LayoutInflater inflater, final ViewGroup container, OverflowCtrl ctrl, final ICheckResult callback) {
        inflater = SecurityTheBridge.themeContext == null ? inflater : LayoutInflater.from(SecurityTheBridge.themeContext);
        View v = inflate("security_pattern_view", container, inflater.getContext());
        ((MyFrameLayout) v).setOverflowCtrl(ctrl);
        final ImageButton button = (ImageButton) v.findViewWithTag("setting_advance");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    ISecurityBridge bridge = SecurityTheBridge.bridge;

                    if (bridge != null) {
                        if (bridge.appName().equals(R.string.app_name)) {
                            Intent intent = new Intent(v.getContext(), AppLockSettingsAdvance.class);
                            intent.putExtra("launchname", bridge + "");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            App.getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(v.getContext(), AppLockSettingsAdvance.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            App.getContext().startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(v.getContext(), AppLockSettingsAdvance.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        App.getContext().startActivity(intent);
                    }
                    callback.unLock();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        final ISecurityBridge bridge = SecurityTheBridge.bridge;
        final SecurityPatternView lock = (SecurityPatternView) v.findViewById(R.id.lpv_lock);
        LinearLayout parent = (LinearLayout) lock.getParent();
        ((LinearLayout.LayoutParams) parent.getLayoutParams()).weight = 1.5f;
        parent.requestLayout();

        v.findViewWithTag("number_cancel").setVisibility(View.GONE);

//        final ViewStub forbidden = new ViewStub(App.getContext(), R.layout.security_myforbidden);
//        ((MyFrameLayout) v).addView(forbidden);
//        final ErrorBiddenView errorBiddenView = new ErrorBiddenView(forbidden);
//        errorBiddenView.init();

        lock.setOnPatternListener(new SecurityPatternView.OnPatternListener() {
            public void onPatternStart() {
            }

            public void onPatternDetected(List<SecurityPatternView.Cell> pattern) {
                if (!bridge.check(LockPatternUtils.patternToString(pattern), false)) {
//                    errorBiddenView.wrong();
                    lock.setDisplayMode(SecurityPatternView.DisplayMode.Wrong);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lock.clearPattern();
                        }
                    }, 500);
                } else {
//                    errorBiddenView.right();
                    callback.onSuccess();
                }
            }

            public void onPatternCleared() {
            }

            public void onPatternCellAdded(List<SecurityPatternView.Cell> pattern) {
            }
        });



        lock.clearPattern();
        v.setOnClickListener(ctrl.hideOverflow);


        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.security_setting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
