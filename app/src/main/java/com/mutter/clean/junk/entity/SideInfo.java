package com.mutter.clean.junk.entity;

/**
 */

public class SideInfo {
    public int textId;
    public boolean isCheck;
    public int drawableId;

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
