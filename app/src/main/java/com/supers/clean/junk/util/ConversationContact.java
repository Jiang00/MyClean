package com.supers.clean.junk.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by Ivy on 2017/5/5.
 */

public class ConversationContact implements Serializable {

    public String name;
    public String phoneNum;
    public String thumb;
    public String rawContactId;
    public String contactID;
    public Drawable drawable;
    public Bitmap bitmap;

    public String nameToPinYin;
    public String firstLetter;
    public boolean isCollect;
    public boolean isChinese;
    public boolean isEnglish;

    public ConversationContact() {
    }

    protected ConversationContact(Parcel in) {
        name = in.readString();
        phoneNum = in.readString();
        thumb = in.readString();
        rawContactId = in.readString();
        contactID = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public String toString() {
        return "name=" + name + "  phoneNum=" + phoneNum + " headUri=" + thumb + " rawContactId=" + rawContactId + " contactID=" + contactID + " firstLetter=" + firstLetter;
    }

}
