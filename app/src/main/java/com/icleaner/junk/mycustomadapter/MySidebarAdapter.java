package com.icleaner.junk.mycustomadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icleaner.clean.core.CleanManager;
import com.icleaner.clean.utils.MyUtils;
import com.icleaner.clean.utils.PreData;
import com.icleaner.junk.R;
import com.icleaner.junk.icleaneractivity.DeepingActivity;
import com.icleaner.junk.icleaneractivity.GoodGameActivity;
import com.icleaner.junk.icleaneractivity.MemoryAvtivity;
import com.icleaner.junk.icleaneractivity.MyNotifingActivity;
import com.icleaner.junk.icleaneractivity.NotifingAnimationActivity;
import com.icleaner.junk.icleaneractivity.PhoneFileManagerActivity;
import com.icleaner.junk.icleaneractivity.PictActivity;
import com.icleaner.junk.icleaneractivity.RubbishActivity;
import com.icleaner.junk.icleaneractivity.SetActivity;
import com.icleaner.junk.mytools.MUtilGp;
import com.icleaner.junk.mytools.MyConstant;
import com.icleaner.junk.mytools.SetAdUtil;
import com.icleaner.junk.services.SuspensionBallService;
import com.icleaner.junk.shitis.SideInfo;
import com.icleaner.module.charge.saver.Utils.BatteryConstants;
import com.icleaner.module.charge.saver.Utils.Utils;


public class MySidebarAdapter extends MybaseAdapter<SideInfo> {
    private static int idx = 0;

    private static final int BATTERY = idx++;
    private static final int FLOAT = idx++;
    private static final int RAM = idx++;
    private static final int JUNK = idx++;
    private static final int POWER = idx++;
    private static final int NOTIFI = idx++;
    //    private static final int MANAGER = idx++;
    private static final int PICTURE = idx++;
    private static final int FILE = idx++;
    private static final int GBOOST = idx++;
    //    private static final int PRIVARY = idx++;
//    private static final int WHITE = idx++;
    private static final int SETTING = idx++;
    private static final int ROTATE = idx++;
    private String powerSize;

    public MySidebarAdapter(Context context) {
        super(context);
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
            holder.side_deep_h = (TextView) convertView.findViewById(R.id.side_deep_h);
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
//        if (position == POWER) {
//            holder.side_deep_h.setVisibility(View.VISIBLE);
//            holder.side_deep_h.setText(powerSize);
//        } else {
//            holder.side_deep_h.setVisibility(View.INVISIBLE);
//        }
       /* if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (position == NOTIFI) {
                holder.rl_item.setVisibility(View.GONE);
                AbsListView.LayoutParams param = new AbsListView.LayoutParams(0, 1);
                convertView.setLayoutParams(param);
            } else {
                holder.rl_item.setVisibility(View.VISIBLE);
                AbsListView.LayoutParams param = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
                convertView.setLayoutParams(param);
            }
        }*/
        if (position == RAM) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else if (position == SETTING) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void onC(int position) {
        if (position == BATTERY) {
            if ((boolean) Utils.readData(context, BatteryConstants.CHARGE_SAVER_SWITCH, true)) {
                SetAdUtil.track("侧边栏", "点击关闭充电屏保", "", 1);
                Utils.writeData(context, BatteryConstants.CHARGE_SAVER_SWITCH, false);
            } else {
                SetAdUtil.track("侧边栏", "点击开启充电屏保", "", 1);
                PreData.putDB(context, MyConstant.FIRST_BATTERY, false);
                Utils.writeData(context, BatteryConstants.CHARGE_SAVER_SWITCH, true);
            }
        } else if (position == FLOAT) {
            if (PreData.getDB(context, MyConstant.FlOAT_SWITCH, true)) {
                SetAdUtil.track("侧边栏", "点击关闭悬浮窗", "", 1);
                PreData.putDB(context, MyConstant.FlOAT_SWITCH, false);
                Intent intent1 = new Intent(context, SuspensionBallService.class);
                context.stopService(intent1);
            } else {
                SetAdUtil.track("侧边栏", "点击开启悬浮窗", "", 1);
                PreData.putDB(context, MyConstant.FlOAT_SWITCH, true);
                Intent intent1 = new Intent(context, SuspensionBallService.class);
                context.startService(intent1);
            }
        } else if (position == JUNK) {
            SetAdUtil.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, RubbishActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            SetAdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, MemoryAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == PICTURE) {
            SetAdUtil.track("侧边栏", "点击进入相似图片页面", "", 1);
            Intent intent4 = new Intent(context, PictActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//            ((Activity) context).startActivityForResult(intent, 1);
            SetAdUtil.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, MyConstant.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, PhoneFileManagerActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == POWER) {
            SetAdUtil.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, DeepingActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            SetAdUtil.track("侧边栏", "点击进入通知栏页面", "", 1);
            PreData.putDB(context, MyConstant.NOTIFI_CLEAN, true);
            if (PreData.getDB(context, MyConstant.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(context)) {
                //通知栏动画
                Intent intent6 = new Intent(context, NotifingAnimationActivity.class);
                context.startActivity(intent6);
            } else {
                //通知栏
                Intent intent6 = new Intent(context, MyNotifingActivity.class);
                context.startActivity(intent6);
            }
        } else if (position == GBOOST) {
            SetAdUtil.track("侧边栏", "点击进入游戏加速", "", 1);
            PreData.putDB(context, MyConstant.GBOOST_CLEAN, true);
            Intent intent = new Intent(context, GoodGameActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == SETTING) {
            SetAdUtil.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SetActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            SetAdUtil.track("侧边栏", "点击好评", "", 1);
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
        TextView side_deep_h;
        TextView tv_name;
        RelativeLayout rl_item;
        View side_divide;
        ImageView iv_le;
    }

}
