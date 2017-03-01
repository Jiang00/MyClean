package com.supers.clean.junk.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.supers.clean.junk.R;
import com.supers.clean.junk.myView.MainScrollView;

public class MainActivity extends BaseActivity {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int NOMAL = 2;

    public static MainActivity instance;
    FrameLayout main_all_cercle;
    MainScrollView main_scroll_view;
    private float firstY;
    private DisplayMetrics dm;
    private int cercleHeight;
    private boolean first = true;
    private int cercle_value;
    private boolean isScroll;

//    int scrollHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        main_all_cercle = (FrameLayout) findViewById(R.id.main_all_cercle);
        main_scroll_view = (MainScrollView) findViewById(R.id.main_scroll_view);
//改变尺寸
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        ViewGroup.LayoutParams layout_ad = main_all_cercle.getLayoutParams();
        int ac = getStatusHeight(this);
        layout_ad.height = dm.heightPixels - ac - dp2px(56) - dp2px(185);
        main_all_cercle.setLayoutParams(layout_ad);

        final ViewGroup.LayoutParams cercle_linearParams = main_all_cercle.getLayoutParams();


        main_scroll_view.setOnTouchListener(new View.OnTouchListener() {

            int state = NOMAL;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (first) {
                    cercle_value = cercle_linearParams.height;
                    first = false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cercleHeight = cercle_linearParams.height;
                        isScroll = main_scroll_view.getScrollY() > 0;
                        firstY = event.getY();
                        Log.e("aaa", "===" + cercleHeight);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isScroll) {
                            break;
                        }
                        float y = event.getY();

                        float deltaY = firstY - y;// 滑动距离
                        /** 对于初次Touch操作要判断方位：UP OR DOWN **/
                        if (deltaY > 0 && state == NOMAL) {
                            state = UP;
                        } else if (deltaY < 0 && state == NOMAL) {
                            state = DOWN;
                        }
                        if (state == UP) {
                            if (cercleHeight == cercle_value) {
                                if ((int) (cercleHeight - deltaY) > cercle_value) {
                                    cercle_linearParams.height = cercle_value;
                                } else if ((int) (cercleHeight - deltaY) < 0) {
                                    cercle_linearParams.height = 0;
                                } else {
                                    cercle_linearParams.height = (int) (cercleHeight - deltaY);
                                }
                                main_all_cercle.setLayoutParams(cercle_linearParams);
                                main_scroll_view.setShutTouch(true);
                            } else if (cercleHeight == 0) {
                                main_scroll_view.setShutTouch(false);
                            }
                        } else {
                            if (cercleHeight == cercle_value) {
                                main_scroll_view.setShutTouch(true);
                            } else if (cercleHeight == 0) {
                                if (main_scroll_view.getScrollY() > 0) {
                                    main_scroll_view.setShutTouch(false);
                                    break;
                                }
                                main_scroll_view.setShutTouch(true);
                                if ((int) (cercleHeight - deltaY) > cercle_value) {
                                    cercle_linearParams.height = cercle_value;
                                } else if ((int) (cercleHeight - deltaY) < 0) {
                                    cercle_linearParams.height = 0;
                                } else {
                                    cercle_linearParams.height = (int) (cercleHeight - deltaY);
                                }
                                main_all_cercle.setLayoutParams(cercle_linearParams);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isScroll) {
                            break;
                        }
                        if (state == UP) {
                            if (cercleHeight == cercle_value) {
                                if (cercle_linearParams.height < (cercle_value / 2)) {
                                    cercle_linearParams.height = 0;
                                    main_all_cercle.setLayoutParams(cercle_linearParams);
                                } else {
                                    cercle_linearParams.height = cercle_value;
                                    main_all_cercle.setLayoutParams(cercle_linearParams);
                                }
                            }
                        } else {
                            if (cercleHeight == 0) {
                                if (cercle_linearParams.height < (cercle_value / 2)) {
                                    cercle_linearParams.height = 0;
                                    main_all_cercle.setLayoutParams(cercle_linearParams);
                                } else {
                                    cercle_linearParams.height = cercle_value;
                                    main_all_cercle.setLayoutParams(cercle_linearParams);
                                }
                            }
                        }
                        state = NOMAL;
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
