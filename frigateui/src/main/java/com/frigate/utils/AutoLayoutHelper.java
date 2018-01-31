/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frigate.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.frigate.R;
import com.frigate.attr.ElevationAttr;
import com.frigate.attr.HeightAttr;
import com.frigate.attr.MarginAttr;
import com.frigate.attr.MarginBottomAttr;
import com.frigate.attr.MarginLeftAttr;
import com.frigate.attr.MarginRightAttr;
import com.frigate.attr.MarginTopAttr;
import com.frigate.attr.MaxHeightAttr;
import com.frigate.attr.MaxWidthAttr;
import com.frigate.attr.MinHeightAttr;
import com.frigate.attr.MinWidthAttr;
import com.frigate.attr.PaddingAttr;
import com.frigate.attr.PaddingBottomAttr;
import com.frigate.attr.PaddingLeftAttr;
import com.frigate.attr.PaddingRightAttr;
import com.frigate.attr.PaddingTopAttr;
import com.frigate.attr.TextSizeAttr;
import com.frigate.attr.WidthAttr;
import com.frigate.config.AutoLayoutConifg;
import com.frigate.event.IAutoEvent;
import com.frigate.layout.AutoDrawerLayout;
import com.frigate.layout.AutoLayoutInfo;
import com.frigate.layout.FrigateFrameLayout;
import com.frigate.layout.FrigateLinearLayout;
import com.frigate.layout.FrigateRelativeLayout;
import com.frigate.parser.ActionEvent;
import com.frigate.parser.Command;
import com.frigate.parser.FrigateData;
import com.frigate.parser.FrigateManager;
import com.frigate.parser.JsonParser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class AutoLayoutHelper {

    private static final String TAG = "AutoLayoutHelper";

    private final ViewGroup mHost;

    private ArrayList<FrigateData> mFrigateDataList;
    private String json;

    private static final int[] LL = new int[]{ //
            android.R.attr.textSize,
            android.R.attr.padding,//
            android.R.attr.paddingLeft,//
            android.R.attr.paddingTop,//
            android.R.attr.paddingRight,//
            android.R.attr.paddingBottom,//
            android.R.attr.layout_width,//
            android.R.attr.layout_height,//
            android.R.attr.layout_margin,//
            android.R.attr.layout_marginLeft,//
            android.R.attr.layout_marginTop,//
            android.R.attr.layout_marginRight,//
            android.R.attr.layout_marginBottom,//
            android.R.attr.maxWidth,//
            android.R.attr.maxHeight,//
            android.R.attr.minWidth,//
            android.R.attr.minHeight,//
            android.R.attr.elevation,//
    };

    private static final int INDEX_TEXT_SIZE = 0;
    private static final int INDEX_PADDING = 1;
    private static final int INDEX_PADDING_LEFT = 2;
    private static final int INDEX_PADDING_TOP = 3;
    private static final int INDEX_PADDING_RIGHT = 4;
    private static final int INDEX_PADDING_BOTTOM = 5;
    private static final int INDEX_WIDTH = 6;
    private static final int INDEX_HEIGHT = 7;
    private static final int INDEX_MARGIN = 8;
    private static final int INDEX_MARGIN_LEFT = 9;
    private static final int INDEX_MARGIN_TOP = 10;
    private static final int INDEX_MARGIN_RIGHT = 11;
    private static final int INDEX_MARGIN_BOTTOM = 12;
    private static final int INDEX_MAX_WIDTH = 13;
    private static final int INDEX_MAX_HEIGHT = 14;
    private static final int INDEX_MIN_WIDTH = 15;
    private static final int INDEX_MIN_HEIGHT = 16;
    private static final int INDEX_ELEVATION = 17;

    /**
     * move to other place?
     */
    private static AutoLayoutConifg mAutoLayoutConifg;

    public String obtainAttrs(AttributeSet attrs) {
        if (attrs != null) {
            Context context = mHost.getContext();
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoLayout_Layout);
            String jsonName = ta.getString(R.styleable.AutoLayout_Layout_jsonfile);
            Log.e(TAG, "jsonName=" + jsonName);
            String json = FrigateManager.parseJson(context, jsonName);
            mFrigateDataList = JsonParser.fromJson(json);
            ta.recycle();
            return json;
        }
        return null;
    }

    public AutoLayoutHelper(ViewGroup host) {
        mHost = host;
        if (mAutoLayoutConifg == null) {
            initAutoLayoutConfig(host);
        }
    }

    public AutoLayoutHelper(ViewGroup host, AttributeSet attributeSet) {
        mHost = host;
        if (mAutoLayoutConifg == null) {
            initAutoLayoutConfig(host);
        }
        json = obtainAttrs(attributeSet);
    }

    private void initAutoLayoutConfig(ViewGroup host) {
        mAutoLayoutConifg = AutoLayoutConifg.getInstance();
        mAutoLayoutConifg.init(host.getContext());
    }

    public String getJson() {
        return json;
    }

    public void adjustChildren() {
        AutoLayoutConifg.getInstance().checkParams();

        for (int i = 0, n = mHost.getChildCount(); i < n; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();

            if (params instanceof AutoLayoutParams) {
                AutoLayoutInfo info =
                        ((AutoLayoutParams) params).getAutoLayoutInfo();
                if (info != null) {
                    info.fillAttrs(view);
                }
            }
        }

    }

    public interface AutoLayoutParams {
        AutoLayoutInfo getAutoLayoutInfo();
    }

    public static AutoLayoutInfo getAutoLayoutInfo(Context context,
                                                   AttributeSet attrs) {

        AutoLayoutInfo info = new AutoLayoutInfo();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLayout_Layout);
        int baseWidth = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_basewidth, 0);
        int baseHeight = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_baseheight, 0);
        a.recycle();

        TypedArray array = context.obtainStyledAttributes(attrs, LL);

        int n = array.getIndexCount();


        for (int i = 0; i < n; i++) {
            int index = array.getIndex(i);
//            String val = array.getString(index);
//            if (!isPxVal(val)) continue;

            if (!DimenUtils.isPxVal(array.peekValue(index))) continue;

            int pxVal;
            try {
                pxVal = array.getDimensionPixelOffset(index, 0);
            } catch (Exception ignore)//not dimension
            {
                continue;
            }
            switch (index) {
                case INDEX_TEXT_SIZE:
                    info.addAttr(new TextSizeAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING:
                    info.addAttr(new PaddingAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_LEFT:
                    info.addAttr(new PaddingLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_TOP:
                    info.addAttr(new PaddingTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_RIGHT:
                    info.addAttr(new PaddingRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_BOTTOM:
                    info.addAttr(new PaddingBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_WIDTH:
                    info.addAttr(new WidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_HEIGHT:
                    info.addAttr(new HeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN:
                    info.addAttr(new MarginAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_LEFT:
                    info.addAttr(new MarginLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_TOP:
                    info.addAttr(new MarginTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_RIGHT:
                    info.addAttr(new MarginRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_BOTTOM:
                    info.addAttr(new MarginBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MAX_WIDTH:
                    info.addAttr(new MaxWidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MAX_HEIGHT:
                    info.addAttr(new MaxHeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MIN_WIDTH:
                    info.addAttr(new MinWidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MIN_HEIGHT:
                    info.addAttr(new MinHeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_ELEVATION:
                    info.addAttr(new ElevationAttr(pxVal, baseWidth, baseHeight));
                    break;
            }
        }
        array.recycle();
        LogUtil.e(" getAutoLayoutInfo " + info.toString());
        return info;
    }

    public void onFinishInflate(ViewGroup viewGroup) {
        if (mFrigateDataList == null || mFrigateDataList.isEmpty() || viewGroup == null) {
            return;
        }
        EventListener eventListener = new EventListener(viewGroup);
        for (FrigateData frigateData : mFrigateDataList) {
            Log.e(TAG, frigateData + "");
            View view = viewGroup.findViewWithTag(frigateData.tag);
            if (view == null) {
                continue;
            }
            view.setTag(view.getId(), frigateData);
            if (!TextUtils.equals(frigateData.click, ActionEvent.CLICK_NO_ACTION)) {
                view.setOnClickListener(eventListener);
            }
            if (!TextUtils.equals(frigateData.long_click, ActionEvent.LONG_CLICK_NO_ACTION)) {
                view.setOnLongClickListener(eventListener);
            }
        }

    }

    class EventListener implements View.OnLongClickListener, View.OnClickListener {

        private ViewGroup mViewGroup;

        private IAutoEvent mAutoEventListener;

        public EventListener(ViewGroup viewGroup) {
            mViewGroup = viewGroup;
        }

        @Override
        public void onClick(View v) {
            Object object = v.getTag(v.getId());
            Log.e(TAG, "onClick v=" + v.getTag() + "--v.getTag(id)=" + object);
            FrigateData frigateData = null;
            if (object != null && object instanceof FrigateData) {
                frigateData = (FrigateData) object;

                parseClickAction(frigateData);
            }

            mAutoEventListener = createEventListener();
            if (mAutoEventListener != null) {
                mAutoEventListener.onClickEvent(v, mViewGroup, frigateData);
                Context context = mViewGroup.getContext();
                if (context instanceof Activity) {
                    Activity activityContext = (Activity) context;
                    Class activityClass = activityContext.getClass();
                    try {
                        Log.e("rqy", "frigateData.click=" + frigateData.click);
                        activityClass.getDeclaredMethod(frigateData.click).invoke(activityContext);
                        Log.e("rqy", "frigateData.click1=" + frigateData.click);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void parseClickAction(FrigateData frigateData) {
            String clickAction = frigateData.click;
            if (TextUtils.isEmpty(clickAction)) {
                return;
            }
            int type = Command.getCommandType(clickAction);
            if (type == Command.TYPE_DO) {
                /*Toast.makeText(mHost.getContext(), "测试DO指令--" + frigateData, Toast.LENGTH_SHORT).show();*/
            } else if (type == Command.TYPE_GO) {
                /*Toast.makeText(mHost.getContext(), "测试GO指令--" + frigateData, Toast.LENGTH_SHORT).show();*/
            }

        }

        @Override
        public boolean onLongClick(View v) {
            Log.e(TAG, "onLongClick v=" + v.getTag() + "--" + v.getTag(v.getId()));
            Object object = v.getTag(v.getId());
            FrigateData frigateData = null;
            if (object != null && object instanceof FrigateData) {
                frigateData = (FrigateData) object;
                parseClickAction(frigateData);
            }
            mAutoEventListener = createEventListener();
            if (mAutoEventListener != null) {
                return mAutoEventListener.onLongClickEvent(v, mViewGroup, frigateData);
            }
            return false;
        }

        private IAutoEvent createEventListener() {
            if (mAutoEventListener != null) {
                return mAutoEventListener;
            }
            if (mViewGroup == null) {
                return null;
            }
            IAutoEvent autoEventListener = null;
            if (mViewGroup instanceof FrigateFrameLayout) {
                autoEventListener = ((FrigateFrameLayout) mViewGroup).getEventListener();
            } else if (mViewGroup instanceof FrigateRelativeLayout) {
                autoEventListener = ((FrigateRelativeLayout) mViewGroup).getEventListener();
            } else if (mViewGroup instanceof FrigateLinearLayout) {
                autoEventListener = ((FrigateLinearLayout) mViewGroup).getEventListener();
            } else if (mViewGroup instanceof AutoDrawerLayout) {
                autoEventListener = ((AutoDrawerLayout) mViewGroup).getEventListener();
            }
            return autoEventListener;
        }

    }

}
