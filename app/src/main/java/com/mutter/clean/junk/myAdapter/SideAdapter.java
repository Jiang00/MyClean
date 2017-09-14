package com.mutter.clean.junk.myAdapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mutter.clean.junk.myActivity.Loading1Activity;
import com.mutter.clean.junk.myActivity.LoadingActivity;
import com.mutter.clean.util.PreData;
import com.mutter.clean.util.Util;
import com.mutter.clean.junk.myActivity.SimilarActivity;
import com.mutter.module.charge.saver.Util.Constants;
import com.mutter.module.charge.saver.Util.Utils;
import com.mutter.clean.junk.R;
import com.mutter.clean.junk.myActivity.JiangwenActivity;
import com.mutter.clean.junk.myActivity.FileManaActivity;
import com.mutter.clean.junk.myActivity.GameActivity;
import com.mutter.clean.junk.myActivity.CleanActivity;
import com.mutter.clean.junk.myActivity.UserAppActivity;
import com.mutter.clean.junk.myActivity.PhoneActivity;
import com.mutter.clean.junk.myActivity.NotifiActivity;
import com.mutter.clean.junk.myActivity.NotifiAnimationActivity;
import com.mutter.clean.junk.myActivity.PowerActivity;
import com.mutter.clean.junk.myActivity.RamAvtivity;
import com.mutter.clean.junk.myActivity.SettingActivity;
import com.mutter.clean.junk.entity.SideInfo;
import com.mutter.clean.junk.service.FloatService;
import com.mutter.clean.junk.util.AdUtil;
import com.mutter.clean.junk.util.Constant;
import com.mutter.clean.junk.util.UtilGp;


public class SideAdapter extends MybaseAdapter<SideInfo> {

    private int BATTERY = -1;
    private int FLOAT = -1;
    private int JUNK = -1;
    private int RAM = -1;
    private int POWER = -1;
    private int NOTIFI = -1;
    private int PICTURE = -1;
    private int FILE = -1;
    private int BATTERY_COOLING = -1;
    private int MANAGER = -1;
    private int GBOOST = -1;
    private int MESSAGE = -1;
    private int SETTING = -1;
    private int ROTATE = -1;

    public SideAdapter(Context context) {
        super(context);
        int idx = 0;

        BATTERY = idx++;
        FLOAT = idx++;
        JUNK = idx++;
        RAM = idx++;
        if (PreData.getDB(context, Constant.DEEP_KAIGUAN, 1) != 0) {
            POWER = idx++;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, Constant.NOTIFI_KAIGUAN, 1) != 0) {
            NOTIFI = idx++;
        }
        if (PreData.getDB(context, Constant.PICTURE_KAIGUAN, 1) != 0) {
            PICTURE = idx++;
        }
        if (PreData.getDB(context, Constant.FILE_KAIGUAN, 1) != 0) {
            FILE = idx++;
        }
        BATTERY_COOLING = idx++;
        MANAGER = idx++;
        if (PreData.getDB(context, Constant.GBOOST_KAIGUAN, 1) != 0) {
            GBOOST = idx++;
        }
        MESSAGE = idx++;
        SETTING = idx++;
        ROTATE = idx++;
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
            holder.iv_hong = (ImageView) convertView
                    .findViewById(R.id.iv_hong);
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

        if (position == BATTERY || position == FLOAT) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.iv_hong.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        if (position == JUNK || position == FILE || position == MESSAGE) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
        }
        hongV(holder, position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isCheck = !info.isCheck;
                if (info.isCheck) {
                    holder.checkBox.setImageResource(R.mipmap.side_check_passed);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.side_check_normal);
                }
                holder.iv_hong.setVisibility(View.GONE);
                onC(position);
            }
        });


        return convertView;
    }

    private void hongV(ViewHolder holder, int position) {
        if (position == JUNK) {
            if (PreData.getDB(context, Constant.HONG_JUNK, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == RAM) {
            if (PreData.getDB(context, Constant.HONG_RAM, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == BATTERY_COOLING) {
            if (PreData.getDB(context, Constant.HONG_COOLING, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == MANAGER) {
            if (PreData.getDB(context, Constant.HONG_MANAGER, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == FILE) {
            if (PreData.getDB(context, Constant.HONG_FILE, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == POWER) {
            if (PreData.getDB(context, Constant.HONG_DEEP, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == NOTIFI) {
            if (PreData.getDB(context, Constant.HONG_NOTIFI, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == PICTURE) {
            if (PreData.getDB(context, Constant.HONG_PHOTO, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == GBOOST) {
            if (PreData.getDB(context, Constant.HONG_GBOOST, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
        if (position == MESSAGE) {
            if (PreData.getDB(context, Constant.HONG_MESSAGE, true)) {
                holder.iv_hong.setVisibility(View.VISIBLE);
            } else {
                holder.iv_hong.setVisibility(View.GONE);
            }
        }
    }

    private void guan(int position, int item, ViewHolder holder, View convertView) {
        if (position == item) {
//            holder.rl_item.setVisibility(View.GONE);
            convertView.setVisibility(View.GONE);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(1, 1);
            convertView.setLayoutParams(param);
        } else {
            convertView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(param);
        }
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
            PreData.putDB(context, Constant.HONG_JUNK, false);
            Intent intent2 = new Intent(context, CleanActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            PreData.putDB(context, Constant.HONG_RAM, false);
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, RamAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == BATTERY_COOLING) {
            PreData.putDB(context, Constant.HONG_COOLING, false);
            AdUtil.track("侧边栏", "点击进入电池降温", "", 1);
            Intent intent = new Intent(context, JiangwenActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == MANAGER) {
            PreData.putDB(context, Constant.HONG_MANAGER, false);
            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, UserAppActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
            PreData.putDB(context, Constant.HONG_FILE, false);
            AdUtil.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, Constant.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, FileManaActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == POWER) {
            PreData.putDB(context, Constant.HONG_DEEP, false);
            PreData.putDB(context, Constant.DEEP_CLEAN, true);
            AdUtil.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, PowerActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            PreData.putDB(context, Constant.HONG_NOTIFI, false);
            AdUtil.track("侧边栏", "点击进入通知栏清理页面", "", 1);
            PreData.putDB(context, Constant.NOTIFI_CLEAN, true);
            if (!Util.isNotificationListenEnabled(context) || !PreData.getDB(context, Constant.KEY_NOTIFI, true)) {
                Intent intent6 = new Intent(context, NotifiAnimationActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            } else {
                Intent intent6 = new Intent(context, NotifiActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            }
        } else if (position == PICTURE) {
            PreData.putDB(context, Constant.HONG_PHOTO, false);
            AdUtil.track("侧边栏", "点击进入相似图片", "", 1);
            PreData.putDB(context, Constant.PHOTO_CLEAN, true);
            Intent intent = new Intent(context, SimilarActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == GBOOST) {
            PreData.putDB(context, Constant.HONG_GBOOST, false);
            AdUtil.track("侧边栏", "点击进入游戏加速", "", 1);
            PreData.putDB(context, Constant.GBOOST_CLEAN, true);
            Intent intent = new Intent(context, GameActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == MESSAGE) {
            PreData.putDB(context, Constant.HONG_MESSAGE, false);
            AdUtil.track("侧边栏", "点击进入硬件信息", "", 1);
            Intent intent = new Intent(context, PhoneActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == SETTING) {
            AdUtil.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SettingActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
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
        ImageView iv_hong;
        ImageView iv_le;
        TextView tv_name;
        View side_divide;
    }

}
