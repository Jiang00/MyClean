package com.supers.clean.junk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.NotifiAdapter;
import com.supers.clean.junk.customeview.DeleteListView;
import com.supers.clean.junk.entity.NotifiInfo;
import com.supers.clean.junk.service.NotificationMonitor;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;

import java.util.ArrayList;

import static com.supers.clean.junk.service.NotificationMonitor.NOTIFI_ACTION;

/**
 * Created by Ivy on 2017/4/13.
 */

public class NotifiActivity extends Activity {
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    DeleteListView list_si;
    TextView white_wu;
    RelativeLayout notifi_button_rl;
    Button notifi_button_clean;

    private NotifiAdapter adapter;
    private MyApplication myApplication;
    private ArrayList<NotifiInfo> list;
    private NotifiReceiver receiver;

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
        list_si = (DeleteListView) findViewById(R.id.list_si);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        white_wu = (TextView) findViewById(R.id.white_wu);
        notifi_button_rl = (RelativeLayout) findViewById(R.id.notifi_button_rl);
        notifi_button_clean = (Button) findViewById(R.id.notifi_button_clean);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi);
        myApplication = (MyApplication) getApplication();
        startService(new Intent(this, NotificationMonitor.class));
        title_name.setText(R.string.side_notifi);
        title_right.setImageResource(R.mipmap.main_setting);
        title_right.setVisibility(View.VISIBLE);
        setListener();
        adapter = new NotifiAdapter(this);
        list_si.setAdapter(adapter);
        list = myApplication.getNotifiList();
        if (list != null && list.size() != 0) {
            adapter.addDataList(list);
            adapter.notifyDataSetChanged();
        } else {
            white_wu.setVisibility(View.VISIBLE);
            notifi_button_rl.setVisibility(View.GONE);
        }
        receiver = new NotifiReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(NOTIFI_ACTION));

    }

    private void setListener() {
        title_left.setOnClickListener(nOnClickListener);
        title_right.setOnClickListener(nOnClickListener);
        notifi_button_clean.setOnClickListener(nOnClickListener);
        list_si.setRemoveListener(new DeleteListView.RemoveListener() {
            @Override
            public void removeItem(DeleteListView.RemoveDirection direction, int position) {
                NotifiInfo info = adapter.getItem(position);
                myApplication.removeNotifi(info);
                LocalBroadcastManager.getInstance(NotifiActivity.this).sendBroadcast(new Intent(NOTIFI_ACTION));
            }
        });
        list_si.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotifiInfo info = adapter.getData(position);
                myApplication.removeNotifi(info);
                LocalBroadcastManager.getInstance(NotifiActivity.this).sendBroadcast(new Intent(NOTIFI_ACTION));
                CommonUtil.doStartApplicationWithPackageName(NotifiActivity.this, info.pkg);
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
                    myApplication.clearNotifi();
                    Bundle bundle = new Bundle();
                    bundle.putInt("num", adapter.getCount());
                    jumpToActivity(SuccessActivity.class, bundle, 1);
                    LocalBroadcastManager.getInstance(NotifiActivity.this).sendBroadcast(new Intent(NOTIFI_ACTION));
                    break;
            }
        }
    };

    public void jumpToActivity(Class<?> classs, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, classs);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void jumpToActivity(Class<?> classs, int requestCode) {
        Intent intent = new Intent(this, classs);
        startActivityForResult(intent, requestCode);
    }

    public class NotifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), NOTIFI_ACTION)) {
                list = myApplication.getNotifiList();
                adapter.upList(list);
                adapter.notifyDataSetChanged();
                if (list != null && list.size() != 0) {
                    white_wu.setVisibility(View.INVISIBLE);
                    notifi_button_rl.setVisibility(View.VISIBLE);
                } else {
                    white_wu.setVisibility(View.VISIBLE);
                    notifi_button_rl.setVisibility(View.GONE);
                }

            }

        }

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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
