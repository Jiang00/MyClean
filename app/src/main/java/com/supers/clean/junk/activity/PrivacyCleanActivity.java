package com.supers.clean.junk.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    private Button clean;

    public int cut_type, read_sms_type, strange_sms_type, dismiss_call_type, strange_call_type;  //0全选，1全不选，2部分选中

    public int cutNum = 0;

    public int TYPE_CHECK = 0;

    public int TYPE_NOT_CHECK = 1;

    public int TYPE_PART_CHECK = 2;

    public int READ_SMS_TYPE = 0;

    public int STRANGE_SMS_TYPE = 1;

    public int DISMISS_CALL_TYPE = 2;

    public int STRANGE_CALL_TYPE = 3;

    private ListView read_sms_listview, strange_sms_listview, dissmiss_call_listview, strange_call_listview;
    private ListView[] listViews = new ListView[4];

    private View layout_dismiss_call, layout_strange_call, layout_read_sms, layout_strange_sms;

    private PrivacyClean privacyClean;

    private HashMap<Integer, PrivacyAdapter> privacyAdapters = new HashMap<>();

    private ArrayList<CallEntity> dissmissCallEntities;
    private ArrayList<CallEntity> strangeCallEnties;

    private ArrayList<SmsEntity> readSmsEntities;
    private ArrayList<SmsEntity> strangeSmsEnties;

    private AlertDialog dialog;

    private int checkNum = 0;

    private int readSmsCheckCount = 0;
    private int strangeSmsCheckCount = 0;
    private int dismissCallCheckCount = 0;
    private int strangeCallCheckCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_privacy_clean);
        privacyClean = PrivacyClean.getInstance(this);

        readSmsEntities = getIntent().getParcelableArrayListExtra("read_sms");

        strangeSmsEnties = getIntent().getParcelableArrayListExtra("strange_sms");

        dissmissCallEntities = getIntent().getParcelableArrayListExtra("dismiss_call");

        strangeCallEnties = getIntent().getParcelableArrayListExtra("strange_call");

        boolean isHaveCut = privacyClean.isHaveCutText();
        cutNum = isHaveCut ? 1 : 0;
        privacy_cut_num_text.setText(cutNum + "");

        updateButtonText();

        privacy_dismiss_call_text.setText(dismissCallCheckCount + "");
        privacy_strange_call_text.setText(strangeCallCheckCount + "");

        int totalCallCount = dismissCallCheckCount + strangeCallCheckCount;
        privacy_call_num_text.setText(totalCallCount + "");

        privacy_read_sms_text.setText(readSmsCheckCount + "");
        privacy_strange_sms.setText(strangeSmsCheckCount + "");
        int totalSmsCount = readSmsCheckCount + strangeSmsCheckCount;
        privacy_sms_text.setText(totalSmsCount + "");

        privacy_all_size_text.setText(checkNum + "");

    }

    @Override
    protected void findId() {
        super.findId();

        TextView title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText(R.string.privacy_clean);

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
        clean = (Button) findViewById(R.id.privacy_clean_button);
        clean.setOnClickListener(this);
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
                if (cutNum != 0) {
                    updateButtonText();
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
                if (checkNum > 0) {
                    showDia(checkNum + "");
                } else {
                    goToSuccessPage();
                }
                break;
            case R.id.read_sms_checkbox:
                if (read_sms_type == TYPE_CHECK) {
                    read_sms_type = TYPE_NOT_CHECK;
                    read_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                    for (SmsEntity smsEntity : readSmsEntities) {
                        smsEntity.isChecked = false;
                    }
                } else {
                    read_sms_type = TYPE_CHECK;
                    read_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                    for (SmsEntity smsEntity : readSmsEntities) {
                        smsEntity.isChecked = true;
                    }
                }
                updateButtonText();
                PrivacyAdapter privacyAdapter = privacyAdapters.get(READ_SMS_TYPE);
                if (privacyAdapter != null) {
                    privacyAdapter.updateData(readSmsEntities);
                }
                break;
            case R.id.strange_sms_checkbox:
                if (strange_sms_type == TYPE_CHECK) {
                    strange_sms_type = TYPE_NOT_CHECK;
                    strange_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                    for (SmsEntity smsEntity : strangeSmsEnties) {
                        smsEntity.isChecked = false;
                    }
                } else {
                    strange_sms_type = TYPE_CHECK;
                    strange_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                    for (SmsEntity smsEntity : strangeSmsEnties) {
                        smsEntity.isChecked = true;
                    }
                }
                updateButtonText();
                privacyAdapter = privacyAdapters.get(STRANGE_SMS_TYPE);
                if (privacyAdapter != null) {
                    privacyAdapter.updateData(readSmsEntities);
                }
                break;
            case R.id.dismiss_call_checkbox:
                if (dismiss_call_type == TYPE_CHECK) {
                    dismiss_call_type = TYPE_NOT_CHECK;
                    dismiss_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                    for (CallEntity smsEntity : dissmissCallEntities) {
                        smsEntity.isChecked = false;
                    }
                } else {
                    dismiss_call_type = TYPE_CHECK;
                    dismiss_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                    for (CallEntity smsEntity : dissmissCallEntities) {
                        smsEntity.isChecked = true;
                    }
                }
                updateButtonText();
                privacyAdapter = privacyAdapters.get(DISMISS_CALL_TYPE);
                if (privacyAdapter != null) {
                    privacyAdapter.updateData(dissmissCallEntities);
                }
                break;
            case R.id.strange_call_checkbox:
                if (strange_call_type == TYPE_CHECK) {
                    strange_call_type = TYPE_NOT_CHECK;
                    strange_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
                    for (CallEntity smsEntity : strangeCallEnties) {
                        smsEntity.isChecked = false;
                    }
                } else {
                    strange_call_type = TYPE_CHECK;
                    strange_call_checkbox.setImageResource(R.mipmap.privacy_checkbox_all_checked);
                    for (CallEntity smsEntity : strangeCallEnties) {
                        smsEntity.isChecked = true;
                    }
                }
                updateButtonText();
                privacyAdapter = privacyAdapters.get(STRANGE_CALL_TYPE);
                if (privacyAdapter != null) {
                    privacyAdapter.updateData(strangeCallEnties);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void showDia(String count) {

        View view = View.inflate(this, R.layout.dialog_file, null);
        TextView message = (TextView) view.findViewById(R.id.message);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancle = (TextView) view.findViewById(R.id.cancle);

        view.findViewById(R.id.dialog_icon).setVisibility(View.GONE);

        message.setText(getString(R.string.delete_2, count));
        dialog = new AlertDialog.Builder(PrivacyCleanActivity.this).create();
        dialog.setView(view);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cutNum != 0 && cut_type == TYPE_CHECK) {
                    privacyClean.cleanCut();
                }

                deleteCheckedSms();

                deleteCheckedCall();

                goToSuccessPage();

                dialog.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void goToSuccessPage() {
        Intent intent = new Intent(this, SuccessActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteCheckedCall() {
        StringBuilder call = new StringBuilder();

        ArrayList<Integer> callIdList = new ArrayList<>();

        for (CallEntity smsEntity : dissmissCallEntities) {
            if (smsEntity.isChecked) {
                callIdList.addAll(smsEntity.idList);
            }
        }
        for (CallEntity smsEntity : strangeCallEnties) {
            if (smsEntity.isChecked) {
                callIdList.addAll(smsEntity.idList);
            }
        }
        int size = callIdList.size();
        for (int i = 0; i < size; i++) {
            call.append(callIdList.get(i));
            if (i != size - 1) {
                call.append(",");
            }
        }
        if (size > 0) {
            privacyClean.deleteCall(call.toString());
        }
    }

    private void deleteCheckedSms() {
        StringBuilder sms = new StringBuilder();

        ArrayList<Integer> smsIdList = new ArrayList<>();

        for (SmsEntity smsEntity : readSmsEntities) {
            if (smsEntity.isChecked) {
                smsIdList.addAll(smsEntity.idList);
            }
        }
        for (SmsEntity smsEntity : strangeSmsEnties) {
            if (smsEntity.isChecked) {
                smsIdList.addAll(smsEntity.idList);
            }
        }
        int size = smsIdList.size();
        for (int i = 0; i < size; i++) {
            sms.append(smsIdList.get(i));
            if (i != size - 1) {
                sms.append(",");
            }
        }
        if (size > 0) {
            privacyClean.deleteSms(sms.toString());
        }
    }

    private void itemClick(ListView read_sms_listview, ArrayList<? extends PrivacyData> readSmsEntities, int type) {
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
                //没一个选中
                read_sms_checkbox.setImageResource(R.mipmap.privacy_checkbox_not_checked);
            } else if (unCheckCount == 0) {
                read_sms_type = TYPE_CHECK;
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
                //没一个选中
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
                //没一个选中
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
        updateButtonText();
    }

    public void updateButtonText() {
        readSmsCheckCount = 0;
        strangeSmsCheckCount = 0;
        dismissCallCheckCount = 0;
        strangeCallCheckCount = 0;
        for (SmsEntity smsEntity : readSmsEntities) {
            if (smsEntity.isChecked) {
                readSmsCheckCount += smsEntity.idList.size();
            }
        }

        for (SmsEntity smsEntity : strangeSmsEnties) {
            if (smsEntity.isChecked) {
                strangeSmsCheckCount += smsEntity.idList.size();
            }
        }

        for (CallEntity smsEntity : dissmissCallEntities) {
            if (smsEntity.isChecked) {
                dismissCallCheckCount += smsEntity.idList.size();
            }
        }

        for (CallEntity smsEntity : strangeCallEnties) {
            if (smsEntity.isChecked) {
                strangeCallCheckCount += smsEntity.idList.size();
            }
        }

        checkNum = readSmsCheckCount + strangeSmsCheckCount + dismissCallCheckCount + strangeCallCheckCount;
        if (cutNum != 0 && cut_type == TYPE_CHECK) {
            checkNum++;
        }

        clean.setText(getString(R.string.junk_button) + "( " + checkNum + " )");
    }
}
