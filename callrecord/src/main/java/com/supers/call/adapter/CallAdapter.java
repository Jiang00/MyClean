package com.supers.call.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.supers.call.Utils.Constants;
import com.supers.call.database.Database;
import com.supers.call.entries.Contact;
import com.supers.call.entries.RejectInfo;
import com.supers.call.record.R;

import java.util.ArrayList;

/**
 * Created by ${} on 2017/8/23.
 */

public class CallAdapter extends MybaseAdapter<RejectInfo> {
    ArrayList<String> idList;

    public CallAdapter(Context context) {
        super(context);
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getWritableDatabase();
        ArrayList<Contact> blockObjectList = database.getDataFromTableContacts(db, Constants.TABLE_BLOCK);
        db.close();
        idList = new ArrayList<>();
        for (Contact contact : blockObjectList) {
            idList.add(contact.phoneNum);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RejectInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater
                    .inflate(R.layout.layout_call_item, null);
            holder.name = (TextView) convertView
                    .findViewById(R.id.call_name);
            holder.time = (TextView) convertView
                    .findViewById(R.id.call_time);
            holder.icon = (ImageView) convertView
                    .findViewById(R.id.call_icon);
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.call_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (info.name != null) {
            holder.name.setText(info.name);
        } else {
            holder.name.setText(info.phoneNum);
        }
        holder.time.setText(info.date);
        holder.icon.setImageResource(R.mipmap.call_icon);
//        if (idList != null && idList.contains(info.phoneNum)) {
        if (info.isChecked) {
            holder.checkBox.setImageResource(R.mipmap.checked);
        } else {
            holder.checkBox.setImageResource(R.mipmap.no_checked);
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView icon;
        TextView time;
        ImageView checkBox;
        TextView name;
    }
}