package com.myboost.junk.activityprivacy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.myboost.clean.core.CleanManager;
import com.myboost.clean.privacydb.CleanDBHelper;
import com.myboost.clean.entity.JunkInfo;
import com.myboost.junk.R;
import com.myboost.junk.customadapterprivacy.IgnoreListViewAdapter;
import com.myboost.junk.toolsprivacy.MyConstantPrivacy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class BoostIgnoresAvtivity extends BaseActivity {
    IgnoreListViewAdapter adapter;
    TextView white_wu;
    List<JunkInfo> white_list;
    ListView listView;
    TextView title_name;
    ImageView title_right;
    private List<String> whiteList;
    FrameLayout title_left;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ignore);
        title_name.setText(R.string.white_list_name);
        title_right.setVisibility(View.VISIBLE);
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);

        white_list = new ArrayList<>();
        adapter = new IgnoreListViewAdapter(this);
        listView.setAdapter(adapter);
        initDAta();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    Intent intent = new Intent(BoostIgnoresAvtivity.this, IgnoresAddActivityBoost.class);
                    BoostIgnoresAvtivity.this.startActivityForResult(intent, 1);
                    break;
            }
        }
    };


    private void initDAta() {
        whiteList = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.Ram);
        white_list.clear();
        for (JunkInfo info : CleanManager.getInstance(this).getAppList()) {
            if (whiteList.contains(info.pkg)) {
                info.isWhiteList = true;
                white_list.add(info);
            }
        }
        adapter.upList(white_list);
        adapter.notifyDataSetChanged();
        if (white_list.size() == 0) {
            white_wu.setVisibility(View.VISIBLE);
        } else {
            white_wu.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        title_right = (ImageView) findViewById(R.id.title_right);
        listView = (ListView) findViewById(R.id.list_si);
        white_wu = (TextView) findViewById(R.id.white_wu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            initDAta();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(MyConstantPrivacy.WHITE_RESUIL);
        finish();
    }
}
