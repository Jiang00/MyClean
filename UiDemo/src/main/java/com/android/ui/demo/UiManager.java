package com.android.ui.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.ui.demo.cross.CrossDialog;
import com.android.ui.demo.cross.CrossView;
import com.android.ui.demo.cross.FloatCrossView;
import com.android.ui.demo.dialog.Builder;
import com.android.ui.demo.dialog.PermanentPraiseView;
import com.android.ui.demo.dialog.VersionOrPraiseDialog;
import com.android.ui.demo.util.JsonParser;
import com.android.ui.demo.util.Utils;
import com.android.ui.demo.util.WrapNullPointException;
import com.android.ui.demo.view.ExitDialog;
import com.android.ui.demo.view.ExitView;

import org.json.JSONObject;

import java.util.concurrent.Executors;

public class UiManager {

    public UiManager() {

    }

    public static VersionOrPraiseDialog requestPraiseDialog(Context context, Builder.DialogType type, Builder builder) {
        return new VersionOrPraiseDialog(context, type, builder);
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
        } else if (builder.getType() == ExitView.Type.TYPE_EXIT_FULL) {
            dialog = new ExitDialog(context, true);
            dialog.show();
            dialog.setContentView(new ExitView(context, builder));
        }
        return dialog;
    }

    public static void getCrossView(Context context, com.android.ui.demo.cross.Builder builder,
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

    public static void showCrossDialog(final Context context, com.android.ui.demo.cross.Builder builder,
                                       final CrossView.OnCrossDialogRequestFinishListener listener) throws WrapNullPointException {
        if (context == null) {
            throw new WrapNullPointException("getCrossView context is null");
        }
        if (builder == null) {
            throw new WrapNullPointException("getCrossView builder is null");
        }
        builder.setType(com.android.ui.demo.cross.Builder.Type.TYPE_DIALOG);

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
                ImageView image = (ImageView) crossView.findViewById(R.id.head);
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


    private FloatCrossView floatCrossView;
    //    private FloatMenuView menuView;
    private FrameLayout.LayoutParams params;

    private int parentWidth = 0;

    private int parentHeight = 0;
    private View.OnTouchListener floatCrossViewTouchListener = new View.OnTouchListener() {
        private float startX;
        private float startY;
        private float x0;
        private float y0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    x0 = event.getRawX();
                    y0 = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int end = (int) event.getRawX();
                    int endY = (int) event.getRawY();
                    float dx = end - startX;
                    float dy = endY - startY;
                    if (Math.abs(end - x0) < 50 && Math.abs(endY - y0) < 50) {
                        break;
                    }
                    params.leftMargin += dx;
                    params.topMargin += dy;
                    if (params.leftMargin <= 0) {
                        params.leftMargin = 0;
                    } else if (params.leftMargin > parentWidth - floatCrossView.width) {
                        params.leftMargin = parentWidth - floatCrossView.width;
                    }
                    if (params.topMargin <= 0) {
                        params.topMargin = 0;
                    } else if (params.topMargin > parentHeight - floatCrossView.height) {
                        params.topMargin = parentHeight - floatCrossView.height;
                    }
                    floatCrossView.setLayoutParams(params);
                    startX = (int) event.getRawX();
                    startY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getRawX();
                    if (Math.abs(endX - x0) > 50) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    break;
            }
            return false;
        }
    };

    public interface OnClickListener {
        void onClick();
    }

    private String pkgName;

    /**
     * @param serviceData 服务器数据
     * @param listener    监听
     */
    public void showFloatCrossView(Context context, String serviceData, int leftMargin, int topMargin, int style, String trackTag, OnClickListener listener) {
        JSONObject object = JsonParser.getSpecifyJsonObject(context, serviceData, "cross");
        if (object == null) {
            return;
        }
        if (TextUtils.isEmpty(trackTag)) {
            trackTag = "交叉推广";
        }
        pkgName = object.optString("package");
        String url = object.optString("icon");
        getFloatCrossView(context, url, leftMargin, topMargin, style, trackTag, listener);

    }

    private void setFloatCrossView(final Context context, int leftMargin, int topMargin, final String trackTag, final OnClickListener listener) {
        if (floatCrossView == null) {
            return;
        }
        Utils.track("交叉推广-分广告位", trackTag, "展示--" + pkgName, 1);
        Utils.track("交叉推广-分应用", pkgName, "展示", 1);
        floatCrossView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.track("交叉推广-分广告位", trackTag, "点击--" + pkgName, 1);
                Utils.track("交叉推广-分应用", pkgName, "点击", 1);
                Utils.reactionForAction(context, pkgName);
                if (listener != null) {
                    listener.onClick();
                }
            }
        });

        parentHeight = Utils.getScreenHeight(context);//parent.getHeight();
        parentWidth = Utils.getScreenWidth(context);//parent.getWidth();

        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (leftMargin < 0) {
            leftMargin = 0;
        } else if (leftMargin > parentWidth - floatCrossView.width) {
            leftMargin = parentWidth - floatCrossView.width;
        }

        if (topMargin < 0) {
            topMargin = 0;
        } else if (topMargin > parentHeight - floatCrossView.height) {
            topMargin = parentHeight - floatCrossView.height;
        }
        params.leftMargin = leftMargin;//parentWidth - floatCrossViewWidth;
        params.topMargin = topMargin;//parentHeight / 2;

        floatCrossView.setLayoutParams(params);

        Activity activity = (Activity) context;
        FrameLayout frameLayout = (FrameLayout) activity.getWindow().getDecorView();
        for (int i = 0; i < frameLayout.getChildCount(); i++) {
            View view = frameLayout.getChildAt(i);
            if (view instanceof FloatCrossView) {
                frameLayout.removeView(view);
                break;
            }
        }
        frameLayout.addView(floatCrossView);
    }

    public void getFloatCrossView(final Context context, final String url, final int leftMargin, final int topMargin, final int style, final String trackTag, final OnClickListener listener) {
        new AsyncTask<String, String, String>() {
            Bitmap bit;

            @Override
            protected String doInBackground(String... params) {
                Log.e("rqy","getFloatCrossView--url="+url);
                bit = Utils.getBitmap(url);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (bit == null) {
                    floatCrossView = null;
                } else {
                    floatCrossView = new FloatCrossView(context, bit, style);
                    floatCrossView.setOnTouchListener(floatCrossViewTouchListener);
                    setFloatCrossView(context, leftMargin, topMargin, trackTag, listener);
                }

            }

        }.executeOnExecutor(Executors.newSingleThreadExecutor());

    }


}
