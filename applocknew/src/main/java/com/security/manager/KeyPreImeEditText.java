package com.security.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by wangqi on 17/3/9.
 */

public class KeyPreImeEditText extends EditText {



    public KeyPreImeEditText(Context context) {
        super(context);
    }

    public KeyPreImeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KeyPreImeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SearchView.clearValur();
        }
        return super.onKeyPreIme(keyCode, event);
    }
}