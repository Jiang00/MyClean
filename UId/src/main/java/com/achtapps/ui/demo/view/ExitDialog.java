package com.achtapps.ui.demo.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;

public class ExitDialog extends AlertDialog {


    public ExitDialog(Context context, boolean isFull) {
        super(context, android.R.style.Theme);
        setOwnerActivity((Activity)context);
    }

    public ExitDialog (Context context){
        super(context);

    }

    @Override
    public void setContentView(@NonNull View view) {
        super.setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }


}
