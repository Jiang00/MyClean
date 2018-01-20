package com.eifmobi.clean.junk.myAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eifmobi.clean.junk.R;
import com.eifmobi.clean.junk.entity.SideInfo;
import com.eifmobi.clean.junk.myActivity.JunkActivity;
import com.eifmobi.clean.junk.myActivity.JiangwenActivity;
import com.eifmobi.clean.junk.myActivity.RamAvtivity;
import com.eifmobi.clean.junk.myActivity.SettingActivity;
import com.eifmobi.clean.junk.myActivity.UserAppActivity;
import com.eifmobi.clean.junk.util.AdUtil;
import com.eifmobi.clean.junk.util.Constant;
import com.eifmobi.clean.junk.util.UtilGp;
import com.eifmobi.clean.util.PreData;


public class SideAdapter extends MybaseAdapter<SideInfo> {

    private int JUNK = -1;
    private int RAM = -1;
    private int MANAGER = -1;
    private int BATTERY_COOLING = -1;
    private int SETTING = -1;
    private int ROTATE = -1;

    public SideAdapter(Context context) {
        super(context);
        int idx = 0;

        JUNK = idx++;
        RAM = idx++;
        MANAGER = idx++;
        BATTERY_COOLING = idx++;
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

//        hongV(holder, position);

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
         if (position == JUNK) {
            AdUtil.track("侧边栏", "点击进入垃圾页面", "", 1);
            Intent intent2 = new Intent(context, JunkActivity.class);
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
        } else if (position == SETTING) {
            AdUtil.track("侧边栏", "点击进入设置页面", "", 1);
            Intent intent9 = new Intent(context, SettingActivity.class);
            ((Activity) context).startActivityForResult(intent9, 1);
        } else if (position == ROTATE) {
            AdUtil.track("侧边栏", "点击好评", "", 1);
            UtilGp.rate(context.getApplicationContext());
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
