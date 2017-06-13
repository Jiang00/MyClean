package com.supers.clean.junk.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.entity.CallEntity;
import com.supers.clean.junk.entity.SmsEntity;
import com.supers.clean.junk.entity.SmsInfo;
import com.supers.clean.junk.util.ConversationContact;
import com.supers.clean.junk.util.GetName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ivy on 2017/5/9.
 */

public class PrivacyActivity extends BaseActivity {
    FrameLayout title_left;
    TextView title_name;
    ImageView privary_small_1, privary_small_2, privary_small_3, privary_small_4, privary_small_5;
    ImageView privary_line_1, privary_line_2;
    ImageView privary_yuan_1, privary_yuan_2, privary_yuan_3;
    ImageView privacy_cut_yuandian, privacy_cut_bg, privacy_sms_yuandian, privacy_sms_bg, privacy_call_yuandian, privacy_call_record_bg;


    private AnimatorSet animatorSmall, animationLine_1, animationLine_2;
    private ObjectAnimator animator;
    private AnimatorSet animatorSet;
    private Handler mHandler;

    @Override
    protected void findId() {
        super.findId();
        title_left = (FrameLayout) findViewById(R.id.title_left);
        title_name = (TextView) findViewById(R.id.title_name);
        privary_small_1 = (ImageView) findViewById(R.id.privary_small_1);
        privary_small_2 = (ImageView) findViewById(R.id.privary_small_2);
        privary_small_3 = (ImageView) findViewById(R.id.privary_small_3);
        privary_small_4 = (ImageView) findViewById(R.id.privary_small_4);
        privary_small_5 = (ImageView) findViewById(R.id.privary_small_5);
        privary_line_1 = (ImageView) findViewById(R.id.privary_line_1);
        privary_line_2 = (ImageView) findViewById(R.id.privary_line_2);
        privary_yuan_1 = (ImageView) findViewById(R.id.privary_yuan_1);
        privary_yuan_2 = (ImageView) findViewById(R.id.privary_yuan_2);
        privary_yuan_3 = (ImageView) findViewById(R.id.privary_yuan_3);
        privacy_cut_yuandian = (ImageView) findViewById(R.id.privacy_cut_yuandian);
        privacy_cut_bg = (ImageView) findViewById(R.id.privacy_cut_bg);
        privacy_sms_yuandian = (ImageView) findViewById(R.id.privacy_sms_yuandian);
        privacy_sms_bg = (ImageView) findViewById(R.id.privacy_sms_bg);
        privacy_call_yuandian = (ImageView) findViewById(R.id.privacy_call_yuandian);
        privacy_call_record_bg = (ImageView) findViewById(R.id.privacy_call_bg);

    }

    public void bgAnimation(View view) {
        final AnimatorSet animationSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1);
        animationSet.setDuration(500);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(animatorX).with(animatorY);
        animationSet.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_privary);

        mHandler = new Handler();
        title_left.setOnClickListener(clickListener);

        animationSmall();
        animationLine();

        animationYuan(privary_yuan_3);
        animationYuan1(privacy_call_yuandian);
        animationYuan1(privacy_sms_yuandian);
        animationYuan1(privacy_cut_yuandian);

        //获取剪贴板管理服务
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        //cm.getPrimaryClip().newHtmlText("","","");
        int count = 0;
        if (cm.hasPrimaryClip()) {
            count = cm.getPrimaryClip().getItemCount();
        }

        ArrayList<SmsEntity> smsEntities = querySms();
        int smsCount = smsEntities.size();

        ArrayList<CallEntity> callEntities = queryCallEntity();
        int callCount = callEntities.size();


        GradientDrawable gradientDrawable;
        String privacy_total = getString(R.string.privacy_total);

        if (count == 0) {
            gradientDrawable = getGradientDrawable("#2c8dea");
        } else {
            gradientDrawable = getGradientDrawable("#f84a4a");
        }
        privacy_cut_bg.setImageDrawable(gradientDrawable);
        animation(privacy_cut_bg);
        TextView textView = (TextView) findViewById(R.id.privacy_cut_total);
        textView.setText(privacy_total.replace("%", count + ""));
        privacy_cut_yuandian.setVisibility(View.GONE);


        if (smsCount == 0) {
            gradientDrawable = getGradientDrawable("#2c8dea");
        } else {
            gradientDrawable = getGradientDrawable("#f84a4a");
        }
        privacy_sms_bg.setImageDrawable(gradientDrawable);
        animation(privacy_sms_bg);
        TextView smsText = (TextView) findViewById(R.id.privacy_sms_total);
        smsText.setText(privacy_total.replace("%", smsCount + ""));
        privacy_sms_yuandian.setVisibility(View.GONE);

        if (callCount == 0) {
            gradientDrawable = getGradientDrawable("#2c8dea");
        } else {
            gradientDrawable = getGradientDrawable("#f84a4a");
        }
        privacy_call_record_bg.setImageDrawable(gradientDrawable);
        animation(privacy_call_record_bg);
        TextView callText = (TextView) findViewById(R.id.privacy_call_total);
        callText.setText(privacy_total.replace("%", callCount + ""));
        privacy_call_yuandian.setVisibility(View.GONE);

        callText.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PrivacyActivity.this, PrivacyCleanActivity.class));
                finish();
            }
        }, 2000);

        //cleanSms();
        //cleanCallLog();
        //cleanUnReadCallLog();

        //querySms();
        //cleanSms();
        //cleanNoContactSms();
        //cleanReadSms();
        //cleanNoContactCallLog();

    }

    private ArrayList<CallEntity> queryCallEntity() {
        ArrayList<CallEntity> callEntities = new ArrayList<>();
        final String[] projection = null;
        final String selection = null;
        final String[] selectionArgs = null;
        final String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, projection,
                    selection, selectionArgs, sortOrder);
            while (cursor.moveToNext()) {
                String callName = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                String callLogID = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls._ID));
                String callNumber = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                //需要对时间进行一定的处理
                String callDate = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls.DATE));
                long callTime = Long.parseLong(callDate);
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "M-dd HH:mm");
                callDate = sdf.format(new Date(callTime));

                String callType = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls.TYPE));
                String isCallNew = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls.NEW));
//                    if (Integer.parseInt(callType) == (CallLog.Calls.MISSED_TYPE)
//                            && Integer.parseInt(isCallNew) > 0)  //通过call.new进行了限定，会对读取有一些问题，要删掉该限定
               /* if (Integer.parseInt(callType) == (CallLog.Calls.MISSED_TYPE)) {*/
                //textView.setText(callType+"|"+callDate+"|"+callNumber+"|");
//只是以最简单ListView显示联系人的一些数据----适配器的如何配置可查看http://blog.csdn.net/cl18652469346/article/details/52237637
                CallEntity callEntity = new CallEntity();
                callEntity.callName = callName;
                callEntity.callNumber = callNumber;
                callEntities.add(callEntity);
                Log.e("rqy", callName + "----" + callDate + "----" + callNumber + "----" + "callType:" + callType);

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


    private void animation(View view) {
        animatorSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.start();
        view.setVisibility(View.VISIBLE);
    }

    private void animationYuan(View view) {
        animator = ObjectAnimator.ofFloat(view, "rotation", 0, 359);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.start();
    }

    private void animationYuan1(View view) {
        animator = ObjectAnimator.ofFloat(view, "rotation", 0, -359);
        animator.setRepeatCount(-1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.start();
    }

    private void animationLine() {
        animationLine_1 = new AnimatorSet();
        ObjectAnimator animator_ine_1_x = ObjectAnimator.ofFloat(privary_line_1, "scaleX", 0, 2.2f);
        animator_ine_1_x.setRepeatCount(-1);
        ObjectAnimator animator_ine_1_y = ObjectAnimator.ofFloat(privary_line_1, "scaleY", 0, 2.2f);
        animator_ine_1_y.setRepeatCount(-1);
        animationLine_1.setDuration(1000);
        animationLine_1.setInterpolator(new AccelerateDecelerateInterpolator());
        animationLine_1.playTogether(animator_ine_1_x, animator_ine_1_y);

        animationLine_2 = new AnimatorSet();
        ObjectAnimator animator_ine_2_x = ObjectAnimator.ofFloat(privary_line_2, "scaleX", 0, 1.4f);
        animator_ine_2_x.setRepeatCount(-1);
        ObjectAnimator animator_ine_2_y = ObjectAnimator.ofFloat(privary_line_2, "scaleY", 0, 1.4f);
        animator_ine_2_y.setRepeatCount(-1);
        animationLine_2.setDuration(1000);
        animationLine_2.setInterpolator(new AccelerateDecelerateInterpolator());
        animationLine_2.playTogether(animator_ine_2_x, animator_ine_2_y);
        animationLine_1.start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationLine_2.start();
                privary_line_2.setVisibility(View.VISIBLE);
            }
        }, 500);

    }

    private void animationSmall() {
        animatorSmall = new AnimatorSet();
        ObjectAnimator animatoX_1 = ObjectAnimator.ofFloat(privary_small_1, "scaleX", 0f, 1f, 0f);
        animatoX_1.setRepeatCount(-1);
        ObjectAnimator animatoY_1 = ObjectAnimator.ofFloat(privary_small_1, "scaleY", 0f, 1f, 0f);
        animatoY_1.setRepeatCount(-1);
        ObjectAnimator animatoX_2 = ObjectAnimator.ofFloat(privary_small_2, "scaleX", 0f, 1f, 0f);
        animatoX_2.setRepeatCount(-1);
        ObjectAnimator animatoY_2 = ObjectAnimator.ofFloat(privary_small_2, "scaleY", 0f, 1f, 0f);
        animatoY_2.setRepeatCount(-1);
        ObjectAnimator animatoX_3 = ObjectAnimator.ofFloat(privary_small_3, "scaleX", 0f, 1f, 0f);
        animatoX_3.setRepeatCount(-1);
        ObjectAnimator animatoY_3 = ObjectAnimator.ofFloat(privary_small_3, "scaleY", 0f, 1f, 0f);
        animatoY_3.setRepeatCount(-1);
        ObjectAnimator animatoX_4 = ObjectAnimator.ofFloat(privary_small_4, "scaleX", 0f, 1f, 0f);
        animatoX_4.setRepeatCount(-1);
        ObjectAnimator animatoY_4 = ObjectAnimator.ofFloat(privary_small_4, "scaleY", 0f, 1f, 0f);
        animatoY_4.setRepeatCount(-1);
        ObjectAnimator animatoX_5 = ObjectAnimator.ofFloat(privary_small_5, "scaleX", 0f, 1f, 0f);
        animatoX_5.setRepeatCount(-1);
        ObjectAnimator animatoY_5 = ObjectAnimator.ofFloat(privary_small_5, "scaleY", 0f, 1f, 0f);
        animatoY_5.setRepeatCount(-1);
        animatorSmall.setDuration(1500);
        animatorSmall.setInterpolator(new DecelerateInterpolator());
        animatorSmall.playTogether(animatoX_1, animatoY_1, animatoX_2, animatoY_2, animatoX_3, animatoY_3, animatoX_4, animatoY_4, animatoX_5, animatoY_5);//两个动画同时开始
        animatorSmall.start();
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_left:
                    onBackPressed();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        animatorSmall.cancel();
        animationLine_1.cancel();
        animator.cancel();
        animationLine_2.cancel();
        super.onDestroy();
    }


    private void cleanCallLog() {
        ContentResolver resolver = getContentResolver();
        resolver.delete(CallLog.Calls.CONTENT_URI, null, null);
    }

    public void cleanUnReadCallLog() {
        ContentResolver resolver = getContentResolver();
        StringBuilder where = new StringBuilder();
        where.append(CallLog.Calls.NEW);
        where.append(" = 0 ");
      /*  where.append(CallLog.Calls.TYPE);
        where.append(" = ?");*/
        resolver.delete(CallLog.Calls.CONTENT_URI, where.toString(), null);
    }

    public void cleanNoContactCallLog() {
        queryMissCall();
        ContentResolver resolver = getContentResolver();
        StringBuilder where = new StringBuilder();
        where.append(CallLog.Calls.NEW);
        where.append(" = 0 AND ");
        where.append(CallLog.Calls.CACHED_NAME);
        where.append(" IS NULL ");
        resolver.delete(CallLog.Calls.CONTENT_URI, where.toString(), null);
    }


    public void queryMissCall() {
        final String[] projection = null;
        final String selection = null;
        final String[] selectionArgs = null;
        final String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
                    CallLog.Calls.CONTENT_URI, projection,
                    selection, selectionArgs, sortOrder);
            while (cursor.moveToNext()) {
                String callName = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                String callLogID = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls._ID));
                String callNumber = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                //需要对时间进行一定的处理
                String callDate = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls.DATE));
                long callTime = Long.parseLong(callDate);
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "M-dd HH:mm");
                callDate = sdf.format(new Date(callTime));

                String callType = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls.TYPE));
                String isCallNew = cursor.getString(cursor
                        .getColumnIndex(android.provider.CallLog.Calls.NEW));
//                    if (Integer.parseInt(callType) == (CallLog.Calls.MISSED_TYPE)
//                            && Integer.parseInt(isCallNew) > 0)  //通过call.new进行了限定，会对读取有一些问题，要删掉该限定
               /* if (Integer.parseInt(callType) == (CallLog.Calls.MISSED_TYPE)) {*/
                //textView.setText(callType+"|"+callDate+"|"+callNumber+"|");
//只是以最简单ListView显示联系人的一些数据----适配器的如何配置可查看http://blog.csdn.net/cl18652469346/article/details/52237637
                Log.e("rqy", callName + "----" + callDate + "----" + callNumber + "----" + "callType:" + callType);
                /*}*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public ArrayList<SmsEntity> querySms() {
        ArrayList<SmsEntity> smsList = new ArrayList<>();
        Uri uri = Uri.parse("content://sms");
        String[] projections = new String[]{"_id", "address", "date",
                "date_sent", "read", "status", "type", "body"};
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(uri, projections,
                    null, null, null);

            Log.e("aaa", "count-- " + cursor.getCount());
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                int columnCount = cursor.getColumnCount();
                Log.v("aaa", "columnCount--" + columnCount);
                String id = cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));
                String address = cursor.getString(cursor
                        .getColumnIndexOrThrow("address")); // 对方号码
                String date = cursor.getString(cursor
                        .getColumnIndexOrThrow("date"));  // 发件日期
                String date_sent = cursor.getString(cursor
                        .getColumnIndexOrThrow("date_sent"));
                String read = cursor.getString(cursor
                        .getColumnIndexOrThrow("read")); //0 “未读”，1“已读”
                String status = cursor.getString(cursor
                        .getColumnIndexOrThrow("status"));  // 状态 integer   default-1。 -1：接收，0：complete,64： pending, 128failed
                String type = cursor.getString(cursor
                        .getColumnIndexOrThrow("type"));  //  1：inbox 接受 2：sent 发送 3：draft56  4：outbox  5：failed  6：queued
                String body = cursor.getString(cursor
                        .getColumnIndexOrThrow("body")); //短信内容
                SmsEntity smsEntity = new SmsEntity();
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
        ContentResolver cResolver = getContentResolver();
        Uri smsUri = Uri.parse("content://sms/");
        cResolver.delete(smsUri, null, null);
    }

    public void cleanReadSms() {
        ContentResolver cResolver = getContentResolver();
        Uri smsUri = Uri.parse("content://sms/");
        StringBuilder where = new StringBuilder();
        where.append("read = 1");
        cResolver.delete(smsUri, where.toString(), null);
    }

    public void cleanNoContactSms() {
        ContentResolver cResolver = getContentResolver();
        Uri smsUri = Uri.parse("content://sms/");
        StringBuilder where = new StringBuilder();
        where.append("_id = ?");

        ArrayList<SmsInfo> smsInfos = queryNoContactSms();
        GetName getName = new GetName(this);
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
        ContentResolver cResolver = getContentResolver();
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

    public static String getContactNameByPhoneNumber(Context context, String address) {
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

    private GradientDrawable getGradientDrawable(String color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(Color.parseColor(color));
        gradientDrawable.setCornerRadius(R.dimen.d61);
        gradientDrawable.setUseLevel(false);
        return gradientDrawable;
    }


}
