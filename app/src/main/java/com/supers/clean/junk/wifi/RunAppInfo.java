package com.supers.clean.junk.wifi;

import android.graphics.drawable.Drawable;

/**
 * Created by Ivy on 2017/4/10.
 */

public class RunAppInfo {
    private String appLabel;
    private Drawable appIcon;
    private String pkgName;
    private int pid;
    private String processName, upspeed, downspeed;
    private Boolean checked;
    private long lastRxBytes, lastTxBytes;

    public RunAppInfo() {
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appName) {
        this.appLabel = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }


    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public long getLastRxBytes() {
        return lastRxBytes;
    }

    public void setLastRxBytes(long lastRxBytes) {
        this.lastRxBytes = lastRxBytes;
    }

    public long getLastTxBytes() {
        return lastTxBytes;
    }

    public void setLastTxBytes(long lastTxBytes) {
        this.lastTxBytes = lastTxBytes;
    }

    public String getUpspeed() {
        return upspeed;
    }

    public void setUpspeed(String upspeed) {
        this.upspeed = upspeed;
    }

    public String getDownspeed() {
        return downspeed;
    }

    public void setDownspeed(String downspeed) {
        this.downspeed = downspeed;
    }
}
