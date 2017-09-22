package com.fraumobi.call.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fraumobi.call.Utils.Constants;
import com.fraumobi.call.database.Database;
import com.fraumobi.call.entries.Contact;
import com.fraumobi.call.entries.RejectInfo;
import com.fraumobi.call.record.R;
import com.fraumobi.call.tools.ACache;

import java.util.ArrayList;

/**
 * Created by ${} on 2017/8/23.
 */

public class CallJiluAdapter extends MybaseAdapter<RejectInfo> {
    private ArrayList<Contact> allContactList;
    ArrayList<String> idList;

    public CallJiluAdapter(Context context) {
        super(context);
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getWritableDatabase();
        ArrayList<Contact> blockObjectList = database.getDataFromTableContacts(db, Constants.TABLE_BLOCK);
        db.close();
        idList = new ArrayList<>();
        for (Contact contact : blockObjectList) {
            idList.add(contact.phoneNum);
        }
        allContactList = (ArrayList<Contact>) ACache.get(context).getAsObject("contacts");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final RejectInfo info = getItem(position);
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
        if (idList != null && idList.contains(info.phoneNum)) {
//        if (info.isChecked) {
            holder.checkBox.setImageResource(R.mipmap.checked);
        } else {
            holder.checkBox.setImageResource(R.mipmap.no_checked);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idList != null && idList.contains(info.phoneNum)) {
                    holder.checkBox.setImageResource(R.mipmap.no_checked);
                    deleteItemToTableBlock(info);
                    idList.remove(info.phoneNum);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.checked);
                    addItemToTableBlock(info);
                    idList.add(info.phoneNum);
                }
            }
        });
        return convertView;
    }

    private void addItemToTableBlock(RejectInfo info) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getWritableDatabase();
        if (!database.tableIsExist(db, Constants.TABLE_BLOCK)) {
            database.createTableContacts(db, Constants.TABLE_BLOCK);
        }
        boolean isAdd = false;
        if (allContactList != null) {
            for (Contact i : allContactList) {

                if (TextUtils.equals(info.phoneNum, i.phoneNum) && TextUtils.equals(info.name, i.name)) {
                    database.insertDataIntoTableContacts(db, Constants.TABLE_BLOCK, i);
                    isAdd = true;
                    break;
                }
            }
        }
        if (!isAdd) {
            Contact contact = new Contact(info.phoneNum, info.name == null ? "" : info.name, info.phoneNum, false, false);

            database.insertDataIntoTableContacts(db, Constants.TABLE_BLOCK, contact);
        }
        db.close();
    }

    private void deleteItemToTableBlock(RejectInfo info) {
        Database database = Database.getInstance(context);
        SQLiteDatabase db = database.getWritableDatabase();
        boolean isAdd = false;
        if (allContactList != null) {
            for (Contact i : allContactList) {
                if (TextUtils.equals(info.phoneNum, i.phoneNum) && TextUtils.equals(info.name, i.name)) {
                    database.deleteDataFromTableContacts(db, Constants.TABLE_BLOCK, i);
                    isAdd = true;
                    break;
                }
            }
        }
        if (!isAdd) {
            Contact contact = new Contact(info.phoneNum, info.name == null ? "" : info.name, info.phoneNum, false, false);
            database.deleteDataFromTableContacts(db, Constants.TABLE_BLOCK, contact);
        }

        db.close();
    }

    public class ViewHolder {
        ImageView icon;
        TextView time;
        ImageView checkBox;
        TextView name;
    }
}