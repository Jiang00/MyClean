package com.android.ui.demo.entry;

import android.text.TextUtils;

/**
 * Created by Ivy on 2017/7/20.
 */

public class CrossItem {

    public String pkgName;
    public String action;
    public String headUrl;
    public String iconUrl;
    private String tagIconUrl;
    public String title;
    public String subTitle;
    public String actionTextInstall;
    public String actionTextOpen;
    public String actionLabelUrl;

    public CrossItem() {
    }

    @Override
    public String toString() {
        return "CrossItem{" +
                "pkgName='" + pkgName + '\'' +
                ", action='" + action + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", tagIconUrl='" + getTagIconUrl() + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", actionTextInstall='" + actionTextInstall + '\'' +
                ", actionTextOpen='" + actionTextOpen + '\'' +
                ", actionLabelUrl='" + actionLabelUrl + '\'' +
                '}';
    }

    public void setPkgName(String str) {
        this.pkgName = str;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setAction(String str) {
        this.action = str;
    }

    public String getAction() {
        return this.action;
    }

    public void setHeadUrl(String str) {
        this.headUrl = str;
    }

    public String getHeadUrl() {
        return this.headUrl;
    }

    public void setIconUrl(String str) {
        this.iconUrl = str;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setSubTitle(String str) {
        this.subTitle = str;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setActionTextInstall(String str) {
        this.actionTextInstall = str;
    }

    public String getActionTextInstall() {
        return this.actionTextInstall;
    }

    public void setActionTextOpen(String str) {
        this.actionTextOpen = str;
    }

    public String getActionTextOpen() {
        return this.actionTextOpen;
    }

    public void setActionLabelUrl(String str) {
        this.actionLabelUrl = str;
    }

    public String getActionLabelUrl() {
        return this.actionLabelUrl;
    }

    public String getTagIconUrl() {
        return TextUtils.isEmpty(tagIconUrl) ? iconUrl : tagIconUrl;
    }

    public void setTagIconUrl(String tagIconUrl) {
        this.tagIconUrl = tagIconUrl;
    }
}
