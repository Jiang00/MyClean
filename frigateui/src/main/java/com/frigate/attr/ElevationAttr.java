package com.frigate.attr;

import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by zhy on 15/12/24.
 */
public class ElevationAttr extends AutoAttr {
    public ElevationAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return Attrs.ELEVATION;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return false;
    }

    @Override
    protected void execute(View view, int val) {
        try {
            Method setMaxWidthMethod = view.getClass().getMethod("setElevation", float.class);
            setMaxWidthMethod.invoke(view, val);
        } catch (Exception ignore) {
        }
    }

    public static ElevationAttr generate(int val, int baseFlag) {
        ElevationAttr attr = null;
        switch (baseFlag) {
            case AutoAttr.BASE_WIDTH:
                attr = new ElevationAttr(val, Attrs.ELEVATION, 0);
                break;
            case AutoAttr.BASE_HEIGHT:
                attr = new ElevationAttr(val, 0, Attrs.ELEVATION);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new ElevationAttr(val, 0, 0);
                break;
        }
        return attr;
    }

    public static int getElevation(View view) {
        try {
            Method setMaxWidthMethod = view.getClass().getMethod("getElevation");
            return (int) setMaxWidthMethod.invoke(view);
        } catch (Exception ignore) {
        }
        return 0;
    }
}
