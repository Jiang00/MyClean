package com.myboost.junk.customadapterboost;

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

import com.myboost.clean.core.CleanManager;
import com.myboost.clean.utilsprivacy.MyUtils;
import com.myboost.clean.utilsprivacy.PreData;
import com.myboost.junk.R;
import com.myboost.junk.boostactivity.BoostDeepingActivity;
import com.myboost.junk.boostactivity.BoostNotifingActivity;
import com.myboost.junk.boostactivity.BoostPictActivity;
import com.myboost.junk.boostactivity.BoostFileManagerActivity;
import com.myboost.junk.boostactivity.BoostGoodGameActivity;
import com.myboost.junk.boostactivity.MemoryAvtivityBoost;
import com.myboost.junk.boostactivity.NotifingAnimationActivityBoost;
import com.myboost.junk.boostactivity.BoostRubbishActivity;
import com.myboost.junk.boostactivity.BoostSetActivity;
import com.myboost.junk.mymodelboost.SideInfo;
import com.myboost.junk.servicesboost.BoostSuspensionBallService;
import com.myboost.junk.boosttools.BoostMyConstant;
import com.myboost.junk.boosttools.UtilGpBoost;
import com.myboost.junk.boosttools.SetAdUtilPrivacy;
import com.myboost.module.charge.saver.boostutils.BoostBatteryConstants;
import com.myboost.module.charge.saver.boostutils.BatteryUtils;


public class SidebarAdapterBoost extends MybaseAdapter<SideInfo> {
    int BATTERY = -1;
    int FLOAT = -1;
    int RAM = -1;
    int JUNK = -1;
    int POWER = -1;
    int NOTIFI = -1;
    int PICTURE = -1;
    int FILE = -1;
    int GBOOST = -1;

    int SETTING = -1;
    int ROTATE = -1;

    private String powerSize;

    public SidebarAdapterBoost(Context context) {
        super(context);
        int idx = 0;
        BATTERY = idx++;
        FLOAT = idx++;
        RAM = idx++;
        JUNK = idx++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, BoostMyConstant.POWERACTIVITY, 1) != 0) {
            POWER = idx++;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, BoostMyConstant.NOTIFIACTIVITY, 1) != 0) {
            NOTIFI = idx++;
        }
        PICTURE = idx++;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, BoostMyConstant.FILEACTIVITY, 1) != 0) {
            FILE = idx++;
        }
        GBOOST = idx++;

        SETTING = idx++;
        ROTATE = idx++;

        long junk_size = CleanManager.getInstance(context).getApkSize() + CleanManager.getInstance(context).getCacheSize() +
                CleanManager.getInstance(context).getUnloadSize() + CleanManager.getInstance(context).getLogSize() + CleanManager.getInstance(context).getDataSize();
        powerSize = MyUtils.convertStorage(junk_size, true);
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
        if (position == RAM || position == SETTING) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void onC(int position) {
        if (position == BATTERY) {
            if ((boolean) BatteryUtils.readData(context, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true)) {
                SetAdUtilPrivacy.track("侧边栏", "点击关闭充电屏保", "", 1);
                BatteryUtils.writeData(context, BoostBatteryConstants.CHARGE_SAVER_SWITCH, false);
            } else {
                SetAdUtilPrivacy.track("侧边栏", "点击开启充电屏保", "", 1);
                PreData.putDB(context, BoostMyConstant.FIRST_BATTERY, false);
                BatteryUtils.writeData(context, BoostBatteryConstants.CHARGE_SAVER_SWITCH, true);
            }
        } else if (position == FLOAT) {
            if (PreData.getDB(context, BoostMyConstant.FlOAT_SWITCH, true)) {
                SetAdUtilPrivacy.track("侧边栏", "点击关闭悬浮窗", "", 1);
                PreData.putDB(context, BoostMyConstant.FlOAT_SWITCH, false);
                Intent intent1 = new Intent(context, BoostSuspensionBallService.class);
                context.stopService(intent1);
            } else {
                SetAdUtilPrivacy.track("侧边栏", "点击开启悬浮窗", "", 1);
                PreData.putDB(context, BoostMyConstant.FlOAT_SWITCH, true);
                Intent intent1 = new Intent(context, BoostSuspensionBallService.class);
                context.startService(intent1);
            }
        } else if (position == JUNK) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, BoostRubbishActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, MemoryAvtivityBoost.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == PICTURE) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入相似图片页面", "", 1);
            Intent intent4 = new Intent(context, BoostPictActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, BoostMyConstant.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, BoostFileManagerActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == POWER) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, BoostDeepingActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入通知栏页面", "", 1);
            PreData.putDB(context, BoostMyConstant.NOTIFI_CLEAN, true);
            if (!PreData.getDB(context, BoostMyConstant.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(context)) {
                //通知栏动画
                Intent intent6 = new Intent(context, NotifingAnimationActivityBoost.class);
                context.startActivity(intent6);
            } else {
                //通知栏
                Intent intent6 = new Intent(context, BoostNotifingActivity.class);
                context.startActivity(intent6);
            }
        } else if (position == GBOOST) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入游戏加速", "", 1);
            PreData.putDB(context, BoostMyConstant.GBOOST_CLEAN, true);
            Intent intent = new Intent(context, BoostGoodGameActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == SETTING) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, BoostSetActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            SetAdUtilPrivacy.track("侧边栏", "点击好评", "", 1);
            UtilGpBoost.rate(context);
        }
    }

    private Bitmap getBitmap(Drawable icon) {
        // TODO Auto-generated method stub
        Bitmap bitmap = null;
        bitmap = ((BitmapDrawable) icon).getBitmap();
        return bitmap;
    }

    public class ViewHolder {
        ImageView checkBox;
        TextView tv_name;
        RelativeLayout rl_item;
        View side_divide;
        ImageView iv_le;
    }

}
