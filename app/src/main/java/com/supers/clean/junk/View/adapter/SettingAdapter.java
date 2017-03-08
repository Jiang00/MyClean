package com.supers.clean.junk.View.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.FloatService;
import com.supers.clean.junk.activity.WhiteListAvtivity;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.UtilGp;
import com.supers.clean.junk.modle.entity.Contents;
import com.supers.clean.junk.modle.entity.JunkInfo;

public class SettingAdapter extends MybaseAdapter<JunkInfo> {
    public static byte idx = 0;
    public static final byte CLEAN_TONGZHI = idx++;
    public static final byte CLEAN_NOTIFI = idx++;
    public static final byte CLEAN_FLOAT = idx++;
    public static final byte CLEAN_BAI = idx++;
    public static final byte CLEAN_BATTERY = idx++;
    public static final byte CLEAN_ROTATE = idx++;

    public SettingAdapter(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_setting_item, null);
            holder = new ViewHolder();
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.iv_check);
            holder.iv_le = (ImageView) convertView
                    .findViewById(R.id.iv_le);
            holder.tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            holder.iv_ro = (ImageView) convertView
                    .findViewById(R.id.iv_ro);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.isChecked = !info.isChecked;
                if (info.isChecked) {
                    holder.checkBox.setImageResource(R.mipmap.side_check_passed);
                } else {
                    holder.checkBox.setImageResource(R.mipmap.side_check_normal);
                }
                onC(position);
            }
        });
        if (info.isChecked) {
            holder.checkBox.setImageResource(R.mipmap.side_check_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.side_check_normal);
        }

        holder.tv_name.setText(info.textrid);
        holder.iv_le.setImageResource(info.drawableRid);

        if (position == CLEAN_ROTATE) {
            holder.iv_ro.setVisibility(View.VISIBLE);
        } else {
            holder.iv_ro.setVisibility(View.INVISIBLE);
        }
        if (position == CLEAN_BAI || position == CLEAN_ROTATE) {
            holder.checkBox.setVisibility(View.INVISIBLE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    private void onC(int position) {
        switch (position) {
            case 0:
                AndroidSdk.track("设置页面", "点击通知开关", "", 1);
//                if (SharedPre.getTongzhi(context)) {
//                    SharedPre.setTongzhi(context, false);
//                } else {
//                    SharedPre.setTongzhi(context, true);
//                }
                break;
            case 1:
                AndroidSdk.track("设置页面", "点击通知栏开关", "", 1);
//                if (SharedPre.getNotifi(context)) {
//                    SharedPre.setNotifi(context, false);
//                    Intent intent = new Intent(context, NotifactionShow.class);
//                    context.stopService(intent);
//                } else {
//                    SharedPre.setNotifi(context, true);
//                    Intent intent = new Intent(context, NotifactionShow.class);
//                    intent.setAction("notification");
//                    context.startService(intent);
//                }
                break;
            case 2:
                AndroidSdk.track("设置页面", "点击悬浮球开关", "", 1);
                if (PreData.getDB(context, Contents.FlOAT_SWITCH, true)) {
                    PreData.putDB(context, Contents.FlOAT_SWITCH, false);
                    Intent intent1 = new Intent(context, FloatService.class);
                    context.stopService(intent1);
                } else {
                    PreData.putDB(context, Contents.FlOAT_SWITCH, true);
                    Intent intent1 = new Intent(context, FloatService.class);
                    context.startService(intent1);
                }
                break;
            case 3:
                AndroidSdk.track("设置页面", "进入白名单", "", 1);
                Intent intent = new Intent(context, WhiteListAvtivity.class);
                context.startActivity(intent);
                break;
            case 4:
                AndroidSdk.track("设置页面", "点击充电屏保开关", "", 1);
                //chongdian
//                if ((boolean) Utils.readData(context, Constants.CHARGE_SAVER_SWITCH, true)) {
//                    Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, false);
//                } else {
//                    Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, true);
//                }
                break;
            case 5:
                AndroidSdk.track("设置页面", "好评", "", 1);
                UtilGp.openPlayStore(context, context.getPackageName());
                break;
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
        ImageView iv_le;
        ImageView iv_ro;
        TextView tv_name;
    }

}
