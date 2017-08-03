package com.froumobic.clean.junk.mactivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.client.AndroidSdk;
import com.android.ui.demo.entry.CrossItem;
import com.android.ui.demo.util.JsonParser;
import com.frau.cleanmodule.cooling.BatteryCoolingActivity;
import com.froumobic.clean.junk.R;
import com.froumobic.clean.junk.util.AdUtil;
import com.froumobic.clean.junk.util.Constant;

import java.util.ArrayList;

/**
 * Created by on 2017/2/28.
 */

public class MBaseActivity extends AppCompatActivity {
    private Toast toast;
    protected View view_title_bar;
    protected boolean onPause = false;
    protected boolean onDestroyed = false;
    protected boolean onResume = false;

    protected String TUIGUAN_MAIN = "main";
    protected String TUIGUAN_MAIN_SOFT = "main_soft";
    protected String TUIGUAN_SIDE = "slide";
    protected String TUIGUAN_SIDE_SOFT = "slide_soft";
    protected String TUIGUAN_SETTING = "setting";
    protected String TUIGUAN_SETTING_SOFT = "setting_soft";
    protected String TUIGUAN_SUCCESS = "success";
    protected String TUIGUAN_SUCCESS_SOFT = "success_soft";
    protected String TUIGUAN_TAB = "main_tab";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        AndroidSdk.onCreate(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);

        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (PreData.getDB(this, Constant.IS_ACTION_BAR, true)) {
            full();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        onPause = false;
        onResume = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPause = true;
        onResume = false;
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        view_title_bar = findViewById(R.id.view_title_bar);
        ViewGroup.LayoutParams linearParams = view_title_bar.getLayoutParams();
        linearParams.height = getStatusHeight(this);
        view_title_bar.setLayoutParams(linearParams);
        findId();
    }

    protected void findId() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyed = true;
    }


    public void tuiguang(final String tag, final boolean isSoftCross, View viewP) {
        ArrayList<CrossItem> crossItems = JsonParser.getCrossData(this, AndroidSdk.getExtraData(), tag);
        if (crossItems != null) {
            for (int i = 0; i < crossItems.size(); i++) {
                final CrossItem item = crossItems.get(i);
                View view = LayoutInflater.from(this).inflate(R.layout.layout_tuiguang_main, null);
                final int j = i + 4;
                String t = "主界面";
                if (tag.equals(TUIGUAN_MAIN) || tag.equals(TUIGUAN_MAIN_SOFT)) {
                    t = "主界面";
                    view = LayoutInflater.from(this).inflate(R.layout.layout_tuiguang_main, null);
                    TextView tuiguang_sub_title = (TextView) view.findViewById(R.id.tuiguang_subtitle);
                    tuiguang_sub_title.setText(item.getSubTitle());
                    tuiguangZhanshi(isSoftCross, item, t, j);
                } else if (tag.equals(TUIGUAN_SIDE) || tag.equals(TUIGUAN_SIDE_SOFT)) {
                    t = "侧边栏";
                    view = LayoutInflater.from(this).inflate(R.layout.layout_tuiguang_side, null);
                    tuiguangZhanshi(isSoftCross, item, t, j);
                } else if (tag.equals(TUIGUAN_SETTING) || tag.equals(TUIGUAN_SETTING_SOFT)) {
                    t = "设置";
                    view = LayoutInflater.from(this).inflate(R.layout.layout_tuiguang_setting, null);
                    tuiguangZhanshi(isSoftCross, item, t, j);
                } else if (tag.equals(TUIGUAN_SUCCESS) || tag.equals(TUIGUAN_SUCCESS_SOFT)) {
                    t = "清理";
                    view = LayoutInflater.from(this).inflate(R.layout.layout_tuiguang_main, null);
                    TextView tuiguang_sub_title = (TextView) view.findViewById(R.id.tuiguang_subtitle);
                    tuiguang_sub_title.setText(item.getSubTitle());
                    tuiguangZhanshi(isSoftCross, item, t, j);
                }
                ImageView image = (ImageView) view.findViewById(R.id.tuiguang_icon);
                Util.loadImg(this, item.getTagIconUrl(), R.mipmap.icon, image);
                TextView tuiguang_title = (TextView) view.findViewById(R.id.tuiguang_title);
                tuiguang_title.setText(item.getTitle());

                final String finalT = t;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (isSoftCross) {
                            if (com.android.ui.demo.util.Utils.checkoutISAppHasInstalled(MBaseActivity.this, item.getPkgName())) {
                                try {
                                    Intent intent = new Intent(item.getAction());
                                    startActivity(intent);
                                } catch (Exception e) {
                                    Log.e("tuiguang", "not find action=" + item.getAction());
                                    com.android.ui.demo.util.Utils.openPlayStore(MBaseActivity.this, item.getPkgName(), AndroidSdk.getExtraData());
                                }
                            } else {
                                if (TextUtils.equals(item.action, Constant.RAM_CLEAN_ACTION)) {
                                    //内存加速
                                    Intent intent = new Intent(MBaseActivity.this, RamAvtivity.class);
                                    startActivity(intent);
                                    AdUtil.track("交叉推广_广告位", "广告位_" + finalT + "1", "点击" + item.getPkgName(), 1);
                                } else if (TextUtils.equals(item.action, Constant.JUNK_CLEAN_ACTION)) {
                                    //垃圾清理
                                    Intent intent = new Intent(MBaseActivity.this, LajiActivity.class);
                                    startActivity(intent);
                                    AdUtil.track("交叉推广_广告位", "广告位_" + finalT + "2", "点击" + item.getPkgName(), 1);
                                } else if (TextUtils.equals(item.action, Constant.BATTERY_COOL_ACTION)) {
                                    //电池降温
                                    Intent intent = new Intent(MBaseActivity.this, BatteryCoolingActivity.class);
                                    startActivity(intent);
                                    AdUtil.track("交叉推广_广告位", "广告位_" + finalT + "3", "点击" + item.getPkgName(), 1);
                                } else {
                                    com.android.ui.demo.util.Utils.openPlayStore(MBaseActivity.this, item.getPkgName(), AndroidSdk.getExtraData());
                                }
                            }
                        } else {
                            com.android.ui.demo.util.Utils.reactionForAction(MBaseActivity.this, AndroidSdk.getExtraData(), item.getPkgName(), item.getAction());
                            AdUtil.track("交叉推广_广告位", "广告位_" + finalT + j, "点击" + item.getPkgName(), 1);
                        }
                    }
                });
                if (tag.equals(TUIGUAN_SIDE) || tag.equals(TUIGUAN_SIDE_SOFT)) {
                    ((ListView) viewP).addHeaderView(view);
                } else {
                    ((LinearLayout) viewP).addView(view);
                }
            }

        }

    }

    public void tuiguangZhanshi(boolean isSoftCross, CrossItem item, String t, int j) {
        if (isSoftCross) {
            if (TextUtils.equals(item.action, Constant.RAM_CLEAN_ACTION)) {
                AdUtil.track("交叉推广_广告位", "广告位_" + t + "1", "展示" + item.pkgName, 1);
            } else if (TextUtils.equals(item.action, Constant.JUNK_CLEAN_ACTION)) {
                //垃圾清理
                AdUtil.track("交叉推广_广告位", "广告位_" + t + "2", "展示" + item.pkgName, 1);
            } else if (TextUtils.equals(item.action, Constant.BATTERY_COOL_ACTION)) {
                //电池降温
                AdUtil.track("交叉推广_广告位", "广告位_" + t + "3", "展示" + item.pkgName, 1);
            }
        } else {
            AdUtil.track("交叉推广_广告位", "广告位_" + t + j, "展示" + item.pkgName, 1);
        }
    }

    public void jumpTo(Class<?> classs) {
        Intent intent = new Intent(this, classs);
        startActivity(intent);
    }

    public void jumpToActivity(Class<?> classs, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, classs);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void jumpToActivity(Class<?> classs, Bundle bundle) {
        Intent intent = new Intent(this, classs);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void jumpToActivity(Class<?> classs, int requestCode) {
        Intent intent = new Intent(this, classs);
        startActivityForResult(intent, requestCode);
    }

    public int getStatusHeight(Activity activity) {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public int getDaoHangHeight(Context context) {
        int result = 0;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void full() {
        setHideVirtualKey(getWindow());
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setHideVirtualKey(getWindow());
            }
        });
    }

    public void setHideVirtualKey(Window window) {
        //保持布局状态
        int uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }

    public int dp2px(int dp) {
        return (int) (Resources.getSystem().getDisplayMetrics().density * dp);
    }

    public void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }


    public void toggleEditAnimation(int resId1, int resId2) {
        final View searchView = findViewById(resId1);
        View normalView = findViewById(resId2);

        final View visibleView, invisibleView;
        if (searchView.getVisibility() == View.GONE) {
            visibleView = normalView;
            invisibleView = searchView;
        } else {
            visibleView = searchView;
            invisibleView = normalView;
        }
        final ObjectAnimator invis2vis = ObjectAnimator.ofFloat(invisibleView, "rotationY", -90, 0);
        invis2vis.setDuration(500);
        invis2vis.setInterpolator(new LinearInterpolator());
        ObjectAnimator vis2invis = ObjectAnimator.ofFloat(visibleView, "rotationY", 0, 90);
        vis2invis.setDuration(500);
        vis2invis.setInterpolator(new LinearInterpolator());

        vis2invis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                visibleView.setVisibility(View.GONE);
                invisibleView.setVisibility(View.VISIBLE);
                invis2vis.start();
            }
        });
        vis2invis.start();
    }

    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

}
