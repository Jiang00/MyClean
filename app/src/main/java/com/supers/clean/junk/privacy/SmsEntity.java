package com.supers.clean.junk.privacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.supers.clean.junk.entity.PrivacyData;

/**
 * Created by renqingyou on 2017/6/9.
 */

public class SmsEntity extends PrivacyData implements Parcelable {
    public String address;
    public String type;
    public String date;
    public int read;
    public String status;
    public int id;

    public SmsEntity() {
    }

    protected SmsEntity(Parcel in) {
        address = in.readString();
        type = in.readString();
        date = in.readString();
        read = in.readInt();
        status = in.readString();
        id = in.readInt();
    }

    public static final Creator<SmsEntity> CREATOR = new Creator<SmsEntity>() {
        @Override
        public SmsEntity createFromParcel(Parcel in) {
            return new SmsEntity(in);
        }

        @Override
        public SmsEntity[] newArray(int size) {
            return new SmsEntity[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(type);
        dest.writeString(date);
        dest.writeInt(read);
        dest.writeString(status);
        dest.writeInt(id);
    }
}
