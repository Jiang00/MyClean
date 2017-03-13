package com.security.manager;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.privacy.lock.R;
import com.security.manager.lib.Utils;
import com.security.manager.meta.SecurityMyPref;
import com.security.manager.page.ShowDialogview;


/**
 * Created by superjoy on 2014/9/4.
 */
public class SecuritySettingsAdvance extends AppCompatActivity {
    public static byte idx = 0;
    public static int SETTING_PERMISSION;
    public static byte SETTING_NOTIFICATION;
    public static byte SETTING_POWER_MODE;


    ListView lv;


    Toolbar toolbar;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        {
            setContentView(R.layout.security_settings_advance);
            intent = getIntent();
            final int[] items;
            if (Build.VERSION.SDK_INT >= 21) {

                SETTING_PERMISSION = 0;
                SETTING_POWER_MODE = 1;
                items = new int[]{
                        R.string.security_service_title,
                        R.string.security_power_mode,
                };
            } else {
                items = new int[]{
                        R.string.security_power_mode,
                };
            }
         toolbar= (Toolbar) this.findViewById(R.id.toolbar);

            setupToolbar();

//        setup(R.string.security_settings_preference);
//            setViewVisible(View.GONE, R.id.search_button, R.id.bottom_action_bar, R.id.progressBar);


            lv = (ListView) this.findViewById(R.id.my_abs_list);
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

                    Log.e("ivalue", i + "---");

                    if (SecurityMyPref.getshowLockAll()) {

                        if (i == SETTING_POWER_MODE) {
                            view = LayoutInflater.from(SecuritySettingsAdvance.this).inflate(R.layout.security_notica, null, false);
                            ((TextView) view.findViewById(R.id.security_title_bar_te)).setText(items[i]);
                            ((TextView) view.findViewById(R.id.security_text_des)).setText(R.string.security_power_mode_des);
                            final ImageView checkbox = (ImageView) view.findViewById(R.id.security_set_checked);

                            checkbox.setImageResource(R.drawable.security_ne);
                            view.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowDialogview.showSaveMode(SecuritySettingsAdvance.this);
                                }
                            });


                        } else if (i == SETTING_PERMISSION) {
                            view = LayoutInflater.from(SecuritySettingsAdvance.this).inflate(R.layout.security_notica, null, false);
                            ((TextView) view.findViewById(R.id.security_title_bar_te)).setText(items[i]);
                            ((TextView) view.findViewById(R.id.security_text_des)).setText(R.string.security_service_description);
                            final ImageView checkbox = (ImageView) view.findViewById(R.id.security_set_checked);
                            if (!Utils.requireCheckAccessPermission(SecuritySettingsAdvance.this)) {
                                checkbox.setImageResource(R.drawable.security_setting_check);
                            } else {
                                checkbox.setImageResource(R.drawable.security_setting_not_check);
                            }
                            checkbox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowDialogview.showSettingPermission50(SecuritySettingsAdvance.this);
                                }
                            });
                        }

                    } else {

                        if (i == SETTING_POWER_MODE) {
                            view = LayoutInflater.from(SecuritySettingsAdvance.this).inflate(R.layout.security_notica, null, false);
                            ((TextView) view.findViewById(R.id.security_title_bar_te)).setText(items[i]);
                            ((TextView) view.findViewById(R.id.security_text_des)).setText(R.string.security_power_mode_des);
                            final ImageView checkbox = (ImageView) view.findViewById(R.id.security_set_checked);

                            checkbox.setImageResource(R.drawable.security_ne);
                            view.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowDialogview.showSaveMode(SecuritySettingsAdvance.this);
                                }
                            });


                        } else if (i == SETTING_PERMISSION) {
                            view = LayoutInflater.from(SecuritySettingsAdvance.this).inflate(R.layout.security_notica, null, false);
                            ((TextView) view.findViewById(R.id.security_title_bar_te)).setText(items[i]);
                            ((TextView) view.findViewById(R.id.security_text_des)).setText(R.string.security_service_description);
                            final ImageView checkbox = (ImageView) view.findViewById(R.id.security_set_checked);
                            if (!Utils.requireCheckAccessPermission(SecuritySettingsAdvance.this)) {
                                checkbox.setImageResource(R.drawable.security_setting_check);
                            } else {
                                checkbox.setImageResource(R.drawable.security_setting_not_check);
                            }
                            checkbox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowDialogview.showSettingPermission50(SecuritySettingsAdvance.this);
                                }
                            });
                        }


                    }

                    return view;
                }
            });
        }
    }


    @Override
    protected void onResume() {
//        lv.getAdapter().notify();
        super.onResume();
    }

    private void setupToolbar() {
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
            if (intent.getExtra("launchname") != null) {
                this.finish();
                Intent nIntent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName() + "");
                nIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(nIntent);
            } else {
                this.finish();

            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (intent.getExtra("launchname") != null) {
            this.finish();
            Intent nIntent = new Intent(this, SecurityPatternEosActivity.class);
            nIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(nIntent);
        } else {
            this.finish();

        }
    }
}