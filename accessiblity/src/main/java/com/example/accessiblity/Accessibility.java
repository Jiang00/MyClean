package com.example.accessiblity;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by song on 15/9/1.
 */
public class Accessibility extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.e("Accessiblity", event.toString() + " pkg " + event.getPackageName());
    }

    @Override
    public void onInterrupt() {
        Log.e("WellDone", "interrupt");
    }

//    @Override
//    protected void onServiceConnected() {
//        AccessibilityServiceInfo asi = new AccessibilityServiceInfo();
//        asi.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
//        asi.flags = AccessibilityServiceInfo.DEFAULT;
//        asi.notificationTimeout = 100;
//        setServiceInfo(asi);
//    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("onUnbind", intent + "");
        return super.onUnbind(intent);
    }
}
