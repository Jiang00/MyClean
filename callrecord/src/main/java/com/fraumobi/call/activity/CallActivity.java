package com.fraumobi.call.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.clean.util.Constant;
import com.android.clean.util.PreData;
import com.android.client.AndroidSdk;
import com.fraumobi.call.Utils.BadgerCount;
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
    TextView call_restore;
    FrameLayout null_call;
    FrameLayout title_left;
    ImageView title_setting;
    LinearLayout add_check;
    FrameLayout add_check_fl;
    TextView check_tongxun, check_jilu, check_shou;
    ImageView title_checked;

    CallAdapter adapter;
    private ArrayList<RejectInfo> blockList;
    private AlertDialog dialog;
    private LinearLayout ll_ad;

    protected void findId() {
        call_list = (ListView) findViewById(R.id.call_list);
        call_delete = (TextView) findViewById(R.id.call_delete);
        call_restore = (TextView) findViewById(R.id.call_restore);
        null_call = (FrameLayout) findViewById(R.id.null_call);
        title_setting = (ImageView) findViewById(R.id.title_setting);
        add_check = (LinearLayout) findViewById(R.id.add_check);
        add_check_fl = (FrameLayout) findViewById(R.id.add_check_fl);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_checked = (ImageView) findViewById(R.id.title_checked);
        check_tongxun = (TextView) findViewById(R.id.check_tongxun);
        check_jilu = (TextView) findViewById(R.id.check_jilu);
        check_shou = (TextView) findViewById(R.id.check_shou);
        ll_ad = (LinearLayout) findViewById(R.id.ll_ad);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_call);
        PreData.putDB(this, Constant.HONG_CALL, false);
        BadgerCount.setCount(this);
        findId();
        startFetcherContacts();
        adapter = new CallAdapter(this);
        call_list.setAdapter(adapter);
        initData();
        call_delete.setOnClickListener(clickListener);
        call_restore.setOnClickListener(clickListener);
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
        addAd();
    }

    private void addAd() {
        View nativeExit = getNativeAdView(R.layout.native_ad_3);
        if (nativeExit != null) {
            ll_ad.addView(nativeExit);
            ll_ad.setVisibility(View.VISIBLE);
        }
    }

    public static View getNativeAdView(@LayoutRes int layout) {
        if (!AndroidSdk.hasNativeAd("eos_native")) {
            return null;
        }
        View nativeView = AndroidSdk.peekNativeAdViewWithLayout("eos_native", layout, null);
        if (nativeView == null) {
            return null;
        }

        if (nativeView != null) {
            ViewGroup viewParent = (ViewGroup) nativeView.getParent();
            if (viewParent != null) {
                viewParent.removeAllViews();
            }
        }
        return nativeView;
    }

    private void updateTitle() {
        boolean isAll = true;
        for (RejectInfo info : blockList) {
            if (!info.isChecked) {
                isAll = false;
            }
        }
        if (isAll) {
            title_checked.setImageResource(R.mipmap.call_check_all);
        } else {
            title_checked.setImageResource(R.mipmap.call_check_all_2);
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
                    startActivity(new Intent("eos.successactivity"));
                    finish();
                }
            } else if (i == R.id.call_restore) {
                List<RejectInfo> data = adapter.getData();
                if (data != null) {
                    for (RejectInfo info : data) {
                        if (info.isChecked) {
                            blockList.remove(info);
                            deleteBlockListItemFromTable(info);
                            insertCallLog(info);
                        }
                    }
                    adapter.notifyDataSetChanged();
//                    startActivity(new Intent(CallActivity.this, SuccessActivity.class));
                    startActivity(new Intent("eos.successactivity"));
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
                    title_checked.setImageResource(R.mipmap.call_check_all);

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
                dialog();
            }

        }
    };

    private void dialog() {
        View view = View.inflate(this, R.layout.dialog_shou, null);
        final EditText name = (EditText) view.findViewById(R.id.name);
        final EditText num = (EditText) view.findViewById(R.id.num);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String call_name = name.getText().toString();
                final String call_num = num.getText().toString();
                if (null != call_num && call_num.length() != 0) {
                    ContactTool.addContact(CallActivity.this, call_name, call_num);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        startReadContacts();
                        for (Contact info : contactsList) {
                            if (TextUtils.equals(info.phoneNum, call_num.replace(" ", ""))) {
                                addItemToTableBlock(info);
                                break;
                            }
                        }
                    }
                }).start();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog = new AlertDialog.Builder(this, R.style.dialog).create();
        dialog.setView(view);
        dialog.show();
//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = dm.widthPixels; //设置宽度
//        lp.height = dm.heightPixels; //设置高度
//        int uiOptions =
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                        //布局位于状态栏下方
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                        //隐藏导航栏
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        if (Build.VERSION.SDK_INT >= 19) {
//            uiOptions |= 0x00001000;
//        } else {
//            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
//        }
//        dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setContentView(view);
    }

    private void initData() {
        getBlockListData();
        if (blockList == null || blockList.size() == 0) {
            call_delete.setVisibility(View.GONE);
            call_restore.setVisibility(View.GONE);
            null_call.setVisibility(View.VISIBLE);
            title_checked.setVisibility(View.GONE);
            title_checked.setImageResource(R.mipmap.call_check_all_2);
        } else {
            title_checked.setVisibility(View.VISIBLE);
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

    //    添加通话记录
    private void insertCallLog(RejectInfo info) {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, info.phoneNum);
        long time = System.currentTimeMillis();
        try {
            time = Long.parseLong(info.date);
        } catch (Exception e) {
        }
        values.put(CallLog.Calls.DATE, time);
        values.put(CallLog.Calls.DURATION, 0);
        values.put(CallLog.Calls.TYPE, 3);//未接
        values.put(CallLog.Calls.CACHED_NAME, info.name);//未接
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
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

    private void addItemToTableBlock(Contact info) {
        Database database = Database.getInstance(this);
        SQLiteDatabase db = database.getWritableDatabase();
        if (!database.tableIsExist(db, Constants.TABLE_BLOCK)) {
            database.createTableContacts(db, Constants.TABLE_BLOCK);
        }
        database.insertDataIntoTableContacts(db, Constants.TABLE_BLOCK, info);
        db.close();
    }
}
