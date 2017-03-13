package com.security.manager;

import android.view.WindowManager;

import com.android.client.AndroidSdk;
import com.security.manager.meta.SecurityMyPref;
import com.security.manager.page.SecurityThemeFragment;

/**
 * Created by superjoy on 2014/11/5.
 */
public class UnlockApp extends SecurityPatternEosActivity {

    @Override
    public void setupView() {


        //设置无标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            super.setupView();

        int min= (int) (System.currentTimeMillis()/1000/60);

        if(min-(SecurityMyPref.getMainFirstFullCountDown())>5){

            AndroidSdk.showFullAd(SecurityThemeFragment.TAG_UNLOCK_FULL);

        }

        Tracker.sendEvent(Tracker.CATE_ACTION_UNLOCK_PAGE, Tracker.CATE_ACTION_UNLOCK_PAGE_OPEN, Tracker.CATE_ACTION_UNLOCK_PAGE_OPEN, 1L);
    }
}