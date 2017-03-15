package com.supers.clean.junk.View.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.client.AndroidSdk;
import com.eos.module.charge.saver.Util.Constants;
import com.eos.module.charge.saver.Util.Utils;
import com.supers.clean.junk.R;
import com.supers.clean.junk.activity.FloatService;
import com.supers.clean.junk.activity.JunkActivity;
import com.supers.clean.junk.activity.ManagerActivity;
import com.supers.clean.junk.activity.RamAvtivity;
import com.supers.clean.junk.activity.SettingActivity;
import com.supers.clean.junk.activity.ThemeActivity;
import com.supers.clean.junk.modle.PreData;
import com.supers.clean.junk.modle.UtilGp;
import com.supers.clean.junk.modle.entity.Contents;
import com.supers.clean.junk.modle.entity.JunkInfo;


public class SideAdapter extends MybaseAdapter<JunkInfo> {

    public SideAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_side_item, null);
            holder = new ViewHolder();
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
        holder.tv_name.setText(info.textrid);
        holder.iv_le.setImageResource(info.drawableRid);
        if (info.isChecked) {
            holder.checkBox.setImageResource(R.mipmap.side_check_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.side_check_normal);
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
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        holder.iv_le.setColorFilter(0xff2475ff);
                        holder.tv_name.setTextColor(0xff2475ff);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        holder.iv_le.setColorFilter(0);
                        holder.tv_name.setTextColor(0xff5d6780);
                        break;
                }
                return false;
            }
        });
        if (position == 0 || position == 1) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        if (position == 2 || position == 5) {
            holder.side_divide.setVisibility(View.VISIBLE);
        } else {
            holder.side_divide.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void onC(int position) {
        switch (position) {
            case 0:
                if ((boolean) Utils.readData(context, Constants.CHARGE_SAVER_SWITCH, true)) {
                    AndroidSdk.track("侧边栏", "点击关闭充电屏保", "", 1);
                    Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, false);
                } else {
                    AndroidSdk.track("侧边栏", "点击开启充电屏保", "", 1);
                    Utils.writeData(context, Constants.CHARGE_SAVER_SWITCH, true);
                }
                break;
            case 1:
                if (PreData.getDB(context, Contents.FlOAT_SWITCH, true)) {
                    AndroidSdk.track("侧边栏", "点击关闭悬浮窗", "", 1);
                    PreData.putDB(context, Contents.FlOAT_SWITCH, false);
                    Intent intent1 = new Intent(context, FloatService.class);
                    context.stopService(intent1);
                } else {
                    AndroidSdk.track("侧边栏", "点击开启悬浮窗", "", 1);
                    PreData.putDB(context, Contents.FlOAT_SWITCH, true);
                    Intent intent1 = new Intent(context, FloatService.class);
                    context.startService(intent1);
                }
                break;
            case 2:
                AndroidSdk.track("侧边栏", "点击进入垃圾页面", "", 1);
                Intent intent2 = new Intent(context, JunkActivity.class);
                ((Activity) context).startActivityForResult(intent2, 1);
                break;
            case 3:
                AndroidSdk.track("侧边栏", "点击进入ram页面", "", 1);
                Intent intent3 = new Intent(context, RamAvtivity.class);
                ((Activity) context).startActivityForResult(intent3, 1);
                break;
            case 4:
                AndroidSdk.track("侧边栏", "点击进入应用管理页面", "", 1);
                Intent intent4 = new Intent(context, ManagerActivity.class);
                ((Activity) context).startActivityForResult(intent4, 1);
                break;
            case 5:
                AndroidSdk.track("侧边栏", "点击进入主题页面", "", 1);
                Intent intent5 = new Intent(context, ThemeActivity.class);
                ((Activity) context).startActivityForResult(intent5, 1);
                break;
            case 6:
                AndroidSdk.track("侧边栏", "点击进入设置页面", "", 1);
                Intent intent6 = new Intent(context, SettingActivity.class);
                ((Activity) context).startActivityForResult(intent6, 1);
                break;
            case 7:
                AndroidSdk.track("侧边栏", "点击好评", "", 1);
                UtilGp.rate(context);
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
        TextView tv_name;
        View side_divide;
    }

}
