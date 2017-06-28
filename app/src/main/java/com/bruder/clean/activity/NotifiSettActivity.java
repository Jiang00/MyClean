package com.bruder.clean.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bruder.clean.junk.R;
import com.bruder.clean.myadapter.NotifiSettingAdapter;
import com.bruder.clean.util.Constant;
import com.cleaner.entity.JunkInfo;
import com.cleaner.heart.CleanManager;
import com.cleaner.sqldb.CleanDBHelper;
import com.cleaner.util.DataPre;

import java.util.ArrayList;
import java.util.List;

import static com.cleaner.sqldb.CleanDBHelper.TableType.Notification;

/**
 * Created by Ivy on 2017/4/13.
 */

public class NotifiSettActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name, tishi;
    ArrayList<JunkInfo> list;
    List<String> isnotifiWhiteList = CleanDBHelper.getInstance(this).getWhiteList(Notification);
    ImageView title_right;
    ListView list_si;
    NotifiSettingAdapter adapter;
    MyApplication myApplication;
    View view_set;
    RelativeLayout notifi_button_rl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi);
        myApplication = (MyApplication) getApplication();
        title_name.setText(R.string.side_notifi);
        tishi.setVisibility(View.VISIBLE);
        title_right.setVisibility(View.VISIBLE);
        notifi_button_rl.setVisibility(View.GONE);
        if (DataPre.getDB(this, Constant.KEY_NOTIFI, true)) {
            title_right.setImageResource(R.mipmap.side_check_passed3);
            view_set.setVisibility(View.GONE);
        } else {
            title_right.setImageResource(R.mipmap.side_check_normal);
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
    public void onBackPressed() {
        setResult(1);
        finish();
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
        tishi = (TextView) findViewById(R.id.tishi);
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
                    if (DataPre.getDB(NotifiSettActivity.this, Constant.KEY_NOTIFI, true)) {
                        DataPre.putDB(NotifiSettActivity.this, Constant.KEY_NOTIFI, false);
                        title_right.setImageResource(R.mipmap.side_check_normal);
                        tishi.setVisibility(View.GONE);
                        view_set.setVisibility(View.VISIBLE);
                    } else {
                        DataPre.putDB(NotifiSettActivity.this, Constant.KEY_NOTIFI, true);
                        title_right.setImageResource(R.mipmap.side_check_passed);
                        tishi.setVisibility(View.VISIBLE);
                        view_set.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

}
