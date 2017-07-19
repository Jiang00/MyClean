package com.easy.junk.easycustomadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.clean.easydb.CleanDBHelper;
import com.easy.clean.easyutils.LoadManager;
import com.easy.junk.R;
import com.easy.clean.entity.JunkInfo;

/**
 * Created by  on 2017/4/17.
 */

public class NotifiSettingAdapter extends MybaseAdapter<JunkInfo> {
    public NotifiSettingAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JunkInfo info = getItem(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_white_list_item, null);
            holder = new ViewHolder();
            holder.checkBox = (ImageView) convertView
                    .findViewById(R.id.iv_check);
            holder.rl_item = (RelativeLayout) convertView
                    .findViewById(R.id.rl_item);
            holder.iv_icon = (ImageView) convertView
                    .findViewById(R.id.iv_le);
            holder.tv_lable = (TextView) convertView
                    .findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.iv_icon.setImageDrawable(LoadManager.getInstance(context).getAppIcon(info.pkg));
        holder.tv_lable.setText(info.label);
        holder.checkBox.setClickable(false);
        if (!info.isnotifiWhiteList) {
            holder.checkBox.setImageResource(R.mipmap.side_check_passed);
        } else {
            holder.checkBox.setImageResource(R.mipmap.side_check_normal);
        }
        holder.rl_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!info.isnotifiWhiteList) {
                    info.isnotifiWhiteList = true;
                    CleanDBHelper.getInstance(context).addItem(CleanDBHelper.TableType.Notification, info.pkg);
                    holder.checkBox.setImageResource(R.mipmap.side_check_normal);
                } else {
                    info.isnotifiWhiteList = false;
                    CleanDBHelper.getInstance(context).deleteItem(CleanDBHelper.TableType.Notification, info.pkg);
                    holder.checkBox.setImageResource(R.mipmap.side_check_passed);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView checkBox;
        TextView tv_lable;
        ImageView iv_icon;
        RelativeLayout rl_item;
    }
}
