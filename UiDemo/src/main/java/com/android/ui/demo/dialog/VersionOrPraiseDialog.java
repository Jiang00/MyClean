package com.android.ui.demo.dialog;

import android.content.Context;

import com.android.ui.demo.UiManager;
import com.android.ui.demo.util.WrapNullPointException;
import com.android.ui.demo.util.Utils;

public final class VersionOrPraiseDialog extends ContentDialogCommonParent {

    public VersionOrPraiseDialog(Context context, Builder.DialogType type, Builder builder){
        this.mContext = context;
        this.mBuilder = builder;
        this.type = type;
        if (Utils.checkoutIsNewVersion(mContext)) {
            try {
                Utils.setHavePraise(mContext);
            } catch (WrapNullPointException e) {
                e.printStackTrace();
            }
            createDialog(context, builder);
        } else {
            if (!checkoutIsShouldShowPraiseDialog()) {
                createDialog(context, builder);
            }
        }
    }


    @Override
    public boolean checkoutIsShouldShowPraiseDialog() {
        try {
            return Utils.isHavePraise(mContext);
        } catch (WrapNullPointException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onCancel() {
        setTrackEvent(mBuilder.trackTag, "关闭");
        if (mBuilder.callBack != null) {
            mBuilder.callBack.onCancel();
        }
    }

    @Override
    public void onPositive() {
        setTrackEvent(mBuilder.trackTag, "Positive");
        if (!checkoutIsVersion()) {
            try {
                Utils.setHavePraise(mContext);
            } catch (WrapNullPointException e) {
                e.printStackTrace();
            }
        }
        Utils.openPlayStore(mContext, mContext.getPackageName());
        if (mBuilder.callBack != null) {
            mBuilder.callBack.onPositive();
        }
    }

    @Override
    public void onNegative() {
        setTrackEvent(mBuilder.trackTag, "Negative");
        if (!checkoutIsVersion()) {
            try {
                Utils.setHavePraise(mContext);
            } catch (WrapNullPointException e) {
                e.printStackTrace();
            }
            Utils.openPlayStore(mContext, mContext.getPackageName());
        }
        if (mBuilder.callBack != null) {
            mBuilder.callBack.onNegative();
        }
    }

    @Override
    public void onDialogDismiss() {
        if (mBuilder.callBack != null) {
            mBuilder.callBack.onDismiss();
        }
    }

    private boolean checkoutIsVersion(){
        if (type == Builder.DialogType.TYPE_VERSION_NARROW || type == Builder.DialogType.TYPE_VERSION_WIDE) {
            return true;
        }
        return false;
    }

    private void setTrackEvent(String action, String label){
        if (type == Builder.DialogType.TYPE_PRAISE_NARROW || type == Builder.DialogType.TYPE_PRAISE_WIDE) {
            Utils.track("好评", action, label, 1);
        } else if (type == Builder.DialogType.TYPE_VERSION_NARROW || type == Builder.DialogType.TYPE_VERSION_WIDE) {
            Utils.track("升级", action, label, 1);
        }
    }

}
