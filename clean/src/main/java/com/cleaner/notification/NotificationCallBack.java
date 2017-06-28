package com.cleaner.notification;

import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/16.
 */

public abstract class NotificationCallBack {
    public abstract void notificationChanged(ArrayList<NotificationInfo> notificationList);
}
