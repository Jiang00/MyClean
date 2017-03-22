package com.eos.manager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.eos.manager.lib.Utils;
import com.eos.module.charge.saver.lottie.LottieAnimationView;
import com.privacy.lock.R;
import com.eos.manager.meta.SecurityMyPref;
import com.eos.manager.page.ShowDialogview;
import com.eos.manager.page.showDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by superjoy on 2014/9/4.
 */
public class AppLockSettings extends ClientActivitySecurity {
    public static byte idx = 0;
    public static byte SETTING_SLOT;
    public static byte SETTING_MODE;
    public static byte SETTING_HIDE_GRAPH_PATH;
    public static byte SETTING_LOCK_NEW;
    public static byte SETTING_SETTING_ADVANCE;
    public static byte SETTING_RATE;

    int[] items;
    ListView lv;
    LottieAnimationView lot_applock_setting;

    TextView normalTitle;


    Toolbar toolbar;
    private String tuiguang = "com.eosmobi.applock";
    private String tuiguang1 = "com.eosmobi.flashlight.free";


    @Override
    protected boolean hasHelp() {
        return false;
    }

    @Override
    public void setupView() {
        setContentView(R.layout.security_settings);

        normalTitle = (TextView) this.findViewById(R.id.normal_title_name);
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        lot_applock_setting = (LottieAnimationView) this.findViewById(R.id.lot_applock_setting);
        setupToolbar();


        SETTING_SLOT = 0;
        SETTING_MODE = 1;
        SETTING_HIDE_GRAPH_PATH = 2;
        SETTING_LOCK_NEW = 3;
        SETTING_SETTING_ADVANCE = 4;
        SETTING_RATE = 5;
        items = new int[]{
                R.string.security_over_short,
                R.string.security_reset_password,
                R.string.security_hide_path,
                R.string.security_newapp_lock,
                R.string.security_settings_preference,
                R.string.security_help_share
        };

        setup(R.string.security_tab_setting);
        normalTitle.setText("   " + getResources().getString(R.string.security_tab_setting));
        normalTitle.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.security_back), null, null, null);

        normalTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setViewVisible(View.GONE, R.id.search_button, R.id.bottom_action_bar, R.id.progressBar);
        findViewById(R.id.abs_list).setVisibility(View.VISIBLE);

        tuiGuang();

        lv = (ListView) findViewById(R.id.abs_list);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return items.length;
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                if (i == SETTING_MODE) {
                    view = getLayoutInflater().inflate(R.layout.security_invade_line, viewGroup, false);

                    TextView title = (TextView) view.findViewById(R.id.security_title_bar_te);
                    TextView desc = (TextView) view.findViewById(R.id.security_text_des);
                    title.setText(R.string.security_reset_passwd_2_btn);
                    View leftIcon = view.findViewById(R.id.left_icon);
                    leftIcon.setBackgroundResource(R.drawable.security_left_restpassword);

                    desc.setText(SecurityMyPref.isUseNormalPasswd() ? R.string.security_password_lock : R.string.security_use_pattern);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            setPasswd(true, !SharPre.isUseNormalPasswd());
                            showDialog.showResetPasswordDialog(v.getContext());
                            Tracker.sendEvent(Tracker.ACT_SETTING_MENU, Tracker.ACT_SETTING_RESETPAS, Tracker.ACT_SETTING_RESETPAS, 1L);
                        }
                    });
                } else if (i == SETTING_SLOT) {
                    view = LayoutInflater.from(AppLockSettings.this).inflate(R.layout.security_invade_line, null, false);
                    LinearLayout it = (LinearLayout) view.findViewById(R.id.security_linera);
                    View leftIcon = view.findViewById(R.id.left_icon);
                    leftIcon.setBackgroundResource(R.drawable.security_left_settiting);
                    int slot = App.getSharedPreferences().getInt(SecurityMyPref.PREF_BRIEF_SLOT, SecurityMyPref.PREF_DEFAULT);
                    ((TextView) it.findViewById(R.id.security_text_des)).setText(getResources().getStringArray(R.array.brief_slot)[slot]);
                    ((TextView) it.findViewById(R.id.security_title_bar_te)).setText(items[i]);
                    it.setOnClickListener(onClickListener);
                    it.setId(i);
                } else if (i == SETTING_LOCK_NEW) {
                    view = LayoutInflater.from(AppLockSettings.this).inflate(R.layout.security_notica_it, null, false);
                    View left_Icon = view.findViewById(R.id.left_icon);
                    left_Icon.setBackgroundResource(R.drawable.security_left_locknew);
                    ((TextView) view.findViewById(R.id.security_title_bar_te)).setText(items[i]);
                    ((TextView) view.findViewById(R.id.security_text_des)).setVisibility(View.GONE);
                    LinearLayout layout = (LinearLayout) view.findViewById(R.id.lin_layout);

                    final ImageView checkbox = (ImageView) view.findViewById(R.id.security_set_checked);
                    if (App.getSharedPreferences().getBoolean(SecurityMyPref.LOCK_NEW, SecurityMyPref.LOCK_DEFAULT)) {
                        checkbox.setImageResource(R.drawable.security_setting_check);
                    } else {
                        checkbox.setImageResource(R.drawable.security_setting_not_check);
                    }
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (App.getSharedPreferences().getBoolean(SecurityMyPref.LOCK_NEW, SecurityMyPref.LOCK_DEFAULT)) {
                                checkbox.setImageResource(R.drawable.security_setting_not_check);
//                                MyTrack.sendEvent(MyTrack.CATE_SETTING, MyTrack.ACT_NEW_APP, MyTrack.ACT_NEW_APP, 1L);
                                App.getSharedPreferences().edit().putBoolean(SecurityMyPref.LOCK_NEW, false).apply();

                            } else {
                                checkbox.setImageResource(R.drawable.security_setting_check);
//                                MyTrack.sendEvent(MyTrack.CATE_SETTING, MyTrack.ACT_NEW_APP, MyTrack.ACT_NEW_APP, 1L);
                                App.getSharedPreferences().edit().putBoolean(SecurityMyPref.LOCK_NEW, true).apply();
                            }

                            Tracker.sendEvent(Tracker.ACT_SETTING_MENU, Tracker.ACT_SETTING_LOCK_NEW, Tracker.ACT_SETTING_LOCK_NEW, 1L);


                        }
                    });


                } else if (i == SETTING_SETTING_ADVANCE) {
                    view = LayoutInflater.from(AppLockSettings.this).inflate(R.layout.security_new_it, null, false);
                    view.findViewById(R.id.left_icon).setBackgroundResource(R.drawable.security_left_permission);


                    TextView it = (TextView) view.findViewById(R.id.security_abuout_bt);
                    it.setText(items[i]);
                    it.setOnClickListener(onClickListener);
                    it.setId(i);
                } else if (i == SETTING_RATE) {
                    view = LayoutInflater.from(AppLockSettings.this).inflate(R.layout.security_new_it, null, false);
                    view.findViewById(R.id.left_icon).setBackgroundResource(R.drawable.security_left_rate);
                    TextView it = (TextView) view.findViewById(R.id.security_abuout_bt);
                    it.setText(items[i]);
                    it.setOnClickListener(onClickListener);
                    it.setId(i);


                } else if (i == SETTING_HIDE_GRAPH_PATH) {
                    view = LayoutInflater.from(AppLockSettings.this).inflate(R.layout.security_notica_it, null, false);
                    View left_Icon = view.findViewById(R.id.left_icon);
                    LinearLayout layout = (LinearLayout) view.findViewById(R.id.lin_layout);

                    left_Icon.setBackgroundResource(R.drawable.security_left_hidepath);
                    ((TextView) view.findViewById(R.id.security_title_bar_te)).setText(items[i]);
                    ((TextView) view.findViewById(R.id.security_text_des)).setVisibility(View.GONE);
                    final ImageView b = (ImageView) view.findViewById(R.id.security_set_checked);
                    if (App.getSharedPreferences().getBoolean("hide_path", false)) {
                        b.setImageResource(R.drawable.security_setting_check);
                    } else {
                        b.setImageResource(R.drawable.security_setting_not_check);
                    }
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (App.getSharedPreferences().getBoolean("hide_path", false)) {
//                                MyTrack.sendEvent(MyTrack.CATE_SETTING, MyTrack.ACT_HIDE_PATH, MyTrack.ACT_HIDE_PATH, 1L);
                                App.getSharedPreferences().edit().putBoolean("hide_path", false).apply();
                                b.setImageResource(R.drawable.security_setting_not_check);


                            } else {
//                                MyTrack.sendEvent(MyTrack.CATE_SETTING, MyTrack.ACT_HIDE_PATH, MyTrack.ACT_HIDE_PATH, 1L);
                                App.getSharedPreferences().edit().putBoolean("hide_path", true).apply();
                                b.setImageResource(R.drawable.security_setting_check);

                            }

                            Tracker.sendEvent(Tracker.ACT_SETTING_MENU, Tracker.ACT_SETTING_HIDEPATH, Tracker.ACT_SETTING_HIDEPATH, 1L);

                        }
                    });


                }
                return view;
            }
        });


        int value = this.checkCallingOrSelfPermission("com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY");

        Log.e("permissionvalue", value + "");
    }

    public void tuiGuang() {
        String extraData = AndroidSdk.getExtraData();
        try {
            JSONObject json = new JSONObject(extraData);
            if (json.has("tuiguang")) {
                JSONArray array = json.getJSONArray("tuiguang");
                tuiguang = array.getString(0);
                tuiguang1 = array.getString(1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!isPkgInstalled(tuiguang, getPackageManager())) {
            lot_applock_setting.setImageAssetsFolder("images/applocks/");
            lot_applock_setting.setAnimation("applocks.json");
            lot_applock_setting.loop(true);
            lot_applock_setting.playAnimation();

        } else if (!isPkgInstalled(tuiguang1, getPackageManager())) {
            lot_applock_setting.setImageAssetsFolder("images/flashs/");
            lot_applock_setting.setAnimation("flashs.json");
            lot_applock_setting.loop(true);
            lot_applock_setting.playAnimation();

        } else {
            lot_applock_setting.setVisibility(View.GONE);
        }
        lot_applock_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPkgInstalled(tuiguang, getPackageManager())) {
                    AndroidSdk.track("applock设置页面", "推广applock点击", "", 1);
                    Utils.openPlayStore(AppLockSettings.this, tuiguang);
                } else if (!isPkgInstalled(tuiguang1, getPackageManager())) {
                    AndroidSdk.track("applock设置页面", "推广手电筒点击", "", 1);
                    Utils.openPlayStore(AppLockSettings.this, tuiguang1);
                }
            }
        });
    }

    //是否安装该应用
    public static boolean isPkgInstalled(String pkgName, PackageManager pm) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == SETTING_SLOT) {
                ShowDialogview.showBriefLock(view.getContext(), lv);
            } else if (id == SETTING_RATE) {
                if (!SecurityMyPref.isOptionPressed(SecurityMyPref.OPT_RATE_REDDOT)) {
                    SecurityMyPref.pressOption(SecurityMyPref.OPT_RATE_REDDOT);
                }
                SecurityShare.rate(context);
                notifyDatasetChanged();
                Tracker.sendEvent(Tracker.ACT_SETTING_MENU, Tracker.ACT_GOOD_RATE, Tracker.ACT_GOOD_RATE, 1L);

            } else if (id == SETTING_SETTING_ADVANCE) {
                Intent intent = new Intent(AppLockSettings.this, AppLockSettingsAdvance.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Tracker.sendEvent(Tracker.ACT_SETTING_MENU, Tracker.ACT_SETTING_PREFRENCE, Tracker.ACT_SETTING_PREFRENCE, 1L);
            }
        }
    };


    public void notifyDatasetChanged() {
        if (lv != null) {
            ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
        }
    }


    private void setupToolbar() {
//        toolbar.setNavigationIcon(R.drawable.security_slide_menu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.security_tab_setting);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

        }
        return true;
    }


}