package com.mutter.clean.junk.entity;

/**
 */

public class SideInfo {
    public int textId;
    public boolean isCheck;
    public int drawableId;

    public SideInfo(int drawableId, int textId, boolean isCheck) {
        this.textId = textId;
        this.drawableId = drawableId;
        this.isCheck = isCheck;
    }

    public SideInfo(int drawableId, int textId) {
        this.textId = textId;
        this.drawableId = drawableId;
    }
}
