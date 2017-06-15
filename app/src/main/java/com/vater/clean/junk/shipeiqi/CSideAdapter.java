package com.vater.clean.junk.shipeiqi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vater.clean.junk.fuwu.XuanfuService;
import com.vater.clean.util.PreData;
import com.vater.clean.util.Util;
import com.vater.module.charge.saver.Util.Constants;
import com.vater.module.charge.saver.Util.Utils;
import com.vater.clean.junk.R;
import com.vater.clean.junk.jiemian.FileManagerActivity;
import com.vater.clean.junk.jiemian.GameActivity;
import com.vater.clean.junk.jiemian.LogActivity;
import com.vater.clean.junk.jiemian.AllAppActivity;
import com.vater.clean.junk.jiemian.NotifiActivity;
import com.vater.clean.junk.jiemian.NotifiAnimaActivity;
import com.vater.clean.junk.jiemian.SimerActivity;
import com.vater.clean.junk.jiemian.DeepActivity;
import com.vater.clean.junk.jiemian.NeicunAvtivity;
import com.vater.clean.junk.jiemian.SettingActivity;
import com.vater.clean.junk.shiti.SideInfo;
import com.vater.clean.junk.gongju.AdUtil;
import com.vater.clean.junk.gongju.Constant;
import com.vater.clean.junk.gongju.MUtilGp;


public class CSideAdapter extends MybaseAdapter<SideInfo> {
    private static int idx = 0;

    private static final int JUNK = idx++;
    private static final int RAM = idx++;
    private static final int MANAGER = idx++;
    private static final int FILE = idx++;
    private static final int POWER = idx++;
    //    private static final int PRIVARY = idx++;
    private static final int NOTIFI = idx++;
    private static final int PICTURE = idx++;

    private static final int BATTERY = idx++;
    private static final int FLOAT = idx++;
    private static final int GBOOST = idx++;
    private static final int SETTING = idx++;
    private static final int ROTATE = idx++;

    public CSideAdapter(Context context) {

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
//        convertView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        holder.iv_le.setColorFilter(ContextCompat.getColor(context, R.color.A1));
//                        holder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.A1));
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        holder.iv_le.setColorFilter(0);
//                        holder.tv_name.setTextColor(ContextCompat.getColor(context, R.color.B2));
//                        break;
//                }
//                return false;
//            }
//        });
        if (position == BATTERY || position == FLOAT) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (position == NOTIFI) {
                holder.rl_item.setVisibility(View.GONE);
                AbsListView.LayoutParams param = new AbsListView.LayoutParams(0, 1);
                convertView.setLayoutParams(param);
            } else {
                holder.rl_item.setVisibility(View.VISIBLE);
                AbsListView.LayoutParams param = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
                convertView.setLayoutParams(param);
            }
        }
        if (position == SETTING) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void onC(int position) {
        if (position == BATTERY) {
            if ((boolean) Utils.readData(context, Constants.CHARGE_SAVER_SWITCH, true)) {
                AdUtil.track("侧边栏", "点击关闭充电屏保", "", 1);
                Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, false);
            } else {
                AdUtil.track("侧边栏", "点击开启充电屏保", "", 1);
                PreData.putDB(context, Constant.FIRST_BATTERY, false);
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
            Intent intent2 = new Intent(context, LogActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, NeicunAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == MANAGER) {
            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, AllAppActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//            ((Activity) context).startActivityForResult(intent, 1);
            AdUtil.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, Constant.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, FileManagerActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == POWER) {
            AdUtil.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, DeepActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            AdUtil.track("侧边栏", "点击进入通知栏清理页面", "", 1);
            PreData.putDB(context, Constant.NOTIFI_CLEAN, true);
            if (!Util.isNotificationListenEnabled(context) || !PreData.getDB(context, Constant.KEY_NOTIFI, true)) {
                Intent intent6 = new Intent(context, NotifiAnimaActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            } else {
                Intent intent6 = new Intent(context, NotifiActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            }
        } else if (position == PICTURE) {
            AdUtil.track("侧边栏", "点击进入相似图片", "", 1);
            PreData.putDB(context, Constant.PHOTO_CLEAN, true);
            Intent intent = new Intent(context, SimerActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == GBOOST) {
            AdUtil.track("侧边栏", "点击进入游戏加速", "", 1);
            PreData.putDB(context, Constant.GBOOST_CLEAN, true);
            Intent intent = new Intent(context, GameActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == SETTING) {
            AdUtil.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SettingActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            AdUtil.track("侧边栏", "点击好评", "", 1);
            MUtilGp.rate(context);
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
