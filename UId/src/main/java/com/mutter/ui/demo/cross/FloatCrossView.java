package com.mutter.ui.demo.cross;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mutter.ui.demo.UiManager;
import com.mutter.ui.demo.entry.CrossItem;
import com.mutter.ui.demo.util.JsonParser;
import com.mutter.ui.demo.util.Utils;

import java.util.concurrent.Executors;

public class FloatCrossView extends View {
    public int width;
    public int height;
    private Bitmap bitmap_normal;

    public static final int STYLE_56 = 56;

    public static final int STYLE_40 = 40;

    public static final int STYLE_WRAP = -1;

    private Builder mBuilder;
    private String pkgName;
    private Context mContext;
    private CrossItem object;
    private UiManager.OnClickListener clickListener = null;

    public interface OnFloatCrossViewInitCallBack {
        void onInit(FloatCrossView floatCrossView);
    }

    class MyTask extends AsyncTask<String, String, String> {
        private Bitmap mBitmap = null;
        private OnFloatCrossViewInitCallBack callBack = null;

        public void setOnInitListener(OnFloatCrossViewInitCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        protected String doInBackground(String... params) {
            mBitmap = Utils.getBitmap(object.iconUrl);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mBitmap == null) {
                if (callBack != null) {
                    callBack.onInit(null);
                }
            } else {
                if (callBack != null) {
                    callBack.onInit(FloatCrossView.this);
                }
                initData(mBitmap);
                setLayout();
            }
        }

    }

    public FloatCrossView(Context context, Builder builder,
                          final OnFloatCrossViewInitCallBack callBack,
                          UiManager.OnClickListener listener) {
        super(context);
        this.mContext = context;
        this.mBuilder = builder;
        this.clickListener = listener;
        object = JsonParser.getSpecifyJsonObject(context, builder.extraData, builder.requestTag);
        if (object == null) {
            return;
        }
        pkgName = object.pkgName;
        MyTask myTask = new MyTask();
        myTask.setOnInitListener(new OnFloatCrossViewInitCallBack() {
            @Override
            public void onInit(FloatCrossView floatCrossView) {
                if (callBack != null) {
                    callBack.onInit(floatCrossView);
                }
            }
        });
        new MyTask().executeOnExecutor(Executors.newSingleThreadExecutor());
    }

    private void initData(Bitmap resBitmap) {
        if (mBuilder.style != STYLE_40 && mBuilder.style != STYLE_56 && mBuilder.style != STYLE_WRAP) {
            throw new RuntimeException("style must be STYLE_40 ,STYLE_56 or STYLE_WRAP");
        }

        if (mBuilder.style == STYLE_WRAP) {
            width = resBitmap.getWidth();
            height = resBitmap.getHeight();
        } else {
            height = width = Utils.dip2px(mContext, mBuilder.style);
        }

        init(resBitmap);
    }

    View.OnTouchListener floatCrossViewTouchListener = new View.OnTouchListener() {
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
                    } else if (params.leftMargin > parentWidth - width) {
                        params.leftMargin = parentWidth - width;
                    }
                    if (params.topMargin <= 0) {
                        params.topMargin = 0;
                    } else if (params.topMargin > parentHeight - height) {
                        params.topMargin = parentHeight - height;
                    }
                    setLayoutParams(params);
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

    private FrameLayout.LayoutParams params;
    private int parentWidth = 0;
    private int parentHeight = 0;

    private void setLayout() {
        Utils.track("交叉推广-分广告位", mBuilder.trackPosition, "展示--" + pkgName, 1);
        Utils.track("交叉推广-分应用", pkgName, "展示", 1);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.track("交叉推广-分广告位", mBuilder.trackPosition, "点击--" + pkgName, 1);
                Utils.track("交叉推广-分应用", pkgName, "点击", 1);
//                if (Utils.checkoutISAppHasInstalled(getContext(), pkgName)) {
//                    Utils.launchApp(getContext(), pkgName);
//                } else {
//                    Utils.openPlayStore(mContext, pkgName, mBuilder.extraData);
//                }
                if (clickListener != null) {
                    clickListener.onClick();
                }
            }
        });
        this.setOnTouchListener(floatCrossViewTouchListener);
        parentHeight = Utils.getScreenHeight(mContext);//parent.getHeight();
        parentWidth = Utils.getScreenWidth(mContext);//parent.getWidth();

        params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mBuilder.leftMargin < 0) {
//            mBuilder.leftMargin = 0;
            mBuilder.setLeftMargin(0);
        } else if (mBuilder.leftMargin > parentWidth - width) {
            mBuilder.setLeftMargin(parentWidth - width);
//            mBuilder.leftMargin = parentWidth - width;
        }
        if (mBuilder.topMargin < 0) {
            mBuilder.topMargin = 0;
            mBuilder.setTopMargin(0);
        } else if (mBuilder.topMargin > parentHeight - height) {
            mBuilder.setTopMargin(parentHeight - height);
//            mBuilder.topMargin = parentHeight - height;
        }

        params.leftMargin = mBuilder.leftMargin;
        params.topMargin = mBuilder.topMargin;
        setLayoutParams(params);

        Activity activity = (Activity) mContext;
        FrameLayout frameLayout = (FrameLayout) activity.getWindow().getDecorView();
        for (int i = 0; i < frameLayout.getChildCount(); i++) {
            View view = frameLayout.getChildAt(i);
            if (view instanceof FloatCrossView) {
                frameLayout.removeView(view);
                break;
            }
        }
        frameLayout.addView(this);
    }


    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmap_normal, 0, 0, null);
    }

    private void init(Bitmap resBitmap) {
//        Bitmap src = BitmapFactory.decodeResource(getResources(), resId);
        bitmap_normal = Bitmap.createScaledBitmap(resBitmap, width, height, true);
        try {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidate();
    }

}