package com.supers.clean.junk.privacy;

import android.os.Parcel;
import android.os.Parcelable;

import com.supers.clean.junk.entity.PrivacyData;

/**
 * Created by renqingyou on 2017/6/9.
 */

public class CallEntity extends PrivacyData implements Parcelable{
    public String callName;
    public String callNumber;
    public String callLogID;
    public String callData;
    public int callType;
    public String isCallNew;

    public CallEntity() {
        super();
    }

    protected CallEntity(Parcel in) {
        callName = in.readString();
        callNumber = in.readString();
        callLogID = in.readString();
        callData = in.readString();
        callType = in.readInt();
        isCallNew = in.readString();
    }

    public static final Creator<CallEntity> CREATOR = new Creator<CallEntity>() {
        @Override
        public CallEntity createFromParcel(Parcel in) {
            return new CallEntity(in);
        }

        @Override
        public CallEntity[] newArray(int size) {
            return new CallEntity[size];
        }
    };

    @Override
    public String toString() {
        return "CallEntity{" +
                "callName='" + callName + '\'' +
                ", callNumber='" + callNumber + '\'' +
                ", callLogID='" + callLogID + '\'' +
                ", callData='" + callData + '\'' +
                ", callType=" + callType +
                ", isCallNew='" + isCallNew + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CallEntity that = (CallEntity) o;

        if (!callName.equals(that.callName)) return false;
        return callNumber.equals(that.callNumber);

    }

    @Override
    public int hashCode() {
        int result = callName.hashCode();
        result = 31 * result + callNumber.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(callName);
        dest.writeString(callNumber);
        dest.writeString(callLogID);
        dest.writeString(callData);
        dest.writeInt(callType);
        dest.writeString(isCallNew);
    }
}
