package com.froumobic.clean.junk.mactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.db.CleanDBHelper;
import com.android.clean.util.PreData;
import com.froumobic.clean.junk.R;
import com.froumobic.clean.junk.adapter.NotifiSettingAdapter;
import com.android.clean.entity.JunkInfo;
import com.froumobic.clean.junk.util.Constant;

import java.util.ArrayList;
import java.util.List;

import static com.android.clean.db.CleanDBHelper.TableType.Notification;

/**
 * Created by froumobi on 2017/4/13.
 */

public class NotifiSettingActivity extends MBaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    ListView list_si;
    View view_set;
    RelativeLayout notifi_button_rl;

    NotifiSettingAdapter adapter;
    MyApplication myApplication;
    ArrayList<JunkInfo> list;
    List<String> isnotifiWhiteList = CleanDBHelper.getInstance(this).getWhiteList(Notification);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi_setting);
        myApplication = (MyApplication) getApplication();
        title_name.setText(R.string.side_notifi);
        title_right.setVisibility(View.VISIBLE);
        notifi_button_rl.setVisibility(View.GONE);
        if (PreData.getDB(this, Constant.KEY_NOTIFI, true)) {
            title_right.setImageResource(R.mipmap.notifi_check);
            view_set.setVisibility(View.GONE);
        } else {
            title_right.setImageResource(R.mipmap.notifi_normal);
            view_set.setVisibility(View.VISIBLE);
        }
        adapter = new NotifiSettingAdapter(this);
        list = CleanManager.getInstance(this).getAppList();
        ArrayList<JunkInfo> listSoft = new ArrayList<>();
        for (JunkInfo info : list) {
            if (!isnotifiWhiteList.contains(info.pkg)) {
                listSoft.add(info);
            } else {
                info.isnotifiWhiteList = true;
            }
        }
        list.removeAll(listSoft);
        list.addAll(0, listSoft);
        adapter.addDataList(list);
        list_si.setAdapter(adapter);
        setListener();
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        list_si = (ListView) findViewById(R.id.list_si);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        view_set = findViewById(R.id.view_set);
        notifi_button_rl = (RelativeLayout) findViewById(R.id.notifi_button_rl);

    }

    private void setListener() {
        title_left.setOnClickListener(nOnClickListener);
        title_right.setOnClickListener(nOnClickListener);
    }

    View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    if (PreData.getDB(NotifiSettingActivity.this, Constant.KEY_NOTIFI, true)) {
                        PreData.putDB(NotifiSettingActivity.this, Constant.KEY_NOTIFI, false);
                        title_right.setImageResource(R.mipmap.notifi_normal);
                        view_set.setVisibility(View.VISIBLE);
                    } else {
                        PreData.putDB(NotifiSettingActivity.this, Constant.KEY_NOTIFI, true);
                        title_right.setImageResource(R.mipmap.notifi_check);
                        view_set.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}