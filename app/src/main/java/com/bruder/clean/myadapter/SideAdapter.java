package com.bruder.clean.myadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bruder.clean.activity.AppManagerActivity;
import com.bruder.clean.activity.FilesActivity;
import com.bruder.clean.activity.GBoostingActivity;
import com.bruder.clean.activity.GarbageActivity;
import com.bruder.clean.activity.NotifiIfActivity;
import com.bruder.clean.activity.NotifingActivity;
import com.bruder.clean.activity.PhoneRamAvtivity;
import com.bruder.clean.activity.PicturesActivity;
import com.bruder.clean.activity.PoweringActivity;
import com.bruder.clean.entity.SideInfo;
import com.bruder.clean.junk.R;
import com.bruder.clean.myservice.FloatingService;
import com.bruder.clean.util.Constant;
import com.bruder.clean.util.UtilAd;
import com.cleaner.util.DataPre;
import com.cleaner.util.Util;
import com.my.bruder.charge.saver.Util.Constants;
import com.my.bruder.charge.saver.Util.Utils;


public class SideAdapter extends MybaseAdapter<SideInfo> {
    int BATTERY = -1;
    int FLOAT = -1;
    int JUNK = -1;
    int RAM = -1;
    int POWER = -1;
    int PICTURE = -1;
    int GBOOST = -1;
    int NOTIFI = -1;
    int FILE = -1;
    int MANAGER = -1;

    public SideAdapter(Context context) {
        super(context);
        int idx = 0;
        BATTERY = idx++;
        FLOAT = idx++;
        JUNK = idx++;
        RAM = idx++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DataPre.getDB(context, Constant.FILEACTIVITY, 0) != 0) {
            FILE = idx++;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DataPre.getDB(context, Constant.POWERACATIVITY, 0) != 0) {
            POWER = idx++;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DataPre.getDB(context, Constant.NOTIFIACTIVITY, 0) != 0) {
            NOTIFI = idx++;
        }

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

        if (position == 9) {
            holder.side_divide.setVisibility(View.GONE);
        } else {
            holder.side_divide.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void onC(int position) {
        if (position == BATTERY) {
            if ((boolean) Utils.readData(context, Constants.CHARGE_SAVER_SWITCH, false)) {
                UtilAd.track("侧边栏", "点击关闭充电屏保", "", 1);
                Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, false);
            } else {
                UtilAd.track("侧边栏", "点击开启充电屏保", "", 1);
                Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, true);
            }
        } else if (position == FLOAT) {
            if (DataPre.getDB(context, Constant.FlOAT_SWITCH, true)) {
                UtilAd.track("侧边栏", "点击关闭悬浮窗", "", 1);
                DataPre.putDB(context, Constant.FlOAT_SWITCH, false);
                Intent intent1 = new Intent(context, FloatingService.class);
                context.stopService(intent1);
            } else {
                UtilAd.track("侧边栏", "点击开启悬浮窗", "", 1);
                DataPre.putDB(context, Constant.FlOAT_SWITCH, true);
                Intent intent1 = new Intent(context, FloatingService.class);
                context.startService(intent1);
            }
        } else if (position == JUNK) {
            UtilAd.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, GarbageActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            UtilAd.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, PhoneRamAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == POWER) {
            DataPre.putDB(context, Constant.DEEP_CLEAN, true);
            UtilAd.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, PoweringActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == PICTURE) {
            UtilAd.track("侧边栏", "点击进入相似图片", "", 1);
            DataPre.putDB(context, Constant.PHOTO_CLEAN, true);
            Intent intent = new Intent(context, PicturesActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == GBOOST) {
            UtilAd.track("侧边栏", "点击进入游戏加速", "", 1);
            DataPre.putDB(context, Constant.GBOOST_CLEAN, true);
            Intent intent = new Intent(context, GBoostingActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == NOTIFI) {
            UtilAd.track("侧边栏", "点击进入通知栏清理页面", "", 1);
            DataPre.putDB(context, Constant.NOTIFI_CLEAN, true);
            if (!Util.isNotificationListenEnabled(context) || !DataPre.getDB(context, Constant.KEY_NOTIFI, true)) {
                Intent intent6 = new Intent(context, NotifiIfActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            } else {
                Intent intent6 = new Intent(context, NotifingActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            }
        } else if (position == MANAGER) {
            UtilAd.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, AppManagerActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
            UtilAd.track("侧边栏", "点击进入文件管理页面", "", 1);
            Intent intent4 = new Intent(context, FilesActivity.class);
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
