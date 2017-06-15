package com.supers.clean.junk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.PrivacyCleanActivity;
import com.supers.clean.junk.entity.PrivacyData;
import com.supers.clean.junk.privacy.CallEntity;
import com.supers.clean.junk.privacy.SmsEntity;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/6/13.
 */

public class PrivacyAdapter extends BaseAdapter {

    private ArrayList<PrivacyData> entities;
    private int type;

    private PrivacyCleanActivity mContext;

    public PrivacyAdapter(int type, ArrayList missEntities, PrivacyCleanActivity context) {
        this.type = type;
        entities = missEntities;
        mContext = context;
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int position) {
        return entities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.privacy_item, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.privacy_item_checkbox = (CheckBox) convertView.findViewById(R.id.privacy_item_checkbox);
        viewHolder.privacy_item_num_text = (TextView) convertView.findViewById(R.id.privacy_item_num_text);
        viewHolder.privacy_item_text = (TextView) convertView.findViewById(R.id.privacy_item_text);

        final PrivacyData object = (PrivacyData) getItem(position);
        if (object instanceof CallEntity) {
            CallEntity callEntity = (CallEntity) object;
            viewHolder.privacy_item_num_text.setText(callEntity.count + "");
            viewHolder.privacy_item_text.setText(callEntity.callName == null ? callEntity.callNumber : callEntity.callName);
        } else if (object instanceof SmsEntity) {
            SmsEntity smsEntity = (SmsEntity) object;
            viewHolder.privacy_item_num_text.setText(smsEntity.count + "");
            viewHolder.privacy_item_text.setText(smsEntity.address);
        }
        viewHolder.privacy_item_checkbox.setChecked(object.isChecked);
        viewHolder.privacy_item_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                object.isChecked = isChecked;
                mContext.onDataChange(type);
            }
        });

        return convertView;
    }

    public void updateData(ArrayList privacyDatas) {
        if (privacyDatas == null || privacyDatas.isEmpty()) {
            return;
        }
        //this.entities = privacyDatas;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView privacy_item_text, privacy_item_num_text;
        CheckBox privacy_item_checkbox;
    }
}
