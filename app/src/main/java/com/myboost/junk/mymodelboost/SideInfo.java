package com.myboost.junk.mymodelboost;

/**
 * Created by on 2017/5/22.
 */

public class SideInfo {
    public boolean isCheck;
    public int textId;
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
