package com.supers.clean.junk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.clean.core.CleanManager;
import com.android.clean.db.CleanDBHelper;
import com.android.clean.gboost.GameBooster;
import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.android.clean.util.LoadManager;
import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.AddGameAdapter;
import com.supers.clean.junk.customeview.PagerView;
import com.android.clean.entity.JunkInfo;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.ShortCutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivy on 2017/5/5.
 */

public class GBoostActivity extends BaseActivity {
    private static final int ADD_DATA = 0;

    FrameLayout title_left;
    TextView title_name;
    ImageView add_right;
    ImageButton clear;
    EditText search_edit_text;
    RecyclerView gboost_recyc;
    LinearLayout ll_add_game;
    FrameLayout add_left;
    ListView list_game;


    private ArrayList<String> list;
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
    private AddGameAdapter whiteListAdapter;
    private MyApplication cleanApplication;

    private boolean search;
    private List<JunkInfo> gboost_add, listEdit;
    private MyAdapter adapter;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        add_right = (ImageView) findViewById(R.id.add_right);
        clear = (ImageButton) findViewById(R.id.clear);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);

        gboost_recyc = (RecyclerView) findViewById(R.id.gboost_recyc);
        ll_add_game = (LinearLayout) findViewById(R.id.ll_add_game);
        add_left = (FrameLayout) findViewById(R.id.add_left);
        list_game = (ListView) findViewById(R.id.list_game);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gboost);
        mHandler = new Handler();
        cleanApplication = (MyApplication) getApplication();

        title_left.setOnClickListener(clickListener);
        add_left.setOnClickListener(clickListener);
        title_name.setText(R.string.gboost_0);


        add_right.setOnClickListener(clickListener);
        clear.setOnClickListener(clickListener);
        gboost_add = new ArrayList<>();
        listEdit = new ArrayList<>();
        initList();
    }

    private void initList() {
        list = new ArrayList<>();
        addData();

    }

    private void addData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (PreData.getDB(GBoostActivity.this, Constant.GBOOST_LUN, true)) {
                    PreData.putDB(GBoostActivity.this, Constant.GBOOST_LUN, false);
                    list.addAll(GameBooster.getInstalledGameList(GBoostActivity.this));
                    shortGame(false);
                }
                ArrayList<String> gboost_names = CleanDBHelper.getInstance(GBoostActivity.this).getWhiteList(CleanDBHelper.TableType.GameBoost);
                for (String pkg : gboost_names) {
                    if (LoadManager.getInstance(GBoostActivity.this).isPkgInstalled(pkg)) {
                        list.add(pkg);
                    }
                }
                Message msg = mHandler.obtainMessage();
                msg.what = ADD_DATA;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        list.add(0, getString(R.string.gboost_7));
                        Log.e("jfy", list.size() + "==");
                        gboost_recyc.setLayoutManager(new GridLayoutManager(GBoostActivity.this, 2));
                        gboost_recyc.setAdapter(adapter = new MyAdapter());

                    }
                });
            }
        }).start();

    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(LayoutInflater.from(
                    GBoostActivity.this).inflate(R.layout.layout_gboost_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if (position == 0) {
                holder.add.setVisibility(View.VISIBLE);
                holder.app.setVisibility(View.GONE);
            } else {
                holder.gboost_item_icon.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(position)));
                holder.gboost_item_name.setText(LoadManager.getInstance(GBoostActivity.this).getAppLabel(list.get(position)));
            }
            holder.gboost_item_c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 0) {
                        AdUtil.track("游戏加速页面", "点击添加游戏", "", 1);
                        ll_add_game.setVisibility(View.VISIBLE);
                        whiteListAdapter = new AddGameAdapter(GBoostActivity.this, list);
                        list_game.setAdapter(whiteListAdapter);
                        initData();
                    } else {
                        try {
                            AdUtil.track("游戏加速页面", "点击启动游戏", list.get(position), 1);
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "GBoost");
                            bundle.putString("packageName", list.get(position));
                            jumpToActivity(PowerActivity.class, bundle);
                        } catch (Exception e) {
                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout app, add;
            ImageView gboost_item_icon;
            TextView gboost_item_name;
            FrameLayout gboost_item_c;

            public MyViewHolder(View view) {
                super(view);
                gboost_item_icon = (ImageView) view.findViewById(R.id.gboost_item_icon);
                gboost_item_c = (FrameLayout) view.findViewById(R.id.gboost_item_c);
                app = (LinearLayout) view.findViewById(R.id.app);
                add = (LinearLayout) view.findViewById(R.id.add);
                gboost_item_name = (TextView) view.findViewById(R.id.gboost_item_name);
            }
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_left:
                    ll_add_game.setVisibility(View.GONE);
                    shortGame(false);
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.title_left:
                    onBackPressed();
                    break;
                case R.id.add_right:
                    toggleEditAnimation();
                    break;
                case R.id.clear:
                    toggleEditAnimation();
                    break;
            }
        }
    };

    private void toggleEditAnimation() {
        final View searchView = findViewById(R.id.goost_container);
        View normalView = findViewById(R.id.normal_bar);

        final View visibleView, invisibleView;
        if (searchView.getVisibility() == View.GONE) {
            visibleView = normalView;
            invisibleView = searchView;
        } else {
            visibleView = searchView;
            invisibleView = normalView;
//            showSoftKeyboard(AbsActivity.this, null, false);
        }

        final ObjectAnimator invis2vis = ObjectAnimator.ofFloat(invisibleView, "rotationY", -90, 0);
        invis2vis.setDuration(500);
        invis2vis.setInterpolator(new LinearInterpolator());
        ObjectAnimator vis2invis = ObjectAnimator.ofFloat(visibleView, "rotationY", 0, 90);
        vis2invis.setDuration(500);
        vis2invis.setInterpolator(new LinearInterpolator());

        vis2invis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                visibleView.setVisibility(View.GONE);
                invisibleView.setVisibility(View.VISIBLE);

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

                invis2vis.start();
            }
        });
        vis2invis.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (ll_add_game.getVisibility() == View.VISIBLE) {
            ll_add_game.setVisibility(View.GONE);
            shortGame(false);
            adapter.notifyDataSetChanged();
        } else {
            finish();
        }
    }

    private void shortGame(boolean isChuangjian) {
        search_edit_text.setText("");
        Log.e("short", "chuangjian1 ");
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.setComponent(new ComponentName(getPackageName(),
                "com.supers.clean.junk.activity.GBoostActivity"));
        String title = GBoostActivity.this.getString(R.string.gboost_0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.short_7);
            ShortCutUtils.addShortcut(GBoostActivity.this, shortcutIntent, title, false, bitmap);
            return;
        }
        if (list.size() > 1 && (!PreData.getDB(GBoostActivity.this, Constant.GBOOST_SI, false) || isChuangjian)) {
            View shortcut_view = View.inflate(GBoostActivity.this, R.layout.layout_gboost_short, null);
            if (list.size() > 1) {
                ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
                iv_1.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(1)));
            }
            if (list.size() > 2) {
                ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
                iv_2.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(2)));
            }
            if (list.size() > 3) {
                ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
                iv_3.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(3)));
            }
            if (list.size() > 4) {
                ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
                iv_4.setImageDrawable(LoadManager.getInstance(GBoostActivity.this).getAppIcon(list.get(4)));
                PreData.putDB(GBoostActivity.this, Constant.GBOOST_SI, true);
            }

            Bitmap bitmap = Util.getViewBitmap(shortcut_view);
            if (bitmap != null) {
                Log.e("short", "chuangjian ");
//                Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
//                shortcutIntent.setClass(GBoostActivity.this, GBoostActivity.class);
//                shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                ShortCutUtils.removeShortcut(GBoostActivity.this, shortcutIntent, title);
                ShortCutUtils.addShortcut(GBoostActivity.this, shortcutIntent, title, false, bitmap);
            }
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
