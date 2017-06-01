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
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.clean.util.PreData;
import com.android.clean.util.Util;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.supers.clean.junk.R;
import com.supers.clean.junk.mactivity.FileActivity;
import com.supers.clean.junk.mactivity.GBoostActivity;
import com.supers.clean.junk.mactivity.LajiActivity;
import com.supers.clean.junk.mactivity.AppActivity;
import com.supers.clean.junk.mactivity.NotifiActivity;
import com.supers.clean.junk.mactivity.NotifiAnimaActivity;
import com.supers.clean.junk.mactivity.PhotoActivity;
import com.supers.clean.junk.mactivity.DeepActivity;
import com.supers.clean.junk.mactivity.RamAvtivity;
import com.supers.clean.junk.mactivity.SettingActivity;
import com.supers.clean.junk.entity.SideInfo;
import com.supers.clean.junk.service.XuanfuService;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.Constant;
import com.supers.clean.junk.util.UtilGp;


public class MSideAdapter extends MybaseAdapter<SideInfo> {
    private static int idx = 0;
    private static final int BATTERY = idx++;
    private static final int FLOAT = idx++;
    private static final int JUNK = idx++;
    private static final int RAM = idx++;
    private static final int MANAGER = idx++;
    private static final int FILE = idx++;
    private static final int POWER = idx++;
    //    private static final int PRIVARY = idx++;
    private static final int NOTIFI = idx++;
    private static final int PICTURE = idx++;
    private static final int GBOOST = idx++;
    //    private static final int FAMILY = idx++;
//    private static final int THEME = idx++;
    private static final int SETTING = idx++;
    private static final int ROTATE = idx++;

    public MSideAdapter(Context context) {

        super(context);
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
            holder.iv_red = (ImageView) convertView
                    .findViewById(R.id.iv_red);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
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
                onC(position, holder);
            }
        });
        if (position == BATTERY || position == FLOAT) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        if (position == ROTATE) {
            if (!PreData.getDB(context, Constant.SIDE_ROTATE, false)) {
                holder.iv_red.setVisibility(View.VISIBLE);
            }
        } else if (position == POWER) {
            if (!PreData.getDB(context, Constant.SIDE_DEEP, false)) {
                holder.iv_red.setVisibility(View.VISIBLE);
            }
        } else if (position == NOTIFI) {
            if (!PreData.getDB(context, Constant.SIDE_NOTIFI, false)) {
                holder.iv_red.setVisibility(View.VISIBLE);
            }
        } else {
            holder.iv_red.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (position == NOTIFI) {
                holder.rl_item.setVisibility(View.GONE);
                AbsListView.LayoutParams param = new AbsListView.LayoutParams(0, 1);
                convertView.setLayoutParams(param);
                PreData.putDB(context, Constant.SIDE_NOTIFI, true);
            } else {
                holder.rl_item.setVisibility(View.VISIBLE);
                AbsListView.LayoutParams param = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
                convertView.setLayoutParams(param);
            }
        }
        return convertView;
    }

    private void onC(int position, ViewHolder holder) {
        if (position == BATTERY) {
            if ((boolean) Utils.readData(context, Constants.CHARGE_SAVER_SWITCH, true)) {
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
                Intent intent1 = new Intent(context, XuanfuService.class);
                context.stopService(intent1);
            } else {
                AdUtil.track("侧边栏", "点击开启悬浮窗", "", 1);
                PreData.putDB(context, Constant.FlOAT_SWITCH, true);
                Intent intent1 = new Intent(context, XuanfuService.class);
                context.startService(intent1);
            }
        } else if (position == JUNK) {
            AdUtil.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, LajiActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, RamAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == MANAGER) {
            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, AppActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
            AdUtil.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, Constant.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, FileActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == POWER) {
            if (!PreData.getDB(context, Constant.SIDE_DEEP, false)) {
                PreData.putDB(context, Constant.SIDE_DEEP, true);
                holder.iv_red.setVisibility(View.GONE);
            }
            PreData.putDB(context, Constant.DEEP_CLEAN, true);
            AdUtil.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, DeepActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            if (!PreData.getDB(context, Constant.SIDE_NOTIFI, false)) {
                PreData.putDB(context, Constant.SIDE_NOTIFI, true);
                holder.iv_red.setVisibility(View.GONE);
            }
            AdUtil.track("侧边栏", "点击进入通知栏清理页面", "", 1);
            PreData.putDB(context, Constant.NOTIFI_CLEAN, true);
            if (!Util.isNotificationListenEnabled(context)) {
                ((Activity) context).startActivityForResult(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS), 100);
            } else if (!PreData.getDB(context, Constant.KEY_NOTIFI, true)) {
                Intent intent6 = new Intent(context, NotifiAnimaActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            } else {
                Intent intent6 = new Intent(context, NotifiActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            }
        } else if (position == PICTURE) {
            AdUtil.track("侧边栏", "点击进入相似图片", "", 1);
            PreData.putDB(context, Constant.PHOTO_CLEAN, true);
            Intent intent = new Intent(context, PhotoActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == GBOOST) {
            AdUtil.track("侧边栏", "点击进入游戏加速", "", 1);
            PreData.putDB(context, Constant.GBOOST_CLEAN, true);
            Intent intent = new Intent(context, GBoostActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == SETTING) {
            AdUtil.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SettingActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            if (!PreData.getDB(context, Constant.SIDE_ROTATE, false)) {
                PreData.putDB(context, Constant.SIDE_ROTATE, true);
                holder.iv_red.setVisibility(View.GONE);
            }
            AdUtil.track("侧边栏", "点击好评", "", 1);
            UtilGp.rate(context);
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
        ImageView iv_red;
        TextView tv_name;
    }

}
