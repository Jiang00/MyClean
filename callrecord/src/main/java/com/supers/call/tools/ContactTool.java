package com.supers.call.tools;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;


import com.supers.call.Utils.Util;
import com.supers.call.database.Database;
import com.supers.call.entries.Contact;

import java.util.ArrayList;

/**
 */

public class ContactTool {

    public static ArrayList<Contact> getAllContacts(ContentResolver cr, boolean isFirst, Database database, SQLiteDatabase db, ACache aCache) {
        if (cr == null) {
            return null;
        }
        ArrayList<Contact> list = new ArrayList<>();
        Cursor cursor = null;
        boolean isChinese = Util.isChinese();
        boolean isEnglish = Util.isEnglish();
        String _id;
        String name;
        String phoneNum;
        try {
            cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                            ContactsContract.Contacts.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER},
                    null,
                    null,
                    "sort_key");
            if (cursor == null) {
                return null;
            }
            while (cursor.moveToNext()) {
                _id = cursor.getString(0);
                name = cursor.getString(1);
                phoneNum = cursor.getString(2);
                Contact contact = new Contact(_id, name, phoneNum, isChinese, isEnglish);
                list.add(contact);
                if (isFirst) {
                    if (aCache != null) {
                        aCache.put(_id, true);
                    }
                }
            }
        } catch (Exception e) {
            list = null;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return list;
    }

    // 一个添加联系人信息的例子
    public static void addContact(Context context, String name, String phoneNumber) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        context.getContentResolver().insert(Data.CONTENT_URI, values);
        values.clear();
    }
}
