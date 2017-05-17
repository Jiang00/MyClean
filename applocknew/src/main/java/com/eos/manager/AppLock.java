package com.eos.manager;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.android.client.AndroidSdk;
import com.eos.manager.db.backgroundData;
import com.eos.manager.lib.Utils;
import com.eos.manager.lib.io.SafeDB;
import com.eos.manager.meta.SecuritProfiles;
import com.eos.manager.meta.SecurityMyPref;
import com.eos.manager.page.AppFragementSecurity;
import com.eos.manager.page.SecurityMenu;
import com.eos.manager.page.ShowDialogview;
import com.privacy.lock.R;


/**
 * Created by SongHualin on 6/12/2015.
 */
public class AppLock extends ClientActivitySecurity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private AppFragementSecurity fragment;

    private String profileName;

    boolean hide;

    private Toolbar toolbar;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void requirePermission() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Utils.requireCheckAccessPermission(this)) {
                final Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                if (getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
//                    startActivity(intent);
                    ShowDialogview.showPermission(this);

                }
            }

        } else {
            if (!SecurityMyPref.getOpenPermission()) {
                Tracker.sendEvent(Tracker.ACT_PERMISSION, Tracker.ACT_PERMISSION_OPEN, Tracker.ACT_PERMISSION_OPEN, 1L);
                SecurityMyPref.setOpenPermission(true);
            }
        }
    }

    @Override
    protected void onIntent(Intent intent) {
        hide = intent.getBooleanExtra("hide", false);
    }

    @Override
    protected void onRestoreInstanceStateOnCreate(Bundle savedInstanceState) {
        hide = savedInstanceState.getBoolean("hide");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hide", hide);
    }

    @Override
    protected boolean hasHelp() {
        return false;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        if (SecuritProfiles.requireUpdateServerStatus()) {
            try {
                server.notifyApplockUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void setupView() {
        setContentView(R.layout.security_slidemenu_data);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setupToolbar();
        SecurityMenu.currentMenuIt = SecurityMenu.MENU_LOCK_APP;
        setup(R.string.security_lock_app);

        profileName = SafeDB.defaultDB().getString(SecurityMyPref.PREF_ACTIVE_PROFILE, SecurityMyPref.PREF_DEFAULT_LOCK);
        long profileId = SafeDB.defaultDB().getLong(SecurityMyPref.PREF_ACTIVE_PROFILE_ID, 1);

        fragment = (AppFragementSecurity) getFragmentManager().findFragmentByTag("fragment");
        if (fragment == null) {
            fragment = new AppFragementSecurity();
            Bundle args = new Bundle();
            args.putLong(AppFragementSecurity.PROFILE_ID_KEY, profileId);
            args.putString(AppFragementSecurity.PROFILE_NAME_KEY, profileName);
            args.putBoolean(AppFragementSecurity.PROFILE_HIDE, hide);
            fragment.setArguments(args);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "fragment").commit();
        }
        requirePermission();

        //侧边栏广告取消
//        ininShowAD();

        initgetData();


    }

    @Override
    protected void onPause() {
        fragment.saveOrCreateProfile(profileName, server);
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();


    }

//    void ininShowAD() {
//        if (AndroidSdk.hasNativeAd(TAG_TLEF_AD, AndroidSdk.NATIVE_AD_TYPE_ALL)) {
//            View scrollView = AndroidSdk.peekNativeAdViewWithLayout(TAG_TLEF_AD, AndroidSdk.NATIVE_AD_TYPE_ALL, R.layout.app_slide_native_layout, null);
//            if (scrollView != null) {
//                ADView.addView(scrollView);
//            }
//        }
//
//    }

    private void setupToolbar() {
//            toolbar.setNavigationIcon(R.drawable.security_slide_menu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
//        if (TextUtils.equals("loading", getIntent().getStringExtra("from"))) {
//            Intent intent = new Intent("com.eos.clean.main");
//            startActivity(intent);
//        }
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.security_applock_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {
            Intent intent = new Intent(this, AppLockSettings.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initgetData() {
        String data = AndroidSdk.getExtraData();
        if (data != null) {
            backgroundData.onReceiveData(this, data);


        }
    }

    @Override
    public void onBackPressed() {
        boolean value = fragment.forOnback();
        if (!value) {
            super.onBackPressed();
            if (SecurityMyPref.getMainFirstFull()) {
//                AndroidSdk.showFullAd(SecurityThemeFragment.TAG_MAIN_PAGE_FULL);
                SecurityMyPref.setMainFirstFull(false);
            } else {
//                AndroidSdk.showFullAd(SecurityThemeFragment.TAG_MAIN_PAGE_FULL);
            }
//            if (TextUtils.equals("loading", getIntent().getStringExtra("from"))) {
//                Intent intent = new Intent("com.eos.clean.main");
//                startActivity(intent);
//            }
        } else {
            fragment.forOnback();
        }
    }


}
