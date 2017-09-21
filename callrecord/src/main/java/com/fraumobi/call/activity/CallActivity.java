package com.fraumobi.call.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fraumobi.call.Utils.Constants;
import com.fraumobi.call.Utils.Util;
import com.fraumobi.call.adapter.CallAdapter;
import com.fraumobi.call.database.Database;
import com.fraumobi.call.entries.Contact;
import com.fraumobi.call.entries.RejectInfo;
import com.fraumobi.call.record.R;
import com.fraumobi.call.tools.ACache;
import com.fraumobi.call.tools.ContactTool;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ${} on 2017/8/23.
 */

public class CallActivity extends BaseActivity {

    protected ArrayList<Contact> contactsList;
    ListView call_list;
    TextView call_delete;
    TextView null_call;
    FrameLayout title_left;
    ImageView title_setting;
    LinearLayout add_check;
    FrameLayout add_check_fl;
    TextView check_tongxun, check_jilu, check_shou;
    ImageView title_checked;

    CallAdapter adapter;
    private ArrayList<RejectInfo> blockList;
    private AlertDialog dialog;

    protected void findId() {
        call_list = (ListView) findViewById(R.id.call_list);
        call_delete = (TextView) findViewById(R.id.call_delete);
        null_call = (TextView) findViewById(R.id.null_call);
        title_setting = (ImageView) findViewById(R.id.title_setting);
        add_check = (LinearLayout) findViewById(R.id.add_check);
        add_check_fl = (FrameLayout) findViewById(R.id.add_check_fl);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_checked = (ImageView) findViewById(R.id.title_checked);
        check_tongxun = (TextView) findViewById(R.id.check_tongxun);
        check_jilu = (TextView) findViewById(R.id.check_jilu);
        check_shou = (TextView) findViewById(R.id.check_shou);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_call);
        findId();
        startFetcherContacts();
        adapter = new CallAdapter(this);
        call_list.setAdapter(adapter);
        initData();
        call_delete.setOnClickListener(clickListener);
        title_setting.setOnClickListener(clickListener);
        title_left.setOnClickListener(clickListener);
        title_checked.setOnClickListener(clickListener);
        add_check_fl.setOnClickListener(clickListener);
        check_tongxun.setOnClickListener(clickListener);
        check_jilu.setOnClickListener(clickListener);
        check_shou.setOnClickListener(clickListener);
        call_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.getItem(position).isChecked = !adapter.getItem(position).isChecked;
                adapter.notifyDataSetChanged();
                updateTitle();
            }
        });

    }

    private void updateTitle() {
        boolean isAll = true;
        for (RejectInfo info : blockList) {
            if (!info.isChecked) {
                isAll = false;
            }
        }
        if (isAll) {
            title_checked.setAlpha(1f);
        } else {
            title_checked.setAlpha(0.54f);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.title_setting) {
                add_check.setPivotY(0);
                add_check.setPivotX(add_check.getWidth());
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator animator = ObjectAnimator.ofFloat(add_check, View.SCALE_X, 0, 1);
                ObjectAnimator animator_2 = ObjectAnimator.ofFloat(add_check, View.SCALE_Y, 0, 1);
                animatorSet.play(animator).with(animator_2);
                animatorSet.setDuration(200);
                animatorSet.start();
                add_check_fl.setVisibility(View.VISIBLE);

            } else if (i == R.id.call_delete) {
                List<RejectInfo> data = adapter.getData();
                if (data != null) {
                    for (RejectInfo info : data) {
                        if (info.isChecked) {
                            blockList.remove(info);
                            deleteBlockListItemFromTable(info);
                        }
                    }
                    adapter.notifyDataSetChanged();
//                    startActivity(new Intent(CallActivity.this, SuccessActivity.class));
                    finish();
                }
            } else if (i == R.id.title_left) {
                finish();
            } else if (i == R.id.title_checked) {
                if (blockList != null && blockList.size() != 0) {
                    for (RejectInfo info : blockList) {
                        info.isChecked = true;
                    }
                    adapter.notifyDataSetChanged();
                    title_checked.setAlpha(1f);
                }
            } else if (i == R.id.add_check_fl) {
                add_check_fl.setVisibility(View.INVISIBLE);
            } else if (i == R.id.check_tongxun) {
                add_check_fl.setVisibility(View.INVISIBLE);
                startActivity(new Intent(CallActivity.this, CallNameActivity.class));
            } else if (i == R.id.check_jilu) {
                add_check_fl.setVisibility(View.INVISIBLE);
                startActivity(new Intent(CallActivity.this, CallName2Activity.class));
            } else if (i == R.id.check_shou) {
                add_check_fl.setVisibility(View.INVISIBLE);

            }

        }
    };

    private void dialog() {
        View view = View.inflate(this, R.layout.dialog_shou, null);
        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        lp.height = dm.heightPixels; //设置高度
        int uiOptions =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setContentView(view);
    }

    private void initData() {
        getBlockListData();
        if (blockList == null || blockList.size() == 0) {
            call_delete.setVisibility(View.GONE);
            null_call.setVisibility(View.VISIBLE);
            title_checked.setAlpha(0.54f);
        } else {
            adapter.upList(blockList);
            adapter.notifyDataSetChanged();
            updateTitle();
        }
    }

    //获取拦截记录
    private void getBlockListData() {
        Database database = Database.getInstance(this);
        SQLiteDatabase db = database.getWritableDatabase();
        blockList = database.getDataFromTableReject(db, Constants.TABLE_INTERCEPTION);
        db.close();
    }

    //    删除拦截记录
    private void deleteBlockListItemFromTable(RejectInfo info) {
        Database database = Database.getInstance(this);
        SQLiteDatabase db = database.getWritableDatabase();
        database.deleteDataFromTableReject(db, Constants.TABLE_INTERCEPTION, info);
        db.close();
    }

    protected void startFetcherContacts() {
        new Thread(fetcherContactsRunnable).start();
    }

    private Runnable fetcherContactsRunnable = new Runnable() {
        @Override
        public void run() {
            startReadContacts();
        }
    };

    private void startReadContacts() {
        Database database = Database.getInstance(CallActivity.this);
        SQLiteDatabase db = database.getWritableDatabase();
        boolean isFirst = false;
        ACache aCache = ACache.get(CallActivity.this);
        if (!database.tableIsExist(db, Constants.TABLE_RECORD)) {
            isFirst = true;
            database.createTableContacts(db, Constants.TABLE_RECORD);
            Util.writeData(CallActivity.this, Constants.CONTACT_SELECT_STATE, true);
        }
//        获取联系人列表
        contactsList = ContactTool.getAllContacts(getContentResolver(), isFirst, database, db, aCache);
        db.close();
        aCache.remove("contacts");
        aCache.put("contacts", contactsList);
    }
}
