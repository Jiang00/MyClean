package com.upupup.clean.junk.myAdapter;

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

import com.upupup.clean.junk.R;
import com.upupup.clean.junk.entity.SideInfo;
import com.upupup.clean.junk.myActivity.CleanActivity;
import com.upupup.clean.junk.myActivity.JiangwenActivity;
import com.upupup.clean.junk.myActivity.PowerActivity;
import com.upupup.clean.junk.myActivity.RamAvtivity;
import com.upupup.clean.junk.myActivity.SettingActivity;
import com.upupup.clean.junk.myActivity.SimilarActivity;
import com.upupup.clean.junk.myActivity.UserAppActivity;
import com.upupup.clean.junk.util.AdUtil;
import com.upupup.clean.junk.util.Constant;
import com.upupup.clean.junk.util.UtilGp;
import com.upupup.clean.util.PreData;


public class SideAdapter extends MybaseAdapter<SideInfo> {

    private int JUNK = -1;
    private int RAM = -1;
    private int BATTERY_COOLING = -1;
    private int MANAGER = -1;
    private int POWER = -1;
    private int PICTURE = -1;
    private int SETTING = -1;
    private int ROTATE = -1;

    public SideAdapter(Context context) {
        super(context);
        int idx = 0;

        JUNK = idx++;
        RAM = idx++;
        BATTERY_COOLING = idx++;
        MANAGER = idx++;
        POWER = idx++;
        PICTURE = idx++;
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


        return convertView;
    }


    private void onC(int position) {
        if (position == JUNK) {
            AdUtil.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, CleanActivity.class);
            ((Activity) context).startActivityForResult(intent2, 1);
        } else if (position == RAM) {
            AdUtil.track("侧边栏", "点击进入ram页面", "", 1);
            Intent intent3 = new Intent(context, RamAvtivity.class);
            ((Activity) context).startActivityForResult(intent3, 1);
        } else if (position == BATTERY_COOLING) {

            AdUtil.track("侧边栏", "点击进入电池降温", "", 1);
            Intent intent = new Intent(context, JiangwenActivity.class);
            ((Activity) context).startActivityForResult(intent, 1);
        } else if (position == MANAGER) {

            AdUtil.track("侧边栏", "点击进入应用管理页面", "", 1);
            Intent intent4 = new Intent(context, UserAppActivity.class);
            ((Activity) context).startActivityForResult(intent4, 1);
        } else if (position == POWER) {
            PreData.putDB(context, Constant.DEEP_CLEAN, true);
            AdUtil.track("侧边栏", "点击进入深度清理页面", "", 1);
            Intent intent5 = new Intent(context, PowerActivity.class);
            ((Activity) context).startActivityForResult(intent5, 1);
        } else if (position == PICTURE) {

            AdUtil.track("侧边栏", "点击进入相似图片", "", 1);
            PreData.putDB(context, Constant.PHOTO_CLEAN, true);
            Intent intent = new Intent(context, SimilarActivity.class);
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
        ImageView iv_le;
        TextView tv_name;
        View side_divide;
    }

}
