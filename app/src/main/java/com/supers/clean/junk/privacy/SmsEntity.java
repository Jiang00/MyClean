package com.supers.clean.junk.privacy;

import com.supers.clean.junk.entity.PrivacyData;

/**
 * Created by renqingyou on 2017/6/9.
 */

public class SmsEntity extends PrivacyData {
    public String address;
    public String type;
    public String date;
    public int read;
    public String status;
    public int id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmsEntity smsEntity = (SmsEntity) o;

        return address.equals(smsEntity.address);

    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
