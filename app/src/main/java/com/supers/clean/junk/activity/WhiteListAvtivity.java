package com.supers.clean.junk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.db.CleanDBHelper;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.WhiteListAdapter;
import com.android.clean.entity.JunkInfo;
import com.supers.clean.junk.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/3/8.
 */

public class WhiteListAvtivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView title_right;
    WhiteListAdapter adapter;
    ListView listView;
    TextView white_wu;
    List<JunkInfo> white_list;
    private List<String> whiteList;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ignore);
        title_name.setText(R.string.white_list_name);
        title_right.setVisibility(View.VISIBLE);
        title_left.setOnClickListener(clickListener);
        title_right.setOnClickListener(clickListener);
        whiteList = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.Ram);
        for (String info : whiteList) {
            Log.e("whitelist", info);
        }


        white_list = new ArrayList<>();
        adapter = new WhiteListAdapter(this);
        listView.setAdapter(adapter);
        initDAta();
    }

    private void initDAta() {
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

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.title_right:
                    Intent intent = new Intent(WhiteListAvtivity.this, WhiteListAddActivity.class);
                    WhiteListAvtivity.this.startActivityForResult(intent, 1);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        setResult(Constant.WHITE_RESUIL);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            initDAta();
        }
    }
}
