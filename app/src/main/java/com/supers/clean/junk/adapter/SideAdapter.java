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
import com.android.theme.internal.data.Theme;
import com.eos.eshop.ShopMaster;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.fraumobi.call.activity.CallActivity;
import com.sample.lottie.LottieAnimationView;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.FileActivity;
import com.supers.clean.junk.activity.GBoostActivity;
import com.supers.clean.junk.activity.JunkActivity;
import com.supers.clean.junk.activity.LanguageSettingActivity;
import com.supers.clean.junk.activity.ManagerActivity;
import com.supers.clean.junk.activity.NetMonitor;
import com.supers.clean.junk.activity.NotifiActivity;
import com.supers.clean.junk.activity.NotifiInfoActivity;
import com.supers.clean.junk.activity.PictureActivity;
import com.supers.clean.junk.activity.PowerActivity;
import com.supers.clean.junk.activity.RamAvtivity;
import com.supers.clean.junk.activity.SettingActivity;
import com.supers.clean.junk.entity.SideInfo;
import com.supers.clean.junk.service.FloatService;
import com.supers.clean.junk.util.AdUtil;
import com.supers.clean.junk.util.UtilGp;
import com.android.clean.util.Constant;


public class SideAdapter extends MybaseAdapter<SideInfo> {
    private static int idx = 0;
    private static final int BATTERY = idx++;
    private static final int DETECT = idx++;
    private static final int FLOAT = idx++;
    private static final int JUNK = idx++;
    private static final int RAM = idx++;
    private static final int MANAGER = idx++;
    private static final int FILE = idx++;
    private static final int POWER = idx++;
    private static final int CALL = idx++;
    private static final int WIFI = idx++;
    //    private static final int LAG_SETTING = idx++;
    private static final int NOTIFI = idx++;
    private static final int PICTURE = idx++;
    private static final int GBOOST = idx++;
    private static final int FAMILY = idx++;
    private static final int THEME = idx++;
    private static final int SETTING = idx++;
    private static final int ROTATE = idx++;

    public SideAdapter(Context context) {

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
            holder.lot_family = (LottieAnimationView) convertView
                    .findViewById(R.id.lot_family);
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
        if (position == FAMILY) {
            holder.lot_family.setImageAssetsFolder("images/box/");
            holder.lot_family.setAnimation("box.json");
            holder.lot_family.loop(true);
            holder.lot_family.playAnimation();
        } else {
            holder.lot_family.setVisibility(View.GONE);
            holder.iv_le.setImageResource(info.drawableId);
        }
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
        if (position == BATTERY || position == FLOAT || position == DETECT) {
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
        if (position == JUNK || position == FAMILY) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
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
        } else if (position == DETECT) {
            if (PreData.getDB(context, Constant.DETECT_KAIGUAN, true)) {
                AdUtil.track("侧边栏", "点击关闭充电检测", "", 1);
                PreData.putDB(context, Constant.DETECT_KAIGUAN, false);
            } else {
                AdUtil.track("侧边栏", "点击开启充电检测", "", 1);
                PreData.putDB(context, Constant.DETECT_KAIGUAN, true);
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
        } else if (position == WIFI) {
            AdUtil.track("侧边栏", "点击进入WIFI", "", 1);
            Intent intent = new Intent(context, NetMonitor.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == RAM) {
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, RamAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == MANAGER) {
            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, ManagerActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == FILE) {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//            ((Activity) context).startActivityForResult(intent, 1);
            AdUtil.track("侧边栏", "点击进入文件管理页面", "", 1);
            PreData.putDB(context, Constant.FILE_CLEAN, true);
            Intent intent5 = new Intent(context, FileActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == POWER) {
            PreData.putDB(context, Constant.DEEP_CLEAN, true);
            AdUtil.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, PowerActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == CALL) {
            AdUtil.track("侧边栏", "点击进入骚扰拦截页面", "", 1);
            Intent intent5 = new Intent(context, CallActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == NOTIFI) {
            AdUtil.track("侧边栏", "点击进入通知栏清理页面", "", 1);
            PreData.putDB(context, Constant.NOTIFI_CLEAN, true);
            if (!Util.isNotificationListenEnabled(context) || !PreData.getDB(context, Constant.KEY_NOTIFI, true)) {
                Intent intent6 = new Intent(context, NotifiInfoActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            } else {
                Intent intent6 = new Intent(context, NotifiActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
            }
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
        } else if (position == FAMILY) {
            AdUtil.track("侧边栏", "点击进入family页面", "", 1);
            ShopMaster.launch(context, "EOS_Family",
                    new Theme(R.raw.battery_0, context.getPackageName()));
        } else if (position == THEME) {
            AdUtil.track("侧边栏", "点击进入主题页面", "", 1);
            ShopMaster.launch(context,
                    new Theme(R.raw.battery_0, context.getPackageName()));
        } else if (position == SETTING) {
            AdUtil.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SettingActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            AdUtil.track("侧边栏", "点击好评", "", 1);
            UtilGp.rate(context);
        }
//        else if (position == LAG_SETTING) {
//            AdUtil.track("侧边栏", "点击多语言", "", 1);
//            Intent intent9 = new Intent(context, LanguageSettingActivity.class);
//            ((Activity) context).startActivityForResult(intent9, Constant.LANGUAGE_RESUIL);
//        }
        /* else if (position == PRIVARY) {
            AdUtil.track("侧边栏", "点击隐私清理", "", 1);
            Intent intent9 = new Intent(context, PrivacyActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        }*/
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
        LottieAnimationView lot_family;
        TextView tv_name;
        View side_divide;
    }

}
