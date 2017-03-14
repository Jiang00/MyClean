package com.eos.manager;

import android.content.Intent;

/**
 * Created by superjoy on 2014/8/25.
 */
public class AppLockPatternActivity extends AppLockPatternEosActivity {
    @Override
    protected void onIntent(Intent intent) {
        super.onIntent(intent);
        if (intent.hasExtra("action")) return;
        action = ACTION_UNLOCK_OTHER;
    }

    @Override
    protected void resetThemeBridgeImpl() {

    }

    public void unlockSuccess(boolean unlockMe) {
        startListApp();
    }

    @Override
    public void startListApp() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}