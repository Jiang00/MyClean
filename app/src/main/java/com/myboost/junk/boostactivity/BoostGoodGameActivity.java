package com.myboost.junk.activityprivacy;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.myboost.clean.core.CleanManager;
import com.myboost.clean.entity.JunkInfo;
import com.myboost.clean.goodgameprivacy.GameBooster;
import com.myboost.clean.privacydb.CleanDBHelper;
import com.myboost.clean.utilsprivacy.LoadManager;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.customadapterprivacy.PrivacyJiaGoodGameAdapter;
import com.myboost.junk.privacycustomview.GridViewAdAdapterPrivacy;
import com.myboost.junk.toolsprivacy.MyConstantPrivacy;
import com.myboost.junk.toolsprivacy.PrivacyShortCutUtils;
import com.myboost.junk.toolsprivacy.SetAdUtilPrivacy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 2017/5/5.
 */

public class BoostGoodGameActivity extends BaseActivity {
    private static final int ADD_DATA = 0;
    private GridViewAdAdapterPrivacy gridViewAdAdapter;
    private ArrayList<String> list;
    private List<JunkInfo> gboost_add, listEdit;
    LinearLayout ll_add_game;
    FrameLayout add_left;
    ListView list_game;
    EditText search_edit_text;
    TextView game_size;
    FrameLayout title_left;
    TextView title_name;
    private PrivacyJiaGoodGameAdapter whiteListAdapter;
    private boolean search;
    TextView gboost_item_textview;
    GridView gboost_gridview;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_DATA:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gboost1);
        mHandler = new Handler();

        title_left.setOnClickListener(clickListener);
        add_left.setOnClickListener(clickListener);
        title_name.setText(R.string.gboost_0);

        gboost_add = new ArrayList<>();
        listEdit = new ArrayList<>();
        initList();

        if (search) {
            initData();
            search = false;
        } else {
            search_edit_text.setText("");
            search_edit_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    upData(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });
            search = true;
        }
    }

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        gboost_gridview = (GridView) findViewById(R.id.gboost_gridview);
        ll_add_game = (LinearLayout) findViewById(R.id.ll_add_game);
        add_left = (FrameLayout) findViewById(R.id.add_left);
        list_game = (ListView) findViewById(R.id.list_game);
        gboost_item_textview = (TextView) findViewById(R.id.gboost_item_textview);
        game_size = (TextView) findViewById(R.id.game_size);
    }

    private void addData() {
        if (PreData.getDB(BoostGoodGameActivity.this, MyConstantPrivacy.GBOOST_LUN, true)) {
            PreData.putDB(BoostGoodGameActivity.this, MyConstantPrivacy.GBOOST_LUN, true);
            shortGame1(false);
        }
        list.addAll(GameBooster.getInstalledGameList(BoostGoodGameActivity.this));
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> gboost_names = CleanDBHelper.getInstance(BoostGoodGameActivity.this).getWhiteList(CleanDBHelper.TableType.GameBoost);
                for (String pkg : gboost_names) {
                    if (LoadManager.getInstance(BoostGoodGameActivity.this).isPkgInstalled(pkg)) {
                        list.add(pkg);
                    }
                }
                Message msg = mHandler.obtainMessage();
                msg.what = ADD_DATA;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        list.add(0, getString(R.string.gboost_7));
                        game_size.setText(getString(R.string.gboost_13, list.size() - 1));
                        gridViewAdAdapter = new GridViewAdAdapterPrivacy(BoostGoodGameActivity.this, list);
                        gboost_gridview.setAdapter(gridViewAdAdapter);
                        gboost_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    SetAdUtilPrivacy.track("游戏加速页面", "点击添加游戏", "", 1);
                                    ll_add_game.setVisibility(View.VISIBLE);
                                    whiteListAdapter = new PrivacyJiaGoodGameAdapter(BoostGoodGameActivity.this, list);
                                    list_game.setAdapter(whiteListAdapter);
                                    initData();
                                } else {
                                    try {
                                        SetAdUtilPrivacy.track("游戏加速页面", "点击启动游戏", list.get(position), 1);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("from", "GBoost");
                                        bundle.putString("packageName", list.get(position));
                                        jumpToActivity(BoostDeepingActivity.class, bundle);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void initList() {
        list = new ArrayList<>();
        addData();
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_left:
                    ll_add_game.setVisibility(View.GONE);
                    gridViewAdAdapter.notifyDataSetChanged();
                    game_size.setText(getString(R.string.gboost_13, list.size() - 1));
                    shortGame1(false);
                    break;
                case R.id.title_left:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (ll_add_game.getVisibility() == View.VISIBLE) {
                    ll_add_game.setVisibility(View.GONE);
                    gridViewAdAdapter.notifyDataSetChanged();
                    shortGame1(false);
                } else {
                    return super.onKeyDown(keyCode, event);
                }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gridViewAdAdapter != null) {
            gridViewAdAdapter.notifyDataSetChanged();
        }
    }

    //游戏加速快捷键（就一个图片）
    private void shortGame1(boolean isChuangjian) {
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.setComponent(new ComponentName(getPackageName(),
                BoostGoodGameActivity.class.getName()));
        String title = BoostGoodGameActivity.this.getString(R.string.gboost_0);
        PreData.putDB(BoostGoodGameActivity.this, MyConstantPrivacy.GBOOST_LUN, true);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.gboost_short);
        PrivacyShortCutUtils.addShortcut(BoostGoodGameActivity.this, shortcutIntent, title, false, bitmap);
    }

    @Override
    public void onBackPressed() {
        if (ll_add_game.getVisibility() == View.VISIBLE) {
            ll_add_game.setVisibility(View.GONE);
            shortGame1(false);
        } else {
            finish();
        }
    }

    private void initData() {
        ArrayList<String> gboost_names = CleanDBHelper.getInstance(this).getWhiteList(CleanDBHelper.TableType.GameBoost);
        gboost_add.clear();
        for (JunkInfo info : CleanManager.getInstance(this).getAppList()) {
            boolean isA = false;
            for (String packagename : gboost_names) {
                if (packagename.equals(info.pkg)) {
                    isA = true;
                }
            }
            if (!isA) {
                gboost_add.add(info);
            }
        }
        whiteListAdapter.upList(gboost_add);
        whiteListAdapter.notifyDataSetChanged();
    }

    private void upData(String string) {
        listEdit.clear();
        for (JunkInfo info : gboost_add) {
            if (info.label.contains(string)) {
                listEdit.add(info);
            }
        }
        whiteListAdapter.upList(listEdit);
        whiteListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
