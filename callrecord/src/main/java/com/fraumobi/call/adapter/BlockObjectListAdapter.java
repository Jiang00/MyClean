package com.fraumobi.call.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.fraumobi.call.Utils.Constants;
import com.fraumobi.call.database.Database;
import com.fraumobi.call.entries.Contact;
import com.fraumobi.call.record.R;

import java.util.ArrayList;

/**
 * Created by Ivy on 2017/4/27.
 */

public class BlockObjectListAdapter extends RecyclerView.Adapter<BlockObjectListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Contact> allContactList;
    private ArrayList<String> selectedList;

    public BlockObjectListAdapter(Context context, ArrayList<Contact> allContactList, ArrayList<String> selectedList) {
        this.mContext = context;
        this.allContactList = allContactList;
        this.selectedList = selectedList;
    }

    public void update(ArrayList<Contact> allContactList) {
        this.allContactList = allContactList;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_contacts_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Contact contact = allContactList.get(position);
        String catalog = contact.getFirstLetter();
        if (position == getPositionForSection(catalog)) {
            holder.catalog.setVisibility(View.VISIBLE);
            holder.catalog.setText(contact.getFirstLetter().toUpperCase());
        } else {
            holder.catalog.setVisibility(View.GONE);
        }
        String tmp = "";
        if (contact.name != null) {
            tmp = contact.name;
        } else if (contact.phoneNum != null) {
            tmp = contact.phoneNum;
        }
        holder.name.setText(tmp);
        holder.phoneNum.setText(contact.phoneNum);
        if (selectedList != null && selectedList.contains(contact.phoneNum)) {
            holder.checkBox.setImageResource(R.mipmap.checked);
        } else {
            holder.checkBox.setImageResource(R.mipmap.no_checked);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedList != null && selectedList.contains(contact.phoneNum)) {
                    holder.checkBox.setImageResource(R.mipmap.no_checked);
                    deleteItemToTableBlock(contact);
                    selectedList.remove(contact.phoneNum);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.checked);
                    addItemToTableBlock(contact);
                    selectedList.add(contact.phoneNum);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allContactList == null ? 0 : allContactList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView headIcon;
        public TextView name;
        public TextView phoneNum;
        public ImageView checkBox;
        public TextView catalog;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.contact_name);
            phoneNum = (TextView) itemView.findViewById(R.id.contact_number);
            headIcon = (ImageView) itemView.findViewById(R.id.contact_head_icon);
            checkBox = (ImageView) itemView.findViewById(R.id.contactCheckBox);
            catalog = (TextView) itemView.findViewById(R.id.catalog);
        }
    }

    private void addItemToTableBlock(Contact contact) {
        Database database = Database.getInstance(mContext);
        SQLiteDatabase db = database.getWritableDatabase();
        if (!database.tableIsExist(db, Constants.TABLE_BLOCK)) {
            database.createTableContacts(db, Constants.TABLE_BLOCK);
        }
        database.insertDataIntoTableContacts(db, Constants.TABLE_BLOCK, contact);
        db.close();
    }

    private void deleteItemToTableBlock(Contact contact) {
        Database database = Database.getInstance(mContext);
        SQLiteDatabase db = database.getWritableDatabase();
        database.deleteDataFromTableContacts(db, Constants.TABLE_BLOCK, contact);
        db.close();
    }

    public int getPositionForSection(String catalog) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = allContactList.get(i).getFirstLetter();
            if (catalog.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }

}
