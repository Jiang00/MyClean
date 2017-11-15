package com.supers.call.entries;

import android.support.annotation.NonNull;


import com.supers.call.tools.sort.Cn2Spell;

import java.io.Serializable;

/**
 * Created by Ivy on 2017/4/26.
 */

public class Contact implements Serializable, Comparable<Contact> {

    public String _id;
    public String name;
    public String phoneNum;

    public String nameToPinYin;
    public String firstLetter = "#";

    public boolean isChinese;
    public boolean isEnglish;

    public Contact() {
    }

    public Contact(String _id, String name, String phoneNum, boolean isChinese, boolean isEnglish) {
        this._id = _id;
        this.name = name;
        this.phoneNum = phoneNum.replace(" ", "");
        this.isChinese = isChinese;
        this.isEnglish = isEnglish;
        if (isChinese) {
            nameToPinYin = Cn2Spell.getPinYin(name);
            firstLetter = nameToPinYin.substring(0, 1).toUpperCase();
            if (!firstLetter.matches("[A-Z]")) { // if not in "A-Z", then config it as "#"
                firstLetter = "#";
            }
        } else if (isEnglish) {
            nameToPinYin = name;
            firstLetter = name.substring(0, 1).toUpperCase();
            if (!firstLetter.matches("[A-Z]")) { // if not in "A-Z", then config it as "#"
                firstLetter = "#";
            }
        }
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getNameInPinYin() {
        return nameToPinYin;
    }

    @Override
    public String toString() {
        return "_id=" + _id + " name=" + name + " phoneNum=" + phoneNum;
    }

    @Override
    public int compareTo(@NonNull Contact another) {
        if (firstLetter.equals("#") && !another.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.getFirstLetter().equals("#")) {
            return -1;
        } else {
            return nameToPinYin.compareToIgnoreCase(another.getNameInPinYin());
        }
    }
}
