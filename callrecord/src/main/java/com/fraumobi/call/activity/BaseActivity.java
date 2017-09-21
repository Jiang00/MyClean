package com.fraumobi.call.activity;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;

import java.util.Locale;

/**
 * Created by on 2017/2/28.
 */

public class BaseActivity extends AppCompatActivity {
    private Toast toast;
    protected String tuiguang = "com.eosmobi.applock";
    protected String extraData;

    protected boolean onPause = false;
    protected boolean onDestroyed = false;
    protected boolean onResume = false;

    protected static final String DEFAULT_SYSTEM_LANGUAGE = "system_language";
    protected static final String LANGUAGE_CHANGE_ACTION = "language_change_action";

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String language = intent.getStringExtra(DEFAULT_SYSTEM_LANGUAGE);
            if (TextUtils.equals(language, DEFAULT_SYSTEM_LANGUAGE)) {
                Locale locale = getSystemLanguage();
                if (locale == null) {
                    return;
                }
                Resources res = getResources();
                Configuration conf = res.getConfiguration();
                DisplayMetrics dm = res.getDisplayMetrics();
                conf.locale = locale;
                res.updateConfiguration(conf, dm);
            } else {
                if (TextUtils.isEmpty(language)) {
                    return;
                }
                changeAppLanguage(language);
            }
            languageChange();

        }
    };

    public Locale getSystemLanguage() {
        try {
            return ActivityManagerNative.getDefault().getConfiguration().locale;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AndroidSdk.onCreate(this);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(mBroadcastReceiver, new IntentFilter(LANGUAGE_CHANGE_ACTION));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);

        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (PreData.getDB(this, Constant.IS_ACTION_BAR, true)) {
            full();
        }

    }

    /**
     * see languageChange
     *
     * @param language
     */
    public void setLanguage(String language) {
        if (TextUtils.isEmpty(language)) {
            return;
        }
        PreData.putDB(this, DEFAULT_SYSTEM_LANGUAGE, language);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent lanuguageIntent = new Intent(LANGUAGE_CHANGE_ACTION);
        lanuguageIntent.putExtra(DEFAULT_SYSTEM_LANGUAGE, language);
        localBroadcastManager.sendBroadcast(lanuguageIntent);
    }

    public boolean recreateActivity() {
        return true;
    }


    public void languageChange() {
    }


    private void changeAppLanguage(String language) {
        // 本地语言设置
        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        DisplayMetrics dm = res.getDisplayMetrics();
        if (TextUtils.equals(language, "cn")) {
            conf.locale = new Locale("zh", "CN");
        } else if (TextUtils.equals(language, "in")) {
            conf.locale = new Locale("in", "ID");
        } else {
            Locale myLocale = new Locale(language);
            conf.locale = myLocale;
        }
        res.updateConfiguration(conf, dm);
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
    protected void onDestroy() {
        super.onDestroy();
        onDestroyed = true;
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
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

    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }

}
