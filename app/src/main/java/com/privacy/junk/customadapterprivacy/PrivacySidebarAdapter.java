package com.privacy.junk.customadapterprivacy;

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

import com.privacy.clean.core.CleanManager;
import com.privacy.clean.utilsprivacy.MyUtils;
import com.privacy.clean.utilsprivacy.PreData;
import com.privacy.junk.R;
import com.privacy.junk.activityprivacy.AllAppsActivityPrivacy;
import com.privacy.junk.activityprivacy.GoodGameActivityPrivacy;
import com.privacy.junk.activityprivacy.MyNotifingActivityPrivacy;
import com.privacy.junk.activityprivacy.PrivacyBatteriesActivity;
import com.privacy.junk.activityprivacy.DeepingActivityPrivacy;
import com.privacy.junk.activityprivacy.PrivacyMemoryAvtivity;
import com.privacy.junk.activityprivacy.PrivacyNotifingAnimationActivity;
import com.privacy.junk.activityprivacy.FileManagerActivityPrivacy;
import com.privacy.junk.activityprivacy.PictActivityPrivacy;
import com.privacy.junk.activityprivacy.RubbishActivityPrivacy;
import com.privacy.junk.activityprivacy.SetActivityPrivacy;
import com.privacy.junk.privacymodel.SideInfo;
import com.privacy.junk.privacyservices.PrivacySuspensionBallService;
import com.privacy.junk.toolsprivacy.PrivacyUtilGp;
import com.privacy.junk.toolsprivacy.MyConstantPrivacy;
import com.privacy.junk.toolsprivacy.SetAdUtilPrivacy;
import com.privacy.module.charge.saver.privacyutils.BatteryConstantsPrivacy;
import com.privacy.module.charge.saver.privacyutils.UtilsPrivacy;


public class PrivacySidebarAdapter extends MybaseAdapter<SideInfo> {
    int BATTERY = -1;
    int FLOAT = -1;
    int JUNK = -1;
    int RAM = -1;
    int NOTIFI = -1;
    int POWER = -1;
    int PICTURE = -1;
    int MANAGER = -1;
    int FILE = -1;
    int GBOOST = -1;
    int COOLING = -1;

    int SETTING = -1;
    int ROTATE = -1;

    private String powerSize;

    public PrivacySidebarAdapter(Context context) {
        super(context);
        int idx = 0;
        BATTERY = idx++;
        FLOAT = idx++;
        JUNK = idx++;
        RAM = idx++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, MyConstantPrivacy.NOTIFIACTIVITY, 1) != 0) {
            NOTIFI = idx++;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, MyConstantPrivacy.POWERACTIVITY, 1) != 0) {
            POWER = idx++;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, MyConstantPrivacy.PICTUREX, 1) != 0) {
            PICTURE = idx++;
        }

        MANAGER = idx++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, MyConstantPrivacy.FILEACTIVITY, 1) != 0) {
            FILE = idx++;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && PreData.getDB(context, MyConstantPrivacy.GOODGAME, 1) != 0) {
            GBOOST = idx++;
        }

        COOLING = idx++;
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
        if (position == JUNK) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void onC(int position) {
        if (position == BATTERY) {
            if ((boolean) UtilsPrivacy.readData(context, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true)) {
                SetAdUtilPrivacy.track("侧边栏", "点击关闭充电屏保", "", 1);
                UtilsPrivacy.writeData(context, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, false);
            } else {
                SetAdUtilPrivacy.track("侧边栏", "点击开启充电屏保", "", 1);
                PreData.putDB(context, MyConstantPrivacy.FIRST_BATTERY, false);
                UtilsPrivacy.writeData(context, BatteryConstantsPrivacy.CHARGE_SAVER_SWITCH, true);
            }
        } else if (position == FLOAT) {
            if (PreData.getDB(context, MyConstantPrivacy.FlOAT_SWITCH, true)) {
                SetAdUtilPrivacy.track("侧边栏", "点击关闭悬浮窗", "", 1);
                PreData.putDB(context, MyConstantPrivacy.FlOAT_SWITCH, false);
                Intent intent1 = new Intent(context, PrivacySuspensionBallService.class);
                context.stopService(intent1);
            } else {
                SetAdUtilPrivacy.track("侧边栏", "点击开启悬浮窗", "", 1);
                PreData.putDB(context, MyConstantPrivacy.FlOAT_SWITCH, true);
                Intent intent1 = new Intent(context, PrivacySuspensionBallService.class);
                context.startService(intent1);
            }
        } else if (position == JUNK) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, RubbishActivityPrivacy.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, PrivacyMemoryAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == PICTURE) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入相似图片页面", "", 1);
            Intent intent4 = new Intent(context, PictActivityPrivacy.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, MyConstantPrivacy.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, FileManagerActivityPrivacy.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == POWER) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, DeepingActivityPrivacy.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入通知栏页面", "", 1);
            PreData.putDB(context, MyConstantPrivacy.NOTIFI_CLEAN, true);
            if (!PreData.getDB(context, MyConstantPrivacy.KEY_NOTIFI, true) || !MyUtils.isNotificationListenEnabled(context)) {
                //通知栏动画
                Intent intent6 = new Intent(context, PrivacyNotifingAnimationActivity.class);
                context.startActivity(intent6);
            } else {
                //通知栏
                Intent intent6 = new Intent(context, MyNotifingActivityPrivacy.class);
                context.startActivity(intent6);
            }
        } else if (position == MANAGER) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入应用管理", "", 1);
            Intent intent = new Intent(context, AllAppsActivityPrivacy.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == GBOOST) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入游戏加速", "", 1);
            PreData.putDB(context, MyConstantPrivacy.GBOOST_CLEAN, true);
            Intent intent = new Intent(context, GoodGameActivityPrivacy.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == COOLING) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入电池降温", "", 1);
            Intent intent = new Intent(context, PrivacyBatteriesActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == SETTING) {
            SetAdUtilPrivacy.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SetActivityPrivacy.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            SetAdUtilPrivacy.track("侧边栏", "点击好评", "", 1);
            PrivacyUtilGp.rate(context);
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