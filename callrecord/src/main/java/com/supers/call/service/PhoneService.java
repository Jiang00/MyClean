package com.supers.call.service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.supers.call.Utils.Constants;
import com.supers.call.Utils.Util;
import com.supers.call.database.Database;
import com.supers.call.entries.Contact;
import com.supers.call.entries.RejectInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 */

public class PhoneService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intentFilter.setPriority(1000);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private String getDirect(boolean isOut) {
        if (isOut) {
            return "out";
        } else {
            return "in";
        }
    }

    private static final String TAG = "PhoneService";

    private Context mContext;
    private Intent mIntent;
    private Handler mHandler = new Handler();
    private String currentNumber = null;
    private boolean isOuting = false;


    //    private Manager manager;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            mIntent = intent;
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                // 呼出
                currentNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                isOuting = true;
            }
            mHandler.removeCallbacks(run_start);
            mHandler.postDelayed(run_start, (Long) Util.readData(context, Constants.KEY_DELAY, 1000L));
        }
    };

    private Runnable run_start = new Runnable() {
        @Override
        public void run() {
            TelephonyManager tm = (TelephonyManager) mContext
                    .getSystemService(Service.TELEPHONY_SERVICE);
            if (currentNumber == null) {
                String inComing = mIntent.getStringExtra(tm.EXTRA_INCOMING_NUMBER);
                if (!TextUtils.isEmpty(inComing)) {
                    currentNumber = inComing.replace(" ", "");
                    isOuting = false;
                }
            }
            if (TextUtils.isEmpty(currentNumber)) {
                return;
            }
            Log.d(TAG, "PhoneNum = " + currentNumber);
            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "挂断：PhoneNum=" + currentNumber);
                    currentNumber = null;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "接听：PhoneNum=" + currentNumber);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "响铃：PhoneNum=" + currentNumber);
                    Log.e("call", currentNumber + "==service1");
                    getInterception(currentNumber);
                    break;
            }
        }
    };

    /* 检查是否应该拦截 */
    private void getInterception(String phoneNum) {
        if (phoneNum == null) {
            return;
        }
        AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        int ringMode = mAudioManager.getRingerMode();
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Database database = Database.getInstance(mContext);
        SQLiteDatabase db = database.getWritableDatabase();
        ArrayList<Contact> list = database.getDataFromTableContacts(db, Constants.TABLE_BLOCK);
        if (list == null) {
            return;
        }
        String comeNumber = Util.resetPhoneNum(currentNumber);
        String resetNum;
        for (Contact contact : list) {
            resetNum = Util.resetPhoneNumber(contact.phoneNum);
            Log.e("call", contact.name + "==service222");
            if (resetNum != null && comeNumber != null && resetNum.equals(comeNumber)) {
                endCall(database, db, contact, currentNumber);
                return;
            }
        }
        db.close();
        mAudioManager.setRingerMode(ringMode);
    }

    private void endCall(Database dbHelper, SQLiteDatabase database, Contact info, String phoneNum) {
        stopCall(phoneNum);
        try {
            sendBroadcast(new Intent("phone.record.endCall"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!dbHelper.tableIsExist(database, Constants.TABLE_INTERCEPTION)) {
            dbHelper.createTableReject(database, Constants.TABLE_INTERCEPTION);
        }
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        String isOut = getDirect(isOuting);

        if (TextUtils.isEmpty(info.name)) {
            info.name = info.phoneNum;
        }
        dbHelper.insertDataToTableReject(database, Constants.TABLE_INTERCEPTION, new RejectInfo(info.name, info.phoneNum, date, isOut));
    }

    public void stopCall(final String phoneNum) {
        try {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            // 获取远程TELEPHONY_SERVICE的IBinder对象的代理
            IBinder binder = (IBinder) method.invoke(null, new Object[]{"phone"});
            // 将IBinder对象的代理转换为ITelephony对象
            ITelephony telephony = ITelephony.Stub.asInterface(binder);
            // 挂断电话
            telephony.endCall();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    deleteLastCallLog(phoneNum);
                }
            }, 500);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
//            Log.d(TAG, "NoSuchMethodException");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
//            Log.d(TAG, "IllegalAccessException");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
//            Log.d(TAG, "InvocationTargetException");
            e.printStackTrace();
        } catch (RemoteException e) {
//            Log.d(TAG, "RemoteException");
            e.printStackTrace();
        }
    }

    //删除通话记录
    private void deleteLastCallLog(String callLog) {
        ContentResolver resolver = getContentResolver();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, new String[]{"_id"}, "number=?", new String[]{callLog}, "_id desc");
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
                resolver.delete(CallLog.Calls.CONTENT_URI, CallLog.Calls._ID + "=?", new String[]{id + ""});

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
