package com.eos.manager.page;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eos.manager.AppLockPermissionActivity;
import com.privacy.lock.R;
import com.eos.manager.App;
import com.eos.manager.Tracker;
import com.eos.manager.lib.Utils;
import com.eos.manager.meta.SecurityMyPref;


/**
 * Created by wangqi on 16/4/11.
 */
public class ShowDialogview {
    public static final String FIVE_STARED = "five_sta_ed";


    public static void showPermission(final Context c) {
        final View alertDialogView = View.inflate(c, R.layout.security_show_permission, null);
        final AlertDialog d = new AlertDialog.Builder(c, R.style.dialog).create();
        d.setView(alertDialogView);
        d.setCanceledOnTouchOutside(false);
        d.show();
//        Utils.addAlertAttribute(d.getWindow());
        alertDialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.cancel();
                final Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                c.startActivity(intent);
                try {
                    new Thread().sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent transintent = new Intent(c, AppLockPermissionActivity.class);
                c.startActivity(transintent);
                Tracker.sendEvent(Tracker.ACT_PERMISSION, Tracker.ACT_PERMISSION_OK, Tracker.ACT_PERMISSION_OK, 1L);
            }
        });

        alertDialogView.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tracker.sendEvent(Tracker.ACT_PERMISSION, Tracker.ACT_PERMISSION_OK, Tracker.ACT_PERMISSION_CANCLE, 1L);
                d.cancel();
            }
        });
    }


    public static void showNewVersion(final Context context) {

        final View alertDialogView = View.inflate(context, R.layout.security_show_newversion, null);
        final MyDialog d = new MyDialog(context, 0, 0, alertDialogView, R.style.dialog);
        d.show();
        alertDialogView.findViewById(R.id.security_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.rate(context);

            }
        });
    }


    public static void showSaveMode(Context context) {
        try {
            final Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            context.startActivity(intent);

            try {
                new Thread().sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent transintent = new Intent(context, AppLockPermissionActivity.class);
            context.startActivity(transintent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSettingPermission50(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            context.startActivity(intent);
            try {
                new Thread().sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent transintent = new Intent(context, AppLockPermissionActivity.class);
            context.startActivity(transintent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void showBriefLock(Context context, final ListView listview) {
        final View alertDialogView = View.inflate(context, R.layout.security_show_brief_new, null);

        final MyDialog d = new MyDialog(context, 0, 0, alertDialogView, R.style.dialog);
        d.show();

        LinearLayout briOne = (LinearLayout) alertDialogView.findViewById(R.id.bri_one);
        LinearLayout briTWO = (LinearLayout) alertDialogView.findViewById(R.id.bri_two);
        LinearLayout briThree = (LinearLayout) alertDialogView.findViewById(R.id.bri_three);


        final ImageView briOneImg = (ImageView) alertDialogView.findViewById(R.id.image_bri_one);
        final ImageView briTwoImg = (ImageView) alertDialogView.findViewById(R.id.image_bri_two);
        final ImageView briThreeImg = (ImageView) alertDialogView.findViewById(R.id.image_bri_three);


        final TextView briOneText = (TextView) alertDialogView.findViewById(R.id.text_bri_one);
        final TextView briTwoText = (TextView) alertDialogView.findViewById(R.id.text_bri_two);
        final TextView briThreeText = (TextView) alertDialogView.findViewById(R.id.text_bri_three);


        int slot = App.getSharedPreferences().getInt(SecurityMyPref.PREF_BRIEF_SLOT, SecurityMyPref.PREF_DEFAULT);

        if (slot == 0) {
            briOneImg.setImageResource(R.drawable.security_brief_check);
            briOneText.setTextColor(App.getContext().getResources().getColor(R.color.security_theme_primary));


            briTwoImg.setImageResource(R.drawable.security_brief_notcheck);
            briTwoText.setTextColor(App.getContext().getResources().getColor(R.color.security_gray_background));

            briThreeImg.setImageResource(R.drawable.security_brief_notcheck);
            briThreeText.setTextColor(App.getContext().getResources().getColor(R.color.security_gray_background));


        } else if (slot == 1) {

            briTwoImg.setImageResource(R.drawable.security_brief_check);
            briTwoText.setTextColor(App.getContext().getResources().getColor(R.color.security_theme_primary));

            briThreeImg.setImageResource(R.drawable.security_brief_notcheck);
            briThreeText.setTextColor(App.getContext().getResources().getColor(R.color.security_gray_background));


            briOneImg.setImageResource(R.drawable.security_brief_notcheck);
            briOneText.setTextColor(App.getContext().getResources().getColor(R.color.security_gray_background));

        } else if (slot == 2) {

            briThreeImg.setImageResource(R.drawable.security_brief_check);
            briThreeText.setTextColor(App.getContext().getResources().getColor(R.color.security_theme_primary));


            briTwoImg.setImageResource(R.drawable.security_brief_notcheck);
            briTwoText.setTextColor(App.getContext().getResources().getColor(R.color.security_gray_background));


            briOneImg.setImageResource(R.drawable.security_brief_notcheck);
            briOneText.setTextColor(App.getContext().getResources().getColor(R.color.security_gray_background));


        }


        briOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                briOneImg.setImageResource(R.drawable.security_brief_check);
                briOneText.setTextColor(v.getResources().getColor(R.color.security_theme_primary));
                briTwoImg.setImageResource(R.drawable.security_brief_notcheck);
                briTwoText.setTextColor(v.getResources().getColor(R.color.security_gray_background));
                briThreeImg.setImageResource(R.drawable.security_brief_notcheck);
                briThreeText.setTextColor(v.getResources().getColor(R.color.security_gray_background));
                App.getSharedPreferences().edit().putInt(SecurityMyPref.PREF_BRIEF_SLOT, 0).apply();
                ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();


            }
        });


        briTWO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                briTwoImg.setImageResource(R.drawable.security_brief_check);
                briTwoText.setTextColor(v.getResources().getColor(R.color.security_theme_primary));
                briThreeImg.setImageResource(R.drawable.security_brief_notcheck);
                briThreeText.setTextColor(v.getResources().getColor(R.color.security_gray_background));
                briOneImg.setImageResource(R.drawable.security_brief_notcheck);
                briOneText.setTextColor(v.getResources().getColor(R.color.security_gray_background));
                App.getSharedPreferences().edit().putInt(SecurityMyPref.PREF_BRIEF_SLOT, 1).apply();
                ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();

            }
        });

        briThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                briThreeImg.setImageResource(R.drawable.security_brief_check);
                briThreeText.setTextColor(v.getResources().getColor(R.color.security_theme_primary));
                briTwoImg.setImageResource(R.drawable.security_brief_notcheck);
                briTwoText.setTextColor(v.getResources().getColor(R.color.security_gray_background));
                briOneImg.setImageResource(R.drawable.security_brief_notcheck);
                briOneText.setTextColor(v.getResources().getColor(R.color.security_gray_background));
                App.getSharedPreferences().edit().putInt(SecurityMyPref.PREF_BRIEF_SLOT, 2).apply();
                ((BaseAdapter) listview.getAdapter()).notifyDataSetChanged();

            }
        });


    }


}
