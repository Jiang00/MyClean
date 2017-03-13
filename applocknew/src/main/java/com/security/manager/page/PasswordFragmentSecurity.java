package com.security.manager.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.privacy.lock.R;
import com.security.manager.App;

import com.security.manager.SecuritySettingsAdvance;
import com.security.manager.Tools;
import com.security.manager.Tracker;
import com.security.manager.meta.SecurityTheBridge;
import com.security.manager.myinterface.ISecurityBridge;

/**
 * Created by huale on 2014/11/19.
 */


public class PasswordFragmentSecurity extends SecurityThemeFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return passwd = getView(inflater, container, ctrl, new ICheckResult() {
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
    public View passwd;
    public static View getView(LayoutInflater inflater, ViewGroup container, OverflowCtrl ctrl, final ICheckResult callback) {
        final ISecurityBridge bridge = SecurityTheBridge.bridge;
        inflater = SecurityTheBridge.themeContext == null ? inflater : LayoutInflater.from(SecurityTheBridge.themeContext);
        View v = inflate("security_number_password", container, inflater.getContext());
        ((MyFrameLayout) v).setOverflowCtrl(ctrl);
        final NumberDot dot = (NumberDot) v.findViewWithTag("passwd_dot_id");
        dot.init(new NumberDot.ICheckListener() {
            @Override
            public void match(String passwd) {
                if (bridge.check(passwd, true)) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                }
            }
        });
        dot.reset();

//        ViewStub forbidden = new ViewStub(App.getContext(), R.layout.security_myforbidden);
//        ErrorBiddenView errorBiddenView=new ErrorBiddenView(forbidden);
//        dot.setErrorBiddenView(errorBiddenView);
//        ((MyFrameLayout) v).addView(forbidden);
//        errorBiddenView.init();

        v.findViewById(R.id.setting_advance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ISecurityBridge bridge = SecurityTheBridge.bridge;
                    if (bridge != null) {
                        if (bridge.appName().equals(R.string.app_name)) {
                            Intent intent = new Intent(v.getContext(), SecuritySettingsAdvance.class);
                            intent.putExtra("launchname", bridge + "");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            App.getContext().startActivity(intent);
                        } else {
                            Intent intent = new Intent(v.getContext(), SecuritySettingsAdvance.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            App.getContext().startActivity(intent);

                        }
                    } else {
                        Intent intent = new Intent(v.getContext(), SecuritySettingsAdvance.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        App.getContext().startActivity(intent);

                    }


                    callback.unLock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        v.findViewById(R.id.use_pattern).setVisibility(View.GONE);


        v.findViewById(R.id.backspace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dot.backSpace();
            }
        });
        String [] buttons = {
                "button0", "button1", "button2", "button3", "button4",
                "button5", "button6", "button7", "button8", "button9",
        };
        Tools.RandomNumpad(bridge, v, buttons);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dot.setNumber(((Button) v).getText().charAt(0));
            }
        };
        for (String btn : buttons) {
            v.findViewWithTag(btn).setOnClickListener(clickListener);
        }

        v.setOnClickListener(ctrl.hideOverflow);

        v.findViewWithTag("number_cancel").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), SecuritySettingsAdvance.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                App.getContext().startActivity(intent);
//                callback.unLock();
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        if (passwd != null) {
            ViewGroup group = (ViewGroup) getView();
            if (group != null) {
                group.removeView(passwd);
            }
            passwd = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (passwd != null) {
            ((NumberDot) passwd.findViewWithTag("passwd_dot_id")).reset();
        }
    }
}
