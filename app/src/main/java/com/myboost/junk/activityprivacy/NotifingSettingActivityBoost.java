package com.myboost.junk.activityprivacy;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myboost.clean.core.CleanManager;
import com.myboost.clean.privacydb.CleanDBHelper;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.customadapterprivacy.PrivacyNotifiSettingAdapter;
import com.myboost.clean.entity.JunkInfo;
import com.myboost.junk.toolsprivacy.MyConstantPrivacy;

import java.util.ArrayList;
import java.util.List;

import static com.myboost.clean.privacydb.CleanDBHelper.TableType.Notification;

/**
 * Created by on 2017/4/13.
 */

public class NotifingSettingActivityBoost extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView notifi_switch;
    ListView list_si;
    View view_set;
    List<String> isnotifiWhiteList = CleanDBHelper.getInstance(this).getWhiteList(Notification);
    RelativeLayout notifi_button_rl, notifi_switch_r;
    MyApplication myApplication;
    PrivacyNotifiSettingAdapter adapter;
    ArrayList<JunkInfo> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifi);
        myApplication = (MyApplication) getApplication();
        title_name.setText(R.string.side_notifi);
        notifi_switch.setVisibility(View.VISIBLE);
        notifi_button_rl.setVisibility(View.GONE);
        notifi_switch_r.setVisibility(View.VISIBLE);
        if (PreData.getDB(this, MyConstantPrivacy.KEY_NOTIFI, true)) {
            notifi_switch.setImageResource(R.mipmap.side_check_passed);
            view_set.setVisibility(View.GONE);
        } else {
            notifi_switch.setImageResource(R.mipmap.notifi_check_normal);
            view_set.setVisibility(View.VISIBLE);
        }
        adapter = new PrivacyNotifiSettingAdapter(this);
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
        notifi_switch = (ImageView) findViewById(R.id.notifi_switch);
        view_set = findViewById(R.id.view_set);
        notifi_button_rl = (RelativeLayout) findViewById(R.id.notifi_button_rl);
        notifi_switch_r = (RelativeLayout) findViewById(R.id.notifi_switch_r);
    }

    private void setListener() {
        title_left.setOnClickListener(nOnClickListener);
        notifi_switch.setOnClickListener(nOnClickListener);
    }

    View.OnClickListener nOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.notifi_switch:
                    if (PreData.getDB(NotifingSettingActivityBoost.this, MyConstantPrivacy.KEY_NOTIFI, true)) {
                        PreData.putDB(NotifingSettingActivityBoost.this, MyConstantPrivacy.KEY_NOTIFI, false);
                        notifi_switch.setImageResource(R.mipmap.notifi_check_normal);
                        view_set.setVisibility(View.VISIBLE);
                    } else {
                        PreData.putDB(NotifingSettingActivityBoost.this, MyConstantPrivacy.KEY_NOTIFI, true);
                        notifi_switch.setImageResource(R.mipmap.side_check_passed);
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
