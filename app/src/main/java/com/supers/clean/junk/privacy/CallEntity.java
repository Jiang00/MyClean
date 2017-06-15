package com.supers.clean.junk.privacy;

import com.supers.clean.junk.entity.PrivacyData;

/**
 * Created by renqingyou on 2017/6/9.
 */

public class CallEntity extends PrivacyData {
    public String callName;
    public String callNumber;
    public String callLogID;
    public String callData;
    public int callType;
    public String isCallNew;

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
}
