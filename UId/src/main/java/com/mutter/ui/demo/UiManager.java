package com.mutter.ui.demo;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mutter.ui.demo.cross.CrossDialog;
import com.mutter.ui.demo.cross.CrossView;
import com.mutter.ui.demo.cross.FloatCrossView;
import com.mutter.ui.demo.dialog.Builder;
import com.mutter.ui.demo.dialog.PermanentPraiseView;
import com.mutter.ui.demo.dialog.VersionOrPraiseDialog;
import com.mutter.ui.demo.util.Utils;
import com.mutter.ui.demo.util.WrapNullPointException;
import com.mutter.ui.demo.view.ExitDialog;
import com.mutter.ui.demo.view.ExitView;

public class UiManager {

    public static VersionOrPraiseDialog requestDialog(Context context, String serviceData, Builder builder) {
        return new VersionOrPraiseDialog(context, serviceData, builder);
    }

    public static PermanentPraiseView getPermaentPraiseView(Context context, Builder builder) throws WrapNullPointException {
        return new PermanentPraiseView(context, builder);
    }

    public static ExitDialog getExitView(Context context, ExitView.Builder builder) throws WrapNullPointException {
        if (context == null) {
            throw new WrapNullPointException("getExitView context is null");
        }
        if (builder == null) {
            throw new WrapNullPointException("getExitView builder is null");
        }
        ExitDialog dialog = null;
        if (builder.getType() == null) {
            return null;
        }
        if (builder.getType() == ExitView.Type.TYPE_EXIT_DIALOG) {
            dialog = new ExitDialog(context);
            dialog.show();
            dialog.setContentView(new ExitView(context, builder));
            Window window = dialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = (int) (Utils.getScreenWidth(context) * 0.8);
            window.setAttributes(params);
        } else if (builder.getType() == ExitView.Type.TYPE_EXIT_FULL) {
            dialog = new ExitDialog(context, true);
            dialog.show();
            dialog.setContentView(new ExitView(context, builder));
        }
        return dialog;
    }

    public static void getCrossView(Context context, com.mutter.ui.demo.cross.Builder builder,
                                    final CrossView.OnDataFinishListener listener) throws WrapNullPointException {
        if (context == null) {
            throw new WrapNullPointException("getCrossView context is null");
        }
        if (builder == null) {
            throw new WrapNullPointException("getCrossView builder is null");
        }

        new CrossView(context, builder, new CrossView.OnDataFinishListener() {
            @Override
            public void onFinish(CrossView crossView) {
                if (crossView == null) {
                    return;
                }
                if (listener != null) {
                    listener.onFinish(crossView);
                }
            }
        });
    }

    public static void showCrossDialog(final Context context, com.mutter.ui.demo.cross.Builder builder,
                                       final CrossView.OnCrossDialogRequestFinishListener listener) throws WrapNullPointException {
        if (context == null) {
            throw new WrapNullPointException("getCrossView context is null");
        }
        if (builder == null) {
            throw new WrapNullPointException("getCrossView builder is null");
        }
        builder.setType(com.mutter.ui.demo.cross.Builder.Type.TYPE_DIALOG);

        new CrossView(context, builder, new CrossView.OnDataFinishListener() {
            @Override
            public void onFinish(CrossView crossView) {
                if (crossView == null) {
                    return;
                }
                CrossDialog dialog = new CrossDialog(context);
                dialog.show();



                int screenWidth = Utils.getScreenWidth(context);
                int width = (int) (screenWidth * 0.9);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams params1 = window.getAttributes();
                params1.width = width;
                params1.gravity = Gravity.CENTER;
                ImageView image = (ImageView) crossView.findViewById(com.mutter.ui.demo.R.id.head);
                if (image != null) {
                    ViewGroup.LayoutParams params = image.getLayoutParams();
                    params.height = (int) (screenWidth * 0.425);//SdkEnv.scaleDp2Px(Utils.px2dip(context, (float) (screenWidth * 0.45)));
                    image.setLayoutParams(params);
                }
                window.setAttributes(params1);
                dialog.setContentView(crossView);
                if (listener != null) {
                    listener.onDialogShow(dialog, crossView);
                }
            }
        });
        return;
    }

    public interface OnClickListener {
        void onClick();
    }

    public static void showFloatCrossView(final Context context, final com.mutter.ui.demo.cross.Builder builder,
                                          final FloatCrossView.OnFloatCrossViewInitCallBack callBack,
                                          final OnClickListener listener) throws WrapNullPointException {
        if (context == null) {
            throw new WrapNullPointException("showFloatCrossView context is null");
        }
        if (builder == null) {
            throw new WrapNullPointException("showFloatCrossView builder is null");
        }
        new FloatCrossView(context, builder, callBack, listener);
    }

}
