package com.security.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by wangqi on 17/3/9.
 */

public class EditTextPreIme extends EditText {


    public EditTextPreIme(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

        }
        return super.dispatchKeyEventPreIme(event);
    }

}