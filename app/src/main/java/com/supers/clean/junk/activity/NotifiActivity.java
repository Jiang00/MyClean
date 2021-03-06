package com.supers.clean.junk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.notification.NotificationCallBack;
import com.android.clean.notification.NotificationInfo;
import com.android.clean.notification.NotificationMonitorService;
import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.NotifiAdapter;
import com.android.clean.util.Util;
import com.android.clean.util.PreData;
import com.android.clean.util.Constant;
import com.supers.clean.junk.util.AdUtil;

import java.util.ArrayList;


/**
 * Created by Ivy on 2017/4/13.
 */

public class NotifiActivity extends Activity {
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    ListView list_si;
    FrameLayout white_wu;
    RelativeLayout notifi_button_rl;
    Button notifi_button_clean;

    private NotifiAdapter adapter;
    private MyApplication myApplication;
    private LinearLayout ll_ad;

    public int getStatusHeight(Activity activity) {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected void findId() {
        View view_title_bar = findViewById(R.id.view_title_bar);
        ViewGroup.LayoutParams linearParams = view_title_bar.getLayoutParams();
        linearParams.height = getStatusHeight(this);
        view_title_bar.setLayoutParams(linearParams);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        list_si = (ListView) findViewById(R.id.list_si);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        white_wu = (FrameLayout) findViewById(R.id.white_wu);
        notifi_button_rl = (RelativeLayout) findViewById(R.id.notifi_button_rl);
        notifi_button_clean = (Button) findViewById(R.id.notifi_button_clean);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
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

    private void full() {
        setHideVirtualKey(getWindow());
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                setHideVirtualKey(getWindow());
            }
        });
    }

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

        setContentView(R.layout.layout_notifi);
        if (!PreData.getDB(this, Constant.BILL_YOUXIAO, true)) {
            AndroidSdk.loadFullAd(AdUtil.DEFAULT, null);
        }
        findId();
        myApplication = (MyApplication) getApplication();
        startService(new Intent(this, NotificationMonitorService.class));
        title_name.setText(R.string.side_notifi);
        title_right.setImageResource(R.mipmap.main_setting);
        title_right.setVisibility(View.VISIBLE);
        setListener();
        adapter = new NotifiAdapter(this);
        list_si.setAdapter(adapter);

        CleanManager.getInstance(this).addNotificationCallBack(notificationCallBack);
        addAd();
    }

    private void addAd() {
        View native_xiao = AdUtil.getNativeAdView(this,"", R.layout.native_ad_3);
        if (ll_ad != null && native_xiao != null) {
            ll_ad.addView(native_xiao);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }


    NotificationCallBack notificationCallBack = new NotificationCallBack() {
        @Override
        public void notificationChanged(final ArrayList<NotificationInfo> notificationList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (notificationList != null && notificationList.size() != 0) {
                        adapter.upList(notificationList);
                        adapter.notifyDataSetChanged();
                        white_wu.setVisibility(View.INVISIBLE);
                        notifi_button_rl.setVisibility(View.VISIBLE);
                    } else {
                        white_wu.setVisibility(View.VISIBLE);
                        notifi_button_rl.setVisibility(View.GONE);
                    }
                }
            });

        }
    };


    private void setListener() {
        title_left.setOnClickListener(nOnClickListener);
        title_right.setOnClickListener(nOnClickListener);
        notifi_button_clean.setOnClickListener(nOnClickListener);
//        list_si.setRemoveListener(new DeleteListView.RemoveListener() {
//            @Override
//            public void removeItem(DeleteListView.RemoveDirection direction, int position) {
//                NotificationInfo info = adapter.getItem(position);
//                CleanManager.getInstance(NotifiActivity.this).notificationChanged(info, false);
//            }
//        });
        list_si.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationInfo info = adapter.getData(position);
                CleanManager.getInstance(NotifiActivity.this).notificationChanged(info, false);
                Util.doStartApplicationWithPackageName(NotifiActivity.this, info.pkg);
            }
        });
    }

    View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    jumpToActivity(NotifiSettingActivity.class, 1);
                    break;
                case R.id.notifi_button_clean:
                    Bundle bundle = new Bundle();
                    bundle.putInt("num", adapter.getCount());
                    jumpToActivity(SuccessActivity.class, bundle, 1);
                    CleanManager.getInstance(NotifiActivity.this).clearNotificationInfo();
                    break;
            }
        }
    };


    public void jumpTo(Class<?> classs) {
        Intent intent = new Intent(this, classs);
        startActivity(intent);
    }

    public void jumpToActivity(Class<?> classs, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, classs);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void jumpToActivity(Class<?> classs, int requestCode) {
        Intent intent = new Intent(this, classs);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void onBackPressed() {
        if ("notifi".equals(getIntent().getStringExtra("from"))) {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        CleanManager.getInstance(this).addNotificationCallBack(notificationCallBack);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            if (!PreData.getDB(this, Constant.KEY_NOTIFI, true)) {
                onBackPressed();
            }
        }
    }
}
