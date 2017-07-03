package com.bruder.clean.activity;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.bruder.clean.customeview.DeleteListMyView;
import com.bruder.clean.junk.R;
import com.bruder.clean.myadapter.NotifiMyAdapter;
import com.bruder.clean.util.Constant;
import com.cleaner.heart.CleanManager;
import com.cleaner.notification.NotificationCallBack;
import com.cleaner.notification.NotificationInfo;
import com.cleaner.notification.NotificationsMonitorService;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;

import java.util.ArrayList;


/**
 */

public class NotifingActivity extends Activity {
    DeleteListMyView list_si;
    TextView white_wu;
    private NotifiMyAdapter adapter;
    private MyApplication myApplication;
    RelativeLayout notifi_button_rl;
    Button notifi_button_clean;
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    TextView notifi_cpu;

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
        list_si = (DeleteListMyView) findViewById(R.id.list_si);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        white_wu = (TextView) findViewById(R.id.white_wu);
        notifi_button_rl = (RelativeLayout) findViewById(R.id.notifi_button_rl);
        notifi_button_clean = (Button) findViewById(R.id.notifi_button_clean);
        notifi_cpu = (TextView) findViewById(R.id.notifi_cpu);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);

        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (DataPre.getDB(this, Constant.IS_ACTION_BAR, true)) {
            full();
        }

        setContentView(R.layout.layout_notifi);
        AndroidSdk.loadFullAd(AndroidSdk.FULL_TAG_PAUSE);
        findId();
        myApplication = (MyApplication) getApplication();
        startService(new Intent(this, NotificationsMonitorService.class));
        title_name.setText(R.string.side_notifi);
        title_right.setImageResource(R.mipmap.main_setting);
        title_right.setVisibility(View.VISIBLE);
        setListener();
        adapter = new NotifiMyAdapter(this);
        list_si.setAdapter(adapter);

        CleanManager.getInstance(this).addNotificationCallBack(notificationCallBack);

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

    NotificationCallBack notificationCallBack = new NotificationCallBack() {
        @Override
        public void notificationChanged(ArrayList<NotificationInfo> notificationList) {

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
    };


    private void setListener() {
        title_left.setOnClickListener(nOnClickListener);
        title_right.setOnClickListener(nOnClickListener);
        notifi_button_clean.setOnClickListener(nOnClickListener);
        list_si.setRemoveListener(new DeleteListMyView.RemoveListener() {
            @Override
            public void removeItem(DeleteListMyView.RemoveDirection direction, int position) {
                NotificationInfo info = adapter.getItem(position);
                CleanManager.getInstance(NotifingActivity.this).notificationChanged(info, false);
            }
        });
        list_si.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationInfo info = adapter.getData(position);
                CleanManager.getInstance(NotifingActivity.this).notificationChanged(info, false);
                Util.doStartApplicationWithPackageName(NotifingActivity.this, info.pkg);
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
                    jumpToActivity(NotifiSettActivity.class, 1);
                    break;
                case R.id.notifi_button_clean:
                    Bundle bundle = new Bundle();
                    bundle.putInt("num", adapter.getCount());
                    jumpToActivity(SuccessingActivity.class, bundle, 1);
                    CleanManager.getInstance(NotifingActivity.this).clearNotificationInfo();
                    break;
            }
        }
    };


    public void jumpTo(Class<?> classs) {
        Intent intent = new Intent(this, classs);
        startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            if (!DataPre.getDB(this, Constant.KEY_NOTIFI, true)) {
                onBackPressed();
            }
        }
    }
}
