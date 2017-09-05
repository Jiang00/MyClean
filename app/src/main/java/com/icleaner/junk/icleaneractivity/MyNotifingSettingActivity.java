package com.icleaner.junk.icleaneractivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icleaner.clean.core.CleanManager;
import com.icleaner.clean.mydb.CleanDBHelper;
import com.icleaner.clean.utils.PreData;
import com.icleaner.junk.R;
import com.icleaner.junk.mycustomadapter.NotifiSettingAdapter;
import com.icleaner.clean.entity.JunkInfo;
import com.icleaner.junk.mytools.MyConstant;

import java.util.ArrayList;
import java.util.List;

import static com.icleaner.clean.mydb.CleanDBHelper.TableType.Notification;

/**
 * Created by on 2017/4/13.
 */

public class MyNotifingSettingActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    MyApplication myApplication;
    NotifiSettingAdapter adapter;
    ArrayList<JunkInfo> list;
    ImageView title_right;
    ListView list_si;
    View view_set;
    List<String> isnotifiWhiteList = CleanDBHelper.getInstance(this).getWhiteList(Notification);
    RelativeLayout notifi_button_rl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi);
        myApplication = (MyApplication) getApplication();
        title_name.setText(R.string.side_notifi);
        title_right.setVisibility(View.VISIBLE);
        notifi_button_rl.setVisibility(View.GONE);
        if (PreData.getDB(this, MyConstant.KEY_NOTIFI, true)) {
            title_right.setImageResource(R.mipmap.notifi_check);
            view_set.setVisibility(View.GONE);
        } else {
            title_right.setImageResource(R.mipmap.notifi_check_normal);
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

    private void setListener() {
        title_left.setOnClickListener(nOnClickListener);
        title_right.setOnClickListener(nOnClickListener);
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

    View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    if (PreData.getDB(MyNotifingSettingActivity.this, MyConstant.KEY_NOTIFI, true)) {
                        PreData.putDB(MyNotifingSettingActivity.this, MyConstant.KEY_NOTIFI, false);
                        title_right.setImageResource(R.mipmap.notifi_check_normal);
                        view_set.setVisibility(View.VISIBLE);
                    } else {
                        PreData.putDB(MyNotifingSettingActivity.this, MyConstant.KEY_NOTIFI, true);
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