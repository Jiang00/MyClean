package com.supers.clean.junk.privacy;

import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by renqingyou on 2017/6/13.
 */

public class PrivacyClean {
    private static PrivacyClean privacyClean;

    private Context mContext;

    private PrivacyClean(Context context) {
        mContext = context;
    }


    public synchronized static PrivacyClean getInstance(Context context) {
        if (privacyClean == null) {
            privacyClean = new PrivacyClean(context);
        }
        return privacyClean;
    }

    private void cleanCallLog() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
    }

    public void cleanUnReadCallLog() {
        ContentResolver resolver = mContext.getContentResolver();
        StringBuilder where = new StringBuilder();
        where.append(CallLog.Calls.NEW);
        where.append(" = 0 ");
      /*  where.append(CallLog.Calls.TYPE);
        where.append(" = ?");*/
        resolver.delete(CallLog.Calls.CONTENT_URI, where.toString(), null);
    }

    public void cleanNoContactCallLog() {
        queryCall();
        ContentResolver resolver = mContext.getContentResolver();
        StringBuilder where = new StringBuilder();
        where.append(CallLog.Calls.NEW);
        where.append(" = 0 AND ");
        where.append(CallLog.Calls.CACHED_NAME);
        where.append(" IS NULL ");
        resolver.delete(CallLog.Calls.CONTENT_URI, where.toString(), null);
    }


    public ArrayList<CallEntity> queryCall() {
        ArrayList<CallEntity> callEntities = new ArrayList<>();
        final String[] projection = null;
        final String selection = null;
        final String[] selectionArgs = null;
        final String sortOrder = CallLog.Calls.DATE + " DESC";
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, projection,
                    selection, selectionArgs, sortOrder);
            while (cursor.moveToNext()) {
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
                CallEntity callEntity = new CallEntity();
                callEntity.id = id;
                callEntity.callNumber = callNumber;
                callEntity.callName = callName;
                callEntity.callData = callDate;
                callEntity.callType = callType;
                callEntity.isCallNew = isCallNew;
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

    public ArrayList<SmsEntity> querySms() {
        ArrayList<SmsEntity> smsList = new ArrayList<>();
        Uri uri = Uri.parse("content://sms");
        String[] projections = new String[]{"_id", "address", "date",
                "date_sent", "read", "status", "type", "body"};
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(uri, projections,
                    null, null, null);

            Log.e("aaa", "count-- " + cursor.getCount());
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                int columnCount = cursor.getColumnCount();
                Log.v("aaa", "columnCount--" + columnCount);
                int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow("_id"));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address")); // 对方号码
                String date = cursor.getString(cursor
                        .getColumnIndexOrThrow("date"));  // 发件日期
                String date_sent = cursor.getString(cursor
                        .getColumnIndexOrThrow("date_sent"));
                int read = cursor.getInt(cursor
                        .getColumnIndexOrThrow("read")); //0 “未读”，1“已读”
                String status = cursor.getString(cursor
                        .getColumnIndexOrThrow("status"));  // 状态 integer   default-1。 -1：接收，0：complete,64： pending, 128failed
                String type = cursor.getString(cursor
                        .getColumnIndexOrThrow("type"));  //  1：inbox 接受 2：sent 发送 3：draft56  4：outbox  5：failed  6：queued
                String body = cursor.getString(cursor
                        .getColumnIndexOrThrow("body")); //短信内容
                SmsEntity smsEntity = new SmsEntity();
                smsEntity.id = id;
                smsEntity.address = address;
                smsEntity.type = type;
                smsEntity.date = date;
                smsEntity.read = read;
                smsEntity.status = status;
                smsList.add(smsEntity);
                Log.e("rqy", address + "--" + date + "--" + date_sent + "--" + read + "--" + status + "--" + type + "--" + body);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return smsList;
    }

    public void cleanSms() {
        ContentResolver cResolver = mContext.getContentResolver();
        Uri smsUri = Uri.parse("content://sms/");
        cResolver.delete(smsUri, null, null);
    }

    public void cleanReadSms() {
        ContentResolver cResolver = mContext.getContentResolver();
        Uri smsUri = Uri.parse("content://sms/");
        StringBuilder where = new StringBuilder();
        where.append("read = 1");
        cResolver.delete(smsUri, where.toString(), null);
    }

    public void cleanNoContactSms() {
        ContentResolver cResolver = mContext.getContentResolver();
        Uri smsUri = Uri.parse("content://sms/");
        StringBuilder where = new StringBuilder();
        where.append("_id = ?");

        ArrayList<SmsInfo> smsInfos = queryNoContactSms();
        GetName getName = new GetName(mContext);
        for (SmsInfo smsInfo : smsInfos) {
            ConversationContact conversationContact = getName.getContentUserName(smsInfo.address);
            if (conversationContact == null) {
                continue;
            }
            Log.e("rqy", "name=" + conversationContact.name);
            cResolver.delete(smsUri, where.toString(), new String[]{smsInfo.id});
        }
    }

    public ArrayList<SmsInfo> queryNoContactSms() {
        ArrayList<SmsInfo> smsInfos = new ArrayList<>();
        ContentResolver cResolver = mContext.getContentResolver();
        Uri smsUri = Uri.parse("content://sms/");
        StringBuilder where = new StringBuilder();
        where.append("read = 1");
        Cursor cursor = cResolver.query(smsUri, new String[]{"_id", "address"}, where.toString(), null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address")); // 对方号码
                SmsInfo smsInfo = new SmsInfo(id, address);
                Log.e("rqy", smsInfo + "");
                smsInfos.add(smsInfo);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return smsInfos;
    }

    public String getContactNameByPhoneNumber(Context context, String address) {
        Log.e("rqy", "address=" + address);
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, // Which columns to return.
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
                        + address + "'", // WHERE clause.
                null, // WHERE clause value substitution
                null); // Sort order.

        if (cursor == null) {
            Log.d("rqy", "getPeople null");
            return null;
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            // 取得联系人名字
            int nameFieldColumnIndex = cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            return name;
        }
        return null;
    }

    public boolean isHaveCutText() {
        //获取剪贴板管理服务
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        //cm.getPrimaryClip().newHtmlText("","","");
        return !TextUtils.isEmpty(cm.getText());
    }

    public void cleanCut() {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        //cm.getPrimaryClip().newHtmlText("","","");
        cm.setText("");
    }
}
