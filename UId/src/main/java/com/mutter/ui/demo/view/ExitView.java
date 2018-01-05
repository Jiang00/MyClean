package com.upupup.ui.demo.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.upupup.ui.demo.R;
import com.upupup.ui.demo.util.Utils;

public class ExitView extends LinearLayout {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public enum Type{
        TYPE_EXIT_FULL, TYPE_EXIT_DIALOG
    }

    public static class Builder {
        protected Type type;
        protected View adView = null;
        protected int titleText = -1;
        protected int titleTextColor = -1;
        protected int exitText = -1;
        protected int exitTextColor = -1;
        protected int exitTextBackground = -1;
        protected int stayText = -1;
        protected int stayTextColor = -1;
        protected int stayTextBackground = -1;
        protected String trackTag;
        protected OnActionCallBack callBack = null;
        public interface OnActionCallBack{
            void onExit();
            void onStay();
        }

        public Builder setTrackTag(String trackTag){
            if (TextUtils.isEmpty(trackTag)) {
                trackTag = "退出";
            }
            this.trackTag = trackTag;
            return this;
        }
        public Builder setType(Type type){
            this.type = type;
            return this;
        }
        public Type getType(){
            return this.type;
        }
        public Builder setAdView(View view){
            this.adView = view;
            return this;
        }
        public Builder setTitleText(int textId){
            this.titleText = textId;
            return this;
        }
        public Builder setTitleTextColor(int color){
            this.titleTextColor = color;
            return this;
        }
        public Builder setExitText(int textId){
            this.exitText = textId;
            return this;
        }
        public Builder setExitTextColor(int color){
            this.exitTextColor = color;
            return this;
        }
        public Builder setExitTextBackgroundResource(int resId){
            this.exitTextBackground = resId;
            return this;
        }
        public Builder setStayText(int textId){
            this.stayText = textId;
            return this;
        }
        public Builder setStayTextColor(int color){
            this.stayTextColor = color;
            return this;
        }
        public Builder setStayTextBackgroundResource(int resId){
            this.stayTextBackground = resId;
            return this;
        }
        public Builder setOnActionCallBack(OnActionCallBack callBack){
            this.callBack = callBack;
            return this;
        }

    }

    protected Context mContext;
    private View view;
    private LinearLayout adContainer;
    private TextView title;
    private TextView exit;
    private TextView stay;

    public ExitView(Context context, Builder builder){
        super(context);
        if (builder == null) {
            return;
        }
        this.mContext = context;
        initViews(builder);
    }

    public void initViews(Builder builder){
        if (builder.type == Type.TYPE_EXIT_DIALOG) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_exit_type_dialog, null);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_exit_type_full, null);
        }
        adContainer = (LinearLayout) view.findViewById(R.id.adContainer);
        title = (TextView) view.findViewById(R.id.title);
        exit = (TextView) view.findViewById(R.id.exit);
        stay = (TextView) view.findViewById(R.id.stay);
        addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setViewsData(builder);
    }


    public void setViewsData(final Builder builder){
        if (adContainer != null && builder.adView != null) {
            adContainer.removeAllViews();
            adContainer.addView(builder.adView);
        }
        if (title != null) {
            if (builder.titleText > 0) {
                title.setText(builder.titleText);
                try {
                    title.setTextColor(builder.titleTextColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (exit != null) {
            if (builder.exitText > 0) {
                exit.setText(builder.exitText);
                try{
                    exit.setTextColor(builder.exitTextColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                exit.setBackgroundResource(builder.exitTextBackground);
            } catch (Exception e) {
                e.printStackTrace();
            }
            exit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTrackEvent(builder, builder.trackTag, "退出");
                    if (builder != null && builder.callBack != null) {
                        builder.callBack.onExit();
                    }
                }
            });
        }
        if (stay != null) {
            if (builder.stayText > 0) {
                stay.setText(builder.stayText);
                try {
                    stay.setTextColor(builder.stayTextColor);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            try {
                stay.setBackgroundResource(builder.stayTextBackground);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stay.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTrackEvent(builder, builder.trackTag, "不退出");
                    if (builder != null && builder.callBack != null) {
                        builder.callBack.onStay();
                    }
                }
            });
        }

    }

    private void setTrackEvent(Builder builder, String action, String label){
        if (builder.type == Type.TYPE_EXIT_DIALOG) {
            Utils.track("退出弹窗", action, label, 1);
        } else if (builder.type == Type.TYPE_EXIT_DIALOG) {
            Utils.track("全屏退出弹窗", action, label, 1);
        }
    }


}