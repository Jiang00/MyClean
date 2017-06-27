package com.supers.clean.junk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.my.module.charge.saver.Util.Constants;
import com.my.module.charge.saver.Util.Utils;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.GBoostActivity;
import com.supers.clean.junk.activity.JunkActivity;
import com.supers.clean.junk.activity.ManagerActivity;
import com.supers.clean.junk.activity.NotifiActivity;
import com.supers.clean.junk.activity.NotifiInfoActivity;
import com.supers.clean.junk.activity.PictureActivity;
import com.supers.clean.junk.activity.PowerActivity;
import com.supers.clean.junk.activity.RamAvtivity;
import com.supers.clean.junk.entity.SideInfo;
import com.supers.clean.junk.service.FloatService;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;


public class SideAdapter extends MybaseAdapter<SideInfo> {
    int BATTERY = -1;
    int FLOAT = -1;
    int JUNK = -1;
    int RAM = -1;
    int POWER = -1;
    int PICTURE = -1;
    int GBOOST = -1;
    int NOTIFI = -1;
    int MANAGER = -1;

    public SideAdapter(Context context) {
        super(context);
        int idx = 0;
        BATTERY = idx++;
        FLOAT = idx++;
        JUNK = idx++;
        RAM = idx++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&PreData.getDB(context,Constant.POWERACATIVITY,0)!=0) {
            POWER = idx++;
        }
        PICTURE = idx++;
        GBOOST = idx++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT&&PreData.getDB(context,Constant.NOTIFIACTIVITY,0)!=0) {
            NOTIFI = idx++;
        }
        MANAGER = idx++;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SideInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_side_item, null);
            holder = new ViewHolder();
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.iv_check);
            holder.iv_le = (ImageView) convertView
                    .findViewById(R.id.iv_le);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.side_divide = convertView
                    .findViewById(R.id.side_divide);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(info.textId);
        holder.iv_le.setImageResource(info.drawableId);
        if (info.isCheck) {
            holder.checkBox.setImageResource(R.mipmap.side_check_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.side_check_normal);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isCheck = !info.isCheck;
                if (info.isCheck) {
                    holder.checkBox.setImageResource(R.mipmap.side_check_passed);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.side_check_normal);
                }
                onC(position);
            }
        });
        if (position == BATTERY || position == FLOAT) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        if (position == 8) {
            holder.side_divide.setVisibility(View.GONE);
        } else {
            holder.side_divide.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void onC(int position) {
        if (position == BATTERY) {
            if ((boolean) Utils.readData(context, Constants.CHARGE_SAVER_SWITCH, false)) {
                AdUtil.track("侧边栏", "点击关闭充电屏保", "", 1);
                Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, false);
            } else {
                AdUtil.track("侧边栏", "点击开启充电屏保", "", 1);
                Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, true);
            }
        } else if (position == FLOAT) {
            if (PreData.getDB(context, Constant.FlOAT_SWITCH, true)) {
                AdUtil.track("侧边栏", "点击关闭悬浮窗", "", 1);
                PreData.putDB(context, Constant.FlOAT_SWITCH, false);
                Intent intent1 = new Intent(context, FloatService.class);
                context.stopService(intent1);
            } else {
                AdUtil.track("侧边栏", "点击开启悬浮窗", "", 1);
                PreData.putDB(context, Constant.FlOAT_SWITCH, true);
                Intent intent1 = new Intent(context, FloatService.class);
                context.startService(intent1);
            }
        } else if (position == JUNK) {
            AdUtil.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, JunkActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, RamAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == POWER) {
            PreData.putDB(context, Constant.DEEP_CLEAN, true);
            AdUtil.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, PowerActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == PICTURE) {
            AdUtil.track("侧边栏", "点击进入相似图片", "", 1);
            PreData.putDB(context, Constant.PHOTO_CLEAN, true);
            Intent intent = new Intent(context, PictureActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == GBOOST) {
            AdUtil.track("侧边栏", "点击进入游戏加速", "", 1);
            PreData.putDB(context, Constant.GBOOST_CLEAN, true);
            Intent intent = new Intent(context, GBoostActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == NOTIFI) {
            AdUtil.track("侧边栏", "点击进入通知栏清理页面", "", 1);
            PreData.putDB(context, Constant.NOTIFI_CLEAN, true);
            if (!Util.isNotificationListenEnabled(context)) {
                ((Activity) context).startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 100);
            } else if (!PreData.getDB(context, Constant.KEY_NOTIFI, true)) {
                Intent intent6 = new Intent(context, NotifiInfoActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            } else {
                Intent intent6 = new Intent(context, NotifiActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            }
        } else if (position == MANAGER) {
            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, ManagerActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        }
    }

    private Bitmap getBitmap(Drawable icon) {
        // TODO Auto-generated method stub
        Bitmap bitmap = null;
        bitmap = ((BitmapDrawable) icon).getBitmap();
        return bitmap;
    }

    public class ViewHolder {
        RelativeLayout rl_item;
        ImageView checkBox;
        ImageView iv_le;
        TextView tv_name;
        View side_divide;
    }
}
