package com.supers.clean.junk.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CallLog;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.adapter.PrivacyAdapter;
import com.supers.clean.junk.entity.PrivacyData;
import com.supers.clean.junk.privacy.CallEntity;
import com.supers.clean.junk.privacy.PrivacyClean;
import com.supers.clean.junk.privacy.SmsEntity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ivy on 2017/5/9.
 */

public class PrivacyCleanActivity extends BaseActivity implements View.OnClickListener {

    private TextView privacy_all_size_text, privacy_cut_num_text, privacy_sms_text, privacy_read_sms_text, privacy_strange_sms, privacy_call_num_text, privacy_dismiss_call_text, privacy_strange_call_text;

    private ImageView cut_checkbox, read_sms_checkbox, strange_sms_checkbox, dismiss_call_checkbox, strange_call_checkbox;

    public int cut_type, read_sms_type, strange_sms_type, dismiss_call_type, strange_call_type;  //0全选，1全不选，2部分选中

    public int TYPE_ALL = 0;

    public int TYPE_NOT_CHECK = 1;

    public int TYPE_PART_CHECK = 2;

    public int READ_SMS_TYPE = 0;

    public int STRANGE_SMS_TYPE = 1;

    public int DISMISS_CALL_TYPE = 2;

    public int STRANGE_CALL_TYPE = 3;

    private ListView[] listViews = new ListView[4];
    private ListView read_sms_listview, strange_sms_listview, dissmiss_call_listview, strange_call_listview;

    private View layout_dismiss_call, layout_strange_call, layout_read_sms, layout_strange_sms;

    private PrivacyClean privacyClean;

    private HashMap<Integer, PrivacyAdapter> privacyAdapters = new HashMap<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int missNum = dissmissCallEntities.size();
            int strangeCallNum = strangeCallEnties.size();
            privacy_dismiss_call_text.setText(missNum + "");
            privacy_strange_call_text.setText(strangeCallNum + "");

            int totalCount = missNum + strangeCallNum;
            privacy_call_num_text.setText(totalCount + "");

            int count = smsEntities.size() + totalCount;
            privacy_read_sms_text.setText(readSmsEntities.size() + "");
            privacy_strange_sms.setText(strangeSmsEnties.size() + "");
            privacy_sms_text.setText(smsEntities.size() + "");

            privacy_all_size_text.setText(count + "");
        }
    };

    ArrayList<CallEntity> dissmissCallEntities = new ArrayList<>();
    ArrayList<CallEntity> strangeCallEnties = new ArrayList<>();
    ArrayList<CallEntity> callEntities = new ArrayList<>();


    ArrayList<SmsEntity> readSmsEntities = new ArrayList<>();
    ArrayList<SmsEntity> strangeSmsEnties = new ArrayList<>();
    ArrayList<SmsEntity> smsEntities = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_privacy_clean);
        privacyClean = PrivacyClean.getInstance(this);

        boolean isHaveCut = privacyClean.isHaveCutText();
        privacy_cut_num_text.setText(isHaveCut ? 1 + "" : 0 + "");

        new Thread() {
            @Override
            public void run() {
                smsEntities = privacyClean.querySms();

                for (SmsEntity smsEntity : smsEntities) {
                    if (smsEntity.read == 1) {
                        //已读短信
                        boolean isHaveSameAddress = false;
                        for (int i = 0; i < readSmsEntities.size(); i++) {
                            SmsEntity smsEntities = readSmsEntities.get(i);
                            if (TextUtils.equals(smsEntities.address, smsEntity.address)) {
                                smsEntities.count++;
                                smsEntities.idList.add(smsEntity.id);
                                isHaveSameAddress = true;
                                break;
                            }
                        }
                        if (!isHaveSameAddress) {
                            smsEntity.idList = new ArrayList<>();
                            smsEntity.idList.add(smsEntity.id);
                            smsEntity.count++;
                            readSmsEntities.add(smsEntity);
                        }
                    } else {
                        String name = privacyClean.getContactNameByPhoneNumber(PrivacyCleanActivity.this, smsEntity.address);
                        if (!TextUtils.isEmpty(name)) {
                            continue;
                        }
                        boolean isHaveSameAddress = false;
                        for (int i = 0; i < strangeSmsEnties.size(); i++) {
                            SmsEntity smsEntities = strangeSmsEnties.get(i);
                            if (TextUtils.equals(smsEntities.address, smsEntity.address)) {
                                smsEntities.count++;
                                smsEntities.idList.add(smsEntity.id);
                                isHaveSameAddress = true;
                                break;
                            }
                        }
                        if (!isHaveSameAddress) {
                            smsEntity.idList = new ArrayList<>();
                            smsEntity.idList.add(smsEntity.id);
                            smsEntity.count++;
                            strangeSmsEnties.add(smsEntity);
                        }
                    }
                }

                Log.e("rqy", smsEntities.size() + "");
                callEntities = privacyClean.queryCall();
                for (CallEntity callEntity : callEntities) {
                    Log.e("rqy", callEntity + "");
                    if (callEntity.callType == CallLog.Calls.MISSED_TYPE) {
                        //未接来电
                        boolean isHaveSameAddress = false;
                        for (int i = 0; i < dissmissCallEntities.size(); i++) {
                            CallEntity callEn = dissmissCallEntities.get(i);
                            if (TextUtils.equals(callEntity.callNumber, callEn.callNumber)) {
                                callEn.count++;
                                callEn.idList.add(callEntity.id);
                                isHaveSameAddress = true;
                                break;
                            }
                        }
                        if (!isHaveSameAddress) {
                            callEntity.idList = new ArrayList<>();
                            callEntity.idList.add(callEntity.id);
                            callEntity.count++;
                            dissmissCallEntities.add(callEntity);
                        }
                    } else {
                        if (callEntity.callName != null) {
                            continue;
                        }

                        boolean isHaveSameAddress = false;
                        for (int i = 0; i < strangeCallEnties.size(); i++) {
                            CallEntity callEn = strangeCallEnties.get(i);
                            if (TextUtils.equals(callEntity.callNumber, callEn.callNumber)) {
                                callEn.count++;
                                callEn.idList.add(callEntity.id);
                                isHaveSameAddress = true;
                                break;
                            }
                        }
                        if (!isHaveSameAddress) {
                            callEntity.idList = new ArrayList<>();
                            callEntity.idList.add(callEntity.id);
                            callEntity.count++;
                            strangeCallEnties.add(callEntity);
                        }
                    }
                }
                handler.sendEmptyMessage(3);
            }
        }.start();
    }

    @Override
    protected void findId() {
        super.findId();

        layout_dismiss_call = findViewById(R.id.layout_dismiss_call);
        layout_strange_call = findViewById(R.id.layout_strange_call);
        layout_read_sms = findViewById(R.id.layout_read_sms);
        layout_strange_sms = findViewById(R.id.layout_strange_sms);

        privacy_all_size_text = (TextView) findViewById(R.id.privacy_all_size_text);
        privacy_cut_num_text = (TextView) findViewById(R.id.privacy_cut_num_text);
        privacy_sms_text = (TextView) findViewById(R.id.privacy_sms_text);
        privacy_read_sms_text = (TextView) findViewById(R.id.privacy_read_sms_text);
        privacy_strange_sms = (TextView) findViewById(R.id.privacy_strange_sms);
        privacy_call_num_text = (TextView) findViewById(R.id.privacy_call_num_text);
        privacy_dismiss_call_text = (TextView) findViewById(R.id.privacy_dismiss_call_text);
        privacy_strange_call_text = (TextView) findViewById(R.id.privacy_strange_call_text);

        cut_checkbox = (ImageView) findViewById(R.id.cut_checkbox);
        read_sms_checkbox = (ImageView) findViewById(R.id.read_sms_checkbox);
        strange_sms_checkbox = (ImageView) findViewById(R.id.strange_sms_checkbox);
        dismiss_call_checkbox = (ImageView) findViewById(R.id.dismiss_call_checkbox);
        strange_call_checkbox = (ImageView) findViewById(R.id.strange_call_checkbox);

        read_sms_listview = (ListView) findViewById(R.id.read_sms_listview);
        strange_sms_listview = (ListView) findViewById(R.id.strange_sms_listview);
        dissmiss_call_listview = (ListView) findViewById(R.id.dissmiss_call_listview);
        strange_call_listview = (ListView) findViewById(R.id.strange_call_listview);
        listViews[0] = read_sms_listview;
        listViews[1] = strange_sms_listview;
        listViews[2] = dissmiss_call_listview;
        listViews[3] = strange_call_listview;

        findViewById(R.id.title_left).setOnClickListener(this);
        findViewById(R.id.privacy_clean_button).setOnClickListener(this);
        layout_dismiss_call.setOnClickListener(this);
        layout_strange_call.setOnClickListener(this);
        layout_read_sms.setOnClickListener(this);
        layout_strange_sms.setOnClickListener(this);

        cut_checkbox.setOnClickListener(this);
        read_sms_checkbox.setOnClickListener(this);
        strange_sms_checkbox.setOnClickListener(this);
        dismiss_call_checkbox.setOnClickListener(this);
        strange_call_checkbox.setOnClickListener(this);
    }

    public void setListViewVisible(ListView listView) {
        if (listView.getVisibility() == View.VISIBLE) {
            return;
        }
        for (ListView listView1 : listViews) {
            if (listView1 == listView) {
                listView.setVisibility(View.VISIBLE);
            } else {
                listView1.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cut_checkbox:
                if (cut_type == 0) {
                    cut_type = 1;
                    cut_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                } else {
                    cut_type = 0;
                    cut_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                }
                break;
            case R.id.title_left:
                finish();
                break;
            case R.id.layout_dismiss_call:
                itemClick(dissmiss_call_listview, dissmissCallEntities, DISMISS_CALL_TYPE);
                break;
            case R.id.layout_strange_call:
                itemClick(strange_call_listview, strangeCallEnties, STRANGE_CALL_TYPE);
                break;
            case R.id.layout_strange_sms:
                itemClick(strange_sms_listview, strangeSmsEnties, STRANGE_SMS_TYPE);
                break;
            case R.id.layout_read_sms:
                itemClick(read_sms_listview, readSmsEntities, READ_SMS_TYPE);
                break;
            case R.id.privacy_clean_button:
                break;
            case R.id.read_sms_checkbox:
                if (read_sms_type == TYPE_ALL) {
                    read_sms_type = TYPE_NOT_CHECK;
                    read_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                    for (SmsEntity smsEntity : readSmsEntities) {
                        smsEntity.isChecked = false;
                    }
                } else {
                    read_sms_type = TYPE_ALL;
                    read_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                    for (SmsEntity smsEntity : readSmsEntities) {
                        smsEntity.isChecked = true;
                    }
                }
                PrivacyAdapter privacyAdapter = privacyAdapters.get(READ_SMS_TYPE);
                if (privacyAdapter != null) {
                    privacyAdapter.updateData(readSmsEntities);
                }
                break;
            case R.id.strange_sms_checkbox:
                if (strange_sms_type == TYPE_ALL) {
                    strange_sms_type = TYPE_NOT_CHECK;
                    strange_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                    for (SmsEntity smsEntity : strangeSmsEnties) {
                        smsEntity.isChecked = false;
                    }
                } else {
                    strange_sms_type = TYPE_ALL;
                    strange_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                    for (SmsEntity smsEntity : strangeSmsEnties) {
                        smsEntity.isChecked = true;
                    }
                }
                privacyAdapter = privacyAdapters.get(STRANGE_SMS_TYPE);
                if (privacyAdapter != null) {
                    privacyAdapter.updateData(readSmsEntities);
                }
                break;
            case R.id.dismiss_call_checkbox:
                if (dismiss_call_type == TYPE_ALL) {
                    dismiss_call_type = TYPE_NOT_CHECK;
                    dismiss_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                    for (CallEntity smsEntity : dissmissCallEntities) {
                        smsEntity.isChecked = false;
                    }
                } else {
                    dismiss_call_type = TYPE_ALL;
                    dismiss_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                    for (CallEntity smsEntity : dissmissCallEntities) {
                        smsEntity.isChecked = true;
                    }
                }
                privacyAdapter = privacyAdapters.get(DISMISS_CALL_TYPE);
                if (privacyAdapter != null) {
                    privacyAdapter.updateData(dissmissCallEntities);
                }
                break;
            case R.id.strange_call_checkbox:
                if (strange_call_type == TYPE_ALL) {
                    strange_call_type = TYPE_NOT_CHECK;
                    strange_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                    for (CallEntity smsEntity : strangeCallEnties) {
                        smsEntity.isChecked = false;
                    }
                } else {
                    strange_call_type = TYPE_ALL;
                    strange_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                    for (CallEntity smsEntity : strangeCallEnties) {
                        smsEntity.isChecked = true;
                    }
                }
                privacyAdapter = privacyAdapters.get(STRANGE_CALL_TYPE);
                if (privacyAdapter != null) {
                    privacyAdapter.updateData(strangeCallEnties);
                }
                break;
            default:
                break;
        }
    }

    private void itemClick(ListView read_sms_listview, ArrayList<? extends PrivacyData> readSmsEntities, int type) {
        Log.e("rqy", "itemClick--" + type);
        if (read_sms_listview.getVisibility() == View.VISIBLE) {
            read_sms_listview.setVisibility(View.GONE);
            return;
        }
        if (readSmsEntities.isEmpty()) {
            return;
        }
        setListViewVisible(read_sms_listview);
        if (read_sms_listview.getAdapter() == null) {
            PrivacyAdapter privacyAdapter = new PrivacyAdapter(type, readSmsEntities, this);
            privacyAdapters.put(type, privacyAdapter);
            read_sms_listview.setAdapter(privacyAdapter);
        }
    }

    public void onDataChange(int type) {
        if (type == READ_SMS_TYPE) {
            int checkCount = 0;
            int unCheckCount = 0;
            for (SmsEntity smsEntity : readSmsEntities) {
                if (smsEntity.isChecked) {
                    checkCount++;
                } else {
                    unCheckCount++;
                }
            }
            if (checkCount == 0) {
                read_sms_type = TYPE_NOT_CHECK;
                //每一个选中
                read_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
            } else if (unCheckCount == 0) {
                read_sms_type = TYPE_ALL;
                read_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
            } else {
                read_sms_type = TYPE_PART_CHECK;
                read_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_half_checked);
            }
        } else if (type == STRANGE_SMS_TYPE) {
            int checkCount = 0;
            int unCheckCount = 0;
            for (SmsEntity smsEntity : strangeSmsEnties) {
                if (smsEntity.isChecked) {
                    checkCount++;
                } else {
                    unCheckCount++;
                }
            }
            if (checkCount == 0) {
                //每一个选中
                strange_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
            } else if (unCheckCount == 0) {
                strange_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
            } else {
                strange_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_half_checked);
            }
        } else if (type == DISMISS_CALL_TYPE) {
            int checkCount = 0;
            int unCheckCount = 0;
            for (CallEntity smsEntity : dissmissCallEntities) {
                if (smsEntity.isChecked) {
                    checkCount++;
                } else {
                    unCheckCount++;
                }
            }
            if (checkCount == 0) {
                //每一个选中
                dismiss_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
            } else if (unCheckCount == 0) {
                dismiss_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
            } else {
                dismiss_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_half_checked);
            }
        } else if (type == STRANGE_CALL_TYPE) {
            int checkCount = 0;
            int unCheckCount = 0;
            for (CallEntity smsEntity : strangeCallEnties) {
                if (smsEntity.isChecked) {
                    checkCount++;
                } else {
                    unCheckCount++;
                }
            }
            if (checkCount == 0) {
                //每一个选中
                strange_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
            } else if (unCheckCount == 0) {
                strange_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
            } else {
                strange_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_half_checked);
            }
        }
    }
}
