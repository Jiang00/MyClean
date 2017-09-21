package com.fraumobi.call.tools;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;


import com.fraumobi.call.Utils.Constants;
import com.fraumobi.call.Utils.Util;
import com.fraumobi.call.database.Database;
import com.fraumobi.call.entries.Contact;

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

}
