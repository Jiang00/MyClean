package com.fraumobi.call.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fraumobi.call.adapter.CallAdapter;
import com.fraumobi.call.adapter.CallJiluAdapter;
import com.fraumobi.call.entries.RejectInfo;
import com.fraumobi.call.record.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by ${} on 2017/8/23.
 */

public class CallName2Activity extends BaseActivity {


    CallJiluAdapter adapter;
    ListView call_list;
    FrameLayout title_left;
    ProgressBar progressbar;
    LinearLayout null_call;

    protected void findId() {
        call_list = (ListView) findViewById(R.id.call_list);
        title_left = (FrameLayout) findViewById(R.id.title_left);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        null_call = (LinearLayout) findViewById(R.id.null_call);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_call_jil);
        findId();
        setClick();
        adapter = new CallJiluAdapter(this);
        call_list.setAdapter(adapter);
        initData();

    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<RejectInfo> tonghuajilu = new ArrayList<>();
                final ArrayList<RejectInfo> call_list = queryCall();
                for (RejectInfo callEntity : call_list) {
                    boolean isHaveSameAddress = false;
                    for (int i = 0; i < tonghuajilu.size(); i++) {
                        RejectInfo callEn = tonghuajilu.get(i);
                        if (TextUtils.equals(callEntity.phoneNum, callEn.phoneNum)) {
                            isHaveSameAddress = true;
                            break;
                        }
                    }
                    if (!isHaveSameAddress) {
                        tonghuajilu.add(callEntity);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressbar.setVisibility(View.GONE);
                        if (null == call_list || tonghuajilu.size() == 0) {
                            null_call.setVisibility(View.VISIBLE);
                        } else {
                            adapter.addDataList(tonghuajilu);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        }).start();
    }

    private void setClick() {
        title_left.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.title_left) {
                onBackPressed();
            }
        }
    };


    public ArrayList<RejectInfo> queryCall() {
        ArrayList<RejectInfo> callEntities = new ArrayList<>();
        final String[] projection = null;
        final String selection = null;
        final String[] selectionArgs = null;
        final String sortOrder = CallLog.Calls.DATE + " DESC";
        Cursor cursor = null;
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            cursor = getApplicationContext().getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, projection,
                    selection, selectionArgs, sortOrder);
            while (cursor.moveToNext()) {
                if (onDestroyed) {
                    return null;
                }
                String callName = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                int id = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls._ID));
                String callNumber = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NUMBER));
                //需要对时间进行一定的处理
                String callDate = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.DATE));
                long callTime = Long.parseLong(callDate);
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "M-dd HH:mm");
                callDate = sdf.format(new Date(callTime));

                int callType = cursor.getInt(cursor
                        .getColumnIndex(CallLog.Calls.TYPE));
                String isCallNew = cursor.getString(cursor
                        .getColumnIndex(CallLog.Calls.NEW));
//                    if (Integer.parseInt(callType) == (CallLog.Calls.MISSED_TYPE)
//                            && Integer.parseInt(isCallNew) > 0)  //通过call.new进行了限定，会对读取有一些问题，要删掉该限定
               /* if (Integer.parseInt(callType) == (CallLog.Calls.MISSED_TYPE)) {*/
                //textView.setText(callType+"|"+callDate+"|"+callNumber+"|");
//只是以最简单ListView显示联系人的一些数据----适配器的如何配置可查看http://blog.csdn.net/cl18652469346/article/details/52237637
                RejectInfo callEntity = new RejectInfo(callName, callNumber.replace(" ", ""), callDate, "in");
                callEntities.add(callEntity);
                /*}*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return callEntities;
    }

}
