package com.supers.clean.junk.gboost;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.supers.clean.junk.BuildConfig;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.GBoostActivity;
import com.supers.clean.junk.activity.MyApplication;
import com.supers.clean.junk.entity.JunkInfo;
import com.supers.clean.junk.util.CommonUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.PreData;
import com.supers.clean.junk.util.ShortCutUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by renqingyou on 2017/5/8.
 */

public class GameBooster {


    public static ArrayList<JunkInfo> getInstalledGameList(Activity context) {
        ArrayList<String> gboost_names = PreData.getNameList(context, Constant.GBOOST_LIST);
        ArrayList<JunkInfo> list = new ArrayList<>();
        Log.e("Gboost1", System.currentTimeMillis() + "");
        try {
            if (PreData.getDB(context, Constant.GBOOST_LUN, true)) {
                PreData.putDB(context, Constant.GBOOST_LUN, false);
                String data = readFileFromAssets(context, "raw/gboost.json");
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String pkg = (String) jsonArray.get(i);
                    if (CommonUtil.isPkgInstalled(pkg, context.getPackageManager())) {
                        if (!gboost_names.contains(pkg)) {
                            PreData.addName(context, pkg, Constant.GBOOST_LIST);
                        }
                        JunkInfo info = new JunkInfo(CommonUtil.getAppIcon(pkg, context.getPackageManager()),
                                CommonUtil.getAppLabel(pkg, context.getPackageManager()), pkg);
                        list.add(info);
                    }
                }
                Intent shortcutIntent = new Intent();
                shortcutIntent.setAction(Intent.ACTION_VIEW);
                shortcutIntent.setComponent(new ComponentName(context.getPackageName(),
                        "com.supers.clean.junk.activity.GBoostActivity"));
                String title = context.getString(R.string.gboost_0);
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.short_7);
                    ShortCutUtils.addShortcut(context, shortcutIntent, title, false, bitmap);
                } else {
                    if (list.size() != 0 && !PreData.getDB(context, Constant.GBOOST_SI, false)) {
                        View shortcut_view = View.inflate(context, R.layout.layout_gboost_short, null);
                        if (list.size() > 0) {
                            ImageView iv_1 = (ImageView) shortcut_view.findViewById(R.id.iv_1);
                            iv_1.setImageDrawable(list.get(0).icon);
                        }
                        if (list.size() > 1) {
                            ImageView iv_2 = (ImageView) shortcut_view.findViewById(R.id.iv_2);
                            iv_2.setImageDrawable(list.get(1).icon);
                        }
                        if (list.size() > 2) {
                            ImageView iv_3 = (ImageView) shortcut_view.findViewById(R.id.iv_3);
                            iv_3.setImageDrawable(list.get(2).icon);
                        }
                        if (list.size() > 3) {
                            ImageView iv_4 = (ImageView) shortcut_view.findViewById(R.id.iv_4);
                            iv_4.setImageDrawable(list.get(3).icon);
                            PreData.putDB(context, Constant.GBOOST_SI, true);
                        }

                        Bitmap bitmap = CommonUtil.getViewBitmap(shortcut_view);
                        if (bitmap != null) {
                            ShortCutUtils.removeShortcut(context, shortcutIntent, title);
                            ShortCutUtils.addShortcut(context, shortcutIntent, title, false, bitmap);

                        }
                    }
                }

            }
            Log.e("Gboost2", System.currentTimeMillis() + "");
            for (String pkg : gboost_names) {
                if (CommonUtil.isPkgInstalled(pkg, context.getPackageManager())) {
                    JunkInfo info = new JunkInfo(CommonUtil.getAppIcon(pkg, context.getPackageManager()),
                            CommonUtil.getAppLabel(pkg, context.getPackageManager()), pkg);
                    list.add(info);
                }
            }
            Log.e("Gboost3", System.currentTimeMillis() + "");
        } catch (Exception e) {
        }


        return list;
    }

    public static String readFileFromAssets(Context context, String fileName) throws IOException, IllegalArgumentException {
        if (null == context || TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("bad arguments!");
        }
        AssetManager assetManager = context.getAssets();
        InputStream input = assetManager.open(fileName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
        output.close();
        input.close();
        return output.toString();
    }

}
