package com.bruder.clean.entity;

/**
 */

public class SideInfo {
    public int textId;
    public int drawableId;
    public boolean isCheck;

    public SideInfo(int textId, int drawableId, boolean isCheck) {
        this.textId = textId;
        this.drawableId = drawableId;
        this.isCheck = isCheck;
    }

    public SideInfo(int textId, int drawableId) {
        this.textId = textId;
        this.drawableId = drawableId;
    }
}
