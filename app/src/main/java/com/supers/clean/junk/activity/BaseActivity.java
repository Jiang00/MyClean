package com.supers.clean.junk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.JsonParser;
import com.supers.clean.junk.util.PreData;
import com.supers.clean.junk.entity.JsonData;

import java.util.Locale;

/**
 * Created by on 2017/2/28.
 */

public class BaseActivity extends AppCompatActivity {
    private Toast toast;
    protected String tuiguang = "com.eosmobi.applock";
    protected String extraData;
    private JsonData data;

    protected boolean onPause = false;
    protected boolean onDestroyed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);

        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (PreData.getDB(this, Constant.IS_ACTION_BAR, true)) {
            full();
        }

        if (data == null) {
            try {
                data = JsonParser.getInstance().fromJson(AndroidSdk.getExtraData(), JsonData.class);
                PreData.putDB(this, Constant.FULL_MAIN, data.full_main);
                PreData.putDB(this, Constant.FULL_MANAGER, data.full_manager);
                PreData.putDB(this, Constant.FULL_MESSAGE, data.full_message);
                PreData.putDB(this, Constant.FULL_SUCCESS, data.full_success);
                PreData.putDB(this, Constant.FULL_SETTING, data.full_setting);
                PreData.putDB(this, Constant.FULL_UNLOAD, data.full_unload);
                PreData.putDB(this, Constant.FULL_FLOAT, data.full_float);
                PreData.putDB(this, Constant.FULL_COOL, data.full_cool);
                PreData.putDB(this, Constant.FULL_SHORTCUT, data.full_shortcut);
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPause = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPause = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyed = true;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        View view_title_bar = findViewById(R.id.view_title_bar);
        ViewGroup.LayoutParams linearParams = view_title_bar.getLayoutParams();
        linearParams.height = getStatusHeight(this);
        view_title_bar.setLayoutParams(linearParams);
        findId();
    }

    protected void findId() {

    }

    public void tuiGuang() {
        extraData = AndroidSdk.getExtraData();
    }

    public void jumpTo(Class<?> classs) {
        Intent intent = new Intent(this, classs);
        startActivity(intent);
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

    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

    public void changeAppLanguage() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (TextUtils.equals(language, "ar")) {
            Locale myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }
        Log.e("rqy", "--language--" + language);
        // 本地语言设置
    }
}
