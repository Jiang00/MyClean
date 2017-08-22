package com.achtapps.ui.demo.cross;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.achtapps.ui.demo.R;
import com.achtapps.ui.demo.entry.CrossItem;
import com.achtapps.ui.demo.util.JsonParser;
import com.achtapps.ui.demo.util.Utils;

import java.util.concurrent.Executors;

public class CrossView extends LinearLayout {

    private FrameLayout rootView;
    private View view;
    private ImageView icon;
    private TextView title;
    private TextView actionBtn;
    private ImageView adInfo;

    private ImageView head;
    private TextView subTitle;
    private Context mContext;
    private Builder builder;
    private boolean isShouldForceShowActionButton = false;
    private String pkgName = null;

    public interface OnCrossDialogRequestFinishListener {
        void onDialogShow(CrossDialog crossDialog, CrossView crossView);
    }

    public interface OnDataFinishListener {
        void onFinish(CrossView crossView);
    }

    class MyTask extends AsyncTask<String, String, String> {
        //        private CrossView crossView = null;
        private Bitmap headBitmap;
        private Bitmap iconBitmap;
        private OnDataFinishListener listener = null;

        public void setOnDataFinishListener(OnDataFinishListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            crossView = new CrossView(mContext);
            init(mContext, builder);
            setServiceData(object, builder);
        }

        @Override
        protected String doInBackground(String... params) {
            if (object == null) {
                return null;
            }
            headBitmap = Utils.getBitmap(object.headUrl);
            iconBitmap = Utils.getBitmap(object.iconUrl);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (headBitmap == null || iconBitmap == null) {
//                if (listener != null) {
//                    listener.onFinish(null);
//                }
//                return;
//            }
            if (head != null && headBitmap != null) {
                head.setImageBitmap(headBitmap);
            }
            if (icon != null && iconBitmap != null) {
                icon.setImageBitmap(iconBitmap);
            }
            if (listener != null) {
                listener.onFinish(CrossView.this);
            }
        }
    }

    public CrossView(Context context, Builder builder, final OnDataFinishListener listener) {
        super(context);
        this.mContext = context;
        this.builder = builder;
        MyTask task = new MyTask();
        task.setOnDataFinishListener(new OnDataFinishListener() {
            @Override
            public void onFinish(CrossView crossView) {
                if (listener != null) {
                    listener.onFinish(crossView);
                }
            }
        });
        task.executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    public CrossView(Context context) {
        super(context);
    }

    public void init(final Context context, final Builder builder) {
//        super(context);
        if (context == null || builder == null) {
            return;
        }

        if (builder.type == Builder.Type.TYPE_56) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_side_item_56, null);
            isShouldForceShowActionButton = false;
        } else if (builder.type == Builder.Type.TYPE_48) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_side_item_48, null);
            isShouldForceShowActionButton = false;
        } else if (builder.type == Builder.Type.TYPE_72) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_side_item_72, null);
            isShouldForceShowActionButton = false;
        } else if (builder.type == Builder.Type.TYPE_HORIZONTAL_76) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_cross_horizontal_bar_76, null);
            subTitle = (TextView) view.findViewById(R.id.subTitle);
            isShouldForceShowActionButton = true;
        } else if (builder.type == Builder.Type.TYPE_HORIZONTAL_99) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_cross_horizontal_bar_99, null);
            subTitle = (TextView) view.findViewById(R.id.subTitle);
            isShouldForceShowActionButton = true;
        } else if (builder.type == Builder.Type.TYPE_SQUARE_272) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_cross_square_272, null);
            subTitle = (TextView) view.findViewById(R.id.subTitle);
            head = (ImageView) view.findViewById(R.id.head);
            isShouldForceShowActionButton = true;
        } else if (builder.type == Builder.Type.TYPE_SQUARE_193) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_cross_square_193, null);
            subTitle = (TextView) view.findViewById(R.id.subTitle);
            head = (ImageView) view.findViewById(R.id.head);
            isShouldForceShowActionButton = true;
        } else if (builder.type == Builder.Type.TYPE_DIALOG) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_cross_dialog, null);
            subTitle = (TextView) view.findViewById(R.id.subTitle);
            head = (ImageView) view.findViewById(R.id.head);
            isShouldForceShowActionButton = true;
        }

        addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView = (FrameLayout) view.findViewById(R.id.rootView);
        icon = (ImageView) view.findViewById(R.id.mainIcon);
        title = (TextView) view.findViewById(R.id.title);
        actionBtn = (TextView) view.findViewById(R.id.action);
        adInfo = (ImageView) view.findViewById(R.id.adInfo);
        initData(context, builder);
    }

    private CrossItem object;

    private void getJsonObjectData(Context context) {
        object = JsonParser.getSpecifyJsonObject(context, builder.extraData, builder.requestTag);
        if (object != null) {
            pkgName = object.pkgName;
            if (TextUtils.equals(pkgName, context.getPackageName())) {
                getJsonObjectData(context);
            }
        }
    }

    public void initData(final Context context, final Builder builder) {

        getJsonObjectData(context);
        if (object != null) {
            setServiceData(object, builder);
        }
        Utils.track("交叉推广-分广告位", builder.trackPosition, "展示--" + pkgName, 1);
        Utils.track("交叉推广-分应用", pkgName, "展示", 1);
        try {
            rootView.setBackgroundColor(Color.TRANSPARENT);
            rootView.setBackgroundColor(builder.rootViewBackground);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (actionBtn != null) {
//            actionBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Utils.reactionForAction(context, pkgName);
//                    if (builder.callBack != null) {
//                        builder.callBack.onAction();
//                    }
//                }
//            });
//        }
        if (rootView != null) {
            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.track("交叉推广-分广告位", builder.trackPosition, "点击--" + pkgName, 1);
                    Utils.track("交叉推广-分应用", pkgName, "点击", 1);
//                    Utils.reactionForAction(context, builder.extraData, pkgName, object.action);
                    if (builder.callBack != null) {
                        builder.callBack.onViewClick();
                    }
                }
            });
        }
    }

    private void setServiceData(CrossItem object, Builder builder) {
        if (adInfo != null && builder.adTagImageId > 0) {
            adInfo.setImageResource(builder.adTagImageId);
        }
        if (title != null) {
            String titleText = object.title;
            if (titleText != null) {
                title.setText(titleText);
                try {
                    title.setTextColor(builder.titleColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (subTitle != null) {
            String subTitleText = object.subTitle;
            if (subTitleText != null) {
                subTitle.setText(subTitleText);
                try {
                    subTitle.setTextColor(builder.subTitleColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (actionBtn != null) {
            if (!isShouldForceShowActionButton) {
                if (!builder.isShowActionBtn) {
                    actionBtn.setVisibility(GONE);
                    return;
                }
            }
            String actionText = "Install";
            if (Utils.checkoutISAppHasInstalled(mContext, pkgName)) {
                actionText = object.actionTextOpen;
            } else {
                actionText = object.actionTextInstall;
            }
            if (TextUtils.isEmpty(actionText)) {
                actionBtn.setVisibility(GONE);
            } else {
                actionBtn.setText(actionText);
                try {
                    actionBtn.setTextColor(builder.actionBtnTextColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    actionBtn.setBackgroundResource(builder.actionBtnBackground);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int width = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        changeHeight(head);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void changeHeight(ImageView image) {
        if (image == null) {
            return;
        }
        if (builder.type == Builder.Type.TYPE_SQUARE_272
                || builder.type == Builder.Type.TYPE_SQUARE_193
                || builder.type == Builder.Type.TYPE_DIALOG) {
            ViewGroup.LayoutParams params = image.getLayoutParams();
            params.height = (int) (width * 0.43);
            image.setLayoutParams(params);
        }
    }

}
