package com.easy.junk.easyactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.clean.core.CleanManager;
import com.easy.clean.easydb.CleanDBHelper;
import com.easy.clean.easyutils.PreData;
import com.easy.junk.R;
import com.easy.junk.easycustomadapter.NotifiSettingAdapter;
import com.easy.clean.entity.JunkInfo;
import com.easy.junk.easytools.MyConstant;

import java.util.ArrayList;
import java.util.List;

import static com.easy.clean.easydb.CleanDBHelper.TableType.Notification;

/**
 * Created by on 2017/4/13.
 */

public class EasyNotifingSettingActivity extends BaseActivity {
    FrameLayout title_left;
    List<String> isnotifiWhiteList = CleanDBHelper.getInstance(this).getWhiteList(Notification);
    RelativeLayout notifi_button_rl;
    TextView title_name;
    MyApplication myApplication;
    NotifiSettingAdapter adapter;
    ImageView title_right;
    ListView list_si;
    View view_set;
    ArrayList<JunkInfo> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi);
        myApplication = (MyApplication) getApplication();
        title_name.setText(R.string.side_notifi);
        title_right.setVisibility(View.VISIBLE);
        notifi_button_rl.setVisibility(View.GONE);
        if (PreData.getDB(this, MyConstant.KEY_NOTIFI, true)) {
            title_right.setImageResource(R.mipmap.notifi_check_passed);
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
                    if (PreData.getDB(EasyNotifingSettingActivity.this, MyConstant.KEY_NOTIFI, true)) {
                        PreData.putDB(EasyNotifingSettingActivity.this, MyConstant.KEY_NOTIFI, false);
                        title_right.setImageResource(R.mipmap.side_check_normal);
                        view_set.setVisibility(View.VISIBLE);
                    } else {
                        PreData.putDB(EasyNotifingSettingActivity.this, MyConstant.KEY_NOTIFI, true);
                        title_right.setImageResource(R.mipmap.notifi_check_passed);
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
